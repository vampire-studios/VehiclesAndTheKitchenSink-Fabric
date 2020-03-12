package io.github.vampirestudios.vks.entity;

import io.github.vampirestudios.vks.client.render.Wheel;
import io.github.vampirestudios.vks.common.entity.PartPosition;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
public abstract class LandVehicleEntity extends PoweredVehicleEntity
{
    private static final TrackedData<Boolean> DRIFTING = DataTracker.registerData(LandVehicleEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public float drifting;
    public float additionalYaw;
    public float prevAdditionalYaw;

    public float frontWheelRotation;
    public float prevFrontWheelRotation;
    public float rearWheelRotation;
    public float prevRearWheelRotation;

    public LandVehicleEntity(EntityType<?> entityType, World worldIn)
    {
        super(entityType, worldIn);
    }

    @Override
    public void initDataTracker()
    {
        super.initDataTracker();
        this.dataTracker.startTracking(DRIFTING, false);
    }

    @Override
    public void onUpdateVehicle() {
        super.onUpdateVehicle();
        this.updateWheels();
    }

    @Override
    public void updateVehicle()
    {
        this.prevAdditionalYaw = this.additionalYaw;
        this.prevFrontWheelRotation = this.frontWheelRotation;
        this.prevRearWheelRotation = this.rearWheelRotation;
        this.updateDrifting();
    }

    @Override
    public void onClientUpdate()
    {
        super.onClientUpdate();
        LivingEntity entity = (LivingEntity) this.getControllingPassenger();
        if(entity != null && entity.equals(MinecraftClient.getInstance().player))
        {
            /*boolean drifting = VehicleMod.PROXY.isDrifting();
            if(this.isDrifting() != drifting)
            {
                this.setDrifting(drifting);
                PacketHandler.instance.sendToServer(new MessageDrift(drifting));
            }*/
        }
    }

    @Override
    public void updateVehicleMotion()
    {
        float currentSpeed = this.currentSpeed;

        if(this.speedMultiplier > 1.0F)
        {
            this.speedMultiplier = 1.0F;
        }

        /* Applies the speed multiplier to the current speed */
        currentSpeed = currentSpeed + (currentSpeed * this.speedMultiplier);

        VehicleProperties properties = this.getProperties();
        if(properties.getFrontAxelVec() != null && properties.getRearAxelVec() != null)
        {
            PartPosition bodyPosition = properties.getBodyPosition();
            Vec3d nextFrontAxelVec = new Vec3d(0, 0, currentSpeed / 20F).rotateY(this.wheelAngle * 0.017453292F);
            nextFrontAxelVec = nextFrontAxelVec.add(properties.getFrontAxelVec().multiply(0.0625));
            Vec3d nextRearAxelVec = new Vec3d(0, 0, currentSpeed / 20F);
            nextRearAxelVec = nextRearAxelVec.add(properties.getRearAxelVec().multiply(0.0625));
            double deltaYaw = Math.toDegrees(Math.atan2(nextRearAxelVec.z - nextFrontAxelVec.z, nextRearAxelVec.x - nextFrontAxelVec.x)) + 90;
            if(this.isRearWheelSteering())
            {
                deltaYaw -= 180;
            }
            this.yaw += deltaYaw;
            this.deltaYaw = (float) -deltaYaw;

            Vec3d nextVehicleVec = nextFrontAxelVec.add(nextRearAxelVec).multiply(0.5);
            nextVehicleVec = nextVehicleVec.subtract(properties.getFrontAxelVec().add(properties.getRearAxelVec()).multiply(0.0625).multiply(0.5));
            nextVehicleVec = nextVehicleVec.multiply(bodyPosition.getScale()).rotateY((-this.yaw + 90) * 0.017453292F);

            float targetRotation = (float) Math.toDegrees(Math.atan2(nextVehicleVec.z, nextVehicleVec.x));
            float f1 = MathHelper.sin(targetRotation * 0.017453292F) / 20F * (currentSpeed > 0 ? 1 : -1);
            float f2 = MathHelper.cos(targetRotation * 0.017453292F) / 20F * (currentSpeed > 0 ? 1 : -1);
            this.vehicleMotionX = (-currentSpeed * f1);
            if(!launching)
            {
                this.setVelocity(this.getVelocity().add(0, -0.08, 0));
            }
            this.vehicleMotionZ = (currentSpeed * f2);
        }
        else
        {
            float f1 = MathHelper.sin(this.yaw * 0.017453292F) / 20F;
            float f2 = MathHelper.cos(this.yaw * 0.017453292F) / 20F;
            this.vehicleMotionX = (-currentSpeed * f1);
            if(!launching)
            {
                this.setVelocity(this.getVelocity().add(0, -0.08, 0));
            }
            this.vehicleMotionZ = (currentSpeed * f2);
        }
    }

    @Override
    protected void updateTurning()
    {
        this.turnAngle = /*VehicleMod.PROXY.getTargetTurnAngle(this, this.isDrifting())*/0.0F;
        this.wheelAngle = this.turnAngle * Math.max(0.1F, 1.0F - Math.abs(currentSpeed / this.getMaxSpeed()));

        VehicleProperties properties = this.getProperties();
        if(properties.getFrontAxelVec() == null || properties.getRearAxelVec() == null)
        {
            this.deltaYaw = this.wheelAngle * (currentSpeed / 30F) / 2F;
        }

        if(world.isClient)
        {
            this.targetWheelAngle = this.isDrifting() ? -35F * (this.turnAngle / (float) this.getMaxTurnAngle()) * this.getNormalSpeed() : this.wheelAngle - 35F * (this.turnAngle / (float) this.getMaxTurnAngle()) * drifting;
            this.renderWheelAngle = this.renderWheelAngle + (this.targetWheelAngle - this.renderWheelAngle) * (this.isDrifting() ? 0.35F : 0.5F);
        }
    }

    private void updateDrifting()
    {
        TurnDirection turnDirection = this.getTurnDirection();
        if(this.getControllingPassenger() != null && this.isDrifting())
        {
            if(turnDirection != TurnDirection.FORWARD)
            {
                AccelerationDirection acceleration = this.getAcceleration();
                if(acceleration == AccelerationDirection.FORWARD)
                {
                    this.currentSpeed *= 0.975F;
                }
                this.drifting = Math.min(1.0F, this.drifting + 0.025F);
            }
        }
        else
        {
            this.drifting *= 0.95F;
        }
        this.additionalYaw = 25F * this.drifting * (this.turnAngle / (float) this.getMaxTurnAngle()) * Math.min(this.getActualMaxSpeed(), this.getActualSpeed() * 2F);

        //Updates the delta yaw to consider drifting
        this.deltaYaw = this.wheelAngle * (this.currentSpeed / 30F) / (this.isDrifting() ? 1.5F : 2F);
    }

    public void updateWheels()
    {
        VehicleProperties properties = this.getProperties();
        double wheelCircumference = 16.0;
        double vehicleScale = properties.getBodyPosition().getScale();
        double speed = this.getSpeed();

        Wheel frontWheel = properties.getFirstFrontWheel();
        if(frontWheel != null)
        {
            double frontWheelCircumference = wheelCircumference * vehicleScale * frontWheel.getScaleY();
            double rotation = (speed * 16) / frontWheelCircumference;
            this.frontWheelRotation -= rotation * 20F;
        }

        Wheel rearWheel = properties.getFirstRearWheel();
        if(rearWheel != null)
        {
            double rearWheelCircumference = wheelCircumference * vehicleScale * rearWheel.getScaleY();
            double rotation = (speed * 16) / rearWheelCircumference;
            this.rearWheelRotation -= rotation * 20F;
        }
    }

    @Override
    public void createParticles()
    {
        if(this.canDrive())
        {
            super.createParticles();
        }
    }

    @Override
    protected void removePassenger(Entity passenger)
    {
        super.removePassenger(passenger);
        if(this.getControllingPassenger() == null)
        {
            this.yaw -= this.additionalYaw;
            this.additionalYaw = 0;
            this.drifting = 0;
        }
    }

    public void setDrifting(boolean drifting)
    {
        this.dataTracker.set(DRIFTING, drifting);
    }

    public boolean isDrifting()
    {
        return this.dataTracker.get(DRIFTING);
    }

    @Override
    protected float getModifiedAccelerationSpeed()
    {
        if(trailer != null)
        {
            if(trailer.getPassengerList().size() > 0)
            {
                return super.getModifiedAccelerationSpeed() * 0.5F;
            }
            else
            {
                return super.getModifiedAccelerationSpeed() * 0.8F;
            }
        }
        return super.getModifiedAccelerationSpeed();
    }

    @Override
    public float getModifiedRotationYaw()
    {
        return this.yaw - this.additionalYaw;
    }

    public boolean isRearWheelSteering()
    {
        VehicleProperties properties = this.getProperties();
        return properties.getFrontAxelVec() != null && properties.getRearAxelVec() != null && properties.getFrontAxelVec().z < properties.getRearAxelVec().z;
    }
}