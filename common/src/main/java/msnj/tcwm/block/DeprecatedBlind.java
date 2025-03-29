package msnj.tcwm.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
#if MC_VERSION < "12000"
import net.minecraft.world.level.storage.loot.LootContext;
#else
import net.minecraft.world.level.storage.loot.LootParams;
#endif
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.*;
import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.html.BlockView;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DeprecatedBlind extends HorizontalDirectionalBlock {

  public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public DeprecatedBlind(Properties settings) {
        super(settings);
      this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    #if MC_VERSION >= "12004"
    public static final MapCodec<DeprecatedBlind> CODEC = Block.simpleCodec(DeprecatedBlind::new);
    #endif

        #if MC_VERSION >= "12004"
    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
    #endif

    public VoxelShape getOutlineShape(BlockState state, BlockGetter world, BlockView view, BlockPos pos, CollisionContext ctx) {
      Vec3 offset = state.getOffset(world, pos);
      switch ((Direction) state.getValue(FACING)) {
        case SOUTH :
          return box(0, 0, 0, 16, 17, 16).move(offset.x, offset.y, offset.z);
        case NORTH :
          return box(0, 0, 0, 16, 17, 16).move(offset.x, offset.y, offset.z);
        case EAST :
          return box(0, 0, 0, 16, 17, 16).move(offset.x, offset.y, offset.z);
        case WEST :
          return box(0, 0, 0, 16, 17, 16).move(offset.x, offset.y, offset.z);
        default :
          return box(0, 0, 0, 16, 17, 16).move(offset.x, offset.y, offset.z);
      }
    }
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING);
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
    return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
  }

  @Override
  public List<ItemStack> getDrops(BlockState state, #if MC_VERSION < "12000" LootContext #else LootParams #endif.Builder builder) {
    List<ItemStack> dropsOriginal = super.getDrops(state, builder);
    if (!dropsOriginal.isEmpty())
      return dropsOriginal;
    return Collections.singletonList(new ItemStack(net.minecraft.world.level.block.Blocks.STONE));
  }

  public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random randomSource) {
    serverLevel.setBlock(blockPos, Blocks.YELLOW_BLIND.get().defaultBlockState().setValue(FACING, blockState.getValue(FACING)), 2);
  }

  public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
      #if MC_VERSION <= "11800"
    levelAccessor.getBlockTicks().scheduleTick(blockPos, this, 20);
      #else
    levelAccessor.scheduleTick(blockPos, this, 20);
        #endif
    return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
  }
}
