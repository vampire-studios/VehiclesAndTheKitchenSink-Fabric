package io.github.vampirestudios.vks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;

public class VehiclesAndTheKitchenSink implements ModInitializer {

    public static final String MOD_ID = "vks";

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(new Identifier(MOD_ID, "item_group")).build();

    @Override
    public void onInitialize() {

    }

}