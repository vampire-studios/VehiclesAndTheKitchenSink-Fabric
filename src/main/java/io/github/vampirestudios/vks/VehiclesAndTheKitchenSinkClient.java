package io.github.vampirestudios.vks;

import io.github.vampirestudios.vks.client.render.*;
import io.github.vampirestudios.vks.client.render.vehicle.RenderGoKart;
import io.github.vampirestudios.vks.entity.VehicleEntity;
import io.github.vampirestudios.vks.init.ModEntities;
import io.github.vampirestudios.vks.utils.S2CEntitySpawnPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.entity.EntityType;

public class VehiclesAndTheKitchenSinkClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        registerVehicleRender(ModEntities.GO_KART, new RenderLandVehicleWrapper<>(new RenderGoKart()));
        ClientSidePacketRegistry.INSTANCE.register(S2CEntitySpawnPacket.ID, S2CEntitySpawnPacket::onPacket);
    }

    private <T extends VehicleEntity/* & EntityRaytracer.IEntityRaytraceable*/, R extends AbstractRenderVehicle<T>> void registerVehicleRender(EntityType<T> type, RenderVehicleWrapper<T, R> wrapper)
    {
        EntityRendererRegistry.INSTANCE.register(type, (entityRenderDispatcher, context) -> new RenderEntityVehicle<>(entityRenderDispatcher, wrapper));
        VehicleRenderRegistry.registerRenderWrapper(type, wrapper);
    }

}