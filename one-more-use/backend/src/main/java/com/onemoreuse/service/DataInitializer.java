package com.onemoreuse.service;

import com.onemoreuse.entity.Item;
import com.onemoreuse.entity.ItemRequest;
import com.onemoreuse.entity.User;
import com.onemoreuse.repository.ItemRepository;
import com.onemoreuse.repository.ItemRequestRepository;
import com.onemoreuse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Initializes realistic demonstration data on application startup
 * if the database is currently empty.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository,
                           ItemRepository itemRepository,
                           ItemRequestRepository itemRequestRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.itemRequestRepository = itemRequestRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            System.out.println("[DataInitializer] Database already seeded. Skipping initial data injection.");
            return;
        }

        System.out.println("[DataInitializer] Seeding demo users and items for One More Use...");

        String defaultHash = passwordEncoder.encode("password123");

        // 1. Seed Users
        User priya = userRepository.save(new User("Priya Kulkarni", "priya.kulkarni@gmail.com", defaultHash, "Kothrud, Pune"));
        User aditi = userRepository.save(new User("Aditi Deshmukh", "aditi.deshmukh@gmail.com", defaultHash, "Kothrud, Pune"));
        User rohan = userRepository.save(new User("Rohan Patil", "rohan.patil@gmail.com", defaultHash, "Baner, Pune"));
        User sneha = userRepository.save(new User("Sneha Kulkarni", "sneha.kulkarni@gmail.com", defaultHash, "Wakad, Pune"));
        User kunal = userRepository.save(new User("Kunal Joshi", "kunal.joshi@gmail.com", defaultHash, "Aundh, Pune"));
        User meera = userRepository.save(new User("Meera Joshi", "meera.joshi@gmail.com", defaultHash, "Kothrud, Pune"));

        // 2. Seed Items
        Item box = new Item();
        box.setOwner(aditi);
        box.setTitle("Old Wooden Box");
        box.setDescription("A sturdy wooden storage box, slightly worn on the edges but structurally solid. Great for storage, planters or upcycling projects.");
        box.setCategory("Furniture / Storage");
        box.setItemCondition("Good — minor wear");
        box.setApproxSize("Medium (fits in a bag)");
        box.setLocation("Kothrud, Pune");
        box.setEmoji("🪵");
        box.setExchangeType("FREE");
        box.setIsAvailable(true);
        box = itemRepository.save(box);

        Item chair = new Item();
        chair.setOwner(rohan);
        chair.setTitle("Cane Study Chair");
        chair.setDescription("A cane-weave study chair, sturdy frame with light surface wear. Ideal for a study table or reading corner.");
        chair.setCategory("Furniture");
        chair.setItemCondition("Fair — visible wear");
        chair.setApproxSize("Large (needs a vehicle)");
        chair.setLocation("Baner, Pune");
        chair.setEmoji("🪑");
        chair.setExchangeType("EXCHANGE");
        chair.setPreferredExchangeItem("Study desk or wooden stool");
        chair.setIsAvailable(true);
        chair = itemRepository.save(chair);

        Item lamp = new Item();
        lamp.setOwner(sneha);
        lamp.setTitle("Table Lamp, Working");
        lamp.setDescription("A working table lamp with a fabric shade, tested and fully functional. Great for a study desk or bedside table.");
        lamp.setCategory("Electronics / Lighting");
        lamp.setItemCondition("Like new");
        lamp.setApproxSize("Small (fits in hand)");
        lamp.setLocation("Wakad, Pune");
        lamp.setEmoji("💡");
        lamp.setExchangeType("FREE");
        lamp.setIsAvailable(true);
        itemRepository.save(lamp);

        Item bookshelf = new Item();
        bookshelf.setOwner(kunal);
        bookshelf.setTitle("2-Tier Bookshelf");
        bookshelf.setDescription("Compact 2-tier wooden bookshelf, good for a small study corner or hallway.");
        bookshelf.setCategory("Furniture / Storage");
        bookshelf.setItemCondition("Good — minor wear");
        bookshelf.setApproxSize("Large (needs a vehicle)");
        bookshelf.setLocation("Aundh, Pune");
        bookshelf.setEmoji("📚");
        bookshelf.setExchangeType("FREE");
        bookshelf.setIsAvailable(true);
        bookshelf = itemRepository.save(bookshelf);

        Item cycle = new Item();
        cycle.setOwner(meera);
        cycle.setTitle("Kids' Cycle, 16 inch");
        cycle.setDescription("16 inch kids' cycle with training wheels, well maintained and ready to ride.");
        cycle.setCategory("Toys & Sports");
        cycle.setItemCondition("Good — minor wear");
        cycle.setApproxSize("Large (needs a vehicle)");
        cycle.setLocation("Kothrud, Pune");
        cycle.setEmoji("🚲");
        cycle.setExchangeType("FREE");
        cycle.setIsAvailable(true);
        cycle = itemRepository.save(cycle);

        Item mixer = new Item();
        mixer.setOwner(aditi);
        mixer.setTitle("Mixer Jar Set");
        mixer.setDescription("A set of 3 mixer jars, compatible with most standard mixer bases. Minor scratches, fully functional.");
        mixer.setCategory("Kitchen Appliances");
        mixer.setItemCondition("Fair — visible wear");
        mixer.setApproxSize("Medium (fits in a bag)");
        mixer.setLocation("Kothrud, Pune");
        mixer.setEmoji("🫙");
        mixer.setExchangeType("EXCHANGE");
        mixer.setPreferredExchangeItem("Toaster or electric kettle");
        mixer.setIsAvailable(true);
        itemRepository.save(mixer);

        // 3. Seed Requests
        ItemRequest req1 = new ItemRequest();
        req1.setItem(box);
        req1.setRequester(rohan);
        req1.setOwner(aditi);
        req1.setMessage("Hi Aditi, I need this wooden box for my balcony gardening project!");
        req1.setStatus("PENDING");
        itemRequestRepository.save(req1);

        ItemRequest req2 = new ItemRequest();
        req2.setItem(box);
        req2.setRequester(sneha);
        req2.setOwner(aditi);
        req2.setMessage("Hi Aditi, I would love to use this box for organizing craft supplies.");
        req2.setStatus("ACCEPTED");
        itemRequestRepository.save(req2);

        ItemRequest req3 = new ItemRequest();
        req3.setItem(bookshelf);
        req3.setRequester(priya);
        req3.setOwner(kunal);
        req3.setMessage("Hi Kunal, I would like to pick up this 2-tier bookshelf for my study room.");
        req3.setStatus("PENDING");
        itemRequestRepository.save(req3);

        ItemRequest req4 = new ItemRequest();
        req4.setItem(cycle);
        req4.setRequester(priya);
        req4.setOwner(meera);
        req4.setMessage("Hi Meera, is this kids cycle still available for pickup?");
        req4.setStatus("ACCEPTED");
        itemRequestRepository.save(req4);

        System.out.println("[DataInitializer] Demonstration data seeded successfully!");
    }
}
