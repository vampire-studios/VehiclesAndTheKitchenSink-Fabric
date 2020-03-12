package io.github.vampirestudios.vks.item;

import io.github.vampirestudios.vks.VehiclesAndTheKitchenSink;
import io.github.vampirestudios.vks.entity.WheelType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class WheelItem extends PartItem implements IDyeable {
    private WheelType wheelType;

    public WheelItem(String id, WheelType wheelType)
    {
        super(id, new Item.Settings().group(VehiclesAndTheKitchenSink.ITEM_GROUP));
        this.wheelType = wheelType;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new LiteralText(Formatting.AQUA + I18n.translate("vehicle.wheel_type." + this.wheelType.getId() + ".name")));
    }

    public WheelType getWheelType() {
        return this.wheelType;
    }

}