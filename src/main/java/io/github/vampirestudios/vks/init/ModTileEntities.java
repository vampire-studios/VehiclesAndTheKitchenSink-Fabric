package io.github.vampirestudios.vks.init;

import io.github.vampirestudios.vampirelib.utils.registry.RegistryUtils;
import io.github.vampirestudios.vks.block.entity.VehicleCrateTileEntity;
import io.github.vampirestudios.vks.utils.Names;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;

/**
 * Author: MrCrayfish
 */
public class ModTileEntities
{
//    public static final TileEntityType<FluidExtractorTileEntity> FLUID_EXTRACTOR = null;
//    public static final TileEntityType<FluidPipeTileEntity> FLUID_PIPE = null;
//    public static final TileEntityType<FluidPumpTileEntity> FLUID_PUMP = null;
//    public static final TileEntityType<FuelDrumTileEntity> FUEL_DRUM = null;
//    public static final TileEntityType<IndustrialFuelDrumTileEntity> INDUSTRIAL_FUEL_DRUM = null;
//    public static final TileEntityType<FluidMixerTileEntity> FLUID_MIXER = null;
    public static final BlockEntityType<VehicleCrateTileEntity> VEHICLE_CRATE = buildType(Names.TileEntity.VEHICLE_CRATE, BlockEntityType.Builder.create(VehicleCrateTileEntity::new, ModBlocks.VEHICLE_CRATE));
//    public static final TileEntityType<WorkstationTileEntity> WORKSTATION = null;
//    public static final TileEntityType<JackTileEntity> JACK = null;
//    public static final TileEntityType<BoostTileEntity> BOOST = null;
//    public static final TileEntityType<GasPumpTileEntity> GAS_PUMP = null;
//    public static final TileEntityType<GasPumpTankTileEntity> GAS_PUMP_TANK = null;

    private static <T extends BlockEntity> BlockEntityType<T> buildType(String id, BlockEntityType.Builder<T> builder)
    {
        return RegistryUtils.registerBlockEntity(builder, new Identifier(id));
    }

    /*@SubscribeEvent
    @SuppressWarnings("unused")
    public static void registerTypes(final RegistryEvent.Register<TileEntityType<?>> event)
    {
        IForgeRegistry<TileEntityType<?>> registry = event.getRegistry();
        registry.register(buildType(Names.TileEntity.FLUID_EXTRACTOR, TileEntityType.Builder.create(FluidExtractorTileEntity::new, ModBlocks.FLUID_EXTRACTOR)));
        registry.register(buildType(Names.TileEntity.FLUID_PIPE, TileEntityType.Builder.create(FluidPipeTileEntity::new, ModBlocks.FLUID_PIPE)));
        registry.register(buildType(Names.TileEntity.FLUID_PUMP, TileEntityType.Builder.create(FluidPumpTileEntity::new, ModBlocks.FLUID_PUMP)));
        registry.register(buildType(Names.TileEntity.FLUID_MIXER, TileEntityType.Builder.create(FluidMixerTileEntity::new, ModBlocks.FLUID_MIXER)));
        registry.register(buildType(Names.TileEntity.FUEL_DRUM, TileEntityType.Builder.create(FuelDrumTileEntity::new, ModBlocks.FUEL_DRUM)));
        registry.register(buildType(Names.TileEntity.INDUSTRIAL_FUEL_DRUM, TileEntityType.Builder.create(IndustrialFuelDrumTileEntity::new, ModBlocks.INDUSTRIAL_FUEL_DRUM)));
        registry.register(buildType(Names.TileEntity.VEHICLE_CRATE, TileEntityType.Builder.create(VehicleCrateTileEntity::new, ModBlocks.VEHICLE_CRATE)));
        registry.register(buildType(Names.TileEntity.WORKSTATION, TileEntityType.Builder.create(WorkstationTileEntity::new, ModBlocks.WORKSTATION)));
        registry.register(buildType(Names.TileEntity.JACK, TileEntityType.Builder.create(JackTileEntity::new, ModBlocks.JACK)));
        registry.register(buildType(Names.TileEntity.BOOST, TileEntityType.Builder.create(BoostTileEntity::new, ModBlocks.BOOST_PAD, ModBlocks.BOOST_RAMP, ModBlocks.STEEP_BOOST_RAMP)));
        registry.register(buildType(Names.TileEntity.GAS_PUMP, TileEntityType.Builder.create(GasPumpTileEntity::new, ModBlocks.GAS_PUMP)));
        registry.register(buildType(Names.TileEntity.GAS_PUMP_TANK, TileEntityType.Builder.create(GasPumpTankTileEntity::new, ModBlocks.GAS_PUMP)));
    }*/
}