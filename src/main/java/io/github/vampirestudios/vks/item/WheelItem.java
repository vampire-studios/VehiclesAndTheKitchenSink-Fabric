package io.github.vampirestudios.vks.item;

import io.github.vampirestudios.vks.VehiclesAndTheKitchenSink;
import io.github.vampirestudios.vks.utils.CommonUtils;
import io.github.vampirestudios.vks.utils.Constants;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public class WheelItem extends ItemPart implements IDyeable {

    public WheelItem() {
        super(new Item.Settings().maxDamage(0).group(VehiclesAndTheKitchenSink.ITEM_GROUP));
    }

    @Override
    public boolean hasColor(ItemStack stack) {
        CompoundTag tagCompound = CommonUtils.getItemTagCompound(stack);
        return tagCompound.contains("color", Constants.NBT.TAG_INT);
    }

    @Override
    public int getColor(ItemStack stack) {
        CompoundTag tagCompound = CommonUtils.getItemTagCompound(stack);
        return tagCompound.getInt("color");
    }

    @Override
    public void setColor(ItemStack stack, int color) {
        CompoundTag tagCompound = CommonUtils.getItemTagCompound(stack);
        tagCompound.putInt("color", color);
    }

}