package net.jonsom.blacksmithmod.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem; // <-- IMPORTAÇÃO ADICIONADA
import net.jonsom.blacksmithmod.block.entity.SteelBarBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.joml.Matrix4f;

public class SteelBarBlockEntityRenderer implements BlockEntityRenderer<SteelBarBlockEntity> {

    private static final int GRID_X_SIZE = 10;
    private static final int GRID_Z_SIZE = 4;
    private static final float VOXEL_SIZE = 1.0f / 24.0f;
    private static final float HEIGHT_SCALE = 1.0f / 24.0f;
    private static final Identifier BLANK_TEXTURE = Identifier.of("minecraft", "textures/block/white_concrete.png");

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

        // --- ETAPA 1: Preenchimento Translúcido ---
        VertexConsumer fillBuffer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(BLANK_TEXTURE));
        final int fillR = 150, fillG = 150, fillB = 150, fillA = 80;

        for (int x = 0; x < GRID_X_SIZE; x++) {
            for (int z = 0; z < GRID_Z_SIZE; z++) {
                drawVoxelSolid(fillBuffer, matrices, voxels[x][z], x, z, fillR, fillG, fillB, fillA, light, overlay);
            }
        }

        // --- ETAPA 2: Grade Preta ---
        // CORREÇÃO 1: Usamos o nome de método correto: getLines()
        VertexConsumer lineBuffer = vertexConsumers.getBuffer(RenderLayer.getLines());
        final int lineR = 0, lineG = 0, lineB = 0, lineA = 255;

        // CORREÇÃO 2: Definimos a espessura da linha para evitar o bug dos planos pretos
        RenderSystem.lineWidth(1.5F);

        for (int x = 0; x < GRID_X_SIZE; x++) {
            for (int z = 0; z < GRID_Z_SIZE; z++) {
                drawVoxelWireframe(lineBuffer, matrices, voxels[x][z], x, z, lineR, lineG, lineB, lineA);
            }
        }

        matrices.pop();
    }

    // (O resto da classe e os métodos auxiliares continuam exatamente iguais)

    private void drawVoxelSolid(VertexConsumer buffer, MatrixStack matrices, float height, int x, int z, int r, int g, int b, int a, int light, int overlay) {
        if (height <= 0) return;
        matrices.push();
        matrices.translate(x * VOXEL_SIZE, 0, z * VOXEL_SIZE);

        MatrixStack.Entry entry = matrices.peek();

        float x1 = 0, y1 = 0, z1 = 0;
        float x2 = VOXEL_SIZE, y2 = height * HEIGHT_SCALE, z2 = VOXEL_SIZE;

        quad(buffer, entry, x1, y1, z1, x2, y1, z1, x2, y2, z1, x1, y2, z1, r, g, b, a, light, overlay, 0, 0, -1);
        quad(buffer, entry, x1, y1, z2, x1, y2, z2, x2, y2, z2, x2, y1, z2, r, g, b, a, light, overlay, 0, 0, 1);
        quad(buffer, entry, x1, y1, z1, x1, y1, z2, x2, y1, z2, x2, y1, z1, r, g, b, a, light, overlay, 0, -1, 0);
        quad(buffer, entry, x1, y2, z1, x2, y2, z1, x2, y2, z2, x1, y2, z2, r, g, b, a, light, overlay, 0, 1, 0);
        quad(buffer, entry, x1, y1, z1, x1, y2, z1, x1, y2, z2, x1, y1, z2, r, g, b, a, light, overlay, -1, 0, 0);
        quad(buffer, entry, x2, y1, z1, x2, y1, z2, x2, y2, z2, x2, y2, z1, r, g, b, a, light, overlay, 1, 0, 0);

        matrices.pop();
    }

    private void drawVoxelWireframe(VertexConsumer buffer, MatrixStack matrices, float height, int x, int z, int r, int g, int b, int a) {
        if (height <= 0) return;
        matrices.push();
        matrices.translate(x * VOXEL_SIZE, 0, z * VOXEL_SIZE);

        MatrixStack.Entry entry = matrices.peek();

        float x1 = 0, y1 = 0, z1 = 0;
        float x2 = VOXEL_SIZE, y2 = height * HEIGHT_SCALE, z2 = VOXEL_SIZE;

        line(buffer, entry, x1, y1, z1, x2, y1, z1, r, g, b, a); line(buffer, entry, x2, y1, z1, x2, y1, z2, r, g, b, a); line(buffer, entry, x2, y1, z2, x1, y1, z2, r, g, b, a); line(buffer, entry, x1, y1, z2, x1, y1, z1, r, g, b, a);
        line(buffer, entry, x1, y2, z1, x2, y2, z1, r, g, b, a); line(buffer, entry, x2, y2, z1, x2, y2, z2, r, g, b, a); line(buffer, entry, x2, y2, z2, x1, y2, z2, r, g, b, a); line(buffer, entry, x1, y2, z2, x1, y2, z1, r, g, b, a);
        line(buffer, entry, x1, y1, z1, x1, y2, z1, r, g, b, a); line(buffer, entry, x2, y1, z1, x2, y2, z1, r, g, b, a); line(buffer, entry, x1, y1, z2, x1, y2, z2, r, g, b, a); line(buffer, entry, x2, y1, z2, x2, y2, z2, r, g, b, a);

        matrices.pop();
    }

    private void quad(VertexConsumer buffer, MatrixStack.Entry entry, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, int r, int g, int b, int a, int light, int overlay, int normalX, int normalY, int normalZ) {
        buffer.vertex(entry.getPositionMatrix(), x1, y1, z1).color(r, g, b, a).texture(0f, 1f).light(light).overlay(overlay).normal(entry, normalX, normalY, normalZ);
        buffer.vertex(entry.getPositionMatrix(), x2, y2, z2).color(r, g, b, a).texture(1f, 1f).light(light).overlay(overlay).normal(entry, normalX, normalY, normalZ);
        buffer.vertex(entry.getPositionMatrix(), x3, y3, z3).color(r, g, b, a).texture(1f, 0f).light(light).overlay(overlay).normal(entry, normalX, normalY, normalZ);
        buffer.vertex(entry.getPositionMatrix(), x4, y4, z4).color(r, g, b, a).texture(0f, 0f).light(light).overlay(overlay).normal(entry, normalX, normalY, normalZ);
    }

    private void line(VertexConsumer buffer, MatrixStack.Entry entry, float x1, float y1, float z1, float x2, float y2, float z2, int r, int g, int b, int a) {
        // Adicionamos a informação de normal que a camada de linhas espera
        buffer.vertex(entry.getPositionMatrix(), x1, y1, z1).color(r, g, b, a).normal(entry, 0, 1, 0);
        buffer.vertex(entry.getPositionMatrix(), x2, y2, z2).color(r, g, b, a).normal(entry, 0, 1, 0);
    }
}