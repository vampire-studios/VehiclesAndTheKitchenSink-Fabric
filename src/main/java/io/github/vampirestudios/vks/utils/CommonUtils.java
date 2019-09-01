package io.github.vampirestudios.vks.utils;

import net.minecraft.client.network.packet.ChatMessageS2CPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.MessageType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

/**
 * Author: MrCrayfish
 */
public class CommonUtils
{
    public static CompoundTag getItemTagCompound(ItemStack stack)
    {
        if(!stack.hasTag())
        {
            stack.setTag(new CompoundTag());
        }
        return stack.getTag();
    }

    public static void writeItemStackToTag(CompoundTag parent, String key, ItemStack stack)
    {
        if(!stack.isEmpty())
        {
            CompoundTag tag = new CompoundTag();
            stack.setTag(tag);
            parent.put(key, tag);
        }
    }

    public static ItemStack readItemStackFromTag(CompoundTag parent, String key)
    {
        if(parent.containsKey(key, Constants.NBT.TAG_COMPOUND))
        {
            return ItemStack.fromTag(parent.getCompound(key));
        }
        return ItemStack.EMPTY;
    }

    public static void sendInfoMessage(PlayerEntity player, String message)
    {
        if(player instanceof ServerPlayerEntity)
        {
            ((ServerPlayerEntity) player).networkHandler.sendPacket(new ChatMessageS2CPacket(new TranslatableText(message), MessageType.GAME_INFO));
        }
    }
}