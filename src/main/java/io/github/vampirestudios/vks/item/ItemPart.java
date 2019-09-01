package io.github.vampirestudios.vks.item;

import net.minecraft.item.Item;

public class ItemPart extends Item {

    private boolean colored = false;

    public ItemPart(Item.Settings settings) {
        super(settings);
    }

    public ItemPart setColored() {
        this.colored = true;
        return this;
    }

    public boolean isColored()
    {
        return colored;
    }

}