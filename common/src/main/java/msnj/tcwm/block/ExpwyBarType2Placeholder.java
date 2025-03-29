package msnj.tcwm.block;

import msnj.tcwm.MCText;
import msnj.tcwm.item.Items;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExpwyBarType2Placeholder extends HorizontalDirectionalBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public ExpwyBarType2Placeholder(Direction defaultDirectional) {
        super(Blocks.copyProperties(Blocks.EXPWY_BAR_TYPE_2.get()));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, defaultDirectional));
    }

    public ExpwyBarType2Placeholder(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

        #if MC_VERSION >= "12004"
    public static final MapCodec<ExpwyBarType2Placeholder> CODEC = Block.simpleCodec(ExpwyBarType2Placeholder::new);
    #endif

        #if MC_VERSION >= "12004"
    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
    #endif

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (player.isHolding(Items.FORGE_TOOL.get())) {
            level.setBlock(blockPos, Blocks.EXPWY_BAR_TYPE_2.get().defaultBlockState().setValue(FACING, blockState.getValue(FACING)), 18);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        Vec3 offset = blockState.getOffset(blockGetter, blockPos);
        switch ((Direction) blockState.getValue(FACING)) {
            case SOUTH:
                return Block.box(0, 8, 0, 16, 16, 10).move(offset.x, offset.y, offset.z);
            case WEST:
                return Block.box(6, 8, 0, 16, 16, 16).move(offset.x, offset.y, offset.z);
            case EAST:
                return Block.box(0, 8, 0, 10, 16, 16).move(offset.x, offset.y, offset.z);
            case NORTH:
            default:
                return Block.box(0, 8, 9, 16, 16, 16).move(offset.x, offset.y, offset.z);
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context) {
        Vec3 offset = blockState.getOffset(blockGetter, blockPos);
        switch ((Direction) blockState.getValue(FACING)) {
            case SOUTH:
                return Block.box(0, 8, 0, 16, 24, 10).move(offset.x, offset.y, offset.z);
            case WEST:
                return Block.box(6, 8, 0, 16, 24, 16).move(offset.x, offset.y, offset.z);
            case EAST:
                return Block.box(0, 8, 0, 10, 24, 16).move(offset.x, offset.y, offset.z);
            case NORTH:
            default:
                return Block.box(0, 0, 9, 16, 24, 16).move(offset.x, offset.y, offset.z);
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable BlockGetter blockGetter, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, blockGetter, list, tooltipFlag);
        list.add(MCText.translatable("tooltip.expwy_bar_type_2_placeholder"));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
    #if MC_VERSION <= "11800"
    context.getLevel().getBlockTicks().scheduleTick(context.getClickedPos(), this, 20);
    #else
        context.getLevel().scheduleTick(context.getClickedPos(), this, 20);
      #endif
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
