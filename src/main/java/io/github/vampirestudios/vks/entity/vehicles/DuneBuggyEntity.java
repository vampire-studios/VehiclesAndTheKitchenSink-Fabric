package io.github.vampirestudios.vks.entity.vehicles;

import io.github.vampirestudios.vks.entity.EngineType;
import io.github.vampirestudios.vks.entity.LandVehicleEntity;
import io.github.vampirestudios.vks.entity.WheelType;
import io.github.vampirestudios.vks.init.ModEntities;
import io.github.vampirestudios.vks.init.ModSounds;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
public class DuneBuggyEntity extends LandVehicleEntity/* implements IEntityRaytraceable*/ {
    public DuneBuggyEntity(World worldIn) {
        super(ModEntities.DUNE_BUGGY, worldIn);
        this.setMaxSpeed(10);
        this.stepHeight = 0.5F;
        this.setFuelCapacity(5000F);
    }

    @Override
    public void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.set(WHEEL_TYPE, WheelType.PLASTIC.ordinal());
        this.dataTracker.set(COLOR, 0xF2B116);
    }

    @Override
    public SoundEvent getMovingSound()
    {
        return ModSounds.ELECTRIC_ENGINE_MONO;
    }

    @Override
    public SoundEvent getRidingSound()
    {
        return ModSounds.ELECTRIC_ENGINE_STEREO;
    }

    @Override
    public EngineType getEngineType()
    {
        return EngineType.ELECTRIC_MOTOR;
    }

    @Override
    public boolean isLockable()
    {
        return false;
    }
}