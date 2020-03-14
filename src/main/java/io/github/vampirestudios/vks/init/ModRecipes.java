package io.github.vampirestudios.vks.init;

import io.github.vampirestudios.vks.VehiclesAndTheKitchenSink;
import io.github.vampirestudios.vks.recipe.RecipeColorSprayCan;
import io.github.vampirestudios.vks.recipe.RecipeRefillSprayCan;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * Author: MrCrayfish
 */
public class ModRecipes {

    public static SpecialRecipeSerializer<RecipeColorSprayCan> COLOR_SPRAY_CAN;
    public static SpecialRecipeSerializer<RecipeRefillSprayCan> REFILL_SPRAY_CAN;

    public static void init() {
        COLOR_SPRAY_CAN = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(VehiclesAndTheKitchenSink.MOD_ID, "color_spray_can"),
                new SpecialRecipeSerializer<>(RecipeColorSprayCan::new));
        REFILL_SPRAY_CAN = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(VehiclesAndTheKitchenSink.MOD_ID, "refill_spray_can"),
                        new SpecialRecipeSerializer<>(RecipeRefillSprayCan::new));
    }

}