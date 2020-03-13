package io.github.vampirestudios.vks.entity.vehicles;

import io.github.vampirestudios.vks.entity.EngineType;
import io.github.vampirestudios.vks.entity.LandVehicleEntity;
import io.github.vampirestudios.vks.init.ModEntities;
import io.github.vampirestudios.vks.init.ModSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PlantBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

/**
 * Author: MrCrayfish
 */
public class LawnMowerEntity extends LandVehicleEntity/* implements IEntityRaytraceable*/ {
    public LawnMowerEntity(World worldIn) {
        super(ModEntities.LAWN_MOWER, worldIn);
        this.setMaxSpeed(8);
        this.setFuelCapacity(5000F);
    }

    @Override
    public void updateVehicle() {
        super.updateVehicle();

        if(!world.isClient && this.getControllingPassenger() != null)
        {
            Box axisAligned = this.getBoundingBox().expand(0.25);
            Vec3d lookVec = this.getRotationVector().multiply(0.5);
            int minX = MathHelper.floor(axisAligned.x1 + lookVec.x);
            int maxX = MathHelper.ceil(axisAligned.x2 + lookVec.x);
            int minZ = MathHelper.floor(axisAligned.z1 + lookVec.z);
            int maxZ = MathHelper.ceil(axisAligned.z2 + lookVec.z);

            for(int x = minX; x < maxX; x++)
            {
                for(int z = minZ; z < maxZ; z++)
                {
                    BlockPos pos = new BlockPos(x, axisAligned.y1 + 0.5, z);
                    BlockState state = world.getBlockState(pos);

                    /*StorageTrailerEntity trailer = null;
                    if(getTrailer() instanceof StorageTrailerEntity)
                    {
                        trailer = (StorageTrailerEntity) getTrailer();
                    }*/

                    if(state.getBlock() instanceof PlantBlock) {
                        List<ItemStack> drops = Block.getDroppedStacks(state, (ServerWorld) world, pos, null);
                        for(ItemStack stack : drops) {
                            this.spawnItemStack(world, stack);
//                            this.addItemToStorage(trailer, stack);
                        }
                        world.setBlockState(pos, Blocks.AIR.getDefaultState());
                        world.playSound(null, pos, state.getBlock().getSoundGroup(state).getBreakSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                        world.playLevelEvent(2001, pos, Block.getRawIdFromState(state));
                    }
                }
            }
        }
    }

    /*private void addItemToStorage(StorageTrailerEntity storageTrailer, ItemStack stack)
    {
        if(stack.isEmpty())
            return;

        if(storageTrailer != null && storageTrailer.getInventory() != null)
        {
            Inventory storage = storageTrailer.getInventory();
            stack = storage.addItem(stack);
            if(!stack.isEmpty())
            {
                if(storageTrailer.getTrailer() instanceof StorageTrailerEntity)
                {
                    this.addItemToStorage((StorageTrailerEntity) storageTrailer.getTrailer(), stack);
                }
                else
                {
                    spawnItemStack(world, stack);
                }
            }
        }
        else
        {
            spawnItemStack(world, stack);
        }
    }*/

    private void spawnItemStack(World worldIn, ItemStack stack)
    {
        while(!stack.isEmpty())
        {
            ItemEntity itemEntity = new ItemEntity(worldIn, prevX, prevY, prevZ, stack.split(random.nextInt(21) + 10));
            itemEntity.setPickupDelay(20);
            itemEntity.setVelocity(-this.getVelocity().x / 4.0, random.nextGaussian() * 0.05D + 0.2D, -this.getVelocity().z / 4.0);
            worldIn.spawnEntity(itemEntity);
        }
    }

    @Override
    public SoundEvent getMovingSound()
    {
        return ModSounds.ATV_ENGINE_MONO;
    }

    @Override
    public SoundEvent getRidingSound()
    {
        return ModSounds.ATV_ENGINE_STEREO;
    }

    @Override
    public EngineType getEngineType()
    {
        return EngineType.SMALL_MOTOR;
    }

    @Override
    public boolean canBeColored()
    {
        return true;
    }

    @Override
    public boolean canTowTrailer()
    {
        return true;
    }

    //TODO remove and add key support
    @Override
    public boolean isLockable()
    {
        return false;
    }
}