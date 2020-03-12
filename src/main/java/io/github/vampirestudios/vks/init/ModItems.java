package io.github.vampirestudios.vks.init;

import io.github.vampirestudios.vampirelib.utils.registry.RegistryUtils;
import io.github.vampirestudios.vks.VehiclesAndTheKitchenSink;
import io.github.vampirestudios.vks.entity.EngineTier;
import io.github.vampirestudios.vks.entity.EngineType;
import io.github.vampirestudios.vks.entity.WheelType;
import io.github.vampirestudios.vks.item.EngineItem;
import io.github.vampirestudios.vks.item.WheelItem;
import io.github.vampirestudios.vks.item.SprayCanItem;
import io.github.vampirestudios.vks.utils.Names;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item PANEL = null;
    public static final Item STANDARD_WHEEL = new WheelItem(Names.Item.STANDARD_WHEEL, WheelType.STANDARD);
    public static final Item SPORTS_WHEEL = new WheelItem(Names.Item.SPORTS_WHEEL, WheelType.SPORTS);
    public static final Item RACING_WHEEL = new WheelItem(Names.Item.RACING_WHEEL, WheelType.RACING);
    public static final Item OFF_ROAD_WHEEL = new WheelItem(Names.Item.OFF_ROAD_WHEEL, WheelType.OFF_ROAD);
    public static final Item SNOW_WHEEL = new WheelItem(Names.Item.SNOW_WHEEL, WheelType.SNOW);
    public static final Item ALL_TERRAIN_WHEEL = new WheelItem(Names.Item.ALL_TERRAIN_WHEEL, WheelType.ALL_TERRAIN);
    public static final Item PLASTIC_WHEEL = new WheelItem(Names.Item.PLASTIC_WHEEL, WheelType.PLASTIC);

    public static final Item WOOD_SMALL_ENGINE = new EngineItem(Names.Item.WOOD_SMALL_ENGINE, EngineType.SMALL_MOTOR, EngineTier.WOOD);
    public static final Item STONE_SMALL_ENGINE = new EngineItem(Names.Item.STONE_SMALL_ENGINE, EngineType.SMALL_MOTOR, EngineTier.STONE);
    public static final Item IRON_SMALL_ENGINE = new EngineItem(Names.Item.IRON_SMALL_ENGINE, EngineType.SMALL_MOTOR, EngineTier.IRON);
    public static final Item GOLD_SMALL_ENGINE = new EngineItem(Names.Item.GOLD_SMALL_ENGINE, EngineType.SMALL_MOTOR, EngineTier.GOLD);
    public static final Item DIAMOND_SMALL_ENGINE = new EngineItem(Names.Item.DIAMOND_SMALL_ENGINE, EngineType.SMALL_MOTOR, EngineTier.DIAMOND);
    public static final Item WOOD_LARGE_ENGINE = new EngineItem(Names.Item.WOOD_LARGE_ENGINE, EngineType.LARGE_MOTOR, EngineTier.WOOD);
    public static final Item STONE_LARGE_ENGINE = new EngineItem(Names.Item.STONE_LARGE_ENGINE, EngineType.LARGE_MOTOR, EngineTier.STONE);
    public static final Item IRON_LARGE_ENGINE = new EngineItem(Names.Item.IRON_LARGE_ENGINE, EngineType.LARGE_MOTOR, EngineTier.IRON);
    public static final Item GOLD_LARGE_ENGINE = new EngineItem(Names.Item.GOLD_LARGE_ENGINE, EngineType.LARGE_MOTOR, EngineTier.GOLD);
    public static final Item DIAMOND_LARGE_ENGINE = new EngineItem(Names.Item.DIAMOND_LARGE_ENGINE, EngineType.LARGE_MOTOR, EngineTier.DIAMOND);
    public static final Item WOOD_ELECTRIC_ENGINE = new EngineItem(Names.Item.WOOD_ELECTRIC_ENGINE, EngineType.ELECTRIC_MOTOR, EngineTier.WOOD);
    public static final Item STONE_ELECTRIC_ENGINE = new EngineItem(Names.Item.STONE_ELECTRIC_ENGINE, EngineType.ELECTRIC_MOTOR, EngineTier.STONE);
    public static final Item IRON_ELECTRIC_ENGINE = new EngineItem(Names.Item.IRON_ELECTRIC_ENGINE, EngineType.ELECTRIC_MOTOR, EngineTier.IRON);
    public static final Item GOLD_ELECTRIC_ENGINE = new EngineItem(Names.Item.GOLD_ELECTRIC_ENGINE, EngineType.ELECTRIC_MOTOR, EngineTier.GOLD);
    public static final Item DIAMOND_ELECTRIC_ENGINE = new EngineItem(Names.Item.DIAMOND_ELECTRIC_ENGINE, EngineType.ELECTRIC_MOTOR, EngineTier.DIAMOND);
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