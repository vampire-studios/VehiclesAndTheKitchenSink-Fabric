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
public class GoKartEntity extends LandVehicleEntity/* implements IEntityRaytraceable*/ {
    public GoKartEntity(World worldIn) {
        super(ModEntities.GO_KART, worldIn);
        this.setMaxSpeed(20F);
        this.setTurnSensitivity(12);
        this.stepHeight = 0.625F;
        this.setFuelConsumption(0.5F);
    }

    public GoKartEntity(World world, double x, double y, double z) {
        super(ModEntities.GO_KART, world);
        this.setPos(x, y, z);
        this.setMaxSpeed(20F);
        this.setTurnSensitivity(12);
        this.stepHeight = 0.625F;
        this.setFuelConsumption(0.5F);
    }

    @Override
    public SoundEvent getMovingSound()
    {
        return ModSounds.GO_KART_ENGINE_MONO;
    }

    @Override
    public SoundEvent getRidingSound()
    {
        return ModSounds.GO_KART_ENGINE_STEREO;
    }

    @Override
    public EngineType getEngineType()
    {
        return EngineType.SMALL_MOTOR;
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
    public boolean shouldShowEngineSmoke()
    {
        return true;
    }

    @Override
    public Vec3d getEngineSmokePosition()
    {
        return new Vec3d(0, 0.55, -0.9);
    }

    @Override
    public boolean canBeColored()
    {
        return true;
    }

    @Override
    public boolean shouldRenderEngine()
    {
        return true;
    }

    @Override
    public boolean shouldRenderFuelPort()
    {
        return false;
    }

    @Override
    public boolean isLockable()
    {
        return false;
    }

}