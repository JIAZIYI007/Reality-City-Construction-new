package msnj.tcwm;

import mtr.client.CustomResources;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TcwmSigns {
  protected static ResourceManager manager;

  private TcwmSigns(){ super(); }

  /*
  本类为Reality City Construction的MTR指示牌实现类
   */
   public static void load(String[] ids, CustomResources.CustomSign[] customSigns){
     for (int i = 0; i < customSigns.length; i++) {
       CustomResources.CUSTOM_SIGNS.put("mtr_custom_sign_" + ids[i], customSigns[i]);
     }
   }
}
