package io.github.vampirestudios.vks;

import io.github.vampirestudios.vks.client.render.AbstractRenderVehicle;
import io.github.vampirestudios.vks.client.render.RenderEntityVehicle;
import io.github.vampirestudios.vks.client.render.RenderVehicleWrapper;
import io.github.vampirestudios.vks.client.render.VehicleRenderRegistry;
import io.github.vampirestudios.vks.entity.VehicleEntity;
import io.github.vampirestudios.vks.entity.VehicleProperties;
import io.github.vampirestudios.vks.init.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;

public class VehiclesAndTheKitchenSink implements ModInitializer {

    public static final String MOD_ID = "vks";

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(new Identifier(MOD_ID, "item_group")).build();

    @Override
    public void onInitialize() {
        new ModBlocks();
        new ModItems();
        new ModSounds();
        new ModTileEntities();
        new ModEntities();
        VehicleProperties.register();
    }

    private <T extends VehicleEntity/* & EntityRaytracer.IEntityRaytraceable*/, R extends AbstractRenderVehicle<T>> void registerVehicleRender(EntityType<T> type, RenderVehicleWrapper<T, R> wrapper)
    {
        EntityRendererRegistry.INSTANCE.register(type, (entityRenderDispatcher, context) -> new RenderEntityVehicle<>(entityRenderDispatcher, wrapper));
        VehicleRenderRegistry.registerRenderWrapper(type, wrapper);
    }

}