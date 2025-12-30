package com.github.rcubedev.riptidefix.networking.packets.S2C;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import com.github.rcubedev.riptidefix.util.RiptideFixConstants;

public record EnableRiptideFixPayload(int protocolVersion, boolean enabled) implements CustomPacketPayload {
    public static final ResourceLocation PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(RiptideFixConstants.MOD_ID, "enabled");
    public static final CustomPacketPayload.Type<EnableRiptideFixPayload> ID = new CustomPacketPayload.Type<>(PAYLOAD_ID);
    public static final StreamCodec<FriendlyByteBuf, EnableRiptideFixPayload> CODEC = StreamCodec.composite(ByteBufCodecs.INT, EnableRiptideFixPayload::protocolVersion, ByteBufCodecs.BOOL, EnableRiptideFixPayload::enabled, EnableRiptideFixPayload::new);

    @Override
    public CustomPacketPayload.Type<EnableRiptideFixPayload> type() {
        return ID;
    }
}
