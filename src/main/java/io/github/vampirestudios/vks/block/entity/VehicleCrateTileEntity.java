package io.github.vampirestudios.vks.block.entity;

import io.github.vampirestudios.vks.block.BlockVehicleCrate;
import io.github.vampirestudios.vks.entity.EngineTier;
import io.github.vampirestudios.vks.entity.PoweredVehicleEntity;
import io.github.vampirestudios.vks.entity.VehicleEntity;
import io.github.vampirestudios.vks.entity.WheelType;
import io.github.vampirestudios.vks.init.ModTileEntities;
import io.github.vampirestudios.vks.utils.Constants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Author: MrCrayfish
 */
public class VehicleCrateTileEntity extends TileEntitySynced implements Tickable {
    private static final Random RAND = new Random();

    private Identifier entityId;
    private int color = VehicleEntity.DYE_TO_COLOR[0];
    private EngineTier engineTier = null;
    private WheelType wheelType = null;
    private int wheelColor = -1;
    private boolean opened = false;
    private int timer;
    private UUID opener;

    @Environment(EnvType.CLIENT)
    private Entity entity;

    public VehicleCrateTileEntity()
    {
        super(ModTileEntities.VEHICLE_CRATE);
    }

    public void setEntityId(Identifier entityId) {
        this.entityId = entityId;
        this.markDirty();
    }

    public Identifier getEntityId()
    {
        return entityId;
    }

    public void open(UUID opener)
    {
        if(this.entityId != null)
        {
            this.opened = true;
            this.opener = opener;
            this.syncToClient();
        }
    }

    public boolean isOpened()
    {
        return opened;
    }

    public int getTimer()
    {
        return timer;
    }

    @Environment(EnvType.CLIENT)
    public <E extends Entity> E getEntity()
    {
        return (E) entity;
    }

    @Override
    public void tick()
    {
        if(this.opened)
        {
            this.timer += 5;
            if(this.world.isClient)
            {
                if(this.entityId != null && this.entity == null)
                {
                    EntityType<?> entityType = Registry.ENTITY_TYPE.get(this.entityId);
                    if(entityType != null)
                    {
                        this.entity = entityType.create(this.world);
                        if(this.entity != null)
                        {
//                            VehicleMod.PROXY.playSound(SoundEvents.ENTITY_ITEM_BREAK, this.pos, 1.0F, 0.5F);
                            List<DataTracker.Entry<?>> entryList = this.entity.getDataTracker().getAllEntries();
                            if(entryList != null)
                            {
                                entryList.forEach(dataEntry -> this.entity.onTrackedDataSet(dataEntry.getData()));
                            }
                            if(this.entity instanceof VehicleEntity)
                            {
                                ((VehicleEntity) this.entity).setColor(this.color);
                            }
                            if(this.entity instanceof PoweredVehicleEntity)
                            {
                                PoweredVehicleEntity entityPoweredVehicle = (PoweredVehicleEntity) this.entity;
                                if(this.engineTier != null)
                                {
                                    entityPoweredVehicle.setEngine(true);
                                    entityPoweredVehicle.setEngineTier(this.engineTier);
                                }
                                if(this.wheelType != null)
                                {
                                    entityPoweredVehicle.setWheels(true);
                                    entityPoweredVehicle.setWheelType(this.wheelType);
                                    if(this.wheelColor != -1)
                                    {
                                        entityPoweredVehicle.setWheelColor(this.wheelColor);
                                    }
                                }
                                else
                                {
                                    entityPoweredVehicle.setWheels(false);
                                }
                            }
                        }
                        else
                        {
                            this.entityId = null;
                        }
                    }
                    else
                    {
                        this.entityId = null;
                    }
                }
                if(this.timer == 90 || this.timer == 110 || this.timer == 130 || this.timer == 150)
                {
                    float pitch = (float) (0.9F + 0.2F * RAND.nextDouble());
//                    VehicleMod.PROXY.playSound(ModSounds.VEHICLE_CRATE_PANEL_LAND, this.pos, 1.0F, pitch);
                }
                if(this.timer == 150)
                {
//                    VehicleMod.PROXY.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, this.pos, 1.0F, 1.0F);
                    this.world.addParticle(ParticleTypes.EXPLOSION_EMITTER, false, this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5, 0, 0, 0);
                }
            }
            if(!this.world.isClient && this.timer > 250)
            {
                BlockState state = this.world.getBlockState(this.pos);
                Direction facing = state.get(BlockVehicleCrate.DIRECTION);
                EntityType<?> entityType = Registry.ENTITY_TYPE.get(this.entityId);
                if(entityType != null)
                {
                    Entity entity = entityType.create(this.world);
                    if(entity != null)
                    {
                        if(entity instanceof VehicleEntity)
                        {
                            ((VehicleEntity) entity).setColor(this.color);
                        }
                        if(this.opener != null && entity instanceof PoweredVehicleEntity)
                        {
                            PoweredVehicleEntity poweredVehicle = (PoweredVehicleEntity) entity;
                            poweredVehicle.setOwner(this.opener);
                            if(this.engineTier != null)
                            {
                                poweredVehicle.setEngine(true);
                                poweredVehicle.setEngineTier(this.engineTier);
                            }
                            if(this.wheelType != null)
                            {
                                poweredVehicle.setWheelType(this.wheelType);
                                if(this.wheelColor != -1)
                                {
                                    poweredVehicle.setWheelColor(this.wheelColor);
                                }
                            }
                        }
                        entity.refreshPositionAndAngles(this.pos.getX() + 0.5, this.pos.getY(), this.pos.getZ() + 0.5, facing.getHorizontal() * 90F + 180F, 0F);
                        entity.setHeadYaw(facing.getHorizontal() * 90F + 180F);
                        this.world.spawnEntity(entity);
                    }
                    this.world.setBlockState(this.pos, Blocks.AIR.getDefaultState());
                }
            }
        }
    }

    @Override
    public void fromTag(CompoundTag compound)
    {
        super.fromTag(compound);
        if(compound.contains("Vehicle", Constants.NBT.TAG_STRING))
        {
            this.entityId = new Identifier(compound.getString("Vehicle"));
        }
        if(compound.contains("Color", Constants.NBT.TAG_INT))
        {
            this.color = compound.getInt("Color");
        }
        if(compound.contains("EngineTier", Constants.NBT.TAG_INT))
        {
            this.engineTier = EngineTier.getType(compound.getInt("EngineTier"));
        }
        if(compound.contains("WheelType", Constants.NBT.TAG_INT))
        {
            this.wheelType = WheelType.getType(compound.getString("WheelType"));
        }
        if(compound.contains("WheelColor", Constants.NBT.TAG_INT))
        {
            this.wheelColor = compound.getInt("WheelColor");
        }
        if(compound.contains("Opener", Constants.NBT.TAG_STRING))
        {
            this.opener = compound.getUuid("Opener");
        }
        if(compound.contains("Opened", Constants.NBT.TAG_BYTE))
        {
            this.opened = compound.getBoolean("Opened");
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag compound)
    {
        if(this.entityId != null)
        {
            compound.putString("Vehicle", this.entityId.toString());
        }
        if(this.opener != null)
        {
            compound.putUuid("Opener", this.opener);
        }
        if(this.engineTier != null)
        {
            compound.putInt("EngineTier", this.engineTier.ordinal());
        }
        if(this.wheelType != null)
        {
            compound.putString("WheelType", this.wheelType.getId());
            if(this.wheelColor != -1)
            {
                compound.putInt("WheelColor", this.wheelColor);
            }
        }
        compound.putInt("Color", this.color);
        compound.putBoolean("Opened", this.opened);
        return super.toTag(compound);
    }

    /*@Override
    public Box getRenderBoundingBox()
    {
        return INFINITE_EXTENT_AABB;
    }*/

    @Override
    @Environment(EnvType.CLIENT)
    public double getSquaredRenderDistance()
    {
        return 65536.0D;
    }
}