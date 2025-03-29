package msnj.tcwm.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class PlatformBlock extends HorizontalDirectionalBlock {
  #if MC_VERSION >= "12004"
    public static final MapCodec<PlatformBlock> CODEC = Block.simpleCodec(PlatformBlock::new);
    #endif

        #if MC_VERSION >= "12004"
    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
    #endif

  private final boolean isIndented;
  public static final EnumProperty<PlatformBlock.EnumDoorType> DOOR_TYPE = EnumProperty.create("door_type", PlatformBlock.EnumDoorType.class);
  public static final IntegerProperty SIDE = IntegerProperty.create("side", 0, 4);

  public PlatformBlock(BlockBehaviour.Properties settings, boolean isIndented) {
    super(settings);
    this.isIndented = isIndented;
    this.registerDefaultState((BlockState)this.defaultBlockState().setValue(DOOR_TYPE, PlatformBlock.EnumDoorType.NONE));
  }

  public PlatformBlock(BlockBehaviour.Properties settings) {
    this(settings, false);
  }

  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    return (BlockState)this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
  }

  public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
    return super.getShape(state, world, pos, context);
  }

  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(new Property[]{FACING, DOOR_TYPE, SIDE});
  }

  private static enum EnumDoorType implements StringRepresentable {
    NONE("none"),
    PSD("psd"),
    APG("apg");

    private final String name;

    private EnumDoorType(String nameIn) {
      this.name = nameIn;
    }

    public String getSerializedName() {
      return this.name;
    }
  }
  //继承站台方块类
}
