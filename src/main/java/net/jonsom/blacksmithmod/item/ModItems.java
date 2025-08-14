package net.jonsom.blacksmithmod.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.jonsom.blacksmithmod.Blacksmith;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item RAW_MAGNETITE = registerItem("raw_magnetite", new Item(new Item.Settings()));
    public static final Item RAW_HEMATITE = registerItem("raw_hematite", new Item(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Blacksmith.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Blacksmith.LOGGER.info("Registering Mod Items for " + Blacksmith.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(RAW_MAGNETITE);
            entries.add(RAW_HEMATITE);
        });
    }
}
