package io.github.vampirestudios.vks.utils;

import io.github.vampirestudios.vks.VehiclesAndTheKitchenSink;
import net.minecraft.network.NetworkSide;
import net.minecraft.util.Identifier;

public class BasePacket {

    public static Identifier id(String name, NetworkSide networkSide) {
        return networkSide == NetworkSide.CLIENTBOUND ? new Identifier(VehiclesAndTheKitchenSink.MOD_ID, "client_" + name) :
                new Identifier(VehiclesAndTheKitchenSink.MOD_ID,  "server_" + name);
    }

}
