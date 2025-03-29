package msnj.tcwm.network;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import msnj.tcwm.RealityCityConstruction;
import msnj.tcwm.block.StationBroadcaster;
//import msnj.tcwm.mappings.NetworkUtilities;
//import mtr.Registry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class PacketScreen {
  public static ResourceLocation PACKET_SHOW_SCREEN = new ResourceLocation(RealityCityConstruction.MOD_ID, "show_screen");

  public static void sendScreenBlockS2C(ServerPlayer player, String screenName, BlockPos pos) {
    final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
    packet.writeUtf(screenName);
    packet.writeBlockPos(pos);
    packet.resetReaderIndex();
    NetworkManager.sendToPlayer(player, PACKET_SHOW_SCREEN, packet);
  }

  public static void receiveScreenS2C(FriendlyByteBuf packet) {
    MakeClassLoaderHappy.receiveScreenS2C(packet);
  }

  private static class MakeClassLoaderHappy {
    public static void receiveScreenS2C(FriendlyByteBuf packet) {
      Minecraft minecraftClient = Minecraft.getInstance();
      String screenName = packet.readUtf();
      BlockPos pos = packet.readBlockPos();
      minecraftClient.execute(() -> {
        switch (screenName) {
          case "SSBAS":
            minecraftClient.setScreen(StationBroadcaster.propertiesScreen.apply(pos));
            break;
        }
      });
    }
  }
}
