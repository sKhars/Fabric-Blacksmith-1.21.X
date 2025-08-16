package net.jonsom.blacksmithmod.block.custom;

import com.mojang.serialization.MapCodec;
import net.jonsom.blacksmithmod.block.entity.SteelBarBlockEntity;
import net.jonsom.blacksmithmod.item.custom.HammerItem;
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
        // A verificação agora é feita aqui fora
        if (!world.isClient && player.getMainHandStack().getItem() instanceof HammerItem) {
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof SteelBarBlockEntity entity) {
                // A lógica de deformação só acontece se o jogador estiver segurando o martelo
                entity.deform(hit.getPos());
                world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
            }
            return ActionResult.SUCCESS; // Ação bem-sucedida
        }
        // Se o jogador não estiver segurando um martelo, a ação "passa" e nada acontece.
        return ActionResult.PASS;
    }
}