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

// Removemos RenderDataBlockEntity e usamos a forma padrão do Minecraft
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

    // --- MÉTODOS DE SINCRONIZAÇÃO (A GRANDE MUDANÇA) ---
    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }
    // --- FIM DOS MÉTODOS DE SINCRONIZAÇÃO ---


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

    // O RenderData não é mais necessário, mas o renderer precisa de um jeito de pegar os voxels.
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

        int voxelX = (int) (normalizedX * 10);
        int voxelZ = (int) (normalizedZ * 4);

        if (voxelX >= 0 && voxelX < 10 && voxelZ >= 0 && voxelZ < 4) {
            if (voxels[voxelX][voxelZ] > 1.0f) {
                voxels[voxelX][voxelZ] -= 0.5f;
                markDirty(); // Marca para salvar
                if (world != null && !world.isClient) {
                    // Envia a atualização para o cliente
                    world.updateListeners(pos, getCachedState(), getCachedState(), 3);
                }
            }
        }
    }
}