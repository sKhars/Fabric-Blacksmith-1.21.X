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

    private final float[][] voxels = new float[10][3];

    public SteelBarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.STEEL_BAR_BLOCK_ENTITY, pos, state);
        for (int x = 0; x < 10; x++) {
            for (int z = 0; z < 3; z++) {
                voxels[x][z] = 4.0f;
            }
        }
    }

    // --- Salvamento NBT ---
    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        for (int x = 0; x < 10; x++) {
            for (int z = 0; z < 3; z++) {
                nbt.putFloat("voxel_" + x + "_" + z, voxels[x][z]);
            }
        }
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        for (int x = 0; x < 10; x++) {
            for (int z = 0; z < 3; z++) {
                if (nbt.contains("voxel_" + x + "_" + z)) {
                    voxels[x][z] = nbt.getFloat("voxel_" + x + "_" + z);
                }
            }
        }
    }

    // --- Render Data para o lado cliente ---
    @Override
    public @Nullable Object getRenderData() {
        return voxels;
    }

    // --- Acesso interno ---
    public float[][] getVoxels() {
        return this.voxels;
    }

    // --- Deformação com sincronização ---
    public void deform(Vec3d hitPos) {
        double localX = hitPos.getX() - getPos().getX();
        double localZ = hitPos.getZ() - getPos().getZ();

        int voxelX = (int) (localX * 10);
        int voxelZ = (int) (localZ * 3);

        if (voxelX >= 0 && voxelX < 10 && voxelZ >= 0 && voxelZ < 3) {
            if (voxels[voxelX][voxelZ] > 1.0f) {
                voxels[voxelX][voxelZ] -= 0.5f;
                markDirty();
                if (world != null && !world.isClient) {
                    world.updateListeners(pos, getCachedState(), getCachedState(), 3);
                }
            }
        }
    }
}
