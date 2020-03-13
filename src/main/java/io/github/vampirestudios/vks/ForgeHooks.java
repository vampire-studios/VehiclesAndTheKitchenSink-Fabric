package io.github.vampirestudios.vks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SkullItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class ForgeHooks {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void onPickBlock(HitResult target, PlayerEntity player, World world) {
        ItemStack result = ItemStack.EMPTY;
        boolean isCreative = player.abilities.creativeMode;
        BlockEntity te = null;

        if (target.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = ((BlockHitResult)target).getBlockPos();
            BlockState state = world.getBlockState(pos);

            if (state.isAir())
                return;

            if (isCreative && Screen.hasControlDown())
                te = world.getBlockEntity(pos);

            result = state.getBlock().getPickStack(world, pos, state);

            if (result.isEmpty())
                LOGGER.warn("Picking on: [{}] {} gave null item", target.getType(), Registry.BLOCK.getId(state.getBlock()));
        }
        else if (target.getType() == HitResult.Type.ENTITY) {
            Entity entity = ((EntityHitResult)target).getEntity();
            result = ((IForgeEntity)entity).getPickedResult(target);

            if (result.isEmpty())
                LOGGER.warn("Picking on: [{}] {} gave null item", target.getType(), Registry.ENTITY_TYPE.getId(entity.getType()));
    }

        if (result.isEmpty())
            return;

        if (te != null)
            addBlockEntityNbt(result, te);

        if (isCreative) {
            player.inventory.addPickBlock(result);
            Objects.requireNonNull(MinecraftClient.getInstance().interactionManager).clickCreativeStack(player.getStackInHand(Hand.MAIN_HAND),
                    36 + player.inventory.selectedSlot);
            return;
        }
        int slot = player.inventory.getSlotWithStack(result);
        if (slot != -1) {
            if (PlayerInventory.isValidHotbarIndex(slot))
                player.inventory.selectedSlot = slot;
            else
                Objects.requireNonNull(MinecraftClient.getInstance().interactionManager).pickFromInventory(slot);
        }
    }

    private static void addBlockEntityNbt(ItemStack stack, BlockEntity blockEntity) {
        CompoundTag compoundTag = blockEntity.toTag(new CompoundTag());
        CompoundTag compoundTag3;
        if (stack.getItem() instanceof SkullItem && compoundTag.contains("Owner")) {
            compoundTag3 = compoundTag.getCompound("Owner");
            stack.getOrCreateTag().put("SkullOwner", compoundTag3);
        } else {
            stack.putSubTag("BlockEntityTag", compoundTag);
            compoundTag3 = new CompoundTag();
            ListTag listTag = new ListTag();
            listTag.add(StringTag.of("\"(+NBT)\""));
            compoundTag3.put("Lore", listTag);
            stack.putSubTag("display", compoundTag3);
        }
    }

}
