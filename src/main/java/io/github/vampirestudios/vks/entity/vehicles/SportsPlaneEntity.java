package io.github.vampirestudios.vks.entity.vehicles;

import io.github.vampirestudios.vks.entity.EngineType;
import io.github.vampirestudios.vks.entity.PlaneEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
public class SportsPlaneEntity extends PlaneEntity/* implements IEntityRaytraceable*/
{
    public float wheelSpeed;
    public float wheelRotation;
    public float prevWheelRotation;

    public float propellerSpeed;
    public float propellerRotation;
    public float prevPropellerRotation;

    public SportsPlaneEntity(EntityType<? extends SportsPlaneEntity> type, World worldIn)
    {
        super(type, worldIn);
        this.setAccelerationSpeed(0.5F);
        this.setMaxSpeed(25F);
        this.setMaxTurnAngle(25);
        this.setTurnSensitivity(2);
        this.setFuelCapacity(75000F);
        this.setFuelConsumption(1.0F);
    }

    @Override
    public Box getVisibilityBoundingBox()
    {
        return this.getBoundingBox().expand(1.5);
    }

    @Override
    public void updateVehicle()
    {
        prevWheelRotation = wheelRotation;
        prevPropellerRotation = propellerRotation;

        if(this.onGround)
        {
            wheelSpeed = currentSpeed / 30F;
        }
        else
        {
            wheelSpeed *= 0.95F;
        }
        wheelRotation -= (90F * wheelSpeed);

        if(this.canDrive() && this.getControllingPassenger() != null)
        {
            propellerSpeed += 1F;
            if(propellerSpeed > 120F)
            {
                propellerSpeed = 120F;
            }
        }
        else
        {
            propellerSpeed *= 0.95F;
        }
        propellerRotation += propellerSpeed;
    }

    @Override
    public SoundEvent getMovingSound()
    {
//        return ModSounds.SPORTS_PLANE_ENGINE_MONO.get();
        return SoundEvents.ENTITY_MINECART_RIDING;
    }

    @Override
    public SoundEvent getRidingSound()
    {
//        return ModSounds.SPORTS_PLANE_ENGINE_STEREO.get();
        return SoundEvents.ENTITY_MINECART_RIDING;
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
    protected float getModifiedAccelerationSpeed()
    {
        return super.getModifiedAccelerationSpeed() * (propellerSpeed / 120F);
    }

    @Override
    public boolean canMountTrailer()
    {
        return false;
    }
}