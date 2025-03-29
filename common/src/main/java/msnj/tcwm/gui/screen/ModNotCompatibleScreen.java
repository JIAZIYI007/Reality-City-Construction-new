package msnj.tcwm.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import msnj.tcwm.MCText;
import msnj.tcwm.RealityCityConstruction;
import msnj.tcwm.util.Logger;
import msnj.tcwm.util.MTRVersionsNotCompatibleException;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
#if MC_VERSION >= "12000"
import net.minecraft.client.gui.GuiGraphics;
#endif

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
#if MC_VERSION < "11903"
import net.minecraft.client.gui.components.Widget;
#endif
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.io.IOException;
import java.util.Objects;

public class ModNotCompatibleScreen extends Screen {
    private static String unity;//版本
    private static Component message;//信息
    private Button ExitMinecraftButton;
    private Button ContinueButton;
    private Component ButtonText1;
    private Component ButtonText2;
    static final Logger LOGGER = new Logger(System.out);
    private MultiLineLabel label;

    public ModNotCompatibleScreen(
            String import_unity,
            Component import_message,
            Component button1text,
            Component button2text
    ) {
        super(MCText.translatable("gui.tcwm.MNCS.Title"));
        label = MultiLineLabel.EMPTY;
        unity = import_unity;
        message = import_message;
        ButtonText1 = button1text;//按钮：退出
        ButtonText2 = button2text;//按钮：继续
    }

    public ModNotCompatibleScreen() {
        this(
                new RealityCityConstruction.ClientInit().MIN_MTRVERSION,
                MCText.translatable("gui.tcwm.MNCS.content", () ->
                {
                    String versionOf;
                    try {
                        versionOf = mtr.Keys.class.getField("MOD_VERSION").get(null).toString();
                    } catch (ReflectiveOperationException roe) {
                        roe.printStackTrace();
                        versionOf = "9.9.9";
                    }
                    return new Object[]{RealityCityConstruction.getClientInit().MIN_MTRVERSION, versionOf};
                }),
                MCText.translatable("gui.tcwm.MNCS.button1.content"),
                MCText.translatable("gui.tcwm.MNCS.button2.content")
        );
    }

    protected void init() {
        super.init();
        label = MultiLineLabel.create(this.font, message, this.width - 70);
    #if MC_VERSION < "11903"
    this.ContinueButton = this.addRenderableWidget(new Button(0, 0, 0, 20, ButtonText2, (Button) -> {
      if(minecraft != null) {
        minecraft.clearLevel();
      }
      else{
        LOGGER.info("Skip clear level.");
      }
      minecraft.setScreen(null);
    }));
    #else
        this.ContinueButton = this.addRenderableWidget(Button.builder(ButtonText2, (Button) ->
        {
            #if MC_VERSION < "12004"
            if (minecraft != null) {
                minecraft.clearLevel();
            } else {
                LOGGER.info("Skip clear level.");
            }
            minecraft.setScreen(null);
            #else
            if (minecraft != null) {
                minecraft.clearClientLevel(null);
            } else {
                LOGGER.info("Skip clear level.");
            }
            #endif
        }).build());
    #endif
        setXY(this.ContinueButton, this.width / 2 - 100, this.height / 4 + 110 + 18);
        this.ContinueButton.setWidth(Mth.clamp(200, 0, 380));
    #if MC_VERSION < "11903"
    this.ExitMinecraftButton = this.addRenderableWidget(new Button(0, 0, 0, 20, ButtonText1, (Button) -> {
      try {
        Runtime.getRuntime().exec(new String[]{"explorer.exe", "https://www.mtrbbs.top/"});
      }
      catch (IOException ie){
        LOGGER.warn("Not open web.");
      }
      Minecraft.crash(new CrashReport(
        "MTR Version Min at '"
          + new RealityCityConstruction.ClientInit().MIN_MTRVERSION
          + "', Please Update MTR.",
        new MTRVersionsNotCompatibleException(
          "MTR Version Min at '"
            + new RealityCityConstruction.ClientInit().MIN_MTRVERSION
        )));
    }));
    #else
        this.ExitMinecraftButton = this.addRenderableWidget(Button.builder(ButtonText1, (Button) ->
        {
            try {
                Runtime.getRuntime().exec(new String[]{"explorer.exe", "https://www.mtrbbs.top/"});
            } catch (IOException ie) {
                LOGGER.warn("Not open web.");
            }
            Minecraft.crash(#if MC_VERSION >= "12004" Minecraft.getInstance(), Minecraft.getInstance().gameDirectory,#endif new CrashReport(
                    "MTR Version Min at '"
                            + new RealityCityConstruction.ClientInit().MIN_MTRVERSION
                            + "', Please Update MTR.",
                    new MTRVersionsNotCompatibleException(
                            "MTR Version Min at '"
                                    + new RealityCityConstruction.ClientInit().MIN_MTRVERSION
                    )));
        }).build());
    #endif
        setXY(ExitMinecraftButton, this.width / 2 - 100, this.height / 4 + 86 + 18);
        this.ExitMinecraftButton.setWidth(Mth.clamp(200, 0, 380));
    }

    private static void setXY(Button w, int x, int y) {
        #if MC_VERSION < "11903"
            w.x = x;
            w.y = y;
        #else
            w.setX(x);
            w.setY(y);
        #endif
    }

    public void render(#if MC_VERSION < "12000" PoseStack #else GuiGraphics #endif poseStack, int i, int j, float f) {
        this.renderBackground(poseStack#if MC_VERSION >= "12004", 0, 0, 0 #endif);
        this.renderTitle(poseStack);
        int k = this.width / 2 - (this.width - 70) / 2;
        this.label.renderLeftAligned(poseStack, k, 70, this.getLineHeight(), 16777215);
        super.render(poseStack, i, j, f);
    }

    protected int getLineHeight() {
        Objects.requireNonNull(this.font);
        return 9 * 2;
    }

    protected void renderTitle(#if MC_VERSION < "12000" PoseStack #else GuiGraphics #endif poseStack) {
    #if MC_VERSION < "12000"
    drawString(poseStack, this.font, this.title, 25, 30, 16777215);
    #else
        poseStack.drawString(this.font, this.title, 25, 30, 16777215);
    #endif
    }
}
