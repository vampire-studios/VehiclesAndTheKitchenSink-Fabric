package io.github.vampirestudios.vks.item;

import io.github.vampirestudios.vks.VehiclesAndTheKitchenSink;
import io.github.vampirestudios.vks.entity.EngineTier;
import io.github.vampirestudios.vks.entity.EngineType;
import net.minecraft.client.gui.screen.Screen;
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

/**
 * Author: MrCrayfish
 */
public class EngineItem extends PartItem
{
    private EngineType engineType;
    private EngineTier engineTier;

    public EngineItem(String id, EngineType engineType, EngineTier engineTier)
    {
        this(id, engineType, engineTier, new Item.Settings().group(VehiclesAndTheKitchenSink.ITEM_GROUP));
    }

    public EngineItem(String id, EngineType engineType, EngineTier engineTier, Item.Settings properties)
    {
        super(id, properties);
        this.engineType = engineType;
        this.engineTier = engineTier;
    }

    public EngineType getEngineType()
    {
        return engineType;
    }

    public EngineTier getEngineTier()
    {
        return engineTier;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        String tierName = I18n.translate("vehicle.engine_tier." + this.engineTier.getTierName() + ".name");
        tooltip.add(new LiteralText(this.engineTier.getTierColor() + Formatting.BOLD.toString() + tierName));
        if(Screen.hasShiftDown())
        {
            tooltip.add(new LiteralText(Formatting.YELLOW + I18n.translate("vehicle.engine_info.acceleration") + ": " + Formatting.RESET + this.engineTier.getAccelerationMultiplier() + "x"));
            tooltip.add(new LiteralText(Formatting.YELLOW + I18n.translate("vehicle.engine_info.additional_max_speed") + ": " + Formatting.RESET + (this.engineTier.getAdditionalMaxSpeed() * 3.6) + "kph"));
            tooltip.add(new LiteralText(Formatting.YELLOW + I18n.translate("vehicle.engine_info.fuel_consumption") + ": " + Formatting.RESET + this.engineTier.getFuelConsumption() + "pt"));
        }
        else
        {
            tooltip.add(new LiteralText(Formatting.YELLOW + I18n.translate("vehicle.info_help")));
        }
    }
}