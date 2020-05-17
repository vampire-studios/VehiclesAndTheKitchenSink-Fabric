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
public class SandBusEntity extends LandVehicleEntity/* implements EntityRaytracer.IEntityRaytraceable*/ {
    public SandBusEntity(World worldIn) {
        super(ModEntities.SAND_BUS, worldIn);
        this.setMaxSpeed(15F);
        this.setTurnSensitivity(2);
        this.setFuelCapacity(30000F);
        this.setFuelConsumption(0.375F);
    }

    @Override
    public SoundEvent getMovingSound()
    {
        return ModSounds.MINI_BUS_ENGINE_MONO;
}

    @Override
    public SoundEvent getRidingSound()
    {
        return ModSounds.MINI_BUS_ENGINE_STEREO;
    }

    @Override
    public float getMinEnginePitch()
    {
        return 0.75F;
    }

    @Override
    public float getMaxEnginePitch()
    {
        return 1.25F;
    }

    @Override
    public EngineType getEngineType()
    {
        return EngineType.LARGE_MOTOR;
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