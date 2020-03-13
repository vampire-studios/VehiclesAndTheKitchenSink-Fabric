package io.github.vampirestudios.vks.entity.vehicles;

import io.github.vampirestudios.vks.entity.EngineType;
import io.github.vampirestudios.vks.entity.LandVehicleEntity;
import io.github.vampirestudios.vks.init.ModEntities;
import io.github.vampirestudios.vks.init.ModSounds;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
public class TractorEntity extends LandVehicleEntity/* implements EntityRaytracer.IEntityRaytraceable*/ {

    public TractorEntity(World worldIn) {
        super(ModEntities.TRACTOR, worldIn);
        this.setMaxSpeed(6);
        this.setTurnSensitivity(3);
    }

    @Override
    public SoundEvent getMovingSound()
    {
        return ModSounds.TRACTOR_ENGINE_MONO;
    }

    @Override
    public SoundEvent getRidingSound()
    {
        return ModSounds.TRACTOR_ENGINE_STEREO;
    }

    @Override
    public float getMinEnginePitch()
    {
        return 0.8F;
    }

    @Override
    public float getMaxEnginePitch()
    {
        return 1.6F;
    }

    @Override
    public EngineType getEngineType()
    {
        return EngineType.LARGE_MOTOR;
    }

    /*@Override
    public FuelPortType getFuelPortType()
    {
        return FuelPortType.DEFAULT;
    }*/

    @Override
    public boolean shouldRenderEngine()
    {
        return true;
    }

    @Override
    public boolean shouldShowEngineSmoke()
    {
        return true;
    }

    @Override
    public Vec3d getEngineSmokePosition()
    {
        return new Vec3d(-0.125, 1.9375, 1.125);
    }

    @Override
    public boolean canTowTrailer()
    {
        return true;
    }

    @Override
    public boolean canMountTrailer()
    {
        return false;
    }

    @Override
    public boolean canBeColored()
    {
        return true;
    }
}