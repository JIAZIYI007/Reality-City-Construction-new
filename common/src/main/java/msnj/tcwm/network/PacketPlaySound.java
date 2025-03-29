package msnj.tcwm.network;

import io.netty.buffer.Unpooled;
import msnj.tcwm.RealityCityConstruction;
import msnj.tcwm.block.StationBroadcaster;
//import msnj.tcwm.mappings.RegistryUtilities;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import mtr.Registry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class PacketPlaySound {
  public static final ResourceLocation PACKET_PLAY_SOUND = new ResourceLocation(RealityCityConstruction.MOD_ID,"packet_play_sound");

  public static void sendPlaySoundS2C(ServerPlayer p, BlockPos blockPos, ResourceLocation rl, float f, StationBroadcaster.StationBroadcasterEntity.Pitch pitch){
    FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
    packet.writeResourceLocation(rl);
    packet.writeFloat(f);
    packet.writeEnum(pitch);
    packet.writeBlockPos(blockPos);
    Registry.sendToPlayer(p, PACKET_PLAY_SOUND, packet);
  }

  public static void receivePlaySoundS2C(FriendlyByteBuf packet){
    ResourceLocation soundID = packet.readResourceLocation();
    float f = packet.readFloat();
    StationBroadcaster.StationBroadcasterEntity.Pitch pitch =
      packet.readEnum(StationBroadcaster.StationBroadcasterEntity.Pitch.class);
    Minecraft minecraft = Minecraft.getInstance();
    //开始播放声音
    BlockPos blockPos = packet.readBlockPos();
    if (minecraft.level == null) {
      RealityCityConstruction.LOGGER.error("Error Play Sound!");
      return;
    }
    minecraft.level.playLocalSound(
      blockPos, #if MC_VERSION >= "11903" SoundEvent.createVariableRangeEvent(soundID) #else new SoundEvent(soundID) #endif, SoundSource.BLOCKS,
      f, pitch.getFloat(), true
    );
  }
}
