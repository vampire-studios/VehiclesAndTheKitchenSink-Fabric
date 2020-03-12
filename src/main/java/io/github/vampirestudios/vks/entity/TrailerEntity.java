package io.github.vampirestudios.vks.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public abstract class TrailerEntity extends VehicleEntity
{
    public static final TrackedData<Integer> PULLING_ENTITY = DataTracker.registerData(TrailerEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private Entity pullingEntity;

    public float wheelRotation;
    public float prevWheelRotation;

    public TrailerEntity(EntityType<?> entityType, World worldIn)
    {
        super(entityType, worldIn);
        this.stepHeight = 1.0F;
    }

    @Override
    protected void initDataTracker()
    {
        super.initDataTracker();
        this.dataTracker.startTracking(PULLING_ENTITY, -1);
    }

    @Override
    public boolean collides()
    {
        return true;
    }

    @Override
    public void onUpdateVehicle() {
        this.prevWheelRotation = this.wheelRotation;

        Vec3d motion = this.getVelocity();
        this.setVelocity(motion.getX(), motion.getY() - 0.08, motion.getZ());

        if(this.pullingEntity != null && !this.world.isClient) {
            if(this.pullingEntity.distanceTo(this) > 6.0) {
                this.world.playSound(null, this.pullingEntity.getBlockPos(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1.0F, 1.0F);
                this.pullingEntity = null;
                return;
            }
        }

        if(this.pullingEntity != null)
        {
            if(!this.pullingEntity.isAlive() || (this.pullingEntity instanceof VehicleEntity && ((VehicleEntity) this.pullingEntity).getTrailer() != this))
            {
                this.pullingEntity = null;
                return;
            }
            this.updatePullingMotion();
        }
        else if(!world.isClient)
        {
            motion = this.getVelocity();
            this.move(MovementType.SELF, new Vec3d(motion.getX() * 0.75, motion.getY(), motion.getZ() * 0.75));
        }

        this.checkBlockCollision();

        float speed = (float) (Math.sqrt(Math.pow(this.getX() - this.prevX, 2) + Math.pow(this.getY() - this.prevY, 2) + Math.pow(this.getZ() - this.prevZ, 2)) * 20);
        wheelRotation -= 90F * (speed / 10F);
    }

    private void updatePullingMotion()
    {
        Vec3d towBar = pullingEntity.getPosVector();
        if(pullingEntity instanceof VehicleEntity)
        {
            VehicleEntity vehicle = (VehicleEntity) pullingEntity;
            Vec3d towBarVec = vehicle.getProperties().getTowBarPosition();
            towBarVec = new Vec3d(towBarVec.x * 0.0625, towBarVec.y * 0.0625, towBarVec.z * 0.0625 + vehicle.getProperties().getBodyPosition().getZ());
            /*if(vehicle instanceof LandVehicleEntity)
            {
                LandVehicleEntity landVehicle = (LandVehicleEntity) vehicle;
                towBar = towBar.add(towBarVec.rotateY((float) Math.toRadians(-vehicle.yaw + landVehicle.additionalYaw)));
            }
            else
            {
                towBar = towBar.add(towBarVec.rotateY((float) Math.toRadians(-vehicle.yaw)));
            }*/
            towBar = towBar.add(towBarVec.rotateY((float) Math.toRadians(-vehicle.yaw)));
        }

        this.yaw = (float) Math.toDegrees(Math.atan2(towBar.z - this.getZ(), towBar.x - this.getX()) - Math.toRadians(90F));
        double deltaRot = this.prevYaw - this.yaw;
        if (deltaRot < -180.0D)
        {
            this.prevYaw += 360.0F;
        }
        else if (deltaRot >= 180.0D)
        {
            this.prevYaw -= 360.0F;
        }

        Vec3d vec = new Vec3d(0, 0, this.getHitchOffset() * 0.0625).rotateY((float) Math.toRadians(-this.yaw)).add(towBar);
        Vec3d motion = this.getVelocity();
        this.setVelocity(vec.x - this.getX(), motion.getY(), vec.z - this.getZ());
        this.move(MovementType.SELF, this.getVelocity());
    }

    @Override
    public double getMountedHeightOffset()
    {
        return 0.0;
    }

    public boolean setPullingEntity(Entity pullingEntity)
    {
        if(pullingEntity instanceof PlayerEntity || (pullingEntity instanceof VehicleEntity && pullingEntity.getVehicle() == null && ((VehicleEntity) pullingEntity).canTowTrailer()))
        {
            this.pullingEntity = pullingEntity;
            this.dataTracker.set(PULLING_ENTITY, pullingEntity.getEntityId());
            return true;
        }
        else
        {
            this.pullingEntity = null;
            this.dataTracker.set(PULLING_ENTITY, -1);
            return false;
        }
    }

    @Nullable
    public Entity getPullingEntity()
    {
        return pullingEntity;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport)
    {
        this.lerpX = x;
        this.lerpY = y;
        this.lerpZ = z;
        this.lerpYaw = yaw;
        this.lerpPitch = pitch;
        this.lerpSteps = 1;
    }

    @Override
    public boolean canMountTrailer()
    {
        return false;
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> key)
    {
        super.onTrackedDataSet(key);
        if(world.isClient)
        {
            if(PULLING_ENTITY.equals(key))
            {
                int entityId = this.dataTracker.get(PULLING_ENTITY);
                if(entityId != -1)
                {
                    Entity entity = world.getEntityById(this.dataTracker.get(PULLING_ENTITY));
                    if(entity instanceof PlayerEntity || (entity instanceof VehicleEntity && ((VehicleEntity) entity).canTowTrailer()))
                    {
                        pullingEntity = entity;
                    }
                    else
                    {
                        pullingEntity = null;
                    }
                }
                else
                {
                    pullingEntity = null;
                }
            }
        }
    }

    public abstract double getHitchOffset();

    @Override
    protected boolean canAddPassenger(Entity entityIn)
    {
        return false;
    }
}