package net.jonsom.blacksmithmod.block.custom;

import com.mojang.serialization.MapCodec;
import net.jonsom.blacksmithmod.block.entity.SteelBarBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SteelBar extends BlockWithEntity implements BlockEntityProvider {

    private static final VoxelShape SHAPE = Block.createCuboidShape(3, 0, 6, 13, 3, 10);

    public SteelBar(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec(SteelBar::new);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SteelBarBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) { // Lógica sempre no servidor
            BlockEntity blockEntity = world.getBlockEntity(pos);

            // Verifica se é a nossa entidade e se o jogador está usando a mão principal
            if (blockEntity instanceof SteelBarBlockEntity entity) {

                // TODO: Adicionar verificação se o jogador segura um martelo

                // Chama o método que deforma a barra, passando a posição exata do clique
                entity.deform(hit.getPos());

                // Força a atualização do bloco no cliente para que o renderer seja chamado
                world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
            }
        }
        return ActionResult.SUCCESS;
    }
}