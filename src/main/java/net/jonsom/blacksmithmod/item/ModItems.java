package net.jonsom.blacksmithmod.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.jonsom.blacksmithmod.Blacksmith;
import net.jonsom.blacksmithmod.item.custom.HammerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item BLACKSMITH_HAMMER = registerItem("blacksmith_hammer", new HammerItem(new Item.Settings().maxCount(1)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Blacksmith.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Blacksmith.LOGGER.info("Registering Mod Items for " + Blacksmith.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {

        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(BLACKSMITH_HAMMER);
        });
    }
}
