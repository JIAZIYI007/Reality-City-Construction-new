package msnj.tcwm.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import msnj.tcwm.util.settings.JsonIO;

public class ModMenuSettingsExecutes implements ModMenuApi {
  @Override
  public ConfigScreenFactory<?> getModConfigScreenFactory() {
    return JsonIO::getConfigScreen;
  }
}
