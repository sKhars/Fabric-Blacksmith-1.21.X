package net.jonsom.blacksmithmod.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class SteelBarBlockEntity extends BlockEntity {

    private final float[][] voxels = new float[10][4];

    public SteelBarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.STEEL_BAR_BLOCK_ENTITY, pos, state);
        for (int x = 0; x < 10; x++) {
            for (int z = 0; z < 4; z++) {
                voxels[x][z] = 3.0f;
            }
        }
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        for (int x = 0; x < 10; x++) {
            for (int z = 0; z < 4; z++) {
                nbt.putFloat("voxel_" + x + "_" + z, voxels[x][z]);
            }
        }
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        for (int x = 0; x < 10; x++) {
            for (int z = 0; z < 4; z++) {
                if (nbt.contains("voxel_" + x + "_" + z)) {
                    voxels[x][z] = nbt.getFloat("voxel_" + x + "_" + z);
                }
            }
        }
    }

    public float[][] getVoxels() {
        return this.voxels;
    }

    public void deform(Vec3d hitPos) {
        double localX = hitPos.getX() - getPos().getX();
        double localZ = hitPos.getZ() - getPos().getZ();

        final float OFFSET_X = 3.0f / 16.0f;
        final float OFFSET_Z = 6.0f / 16.0f;
        final float BAR_WIDTH_X = 10.0f / 16.0f;
        final float BAR_WIDTH_Z = 4.0f / 16.0f;

        double normalizedX = (localX - OFFSET_X) / BAR_WIDTH_X;
        double normalizedZ = (localZ - OFFSET_Z) / BAR_WIDTH_Z;

        int centerX = (int) (normalizedX * 10);
        int centerZ = (int) (normalizedZ * 4);

        int impactRadius = 2;
        float maxDeformation = 0.75f;
        boolean anyVoxelChanged = false;

        for (int x = centerX - impactRadius; x <= centerX + impactRadius; x++) {
            for (int z = centerZ - impactRadius; z <= centerZ + impactRadius; z++) {
                if (x >= 0 && x < 10 && z >= 0 && z < 4) {
                    double distance = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(z - centerZ, 2));
                    if (distance <= impactRadius) {
                        float deformation = maxDeformation * (float)(1.0 - distance / impactRadius);
                        if (voxels[x][z] > 1.0f) {
                            voxels[x][z] -= deformation;
                            if (voxels[x][z] < 1.0f) {
                                voxels[x][z] = 1.0f;
                            }
                            anyVoxelChanged = true;
                        }
                    }
                }
            }
        }

        if (anyVoxelChanged) {
            markDirty();
            if (world != null && !world.isClient) {
                world.updateListeners(pos, getCachedState(), getCachedState(), 3);
            }
        }
    }
}