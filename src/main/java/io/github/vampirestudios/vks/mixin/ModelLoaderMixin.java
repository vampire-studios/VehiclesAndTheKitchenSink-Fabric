package io.github.vampirestudios.vks.mixin;

import com.google.common.collect.ImmutableSet;
import io.github.vampirestudios.vks.IModelLoaderAdditions;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Set;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin implements IModelLoaderAdditions {

    @Shadow @Final private Map<Identifier, UnbakedModel> unbakedModels;
    @Shadow @Final private Map<Identifier, UnbakedModel> modelsToBake;

    @Shadow public abstract UnbakedModel getOrLoadModel(Identifier id);

    @Inject(method = "<init>", at=@At("RETURN"))
    public void onInit(CallbackInfo callbackInfo) {
        for (ModelIdentifier rl : getSpecialModels()) {
            UnbakedModel iunbakedmodel = this.getOrLoadModel(rl);
            this.unbakedModels.put(rl, iunbakedmodel);
            this.modelsToBake.put(rl, iunbakedmodel);
        }
    }

    @Override
    public Set<ModelIdentifier> getSpecialModels() {
        return ImmutableSet.of(
            new ModelIdentifier(new Identifier("vks:vehicle/atv_body"), "inventory"),
            new ModelIdentifier(new Identifier("vks:vehicle/atv_handles"), "inventory"),
            new ModelIdentifier(new Identifier("vks:vehicle/dune_buggy_body"), "inventory"),
            new ModelIdentifier(new Identifier("vks:vehicle/dune_buggy_handles"), "inventory"),
            new ModelIdentifier(new Identifier("vks:vehicle/go_kart_body"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/go_kart_steering_wheel"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/shopping_cart_body"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/mini_bike_body"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/mini_bike_handles"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/bumper_car_body"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/jet_ski_body"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/speed_boat_body"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/aluminum_boat_body"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/smart_car_body"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/lawn_mower_body"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/moped_body"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/moped_mud_guard"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/moped_handles"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/sports_plane_body"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/sports_plane_wing"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/sports_plane_wheel_cover"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/sports_plane_leg"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/sports_plane_propeller"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/golf_cart_body"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/off_roader_body"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/tractor_body"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/mini_bus_body"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/tractor_body"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/trailer_body"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/trailer_chest_body"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/trailer_seeder_body"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/trailer_fertilizer_body"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/trailer_fluid_body"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/vehicle_crate_panel"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/jack_piston_head"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/seed_spiker"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/nozzle"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/tow_bar"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/big_tow_bar"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/fuel_door_closed"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/fuel_door_open"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/small_fuel_door_closed"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/small_fuel_door_open"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/key_hole"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/sofa_helicopter_arm"), "inventory"),
                new ModelIdentifier(new Identifier("vks:vehicle/sofa_helicopter_skid"), "inventory"),
                new ModelIdentifier(new Identifier("vks:all_terrain_wheel"), "inventory")
        );
    }

}