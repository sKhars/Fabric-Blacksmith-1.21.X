package net.jonsom.blacksmithmod.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.jonsom.blacksmithmod.Blacksmith;
import net.jonsom.blacksmithmod.block.custom.SteelBar;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block HEMATITE_BLOCK = registerBlock("hematite_block",
            new Block(AbstractBlock.Settings.create().strength(4f)
                    .requiresTool().sounds(BlockSoundGroup.DRIPSTONE_BLOCK)));

    public static final Block STEEL_BAR = registerBlock("steel_bar",
            new SteelBar(Block.Settings.copy(Blocks.IRON_BLOCK).nonOpaque()));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK,Identifier.of(Blacksmith.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(Blacksmith.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        Blacksmith.LOGGER.info("Registering Mod Blocks for " + Blacksmith.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries ->{
            entries.add(ModBlocks.HEMATITE_BLOCK);
        });
    }
}
