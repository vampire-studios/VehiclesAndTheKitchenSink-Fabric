package io.github.vampirestudios.vks.entity;

import io.github.vampirestudios.vks.utils.Constants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
public abstract class PlaneEntity extends PoweredVehicleEntity {
    //TODO Create own data parameter system if problems continue to occur
    private static final TrackedData<Integer> FLAP_DIRECTION = DataTracker.registerData(PlaneEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Float> LIFT = DataTracker.registerData(PlaneEntity.class, TrackedDataHandlerRegistry.FLOAT);

    private float lift;

    public float prevBodyRotationX;
    public float prevBodyRotationY;
    public float prevBodyRotationZ;

    public float bodyRotationX;
    public float bodyRotationY;
    public float bodyRotationZ;

    protected PlaneEntity(EntityType<?> entityType, World worldIn)
    {
        super(entityType, worldIn);
        this.setAccelerationSpeed(0.5F);
        this.setMaxSpeed(25F);
        this.setTurnSensitivity(5);
    }

    @Override
    public void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(FLAP_DIRECTION, FlapDirection.NONE.ordinal());
        this.dataTracker.startTracking(LIFT, 0F);
    }

    @Override
    public void updateVehicleMotion() {
        float f1 = MathHelper.sin(this.yaw * 0.017453292F) / 20F; //Divide by 20 ticks
        float f2 = MathHelper.cos(this.yaw * 0.017453292F) / 20F;

        this.updateLift();

        this.vehicleMotionX = (-this.currentSpeed * f1);
        this.vehicleMotionZ = (this.currentSpeed * f2);
        this.setVelocity(this.getVelocity().add(0, this.lift - 0.05, 0));
    }

    @Override
    public void onClientUpdate() {
        super.onClientUpdate();

        this.prevBodyRotationX = this.bodyRotationX;
        this.prevBodyRotationY = this.bodyRotationY;
        this.prevBodyRotationZ = this.bodyRotationZ;

        LivingEntity entity = (LivingEntity) this.getControllingPassenger();
        if(entity != null && entity.equals(MinecraftClient.getInstance().player)) {
            /*FlapDirection flapDirection = VehiclesAndTheKitchenSink.getFlapDirection();
            if(this.getFlapDirection() != flapDirection)
            {
                this.setFlapDirection(flapDirection);
                PacketHandler.instance.sendToServer(new MessageFlaps(flapDirection));
            }*/
        }

        if(this.canAccelerateInAir()) {
            this.bodyRotationX = (float) Math.toDegrees(Math.atan2(this.getVelocity().getY(), currentSpeed / 20F));
            this.bodyRotationZ = (this.turnAngle / (float) getMaxTurnAngle()) * 20F;
        } else {
            this.bodyRotationX *= 0.5F;
            this.bodyRotationZ *= 0.5F;
        }
    }

    @Override
    protected void updateSpeed() {
        lift = 0;
        currentSpeed = this.getSpeed();

        if(this.getControllingPassenger() != null)
        {
            AccelerationDirection acceleration = getAcceleration();
            if(this.canDrive() && acceleration == AccelerationDirection.FORWARD)
            {
                if(this.getVelocity().getY() < 0)
                {
                    this.setVelocity(this.getVelocity().multiply(1.0, 0.95, 1.0));
                }

                EngineTier engineTier = this.getEngineTier();
                float accelerationSpeed = this.getModifiedAccelerationSpeed() * engineTier.getAccelerationMultiplier();
                if(this.currentSpeed < this.getActualMaxSpeed())
                {
                    this.currentSpeed += accelerationSpeed;
                }
                this.lift = 0.051F * (Math.min(currentSpeed, 15F) / 15F);
            }
            else if(acceleration == AccelerationDirection.REVERSE)
            {
                if(this.isFlying())
                {
                    this.currentSpeed *= 0.95F;
                }
                else
                {
                    this.currentSpeed *= 0.9F;
                }
            }

            if(acceleration != AccelerationDirection.FORWARD)
            {
                if(this.isFlying())
                {
                    this.currentSpeed *= 0.995F;
                }
                else
                {
                    this.currentSpeed *= 0.98F;
                }
                this.lift = 0.04F * (Math.min(currentSpeed, 15F) / 15F);
            }
        }
        else
        {
            if(this.isFlying())
            {
                this.currentSpeed *= 0.98F;
            }
            else
            {
                this.currentSpeed *= 0.85F;
            }
        }
    }

    @Override
    protected void updateTurning()
    {
        TurnDirection direction = this.getTurnDirection();
        if(this.getControllingPassenger() != null && direction != TurnDirection.FORWARD)
        {
            this.turnAngle += direction.dir * getTurnSensitivity();
            if(Math.abs(this.turnAngle) > getMaxTurnAngle())
            {
                this.turnAngle = getMaxTurnAngle() * direction.dir;
            }
        }
        else
        {
            this.turnAngle *= 0.95;
        }

        if(this.isFlying())
        {
            this.wheelAngle = this.turnAngle * Math.max(0.25F, 1.0F - Math.abs(Math.min(currentSpeed, 30F) / 30F));
        }
        else
        {
            this.wheelAngle = this.turnAngle * Math.abs(Math.min(currentSpeed, 30F) / 30F);
        }

        this.deltaYaw = this.wheelAngle;

        if(this.isFlying())
        {
            this.deltaYaw *= 0.5;
        }
        else
        {
            this.deltaYaw *= 0.5 * (0.5 + 0.5 * (1.0F - Math.min(currentSpeed, 15F) / 15F));
        }
    }

    public void updateLift()
    {
        FlapDirection flapDirection = getFlapDirection();
        if(flapDirection == FlapDirection.UP)
        {
            this.lift += 0.04F * (Math.min(Math.max(currentSpeed - 5F, 0F), 15F) / 15F);
        }
        else if(flapDirection == FlapDirection.DOWN)
        {
            this.lift -= 0.06F * (Math.min(currentSpeed, 15F) / 15F);
        }
        this.setLift(this.lift);
    }

    @Override
    public void writeCustomDataToTag(CompoundTag compound) {
        super.writeCustomDataToTag(compound);
        compound.putInt("FlapDirection", this.getFlapDirection().ordinal());
        compound.putFloat("Lift", this.getLift());
    }

    @Override
    public void readCustomDataFromTag(CompoundTag compound) {
        super.readCustomDataFromTag(compound);
        if(compound.contains("FlapDirection", Constants.NBT.TAG_INT))
        {
            this.setFlapDirection(FlapDirection.values()[compound.getInt("FlapDirection")]);
        }
        if(compound.contains("Lift", Constants.NBT.TAG_FLOAT))
        {
            this.setLift(compound.getFloat("Lift"));
        }
    }

    public void setFlapDirection(FlapDirection flapDirection)
    {
        this.dataTracker.set(FLAP_DIRECTION, flapDirection.ordinal());
    }

    public FlapDirection getFlapDirection()
    {
        return FlapDirection.values()[this.dataTracker.get(FLAP_DIRECTION)];
    }

    public float getLift()
    {
        return this.dataTracker.get(LIFT);
    }

    public void setLift(float lift)
    {
        this.dataTracker.set(LIFT, lift);
    }

    public boolean isFlying()
    {
        return !this.onGround;
    }

    /*
     * Overridden to prevent players from taking fall damage when landing a plane
     */
    /*@Override
    public boolean func_225503_b_(float distance, float damageMultiplier)
    {
        return false;
    }*/

    @Override
    public boolean canChangeWheels()
    {
        return false;
    }

    public enum FlapDirection
    {
        UP, DOWN, NONE;

        public static FlapDirection fromInput(boolean up, boolean down)
        {
            return up && !down ? UP : down && !up ? DOWN : NONE;
        }
    }
}