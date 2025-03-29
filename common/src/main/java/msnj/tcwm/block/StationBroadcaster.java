package msnj.tcwm.block;

import msnj.tcwm.RealityCityConstruction;
import msnj.tcwm.item.Items;
import msnj.tcwm.network.PacketScreen;
import msnj.tcwm.util.TcwmBlockEntity;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
#if MC_VERSION < "11903"
import net.minecraft.network.protocol.game.ClientboundCustomSoundPacket;
#endif
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class StationBroadcaster extends Block implements EntityBlock {
    public static Function<BlockPos, Screen> propertiesScreen = null;
    private static final BooleanProperty POWERED = BooleanProperty.create("powered");

    public StationBroadcaster() {
        super(Blocks.copyProperties(net.minecraft.world.level.block.Blocks.STONE));
        this.registerDefaultState(this.getStateDefinition().any().setValue(POWERED, false));
    }

    //此处放广播器代码
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StationBroadcasterEntity(pos, state);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return (BlockState) this.defaultBlockState().setValue(POWERED, blockPlaceContext.getLevel().hasNeighborSignal(blockPlaceContext.getClickedPos()));
    }

    public StationBroadcasterEntity getBlockEntity(Level level, BlockPos blockPos) {
        StationBroadcasterEntity blockEntity;
        BlockEntity temp = level.getBlockEntity(blockPos);
        if (temp instanceof StationBroadcasterEntity) {
            blockEntity = (StationBroadcasterEntity) temp;
            return blockEntity;
        } else {
            return null;
        }
    }

    @Override
    public InteractionResult use(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        //open BroadCaster Screen
        if (player.isHolding(Items.FORGE_TOOL.get())) {
            if (!level.isClientSide) {
                PacketScreen.sendScreenBlockS2C((ServerPlayer) player, "SSBAS", blockPos);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        if (!level.isClientSide) {
            boolean bl2 = (Boolean) blockState.getValue(POWERED);
            if (bl2 != level.hasNeighborSignal(blockPos)) {
                if (bl2) {
                    level.setBlock(blockPos, (BlockState) blockState.setValue(POWERED, false), 1 | 2);
                } else {
                    level.setBlock(blockPos, blockState.setValue(POWERED, true), 1 | 2);
                    playSound(level, blockPos);
                }
            }
        }
    }

    public void playSound(Level level, BlockPos blockPos) {
        StationBroadcasterEntity s = getBlockEntity(level, blockPos);
        ResourceLocation soundEventId = new ResourceLocation(s.getSoundID());

        if (!level.isClientSide()) {
            Vec3 vpos = new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            level.players().forEach((player) ->
            {
                Holder<SoundEvent> hse = Holder.direct(#if MC_VERSION >= "11903" SoundEvent.createVariableRangeEvent #else new SoundEvent #endif(soundEventId));
                Packet<?> packet = #if MC_VERSION < "11903"
                    new ClientboundCustomSoundPacket(soundEventId, SoundSource.BLOCKS, vpos, s.getRange(), s.getPitch().fLoat#if MC_VERSION >= "11900" , 0 #endif)
                #else
                    new ClientboundSoundPacket(hse, SoundSource.BLOCKS, vpos.x, vpos.y, vpos.z, s.getRange(), s.getPitch().fLoat, level.getRandom().nextLong())
                #endif;
                ((ServerPlayer) player).connection.send(packet);
            });
        } else {
            RealityCityConstruction.LOGGER.error("Failed to Play Sound to Player.");
        }
    }

    public static class StationBroadcasterEntity extends TcwmBlockEntity {
        //存放方块实体的变量
        private String soundID = "tcwm:music.example";
        private float range = 1.4F;
        private Pitch pitch = Pitch.DEFAULT;

        //方块实体代码
        public StationBroadcasterEntity(BlockPos blockPos, BlockState blockState) {
            super(Blocks.BlockEntityTypes.HOMO_STATION_BROADCASTER.get(), blockPos, blockState);
        }

        @Override
        public final void load(CompoundTag compoundTag) {
            super.load(compoundTag);
            soundID = compoundTag.getString("soundID");
            range = compoundTag.getFloat("range");
            pitch = Pitch.getValue(compoundTag.getFloat("pitch"));
        }

        public float getRange() {
            return range;
        }

        public Pitch getPitch() {
            return pitch;
        }

        public String getSoundID() {
            return soundID;
        }

        public void setPitch(Pitch pitch) {
            this.pitch = pitch;
        }

        public void setSoundID(String soundID) {
            this.soundID = soundID;
        }

        public void setRange(float range) {
            this.range = range;
        }

        @Override
        public final void saveAdditional(CompoundTag compoundTag) {
            super.saveAdditional(compoundTag);
            compoundTag.putString("soundID", soundID);
            compoundTag.putFloat("range", range);
            compoundTag.putFloat("pitch", pitch.getFloat());
        }

        public static enum Pitch {
            VERY_SLOW(0.5F), SLOW(0.75F), DEFAULT(1.0F), FAST(1.5F), VERY_FAST(2.0F);
            final float fLoat;

            Pitch(float pitch) {
                this.fLoat = pitch;
            }

            public float getFloat() {
                return fLoat;
            }

            public static Pitch getValue(float f) {
              if (f == 0.5F) {
                return VERY_SLOW;
              } else if (f == 0.75F) {
                return SLOW;
              } else if (f == 1.0F) {
                return DEFAULT;
              } else if (f == 1.5F) {
                return FAST;
              } else if (f == 2.0F) {
                return VERY_FAST;
              } else {
                return DEFAULT;
              }
            }
        }
    }
}
