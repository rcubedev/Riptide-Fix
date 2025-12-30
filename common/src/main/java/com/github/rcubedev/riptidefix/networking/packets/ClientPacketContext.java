package com.github.rcubedev.riptidefix.networking.packets;

import java.util.function.Predicate;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.configuration.ClientConfigurationPacketListener;

public record ClientPacketContext(Predicate<CustomPacketPayload.Type<? extends CustomPacketPayload>> hasChannel, ClientConfigurationPacketListener packetListener) {}
