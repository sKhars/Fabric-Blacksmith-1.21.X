package net.jonsom.blacksmithmod.client.renderer;

import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;

// Importamos RenderPhase para acessar a constante NO_TEXTURE
import net.minecraft.client.render.RenderPhase;

import static net.minecraft.client.render.RenderLayer.*;

public class ModRenderLayers extends RenderLayer {

    // Construtor privado, conforme necessário
    private ModRenderLayers(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int bufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, bufferSize, hasCrumbling, translucent, startAction, endAction);
    }

    public static RenderLayer getSteelBarLayer() {
        return RenderLayer.of("steel_bar_solid",
                VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL,
                VertexFormat.DrawMode.QUADS,
                256,
                false,
                false,
                MultiPhaseParameters.builder()
                        .lightmap(ENABLE_LIGHTMAP)
                        .cull(DISABLE_CULLING)

                        // --- AQUI ESTÁ A CORREÇÃO CRUCIAL ---
                        // Em vez de criar uma textura falsa, dizemos explicitamente para não usar nenhuma.
                        .texture(RenderPhase.NO_TEXTURE)
                        // --- FIM DA CORREÇÃO ---

                        .build(false));
    }
}