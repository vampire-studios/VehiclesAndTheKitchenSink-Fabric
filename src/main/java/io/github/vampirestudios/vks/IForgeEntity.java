package io.github.vampirestudios.vks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.EnderCrystalEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.decoration.LeadKnotEntity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.*;
import net.minecraft.util.hit.HitResult;

public interface IForgeEntity {

    default Entity getEntity() { return (Entity) this; }

    default ItemStack getPickedResult(HitResult target) {
        if (this instanceof PaintingEntity)
            return new ItemStack(Items.PAINTING);
        else if (this instanceof LeadKnotEntity)
            return new ItemStack(Items.LEAD);
        else if (this instanceof ItemFrameEntity) {
            ItemStack held = ((ItemFrameEntity)this).getHeldItemStack();
            if (held.isEmpty()) return new ItemStack(Items.ITEM_FRAME);
            else return held.copy();
        }
        else if (this instanceof AbstractMinecartEntity)
            return new ItemStack(Items.MINECART);
        else if (this instanceof BoatEntity)
            return new ItemStack(Items.OAK_BOAT);
        else if (this instanceof ArmorStandEntity)
            return new ItemStack(Items.ARMOR_STAND);
        else if (this instanceof EnderCrystalEntity)
            return new ItemStack(Items.END_CRYSTAL);
        else {
            SpawnEggItem egg = SpawnEggItem.forEntity(getEntity().getType());
            if (egg != null) return new ItemStack(egg);
        }
        return ItemStack.EMPTY;
    }

}
