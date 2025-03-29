package msnj.tcwm;

import com.mojang.datafixers.util.Function3;
import msnj.tcwm.util.*;
//import msnj.tcwm.mappings.BlockEntityMapper;
import msnj.tcwm.util.RegistryObject;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class TRegistry<K> {
  public static TRegistry<Object> INSTANCE = new TRegistry<>();
  public static List<Item> registryItems = new ArrayList<>();

  public boolean isRegisterSet2 = false;
  private FunctionD<String, RegistryObject<Item>> regItem;
  private FunctionD<String, RegistryObject<Block>> regBlock;
  private FunctionT<String, String, RegistryObject<Item>, Object> regModidItem;
  private FunctionT<String, String, RegistryObject<Block>, Object> regModidBlock;
  private FunctionR<String, String, RegistryObject<Block>, CreativeModeTab, Object> regModidBlockItem;
  private Function3<String, RegistryObject<Block>, CreativeModeTab, BlockItem> regblockitem;
  private FunctionD<String, RegistryObject<? extends BlockEntityType<? extends BlockEntity>>> regBlockEntityType;
  private FunctionD<String, SoundEvent> regSoundEvent;
  BiConsumer<String, RegistryObject<K>> regObject;
  public TRegistry(BiConsumer<String, RegistryObject<K>> register){
    this.regObject = register;
  }
  protected TRegistry() {}
  public void ForgeLoad(){
  }
  public final Logger LOGGER = new Logger(System.out);
  public void b(String id, RegistryObject<Block> block){
    regBlock.load(id, block);
  }

  public void i(String id, RegistryObject<Item> item){
    regItem.load(id, item);
    registryItems.add(item.get());
  }

  public void bi(String id, RegistryObject<Block> block, CreativeModeTab tab){
    registryItems.add(regblockitem.apply(id, block, tab));
  }
  public void bi(String id, RegistryObject<Block> block){
    regBlock.load(id, block);
    regItem.load(id, new RegistryObject<>(() -> new BlockItem(block.get(), new Item.Properties())));
  }

  public void bgs(String modid, String id, RegistryObject<Block> block, CreativeModeTab tab) {
    regModidBlockItem.load(modid, id, block, tab, "");
  }

  public void be(String id, RegistryObject<? extends BlockEntityType<? extends BlockEntity>> blockEntity){
    regBlockEntityType.load(id, blockEntity);
  }

  public void s(String id, SoundEvent se){
    if(!isRegisterSet2){
      throw new RuntimeException();
    } else {
      regSoundEvent.load(id, se);
    }
  }

  public void o(String id, RegistryObject<K> object){
    this.regObject.accept(id, object);
  }

  public void set(
    FunctionD<String, RegistryObject<Item>> registerItem,
    FunctionD<String, RegistryObject<Block>> registerBlock,
    FunctionT<String, String, RegistryObject<Item>, Object> registerModidItem,
    FunctionT<String, String, RegistryObject<Block>, Object> registerModidBlock,
    FunctionR<String, String, RegistryObject<Block>, CreativeModeTab, Object> registerModidBlockItem,
    Function3<String, RegistryObject<Block>, CreativeModeTab, BlockItem> registerBlockItem,
    FunctionD<String, RegistryObject<? extends BlockEntityType<? extends BlockEntity>>> registerBlockEntity
  ){
    regItem = registerItem;
    regBlock = registerBlock;
    regblockitem = registerBlockItem;
    regModidBlock = registerModidBlock;
    regModidItem = registerModidItem;
    regModidBlockItem = registerModidBlockItem;
    regBlockEntityType = registerBlockEntity;
    isRegisterSet2 = false;
  }

  public void set(
    FunctionD<String, RegistryObject<Item>> registerItem,
    FunctionD<String, RegistryObject<Block>> registerBlock,
    FunctionT<String, String, RegistryObject<Item>, Object> registerModidItem,
    FunctionT<String, String, RegistryObject<Block>, Object> registerModidBlock,
    FunctionR<String, String, RegistryObject<Block>, CreativeModeTab, Object> registerModidBlockItem,
    Function3<String, RegistryObject<Block>, CreativeModeTab, BlockItem> registerBlockItem,
    FunctionD<String, RegistryObject<? extends BlockEntityType<? extends BlockEntity>>> registerBlockEntity,
    FunctionD<String, SoundEvent> registerSoundEvent
    ){
    regItem = registerItem;
    regBlock = registerBlock;
    regblockitem = registerBlockItem;
    regModidBlock = registerModidBlock;
    regModidItem = registerModidItem;
    regModidBlockItem = registerModidBlockItem;
    regBlockEntityType = registerBlockEntity;
    regSoundEvent = registerSoundEvent;
    isRegisterSet2 = true;
  }
}
