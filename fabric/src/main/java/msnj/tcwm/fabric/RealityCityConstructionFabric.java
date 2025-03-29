package msnj.tcwm.fabric;

import msnj.tcwm.Info;
import msnj.tcwm.MCText;
import msnj.tcwm.TRegistry;
import msnj.tcwm.util.Logger;
import msnj.tcwm.util.RegistryObject;
#if MC_VERSION < "11900"
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
#elif MC_VERSION >= "11900"
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
#endif
#if MC_VERSION < "11903"
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
#else
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
#endif
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.core.Registry;
#if MC_VERSION >= "11903"
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
#else
import net.minecraft.core.Registry;
#endif
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import msnj.tcwm.RealityCityConstruction;
import net.fabricmc.api.ModInitializer;

import java.util.function.Supplier;

public class RealityCityConstructionFabric implements ModInitializer {
  public static final Logger LOGGER = new Logger(System.out);
  @Override
  public void onInitialize() {
    LOGGER.info("RCC mod Fabric Private System is loading...");
    Info.isMTRInstalled = isMTRInstalled();
    msnj.tcwm.util.CreativeModeTab.setCreateTabFunction(RealityCityConstructionFabric::createTab);
    TRegistry.INSTANCE.set(RealityCityConstructionFabric::registerItem, RealityCityConstructionFabric::registerBlock, RealityCityConstructionFabric::registerModidItem, RealityCityConstructionFabric::registerModidBlock, RealityCityConstructionFabric::registerModidBlockItem, RealityCityConstructionFabric::registerBlock, RealityCityConstructionFabric::registerBlockEntityType, RealityCityConstructionFabric::registerSoundEvent);
    RealityCityConstruction.init((r, i) -> ServerPlayNetworking.registerGlobalReceiver(r, (server, player, handler, packet, responseSender) -> i.load(server, player, packet)));
  }

  boolean isMTRInstalled() {
    for (ModContainer m : FabricLoader.getInstance().getAllMods()) {
      if (m.getMetadata().getId().equals("mtr") && m.getMetadata().getVersion().toString().startsWith("3")) {
        return true;
      }
    }
    return false;
  }

  private static Supplier<CreativeModeTab> createTab(ResourceLocation r, Supplier<ItemStack> s#if MC_VERSION >= "12001", CreativeModeTab.DisplayItemsGenerator d#endif) {
    return #if MC_VERSION < "11903"
            () -> FabricItemGroupBuilder.create(r).icon(s).build();
          #elif MC_VERSION < "12001"
            () -> FabricItemGroup.builder(r).icon(s).build();
          #else
            () -> FabricItemGroup.builder().icon(s).displayItems(d).title(MCText.translatable(String.format("itemGroup.%s.%s", r.getNamespace(), r.getPath()))).build();
          #endif
  }

  private static void registerItem(String path, RegistryObject<Item> item) {
    final Item itemObject = item.get();
    net.minecraft.core.Registry.register(#if MC_VERSION >= "11903" BuiltInRegistries #else Registry #endif.ITEM, new ResourceLocation(RealityCityConstruction.MOD_ID, path), itemObject);
  }

  private static void registerBlock(String path, RegistryObject<Block> block) {
    net.minecraft.core.Registry.register(#if MC_VERSION >= "11903" BuiltInRegistries #else Registry #endif.BLOCK, new ResourceLocation(RealityCityConstruction.MOD_ID, path), block.get());
  }

  private static BlockItem registerBlock(String path, RegistryObject<Block> block, msnj.tcwm.util.CreativeModeTab creativeModeTab) {
    registerBlock(path, block);
    #if MC_VERSION >= "12000"
    final BlockItem blockItem = new BlockItem(block.get(), new Item.Properties());
    #elif MC_VERSION >= "11903"
    final BlockItem blockItem = new BlockItem(block.get(), new Item.Properties());
    ItemGroupEvents.modifyEntriesEvent(creativeModeTab.get()).register(entries -> entries.accept(blockItem));
    #else
    final BlockItem blockItem = new BlockItem(block.get(), new Item.Properties().tab(creativeModeTab.get()));
    #endif
    net.minecraft.core.Registry.register(#if MC_VERSION >= "11903" BuiltInRegistries #else Registry #endif.ITEM, new ResourceLocation(RealityCityConstruction.MOD_ID, path), blockItem);
    return blockItem;
  }

  private static void registerModidBlock(String modid, String path, RegistryObject<Block> block, Object text) {
    net.minecraft.core.Registry.register(#if MC_VERSION >= "11903" BuiltInRegistries #else Registry #endif.BLOCK, new ResourceLocation(modid, path), block.get());
  }

  @Deprecated
  private static void registerModidItem(String modid, String path, RegistryObject<Item> item, Object text) {
    net.minecraft.core.Registry.register(#if MC_VERSION >= "11903" BuiltInRegistries #else Registry #endif.ITEM, new ResourceLocation(modid, path), item.get());
  }

  private static void registerModidBlockItem(String modid, String path, RegistryObject<Block> block, msnj.tcwm.util.CreativeModeTab tab, Object text) {
    BlockItem blockItem;
    #if MC_VERSION >= "12000"
    blockItem = new BlockItem(block.get(), new Item.Properties());
    #elif MC_VERSION >= "11903"
    blockItem = new BlockItem(block.get(), new Item.Properties());
    ItemGroupEvents.modifyEntriesEvent(tab.get()).register(entries -> entries.accept(blockItem));
    #else
    blockItem = new BlockItem(block.get(), new Item.Properties().tab(tab.get()));
    #endif
    net.minecraft.core.Registry.register(#if MC_VERSION >= "11903" BuiltInRegistries #else Registry #endif.ITEM, new ResourceLocation(modid, path), blockItem);
  }

  @Deprecated
  private static SoundEvent getSoundEvent(String id, String path) {
    return net.minecraft.core.Registry.register(#if MC_VERSION >= "11903" BuiltInRegistries #else Registry #endif.SOUND_EVENT, new ResourceLocation(id, path), #if MC_VERSION >= "11903" SoundEvent.createVariableRangeEvent #else new SoundEvent #endif(new ResourceLocation(id, path)));
  }

  private static void registerSoundEvent(String id, SoundEvent event){
    net.minecraft.core.Registry.register(#if MC_VERSION >= "11903" BuiltInRegistries #else Registry #endif.SOUND_EVENT, new ResourceLocation(id), event);
  }


  private static void registerBlockEntityType(String path, RegistryObject<? extends BlockEntityType<? extends BlockEntity>> blockEntityType) {
    net.minecraft.core.Registry.register(#if MC_VERSION >= "11903" BuiltInRegistries #else Registry #endif.BLOCK_ENTITY_TYPE, new ResourceLocation(RealityCityConstruction.MOD_ID, path), blockEntityType.get());
  }
}
