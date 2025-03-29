package msnj.tcwm.mixin;

import msnj.tcwm.Info;
import msnj.tcwm.MTRVersions;
import msnj.tcwm.RealityCityConstruction;
import msnj.tcwm.gui.screen.ModNotCompatibleScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class MixinTitleScreenOfModVersion {
  @Unique
  private boolean realityCityConstructionLocal1_16_5_1_19_4$isOpen = true;
  @Inject(
    at = @At("TAIL"),
    method = {"tick"}
  )
  private void init(CallbackInfo ci){
    if (!Info.isMTRInstalled) return;

    if (realityCityConstructionLocal1_16_5_1_19_4$isOpen){
      String version;
      try {
        version = (Class.forName("mtr.Keys").getField("MOD_VERSION").toString());
      } catch (Exception e){
        e.printStackTrace();
        version = ("-" + "9.9.9");
      }
      if (version.startsWith("3")) {
        if (version.equals("-" + "9.9.9") && (MTRVersions.parse(version).compareTo(MTRVersions.parse(
                "-" + RealityCityConstruction.getClientInit().MIN_MTRVERSION)) < 0))
          Minecraft.getInstance().execute(() ->
          {
            Minecraft.getInstance().setScreen(new ModNotCompatibleScreen());
          });
        realityCityConstructionLocal1_16_5_1_19_4$isOpen = false;
      }
    }
  }
}
