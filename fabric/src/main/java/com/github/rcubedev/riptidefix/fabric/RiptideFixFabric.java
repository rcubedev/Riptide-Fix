package com.github.rcubedev.riptidefix.fabric;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;

import com.github.rcubedev.riptidefix.RiptideFix;
import com.github.rcubedev.riptidefix.networking.packets.C2S.AffirmRiptideFixPayload;
import com.github.rcubedev.riptidefix.networking.packets.S2C.EnableRiptideFixPayload;
import com.github.rcubedev.riptidefix.networking.packets.ServerPacketContext;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;

public class RiptideFixFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        RiptideFix.initHelpers();

        PayloadTypeRegistry.configurationS2C().register(EnableRiptideFixPayload.ID, EnableRiptideFixPayload.CODEC);
        PayloadTypeRegistry.configurationC2S().register(AffirmRiptideFixPayload.ID, AffirmRiptideFixPayload.CODEC);

        RiptideFix.init();
        ServerConfigurationConnectionEvents.CONFIGURE.register((listener, server) -> wrap(listener, RiptideFix::configure));
        ServerConfigurationNetworking.registerGlobalReceiver(AffirmRiptideFixPayload.ID, (payload, context) -> wrap(payload, context, RiptideFix::receiveAffirm));
    }

    /**
     * Wraps a server configuration handler to provide a ServerPacketContext.
     * This overload is used to register configuration tasks, which is registered
     * via the callback.
     *
     * @param listener The server configuration packet listener
     * @param handler The handler that processes the listener and context
     * @param <T> The type of the configuration task
     */
    public static <T extends ConfigurationTask> void wrap(ServerConfigurationPacketListenerImpl listener, BiConsumer<ServerPacketContext, Consumer<T>> handler) {
        handler.accept(new ServerPacketContext((type) -> hasChannel(listener, type), listener, null, listener::completeTask), listener::addTask);
    }

    /**
     * Wraps a server configuration handler to provide a ServerPacketContext.
     * This overload is used to register packet receivers, with an optional response
     * that is managed by the handler via {@link ServerPacketContext#sendPacket()}.
     *
     * @param payload The received payload
     * @param context The networking context
     * @param handler The handler that processes the payload and context
     * @param <T> The type of the received payload
     */
    public static <T extends CustomPacketPayload> void wrap(T payload, ServerConfigurationNetworking.Context context, BiConsumer<T, ServerPacketContext> handler) {
        handler.accept(payload, new ServerPacketContext((type) -> hasChannel(context.networkHandler(), type), context.networkHandler(), (response) -> context.responseSender().sendPacket(response), (task) -> context.networkHandler().completeTask(task)));
    }

    /**
     * Wraps a server configuration handler to provide a ServerPacketContext.
     * This overload is used to register packet receivers, where the payload
     * is the return value from the handler.
     *
     * @param payload The received payload
     * @param context The networking context
     * @param handler The handler that processes the payload and context to produce a response payload
     * @param <T> The type of the received payload
     * @param <R> The type of the response payload
     * @deprecated Use another overload and use {@link ServerPacketContext#sendPacket()} to send a response.
     */
    @Deprecated
    public static <T extends CustomPacketPayload, R extends CustomPacketPayload> void wrap(T payload, ServerConfigurationNetworking.Context context, BiFunction<T, ServerPacketContext, R> handler) {
        context.responseSender().sendPacket(handler.apply(payload, new ServerPacketContext((type) -> hasChannel(context.networkHandler(), type), context.networkHandler(), (response) -> context.responseSender().sendPacket(response), (task) -> context.networkHandler().completeTask(task))));
    }

    private static <T extends CustomPacketPayload> boolean hasChannel(ServerConfigurationPacketListenerImpl listener, CustomPacketPayload.Type<T> type) {
        return ServerConfigurationNetworking.canSend(listener, type);
    }
}
