package msnj.tcwm.item;

import msnj.tcwm.TRegistry;
import msnj.tcwm.block.Blocks;
import msnj.tcwm.util.CreativeModeTab;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class TcwmCreativeModeTab {
  public static final CreativeModeTab ITEMS = new CreativeModeTab(new ResourceLocation("tcwm","core"), () -> new ItemStack(() -> Blocks.LOGO_BLOCK.get().asItem())#if MC_VERSION >= "12000", (net.minecraft.world.item.CreativeModeTab.DisplayItemsGenerator) (c, e) -> {
    for (Item i : TRegistry.registryItems) {
      e.accept(i);
    }
  }#endif);
  //public static final CreativeModeTabs.Wrapper RAILS = new CreativeModeTabs.Wrapper(new ResourceLocation("tcwm","rails"), () -> new ItemStack(() -> Items.RAIL_CONNECTOR_HOMO.get()));
}
