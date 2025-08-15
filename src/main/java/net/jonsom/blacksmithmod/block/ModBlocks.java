package net.jonsom.blacksmithmod.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.jonsom.blacksmithmod.Blacksmith;
import net.jonsom.blacksmithmod.block.custom.SteelBar;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks; // IMPORTANTE: Adicione este import
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {

    // Versão moderna, sem avisos de "deprecated"
    public static final Block STEEL_BAR = registerBlock("steel_bar",
            new SteelBar(Block.Settings.copy(Blocks.ANVIL).nonOpaque()));


    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(Blacksmith.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(Blacksmith.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        Blacksmith.LOGGER.info("Registering Mod Blocks for " + Blacksmith.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.add(STEEL_BAR);
        });
    }
}