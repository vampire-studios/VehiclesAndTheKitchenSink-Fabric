package io.github.vampirestudios.vks.entity;

import io.github.vampirestudios.vks.init.ModItems;
import io.github.vampirestudios.vks.utils.Constants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.world.World;

import java.util.UUID;

public class VehicleEntity extends Entity {

    public static final int[] DYE_TO_COLOR = new int[] {16383998, 16351261, 13061821, 3847130, 16701501, 8439583, 15961002, 4673362, 10329495, 1481884, 8991416, 3949738, 8606770, 6192150, 11546150, 1908001};

    protected static final TrackedData<Integer> COLOR = DataTracker.registerData(VehicleEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> TIME_SINCE_HIT = DataTracker.registerData(VehicleEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Float> MAX_HEALTH = DataTracker.registerData(VehicleEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> HEALTH = DataTracker.registerData(VehicleEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Integer> TRAILER = DataTracker.registerData(VehicleEntity.class, TrackedDataHandlerRegistry.INTEGER);

    protected UUID trailerId;
//    protected EntityTrailer trailer = null;
    private int searchDelay = 20;

    /**
     * ItemStack instances used for rendering
     */
    @Environment(EnvType.CLIENT)
    public ItemStack body, wheel;

    @Environment(EnvType.CLIENT)
    public ItemStack towBar;

    protected int lerpSteps;
    protected double lerpX;
    protected double lerpY;
    protected double lerpZ;
    protected double lerpYaw;
    protected double lerpPitch;

    public VehicleEntity(EntityType<?> entityType_1, World world_1) {
        super(entityType_1, world_1);
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

    public void onClientInit() {
        towBar = new ItemStack(ModItems.TOW_BAR);
        wheel = new ItemStack(ModItems.STANDARD_WHEEL, 1);
    }

    @Override
    protected void readCustomDataFromTag(CompoundTag compoundTag) {
        if(compoundTag.containsKey("color", Constants.NBT.TAG_INT_ARRAY)) {
            int[] c = compoundTag.getIntArray("color");
            if(c.length == 3) {
                int color = ((c[0] & 0xFF) << 16) | ((c[1] & 0xFF) << 8) | ((c[2] & 0xFF));
                this.setColor(color);
            }
        }
        else if(compoundTag.containsKey("color", Constants.NBT.TAG_INT)) {
            int index = compoundTag.getInt("color");
            if(index >= 0 && index < DYE_TO_COLOR.length) {
                this.setColor(DYE_TO_COLOR[index]);
            }
            compoundTag.remove("color");
        }
        if(compoundTag.containsKey("maxHealth", Constants.NBT.TAG_FLOAT)){
            this.setMaxHealth(compoundTag.getFloat("maxHealth"));
        }
        if(compoundTag.containsKey("health", Constants.NBT.TAG_FLOAT)) {
            this.setHealth(compoundTag.getFloat("health"));
        }
        if(compoundTag.hasUuid("trailer")) {
            this.trailerId = compoundTag.getUuid("trailer");
        }
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


}
