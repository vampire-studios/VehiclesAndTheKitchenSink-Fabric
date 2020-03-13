package io.github.vampirestudios.vks.item;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * Author: MrCrayfish
 */
public class PartItem extends Item
{
    private boolean colored;

    public PartItem(String id, Item.Settings properties)
    {
        super(properties);
        Registry.register(Registry.ITEM, new Identifier(id), this);
    }

    public PartItem setColored()
    {
        this.colored = true;
        return this;
    }

    public boolean isColored()
    {
        return colored;
    }
}