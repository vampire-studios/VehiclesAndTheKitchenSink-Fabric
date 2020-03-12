package io.github.vampirestudios.vks.item;

import io.github.vampirestudios.vampirelib.utils.registry.RegistryUtils;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

/**
 * Author: MrCrayfish
 */
public class PartItem extends Item
{
    private boolean colored;

    public PartItem(String id, Item.Settings properties)
    {
        super(properties);
//        this.setRegistryName(id);
        RegistryUtils.registerItem(this, new Identifier(id));
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