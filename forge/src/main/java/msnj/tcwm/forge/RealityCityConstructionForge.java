package msnj.tcwm.forge;

#if MC_VERSION <= "11700"
import me.shedaniel.architectury.platform.forge.EventBuses;
import me.shedaniel.architectury.networking.NetworkManager;
import me.shedaniel.architectury.registry.RenderTypes;
import me.shedaniel.architectury.registry.client.rendering.RenderTypeRegistry;
import me.shedaniel.architectury.registry.registries.DeferredRegister;
#else
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.platform.forge.EventBuses;
import dev.architectury.networking.NetworkManager;
#endif
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import msnj.tcwm.Info;
import msnj.tcwm.MCText;
import msnj.tcwm.RealityCityConstruction;
import msnj.tcwm.block.Blocks;
import msnj.tcwm.block.StationBroadcaster;
import msnj.tcwm.util.*;
import msnj.tcwm.util.settings.JsonIO;
import msnj.tcwm.forge.mappings.ForgeUtilities;
import msnj.tcwm.TRegistry;
import msnj.tcwm.network.PacketUpdateBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.language.IModInfo;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

@Mod(RealityCityConstruction.MOD_ID)
public class RealityCityConstructionForge {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(RealityCityConstruction.MOD_ID, Registries.ITEM);
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(RealityCityConstruction.MOD_ID, Registries.BLOCK);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(RealityCityConstruction.MOD_ID, Registries.BLOCK_ENTITY_TYPE);
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(RealityCityConstruction.MOD_ID, Registries.ENTITY_TYPE);
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(RealityCityConstruction.MOD_ID, Registries.SOUND_EVENT);

    //D:\Dex\MTR-common.jar
    public static final Logger LOGGER = new Logger(System.out);

    public RealityCityConstructionForge() {
        LOGGER.info("RCC mod Forge Private System is loading...");
        Info.isMTRInstalled = isMTRInstalled();
        msnj.tcwm.util.CreativeModeTab.setCreateTabFunction(RealityCityConstructionForge::createTab);
        final IEventBus eventbus = FMLJavaModLoadingContext.get().getModEventBus();
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(RealityCityConstruction.MOD_ID, eventbus);
        ITEMS.register();
        BLOCKS.register();
        BLOCK_ENTITY_TYPES.register();
        ENTITY_TYPES.register();
        SOUND_EVENTS.register();
        TRegistry.INSTANCE.set(RealityCityConstructionForge::registerItem, RealityCityConstructionForge::registerBlock, RealityCityConstructionForge::registerModidItem, RealityCityConstructionForge::registerModidBlock, RealityCityConstructionForge::registerModidBlockItem, RealityCityConstructionForge::registerBlock, RealityCityConstructionForge::registerBlockEntityType, RealityCityConstructionForge::registerSoundEvent);
        RealityCityConstruction.init(Network::registerReceiverC2S);
        eventbus.register(RCCModEventBus.class);
        MinecraftForge.EVENT_BUS.register(RCCModEventBus.ForgeEventBusListener.class);
    }

    static boolean isMTRInstalled() {
        for (
            #if MC_VERSION <= "11700"
            ModInfo
             #else IModInfo
                        #endif info : ModList.get().getMods()) {
            if (Objects.equals(info.getModId(), "mtr")) {
                return true;
            }
        }
        return false;
    }

    static Supplier<CreativeModeTab> createTab(ResourceLocation r, Supplier<ItemStack> s#if MC_VERSION >= "12000", DisplayItemsGenerator generator#endif) {
        return ForgeUtilities.createCreativeModeTab(r, s, String.format("itemGroup.%s.%s", r.getNamespace(), r.getPath()));
    }

    private static void registerItem(String path, RegistryObject<Item> item) {
        ITEMS.register(path, () ->
        {
            final Item itemObject = item.get();
            if (itemObject instanceof Item) {
                ForgeUtilities.registerCreativeModeTab(new ResourceLocation("tcwm", "core"), itemObject);
            }
            return itemObject;
        });
    }

    private static void registerBlock(String path, RegistryObject<Block> block) {
        BLOCKS.register(path, block::get);
    }

    private static BlockItem registerBlock(String path, RegistryObject<Block> block, msnj.tcwm.util.CreativeModeTab creativeModeTabWrapper) {
        registerBlock(path, block);
        AtomicReference<BlockItem> item = new AtomicReference<>(null);
        ITEMS.register(path, () ->
        {
            final BlockItem blockItem = new BlockItem(block.get(), new Item.Properties(#if MC_VERSION < "11930" creativeModeTabWrapper::get #endif));
            ForgeUtilities.registerCreativeModeTab(creativeModeTabWrapper.resourceLocation, blockItem);
            item.set(blockItem);
            return blockItem;
        });
        return item.get();
    }

    private static void registerModidBlock(String modid, String path, RegistryObject<Block> block, Object text) {
        DeferredRegister<Block> ModidBlock = DeferredRegister.create(modid, ForgeUtilities.registryGetBlock());
        ModidBlock.register(path, block::get);
    }

    @Deprecated
    private static void registerModidItem(String modid, String path, Supplier<? extends Item> item, Object text) {
        DeferredRegister<Item> ModidItem = DeferredRegister.create(modid, ForgeUtilities.registryGetItem());
        ModidItem.register(path, item);
    }

    @Deprecated
    private static void registerModidItem(String modid, String path, RegistryObject<Item> item, Object text) {
        DeferredRegister<Item> ModidItem = DeferredRegister.create(modid, ForgeUtilities.registryGetItem());
        ModidItem.register(path, item::get);
    }

    private static void registerSoundEvent(String id, SoundEvent event) {
        SOUND_EVENTS.register(id, () -> event);
    }

    private static void registerModidBlockItem(String modid, String path, RegistryObject<Block> block, msnj.tcwm.util.CreativeModeTab tab, Object text) {
        registerModidBlock(modid, path, block, "");
        registerModidItem(modid, path, () ->
        {
            final BlockItem blockItem = new BlockItem(block.get(), new Item.Properties());
            ForgeUtilities.registerCreativeModeTab(tab.resourceLocation, blockItem);
            return blockItem;
        }, "");
    }

    private static void registerBlockEntityType(String path, RegistryObject<? extends BlockEntityType<? extends BlockEntity>> blockEntityType) {
        BLOCK_ENTITY_TYPES.register(path, blockEntityType::get);
    }

    private static class RCCModEventBus {
        @SubscribeEvent
        public static void onClientSetupEvent(FMLClientSetupEvent event) {
            JsonIO.setScreenCreator(RCCModEventBus::createConfigScreen);
            StationBroadcaster.propertiesScreen = RCCModEventBus::createHomoBroadcasterScreen;
            if (Info.isMTRInstalled) {
                ForgeUtilities.registerTextureStitchEvent(textureAtlas ->
                {
                    if (((TextureAtlas) textureAtlas).location().getPath().equals("textures/atlas/blocks.png")) {
                        TcwmResources.reload(Minecraft.getInstance().getResourceManager());
                    }
                });
            }
            msnj.tcwm.RealityCityConstruction.getClientInit().init(RenderTypeRegistry::register, (r, i) -> NetworkManager.registerReceiver(NetworkManager.Side.S2C, r, (p, c) -> i.accept(p)));
        }

        static Screen createConfigScreen(Screen parent) {
            JsonIO io = JsonIO.read(Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve("tcwm.json"));
            ConfigBuilder bd = (ConfigBuilder.create().setParentScreen(parent).setTitle(MCText.translatable("gui.tcwm.Settings.title")).transparentBackground());

            ConfigEntryBuilder eb = bd.entryBuilder();

            //第一分类
            {
                ConfigCategory general = bd.getOrCreateCategory(MCText.translatable("gui.tcwm.Settings.category.general"));

                general.addEntry(eb.startBooleanToggle(MCText.translatable("gui.tcwm.Settings.properties1.content"), io.customRailwaySignIsOpen).setDefaultValue(true).setSaveConsumer((value) ->
                {
                    if (value != io.customRailwaySignIsOpen) {
                        io.customRailwaySignIsOpen = value;
                        JsonIO.write(io);
                        Minecraft.getInstance().reloadResourcePacks();
                    }
                }).build());

                general.addEntry(eb.startBooleanToggle(MCText.translatable("gui.tcwm.Settings.properties2.content"), io.disabledMixins).setDefaultValue(false).setSaveConsumer((value) ->
                {
                    if (value != io.disabledMixins) {
                        io.disabledMixins = value;
                        JsonIO.write(io);
                    }
                }).build());
            }

            return bd.build();
        }

        static Screen createHomoBroadcasterScreen(BlockPos pos) {
            Level level = Minecraft.getInstance().level;
            if (level == null) {
                throw new NullPointerException();
            }
            BlockEntity temp = level.getBlockEntity(pos);
            StationBroadcaster.StationBroadcasterEntity entity;
            if (temp instanceof StationBroadcaster.StationBroadcasterEntity &&
                    temp.getType() == Blocks.BlockEntityTypes.HOMO_STATION_BROADCASTER.get()) {
                entity = (StationBroadcaster.StationBroadcasterEntity) temp;
            } else {
                return new ChatScreen("Failed create screen.");
            }

            ConfigBuilder bd = (ConfigBuilder.create().setParentScreen(null).setTitle(MCText.translatable("gui.tcwm.SSBAS.title")).transparentBackground());

            ConfigEntryBuilder eb = bd.entryBuilder();

            ConfigCategory main = bd.getOrCreateCategory(MCText.text("main"));

            main.addEntry(eb.startTextField(MCText.translatable("gui.tcwm.SSBAS.control1title"), entity.getSoundID()).setDefaultValue("tcwm:music.example").setErrorSupplier((str) ->
            {
                if (!str.matches("[a-z]+[a-z_\\-.]")) {
                    return Optional.of(MCText.text("example: tcwm:music.example"));
                }
                return Optional.empty();
            }).setSaveConsumer((str) ->
            {
                if (entity.getSoundID().equals(str)) {
                    return;
                }
                if (!str.isEmpty()) {
                    entity.setSoundID(str);
                    PacketUpdateBlockEntity.sendUpdateC2S(entity, pos);
                }
            }).build());

            main.addEntry(eb.startFloatField(MCText.translatable("gui.tcwm.SSBAS.control2title"), entity.getRange()).setDefaultValue(114.514f).setSaveConsumer((f) ->
            {
                if (entity.getRange() == f) {
                    return;
                }
                try {
                    entity.setRange(f);
                    PacketUpdateBlockEntity.sendUpdateC2S(entity, pos);
                } catch (Exception exception) {
                    RealityCityConstruction.LOGGER.error("Set Float Value Error!");
                    exception.printStackTrace();
                }
            }).build());

            main.addEntry(eb.startEnumSelector(MCText.translatable("gui.tcwm.SSBAS.control3title"), StationBroadcaster.StationBroadcasterEntity.Pitch.class, entity.getPitch()).setDefaultValue(StationBroadcaster.StationBroadcasterEntity.Pitch.DEFAULT).setEnumNameProvider((e) ->
            {
                switch ((StationBroadcaster.StationBroadcasterEntity.Pitch) e) {
                    case DEFAULT:
                        return MCText.translatable("gui.tcwm.AABAS.pitch_default_button");
                    case VERY_SLOW:
                        return MCText.translatable("gui.tcwm.AABAS.pitch_very_slow_button");
                    case SLOW:
                        return MCText.translatable("gui.tcwm.AABAS.pitch_slow_button");
                    case FAST:
                        return MCText.translatable("gui.tcwm.AABAS.pitch_fast_button");
                    case VERY_FAST:
                        return MCText.translatable("gui.tcwm.AABAS.pitch_very_fast_button");
                    default:
                        return MCText.text("error");
                }
            }).setSaveConsumer((e) ->
            {
                if (e != null && e != entity.getPitch()) {
                    entity.setPitch(e);
                    PacketUpdateBlockEntity.sendUpdateC2S(entity, pos);
                }
            }).build());

            return bd.build();
        }

        public static class ForgeEventBusListener {
            @SubscribeEvent
            public static void onRegisterCommands(RegisterCommandsEvent event) {
                event.getDispatcher().register(Commands.literal("tcwm").then(Commands.literal("settings").executes((stack) ->
                {
                    Minecraft client = Minecraft.getInstance();
                    client.tell(() -> client.setScreen(JsonIO.getConfigScreen(new ChatScreen("/tcwm settings"))));
                    return 1;
                })));
            }
        }
    }
}
