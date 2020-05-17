package io.github.vampirestudios.vks.init;

import io.github.vampirestudios.vks.VehiclesAndTheKitchenSink;
import io.github.vampirestudios.vks.block.BlockJack;
import io.github.vampirestudios.vks.block.BlockTrafficCone;
import io.github.vampirestudios.vks.block.BlockVehicleCrate;
import io.github.vampirestudios.vks.item.ItemTrafficCone;
import io.github.vampirestudios.vks.utils.Names;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Function;

/**
 * Author: MrCrayfish
 */
public class ModBlocks {

    public static final Block TRAFFIC_CONE = register(new Identifier(Names.Block.TRAFFIC_CONE), new BlockTrafficCone(), ItemTrafficCone::new);
//    public static final Block BOOST_PAD = register(new BlockBoostPad(), null);
//    public static final Block BOOST_RAMP = register(new BlockBoostRamp(), null); //ItemBoostRamp::new
//    public static final Block STEEP_BOOST_RAMP = register(new BlockSteepBoostRamp(), null);
//    public static final Block FLUID_EXTRACTOR = register(new BlockFluidExtractor());
//    public static final Block FLUID_MIXER = register(new BlockFluidMixer());
//    public static final Block GAS_PUMP = register(new BlockGasPump());
//    public static final Block FLUID_PIPE = register(new BlockFluidPipe());
//    public static final Block FLUID_PUMP = register(new BlockFluidPump());
//    public static final Block FUEL_DRUM = register(new BlockFuelDrum());
//    public static final Block INDUSTRIAL_FUEL_DRUM = register(new BlockIndustrialFuelDrum());
//    public static final Block WORKSTATION = register(new BlockWorkstation());
    public static final Block VEHICLE_CRATE = register(new Identifier(Names.Block.VEHICLE_CRATE), new BlockVehicleCrate(), block -> new BlockItem(block, new Item.Settings().maxCount(1)));
    public static final Block JACK = register(new Identifier(Names.Block.JACK), new BlockJack());
//    public static final FluidBlock FUELIUM = (FluidBlock) register(new FluidBlock(() -> ModFluids.FLOWING_FUELIUM, FabricBlockSettings.of(Material.WATER).noCollision().hardness(100.0F).dropsNothing().build())/*.setRegistryName(VehiclesAndTheKitchenSink.MOD_ID, "fuelium")*/, null);
//    public static final FluidBlock ENDER_SAP = (FluidBlock) register(new FluidBlock(() -> ModFluids.FLOWING_ENDER_SAP, FabricBlockSettings.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops())/*.setRegistryName(VehiclesAndTheKitchenSink.MOD_ID, "ender_sap")*/, null);
//    public static final FluidBlock BLAZE_JUICE = (FluidBlock) register(new FluidBlock(() -> ModFluids.FLOWING_BLAZE_JUICE, FabricBlockSettings.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops())/*.setRegistryName(VehiclesAndTheKitchenSink.MOD_ID, "blaze_juice")*/, null);

    private static Block register(Identifier name, Block block) {
        return register(name, block, block1 -> new BlockItem(block1, new Item.Settings().group(VehiclesAndTheKitchenSink.ITEM_GROUP)));
    }

    private static Block register(Identifier name, Block block, @Nullable Function<Block, BlockItem> supplier) {
        Registry.register(Registry.ITEM, name, Objects.requireNonNull(supplier).apply(block));
        return Registry.register(Registry.BLOCK, name, block);
    }

}