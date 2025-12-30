package com.github.rcubedev.riptidefix.networking.packets.C2S;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import com.github.rcubedev.riptidefix.util.RiptideFixConstants;

public record AffirmRiptideFixPayload(int protocolVersion, boolean enabled) implements CustomPacketPayload {
    public static final ResourceLocation PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(RiptideFixConstants.MOD_ID, "affirm");
    public static final CustomPacketPayload.Type<AffirmRiptideFixPayload> ID = new CustomPacketPayload.Type<>(PAYLOAD_ID);
    public static final StreamCodec<FriendlyByteBuf, AffirmRiptideFixPayload> CODEC = StreamCodec.composite(ByteBufCodecs.INT, AffirmRiptideFixPayload::protocolVersion, ByteBufCodecs.BOOL, AffirmRiptideFixPayload::enabled, AffirmRiptideFixPayload::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
