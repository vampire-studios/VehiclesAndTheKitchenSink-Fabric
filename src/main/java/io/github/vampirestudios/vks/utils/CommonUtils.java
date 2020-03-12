package io.github.vampirestudios.vks.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

/**
 * Author: MrCrayfish
 */
public class CommonUtils {

    public static CompoundTag getOrCreateStackTag(ItemStack stack) {
        if(stack.getTag() == null) {
            stack.setTag(new CompoundTag());
        }
        return stack.getTag();
    }

    public static void writeItemStackToTag(CompoundTag compound, String key, ItemStack stack) {
        if(!stack.isEmpty()) {
            CompoundTag tag = new CompoundTag();
            stack.toTag(tag);
            compound.put(key, tag);
        }
    }

    public static ItemStack readItemStackFromTag(CompoundTag compound, String key) {
        if(compound.contains(key, Constants.NBT.TAG_COMPOUND)) {
            return ItemStack.fromTag(compound.getCompound(key));
        }
        return ItemStack.EMPTY;
    }

    public static void sendInfoMessage(PlayerEntity player, String message) {
        if(player instanceof ServerPlayerEntity) {
            ((ServerPlayerEntity) player).networkHandler.sendPacket(new ChatMessageS2CPacket(new TranslatableText(message), MessageType.GAME_INFO));
        }
    }

    public static boolean isMouseWithin(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

}