package msnj.tcwm.block;

import msnj.tcwm.MCText;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;

public class ExpwyCautionBar extends HorizontalDirectionalBlock {
    protected ExpwyCautionBar(Properties properties) {
        super(properties);
    }

            #if MC_VERSION >= "12004"
    public static final MapCodec<ExpwyCautionBar> CODEC = Block.simpleCodec(ExpwyCautionBar::new);
    #endif

        #if MC_VERSION >= "12004"
    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
    #endif

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    @SuppressWarnings("all")
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Vec3 offset = state.getOffset(level, pos);
        return Block.box(6, 0, 6, 10, 19, 10).move(offset.x, offset.y, offset.z);
    }

    @Override
    @SuppressWarnings("all")
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Vec3 offset = state.getOffset(level, pos);
        return Block.box(6, 0, 6, 10, 24, 10).move(offset.x, offset.y, offset.z);
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
