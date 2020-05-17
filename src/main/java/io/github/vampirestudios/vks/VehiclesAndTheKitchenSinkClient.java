package io.github.vampirestudios.vks;

import io.github.vampirestudios.vks.client.render.*;
import io.github.vampirestudios.vks.client.render.be.JackRenderer;
import io.github.vampirestudios.vks.client.render.vehicle.*;
import io.github.vampirestudios.vks.entity.VehicleEntity;
import io.github.vampirestudios.vks.init.ModEntities;
import io.github.vampirestudios.vks.init.ModTileEntities;
import io.github.vampirestudios.vks.item.PartItem;
import io.github.vampirestudios.vks.item.SprayCanItem;
import io.github.vampirestudios.vks.utils.Constants;
import io.github.vampirestudios.vks.utils.S2CEntitySpawnPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.impl.client.rendering.ColorProviderRegistryImpl;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;

import java.util.Objects;

public class VehiclesAndTheKitchenSinkClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        for(SpecialModels model : SpecialModels.values()) {
            if(model.isSpecialModel()) {
                ModelLoader.addSpecialModel(new ModelIdentifier(model.getModelLocation(), "inventory"));
            }
        }

        registerVehicleRender(ModEntities.ATV, new RenderLandVehicleWrapper<>(new RenderATV()));
        registerVehicleRender(ModEntities.BUMPER_CAR, new RenderLandVehicleWrapper<>(new RenderBumperCar()));
        registerVehicleRender(ModEntities.DUNE_BUGGY, new RenderLandVehicleWrapper<>(new RenderDuneBuggy()));
        registerVehicleRender(ModEntities.GO_KART, new RenderLandVehicleWrapper<>(new RenderGoKart()));
        registerVehicleRender(ModEntities.GOLF_CART, new RenderLandVehicleWrapper<>(new RenderGolfCart()));
        registerVehicleRender(ModEntities.LAWN_MOWER, new RenderLandVehicleWrapper<>(new RenderLawnMower()));
        registerVehicleRender(ModEntities.MINI_BUS, new RenderLandVehicleWrapper<>(new RenderMiniBus()));
        registerVehicleRender(ModEntities.SAND_BUS, new RenderLandVehicleWrapper<>(new RenderSandBus()));
        registerVehicleRender(ModEntities.OFF_ROADER, new RenderLandVehicleWrapper<>(new RenderOffRoader()));
        registerVehicleRender(ModEntities.SMART_CAR, new RenderLandVehicleWrapper<>(new RenderSmartCar()));
        registerVehicleRender(ModEntities.TRACTOR, new RenderLandVehicleWrapper<>(new RenderTractor()));
        EntityRendererRegistry.INSTANCE.register(ModEntities.JACK, (entityRenderDispatcher, context) ->
                new io.github.vampirestudios.vks.client.render.JackRenderer(entityRenderDispatcher));
        BlockEntityRendererRegistry.INSTANCE.register(ModTileEntities.JACK, JackRenderer::new);
        ClientSidePacketRegistry.INSTANCE.register(S2CEntitySpawnPacket.ID, S2CEntitySpawnPacket::onPacket);

        ItemColorProvider color = (stack, index) -> {
            if(index == 0 && stack.hasTag() && Objects.requireNonNull(stack.getTag()).contains("Color", Constants.NBT.TAG_INT))
            {
                return stack.getTag().getInt("Color");
            }
            return 0xFFFFFF;
        };

        Registry.ITEM.forEach(item -> {
            if(item instanceof SprayCanItem/* || item instanceof KeyItem*/ || (item instanceof PartItem && ((PartItem) item).isColored())) {
                ColorProviderRegistryImpl.ITEM.register(color, item);
            }
        });
    }

    private <T extends VehicleEntity/* & EntityRaytracer.IEntityRaytraceable*/, R extends AbstractRenderVehicle<T>> void registerVehicleRender(EntityType<T> type, RenderVehicleWrapper<T, R> wrapper) {
        EntityRendererRegistry.INSTANCE.register(type, (entityRenderDispatcher, context) -> new RenderEntityVehicle<>(entityRenderDispatcher, wrapper));
        VehicleRenderRegistry.registerRenderWrapper(type, wrapper);
    }

}