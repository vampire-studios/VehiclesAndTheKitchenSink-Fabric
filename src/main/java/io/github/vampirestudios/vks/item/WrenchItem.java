package io.github.vampirestudios.vks.item;

import io.github.vampirestudios.vks.VehiclesAndTheKitchenSink;
import io.github.vampirestudios.vks.utils.Names;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

/**
 * Author: MrCrayfish
 */
public class WrenchItem extends Item {
    public WrenchItem() {
        super(new Settings().maxCount(1).group(VehiclesAndTheKitchenSink.ITEM_GROUP));
        Registry.register(Registry.ITEM, Names.Item.WRENCH, this);
    }
}