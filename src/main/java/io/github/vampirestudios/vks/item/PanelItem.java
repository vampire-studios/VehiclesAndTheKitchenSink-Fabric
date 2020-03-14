package io.github.vampirestudios.vks.item;

import io.github.vampirestudios.vks.VehiclesAndTheKitchenSink;
import io.github.vampirestudios.vks.utils.Names;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

/**
 * Author: MrCrayfish
 */
public class PanelItem extends Item {
    public PanelItem() {
        super(new Settings().group(VehiclesAndTheKitchenSink.ITEM_GROUP));
        Registry.register(Registry.ITEM, Names.Item.PANEL, this);
    }
}