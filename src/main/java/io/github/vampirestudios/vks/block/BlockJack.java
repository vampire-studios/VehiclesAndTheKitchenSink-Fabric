package io.github.vampirestudios.vks.block;

import io.github.vampirestudios.vks.block.entity.JackTileEntity;
import io.github.vampirestudios.vks.utils.Names;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public class BlockJack extends BlockObject implements BlockEntityProvider {
    private static final VoxelShape SHAPE = Block.createCuboidShape(1, 0, 1, 15, 9, 15);

    public BlockJack()
    {
        super(Names.Block.JACK, Block.Settings.of(Material.PISTON));
    }

    @Override
    public boolean isSimpleFullBlock(BlockState state, BlockView worldIn, BlockPos pos)
    {
        return false;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView worldIn, BlockPos pos, EntityContext context) {
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        if(tileEntity instanceof JackTileEntity) {
            JackTileEntity jack = (JackTileEntity) tileEntity;
            return VoxelShapes.cuboid(SHAPE.getBoundingBox().expand(0, 0.5 * jack.getProgress(), 0));
        }
        return SHAPE;
    }

    @Override
    public boolean hasBlockEntity()
    {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        return new JackTileEntity();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

}