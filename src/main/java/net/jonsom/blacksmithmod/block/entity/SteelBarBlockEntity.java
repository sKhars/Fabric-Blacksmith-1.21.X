package net.jonsom.blacksmithmod.block.entity;

import net.fabricmc.fabric.api.blockview.v2.RenderDataBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class SteelBarBlockEntity extends BlockEntity implements RenderDataBlockEntity {

    // CORRIGIDO: Array de 10x4
    private final float[][] voxels = new float[10][4];

    public SteelBarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.STEEL_BAR_BLOCK_ENTITY, pos, state);
        for (int x = 0; x < 10; x++) {
            for (int z = 0; z < 4; z++) {
                // MUDANÇA SUTIL: Um pouco abaixo do máximo para evitar bugs de renderização
                voxels[x][z] = 3.0f;
            }
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        for (int x = 0; x < 10; x++) {
            for (int z = 0; z < 4; z++) { // CORRIGIDO: Loop até 4
                nbt.putFloat("voxel_" + x + "_" + z, voxels[x][z]);
            }
        }
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        for (int x = 0; x < 10; x++) {
            for (int z = 0; z < 4; z++) { // CORRIGIDO: Loop até 4
                if (nbt.contains("voxel_" + x + "_" + z)) {
                    voxels[x][z] = nbt.getFloat("voxel_" + x + "_" + z);
                }
            }
        }
    }

    @Override
    public @Nullable Object getRenderData() {
        return voxels;
    }

    public float[][] getVoxels() {
        return this.voxels;
    }

    // --- MÉTODO DEFORM TOTALMENTE NOVO E CORRIGIDO ---
    public void deform(Vec3d hitPos) {
        // Posição relativa do clique dentro do bloco (0.0 a 1.0)
        double localX = hitPos.getX() - getPos().getX();
        double localZ = hitPos.getZ() - getPos().getZ();

        // Constantes para o offset e tamanho da VoxelShape (em unidades de 1/16 de bloco)
        final float OFFSET_X = 3.0f / 16.0f;
        final float OFFSET_Z = 6.0f / 16.0f;
        final float BAR_WIDTH_X = 10.0f / 16.0f;
        final float BAR_WIDTH_Z = 4.0f / 16.0f;

        // Normaliza a posição do clique para o espaço da barra (de 0.0 a 1.0)
        double normalizedX = (localX - OFFSET_X) / BAR_WIDTH_X;
        double normalizedZ = (localZ - OFFSET_Z) / BAR_WIDTH_Z;

        // Converte para coordenadas do array de voxels (0-9 para X, 0-3 para Z)
        int voxelX = (int) (normalizedX * 10);
        int voxelZ = (int) (normalizedZ * 4);

        // Verifica se o clique foi dentro dos limites da barra
        if (voxelX >= 0 && voxelX < 10 && voxelZ >= 0 && voxelZ < 4) {
            if (voxels[voxelX][voxelZ] > 1.0f) {
                voxels[voxelX][voxelZ] -= 0.5f; // Deforma o voxel clicado
                markDirty(); // Marca a entidade como "suja" para salvar
                if (world != null && !world.isClient) {
                    // Sincroniza as mudanças com o cliente
                    world.updateListeners(pos, getCachedState(), getCachedState(), 3);
                }
            }
        }
    }
}