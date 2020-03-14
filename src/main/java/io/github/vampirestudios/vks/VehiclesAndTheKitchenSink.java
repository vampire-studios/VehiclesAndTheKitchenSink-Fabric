package io.github.vampirestudios.vks;

import io.github.vampirestudios.vks.common.ItemLookup;
import io.github.vampirestudios.vks.entity.VehicleProperties;
import io.github.vampirestudios.vks.init.*;
import io.github.vampirestudios.vks.utils.Tags;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;

public class VehiclesAndTheKitchenSink implements ModInitializer {

    public static final String MOD_ID = "vks";

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(new Identifier(MOD_ID, "item_group")).build();

    @Override
    public void onInitialize() {
        new Tags.Items();
        new Tags.Blocks();
        ModRecipes.init();
        new ModBlocks();
        new ModItems();
        new ModSounds();
        new ModTileEntities();
        new ModEntities();
        ItemLookup.init();
        VehicleProperties.register();
    }

}