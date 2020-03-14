package io.github.vampirestudios.vks.block;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.entity.EntityContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

/**
 * Author: MrCrayfish
 */
public class BlockTrafficCone extends BlockObject {

    private static final VoxelShape COLLISION_SHAPE = Block.createCuboidShape(2, 0, 2, 14, 18, 14);
    private static final VoxelShape SELECTION_SHAPE = Block.createCuboidShape(1, 0, 1, 15, 16, 15);

    public BlockTrafficCone() {
        super(FabricBlockSettings.of(Material.CLAY, MaterialColor.ORANGE_TERRACOTTA).hardness(0.5F).build());
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, EntityContext context) {
        return COLLISION_SHAPE;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext context) {
        return SELECTION_SHAPE;
    }

}