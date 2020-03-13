package io.github.vampirestudios.vks.entity;

import io.github.vampirestudios.vks.init.ModEntities;
import io.github.vampirestudios.vks.utils.S2CEntitySpawnPacket;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
public class EntityJack extends Entity/* implements IEntityAdditionalSpawnData*/ {
    private double initialX;
    private double initialY;
    private double initialZ;
    private boolean activated = false;
    private int liftProgress;

    public EntityJack(World worldIn) {
        super(ModEntities.JACK, worldIn);
        this.setNoGravity(true);
        this.noClip = true;
    }

    public EntityJack(World worldIn, BlockPos pos, double yOffset, float yaw) {
        this(worldIn);
        this.setPos(pos.getX() + 0.5, pos.getY() + yOffset, pos.getZ() + 0.5);
        this.setRotation(yaw, 0F);
        this.initialX = pos.getX() + 0.5;
        this.initialY = pos.getY() + yOffset;
        this.initialZ = pos.getZ() + 0.5;
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    public void tick() {
        super.tick();

        if(!world.isClient && this.getPassengerList().size() == 0) {
            this.remove();
        }

        if(!this.isAlive())
            return;

        if(!this.activated && this.getPassengerList().size() > 0) {
            this.activated = true;
        }

        if(this.activated) {
            if(this.liftProgress < 10) {
                this.liftProgress++;
            }
        } else if(this.liftProgress > 0) {
            this.liftProgress--;
        }

        /*BlockEntity tileEntity = this.world.getBlockEntity(new BlockPos(this.initialX, this.initialY, this.initialZ));
        if(tileEntity instanceof JackTileEntity) {
            JackTileEntity jackTileEntity = (JackTileEntity) tileEntity;
            this.setPos(this.initialX, this.initialY + 0.5 * (jackTileEntity.liftProgress / (double) JackTileEntity.MAX_LIFT_PROGRESS), this.initialZ);
        }*/
    }

    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        if(this.getPassengerList().contains(passenger)) {
            passenger.prevX = this.getX();
            passenger.prevY = this.getY();
            passenger.prevZ = this.getZ();
            passenger.lastRenderX = this.getX();
            passenger.lastRenderY = this.getY();
            passenger.lastRenderZ = this.getZ();
        }
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return S2CEntitySpawnPacket.createPacket(this);
    }

    @Override
    public void updatePassengerPosition(Entity passenger) {
        if(passenger instanceof VehicleEntity) {
            VehicleEntity vehicle = (VehicleEntity) passenger;
            Vec3d heldOffset = vehicle.getProperties().getHeldOffset().rotateY(passenger.yaw * 0.017453292F);
            vehicle.setPos(this.getX() - heldOffset.z * 0.0625, this.getY() - heldOffset.y * 0.0625 - 2 * 0.0625, this.getZ() - heldOffset.x * 0.0625);
        }
    }

    @Override
    protected void readCustomDataFromTag(CompoundTag compound) {
        this.initialX = compound.getDouble("initialX");
        this.initialY = compound.getDouble("initialY");
        this.initialZ = compound.getDouble("initialZ");
    }

    @Override
    protected void writeCustomDataToTag(CompoundTag compound) {
        compound.putDouble("initialX", this.initialX);
        compound.putDouble("initialY", this.initialY);
        compound.putDouble("initialZ", this.initialZ);
    }

    /*@Override
    public void writeSpawnData(PacketBuffer buffer)
    {
        buffer.writeDouble(this.initialX);
        buffer.writeDouble(this.initialY);
        buffer.writeDouble(this.initialZ);
    }

    @Override
    public void readSpawnData(PacketBuffer buffer)
    {
        this.initialX = buffer.readDouble();
        this.initialY = buffer.readDouble();
        this.initialZ = buffer.readDouble();
        this.setLocationAndAngles(this.initialX, this.initialY, this.initialZ, this.rotationYaw, this.rotationPitch);
        this.prevPosX = this.initialX;
        this.prevPosY = this.initialY;
        this.prevPosZ = this.initialZ;
        this.lastTickPosX = this.initialX;
        this.lastTickPosY = this.initialY;
        this.lastTickPosZ = this.initialZ;
    }*/
}