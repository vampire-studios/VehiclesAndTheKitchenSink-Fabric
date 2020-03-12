package io.github.vampirestudios.vks.block;

import com.google.common.base.Strings;
import io.github.vampirestudios.vks.block.entity.VehicleCrateTileEntity;
import io.github.vampirestudios.vks.entity.EngineTier;
import io.github.vampirestudios.vks.entity.WheelType;
import io.github.vampirestudios.vks.init.ModBlocks;
import io.github.vampirestudios.vks.init.ModItems;
import io.github.vampirestudios.vks.utils.Bounds;
import io.github.vampirestudios.vks.utils.Constants;
import io.github.vampirestudios.vks.utils.Names;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.particle.BlockDustParticle;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Author: MrCrayfish
 */
public class BlockVehicleCrate extends BlockRotatedObject implements BlockEntityProvider {
    public static final List<Identifier> REGISTERED_CRATES = new ArrayList<>();

    private static final Box PANEL = new Bounds(0, 0, 0, 16, 2, 16).toAABB(); //TODO add collisions back

    public BlockVehicleCrate() {
        super(Names.Block.VEHICLE_CRATE, Block.Settings.of(Material.METAL, DyeColor.LIGHT_GRAY).strength(1.5F, 5.0F).nonOpaque());
    }

    @Override
    public boolean isTranslucent(BlockState state, BlockView reader, BlockPos pos)
    {
        return true;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView worldIn, BlockPos pos, EntityContext context) {
        return VoxelShapes.fullCube();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext ePos) {
        return VoxelShapes.fullCube();
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return this.isBelowBlockTopSolid(world, pos) && this.canOpen(world, pos);
    }

    private boolean canOpen(WorldView reader, BlockPos pos) {
        for(Direction side : Direction.Type.HORIZONTAL) {
            BlockPos adjacentPos = pos.offset(side);
            BlockState state = reader.getBlockState(adjacentPos);
            if(state.isAir())
                continue;
            if(!state.getMaterial().isReplaceable() || this.isBelowBlockTopSolid(reader, adjacentPos)) {
                return false;
            }
        }
        return true;
    }

    private boolean isBelowBlockTopSolid(WorldView reader, BlockPos pos) {
        return reader.getBlockState(pos.down()).isSideSolidFullSquare(reader, pos.down(), Direction.UP);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockHitResult result) {
        if(result.getSide() == Direction.UP && playerEntity.getStackInHand(hand).getItem() == ModItems.WRENCH) {
            this.openCrate(world, pos, state, playerEntity);
            return ActionResult.SUCCESS;
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, ItemStack stack) {
        if(livingEntity instanceof PlayerEntity && ((PlayerEntity) livingEntity).isCreative()) {
            this.openCrate(world, pos, state, livingEntity);
        }
    }

    private void openCrate(World world, BlockPos pos, BlockState state, LivingEntity placer) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if(tileEntity instanceof VehicleCrateTileEntity && this.canOpen(world, pos)) {
            if(world.isClient) {
                this.spawnCrateOpeningParticles(world, pos, state);
            }
            else {
                ((VehicleCrateTileEntity) tileEntity).open(placer.getUuid());
            }
        }
    }

    @Environment(EnvType.CLIENT)
    private void spawnCrateOpeningParticles(World world, BlockPos pos, BlockState state) {
        double y = 0.875;
        double x, z;
        BlockDustParticle.Factory factory = new BlockDustParticle.Factory();
        for(int j = 0; j < 4; ++j) {
            for(int l = 0; l < 4; ++l) {
                x = (j + 0.5D) / 4.0D;
                z = (l + 0.5D) / 4.0D;
                MinecraftClient.getInstance().particleManager.addParticle(factory.createParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, state), world, pos.getX() + x, pos.getY() + y, pos.getZ() + z, x - 0.5D, y - 0.5D, z - 0.5D));
            }
        }
    }

    @Override
    public boolean hasBlockEntity()
    {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world)
    {
        return new VehicleCrateTileEntity();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void buildTooltip(ItemStack stack, @Nullable BlockView reader, List<Text> list, TooltipContext advanced)
    {
        String vehicle = "vehicle";
        CompoundTag tagCompound = stack.getTag();
        if(tagCompound != null)
        {
            if(tagCompound.contains("BlockEntityTag", Constants.NBT.TAG_COMPOUND))
            {
                CompoundTag blockEntityTag = tagCompound.getCompound("BlockEntityTag");
                vehicle = blockEntityTag.getString("Vehicle");
                if(!Strings.isNullOrEmpty(vehicle))
                {
                    vehicle = I18n.translate("entity.vehicle." + vehicle.split(":")[1]);
                    list.add(new LiteralText(Formatting.BLUE + vehicle));
                }
            }
        }

        if(Screen.hasShiftDown())
        {
            String info = I18n.translate(this.getTranslationKey() + ".info", vehicle);
            list.addAll(MinecraftClient.getInstance().textRenderer.wrapStringToWidthAsList(info, 150).stream().map((Function<String, Text>) LiteralText::new).collect(Collectors.toList()));
        }
        else
        {
            list.add(new LiteralText(Formatting.YELLOW + I18n.translate("vehicle.info_help")));
        }
    }

    //TODO turn this into a builder
    public static ItemStack create(Identifier entityId, int color, @Nullable EngineTier engineTier, @Nullable WheelType wheelType, int wheelColor) {
        CompoundTag blockEntityTag = new CompoundTag();
        blockEntityTag.putString("Vehicle", entityId.toString());
        blockEntityTag.putInt("Color", color);

        if(engineTier != null) {
            blockEntityTag.putInt("EngineTier", engineTier.ordinal());
        }

        if(wheelType != null) {
            blockEntityTag.putInt("WheelType", wheelType.ordinal());
            if(wheelColor != -1) {
                blockEntityTag.putInt("WheelColor", wheelColor);
            }
        }

        CompoundTag itemTag = new CompoundTag();
        itemTag.put("BlockEntityTag", blockEntityTag);
        ItemStack stack = new ItemStack(ModBlocks.VEHICLE_CRATE);
        stack.setTag(itemTag);
        return stack;
    }

    public static void registerVehicle(String id) {
        Identifier resource = new Identifier(id);
        if(!REGISTERED_CRATES.contains(resource)) {
            REGISTERED_CRATES.add(resource);
            Collections.sort(REGISTERED_CRATES);
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if(!world.isClient && !player.isCreative()) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if(tileEntity instanceof VehicleCrateTileEntity) {
                ItemStack drop = new ItemStack(Item.fromBlock(this));

                CompoundTag tileEntityTag = new CompoundTag();
                tileEntity.toTag(tileEntityTag);
                tileEntityTag.remove("x");
                tileEntityTag.remove("y");
                tileEntityTag.remove("z");
                tileEntityTag.remove("id");

                CompoundTag compound = new CompoundTag();
                compound.put("BlockEntityTag", tileEntityTag);
                drop.setTag(compound);

                world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop));
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
            }
        }
    }

}