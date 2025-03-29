package msnj.tcwm.fabric;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import msnj.tcwm.Info;
import msnj.tcwm.MCText;
import msnj.tcwm.RealityCityConstruction;
import msnj.tcwm.block.Blocks;
import msnj.tcwm.block.StationBroadcaster;
import msnj.tcwm.util.Logger;
import msnj.tcwm.util.TcwmResources;
import msnj.tcwm.util.settings.JsonIO;
import msnj.tcwm.network.PacketUpdateBlockEntity;
import net.fabricmc.api.ClientModInitializer;
#if MC_VERSION >= "12000"
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
#elif MC_VERSION >= "11900"
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
#else
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
#endif
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Optional;

public class ClientInitFabric implements ClientModInitializer {
    public static final Logger LOGGER = new Logger(System.out);
    private static Runtime rn = Runtime.getRuntime();

    @Override
    public void onInitializeClient() {
        #if MC_VERSION >= "11900"
        ClientCommandRegistrationCallback.EVENT.register((d, a) ->
        {
            d#else ClientCommandManager.DISPATCHER #endif.register(ClientCommandManager.literal("tcwm").then(ClientCommandManager.literal("settings").executes((stack) ->
            {
                Minecraft client = ((Minecraft) stack.getSource().getClient());
                client.tell(() -> client.setScreen(JsonIO.getConfigScreen(new ChatScreen("/tcwm settings"))));
                return 1;
            })));
            #if MC_VERSION >= "11900"
        });
        #endif
        JsonIO.setScreenCreator(this::createConfigScreen);
        StationBroadcaster.propertiesScreen = this::createHomoBroadcasterScreen;
        if (Info.isMTRInstalled) {
            ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new ResourcesLoader());
        }
        new RealityCityConstruction.ClientInit().init((t, b) -> BlockRenderLayerMap.INSTANCE.putBlock(b, t), (r, i) -> ClientPlayNetworking.registerGlobalReceiver(r, (client, handler, packet, responseSender) -> i.accept(packet)));
    }

    private static class ResourcesLoader implements SimpleSynchronousResourceReloadListener {

        @Override
        public ResourceLocation getFabricId() {
            return new ResourceLocation(RealityCityConstruction.MOD_ID, "tcwm_resources_rewriter");
        }

        @Override
        public void onResourceManagerReload(ResourceManager resourceManager) {
            TcwmResources.reload(resourceManager);
        }
    }

    Screen createConfigScreen(Screen parent) {
        JsonIO io = JsonIO.read(Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve("tcwm.json"));
        ConfigBuilder bd = (
                ConfigBuilder.create()
                        .setParentScreen(parent)
                        .setTitle(MCText.translatable("gui.tcwm.Settings.title"))
                        .transparentBackground()
                        .alwaysShowTabs()
        );

        ConfigEntryBuilder eb = bd.entryBuilder();

        //第一分类
        {
            ConfigCategory general = bd.getOrCreateCategory(MCText.translatable("gui.tcwm.Settings.category.general"));

            general.addEntry(
                    eb.startBooleanToggle(MCText.translatable("gui.tcwm.Settings.properties1.content"), io.customRailwaySignIsOpen)
                            .setDefaultValue(true)
                            .setSaveConsumer((value) ->
                            {
                                if (value != io.customRailwaySignIsOpen) {
                                    io.customRailwaySignIsOpen = value;
                                    JsonIO.write(io);
                                    Minecraft.getInstance().reloadResourcePacks();
                                }
                            }).build()
            );

            general.addEntry(
                    eb.startBooleanToggle(MCText.translatable("gui.tcwm.Settings.properties2.content"), io.disabledMixins)
                            .setDefaultValue(false)
                            .setSaveConsumer((value) ->
                            {
                                if (value != io.disabledMixins) {
                                    io.disabledMixins = value;
                                    JsonIO.write(io);
                                }
                            }).build()
            );
        }

        return bd.build();
    }

    Screen createHomoBroadcasterScreen(BlockPos pos) {
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

        ConfigBuilder bd = (
                ConfigBuilder.create()
                        .setParentScreen(null)
                        .setTitle(MCText.translatable("gui.tcwm.SSBAS.title"))
                        .transparentBackground()
        );

        ConfigEntryBuilder eb = bd.entryBuilder();

        ConfigCategory main = bd.getOrCreateCategory(MCText.text("main"));

        main.addEntry(
                eb.startTextField(MCText.translatable("gui.tcwm.SSBAS.control1title"), entity.getSoundID())
                        .setDefaultValue("tcwm:music.example")
                        .setErrorSupplier((str) ->
                        {
                            if (!str.matches("[a-z]+:[a-z0-9_\\-.]+")) {
                                return Optional.of(MCText.translatable("gui.tcwm.SSBAS.error.id"));
                            }
                            return Optional.empty();
                        })
                        .setSaveConsumer((str) ->
                        {
                            if (entity.getSoundID().equals(str)) {
                                return;
                            }
                            if (!str.isEmpty()) {
                                entity.setSoundID(str);
                                PacketUpdateBlockEntity.sendUpdateC2S(entity, pos);
                            }
                        }).build()
        );

        main.addEntry(
                eb.startFloatField(MCText.translatable("gui.tcwm.SSBAS.control2title"), entity.getRange())
                        .setDefaultValue(114.514f)
                        .setSaveConsumer((f) ->
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
                        }).build()
        );

        main.addEntry(
                eb.startEnumSelector(MCText.translatable("gui.tcwm.SSBAS.control3title"), StationBroadcaster.StationBroadcasterEntity.Pitch.class, entity.getPitch())
                        .setDefaultValue(StationBroadcaster.StationBroadcasterEntity.Pitch.DEFAULT)
                        .setEnumNameProvider((e) ->
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
                        })
                        .setSaveConsumer((e) ->
                        {
                            if (e != null && e != entity.getPitch()) {
                                entity.setPitch(e);
                                PacketUpdateBlockEntity.sendUpdateC2S(entity, pos);
                            }
                        }).build()
        );

        return bd.build();
    }
}
