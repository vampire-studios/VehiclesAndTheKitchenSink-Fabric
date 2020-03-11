package io.github.vampirestudios.vks.item;

import io.github.vampirestudios.vks.VehiclesAndTheKitchenSink;
import io.github.vampirestudios.vks.utils.Constants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Author: MrCrayfish
 */
public class SprayCanItem extends Item implements IDyeable, ItemDurabilityExtensions {

    public static final int MAX_SPRAYS = 5;

    public SprayCanItem() {
        super(new Item.Settings().group(VehiclesAndTheKitchenSink.ITEM_GROUP));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        if(Screen.hasShiftDown()) {
            String info = I18n.translate("item.vehicle.spray_can.info");
            tooltip.addAll(MinecraftClient.getInstance().textRenderer.wrapStringToWidthAsList(info, 150).stream().map((Function<String, Text>) LiteralText::new).collect(Collectors.toList()));
        }
        else {
            if(this.hasColor(stack)) {
                tooltip.add(new LiteralText(I18n.translate("item.color", Formatting.DARK_GRAY.toString() + String.format("#%06X", this.getColor(stack)))));
            }
            else {
                tooltip.add(new LiteralText(I18n.translate("item.vehicle.spray_can.empty")));
            }
            tooltip.add(new LiteralText(Formatting.YELLOW + I18n.translate("vehicle.info_help")));
        }
    }

    public static CompoundTag getStackTag(ItemStack stack) {
        if(stack.getTag() == null) {
            stack.setTag(new CompoundTag());
        }
        if(stack.getItem() instanceof SprayCanItem) {
            SprayCanItem sprayCan = (SprayCanItem) stack.getItem();
            CompoundTag compound = stack.getTag();
            if(compound != null) {
                if(!compound.contains("RemainingSprays", Constants.NBT.TAG_INT)) {
                    compound.putInt("RemainingSprays", sprayCan.getCapacity(stack));
                }
            }
        }
        return stack.getTag();
    }

    @Override
    public boolean showDurability(ItemStack stack) {
        CompoundTag compound = stack.getTag();
        if(compound != null && compound.contains("RemainingSprays", Constants.NBT.TAG_INT)) {
            int remainingSprays = compound.getInt("RemainingSprays");
            return this.hasColor(stack) && remainingSprays >= 0 && remainingSprays < this.getCapacity(stack);
        }
        return false;
    }

    @Override
    public double getDurability(ItemStack stack) {
        CompoundTag compound = stack.getTag();
        if(compound != null && compound.contains("RemainingSprays", Constants.NBT.TAG_INT)) {
            return 1.0 - (compound.getInt("RemainingSprays") / (double) this.getCapacity(stack));
        }
        return 0.0;
    }

    public float getRemainingSprays(ItemStack stack) {
        CompoundTag compound = stack.getTag();
        if(compound != null && compound.contains("RemainingSprays", Constants.NBT.TAG_INT)) {
            return compound.getInt("RemainingSprays") / (float) this.getCapacity(stack);
        }
        return 0.0F;
    }

    public int getCapacity(ItemStack stack) {
        CompoundTag compound = stack.getTag();
        if(compound != null && compound.contains("Capacity", Constants.NBT.TAG_INT)) {
            return compound.getInt("Capacity");
        }
        return MAX_SPRAYS;
    }

    public void refill(ItemStack stack) {
        CompoundTag compound = getStackTag(stack);
        compound.putInt("RemainingSprays", this.getCapacity(stack));
    }
}