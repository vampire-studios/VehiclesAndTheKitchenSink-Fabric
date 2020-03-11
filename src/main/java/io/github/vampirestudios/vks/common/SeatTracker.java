package io.github.vampirestudios.vks.common;

import com.google.common.collect.HashBiMap;
import com.mrcrayfish.vehicle.entity.VehicleProperties;
import com.mrcrayfish.vehicle.network.PacketHandler;
import com.mrcrayfish.vehicle.network.message.MessageSyncPlayerSeat;
import io.github.vampirestudios.vks.entity.VehicleEntity;
import io.github.vampirestudios.vks.utils.Constants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.network.PacketDistributor;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.UUID;

/**
 * Author: MrCrayfish
 */
public class SeatTracker
{
    private final int maxSeatSize;
    private HashBiMap<UUID, Integer> playerSeatMap = HashBiMap.create();
    private WeakReference<VehicleEntity> vehicleRef;

    public SeatTracker(VehicleEntity entity)
    {
        this.maxSeatSize = entity.getProperties().getSeats().size();
        this.vehicleRef = new WeakReference<>(entity);
    }

    public int getSeatIndex(UUID uuid)
    {
        if(this.playerSeatMap.containsKey(uuid))
        {
            return this.playerSeatMap.getOrDefault(uuid, -1);
        }
        return -1;
    }

    /**
     * Sets the seat index for the corresponding player uuid. If the uuid already exists
     * in the seating map, it will automatically be updated to the new index.
     *
     * @param index the index of the seat
     * @param uuid the uuid of the player
     */
    public void setSeatIndex(int index, UUID uuid)
    {
        if(index < 0 || index >= this.maxSeatSize)
            return;
        this.playerSeatMap.forcePut(uuid, index);
        VehicleEntity vehicle = this.vehicleRef.get();
        if(vehicle != null && !vehicle.world.isClient)
        {
            PacketHandler.instance.send(PacketDistributor.TRACKING_ENTITY.with(() -> vehicle), new MessageSyncPlayerSeat(vehicle.getEntityId(), index, uuid));
        }
    }

    public boolean isSeatAvailable(int index)
    {
        if(index < 0 || index >= this.maxSeatSize)
            return false;
        if(!this.playerSeatMap.inverse().containsKey(index))
            return true;
        VehicleEntity vehicle = this.vehicleRef.get();
        if(vehicle != null)
        {
            UUID uuid = this.playerSeatMap.inverse().get(index);
            return vehicle.getPassengerList().stream().noneMatch(entity -> entity.getUuid().equals(uuid));
        }
        return false;
    }

    public void remove(UUID uuid)
    {
        this.playerSeatMap.remove(uuid);
    }

    public int getNextAvailableSeat()
    {
        VehicleEntity vehicle = this.vehicleRef.get();
        if(vehicle != null && !vehicle.world.isClient)
        {
            VehicleProperties properties = vehicle.getProperties();
            List<Seat> seats = properties.getSeats();
            for(int i = 0; i < seats.size(); i++)
            {
                if(!this.playerSeatMap.values().contains(i))
                {
                    return i;
                }
                UUID uuid = this.playerSeatMap.inverse().get(i);
                if(vehicle.getPassengerList().stream().noneMatch(entity -> entity.getUuid().equals(uuid)))
                {
                    this.playerSeatMap.remove(uuid);
                    return i;
                }
            }
        }
        return -1;
    }

    public int getClosestAvailableSeatToPlayer(PlayerEntity player)
    {
        VehicleEntity vehicle = this.vehicleRef.get();
        if(vehicle != null && !vehicle.world.isClient)
        {
            VehicleProperties properties = vehicle.getProperties();
            List<Seat> seats = properties.getSeats();

            /* If vehicle is full of passengers, no need to search */
            if(vehicle.getPassengerList().size() == seats.size())
                return -1;

            int closestSeatIndex = -1;
            double closestDistance = 0;
            for(int i = 0; i < seats.size(); i++)
            {
                if(!this.isSeatAvailable(i))
                    continue;

                /* Get the real world distance to the seat and check if it's the closest */
                Seat seat = seats.get(i);
                Vec3d seatVec = seat.getPosition().add(0, properties.getAxleOffset() + properties.getWheelOffset(), 0).scale(properties.getBodyPosition().getScale()).mul(-1, 1, 1).scale(0.0625);
                seatVec = seatVec.rotateY(-(vehicle.getModifiedRotationYaw()) * 0.017453292F);
                seatVec = seatVec.add(vehicle.getPosVector());
                double distance = player.squaredDistanceTo(seatVec.x, seatVec.y - player.getHeight() / 2F, seatVec.z);
                if(closestSeatIndex == -1 || distance < closestDistance)
                {
                    closestSeatIndex = i;
                    closestDistance = distance;
                }
            }
            return closestSeatIndex;
        }
        return -1;
    }

    public CompoundTag write()
    {
        CompoundTag compound = new CompoundTag();
        ListTag list = new ListTag();
        this.playerSeatMap.forEach((uuid, seatIndex) -> {
            CompoundTag seatTag = new CompoundTag();
            seatTag.putUuid("UUID", uuid);
            seatTag.putInt("SeatIndex", seatIndex);
            list.add(seatTag);
        });
        compound.put("PlayerSeatMap", list);
        return compound;
    }

    public void read(CompoundTag compound) {
        if(compound.contains("PlayerSeatMap", Constants.NBT.TAG_LIST)) {
            this.playerSeatMap.clear();
            ListTag list = compound.getList("PlayerSeatMap", Constants.NBT.TAG_COMPOUND);
            list.forEach(nbt -> {
                CompoundTag seatTag = (CompoundTag) nbt;
                UUID uuid = seatTag.getUuid("UUID");
                int seatIndex = seatTag.getInt("SeatIndex");
                this.playerSeatMap.put(uuid, seatIndex);
            });
        }
    }

    public void write(PacketByteBuf buffer)
    {
        buffer.writeVarInt(this.playerSeatMap.size());
        this.playerSeatMap.forEach((uuid, seatIndex) -> {
            buffer.writeUuid(uuid);
            buffer.writeVarInt(seatIndex);
        });
    }

    public void read(PacketByteBuf buffer)
    {
        this.playerSeatMap.clear();
        int size = buffer.readVarInt();
        for(int i = 0; i < size; i++)
        {
            UUID uuid = buffer.readUuid();
            int seatIndex = buffer.readVarInt();
            this.playerSeatMap.put(uuid, seatIndex);
        }
    }
}