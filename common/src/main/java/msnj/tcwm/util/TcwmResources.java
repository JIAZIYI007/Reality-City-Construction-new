package msnj.tcwm.util;

import msnj.tcwm.TcwmSigns;
import msnj.tcwm.util.settings.JsonIO;
import mtr.client.CustomResources;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class TcwmResources {
  public static void reload(ResourceManager manager) {
    boolean isOpen;

    if(Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve("tcwm.json").toFile().exists()){
      isOpen = JsonIO.read(Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve("tcwm.json")).customRailwaySignIsOpen;
    }
    else{
      JsonIO io = JsonIO.get(Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve("tcwm.json"));
      isOpen = io.customRailwaySignIsOpen;
      JsonIO.write(io);
    }

    if(!isOpen){
      return;
    }
    String[] ids = {
      "entrance_gate_1",
      "entrance_gate_2",
      "entrance_gate",
      "exit_gate_1",
      "exit_gate_2",
      "exit_gate",
      "entrance_station"
    };

    CustomResources.CustomSign[] signs = {
      new CustomResources.CustomSign(new ResourceLocation("mtr","graphics/entrance_gate.png"), false, "入闸处|Entrance", false, false, 0),
      new CustomResources.CustomSign(new ResourceLocation("mtr","graphics/entrance_gate.png"), true, "入闸处|Entrance", true, false, 0),
      new CustomResources.CustomSign(new ResourceLocation("mtr","graphics/entrance_gate.png"), false, "", false, false, 0),
      new CustomResources.CustomSign(new ResourceLocation("mtr","graphics/exit_gate.png"), false, "出闸处|Exit", false, false, 0),
      new CustomResources.CustomSign(new ResourceLocation("mtr","graphics/exit_gate.png"), true, "出闸处|Exit", true, false, 0),
      new CustomResources.CustomSign(new ResourceLocation("mtr","graphics/exit_gate.png"), false, "", false, false, 0),
      new CustomResources.CustomSign(new ResourceLocation("mtr","graphics/entrance_station.png"), false, "", false, true, 0)
    };

      TcwmSigns.load(ids, signs);

    //TcwmCustomResources.reload(manager);

  }
}
