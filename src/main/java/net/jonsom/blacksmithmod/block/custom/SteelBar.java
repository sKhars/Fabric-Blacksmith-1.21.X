package net.jonsom.blacksmithmod.block.custom;

import com.mojang.serialization.MapCodec;
import net.jonsom.blacksmithmod.block.entity.SteelBarBlockEntity; // IMPORTANTE: Importe a sua futura classe BlockEntity
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class SteelBar extends BlockWithEntity implements BlockEntityProvider {

    // Define a "caixa de colisão" inicial da barra de metal.
    // Isso é usado para o contorno preto e para o jogador não atravessar o bloco.
    private static final VoxelShape SHAPE = Block.createCuboidShape(3, 0, 6, 13, 3, 10);

    // Construtor padrão do bloco.
    public SteelBar(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    // --- MÉTODOS IMPORTANTES SOBRESCRITOS ---

    // Este método informa ao Minecraft qual a forma física do bloco.
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    // !! PONTO CRUCIAL !!
    // Define como o bloco deve ser renderizado.
    // Usamos INVISIBLE porque a renderização não será feita por um modelo JSON,
    // mas sim por um BlockEntityRenderer customizado que faremos a seguir.
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    // Este é o método que conecta o Bloco à sua "mente" (a BlockEntity).
    // Toda vez que o bloco é colocado no mundo, este método é chamado.
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        // Ele deve retornar uma nova instância da sua SteelBarBlockEntity.
        return new SteelBarBlockEntity(pos, state);
    }
}