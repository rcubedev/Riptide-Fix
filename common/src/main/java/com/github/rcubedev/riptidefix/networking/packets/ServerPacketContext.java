package com.github.rcubedev.riptidefix.networking.packets;

import java.util.function.Consumer;
import java.util.function.Predicate;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener;
import net.minecraft.server.network.ConfigurationTask;

import org.jetbrains.annotations.Nullable;

public record ServerPacketContext(Predicate<CustomPacketPayload.Type<? extends CustomPacketPayload>> hasChannel,
                                  ServerConfigurationPacketListener packetListener,
                                  @Nullable Consumer<? extends CustomPacketPayload> sendPacket,
                                  @Nullable Consumer<ConfigurationTask.Type> completeTask) {}
