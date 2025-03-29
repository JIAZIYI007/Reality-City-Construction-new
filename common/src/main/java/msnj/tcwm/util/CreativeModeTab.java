package msnj.tcwm.util;

//import msnj.tcwm.mappings.Utilities;
import com.mojang.datafixers.util.Function3;
import msnj.tcwm.TRegistry;
import net.minecraft.core.Registry;
#if MC_VERSION >= "11903"
import net.minecraft.core.registries.BuiltInRegistries;
#else
import net.minecraft.data.BuiltinRegistries;
#endif
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
#if MC_VERSION >= "11903" import net.minecraft.world.item.CreativeModeTabs; #endif
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
#if MC_VERSION >= "12000" import static net.minecraft.world.item.CreativeModeTab.DisplayItemsGenerator; #endif

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class CreativeModeTab {
    public final ResourceLocation resourceLocation;
    private final Supplier<ItemStack> itemSupplier;
    #if MC_VERSION >= "12000" private DisplayItemsGenerator generator = null; #endif
    private Supplier<net.minecraft.world.item.CreativeModeTab> creativeModeTabSupplier;
    private net.minecraft.world.item.CreativeModeTab creativeModeTab;
    private static #if MC_VERSION >= "12000" Function3#else BiFunction#endif<ResourceLocation, Supplier<ItemStack>#if MC_VERSION >= "12000", DisplayItemsGenerator#endif, Supplier<net.minecraft.world.item.CreativeModeTab>> createTabFunction = null;
    private static BiFunction<ResourceLocation, net.minecraft.world.item.CreativeModeTab, ResourceKey<net.minecraft.world.item.CreativeModeTab>> fabricTabRegistryFunction = (i, j) -> { throw new RuntimeException(); };

    public CreativeModeTab(ResourceLocation resourceLocation, Supplier<ItemStack> itemSupplier#if MC_VERSION >= "12000", DisplayItemsGenerator generator#endif) {
        this.itemSupplier = itemSupplier;
        #if MC_VERSION >= "12000"this.generator = generator#endif;
        this.resourceLocation = resourceLocation;
        this.creativeModeTabSupplier = null;
    }

    public void register() {
        this.creativeModeTabSupplier = createTabFunction.apply(resourceLocation, itemSupplier#if MC_VERSION >= "12000", generator#endif);
        #if MC_VERSION >= "12000" Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, resourceLocation, creativeModeTabSupplier.get()); #endif
    }

    public net.minecraft.world.item.CreativeModeTab get() {
        if (this.creativeModeTab == null) {
            this.creativeModeTab = this.creativeModeTabSupplier.get();
        }

        return this.creativeModeTab;
    }

    public static void setCreateTabFunction(#if MC_VERSION >= "12000" Function3#else BiFunction#endif<ResourceLocation, Supplier<ItemStack>#if MC_VERSION >= "12000", DisplayItemsGenerator#endif, Supplier<net.minecraft.world.item.CreativeModeTab>> fun) {
        createTabFunction = fun;
    }

//    public static void setFabricTabRegistryFunction(BiFunction<ResourceLocation, net.minecraft.world.item.CreativeModeTab, ResourceKey<net.minecraft.world.item.CreativeModeTab>> fun) {
//        fabricTabRegistryFunction = fun;
//    }
//
//    public static ResourceKey<net.minecraft.world.item.CreativeModeTab> fabricTabRegistry(ResourceLocation v1, net.minecraft.world.item.CreativeModeTab v2) {
//        return fabricTabRegistryFunction.apply(v1, v2);
//    }
}
