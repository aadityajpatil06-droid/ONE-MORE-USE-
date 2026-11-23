package com.onemoreuse.service;

import com.onemoreuse.dto.IdeaDto;
import com.onemoreuse.dto.ReuseSuggestionDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service providing rule-based object analysis and reuse suggestions.
 *
 * College Project Explanation:
 * For the college demonstration, this service implements an honest, rule-based
 * keyword matching engine that parses the object's name and description.
 * It detects the material, category, condition, and generates 5 actionable reuse ideas.
 * This decoupled service structure can easily be wired to an OpenAI / Gemini / Vision API
 * later without changing the frontend or controller.
 */
@Service
public class ReuseIdeaService {

    public ReuseSuggestionDto analyze(String input, String conditionInput) {
        String text = (input != null ? input.toLowerCase().trim() : "");
        String condition = (conditionInput != null && !conditionInput.isBlank()) ? conditionInput : "Good — minor wear";

        if (text.contains("box") || text.contains("crate") || text.contains("wood")) {
            return buildWoodenBoxSuggestions(condition);
        } else if (text.contains("chair") || text.contains("stool") || text.contains("bench")) {
            return buildChairSuggestions(condition);
        } else if (text.contains("lamp") || text.contains("light") || text.contains("bulb")) {
            return buildLampSuggestions(condition);
        } else if (text.contains("shelf") || text.contains("bookshelf") || text.contains("rack")) {
            return buildBookshelfSuggestions(condition);
        } else if (text.contains("cycle") || text.contains("bicycle") || text.contains("bike")) {
            return buildCycleSuggestions(condition);
        } else if (text.contains("jar") || text.contains("glass") || text.contains("bottle")) {
            return buildJarSuggestions(condition);
        } else if (text.contains("cloth") || text.contains("shirt") || text.contains("jeans") || text.contains("fabric")) {
            return buildFabricSuggestions(condition);
        } else if (text.contains("mixer") || text.contains("blender") || text.contains("appliance")) {
            return buildApplianceSuggestions(condition);
        } else {
            return buildGenericSuggestions(input, condition);
        }
    }

    private ReuseSuggestionDto buildWoodenBoxSuggestions(String condition) {
        List<IdeaDto> ideas = new ArrayList<>();
        ideas.add(new IdeaDto("Storage Box", "Use it to organise stationery, tools, books or winter clothes inside a cupboard.", "📦"));
        ideas.add(new IdeaDto("Plant Holder", "Line it with plastic sheeting and turn it into a rustic planter for herbs on your balcony.", "🪴"));
        ideas.add(new IdeaDto("Mini Bookshelf", "Mount it sideways on a wall to display paperbacks, photos or indoor succulents.", "🗄️"));
        ideas.add(new IdeaDto("Decorative Furniture", "Sand and repaint or polish it to serve as a rustic side table or footstool.", "🛋️"));
        ideas.add(new IdeaDto("Craft Organiser", "Add cardboard dividers to neatly store art supplies, cables or sewing kits.", "🎨"));

        return new ReuseSuggestionDto(
                "Old Wooden Box",
                "Furniture / Storage",
                "Solid wood, natural finish",
                condition,
                94,
                "🪵",
                ideas
        );
    }

    private ReuseSuggestionDto buildChairSuggestions(String condition) {
        List<IdeaDto> ideas = new ArrayList<>();
        ideas.add(new IdeaDto("Plant Stand", "Place potted indoor plants on the seat for a tiered botanical corner in your living room.", "🪴"));
        ideas.add(new IdeaDto("Bedside Table", "Repaint it and place a lamp, alarm clock, and books on top as a rustic bedside stand.", "🛏️"));
        ideas.add(new IdeaDto("Towel Rack", "Remove the seat and mount the chair back to your bathroom wall to hang bath towels.", "🧖"));
        ideas.add(new IdeaDto("Balcony Reading Corner", "Add a colorful cushion and throw blanket for a cosy morning coffee spot.", "☕"));
        ideas.add(new IdeaDto("Kids Study Seat", "Sand down any rough edges and paint with chalkboard paint for a creative study chair.", "🎨"));

        return new ReuseSuggestionDto(
                "Study Chair",
                "Furniture",
                "Cane / Wood frame",
                condition,
                92,
                "🪑",
                ideas
        );
    }

    private ReuseSuggestionDto buildLampSuggestions(String condition) {
        List<IdeaDto> ideas = new ArrayList<>();
        ideas.add(new IdeaDto("Smart Ambient Light", "Fit a warm smart LED bulb to use for ambient evening study or meditation.", "💡"));
        ideas.add(new IdeaDto("Succulent Terrarium Base", "Remove broken wiring and repurpose a glass or wide base as an open terrarium.", "🌵"));
        ideas.add(new IdeaDto("Bedside Reading Lamp", "Clean the lampshade and use beside your bed for low-strain night reading.", "📖"));
        ideas.add(new IdeaDto("Upcycled Photo Display", "Attach mini wooden clips around the shade rim to hold family polaroids.", "📷"));
        ideas.add(new IdeaDto("Desk Task Light", "Repurpose for a dedicated hobby, sewing, or soldering desk.", "🛠️"));

        return new ReuseSuggestionDto(
                "Table Lamp",
                "Electronics / Lighting",
                "Ceramic/Metal base with fabric shade",
                condition,
                89,
                "💡",
                ideas
        );
    }

    private ReuseSuggestionDto buildBookshelfSuggestions(String condition) {
        List<IdeaDto> ideas = new ArrayList<>();
        ideas.add(new IdeaDto("Shoe Rack", "Place near your entryway to store 6-8 pairs of everyday footwear neatly.", "👟"));
        ideas.add(new IdeaDto("Potted Herb Tier", "Place on a balcony or kitchen windowsill for fresh mint, basil, and coriander pots.", "🌿"));
        ideas.add(new IdeaDto("Kitchen Spice Organizer", "Store dry ingredient jars, condiments, and oils within arm's reach.", "🧂"));
        ideas.add(new IdeaDto("Toy Storage Caddy", "Use fabric bins on each shelf to organize board games and toys.", "🧸"));
        ideas.add(new IdeaDto("Mini Home Bar", "Store glassware, coasters, and tea collection neatly in a corner.", "🍵"));

        return new ReuseSuggestionDto(
                "2-Tier Bookshelf",
                "Furniture / Storage",
                "Engineered wood / Plywood",
                condition,
                95,
                "📚",
                ideas
        );
    }

    private ReuseSuggestionDto buildCycleSuggestions(String condition) {
        List<IdeaDto> ideas = new ArrayList<>();
        ideas.add(new IdeaDto("Community Sharing", "Pass it on to a neighbor with younger kids learning how to cycle.", "🤝"));
        ideas.add(new IdeaDto("Garden Planter Stand", "Fix flower baskets to the front handlebars and rear rack for a garden centerpiece.", "🌸"));
        ideas.add(new IdeaDto("Refurbish & Exchange", "Oil the chain, tune the brakes, and exchange it for a bicycle size that fits you now.", "🚲"));
        ideas.add(new IdeaDto("Wall Hanging Décor", "Mount the stripped frame on a garage or cafe wall as vintage industrial art.", "🎨"));
        ideas.add(new IdeaDto("Spare Parts Donation", "Salvage the bell, pedals, seat, and reflector for other community repairs.", "🔧"));

        return new ReuseSuggestionDto(
                "Bicycle / Kids' Cycle",
                "Sports & Mobility",
                "Steel frame with rubber tires",
                condition,
                91,
                "🚲",
                ideas
        );
    }

    private ReuseSuggestionDto buildJarSuggestions(String condition) {
        List<IdeaDto> ideas = new ArrayList<>();
        ideas.add(new IdeaDto("Kitchen Pantry Storage", "Store pulses, lentils, whole spices, or tea leaves airtight.", "🫙"));
        ideas.add(new IdeaDto("Fairy Light Lantern", "Place a string of warm fairy lights inside for evening dining lighting.", "✨"));
        ideas.add(new IdeaDto("Money Saving Bank", "Cut a coin slot in the lid to save coins or small emergency cash.", "🪙"));
        ideas.add(new IdeaDto("Bathroom Q-tip & Pad Holder", "Keep cotton swabs and bathroom essentials clean and dry on the counter.", "🧼"));
        ideas.add(new IdeaDto("Water Propagation Vase", "Fill with water to propagate pothos (money plant) cuttings on your desk.", "🌱"));

        return new ReuseSuggestionDto(
                "Glass Jar / Container",
                "Kitchen & Home",
                "Clear glass / food-grade plastic",
                condition,
                96,
                "🫙",
                ideas
        );
    }

    private ReuseSuggestionDto buildFabricSuggestions(String condition) {
        List<IdeaDto> ideas = new ArrayList<>();
        ideas.add(new IdeaDto("Reusable Grocery Tote", "Sew old denim or heavy cotton shirts into strong, washable market bags.", "🛍️"));
        ideas.add(new IdeaDto("Household Cleaning Rags", "Cut into 10x10 inch squares to replace disposable paper towels.", "🧽"));
        ideas.add(new IdeaDto("Patchwork Cushion Cover", "Combine colorful fabric swatches into an upcycled bohemian pillow cover.", "🛋️"));
        ideas.add(new IdeaDto("Plant Pot Cozy", "Wrap and knot around plain plastic nursery pots for decorative warmth.", "🪴"));
        ideas.add(new IdeaDto("Fabric Gift Wrap (Furoshiki)", "Use the Japanese cloth wrapping technique for sustainable gift giving.", "🎁"));

        return new ReuseSuggestionDto(
                "Textiles & Clothing",
                "Fabric / Apparel",
                "Cotton / Denim / Linen",
                condition,
                90,
                "👕",
                ideas
        );
    }

    private ReuseSuggestionDto buildApplianceSuggestions(String condition) {
        List<IdeaDto> ideas = new ArrayList<>();
        ideas.add(new IdeaDto("Community Exchange", "Exchange with a neighbor who has the matching mixer motor base.", "🔄"));
        ideas.add(new IdeaDto("Pencil & Pen Holder", "Clean thoroughly and use the sturdy container on a workbench for brushes or pens.", "✏️"));
        ideas.add(new IdeaDto("Dry Seed Storage", "Store gardening seeds and plant markers dry and safe from moisture.", "🌱"));
        ideas.add(new IdeaDto("DIY Tool Caddy", "Store screwdrivers, Allen keys, and small repair tools safely.", "🔨"));
        ideas.add(new IdeaDto("Refurbish with New Rubber Gasket", "Replace the 20-rupee silicone ring to restore full sealing performance.", "⚙️"));

        return new ReuseSuggestionDto(
                "Kitchen Appliance / Jar Set",
                "Kitchen Appliances",
                "Stainless steel / polycarbonate",
                condition,
                88,
                "🫙",
                ideas
        );
    }

    private ReuseSuggestionDto buildGenericSuggestions(String rawInput, String condition) {
        String title = (rawInput != null && !rawInput.isBlank()) ? rawInput.trim() : "Reusable Item";
        List<IdeaDto> ideas = new ArrayList<>();
        ideas.add(new IdeaDto("Organize & Store", "Repurpose this item to declutter drawers, closet shelves, or your garage.", "📦"));
        ideas.add(new IdeaDto("Upcycle & Repaint", "Add a fresh coat of acrylic paint, varnish, or stickers to give it a second life.", "🎨"));
        ideas.add(new IdeaDto("Balcony / Garden Use", "Find an outdoor application as a planter, birdfeeder, or garden accent.", "🪴"));
        ideas.add(new IdeaDto("Community Exchange", "List on One More Use to swap with someone in your neighbourhood who needs it.", "🤝"));
        ideas.add(new IdeaDto("Material Recovery", "Disassemble usable parts, screws, or materials for future DIY home repairs.", "🔧"));

        return new ReuseSuggestionDto(
                title,
                "General Household",
                "Mixed reusable materials",
                condition,
                85,
                "♻️",
                ideas
        );
    }
}
