package com.github.rcubedev.riptidefix.fabric.client;

import java.util.function.BiFunction;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import com.github.rcubedev.riptidefix.client.RiptideFixClient;
import com.github.rcubedev.riptidefix.networking.packets.ClientPacketContext;
import com.github.rcubedev.riptidefix.networking.packets.S2C.EnableRiptideFixPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;

public class RiptideFixClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        RiptideFixClient.initHelpers();
        RiptideFixClient.init();

        ClientConfigurationNetworking.registerGlobalReceiver(EnableRiptideFixPayload.ID, (payload, context) -> wrap(payload, context, RiptideFixClient::handleEnablePayload));
    }

    private <T extends CustomPacketPayload, R extends CustomPacketPayload> void wrap(T payload, ClientConfigurationNetworking.Context context, BiFunction<T, ClientPacketContext, R> handler) {
        context.responseSender().sendPacket(handler.apply(payload, new ClientPacketContext(RiptideFixClientFabric::hasChannel, context.networkHandler())));
    }

    private static <T extends CustomPacketPayload> boolean hasChannel(CustomPacketPayload.Type<T> type) {
        return ClientConfigurationNetworking.canSend(type);
    }
}
