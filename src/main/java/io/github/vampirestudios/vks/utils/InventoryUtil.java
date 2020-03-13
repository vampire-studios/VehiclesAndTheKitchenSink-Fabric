package io.github.vampirestudios.vks.utils;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Author: MrCrayfish
 */
public class InventoryUtil
{
    private static final Random RANDOM = new Random();

    public static void writeInventoryToNBT(CompoundTag compound, String tagName, Inventory inventory)
    {
        ListTag tagList = new ListTag();
        for(int i = 0; i < inventory.getInvSize(); i++)
        {
            ItemStack stack = inventory.getInvStack(i);
            if(!stack.isEmpty())
            {
                CompoundTag stackTag = new CompoundTag();
                stackTag.putByte("Slot", (byte) i);
                stack.toTag(stackTag);
                tagList.add(stackTag);
            }
        }
        compound.put(tagName, tagList);
    }

    public static <T extends Inventory> T readInventoryToNBT(CompoundTag compound, String tagName, T t)
    {
        if(compound.contains(tagName, Constants.NBT.TAG_LIST))
        {
            ListTag tagList = compound.getList(tagName, Constants.NBT.TAG_COMPOUND);
            for(int i = 0; i < tagList.size(); i++)
            {
                CompoundTag tagCompound = tagList.getCompound(i);
                byte slot = tagCompound.getByte("Slot");
                if(slot >= 0 && slot < t.getInvSize())
                {
                    t.setInvStack(slot, ItemStack.fromTag(tagCompound));
                }
            }
        }
        return t;
    }

    public static void dropInventoryItems(World worldIn, double x, double y, double z, Inventory inventory)
    {
        for(int i = 0; i < inventory.getInvSize(); ++i)
        {
            ItemStack itemstack = inventory.getInvStack(i);

            if(!itemstack.isEmpty())
            {
                spawnItemStack(worldIn, x, y, z, itemstack);
            }
        }
    }

    public static void spawnItemStack(World worldIn, double x, double y, double z, ItemStack stack)
    {
        float offsetX = -0.25F + RANDOM.nextFloat() * 0.5F;
        float offsetY = RANDOM.nextFloat() * 0.8F;
        float offsetZ = -0.25F + RANDOM.nextFloat() * 0.5F;

        while(!stack.isEmpty())
        {
            ItemEntity entity = new ItemEntity(worldIn, x + offsetX, y + offsetY, z + offsetZ, stack.split(RANDOM.nextInt(21) + 10));
            entity.setVelocity(RANDOM.nextGaussian() * 0.05D, RANDOM.nextGaussian() * 0.05D + 0.2D, RANDOM.nextGaussian() * 0.05D);
            entity.setToDefaultPickupDelay();
            worldIn.spawnEntity(entity);
        }
    }

    public static int getItemAmount(PlayerEntity player, Item item)
    {
        int amount = 0;
        for(int i = 0; i < player.inventory.getInvSize(); i++)
        {
            ItemStack stack = player.inventory.getInvStack(i);
            if(!stack.isEmpty() && stack.getItem() == item)
            {
                amount += stack.getCount();
            }
        }
        return amount;
    }

    public static boolean hasItemAndAmount(PlayerEntity player, Item item, int amount)
    {
        int count = 0;
        for(ItemStack stack : player.inventory.main) {
            if(stack != null && stack.getItem() == item) {
                count += stack.getCount();
            }
        }
        return amount <= count;
    }

    public static boolean removeItemWithAmount(PlayerEntity player, Item item, int amount) {
        if(hasItemAndAmount(player, item, amount)) {
            for(int i = 0; i < player.inventory.getInvSize(); i++) {
                ItemStack stack = player.inventory.getInvStack(i);
                if(!stack.isEmpty() && stack.getItem() == item) {
                    if(amount - stack.getCount() < 0) {
                        stack.decrement(amount);
                        return true;
                    } else {
                        amount -= stack.getCount();
                        player.inventory.main.set(i, ItemStack.EMPTY);
                        if(amount == 0) return true;
                    }
                }
            }
        }
        return false;
    }

    public static int getItemStackAmount(PlayerEntity player, ItemStack find)
    {
        int count = 0;
        for(ItemStack stack : player.inventory.main)
        {
            if(!stack.isEmpty() && areItemStacksEqualIgnoreCount(stack, find))
            {
                count += stack.getCount();
            }
        }
        return count;
    }

    public static boolean hasItemStack(PlayerEntity player, ItemStack find)
    {
        int count = 0;
        for(ItemStack stack : player.inventory.main)
        {
            if(!stack.isEmpty() && areItemStacksEqualIgnoreCount(stack, find))
            {
                count += stack.getCount();
            }
        }
        return find.getCount() <= count;
    }

    public static boolean removeItemStack(PlayerEntity player, ItemStack find) {
        int amount = find.getCount();
        for(int i = 0; i < player.inventory.getInvSize(); i++) {
            ItemStack stack = player.inventory.getInvStack(i);
            if(!stack.isEmpty() && areItemStacksEqualIgnoreCount(stack, find)) {
                if(amount - stack.getCount() < 0) {
                    stack.decrement(amount);
                    return true;
                } else {
                    amount -= stack.getCount();
                    player.inventory.main.set(i, ItemStack.EMPTY);
                    if(amount == 0) return true;
                }
            }
        }
        return false;
    }

    public static boolean areItemStacksEqualIgnoreCount(ItemStack source, ItemStack target) {
        if(source.getItem() != target.getItem()) {
            return false;
        } else if(source.getTag() == null && target.getTag() != null) {
            return false;
        } else {
            return (source.getTag() == null || source.getTag().equals(target.getTag())) && source.isItemEqual(target);
        }
    }
}