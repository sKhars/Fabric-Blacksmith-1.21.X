package net.jonsom.blacksmithmod;

import net.fabricmc.api.ClientModInitializer;
import net.jonsom.blacksmithmod.block.entity.ModBlockEntities;
import net.jonsom.blacksmithmod.client.renderer.SteelBarBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class BlacksmithModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // A ÚNICA MUDANÇA É ADICIONAR ESTA LINHA:
        System.out.println("BlacksmithModClient está sendo inicializado! Registrando renderer.");

        BlockEntityRendererFactories.register(ModBlockEntities.STEEL_BAR_BLOCK_ENTITY, SteelBarBlockEntityRenderer::new);
    }
}