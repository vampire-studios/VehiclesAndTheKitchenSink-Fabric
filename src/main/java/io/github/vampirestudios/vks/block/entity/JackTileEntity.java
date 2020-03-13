package io.github.vampirestudios.vks.block.entity;

import io.github.vampirestudios.vks.block.BlockJack;
import io.github.vampirestudios.vks.entity.EntityJack;
import io.github.vampirestudios.vks.entity.VehicleEntity;
import io.github.vampirestudios.vks.init.ModSounds;
import io.github.vampirestudios.vks.init.ModTileEntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class JackTileEntity extends TileEntitySynced implements Tickable {
    public static final int MAX_LIFT_PROGRESS = 20;

    private EntityJack jack = null;

    private boolean activated = false;
    public int prevLiftProgress;
    public int liftProgress;

    public JackTileEntity()
    {
        super(ModTileEntities.JACK);
    }

    public void setVehicle(VehicleEntity vehicle) {
        this.jack = new EntityJack(this.world, this.pos, 9 * 0.0625, vehicle.yaw);
        vehicle.startRiding(this.jack, true);
        this.jack.tickRiding();
        this.world.spawnEntity(this.jack);
    }

    @Nullable
    public EntityJack getJack()
    {
        return this.jack;
    }

    @Override
    public void tick()
    {
        this.prevLiftProgress = this.liftProgress;

        if(this.jack == null)
        {
            List<EntityJack> jacks = this.world.getNonSpectatingEntities(EntityJack.class, new Box(this.pos));
            if(jacks.size() > 0)
            {
                this.jack = jacks.get(0);
            }
        }

        if(this.jack != null && (this.jack.getPassengerList().isEmpty() || !this.jack.isAlive()))
        {
            this.jack = null;
        }

        if(this.jack != null)
        {
            if(this.jack.getPassengerList().size() > 0)
            {
                if(!this.activated)
                {
                    this.world.playSound(null, this.pos, ModSounds.JACK_UP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    this.activated = true;
                }
            }
            else if(this.activated)
            {
                this.world.playSound(null, this.pos, ModSounds.JACK_DOWN, SoundCategory.BLOCKS, 1.0F, 1.0F);
                this.activated = false;
            }
        }
        else if(this.activated)
        {
            this.world.playSound(null, this.pos, ModSounds.JACK_DOWN, SoundCategory.BLOCKS, 1.0F, 1.0F);
            this.activated = false;
        }

        if(this.activated)
        {
            if(this.liftProgress < MAX_LIFT_PROGRESS)
            {
                this.liftProgress++;
                this.moveCollidedEntities();
            }
        }
        else if(liftProgress > 0)
        {
            this.liftProgress--;
            this.moveCollidedEntities();
        }
    }

    private void moveCollidedEntities()
    {
        BlockState state = this.world.getBlockState(this.getPos());
        if(state.getBlock() instanceof BlockJack)
        {
            Box boundingBox = state.getCollisionShape(this.world, this.pos).getBoundingBox().offset(this.pos);
            List<Entity> list = this.world.getEntities(this.jack, boundingBox);
            if(!list.isEmpty())
            {
                for(Entity entity : list)
                {
                    if(entity.getPistonBehavior() != PistonBehavior.IGNORE)
                    {
                        Box entityBoundingBox = entity.getBoundingBox();
                        double posY = boundingBox.y2 - entityBoundingBox.y1;
                        entity.move(MovementType.PISTON, new Vec3d(0.0, posY, 0.0));
                    }
                }
            }
        }
    }

    public float getProgress()
    {
        return (float) this.liftProgress / (float) MAX_LIFT_PROGRESS;
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