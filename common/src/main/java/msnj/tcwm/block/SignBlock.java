package msnj.tcwm.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class SignBlock extends HorizontalDirectionalBlock {
    public static final EnumProperty<EnumSide> SIDE = EnumProperty.create("side", EnumSide.class);

    protected SignBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(SIDE, EnumSide.LEFT));
    }

            #if MC_VERSION >= "12004"
    public static final MapCodec<SignBlock> CODEC = Block.simpleCodec(SignBlock::new);
    #endif

        #if MC_VERSION >= "12004"
    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
    #endif

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SIDE);
    }

 /*   public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (direction == getNeighbourDirection(state.getValue(SIDE), state.getValue(FACING))) {
            return neighborState.is(this) && neighborState.getValue(SIDE) != state.getValue(SIDE) ? state : Blocks.AIR.defaultBlockState();
        } else {
            return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
        }
    }

    public #if MC_VERSION >= "12004" BlockState #else void #endif playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative()) {
            if (state.getValue(SIDE) == EnumSide.LEFT) {
                BlockPos rightPos = pos.relative(getNeighbourDirection(state.getValue(SIDE), state.getValue(FACING)));
                BlockState blockState = level.getBlockState(rightPos);
                if (blockState.getBlock() == state.getBlock() && blockState.getValue(SIDE) == EnumSide.RIGHT) {
                    level.setBlock(rightPos, Blocks.AIR.defaultBlockState(), 35);
                    level.levelEvent(player, 2001, rightPos, Block.getId(blockState));
                }
            }
        }

        super.playerWillDestroy(level, pos, state, player);
        #if MC_VERSION >= "12004" return state; #endif
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide) {
            BlockPos blockPos = pos.relative(getNeighbourDirection(EnumSide.LEFT, state.getValue(FACING)));
            level.setBlock(blockPos, state.setValue(SIDE, EnumSide.RIGHT), 3);
            level.blockUpdated(pos, Blocks.AIR);
            state.updateNeighbourShapes(level, pos, 3);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
            #if MC_VERSION <= "11800"
                context.getLevel().getBlockTicks().scheduleTick(context.getClickedPos(), this, 20);
            #else
                context.getLevel().scheduleTick(context.getClickedPos(), this, 20);
            #endif
        return context
                .getLevel()
                .getBlockState(
                        context.getClickedPos()
                                .relative(
                                        getNeighbourDirection(
                                                EnumSide.LEFT,
                                                context.getHorizontalDirection().getOpposite()
                                        )
                                )
                ).canBeReplaced(context) ?
                this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()) : null;
    }*/

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
            #if MC_VERSION <= "11800"
                context.getLevel().getBlockTicks().scheduleTick(context.getClickedPos(), this, 20);
            #else
        context.getLevel().scheduleTick(context.getClickedPos(), this, 20);
            #endif
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    Direction getNeighbourDirection(EnumSide side, Direction direction) {
        return side == EnumSide.LEFT ? direction.getCounterClockWise() : direction.getClockWise();
    }

    public enum EnumSide implements StringRepresentable {
        LEFT("left"), RIGHT("right");

        private final String l;

        private EnumSide(String literal) {
            l = literal;
        }

        @Override
        public String getSerializedName() {
            return l;
        }
    }
}
