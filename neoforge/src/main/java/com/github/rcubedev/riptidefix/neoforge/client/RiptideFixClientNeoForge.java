package com.github.rcubedev.riptidefix.neoforge.client;

import java.util.function.BiFunction;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.configuration.ClientConfigurationPacketListener;

import com.github.rcubedev.riptidefix.client.RiptideFixClient;
import com.github.rcubedev.riptidefix.networking.packets.ClientPacketContext;
//? if >=1.21.10
/*import com.github.rcubedev.riptidefix.networking.packets.S2C.EnableRiptideFixPayload;*/
import com.github.rcubedev.riptidefix.util.RiptideFixConstants;
import net.neoforged.api.distmarker.Dist;
//? if >=1.21.10
/*import net.neoforged.bus.api.SubscribeEvent;*/
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
//? if >=1.21.10
/*import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;*/
import net.neoforged.neoforge.common.extensions.ICommonPacketListener;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@Mod(value = RiptideFixConstants.MOD_ID, dist = Dist.CLIENT)
//? if >=1.21.10
/*@EventBusSubscriber(modid = RiptideFixConstants.MOD_ID, value = Dist.CLIENT)*/
public class RiptideFixClientNeoForge {

    public RiptideFixClientNeoForge() {
        RiptideFixClient.initHelpers();
        RiptideFixClient.init();
    }

    //? if >=1.21.10 {
    /*@SubscribeEvent
    public static void registerPayloadsClient(RegisterClientPayloadHandlersEvent event) {
        event.register(EnableRiptideFixPayload.ID, (payload, context) -> RiptideFixClientNeoForge.wrap(payload, context, RiptideFixClient::handleEnablePayload));
    }
    *///?}

    public static <T extends CustomPacketPayload, R extends CustomPacketPayload> void wrap(T payload, IPayloadContext context, BiFunction<T, ClientPacketContext, R> handler) {
        context.reply(handler.apply(payload, new ClientPacketContext((type) -> hasChannel(context.listener(), type), (ClientConfigurationPacketListener) context.listener())));
    }

    private static <T extends CustomPacketPayload> boolean hasChannel(ICommonPacketListener listener, CustomPacketPayload.Type<T> type) {
        return listener.hasChannel(type);
    }
}
