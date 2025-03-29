package msnj.tcwm.block;

import mtr.Blocks;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class MetalBlock extends Block {
  public MetalBlock(Properties properties) {
    super(properties);
  }

  public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random randomSource) {
    serverLevel.setBlock(blockPos, Blocks.METAL.get().defaultBlockState(), 2);
  }

  public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
    #if MC_VERSION <= "11800"
      levelAccessor.getBlockTicks().scheduleTick(blockPos, this, 20);
    #else
    levelAccessor.scheduleTick(blockPos, this, 20);
      #endif
    return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
  }

  @Nullable
  public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
    #if MC_VERSION<="11800"
      blockPlaceContext.getLevel().getBlockTicks().scheduleTick(blockPlaceContext.getClickedPos(), this, 20);
#else
    blockPlaceContext.getLevel().scheduleTick(blockPlaceContext.getClickedPos(), this, 20);
#endif
    return this.defaultBlockState();
  }
}
