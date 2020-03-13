package io.github.vampirestudios.vks.entity.vehicles;

import io.github.vampirestudios.vks.entity.EngineType;
import io.github.vampirestudios.vks.entity.LandVehicleEntity;
import io.github.vampirestudios.vks.init.ModEntities;
import io.github.vampirestudios.vks.init.ModSounds;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
public class ATVEntity extends LandVehicleEntity/* implements IEntityRaytraceable*/ {
    public ATVEntity(World worldIn)
    {
        super(ModEntities.ATV, worldIn);
        this.setMaxSpeed(15);
        this.setFuelCapacity(20000F);
    }

    /*@Override
    public FuelPortType getFuelPortType()
    {
        return FuelPortType.SMALL;
    }*/

    @Override
    public SoundEvent getMovingSound()
    {
        return ModSounds.ATV_ENGINE_MONO;
    }

    @Override
    public SoundEvent getRidingSound()
    {
        return ModSounds.ATV_ENGINE_STEREO;
    }

    @Override
    public EngineType getEngineType()
    {
        return EngineType.SMALL_MOTOR;
    }

    @Override
    public boolean canBeColored()
    {
        return true;
    }

    @Override
    public boolean canTowTrailer()
    {
        return true;
    }
}