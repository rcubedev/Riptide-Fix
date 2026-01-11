package com.github.rcubedev.riptidefix;

import java.util.function.Consumer;

import com.github.rcubedev.riptidefix.networking.packets.C2S.AffirmRiptideFixPayload;
import com.github.rcubedev.riptidefix.networking.packets.PacketConstants;
import com.github.rcubedev.riptidefix.networking.packets.S2C.EnableRiptideFixPayload;
import com.github.rcubedev.riptidefix.networking.packets.ServerPacketContext;
import com.github.rcubedev.riptidefix.networking.tasks.RiptideFixTask;
import com.github.rcubedev.riptidefix.util.RiptideFixConstants;

public class RiptideFix {

    private static boolean enabled = false;

    public static void initHelpers() {}

    public static void init() {}

    public static void configure(ServerPacketContext context, Consumer<RiptideFixTask> addTask) {
        if (!context.hasChannel().test(EnableRiptideFixPayload.ID)) return;
        addTask.accept(new RiptideFixTask(new EnableRiptideFixPayload(PacketConstants.PROTOCOL_VERSION, true)));
    }

    public static void receiveAffirm(AffirmRiptideFixPayload payload, ServerPacketContext context) {
        context.completeTask().accept(RiptideFixTask.TYPE);
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean enabled) {
        RiptideFix.enabled = enabled;
    }
}
