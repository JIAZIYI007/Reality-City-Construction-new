package msnj.tcwm.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AirConditioner extends HorizontalDirectionalBlock {
    #if MC_VERSION >= "12004"
    public static final MapCodec<AirConditioner> CODEC = Block.simpleCodec(AirConditioner::new);
    #endif

    public static EnumProperty<EnumSide> SIDE = EnumProperty.create("side", EnumSide.class);

    public AirConditioner(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(SIDE, EnumSide.LEFT));
    }

    #if MC_VERSION >= "12004"
    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
    #endif

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SIDE);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Vec3 offset = state.getOffset(level, pos);
        switch (state.getValue(FACING)) {
            default:
            case NORTH:
                return box(0, 0, 6, 16, 16, 16);
            case EAST:
                return box(0, 0, 0, 10, 16, 16);
            case SOUTH:
                return box(0, 0, 0, 16, 16, 10);
            case WEST:
                return box(6, 0, 0, 16, 16, 16);
        }
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (direction == getNeighbourDirection(state.getValue(SIDE), state.getValue(FACING))) {
            return neighborState.is(this) && neighborState.getValue(SIDE) != state.getValue(SIDE) ? state : Blocks.AIR.defaultBlockState();
        } else {
            return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
        }
    }

    Direction getNeighbourDirection(EnumSide side, Direction direction) {
        return side == EnumSide.LEFT ? direction.getCounterClockWise() : direction.getClockWise();
    }

    public #if MC_VERSION >= "12004" BlockState #else void #endif  playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
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

        #if MC_VERSION >= "12004" return #endif super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide) {
            BlockPos blockPos = pos.relative((Direction)getNeighbourDirection(EnumSide.LEFT, state.getValue(FACING)));
            level.setBlock(blockPos, (BlockState)state.setValue(SIDE, EnumSide.RIGHT), 3);
            level.blockUpdated(pos, Blocks.AIR);
            state.updateNeighbourShapes(level, pos, 3);
        }
    }

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
                )
                .canBeReplaced(context) ?
                this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()) : null;
    }

    @Environment(EnvType.CLIENT)
    public static DoubleBlockCombiner.BlockType getBlockType(BlockState state) {
        return state.getValue(SIDE) == EnumSide.LEFT ? DoubleBlockCombiner.BlockType.FIRST : DoubleBlockCombiner.BlockType.SECOND;
    }

    public enum EnumSide implements StringRepresentable {
        LEFT("left"), RIGHT("right");

        private final String name;

        private EnumSide(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
