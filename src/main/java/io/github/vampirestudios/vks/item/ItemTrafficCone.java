package io.github.vampirestudios.vks.item;

import io.github.vampirestudios.vks.VehiclesAndTheKitchenSink;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
public class ItemTrafficCone extends BlockItem {

    public ItemTrafficCone(Block block) {
        super(block, new Item.Settings().group(VehiclesAndTheKitchenSink.ITEM_GROUP));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        if(Screen.hasShiftDown()) {
            String info = I18n.translate("tile.vehicle.traffic_cone.info");
            tooltip.addAll(MinecraftClient.getInstance().textRenderer.wrapStringToWidthAsList(info, 150).stream().map((Function<String, Text>) LiteralText::new).collect(Collectors.toList()));
        }
        else
        {
            tooltip.add(new LiteralText(Formatting.YELLOW + I18n.translate("vehicle.info_help")));
        }
    }
}