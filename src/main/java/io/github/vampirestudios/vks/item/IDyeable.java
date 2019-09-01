package io.github.vampirestudios.vks.item;

import net.minecraft.item.ItemStack;

public interface IDyeable {

    boolean hasColor(ItemStack stack);

    int getColor(ItemStack stack);

    void setColor(ItemStack stack, int color);

}