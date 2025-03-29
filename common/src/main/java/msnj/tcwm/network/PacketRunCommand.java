package msnj.tcwm.network;

import io.netty.buffer.Unpooled;
import msnj.tcwm.RealityCityConstruction;
import msnj.tcwm.util.settings.JsonIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import mtr.Registry;

public class PacketRunCommand {
    public static final ResourceLocation PACKET_RUNCOMMAND = new ResourceLocation(RealityCityConstruction.MOD_ID, "packet_run_command");

    public static int sendRunCommandS2C(ServerPlayer sp, String commandName) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeUtf(commandName);
        Registry.sendToPlayer(sp, PACKET_RUNCOMMAND, packet);
        return 1;
    }

    public static void receiveCommandS2C(FriendlyByteBuf receive) {
        String commandName = receive.readUtf();
        switch (commandName) {
            case "RCCMOD":
                Minecraft.getInstance().execute(() ->
                        Minecraft.getInstance().setScreen(JsonIO.getConfigScreen(new ChatScreen("/tcwm settings"))));
        }
    }
}
