package net.jonsom.blacksmithmod.block.entity;

import net.jonsom.blacksmithmod.Blacksmith;
import net.jonsom.blacksmithmod.block.ModBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<SteelBarBlockEntity> STEEL_BAR_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE,
                    Identifier.of(Blacksmith.MOD_ID, "steel_bar_be"),

                    BlockEntityType.Builder.create(SteelBarBlockEntity::new,
                            ModBlocks.STEEL_BAR).build(null));

    public static void registerBlockEntities() {
        Blacksmith.LOGGER.info("Registering Block Entities for " + Blacksmith.MOD_ID);
    }
}