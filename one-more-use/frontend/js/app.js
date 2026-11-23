/**
 * ========================================================
 * One More Use — Frontend Application Controller
 * Technology: Vanilla JavaScript (ES6+), Fetch API
 * Architecture: Clean Client communicating with Spring Boot REST APIs
 * ========================================================
 */

// Base URL for the Spring Boot backend
const API_BASE_URL = 'http://localhost:8080/api';

// Current session state
let currentUser = null;
let currentSelectedFile = null;
let currentAnalysisResult = null;
let currentItemDetails = null;
let allItemsCache = [];

/* ========================================================
   1. Application Initialization & Auth State
   ======================================================== */
document.addEventListener('DOMContentLoaded', () => {
  // Check if user session already exists in localStorage
  const savedUser = localStorage.getItem('omu_user');
  if (savedUser) {
    try {
      currentUser = JSON.parse(savedUser);
      updateSidebarUserInfo();
      showAppShell();
      loadHomeDashboard();
    } catch (e) {
      localStorage.removeItem('omu_user');
      showLoginView();
    }
  } else {
    showLoginView();
  }
});

function showLoginView() {
  document.getElementById('view-login').classList.remove('hidden');
  document.getElementById('app').classList.add('hidden');
}

function showAppShell() {
  document.getElementById('view-login').classList.add('hidden');
  document.getElementById('app').classList.remove('hidden');
}

function updateSidebarUserInfo() {
  if (!currentUser) return;
  const initials = getInitials(currentUser.name);
  document.getElementById('sidebar-avatar').textContent = initials;
  document.getElementById('sidebar-name').textContent = currentUser.name;
  document.getElementById('sidebar-loc').textContent = currentUser.location || 'Pune';

  // Also update profile view if present
  const profAvatar = document.getElementById('profile-avatar');
  const profName = document.getElementById('profile-name');
  const profSub = document.getElementById('profile-sub');
  if (profAvatar) profAvatar.textContent = initials;
  if (profName) profName.textContent = currentUser.name;
  if (profSub) profSub.textContent = `${currentUser.location || 'Pune'} · Member since 2025`;

  const homeGreeting = document.getElementById('home-greeting');
  if (homeGreeting) {
    const firstName = currentUser.name.split(' ')[0];
    homeGreeting.textContent = `Give it one more use, ${firstName}.`;
  }
}

function getInitials(name) {
  if (!name) return 'U';
  const parts = name.trim().split(' ');
  if (parts.length === 1) return parts[0].substring(0, 2).toUpperCase();
  return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
}

/* ========================================================
   2. Screen Routing (Single Page Navigation)
   ======================================================== */
const screens = ['home', 'upload', 'analysis', 'suggestions', 'nearby', 'detail', 'give', 'requests', 'profile'];

function showScreen(name) {
  screens.forEach(s => {
    const el = document.getElementById('screen-' + s);
    if (el) el.classList.toggle('hidden', s !== name);
  });

  document.querySelectorAll('.nav-item').forEach(b => {
    b.classList.toggle('active', b.dataset.screen === name);
  });

  window.scrollTo(0, 0);

  // Trigger data refreshes on specific screen activations
  if (name === 'home') {
    loadHomeDashboard();
  } else if (name === 'nearby') {
    loadBrowseItems();
  } else if (name === 'requests') {
    loadRequests();
  } else if (name === 'profile') {
    loadProfile();
  }
}

/* ========================================================
   3. Authentication (Sign In & Registration)
   ======================================================== */
function setAuthTab(tab) {
  const isSignIn = (tab === 'signin');
  document.getElementById('tab-signin').classList.toggle('active', isSignIn);
  document.getElementById('tab-register').classList.toggle('active', !isSignIn);
  document.getElementById('auth-register-fields').classList.toggle('hidden', isSignIn);
  document.getElementById('auth-btn-label').textContent = isSignIn ? 'Sign In' : 'Create Account';
  document.getElementById('auth-switch-text').innerHTML = isSignIn
    ? 'New here? <button type="button" onclick="setAuthTab(\'register\')">Create an account</button>'
    : 'Already have an account? <button type="button" onclick="setAuthTab(\'signin\')">Sign in</button>';
  clearAuthMessage();
}

function showAuthMessage(msg, isError = true) {
  const el = document.getElementById('auth-message');
  el.textContent = msg;
  el.className = 'auth-msg ' + (isError ? 'error' : 'success');
}

function clearAuthMessage() {
  const el = document.getElementById('auth-message');
  el.textContent = '';
  el.className = 'auth-msg';
}

async function handleAuthSubmit() {
  clearAuthMessage();
  const isSignIn = document.getElementById('tab-signin').classList.contains('active');
  const email = document.getElementById('auth-email').value.trim();
  const password = document.getElementById('auth-password').value;

  if (!email || !password) {
    showAuthMessage('Please enter both email and password.');
    return;
  }

  const btn = document.getElementById('btn-auth-submit');
  btn.disabled = true;

  try {
    if (isSignIn) {
      // Live call to Spring Boot: POST /api/auth/login
      const res = await fetch(`${API_BASE_URL}/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
      });

      const data = await res.json();
      if (!res.ok || !data.success) {
        showAuthMessage(data.message || 'Invalid email or password.');
        btn.disabled = false;
        return;
      }

      // Store authenticated user in localStorage
      currentUser = {
        id: data.id,
        name: data.name,
        email: data.email,
        location: data.location,
        token: data.token
      };
      localStorage.setItem('omu_user', JSON.stringify(currentUser));
      updateSidebarUserInfo();
      showAppShell();
      showScreen('home');
      showToast('Welcome back, ' + currentUser.name + '!', 'success');
    } else {
      // Live call to Spring Boot: POST /api/auth/register
      const name = document.getElementById('reg-name').value.trim();
      const location = document.getElementById('reg-location').value;

      if (!name) {
        showAuthMessage('Please enter your full name.');
        btn.disabled = false;
        return;
      }
      if (password.length < 6) {
        showAuthMessage('Password must be at least 6 characters.');
        btn.disabled = false;
        return;
      }

      const res = await fetch(`${API_BASE_URL}/auth/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, email, password, location })
      });

      const data = await res.json();
      if (!res.ok || !data.success) {
        showAuthMessage(data.message || 'Registration failed. Email may already be in use.');
        btn.disabled = false;
        return;
      }

      currentUser = {
        id: data.id,
        name: data.name,
        email: data.email,
        location: data.location,
        token: data.token
      };
      localStorage.setItem('omu_user', JSON.stringify(currentUser));
      updateSidebarUserInfo();
      showAppShell();
      showScreen('home');
      showToast('Account created successfully!', 'success');
    }
  } catch (err) {
    showAuthMessage('Cannot connect to backend server. Make sure Spring Boot is running on port 8080.');
  } finally {
    btn.disabled = false;
  }
}

function handleLogout() {
  localStorage.removeItem('omu_user');
  currentUser = null;
  showLoginView();
  showToast('You have signed out.', 'info');
}

/* ========================================================
   4. Home Dashboard (Load Recommended & Recent Items)
   ======================================================== */
async function loadHomeDashboard() {
  const recContainer = document.getElementById('home-recommended-items');
  const recentContainer = document.getElementById('home-recent-items');
  if (!recContainer || !recentContainer) return;

  recContainer.innerHTML = '<div class="empty-state">Loading recommended items from database…</div>';
  recentContainer.innerHTML = '<div class="empty-state">Loading recent items…</div>';

  try {
    const res = await fetch(`${API_BASE_URL}/items`);
    if (!res.ok) throw new Error('Failed to load items');
    const items = await res.json();
    allItemsCache = items;

    if (!items || items.length === 0) {
      recContainer.innerHTML = '<div class="empty-state">No items listed yet. Be the first to upload an object!</div>';
      recentContainer.innerHTML = '<div class="empty-state">No recent items.</div>';
      return;
    }

    // Split items for Recommended and Recent demonstration
    const recommended = items.slice(0, 3);
    const recent = items.slice(3, 6).length > 0 ? items.slice(3, 6) : items.slice(0, 3);

    recContainer.innerHTML = recommended.map(item => renderItemCardHtml(item)).join('');
    recentContainer.innerHTML = recent.map(item => renderItemCardHtml(item)).join('');
  } catch (err) {
    recContainer.innerHTML = '<div class="empty-state">Unable to connect to database API. Make sure Spring Boot is running.</div>';
    recentContainer.innerHTML = '';
  }
}

function renderItemCardHtml(item) {
  const chipClass = item.exchangeType === 'EXCHANGE' ? 'chip-exchange' : 'chip-free';
  const chipLabel = item.exchangeType === 'EXCHANGE' ? 'Exchange' : 'Free';
  const ownerName = item.ownerName || 'Community Member';
  const location = item.location || 'Pune';

  let thumbContent = item.emoji || '📦';
  if (item.imageUrl) {
    thumbContent = `<img src="http://localhost:8080${item.imageUrl}" alt="${escapeHtml(item.title)}">`;
  }

  return `
    <div class="card item-card" onclick="openItemDetails(${item.id})">
      <div class="item-thumb">${thumbContent}</div>
      <div class="item-body">
        <h4>${escapeHtml(item.title)}</h4>
        <div class="item-meta">${escapeHtml(ownerName)} · ${escapeHtml(location)}</div>
        <span class="chip ${chipClass}">${chipLabel}</span>
      </div>
    </div>
  `;
}

function triggerHomeSearch() {
  const input = document.getElementById('home-search-input');
  const query = input ? input.value.trim() : '';
  showScreen('nearby');
  const nearbyInput = document.getElementById('nearby-search-input');
  if (nearbyInput) {
    nearbyInput.value = query;
    loadBrowseItems(query);
  }
}

/* ========================================================
   5. Browse & Nearby Screen
   ======================================================== */
let browseDebounceTimer = null;
function debounceBrowseSearch() {
  clearTimeout(browseDebounceTimer);
  browseDebounceTimer = setTimeout(() => {
    const query = document.getElementById('nearby-search-input').value.trim();
    loadBrowseItems(query);
  }, 300);
}

async function loadBrowseItems(searchQuery = '') {
  const container = document.getElementById('nearby-items-grid');
  if (!container) return;

  container.innerHTML = '<div class="empty-state">Loading items from MySQL…</div>';

  try {
    const url = searchQuery
      ? `${API_BASE_URL}/items/search?q=${encodeURIComponent(searchQuery)}`
      : `${API_BASE_URL}/items`;

    const res = await fetch(url);
    if (!res.ok) throw new Error('Failed to fetch items');
    const items = await res.json();

    if (!items || items.length === 0) {
      container.innerHTML = `<div class="empty-state">No items found matching "${escapeHtml(searchQuery)}".</div>`;
      return;
    }

    container.innerHTML = items.map(item => renderItemCardHtml(item)).join('');
  } catch (err) {
    container.innerHTML = '<div class="empty-state">Unable to load items from server.</div>';
  }
}

/* ========================================================
   6. Upload & AI Object Analysis
   ======================================================== */
function handleFileSelect(event) {
  const file = event.target.files[0];
  if (!file) return;

  // Validate file type
  if (!file.type.startsWith('image/')) {
    showToast('Please select a valid image file (JPG, PNG, WebP).', 'error');
    return;
  }

  // Validate size (10MB limit)
  if (file.size > 10 * 1024 * 1024) {
    showToast('Image file size must be less than 10MB.', 'error');
    return;
  }

  currentSelectedFile = file;

  // Update upload preview box
  document.getElementById('upload-zone-title').textContent = 'Photo attached';
  document.getElementById('upload-zone-sub').textContent = `${file.name} · Click Analyze to continue`;
  document.getElementById('upload-preview-wrap').classList.remove('hidden');
  document.getElementById('upload-preview-name').textContent = file.name;
  document.getElementById('upload-preview-size').textContent = `${(file.size / (1024 * 1024)).toFixed(2)} MB · Ready for AI analysis`;

  // Read thumbnail image locally using FileReader
  const reader = new FileReader();
  reader.onload = (e) => {
    const thumbEl = document.getElementById('upload-preview-thumb');
    thumbEl.innerHTML = `<img src="${e.target.result}" style="width:100%;height:100%;object-fit:cover;">`;

    const analysisPhoto = document.getElementById('analysis-photo-preview');
    if (analysisPhoto) {
      analysisPhoto.innerHTML = `<img src="${e.target.result}" style="width:100%;height:100%;object-fit:cover;">`;
    }
  };
  reader.readAsDataURL(file);
}

function resetUpload(event) {
  if (event) event.stopPropagation();
  currentSelectedFile = null;
  document.getElementById('item-file-input').value = '';
  document.getElementById('upload-zone-title').textContent = 'Upload object photo';
  document.getElementById('upload-zone-sub').textContent = 'Drag and drop an image here, or click to browse · JPG, PNG up to 10MB';
  document.getElementById('upload-preview-wrap').classList.add('hidden');
  document.getElementById('upload-preview-thumb').textContent = '🪵';
}

async function startObjectAnalysis() {
  const desc = document.getElementById('upload-desc').value.trim();
  const location = document.getElementById('upload-location').value;
  const size = document.getElementById('upload-size').value;

  const btn = document.getElementById('analyze-btn');
  btn.disabled = true;

  try {
    // Call Spring Boot rule-based AI suggestion service: POST /api/reuse-ideas/analyze
    const res = await fetch(`${API_BASE_URL}/reuse-ideas/analyze`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        objectName: desc || (currentSelectedFile ? currentSelectedFile.name : 'Unknown Item'),
        condition: 'Good — minor wear'
      })
    });

    if (!res.ok) throw new Error('AI analysis failed');
    const data = await res.json();
    currentAnalysisResult = data;
    currentAnalysisResult.location = location;
    currentAnalysisResult.size = size;
    currentAnalysisResult.userDescription = desc;

    // Populate Analysis Screen
    document.getElementById('res-detected-object').textContent = data.detectedObject;
    document.getElementById('res-category').textContent = data.category;
    document.getElementById('res-material').textContent = data.material;
    document.getElementById('res-condition').textContent = data.condition;
    document.getElementById('res-confidence').textContent = `${data.confidenceScore}%`;
    document.getElementById('res-confidence-bar').style.width = `${data.confidenceScore}%`;

    const caption = document.getElementById('analysis-photo-caption');
    if (caption) {
      caption.textContent = currentSelectedFile ? currentSelectedFile.name : `${data.detectedObject}.jpg`;
    }

    // Populate Suggestions Screen with 5 generated ideas
    renderReuseSuggestions(data);

    showScreen('analysis');
  } catch (err) {
    showToast('Failed to perform analysis. Ensure backend is running.', 'error');
  } finally {
    btn.disabled = false;
  }
}

function renderReuseSuggestions(analysis) {
  const container = document.getElementById('ideas-container');
  const subtitle = document.getElementById('suggestions-subtitle');
  if (subtitle) {
    subtitle.textContent = `Based on the ${analysis.detectedObject}, here are 5 practical ways to reuse your item.`;
  }

  if (!container || !analysis.ideas) return;

  container.innerHTML = analysis.ideas.map((idea, idx) => `
    <div class="card idea-card">
      <div class="idea-icon">${idea.icon || '♻️'}</div>
      <h3>${escapeHtml(idea.title)}</h3>
      <p>${escapeHtml(idea.description)}</p>
      <button class="btn btn-secondary" type="button" onclick="pickReuseIdea('${escapeHtml(idea.title)}')">Use This Idea</button>
    </div>
  `).join('');
}

function pickReuseIdea(ideaTitle) {
  showToast(`Great choice! "${ideaTitle}" has been saved to your reuse projects.`, 'success');
}

function prepareGiveFormFromAnalysis() {
  if (currentAnalysisResult) {
    document.getElementById('post-title').value = currentAnalysisResult.detectedObject || '';
    document.getElementById('post-desc').value = currentAnalysisResult.userDescription || `${currentAnalysisResult.detectedObject} in ${currentAnalysisResult.condition} condition.`;
    document.getElementById('post-location').value = currentAnalysisResult.location || 'Kothrud, Pune';

    // Match category if possible
    const catSelect = document.getElementById('post-category');
    for (let i = 0; i < catSelect.options.length; i++) {
      if (catSelect.options[i].value === currentAnalysisResult.category) {
        catSelect.selectedIndex = i;
        break;
      }
    }
  }
  showScreen('give');
}

/* ========================================================
   7. Item Listing Creation (Give for Free / Exchange)
   Strictly no donation wording!
   ======================================================== */
function chooseGiveType(type) {
  document.getElementById('opt-free').classList.toggle('sel', type === 'FREE');
  document.getElementById('opt-exchange').classList.toggle('sel', type === 'EXCHANGE');
  document.getElementById('post-exchange-type').value = type;
  document.getElementById('exchange-wish-field').classList.toggle('hidden', type !== 'EXCHANGE');
}

async function handlePostItem(event) {
  event.preventDefault();
  if (!currentUser) {
    showToast('You must be signed in to post an item.', 'error');
    showLoginView();
    return;
  }

  const title = document.getElementById('post-title').value.trim();
  const category = document.getElementById('post-category').value;
  const exchangeType = document.getElementById('post-exchange-type').value;
  const condition = document.getElementById('post-condition').value;
  const location = document.getElementById('post-location').value;
  const description = document.getElementById('post-desc').value.trim();
  const exchangeWish = document.getElementById('post-exchange-wish').value.trim();

  if (!title || !description) {
    showToast('Please fill in all required fields.', 'error');
    return;
  }

  const submitBtn = document.getElementById('btn-submit-post');
  submitBtn.disabled = true;

  try {
    // Build multipart form data for image upload + item metadata
    const formData = new FormData();
    formData.append('ownerId', currentUser.id);
    formData.append('title', title);
    formData.append('description', description);
    formData.append('category', category);
    formData.append('itemCondition', condition);
    formData.append('approxSize', 'Medium (fits in a bag)');
    formData.append('location', location);
    formData.append('exchangeType', exchangeType);
    if (exchangeType === 'EXCHANGE') {
      formData.append('preferredExchangeItem', exchangeWish);
    }
    if (currentSelectedFile) {
      formData.append('image', currentSelectedFile);
    }

    const res = await fetch(`${API_BASE_URL}/items`, {
      method: 'POST',
      body: formData
    });

    if (!res.ok) {
      const err = await res.json();
      throw new Error(err.message || 'Failed to list item');
    }

    const savedItem = await res.json();
    showToast('Item posted successfully to MySQL database!', 'success');

    // Reset form and file selection
    document.getElementById('post-item-form').reset();
    resetUpload();
    currentAnalysisResult = null;

    // Refresh items and route to browse screen
    loadBrowseItems();
    showScreen('nearby');
  } catch (err) {
    showToast(err.message || 'Failed to list item. Backend may be offline.', 'error');
  } finally {
    submitBtn.disabled = false;
  }
}

/* ========================================================
   8. Item Details & Request Submission
   ======================================================== */
async function openItemDetails(itemId) {
  try {
    const res = await fetch(`${API_BASE_URL}/items/${itemId}`);
    if (!res.ok) throw new Error('Item not found');
    const item = await res.json();
    currentItemDetails = item;

    // Populate Detail View
    const thumbEl = document.getElementById('detail-thumb');
    if (item.imageUrl) {
      thumbEl.innerHTML = `<img src="http://localhost:8080${item.imageUrl}" alt="${escapeHtml(item.title)}">`;
    } else {
      thumbEl.textContent = item.emoji || '📦';
    }

    document.getElementById('detail-title').textContent = item.title;
    document.getElementById('detail-desc').textContent = item.description || 'No description provided.';
    document.getElementById('detail-category').textContent = item.category || 'General';
    document.getElementById('detail-condition').textContent = item.itemCondition || 'Good';
    document.getElementById('detail-size').textContent = item.approxSize || 'Medium';
    document.getElementById('detail-location').textContent = item.location || 'Pune';

    // Availability pills: Strictly Free or Exchange
    const pillFree = document.getElementById('pill-free');
    const pillExchange = document.getElementById('pill-exchange');
    const isExchange = (item.exchangeType === 'EXCHANGE');

    pillFree.classList.toggle('on', !isExchange);
    pillExchange.classList.toggle('on', isExchange);

    const exchangeRow = document.getElementById('detail-exchange-row');
    if (exchangeRow) {
      exchangeRow.classList.toggle('hidden', !isExchange);
      if (isExchange) {
        document.getElementById('detail-exchange-item').textContent = item.preferredExchangeItem || 'Any useful household item';
      }
    }

    // Owner card
    document.getElementById('detail-owner-initials').textContent = getInitials(item.ownerName);
    document.getElementById('detail-owner-name').textContent = item.ownerName || 'Verified Member';
    document.getElementById('detail-owner-location').textContent = `${item.ownerLocation || 'Pune'} · Community Member`;

    // Disable request button if current user owns the item
    const sendBtn = document.getElementById('btn-send-request');
    if (currentUser && item.ownerId === currentUser.id) {
      sendBtn.disabled = true;
      sendBtn.textContent = 'This is your own listing';
    } else {
      sendBtn.disabled = false;
      sendBtn.innerHTML = `
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none"><path d="M21 11.5a8.4 8.4 0 0 1-9 8.4A8.6 8.6 0 0 1 7 18l-4 1 1-3.6A8.4 8.4 0 1 1 21 11.5z" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/></svg>
        Send Request
      `;
    }

    showScreen('detail');
  } catch (err) {
    showToast('Failed to load item details.', 'error');
  }
}

async function promptAndSendRequest() {
  if (!currentUser) {
    showToast('Please sign in to send requests.', 'error');
    showLoginView();
    return;
  }
  if (!currentItemDetails) return;

  const defaultMsg = currentItemDetails.exchangeType === 'EXCHANGE'
    ? `Hi ${currentItemDetails.ownerName}, I would love to exchange for your ${currentItemDetails.title}!`
    : `Hi ${currentItemDetails.ownerName}, I am interested in obtaining your ${currentItemDetails.title}. Can we arrange pickup?`;

  const userMsg = prompt('Enter a message for the owner:', defaultMsg);
  if (userMsg === null) return; // User cancelled prompt

  try {
    const res = await fetch(`${API_BASE_URL}/requests`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        itemId: currentItemDetails.id,
        requesterId: currentUser.id,
        message: userMsg.trim() || defaultMsg
      })
    });

    const data = await res.json();
    if (!res.ok) {
      throw new Error(data.message || 'Failed to send request');
    }

    showToast('Request sent successfully!', 'success');
    showScreen('requests');
  } catch (err) {
    showToast(err.message || 'Failed to submit request.', 'error');
  }
}

/* ========================================================
   9. Requests Management (Incoming, Outgoing, Accept/Reject)
   ======================================================== */
function setReqTab(tab) {
  const isIncoming = (tab === 'incoming');
  document.getElementById('rtab-incoming').classList.toggle('active', isIncoming);
  document.getElementById('rtab-outgoing').classList.toggle('active', !isIncoming);
  document.getElementById('requests-incoming').classList.toggle('hidden', !isIncoming);
  document.getElementById('requests-outgoing').classList.toggle('hidden', isIncoming);
}

async function loadRequests() {
  if (!currentUser) return;

  const incomingContainer = document.getElementById('requests-incoming');
  const outgoingContainer = document.getElementById('requests-outgoing');

  incomingContainer.innerHTML = '<div class="empty-state">Loading incoming requests…</div>';
  outgoingContainer.innerHTML = '<div class="empty-state">Loading outgoing requests…</div>';

  try {
    // 1. Fetch incoming requests (requests on items owned by current user)
    const incRes = await fetch(`${API_BASE_URL}/requests/incoming/${currentUser.id}`);
    const incoming = await incRes.json();

    if (!incoming || incoming.length === 0) {
      incomingContainer.innerHTML = '<div class="empty-state">No incoming requests on your items yet.</div>';
    } else {
      incomingContainer.innerHTML = incoming.map(req => renderIncomingRequestRow(req)).join('');
    }

    // 2. Fetch outgoing requests (requests sent by current user)
    const outRes = await fetch(`${API_BASE_URL}/requests/outgoing/${currentUser.id}`);
    const outgoing = await outRes.json();

    if (!outgoing || outgoing.length === 0) {
      outgoingContainer.innerHTML = '<div class="empty-state">You have not sent any requests yet.</div>';
    } else {
      outgoingContainer.innerHTML = outgoing.map(req => renderOutgoingRequestRow(req)).join('');
    }
  } catch (err) {
    incomingContainer.innerHTML = '<div class="empty-state">Could not connect to requests API.</div>';
    outgoingContainer.innerHTML = '';
  }
}

function renderIncomingRequestRow(req) {
  const statusClass = req.status === 'ACCEPTED' ? 'status-accepted' : (req.status === 'REJECTED' ? 'status-rejected' : 'status-pending');

  let actionButtons = '';
  if (req.status === 'PENDING') {
    actionButtons = `
      <button class="btn btn-secondary" type="button" onclick="updateRequestStatus(${req.id}, 'REJECTED')">Reject</button>
      <button class="btn btn-primary" type="button" onclick="updateRequestStatus(${req.id}, 'ACCEPTED')">Accept</button>
    `;
  } else if (req.status === 'ACCEPTED') {
    actionButtons = `
      <button class="btn btn-secondary" type="button" onclick="focusChat('${escapeHtml(req.itemTitle)}', '${escapeHtml(req.requesterName)}')">Open Chat</button>
    `;
  }

  return `
    <div class="card req-row">
      <div class="p-avatar">${getInitials(req.requesterName)}</div>
      <div class="ri">
        <h4>${escapeHtml(req.requesterName)} wants: ${escapeHtml(req.itemTitle)}</h4>
        <div class="rmeta">${escapeHtml(req.requesterLocation || 'Pune')} · "${escapeHtml(req.message || '')}"</div>
      </div>
      <span class="status-pill ${statusClass}">${req.status}</span>
      ${actionButtons}
    </div>
  `;
}

function renderOutgoingRequestRow(req) {
  const statusClass = req.status === 'ACCEPTED' ? 'status-accepted' : (req.status === 'REJECTED' ? 'status-rejected' : 'status-pending');
  return `
    <div class="card req-row">
      <div class="p-avatar">${getInitials(req.ownerName)}</div>
      <div class="ri">
        <h4>You requested: ${escapeHtml(req.itemTitle)}</h4>
        <div class="rmeta">From ${escapeHtml(req.ownerName)} · ${escapeHtml(req.ownerLocation || 'Pune')}</div>
      </div>
      <span class="status-pill ${statusClass}">${req.status}</span>
    </div>
  `;
}

async function updateRequestStatus(requestId, newStatus) {
  if (!currentUser) return;

  try {
    const res = await fetch(`${API_BASE_URL}/requests/${requestId}/status`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        ownerId: currentUser.id,
        status: newStatus
      })
    });

    if (!res.ok) {
      const err = await res.json();
      throw new Error(err.message || 'Failed to update request');
    }

    showToast(`Request marked as ${newStatus}!`, 'success');
    loadRequests();
  } catch (err) {
    showToast(err.message || 'Could not update request status.', 'error');
  }
}

function focusChat(itemTitle, personName) {
  const headerTitle = document.getElementById('chat-header-title');
  const headerSub = document.getElementById('chat-header-sub');
  if (headerTitle) headerTitle.textContent = itemTitle;
  if (headerSub) headerSub.innerHTML = `Coordination with <strong>${personName}</strong> · <span style="color:var(--moss); font-weight:600;">Accepted</span>`;

  document.querySelector('.chat-wrap').scrollIntoView({ behavior: 'smooth', block: 'center' });
  document.getElementById('chat-input-field').focus();
}

function sendMsg() {
  const input = document.getElementById('chat-input-field');
  const val = input.value.trim();
  if (!val) return;

  const chatBody = document.getElementById('chat-body');
  const msgDiv = document.createElement('div');
  msgDiv.className = 'msg me';
  const now = new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
  msgDiv.innerHTML = `${escapeHtml(val)}<div class="msg-time">${now}</div>`;

  chatBody.appendChild(msgDiv);
  chatBody.scrollTop = chatBody.scrollHeight;
  input.value = '';
}

/* ========================================================
   10. Profile View & Live User Stats
   ======================================================== */
async function loadProfile() {
  if (!currentUser) return;
  updateSidebarUserInfo();

  try {
    // 1. Fetch live user stats from Spring Boot
    const statsRes = await fetch(`${API_BASE_URL}/users/${currentUser.id}/stats`);
    if (statsRes.ok) {
      const stats = await statsRes.json();
      document.getElementById('stat-posted').textContent = stats.itemsPosted || 0;
      document.getElementById('stat-given').textContent = stats.itemsGiven || 0;
      document.getElementById('stat-received').textContent = stats.itemsReceived || 0;
      document.getElementById('stat-active').textContent = stats.activeRequests || 0;
    }

    // 2. Fetch items posted by this user
    const itemsRes = await fetch(`${API_BASE_URL}/items/user/${currentUser.id}`);
    const grid = document.getElementById('profile-items-grid');
    if (itemsRes.ok && grid) {
      const items = await itemsRes.json();
      if (!items || items.length === 0) {
        grid.innerHTML = '<div class="empty-state">You have not listed any items yet.</div>';
      } else {
        grid.innerHTML = items.map(item => renderItemCardHtml(item)).join('');
      }
    }
  } catch (err) {
    showToast('Failed to load profile details from server.', 'error');
  }
}

/* ========================================================
   11. Utility Functions
   ======================================================== */
function showToast(message, type = 'success') {
  const container = document.getElementById('toast-container');
  if (!container) return;

  const toast = document.createElement('div');
  toast.className = `toast toast-${type}`;
  toast.textContent = message;

  container.appendChild(toast);
  setTimeout(() => {
    toast.style.opacity = '0';
    toast.style.transform = 'translateY(8px)';
    toast.style.transition = 'all 0.3s ease';
    setTimeout(() => toast.remove(), 300);
  }, 3500);
}

function escapeHtml(text) {
  if (!text) return '';
  return String(text)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#039;');
}
