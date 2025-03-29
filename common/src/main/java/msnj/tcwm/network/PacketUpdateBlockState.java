package msnj.tcwm.network;

import io.netty.buffer.Unpooled;
import msnj.tcwm.RealityCityConstruction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import mtr.RegistryClient;
import net.minecraft.world.level.block.state.BlockState;

public class PacketUpdateBlockState {
  public static final ResourceLocation PACKET_UPDATE_BLOCK_STATE = new ResourceLocation(RealityCityConstruction.MOD_ID, "packet_update_block_state");

  public static final void sendUpdateBlockStateC2S(BlockState state, BlockPos pos){
    FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
    int newStateId = Block.BLOCK_STATE_REGISTRY.getId(state);
    packet.writeInt(newStateId);
    packet.writeBlockPos(pos);
    RegistryClient.sendToServer(PACKET_UPDATE_BLOCK_STATE, packet);
  }

  public static final void receiveUpdateBlockStateC2S(MinecraftServer server, ServerPlayer player, FriendlyByteBuf packet){
    int newStateId = packet.readInt();
    BlockPos pos = packet.readBlockPos();
    BlockState newBlockState = Block.BLOCK_STATE_REGISTRY.byId(newStateId);

    ServerLevel level = #if MC_VERSION < "12000" player.getLevel() #else player.serverLevel() #endif; // 获取世界
    level.setBlock(pos, newBlockState, 3);
  }
}
