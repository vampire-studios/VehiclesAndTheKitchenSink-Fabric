package io.github.vampirestudios.vks.item;

import io.github.vampirestudios.vks.VehiclesAndTheKitchenSink;
import io.github.vampirestudios.vks.utils.Names;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

/**
 * Author: MrCrayfish
 */
public class HammerItem extends Item {
    public HammerItem() {
        super(new Item.Settings().maxDamage(200).group(VehiclesAndTheKitchenSink.ITEM_GROUP));
        Registry.register(Registry.ITEM, Names.Item.HAMMER, this);
    }
}