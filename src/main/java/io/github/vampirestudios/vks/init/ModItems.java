package io.github.vampirestudios.vks.init;

import io.github.vampirestudios.vampirelib.utils.registry.RegistryUtils;
import io.github.vampirestudios.vks.VehiclesAndTheKitchenSink;
import io.github.vampirestudios.vks.item.ItemWheel;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item TOW_BAR = null;
    public static final Item STANDARD_WHEEL = RegistryUtils.registerItem(new ItemWheel(), new Identifier(VehiclesAndTheKitchenSink.MOD_ID, "standard_wheel"));
    public static final Item SPORTS_WHEEL = RegistryUtils.registerItem(new ItemWheel(), new Identifier(VehiclesAndTheKitchenSink.MOD_ID, "sports_wheel"));
    public static final Item RACING_WHEEL = RegistryUtils.registerItem(new ItemWheel(), new Identifier(VehiclesAndTheKitchenSink.MOD_ID, "racing_wheel"));
    public static final Item OFF_ROAD_WHEEL = RegistryUtils.registerItem(new ItemWheel(), new Identifier(VehiclesAndTheKitchenSink.MOD_ID, "off_road_wheel"));
    public static final Item SNOW_WHEEL = RegistryUtils.registerItem(new ItemWheel(), new Identifier(VehiclesAndTheKitchenSink.MOD_ID, "snow_wheel"));
    public static final Item ALL_TERRAIN_WHEEL = RegistryUtils.registerItem(new ItemWheel(), new Identifier(VehiclesAndTheKitchenSink.MOD_ID, "all_terrain_wheel"));
    public static final Item PLASTIC_WHEEL = RegistryUtils.registerItem(new ItemWheel(), new Identifier(VehiclesAndTheKitchenSink.MOD_ID, "plastic_wheel"));

}