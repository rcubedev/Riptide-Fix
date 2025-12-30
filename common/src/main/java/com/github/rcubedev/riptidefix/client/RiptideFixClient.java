package com.github.rcubedev.riptidefix.client;

import com.github.rcubedev.riptidefix.RiptideFix;
import com.github.rcubedev.riptidefix.networking.packets.ClientPacketContext;
import com.github.rcubedev.riptidefix.networking.packets.PacketConstants;
import com.github.rcubedev.riptidefix.networking.packets.C2S.AffirmRiptideFixPayload;
import com.github.rcubedev.riptidefix.networking.packets.S2C.EnableRiptideFixPayload;
import com.github.rcubedev.riptidefix.util.RiptideFixConstants;

public class RiptideFixClient {

    public static void initHelpers() {
    }

    public static void init() {
    }

    public static AffirmRiptideFixPayload handleEnablePayload(EnableRiptideFixPayload payload, ClientPacketContext context) {
        RiptideFixConstants.LOG.info("[CLIENT] Sending Riptide Fix response packet to server.");
        if (payload.protocolVersion() == PacketConstants.PROTOCOL_VERSION) {
            RiptideFixConstants.LOG.info("[CLIENT] Enabled? {}", payload.enabled());
            RiptideFix.setEnabled(payload.enabled());
        }
        return new AffirmRiptideFixPayload(PacketConstants.PROTOCOL_VERSION, payload.enabled());
    }
}
