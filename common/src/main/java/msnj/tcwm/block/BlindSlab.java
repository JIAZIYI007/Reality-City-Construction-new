package msnj.tcwm.block;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
#if MC_VERSION < "12000"
import net.minecraft.world.level.storage.loot.LootContext;
#else
import net.minecraft.world.level.storage.loot.LootParams;
#endif
import org.jetbrains.annotations.Nullable;
import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BlindSlab extends SlabBlock {
    public static final EnumProperty<SlabType> TYPE = SlabBlock.TYPE;
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public BlindSlab(Properties settings) {
        super(settings);
      this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false)
                .setValue(TYPE, SlabType.BOTTOM).setValue(FACING, Direction.NORTH));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TYPE, WATERLOGGED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(TYPE, Objects.requireNonNull(super.getStateForPlacement(context)).getValue(TYPE));
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    public List<ItemStack> getDrops(BlockState state, #if MC_VERSION < "12000" LootContext #else LootParams #endif.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this, state.getValue(TYPE) == SlabType.DOUBLE ? 2 : 1));
    }
}
