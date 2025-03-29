package msnj.tcwm.util;

import dev.architectury.networking.NetworkManager;
//import msnj.tcwm.mappings.NetworkUtilities;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class Network {
    public static void registerReceiverC2S(ResourceLocation id, FunctionP<MinecraftServer, ServerPlayer, FriendlyByteBuf> packetCallback) {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, id, (packet, context) -> {
            final Player player = context.getPlayer();
            if (player != null) {
                packetCallback.load(player.getServer(), (ServerPlayer) player, packet);
            }
        });
    }
}
