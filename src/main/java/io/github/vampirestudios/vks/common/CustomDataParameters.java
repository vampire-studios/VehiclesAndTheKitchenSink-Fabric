package io.github.vampirestudios.vks.common;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

/**
 * Author: MrCrayfish
 */
public class CustomDataParameters
{
    public static final TrackedData<Boolean> PUSHING_CART = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<CompoundTag> HELD_VEHICLE = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.TAG_COMPOUND);
    public static final TrackedData<Integer> TRAILER = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static final TrackedData<Optional<BlockPos>> GAS_PUMP = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.OPTIONA_BLOCK_POS);
}