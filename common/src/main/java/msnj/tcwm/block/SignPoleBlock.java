package msnj.tcwm.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SignPoleBlock extends Block implements SimpleWaterloggedBlock {
  //新建BLOCKSTATES
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
  public SignPoleBlock(Properties settings) {
    super(settings);
    //设置含水方块
    this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
  }

  //添加含水方块属性
  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(WATERLOGGED);
  }
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    boolean flag = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
    ;
    return this.defaultBlockState().setValue(WATERLOGGED, flag);
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  @Override
  public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos,
                                BlockPos facingPos) {
    if (state.getValue(WATERLOGGED)) {

    }
    return super.updateShape(state, facing, facingState, world, currentPos, facingPos);
  }

  //设置  Voxelshape
  public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
    Vec3 offset = state.getOffset(world, pos);
    return box(5,0,5,11,16,11).move(offset.x, offset.y, offset.z);
  }
}
