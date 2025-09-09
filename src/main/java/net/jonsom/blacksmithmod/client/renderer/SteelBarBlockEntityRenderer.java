package net.jonsom.blacksmithmod.client.renderer;

import net.jonsom.blacksmithmod.block.entity.SteelBarBlockEntity;
import net.minecraft.client.render.RenderLayer; // Importação normal
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.world.World;
import org.joml.Matrix4f;

public class SteelBarBlockEntityRenderer implements BlockEntityRenderer<SteelBarBlockEntity> {

    private static final int GRID_X_SIZE = 10;
    private static final int GRID_Z_SIZE = 4;
    private static final float VOXEL_SIZE = 1.0f / 16.0f;
    private static final float HEIGHT_SCALE = 1.0f / 16.0f;

    public SteelBarBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(SteelBarBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {

        World world = entity.getWorld();
        if (world == null) return;

        float[][] voxels = entity.getVoxels();
        if (voxels == null) return;

        // --- AQUI ESTÁ A MUDANÇA ---
        // Usamos uma camada de renderização do próprio Minecraft, que é garantida para funcionar.
        // Ela não usa iluminação do mundo, então o objeto ficará com uma cor sólida e brilhante.
        VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getDebugQuads());
        // --- FIM DA MUDANÇA ---

        final int R = 160, G = 160, B = 160, A = 255;

        matrices.push();
        matrices.translate(3.0f * VOXEL_SIZE, 0.005f, 6.0f * VOXEL_SIZE);

        for (int x = 0; x < GRID_X_SIZE; x++) {
            for (int z = 0; z < GRID_Z_SIZE; z++) {
                float height = voxels[x][z];
                if (height <= 0) continue;

                matrices.push();
                float startX = x * VOXEL_SIZE;
                float startZ = z * VOXEL_SIZE;
                float endY = height * HEIGHT_SCALE;
                matrices.translate(startX, 0, startZ);

                MatrixStack.Entry entry = matrices.peek();
                Matrix4f positionMatrix = entry.getPositionMatrix();

                float x1 = 0, y1 = 0, z1 = 0;
                float x2 = VOXEL_SIZE, y2 = endY, z2 = VOXEL_SIZE;

                // Para o RenderLayer.getDebugQuads(), não precisamos passar luz ou normais.
                // Apenas posição e cor.

                // Face de Trás (Z negativo)
                buffer.vertex(positionMatrix, x1, y1, z1).color(R, G, B, A);
                buffer.vertex(positionMatrix, x2, y1, z1).color(R, G, B, A);
                buffer.vertex(positionMatrix, x2, y2, z1).color(R, G, B, A);
                buffer.vertex(positionMatrix, x1, y2, z1).color(R, G, B, A);

                // Face da Frente (Z positivo)
                buffer.vertex(positionMatrix, x1, y2, z2).color(R, G, B, A);
                buffer.vertex(positionMatrix, x2, y2, z2).color(R, G, B, A);
                buffer.vertex(positionMatrix, x2, y1, z2).color(R, G, B, A);
                buffer.vertex(positionMatrix, x1, y1, z2).color(R, G, B, A);

                // Face de Baixo (Y negativo)
                buffer.vertex(positionMatrix, x1, y1, z2).color(R, G, B, A);
                buffer.vertex(positionMatrix, x2, y1, z2).color(R, G, B, A);
                buffer.vertex(positionMatrix, x2, y1, z1).color(R, G, B, A);
                buffer.vertex(positionMatrix, x1, y1, z1).color(R, G, B, A);

                // Face de Cima (Y positivo)
                buffer.vertex(positionMatrix, x1, y2, z1).color(R, G, B, A);
                buffer.vertex(positionMatrix, x2, y2, z1).color(R, G, B, A);
                buffer.vertex(positionMatrix, x2, y2, z2).color(R, G, B, A);
                buffer.vertex(positionMatrix, x1, y2, z2).color(R, G, B, A);

                // Face da Esquerda (X negativo)
                buffer.vertex(positionMatrix, x1, y1, z2).color(R, G, B, A);
                buffer.vertex(positionMatrix, x1, y1, z1).color(R, G, B, A);
                buffer.vertex(positionMatrix, x1, y2, z1).color(R, G, B, A);
                buffer.vertex(positionMatrix, x1, y2, z2).color(R, G, B, A);

                // Face da Direita (X positivo)
                buffer.vertex(positionMatrix, x2, y2, z2).color(R, G, B, A);
                buffer.vertex(positionMatrix, x2, y2, z1).color(R, G, B, A);
                buffer.vertex(positionMatrix, x2, y1, z1).color(R, G, B, A);
                buffer.vertex(positionMatrix, x2, y1, z2).color(R, G, B, A);

                matrices.pop();
            }
        }
        matrices.pop();
    }
}