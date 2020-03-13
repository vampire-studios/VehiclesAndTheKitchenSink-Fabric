package io.github.vampirestudios.vks.entity.vehicles;

import io.github.vampirestudios.vks.entity.EngineType;
import io.github.vampirestudios.vks.entity.LandVehicleEntity;
import io.github.vampirestudios.vks.init.ModEntities;
import io.github.vampirestudios.vks.init.ModSounds;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
public class BumperCarEntity extends LandVehicleEntity/* implements IEntityRaytraceable*/ {

    public BumperCarEntity(World worldIn) {
        super(ModEntities.BUMPER_CAR, worldIn);
        this.setMaxSpeed(10);
        this.setTurnSensitivity(20);
        this.stepHeight = 0.625F;
        //TODO figure out fuel system
    }

    /*@Override
    public void applyEntityCollision(Entity entityIn) {
        if(entityIn instanceof BumperCarEntity && this.hasPassengers())
        {
            applyBumperCollision((BumperCarEntity) entityIn);
        }
    }*/

    private void applyBumperCollision(BumperCarEntity entity) {
        this.setVelocity(this.getVelocity().add(this.vehicleMotionX * 2, 0, this.vehicleMotionZ * 2));
        world.playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.BONK, SoundCategory.NEUTRAL, 1.0F, 0.6F + 0.1F * this.getNormalSpeed());
        this.currentSpeed *= 0.25F;
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
    public float getMaxEnginePitch()
    {
        return 0.8F;
    }

    @Override
    public EngineType getEngineType()
    {
        return EngineType.ELECTRIC_MOTOR;
    }

    @Override
    public boolean canBeColored()
    {
        return true;
    }

    @Override
    public boolean isLockable()
    {
        return false;
    }
}