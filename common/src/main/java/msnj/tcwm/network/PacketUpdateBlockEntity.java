package msnj.tcwm.network;
import io.netty.buffer.Unpooled;
import msnj.tcwm.RealityCityConstruction;
import msnj.tcwm.util.Logger;
//import msnj.tcwm.mappings.BlockEntityClientSerializableMapper;
import msnj.tcwm.util.TcwmBlockEntity;
import mtr.RegistryClient;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class PacketUpdateBlockEntity {

  public static ResourceLocation PACKET_UPDATE_BLOCK_ENTITY = new ResourceLocation(RealityCityConstruction.MOD_ID, "update_block_entity");

  public static void sendUpdateC2S(TcwmBlockEntity blockEntity, BlockPos pos) {
    Level level = blockEntity.getLevel();
    if (level == null) return;

    final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
    packet.writeResourceLocation(level.dimension().location());
    packet.writeBlockPos(pos);
#if MC_VERSION >= "11903"
    packet.writeId(net.minecraft.core.registries.BuiltInRegistries.BLOCK_ENTITY_TYPE, blockEntity.getType());
#else
    packet.writeVarInt(net.minecraft.core.Registry.BLOCK_ENTITY_TYPE.getId(blockEntity.getType()));
#endif
    CompoundTag tag = new CompoundTag();
    blockEntity.saveAdditional(tag);
    packet.writeNbt(tag);
    RegistryClient.sendToServer(PACKET_UPDATE_BLOCK_ENTITY, packet);
  }

  public static void receiveUpdateC2S(MinecraftServer server, ServerPlayer player, FriendlyByteBuf packet) {
#if MC_VERSION >= "11903"
    ResourceKey<Level> levelKey = packet.readResourceKey(net.minecraft.core.registries.Registries.DIMENSION);
#else
    ResourceKey<Level> levelKey = ResourceKey.create(net.minecraft.core.Registry.DIMENSION_REGISTRY, packet.readResourceLocation());
#endif
    BlockPos blockPos = packet.readBlockPos();
#if MC_VERSION >= "11903"
    BlockEntityType<?> blockEntityType = packet.readById(net.minecraft.core.registries.BuiltInRegistries.BLOCK_ENTITY_TYPE);
#else
    BlockEntityType<?> blockEntityType = net.minecraft.core.Registry.BLOCK_ENTITY_TYPE.byId(packet.readVarInt());
#endif

    CompoundTag compoundTag = packet.readNbt();
    new Logger(System.out).info("[D    E    B    U    G] " + compoundTag);

    #if MC_VERSION <= "11700"
    server.execute(() -> {
      ServerLevel level = server.getLevel(levelKey);
      if (level == null || blockEntityType == null) return;
      BlockEntity blockEntity = level.getBlockEntity(blockPos);
      if(blockEntity.getType().equals(blockEntityType)) {
        if (compoundTag == null) return;
        blockEntity.load(blockEntity.getBlockState(), compoundTag);
        blockEntity.setPosition(blockPos);
        blockEntity.setChanged();
        level.getChunkSource().blockChanged(blockPos);
      }
    });
    #else
    server.execute(() -> {
      ServerLevel level = server.getLevel(levelKey);
      if (level == null || blockEntityType == null) return;
      level.getBlockEntity(blockPos, blockEntityType).ifPresent(blockEntity -> {
        if (compoundTag != null) {
          blockEntity.load(compoundTag);
          blockEntity.setChanged();
          level.getChunkSource().blockChanged(blockPos);
        }
      });
    });
    #endif
  }
}
