package io.github.vampirestudios.vks.block.entity;

import io.github.vampirestudios.vks.utils.TileEntityUtil;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;

import javax.annotation.Nullable;

public class TileEntitySynced extends BlockEntity {

    public TileEntitySynced(BlockEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public void syncToClient() {
        this.markDirty();
        TileEntityUtil.sendUpdatePacket(this);
    }

    @Override
    public CompoundTag toInitialChunkDataTag()
    {
        return this.toTag(new CompoundTag());
    }

    @Nullable
    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return new BlockEntityUpdateS2CPacket(this.getPos(), 0, this.toInitialChunkDataTag());
    }

    /*@Override
    public void onDataPacket(final NetworkManager net, final SUpdateTileEntityPacket pkt)
    {
        this.read(pkt.getNbtCompound());
    }*/
}