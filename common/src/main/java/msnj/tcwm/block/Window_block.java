package msnj.tcwm.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
#if MC_VERSION < "12004"
import net.minecraft.world.level.block.GlassBlock;
#else
import net.minecraft.world.level.block.TransparentBlock;
#endif

public class Window_block extends #if MC_VERSION < "12004" GlassBlock #else TransparentBlock#endif {
    public Window_block(BlockBehaviour.Properties settings) {
        super(settings);
    }
}
