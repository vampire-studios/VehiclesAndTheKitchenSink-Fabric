package io.github.vampirestudios.vks.recipe;

import com.google.common.collect.Lists;
import io.github.vampirestudios.vks.init.ModRecipes;
import io.github.vampirestudios.vks.item.IDyeable;
import io.github.vampirestudios.vks.utils.Tags;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

/**
 * Author: MrCrayfish
 */
public class RecipeColorSprayCan extends SpecialCraftingRecipe {

    public RecipeColorSprayCan(Identifier id) {
        super(id);
        System.out.println("Testing");
    }

    @Override
    public boolean matches(CraftingInventory inventory, World worldIn) {
        ItemStack dyeableItem = ItemStack.EMPTY;
        List<ItemStack> dyes = Lists.newArrayList();
        dyes.add(new ItemStack(Items.RED_DYE));
        dyes.add(new ItemStack(Items.GREEN_DYE));
        dyes.add(new ItemStack(Items.BLUE_DYE));

        for(int i = 0; i < inventory.getInvSize(); ++i) {
            ItemStack stack = inventory.getInvStack(i);
            if(!stack.isEmpty()) {
                if(stack.getItem() instanceof IDyeable) {
                    if(!dyeableItem.isEmpty()) {
                        return false;
                    }
                    dyeableItem = stack.copy();
                } else {
                    if(!stack.getItem().isIn(Tags.Items.DYES)) {
                        return false;
                    }
                    dyes.add(stack);
                }
            }
        }

        return !dyeableItem.isEmpty() && !dyes.isEmpty();
    }

    @Override
    public ItemStack craft(CraftingInventory inventory) {
        ItemStack dyeableItem = ItemStack.EMPTY;
        List<DyeItem> dyes = Lists.newArrayList();
        dyes.add((DyeItem) Items.RED_DYE);
        dyes.add((DyeItem) Items.GREEN_DYE);
        dyes.add((DyeItem) Items.BLUE_DYE);

        for(int i = 0; i < inventory.getInvSize(); ++i) {
            ItemStack stack = inventory.getInvStack(i);
            if(!stack.isEmpty()) {
                if(stack.getItem() instanceof IDyeable) {
                    if(!dyeableItem.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    dyeableItem = stack.copy();
                } else {
                    if(!(stack.getItem() instanceof DyeItem)) {
                        return ItemStack.EMPTY;
                    }
                    dyes.add((DyeItem) stack.getItem());
                }
            }
        }

        return !dyeableItem.isEmpty() && !dyes.isEmpty() ? IDyeable.dyeStack(dyeableItem, dyes) : ItemStack.EMPTY;
    }

    @Override
    public boolean fits(int width, int height)
    {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return ModRecipes.COLOR_SPRAY_CAN;
    }
}