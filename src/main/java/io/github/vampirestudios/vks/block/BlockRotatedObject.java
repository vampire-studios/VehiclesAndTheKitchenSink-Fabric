package io.github.vampirestudios.vks.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;

/**
 * Author: MrCrayfish
 */
public abstract class BlockRotatedObject extends BlockObject {

    public static final DirectionProperty DIRECTION = HorizontalFacingBlock.FACING;

    public BlockRotatedObject(Block.Settings properties) {
        super(properties);
        this.setDefaultState(this.getStateManager().getDefaultState().with(DIRECTION, Direction.NORTH));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return super.getPlacementState(context).with(DIRECTION, context.getPlayerFacing());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(DIRECTION);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(DIRECTION, rotation.rotate(state.get(DIRECTION)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(DIRECTION)));
    }

}