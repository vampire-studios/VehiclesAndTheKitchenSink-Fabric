package io.github.vampirestudios.vks.entity;

import io.github.vampirestudios.vks.common.CustomDataParameters;
import io.github.vampirestudios.vks.common.SeatTracker;
import io.github.vampirestudios.vks.init.ModItems;
import io.github.vampirestudios.vks.init.ModSounds;
import io.github.vampirestudios.vks.utils.Constants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.packet.EntityAnimationS2CPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.UUID;

public class VehicleEntity extends Entity implements  {

    public static final int[] DYE_TO_COLOR = new int[] {16383998, 16351261, 13061821, 3847130, 16701501, 8439583, 15961002, 4673362, 10329495, 1481884, 8991416, 3949738, 8606770, 6192150, 11546150, 1908001};

    protected static final TrackedData<Integer> COLOR = DataTracker.registerData(VehicleEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> TIME_SINCE_HIT = DataTracker.registerData(VehicleEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Float> MAX_HEALTH = DataTracker.registerData(VehicleEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> HEALTH = DataTracker.registerData(VehicleEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Integer> TRAILER = DataTracker.registerData(VehicleEntity.class, TrackedDataHandlerRegistry.INTEGER);

    protected UUID trailerId;
    protected TrailerEntity trailer = null;
    private int searchDelay = 20;

    protected int lerpSteps;
    protected double lerpX;
    protected double lerpY;
    protected double lerpZ;
    protected double lerpYaw;
    protected double lerpPitch;

    protected SeatTracker seatTracker;

    public VehicleEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
        this.seatTracker = new SeatTracker(this);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(TIME_SINCE_HIT, 0);
        this.dataTracker.startTracking(MAX_HEALTH, 100F);
        this.dataTracker.startTracking(HEALTH, 100F);
        this.dataTracker.startTracking(COLOR, 16383998);
        this.dataTracker.startTracking(TRAILER, -1);

        if (world.isClient) {
            onClientInit();
        }

    }

    public void onClientInit() {}

    @Override
    public boolean interact(PlayerEntity player, Hand hand) {
        if(!world.isClient && !player.isSneaking())
        {
            int trailerId = player.getDataTracker().get(CustomDataParameters.TRAILER);
            if(trailerId != -1)
            {
                if(this.getVehicle() == null && this.canTowTrailer() && this.getTrailer() == null)
                {
                    Entity entity = world.getEntityById(trailerId);
                    if(entity instanceof TrailerEntity && entity != this)
                    {
                        TrailerEntity trailer = (TrailerEntity) entity;
                        this.setTrailer(trailer);
                        player.getDataTracker().set(CustomDataParameters.TRAILER, -1);
                    }
                }
                return true;
            }

            ItemStack heldItem = player.getStackInHand(hand);
            if(heldItem.getItem() instanceof SprayCanItem)
            {
                if(this.canBeColored())
                {
                    CompoundTag compound = heldItem.getTag();
                    if(compound != null)
                    {
                        int remainingSprays = compound.getInt("RemainingSprays");
                        if(compound.contains("Color", Constants.NBT.TAG_INT) && remainingSprays > 0)
                        {
                            int color = compound.getInt("Color");
                            if(this.getColor() != color)
                            {
                                this.setColor(compound.getInt("Color"));
                                player.world.playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.SPRAY_CAN_SPRAY, SoundCategory.PLAYERS, 1.0F, 1.0F);
                                compound.putInt("RemainingSprays", remainingSprays - 1);
                            }
                        }
                    }
                }
                return true;
            }
            else if(heldItem.getItem() == ModItems.HAMMER && this.getVehicle() instanceof EntityJack)
            {
                if(this.getHealth() < this.getMaxHealth())
                {
                    heldItem.damage(1, player, playerEntity -> player.sendToolBreakStatus(hand));
                    this.setHealth(this.getHealth() + 5F);
                    this.world.playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.VEHICLE_THUD, SoundCategory.PLAYERS, 1.0F, 0.8F + 0.4F * random.nextFloat());
                    player.swingHand(hand);
                    if(player instanceof ServerPlayerEntity)
                    {
                        ((ServerPlayerEntity) player).networkHandler.sendPacket(new EntityAnimationS2CPacket(player, hand == Hand.MAIN_HAND ? 0 : 3));
                    }
                    if(this.getHealth() == this.getMaxHealth())
                    {
                        if(world instanceof ServerWorld)
                        {
                            //TODO send as single packet instead of multiple
                            int count = (int) (50 * (this.getWidth() * this.getHeight()));
                            for(int i = 0; i < count; i++)
                            {
                                double width = this.getWidth() * 2;
                                double height = this.getHeight() * 1.5;

                                Vec3d heldOffset = this.getProperties().getHeldOffset().rotateY((float) Math.toRadians(-this.yaw));
                                double x = this.getX() + width * random.nextFloat() - width / 2 + heldOffset.z * 0.0625;
                                double y = this.getY() + height * random.nextFloat();
                                double z = this.getZ() + width * random.nextFloat() - width / 2 + heldOffset.x * 0.0625;

                                double d0 = random.nextGaussian() * 0.02D;
                                double d1 = random.nextGaussian() * 0.02D;
                                double d2 = random.nextGaussian() * 0.02D;
                                ((ServerWorld) this.world).spawnParticles(ParticleTypes.HAPPY_VILLAGER, x, y, z, 1, d0, d1, d2, 1.0);
                            }
                        }
                        this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1.0F, 1.5F);
                    }
                }
                return true;
            }
            else if(this.canAddPassenger(player))
            {
                int seatIndex = this.seatTracker.getClosestAvailableSeatToPlayer(player);
                if(seatIndex != -1)
                {
                    if(player.startRiding(this))
                    {
                        this.getSeatTracker().setSeatIndex(seatIndex, player.getUuid());
                    }
                }
                return true;
            }
        }
        return true;
    }

    @Override
    protected void readCustomDataFromTag(CompoundTag compoundTag) {
        if(compoundTag.contains("color", Constants.NBT.TAG_INT_ARRAY)) {
            int[] c = compoundTag.getIntArray("color");
            if(c.length == 3) {
                int color = ((c[0] & 0xFF) << 16) | ((c[1] & 0xFF) << 8) | ((c[2] & 0xFF));
                this.setColor(color);
            }
        }
        else if(compoundTag.contains("color", Constants.NBT.TAG_INT)) {
            int index = compoundTag.getInt("color");
            if(index >= 0 && index < DYE_TO_COLOR.length) {
                this.setColor(DYE_TO_COLOR[index]);
            }
            compoundTag.remove("color");
        }
        if(compoundTag.contains("maxHealth", Constants.NBT.TAG_FLOAT)){
            this.setMaxHealth(compoundTag.getFloat("maxHealth"));
        }
        if(compoundTag.contains("health", Constants.NBT.TAG_FLOAT)) {
            this.setHealth(compoundTag.getFloat("health"));
        }
        if(compoundTag.contains("trailer")) {
            this.trailerId = compoundTag.getUuid("trailer");
        }
    }

    @Override
    public Box getVisibilityBoundingBox() {
        return this.getBoundingBox().expand(1);
    }

    @Override
    protected void writeCustomDataToTag(CompoundTag compoundTag) {

    }

    @Override
    public Packet<?> createSpawnPacket() {
        return null;
    }

    /**
     * Sets the time to count down from since the last time entity was hit.
     */
    public void setTimeSinceHit(int timeSinceHit)
    {
        this.dataTracker.set(TIME_SINCE_HIT, timeSinceHit);
    }

    /**
     * Gets the time since the last hit.
     */
    public int getTimeSinceHit()
    {
        return this.dataTracker.get(TIME_SINCE_HIT);
    }

    /**
     * Sets the max health of the vehicle.
     */
    public void setMaxHealth(float maxHealth)
    {
        this.dataTracker.set(MAX_HEALTH, maxHealth);
    }

    /**
     * Gets the max health of the vehicle.
     */
    public float getMaxHealth()
    {
        return this.dataTracker.get(MAX_HEALTH);
    }

    /**
     * Sets the current health of the vehicle.
     */
    public void setHealth(float health)
    {
        this.dataTracker.set(HEALTH, Math.min(this.getMaxHealth(), health));
    }

    /**
     * Gets the current health of the vehicle.
     */
    public float getHealth()
    {
        return this.dataTracker.get(HEALTH);
    }

    //TODO look into this and why its here. May have to send vanilla event to client
    @Environment(EnvType.CLIENT)
    public void performHurtAnimation()
    {
        this.setTimeSinceHit(10);
    }

    public boolean canBeColored()
    {
        return false;
    }

    public void setColor(int color)
    {
        if(this.canBeColored())
        {
            this.dataTracker.set(COLOR, color);
        }
    }

    public void setColorRGB(int r, int g, int b)
    {
        int color = ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF));
        this.dataTracker.set(COLOR, color);
    }

    public int getColor()
    {
        return this.dataTracker.get(COLOR);
    }

    public int[] getColorRGB()
    {
        int color = this.dataTracker.get(COLOR);
        return new int[]{ (color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF };
    }

    public boolean canMountTrailer()
    {
        return true;
    }

    public boolean canTowTrailer()
    {
        return false;
    }

    public VehicleProperties getProperties()
    {
        return VehicleProperties.getProperties(this.getType());
    }

    public SeatTracker getSeatTracker()
    {
        return this.seatTracker;
    }


}
