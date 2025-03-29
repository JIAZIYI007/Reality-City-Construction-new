package msnj.tcwm.block;

import msnj.tcwm.RealityCityConstruction;
import msnj.tcwm.item.Items;
import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;

public class ConveyerBelt extends HorizontalDirectionalBlock {//mtr.block.BlockEscalatorStep
      #if MC_VERSION >= "12004"
    public static final MapCodec<ConveyerBelt> CODEC = Block.simpleCodec(ConveyerBelt::new);
    #endif

  public ConveyerBelt(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
  }

  #if MC_VERSION >= "12004"
    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
    #endif

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
        #if MC_VERSION<="11800"
    context.getLevel().getBlockTicks().scheduleTick(context.getClickedPos(), this, 20);
#else
    context.getLevel().scheduleTick(context.getClickedPos(), this, 20);
#endif
    return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
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
  public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
    return Block.box(0, 0, 0, 16, 1.7, 16);
  }

  @Override
  public String toString(){
    return (
      "{"+
        "\n\tBlock:"+
        "\n\t\tTransmissonLine"+
        "}"
      );
  }

  @Override
  public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
    if (player.isHolding(Items.FORGE_TOOL.get())) {
      brushUse(blockState, blockPos, level);
      return InteractionResult.SUCCESS;
    }
    return InteractionResult.PASS;
  }

  private void brushUse(BlockState state, BlockPos blockPos, Level level){
    try {
      switch (state.getValue(FACING)) {
        case NORTH :
          level.setBlock(blockPos, state.setValue(FACING, Direction.SOUTH), 18);
          break;
        case SOUTH :
          level.setBlock(blockPos, state.setValue(FACING, Direction.NORTH), 18);
          break;
        case WEST :
          level.setBlock(blockPos, state.setValue(FACING, Direction.EAST), 18);
          break;
        case EAST :
          level.setBlock(blockPos, state.setValue(FACING, Direction.WEST), 18);
          break;
        default :
          level.setBlock(blockPos, state.setValue(FACING, Direction.SOUTH), 18);
          break;
      }
    } catch (Exception exception) {
      RealityCityConstruction.LOGGER.error("ERROR - ");
      System.out.print("ERROR!!! BlockState Edit Failed!");
      exception.printStackTrace();
    }
  }

  public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
    Direction facing = (Direction) IBlock.getStatePropertySafe(state, FACING);
    float speed = 0.1F;
    switch (facing) {
      case SOUTH:
        entity.push(0.0, 0.0, -0.114);
        break;
      case WEST:
        entity.push(0.114, 0.0, 0.0);
        break;
      case NORTH:
        entity.push(0.0, 0.0, 0.114);
        break;
      case EAST:
        entity.push(-0.114, 0.0, 0.0);
    }
  }
}
