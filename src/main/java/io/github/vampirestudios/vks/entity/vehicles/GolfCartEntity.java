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
public class GolfCartEntity extends LandVehicleEntity/* implements EntityRaytracer.IEntityRaytraceable*/ {
    public GolfCartEntity(World worldIn) {
        super(ModEntities.GOLF_CART, worldIn);
        //TODO figure out electric vehicles
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
    public float getMinEnginePitch()
    {
        return 0.6F;
    }

    @Override
    public float getMaxEnginePitch()
    {
        return 1.4F;
    }

    @Override
    public boolean canBeColored()
    {
        return true;
    }

    @Override
    public boolean canMountTrailer()
    {
        return false;
    }
}