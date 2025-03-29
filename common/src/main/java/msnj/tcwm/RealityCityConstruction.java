package msnj.tcwm;

import msnj.tcwm.block.Blocks;
import msnj.tcwm.util.FunctionP;
import msnj.tcwm.util.Logger;
import msnj.tcwm.util.settings.JsonIO;
import msnj.tcwm.item.Items;
import msnj.tcwm.item.TcwmCreativeModeTab;
import msnj.tcwm.network.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class RealityCityConstruction {
    private static TRegistry<Object> sys = TRegistry.INSTANCE;
    public static final String MOD_ID = "tcwm";

    public static final Logger LOGGER = new Logger(System.out);

    public static final Map<String, String> REPLACE_BLOCKS = new HashMap<>();

    @SuppressWarnings("all")
    public static void init(BiConsumer<ResourceLocation, FunctionP<MinecraftServer, ServerPlayer, FriendlyByteBuf>> cnrr) {
        #if MC_VERSION < "12000" TcwmCreativeModeTab.ITEMS.register(); #endif
        sys.bi("logo", Blocks.LOGO_BLOCK, TcwmCreativeModeTab.ITEMS);
        sys.bi("marble", Blocks.MARBLE, TcwmCreativeModeTab.ITEMS);
        sys.bi("white_marble", Blocks.WHITE_MARBLE, TcwmCreativeModeTab.ITEMS);
        sys.bi("yellow_blind", Blocks.YELLOW_BLIND, TcwmCreativeModeTab.ITEMS);
        sys.bi("black_blind", Blocks.BLACK_BLIND, TcwmCreativeModeTab.ITEMS);
        sys.bi("yellow_blind_corner", Blocks.YELLOW_BLIND_CORNER, TcwmCreativeModeTab.ITEMS);
        sys.bi("black_blind_corner", Blocks.BLACK_BLIND_CORNER, TcwmCreativeModeTab.ITEMS);
        sys.bi("yellow_blind_slab", Blocks.YELLOW_BLIND_SLAB, TcwmCreativeModeTab.ITEMS);
        sys.bi("black_blind_slab", Blocks.BLACK_BLIND_SLAB, TcwmCreativeModeTab.ITEMS);
        sys.bi("peoplemangdao", Blocks.D_YELLOW_BLIND);
        sys.bi("peoplemangdaoblack", Blocks.D_BLACK_BLIND);
        sys.bi("peoplemangdao_slab", Blocks.D_YELLOW_BLIND_SLAB);
        sys.bi("peoplemangdaoblack_slab", Blocks.D_BLACK_BLIND_SLAB);
        sys.bi("metal", Blocks.METAL);
        sys.bi("tunnelline", Blocks.TUNNEL_WIRES_STYLE_1, TcwmCreativeModeTab.ITEMS);
        sys.bi("tunnellinenoblock", Blocks.TUNNEL_WIRES_STYLE_2, TcwmCreativeModeTab.ITEMS);
        sys.bi("tunnellight", Blocks.TUNNEL_LIGHT, TcwmCreativeModeTab.ITEMS);
        sys.bi("window", Blocks.WINDOW, TcwmCreativeModeTab.ITEMS);
        sys.bi("windowpane", Blocks.WINDOW_PANE, TcwmCreativeModeTab.ITEMS);
        sys.bi("platform", Blocks.PLATFORM, TcwmCreativeModeTab.ITEMS);
        sys.bi("platform_type_quartz", Blocks.PLATFORM_TYP_QUARTZ, TcwmCreativeModeTab.ITEMS);
        sys.bi("platform_indented", Blocks.PLATFORM_INDENTED, TcwmCreativeModeTab.ITEMS);
        sys.bi("sign_1", Blocks.SIGN, TcwmCreativeModeTab.ITEMS);
        sys.bi("sign_2", Blocks.SIGN_2, TcwmCreativeModeTab.ITEMS);
        sys.bi("sign_3", Blocks.SIGN_3, TcwmCreativeModeTab.ITEMS);
        sys.bi("expwy_bar_type_1", Blocks.EXPWY_BAR_TYPE_1, TcwmCreativeModeTab.ITEMS);
        sys.bi("expwy_bar_type_1_bj_4", Blocks.EXPWY_BAR_TYPE_1_BEIJING_4, TcwmCreativeModeTab.ITEMS);
        sys.bi("expwy_bar_type_1_sh", Blocks.EXPWY_BAR_TYPE_1_SHANGHAI, TcwmCreativeModeTab.ITEMS);
        sys.bi("expwy_bar_type_1_oc_new", Blocks.EXPWY_BAR_TYPE_1_OC_NEW, TcwmCreativeModeTab.ITEMS);
        sys.bi("expwy_bar_type_1_oc_new_ref", Blocks.EXPWY_BAR_TYPE_1_OC_NEW_REF, TcwmCreativeModeTab.ITEMS);
        sys.bi("expwy_bar_type_1_oc_new_2", Blocks.EXPWY_BAR_TYPE_1_OC_NEW_2, TcwmCreativeModeTab.ITEMS);
        sys.bi("expwy_bar_type_1_oc_new_2_ref", Blocks.EXPWY_BAR_TYPE_1_OC_NEW_2_REF, TcwmCreativeModeTab.ITEMS);
        sys.bi("expwy_bar_type_2", Blocks.EXPWY_BAR_TYPE_2, TcwmCreativeModeTab.ITEMS);
        sys.bi("expwy_bar_type_2_placeholder", Blocks.EXPWY_BAR_TYPE_2_PLACEHOLDER, TcwmCreativeModeTab.ITEMS);
        sys.bi("expwy_bar_type_2_green", Blocks.EXPWY_BAR_TYPE_2_GR, TcwmCreativeModeTab.ITEMS);
        sys.bi("expwy_bar_type_2_placeholder_green", Blocks.EXPWY_BAR_TYPE_2_PLACEHOLDER_GR, TcwmCreativeModeTab.ITEMS);
        sys.bi("expwy_bar_type_2_new_gray", Blocks.EXPWY_BAR_TYPE_2_NEW, TcwmCreativeModeTab.ITEMS);
        sys.bi("expwy_bar_type_2_new_placeholder_gray", Blocks.EXPWY_BAR_TYPE_2_NEW_PLACEHOLDER, TcwmCreativeModeTab.ITEMS);
        sys.bi("expwy_bar_type_2_new_green", Blocks.EXPWY_BAR_TYPE_2_NEW_GR, TcwmCreativeModeTab.ITEMS);
        sys.bi("expwy_bar_type_2_new_placeholder_green", Blocks.EXPWY_BAR_TYPE_2_NEW_PLACEHOLDER_GR, TcwmCreativeModeTab.ITEMS);
        sys.bi("expwy_bar_type_2_double_gray", Blocks.EXPWY_BAR_TYPE_2_DOUBLE, TcwmCreativeModeTab.ITEMS);
        sys.bi("expwy_bar_type_2_double_placeholder_gray", Blocks.EXPWY_BAR_TYPE_2_DOUBLE_PLACEHOLDER, TcwmCreativeModeTab.ITEMS);
        sys.bi("expwy_bar_type_2_double_green", Blocks.EXPWY_BAR_TYPE_2_DOUBLE_GR, TcwmCreativeModeTab.ITEMS);
        sys.bi("expwy_bar_type_2_double_placeholder_green", Blocks.EXPWY_BAR_TYPE_2_DOUBLE_PLACEHOLDER_GR, TcwmCreativeModeTab.ITEMS);
        sys.bi("expwy_bar_type_2_new_double_gray", Blocks.EXPWY_BAR_TYPE_2_NEW_DOUBLE, TcwmCreativeModeTab.ITEMS);
        sys.bi("expwy_bar_type_2_new_double_placeholder_gray", Blocks.EXPWY_BAR_TYPE_2_NEW_DOUBLE_PLACEHOLDER, TcwmCreativeModeTab.ITEMS);
        sys.bi("expwy_bar_type_2_new_double_green", Blocks.EXPWY_BAR_TYPE_2_NEW_DOUBLE_GR, TcwmCreativeModeTab.ITEMS);
        sys.bi("expwy_bar_type_2_new_double_placeholder_green", Blocks.EXPWY_BAR_TYPE_2_NEW_DOUBLE_PLACEHOLDER_GR, TcwmCreativeModeTab.ITEMS);
        /*sys.bi("expwy_bar_type_2_new", Blocks.EXPWY_BAR_TYPE_2_NEW, BLO);
        sys.bi("expwy_bar_type_2_new_placeholder", Blocks.EXPWY_BAR_TYPE_2_NEW_PLACEHOLDER, BLO);*/
        sys.bi("expwy_bar_type_3", Blocks.EXPWY_BAR_TYPE_3, TcwmCreativeModeTab.ITEMS);
        sys.bi("homo_station_broadcaster", Blocks.STATION_BROADCASTER, TcwmCreativeModeTab.ITEMS);
        sys.be("homo_station_broadcaster_entity", Blocks.BlockEntityTypes.HOMO_STATION_BROADCASTER);
        sys.bi("transmission_line", Blocks.TRANSMISSIONLINE, TcwmCreativeModeTab.ITEMS);
        sys.bi("boat_dock", Blocks.BOAT_DOCK, TcwmCreativeModeTab.ITEMS);
        sys.bi("expwy_caution_bar", Blocks.EXPWY_CAUTION_BAR, TcwmCreativeModeTab.ITEMS);
        sys.bi("anti_glare_board_type_1", Blocks.SHADING_PANEL_TYPE_1, TcwmCreativeModeTab.ITEMS);
        REPLACE_BLOCKS.put("tcwm:shading_panel_type_1", "tcwm:anti_glare_board_type_1");
        sys.bi("anti_glare_board_type_2", Blocks.SHADING_PANEL_TYPE_2, TcwmCreativeModeTab.ITEMS);
        REPLACE_BLOCKS.put("tcwm:shading_panel_type_2", "tcwm:anti_glare_board_type_2");
        sys.bi("air_conditioner", Blocks.AIR_CONDITIONER);
        sys.bi("air_conditioner_external_unit", Blocks.AIR_CONDITIONER_EU);

        sys.i("forge_tool", Items.FORGE_TOOL);
        LOGGER.info("Reality City Construction Mod Items and Blocks Registred.");
        #if MC_VERSION >= "12000" TcwmCreativeModeTab.ITEMS.register(); #endif
        cnrr.accept(PacketUpdateBlockState.PACKET_UPDATE_BLOCK_STATE, PacketUpdateBlockState::receiveUpdateBlockStateC2S);
        cnrr.accept(PacketUpdateBlockEntity.PACKET_UPDATE_BLOCK_ENTITY, PacketUpdateBlockEntity::receiveUpdateC2S);
        LOGGER.info("Reality City Construction Mod Initialized.");
    }

    public static ClientInit getClientInit() {
        return new RealityCityConstruction.ClientInit();
    }

    public static class ClientInit {
        public String MIN_MTRVERSION = "3.2.0";
        public final Logger LOGGER = new Logger(System.out);
        private Runtime rn = Runtime.getRuntime();

        @SuppressWarnings("all")
        public void init(BiConsumer<RenderType, Block> brr, BiConsumer<ResourceLocation, Consumer<FriendlyByteBuf>> nrr) {
            LOGGER.info("RCCmod Client Task Loading");
            if (!Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve("tcwm.json").toFile().exists()) {
                JsonIO io = JsonIO.get(Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve("tcwm.json"));
                JsonIO.write(io);
            }
            brr.accept(RenderType.cutoutMipped(), Blocks.PLATFORM.get());
            brr.accept(RenderType.cutoutMipped(), Blocks.PLATFORM_TYP_QUARTZ.get());
            brr.accept(RenderType.cutoutMipped(), Blocks.PLATFORM_INDENTED.get());
            brr.accept(RenderType.translucent(), Blocks.WINDOW.get());
            brr.accept(RenderType.translucent(), Blocks.WINDOW_PANE.get());
            brr.accept(RenderType.cutout(), Blocks.EXPWY_BAR_TYPE_1.get());
            brr.accept(RenderType.cutout(), Blocks.EXPWY_BAR_TYPE_2.get());
            brr.accept(RenderType.cutout(), Blocks.EXPWY_BAR_TYPE_3.get());
            //brr.accept(RenderType.cutout(), Blocks.BLACK_BLIND.get());
            //brr.accept(RenderType.cutout(), Blocks.YELLOW_BLIND.get());
            brr.accept(RenderType.cutout(), Blocks.BOAT_DOCK.get());
            brr.accept(RenderType.cutout(), Blocks.AIR_CONDITIONER.get());
            nrr.accept(PacketScreen.PACKET_SHOW_SCREEN, PacketScreen::receiveScreenS2C);
            nrr.accept(PacketRunCommand.PACKET_RUNCOMMAND, PacketRunCommand::receiveCommandS2C);
            nrr.accept(PacketPlaySound.PACKET_PLAY_SOUND, PacketPlaySound::receivePlaySoundS2C);
        }
    }
}
