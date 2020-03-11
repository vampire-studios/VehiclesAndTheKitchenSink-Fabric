package io.github.vampirestudios.vks.init;

import io.github.vampirestudios.vampirelib.utils.registry.RegistryUtils;
import io.github.vampirestudios.vks.VehiclesAndTheKitchenSink;
import io.github.vampirestudios.vks.item.WheelItem;
import io.github.vampirestudios.vks.item.SprayCanItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item PANEL = null;
    public static final Item STANDARD_WHEEL = RegistryUtils.registerItem(new WheelItem(), new Identifier(VehiclesAndTheKitchenSink.MOD_ID, "standard_wheel"));
    public static final Item SPORTS_WHEEL = RegistryUtils.registerItem(new WheelItem(), new Identifier(VehiclesAndTheKitchenSink.MOD_ID, "sports_wheel"));
    public static final Item RACING_WHEEL = RegistryUtils.registerItem(new WheelItem(), new Identifier(VehiclesAndTheKitchenSink.MOD_ID, "racing_wheel"));
    public static final Item OFF_ROAD_WHEEL = RegistryUtils.registerItem(new WheelItem(), new Identifier(VehiclesAndTheKitchenSink.MOD_ID, "off_road_wheel"));
    public static final Item SNOW_WHEEL = RegistryUtils.registerItem(new WheelItem(), new Identifier(VehiclesAndTheKitchenSink.MOD_ID, "snow_wheel"));
    public static final Item ALL_TERRAIN_WHEEL = RegistryUtils.registerItem(new WheelItem(), new Identifier(VehiclesAndTheKitchenSink.MOD_ID, "all_terrain_wheel"));
    public static final Item PLASTIC_WHEEL = RegistryUtils.registerItem(new WheelItem(), new Identifier(VehiclesAndTheKitchenSink.MOD_ID, "plastic_wheel"));

    public static final Item WOOD_SMALL_ENGINE = null;
    public static final Item STONE_SMALL_ENGINE = null;
    public static final Item IRON_SMALL_ENGINE = null;
    public static final Item GOLD_SMALL_ENGINE = null;
    public static final Item DIAMOND_SMALL_ENGINE = null;
    public static final Item WOOD_LARGE_ENGINE = null;
    public static final Item STONE_LARGE_ENGINE = null;
    public static final Item IRON_LARGE_ENGINE = null;
    public static final Item GOLD_LARGE_ENGINE = null;
    public static final Item DIAMOND_LARGE_ENGINE = null;
    public static final Item WOOD_ELECTRIC_ENGINE = null;
    public static final Item STONE_ELECTRIC_ENGINE = null;
    public static final Item IRON_ELECTRIC_ENGINE = null;
    public static final Item GOLD_ELECTRIC_ENGINE = null;
    public static final Item DIAMOND_ELECTRIC_ENGINE = null;
    public static final Item SPRAY_CAN = RegistryUtils.registerItem(new SprayCanItem(), new Identifier(VehiclesAndTheKitchenSink.MOD_ID, "spray_can"));
    public static final Item JERRY_CAN = null;
    public static final Item INDUSTRIAL_JERRY_CAN = null;
    public static final Item WRENCH = null;
    public static final Item HAMMER = null;
    public static final Item KEY = null;
    public static final Item FUELIUM_BUCKET = null;
    public static final Item ENDER_SAP_BUCKET = null;
    public static final Item BLAZE_JUICE_BUCKET = null;

}