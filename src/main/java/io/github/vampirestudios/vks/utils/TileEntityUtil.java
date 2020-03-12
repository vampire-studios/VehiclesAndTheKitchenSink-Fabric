package io.github.vampirestudios.vks.utils;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.stream.Stream;

/**
 * Author: MrCrayfish
 */
public class TileEntityUtil {
    /**
     * Sends an update packet to clients tracking a tile entity.
     *
     * @param tileEntity the tile entity to update
     */
    public static void sendUpdatePacket(BlockEntity tileEntity) {
        BlockEntityUpdateS2CPacket packet = tileEntity.toUpdatePacket();
        if(packet != null)
        {
            sendUpdatePacket(tileEntity.getWorld(), tileEntity.getPos(), packet);
        }
    }

    /**
     * Sends an update packet to clients tracking a tile entity with a specific CompoundNBT
     *
     * @param tileEntity the tile entity to update
     */
    public static void sendUpdatePacket(BlockEntity tileEntity, CompoundTag compound)
    {
        BlockEntityUpdateS2CPacket packet = new BlockEntityUpdateS2CPacket(tileEntity.getPos(), 0, compound);
        sendUpdatePacket(tileEntity.getWorld(), tileEntity.getPos(), packet);
    }

    /**
     * Sends an update packet but only to a specific player. This helps reduce overhead on the network
     * when you only want to update a tile entity for a single player rather than everyone who is
     * tracking the tile entity.
     *
     * @param tileEntity the tile entity to update
     * @param player the player to send the update to
     */
    public static void sendUpdatePacket(BlockEntity tileEntity, ServerPlayerEntity player)
    {
        sendUpdatePacket(tileEntity, tileEntity.toInitialChunkDataTag(), player);
    }

    /**
     * Sends an update packet with a custom nbt compound but only to a specific player. This helps
     * reduce overhead on the network when you only want to update a tile entity for a single player
     * rather than everyone who is tracking the tile entity.
     *
     * @param tileEntity the tile entity to update
     * @param compound the update tag to send
     * @param player the player to send the update to
     */
    public static void sendUpdatePacket(BlockEntity tileEntity, CompoundTag compound, ServerPlayerEntity player)
    {
        BlockEntityUpdateS2CPacket packet = new BlockEntityUpdateS2CPacket(tileEntity.getPos(), 0, compound);
        player.networkHandler.sendPacket(packet);
    }

    private static void sendUpdatePacket(World world, BlockPos pos, BlockEntityUpdateS2CPacket packet)
    {
        if(world instanceof ServerWorld)
        {
            ServerWorld server = (ServerWorld) world;
            Stream<ServerPlayerEntity> players = server.getChunkManager().threadedAnvilChunkStorage.getPlayersWatchingChunk(new ChunkPos(pos), false);
            players.forEach(player -> player.networkHandler.sendPacket(packet));
        }
    }
}