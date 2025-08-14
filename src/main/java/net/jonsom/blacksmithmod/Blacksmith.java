package net.jonsom.blacksmithmod;

import net.fabricmc.api.ModInitializer;

import net.jonsom.blacksmithmod.block.ModBlocks;
import net.jonsom.blacksmithmod.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Blacksmith implements ModInitializer {
	public static final String MOD_ID = "blacksmithmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
	}
}