package msnj.tcwm.util;

import net.minecraft.util.StringRepresentable;

public enum EnumExpwyBarRotateType implements StringRepresentable {
  R22_5("rotate_22_5"), R45("rotate_45"),
  DISABLE("disabledrotate");
  private final String name;
  private EnumExpwyBarRotateType(String name){
    this.name = name;
  }

  @Override
  public String getSerializedName() {
    return name;
  }
}
