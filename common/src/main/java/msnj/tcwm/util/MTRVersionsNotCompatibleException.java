package msnj.tcwm.util;

import msnj.tcwm.MTRVersions;
import msnj.tcwm.RealityCityConstruction;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;

public class MTRVersionsNotCompatibleException extends Exception implements FunctionD<String, MTRVersions<Object>> {
  public MTRVersionsNotCompatibleException(){
    super();
  }

  public MTRVersionsNotCompatibleException(String string){
    super(string);
  }

  @Override
  public void load(String o, MTRVersions<Object> objectMTRVersions) {
    if(objectMTRVersions.verification("-" + new RealityCityConstruction.ClientInit().MIN_MTRVERSION)){
      return;
    }
    else{
      RealityCityConstruction.LOGGER.error("o");
    }
    Minecraft.crash(#if MC_VERSION >= "12004" Minecraft.getInstance(), Minecraft.getInstance().gameDirectory,#endif new CrashReport(o, this));
  }
}
