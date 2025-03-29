package msnj.tcwm;

import msnj.tcwm.util.ObjectFunction;
import net.minecraft.network.chat.Component;
#if MC_VERSION <= "11900"
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
#endif

public class MCText {
  #if MC_VERSION <= "11900"
  public static TranslatableComponent translatable(String s) {return new TranslatableComponent(s);};
  public static TranslatableComponent translatable(String s, Object... j) {return new TranslatableComponent(s,j);};
  public static TranslatableComponent translatable(String s, ObjectFunction f) {return new TranslatableComponent(s,f.get());};
  public static TextComponent text(String s) {return new TextComponent(s);};
  #else
  public static Component translatable(String s) {return Component.translatable(s);};
  public static Component translatable(String s, Object... j) {return Component.translatable(s,j);};
  public static Component translatable(String s, ObjectFunction f) {return Component.translatable(s,f.get());};
  public static Component text(String s) {return Component.literal(s);};
  #endif
}
