package com.github.rcubedev.riptidefix.neoforge;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener;
import net.minecraft.server.network.ConfigurationTask;

import com.github.rcubedev.riptidefix.RiptideFix;
import com.github.rcubedev.riptidefix.client.RiptideFixClient;
//? if <1.21.10
import com.github.rcubedev.riptidefix.neoforge.client.RiptideFixClientNeoForge;
import com.github.rcubedev.riptidefix.networking.packets.C2S.AffirmRiptideFixPayload;
import com.github.rcubedev.riptidefix.networking.packets.PacketConstants;
import com.github.rcubedev.riptidefix.networking.packets.S2C.EnableRiptideFixPayload;
import com.github.rcubedev.riptidefix.networking.packets.ServerPacketContext;
import com.github.rcubedev.riptidefix.util.RiptideFixConstants;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.extensions.ICommonPacketListener;
import net.neoforged.neoforge.network.event.RegisterConfigurationTasksEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@Mod(RiptideFixConstants.MOD_ID)
@EventBusSubscriber(modid = RiptideFixConstants.MOD_ID)
public class RiptideFixNeoForge {

    public RiptideFixNeoForge() {
        RiptideFix.initHelpers();
        RiptideFix.init();
    }

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(Integer.toString(PacketConstants.PROTOCOL_VERSION)).executesOn(HandlerThread.NETWORK).optional();

        registrar.configurationToClient(EnableRiptideFixPayload.ID, EnableRiptideFixPayload.CODEC /*? if <1.21.10 {*/, (payload, context) -> RiptideFixClientNeoForge.wrap(payload, context, RiptideFixClient::handleEnablePayload)/*?}*/);
        registrar.configurationToServer(AffirmRiptideFixPayload.ID, AffirmRiptideFixPayload.CODEC, (payload, context) -> wrap(payload, context, RiptideFix::receiveAffirm));
    }

    @SubscribeEvent
    public static void registerTasks(RegisterConfigurationTasksEvent event) {
        wrap(event.getListener(), event::register, RiptideFix::configure);
    }

    /**
     * Wraps a server configuration handler to provide a ServerPacketContext.
     * This overload is used to register configuration tasks, which is registered
     * via the callback.
     *
     * @param listener The server configuration packet listener
     * @param addTask The callback to register configuration tasks
     * @param handler The handler that processes the listener and context
     * @param <T> The type of the configuration task
     */
    public static <T extends ConfigurationTask> void wrap(ServerConfigurationPacketListener listener, Consumer<T> addTask, BiConsumer<ServerPacketContext, Consumer<T>> handler) {
        handler.accept(new ServerPacketContext((type) -> hasChannel(listener, type), listener, null, null), addTask);
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
    public static <T extends CustomPacketPayload> void wrap(T payload, IPayloadContext context, BiConsumer<T, ServerPacketContext> handler) {
        handler.accept(payload, new ServerPacketContext((type) -> hasChannel(context.listener(), type), (ServerConfigurationPacketListener) context.listener(), context::reply, context::finishCurrentTask));
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
    public static <T extends CustomPacketPayload, R extends CustomPacketPayload> void wrap(T payload, IPayloadContext context, BiFunction<T, ServerPacketContext, R> handler) {
        context.reply(handler.apply(payload, new ServerPacketContext((type) -> hasChannel(context.listener(), type), (ServerConfigurationPacketListener) context.listener(), context::reply, context::finishCurrentTask)));
    }

    private static <T extends CustomPacketPayload> boolean hasChannel(ICommonPacketListener listener, CustomPacketPayload.Type<T> type) {
        return listener.hasChannel(type);
    }
}
