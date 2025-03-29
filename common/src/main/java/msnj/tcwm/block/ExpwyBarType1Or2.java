package msnj.tcwm.block;

import msnj.tcwm.MCText;
import msnj.tcwm.util.EnumExpwyBarRotateType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static msnj.tcwm.util.EnumExpwyBarRotateType.*;
import static msnj.tcwm.util.EnumExpwyBarRotateType.DISABLE;

public class ExpwyBarType1Or2 extends HorizontalDirectionalBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public final String hoverText;
    public static final EnumProperty<BarEndType> END_TYPE = EnumProperty.create("end_type", BarEndType.class);
    public static final EnumProperty<EnumExpwyBarRotateType> ROTATE_TYPE = EnumProperty.create("rotate_type", EnumExpwyBarRotateType.class);
    private final int barType;

    public ExpwyBarType1Or2(Properties properties, String s, int barType) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH)
                .setValue(ROTATE_TYPE, DISABLE)
                .setValue(END_TYPE, BarEndType.NO_END)
        );
        this.hoverText = s;
        this.barType = barType;
    }

    public ExpwyBarType1Or2(Properties properties) {
        this(properties, "", 0);
    }

        #if MC_VERSION >= "12004"
    public static final MapCodec<ExpwyBarType1Or2> CODEC = Block.simpleCodec(ExpwyBarType1Or2::new);
    #endif

        #if MC_VERSION >= "12004"
    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
    #endif

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable BlockGetter blockGetter, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, blockGetter, list, tooltipFlag);
        list.add(MCText.translatable(hoverText));
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        /*return IBlock.checkHoldingBrush(level, player, () ->
        {
            if (!level.isClientSide) {
                PacketScreen.sendScreenBlockS2C((ServerPlayer) player, "EEBSS", blockPos);
            }
        });*/
        return InteractionResult.PASS;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        Vec3 offset = blockState.getOffset(blockGetter, blockPos);
        switch ((Direction) blockState.getValue(FACING)) {
            case SOUTH:
                if (blockState.getValue(ROTATE_TYPE) == R22_5) {
                    return Block.box(0, 0, 0, 16, 16, 16).move(offset.x, offset.y, offset.z);
                } else if (blockState.getValue(ROTATE_TYPE) == R45) {
                    return Block.box(0, 0, 0, 16, 16, 16).move(offset.x, offset.y, offset.z);
                } else {
                    return Block.box(0, 0, 0, 16, 16, 10).move(offset.x, offset.y, offset.z);
                }
            case WEST:
                if (blockState.getValue(ROTATE_TYPE) == R22_5) {
                    return Block.box(0, 0, 0, 16, 16, 16).move(offset.x, offset.y, offset.z);
                } else if (blockState.getValue(ROTATE_TYPE) == R45) {
                    return Block.box(0, 0, 0, 16, 16, 16).move(offset.x, offset.y, offset.z);
                } else {
                    return Block.box(6, 0, 0, 16, 16, 16).move(offset.x, offset.y, offset.z);
                }
            case EAST:
                if (blockState.getValue(ROTATE_TYPE) == R22_5) {
                    return Block.box(0, 0, 0, 16, 16, 16).move(offset.x, offset.y, offset.z);
                } else if (blockState.getValue(ROTATE_TYPE) == R45) {
                    return Block.box(0, 0, 0, 16, 16, 16).move(offset.x, offset.y, offset.z);
                } else {
                    return Block.box(0, 0, 0, 10, 16, 16).move(offset.x, offset.y, offset.z);
                }
            case NORTH:
            default:
                if (blockState.getValue(ROTATE_TYPE) == R22_5) {
                    return Block.box(0, 0, 0, 16, 16, 16).move(offset.x, offset.y, offset.z);
                } else if (blockState.getValue(ROTATE_TYPE) == R45) {
                    return Block.box(0, 0, 0, 16, 16, 16).move(offset.x, offset.y, offset.z);
                } else {
                    return Block.box(0, 0, 9, 16, 16, 16).move(offset.x, offset.y, offset.z);
                }
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context) {
        Vec3 offset = blockState.getOffset(blockGetter, blockPos);
        switch ((Direction) blockState.getValue(FACING)) {
            case SOUTH:
                if (blockState.getValue(ROTATE_TYPE) == R22_5) {
                    return Block.box(0, 0, 0, 16, 24, 16).move(offset.x, offset.y, offset.z);
                } else if (blockState.getValue(ROTATE_TYPE) == R45) {
                    return Block.box(0, 0, 0, 16, 24, 16).move(offset.x, offset.y, offset.z);
                } else {
                    return Block.box(0, 0, 0, 16, 24, 10).move(offset.x, offset.y, offset.z);
                }
            case WEST:
                if (blockState.getValue(ROTATE_TYPE) == R22_5) {
                    return Block.box(0, 0, 0, 16, 24, 16).move(offset.x, offset.y, offset.z);
                } else if (blockState.getValue(ROTATE_TYPE) == R45) {
                    return Block.box(0, 0, 0, 16, 24, 16).move(offset.x, offset.y, offset.z);
                } else {
                    return Block.box(6, 0, 0, 16, 24, 16).move(offset.x, offset.y, offset.z);
                }
            case EAST:
                if (blockState.getValue(ROTATE_TYPE) == R22_5) {
                    return Block.box(0, 0, 0, 16, 24, 16).move(offset.x, offset.y, offset.z);
                } else if (blockState.getValue(ROTATE_TYPE) == R45) {
                    return Block.box(0, 0, 0, 16, 24, 16).move(offset.x, offset.y, offset.z);
                } else {
                    return Block.box(0, 0, 0, 10, 24, 16).move(offset.x, offset.y, offset.z);
                }
            case NORTH:
            default:
                if (blockState.getValue(ROTATE_TYPE) == R22_5) {
                    return Block.box(0, 0, 0, 16, 24, 16).move(offset.x, offset.y, offset.z);
                } else if (blockState.getValue(ROTATE_TYPE) == R45) {
                    return Block.box(0, 0, 0, 16, 24, 16).move(offset.x, offset.y, offset.z);
                } else {
                    return Block.box(0, 0, 9, 16, 24, 16).move(offset.x, offset.y, offset.z);
                }
        }
    }

    public void EditBlockState(BlockState state, BlockPos pos, Level level) {
        level.setBlock(pos, state, 18);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ROTATE_TYPE, END_TYPE);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
    #if MC_VERSION <= "11800"
    context.getLevel().getBlockTicks().scheduleTick(context.getClickedPos(), this, 20);
    #else
        context.getLevel().scheduleTick(context.getClickedPos(), this, 20);
      #endif
        BarEndType test = BarEndType.NO_END;
        Direction facing = context.getHorizontalDirection().getOpposite();
//        BlockPos pos = context.getClickedPos();
//        Level level = context.getLevel();
//        if (facing != Direction.DOWN && facing != Direction.UP) {
//            var ccw = level.getBlockState(pos.relative(facing.getCounterClockWise()));
//            var cw = level.getBlockState(pos.relative(facing.getClockWise()));
//            if (ccw.getBlock() instanceof AirBlock &&
//                    !ccw.isFaceSturdy(level, pos.relative(facing.getCounterClockWise()), facing.getOpposite())) {
//                test = BarEndType.END_RIGHT;
//            } else if (ccw.getBlock() instanceof ExpwyBarType1Or2) {
//                if (barType == 1 && ((ExpwyBarType1Or2) ccw.getBlock()).barType == 2) {
//                    test = BarEndType.END_RIGHT;
//                }
//            }
//            if (cw.getBlock() instanceof AirBlock &&
//                    !cw.isFaceSturdy(level, pos.relative(facing.getClockWise()), facing.getOpposite())) {
//                if (test != BarEndType.NO_END) {
//                    test = BarEndType.NO_END;
//                } else {
//                    test = BarEndType.END_LEFT;
//                }
//            } else if (cw.getBlock() instanceof ExpwyBarType1Or2) {
//                if (barType == 1 && ((ExpwyBarType1Or2) cw.getBlock()).barType == 2) {
//                    test = BarEndType.END_LEFT;
//                }
//            }
//        }
        return this.defaultBlockState().setValue(FACING, facing).setValue(END_TYPE, test);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        BarEndType test = BarEndType.NO_END;
//        var ccw = level.getBlockState(pos.relative(facing.getCounterClockWise()));
//        var cw = level.getBlockState(pos.relative(facing.getClockWise()));
//        if (ccw.getBlock() instanceof AirBlock && !ccw.isFaceSturdy(level, pos.relative(facing.getCounterClockWise()), facing.getOpposite())) {
//            test = BarEndType.END_RIGHT;
//        } else if (ccw.getBlock() instanceof ExpwyBarType1Or2) {
//            if (barType == 1 && ((ExpwyBarType1Or2) ccw.getBlock()).barType == 2) {
//                test = BarEndType.END_RIGHT;
//            }
//        }
//        if (cw.getBlock() instanceof AirBlock && !cw.isFaceSturdy(level, pos.relative(facing.getClockWise()), facing.getOpposite())) {
//            if (test != BarEndType.NO_END) {
//                test = BarEndType.NO_END;
//            } else {
//                test = BarEndType.END_LEFT;
//            }
//        } else if (cw.getBlock() instanceof ExpwyBarType1Or2) {
//            if (barType == 1 && ((ExpwyBarType1Or2) cw.getBlock()).barType == 2) {
//                test = BarEndType.END_LEFT;
//            }
//        }
        return state.setValue(END_TYPE, test);
    }

    public static enum BarEndType implements StringRepresentable {
        NO_END("no_end"),
        END_LEFT("end_left"),
        END_RIGHT("end_right");

        private final String name;
        private BarEndType(String name) { this.name = name; }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }
    }
}
