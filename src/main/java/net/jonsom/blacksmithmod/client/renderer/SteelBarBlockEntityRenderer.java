package net.jonsom.blacksmithmod.client.renderer;

import net.jonsom.blacksmithmod.block.entity.SteelBarBlockEntity;
import net.minecraft.client.render.RenderLayer;
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
    private static final float VOXEL_SIZE = 1.0f / 24.0f;
    private static final float HEIGHT_SCALE = 1.0f / 24.0f;
    private static final float GRID_OFFSET = 0.002f;

    public SteelBarBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(SteelBarBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {

        World world = entity.getWorld();
        if (world == null) return;

        float[][] voxels = entity.getVoxels();
        if (voxels == null) return;

        matrices.push();
        matrices.translate(7.0f * VOXEL_SIZE, 0.005f, 10.0f * VOXEL_SIZE);

        // --- ETAPA 1: Desenhar os sólidos ---
        VertexConsumer solidBuffer = vertexConsumers.getBuffer(RenderLayer.getDebugQuads());
        final int R = 160, G = 160, B = 160, A = 255;

        for (int x = 0; x < GRID_X_SIZE; x++) {
            for (int z = 0; z < GRID_Z_SIZE; z++) {
                float height = voxels[x][z];
                if (height <= 0) continue;

                matrices.push();
                // ... (código de posicionamento igual)
                float startX = x * VOXEL_SIZE;
                float startZ = z * VOXEL_SIZE;
                float endY = height * HEIGHT_SCALE;
                matrices.translate(startX, 0, startZ);
                MatrixStack.Entry entry = matrices.peek();
                Matrix4f positionMatrix = entry.getPositionMatrix();
                float x1 = 0, y1 = 0, z1 = 0;
                float x2 = VOXEL_SIZE, y2 = endY, z2 = VOXEL_SIZE;

                // Adicionamos a chamada .normal() de volta a todos os vértices
                solidBuffer.vertex(positionMatrix, x1, y1, z1).color(R, G, B, A).normal(entry, 0, -1, 0); solidBuffer.vertex(positionMatrix, x2, y1, z1).color(R, G, B, A).normal(entry, 0, -1, 0); solidBuffer.vertex(positionMatrix, x2, y2, z1).color(R, G, B, A).normal(entry, 0, 1, 0); solidBuffer.vertex(positionMatrix, x1, y2, z1).color(R, G, B, A).normal(entry, 0, 1, 0);
                solidBuffer.vertex(positionMatrix, x1, y2, z2).color(R, G, B, A).normal(entry, 0, 1, 0); solidBuffer.vertex(positionMatrix, x2, y2, z2).color(R, G, B, A).normal(entry, 0, 1, 0); solidBuffer.vertex(positionMatrix, x2, y1, z2).color(R, G, B, A).normal(entry, 0, -1, 0); solidBuffer.vertex(positionMatrix, x1, y1, z2).color(R, G, B, A).normal(entry, 0, -1, 0);
                solidBuffer.vertex(positionMatrix, x1, y1, z2).color(R, G, B, A).normal(entry, 0, -1, 0); solidBuffer.vertex(positionMatrix, x2, y1, z2).color(R, G, B, A).normal(entry, 0, -1, 0); solidBuffer.vertex(positionMatrix, x2, y1, z1).color(R, G, B, A).normal(entry, 0, -1, 0); solidBuffer.vertex(positionMatrix, x1, y1, z1).color(R, G, B, A).normal(entry, 0, -1, 0);
                solidBuffer.vertex(positionMatrix, x1, y2, z1).color(R, G, B, A).normal(entry, 0, 1, 0); solidBuffer.vertex(positionMatrix, x2, y2, z1).color(R, G, B, A).normal(entry, 0, 1, 0); solidBuffer.vertex(positionMatrix, x2, y2, z2).color(R, G, B, A).normal(entry, 0, 1, 0); solidBuffer.vertex(positionMatrix, x1, y2, z2).color(R, G, B, A).normal(entry, 0, 1, 0);
                solidBuffer.vertex(positionMatrix, x1, y1, z2).color(R, G, B, A).normal(entry, -1, 0, 0); solidBuffer.vertex(positionMatrix, x1, y1, z1).color(R, G, B, A).normal(entry, -1, 0, 0); solidBuffer.vertex(positionMatrix, x1, y2, z1).color(R, G, B, A).normal(entry, -1, 0, 0); solidBuffer.vertex(positionMatrix, x1, y2, z2).color(R, G, B, A).normal(entry, -1, 0, 0);
                solidBuffer.vertex(positionMatrix, x2, y2, z2).color(R, G, B, A).normal(entry, 1, 0, 0); solidBuffer.vertex(positionMatrix, x2, y2, z1).color(R, G, B, A).normal(entry, 1, 0, 0); solidBuffer.vertex(positionMatrix, x2, y1, z1).color(R, G, B, A).normal(entry, 1, 0, 0); solidBuffer.vertex(positionMatrix, x2, y1, z2).color(R, G, B, A).normal(entry, 1, 0, 0);

                matrices.pop();
            }
        }

        // --- ETAPA 2: Desenhar a grade ---
        VertexConsumer lineBuffer = vertexConsumers.getBuffer(RenderLayer.getLines());
        final int lineR = 0, lineG = 0, lineB = 0, lineA = 255;

        for (int x = 0; x < GRID_X_SIZE; x++) {
            for (int z = 0; z < GRID_Z_SIZE; z++) {
                float height = voxels[x][z];
                if (height <= 0) continue;

                matrices.push();
                // ... (código de posicionamento igual)
                float startX = x * VOXEL_SIZE;
                float startZ = z * VOXEL_SIZE;
                float endY = height * HEIGHT_SCALE;
                matrices.translate(startX, 0, startZ);
                MatrixStack.Entry entry = matrices.peek();
                Matrix4f positionMatrix = entry.getPositionMatrix();
                float x1 = 0, z1 = 0;
                float x2 = VOXEL_SIZE, z2 = VOXEL_SIZE;
                float gridY = endY + GRID_OFFSET;

                // Adicionamos a chamada .normal() aqui também. Como a grade está no topo, o normal é (0, 1, 0) para "para cima".
                lineBuffer.vertex(positionMatrix, x1, gridY, z1).color(lineR, lineG, lineB, lineA).normal(entry, 0, 1, 0); lineBuffer.vertex(positionMatrix, x2, gridY, z1).color(lineR, lineG, lineB, lineA).normal(entry, 0, 1, 0);
                lineBuffer.vertex(positionMatrix, x2, gridY, z1).color(lineR, lineG, lineB, lineA).normal(entry, 0, 1, 0); lineBuffer.vertex(positionMatrix, x2, gridY, z2).color(lineR, lineG, lineB, lineA).normal(entry, 0, 1, 0);
                lineBuffer.vertex(positionMatrix, x2, gridY, z2).color(lineR, lineG, lineB, lineA).normal(entry, 0, 1, 0); lineBuffer.vertex(positionMatrix, x1, gridY, z2).color(lineR, lineG, lineB, lineA).normal(entry, 0, 1, 0);
                lineBuffer.vertex(positionMatrix, x1, gridY, z2).color(lineR, lineG, lineB, lineA).normal(entry, 0, 1, 0); lineBuffer.vertex(positionMatrix, x1, gridY, z1).color(lineR, lineG, lineB, lineA).normal(entry, 0, 1, 0);

                matrices.pop();
            }
        }

        matrices.pop();
    }
}