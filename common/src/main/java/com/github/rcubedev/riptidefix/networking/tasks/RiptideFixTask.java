package com.github.rcubedev.riptidefix.networking.tasks;

import java.util.function.Consumer;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.network.ConfigurationTask;

import com.github.rcubedev.riptidefix.util.RiptideFixConstants;

public record RiptideFixTask(CustomPacketPayload payload) implements ConfigurationTask {
    public static final Type TYPE = new Type(ResourceLocation.fromNamespaceAndPath(RiptideFixConstants.MOD_ID, "enabled").toString());


    @Override
    public void start(Consumer<Packet<?>> sender) {
        sender.accept(new ClientboundCustomPayloadPacket(payload));
    }

    @Override
    public Type type() {
        return TYPE;
    }
}