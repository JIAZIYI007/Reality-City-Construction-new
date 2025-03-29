package msnj.tcwm;
import msnj.tcwm.util.MTRVersionsNotCompatibleException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MTRVersions<G> implements Comparable<MTRVersions<G>> {
  public final int[] parts;
  public MTRVersions(int[] parts){
    this.parts = parts;
  }

  public static MTRVersions<Object> parse(String src) {
    ArrayList<Integer> parts = new ArrayList<>();
    String[] strParts = src.split("-");
    for (int i = 1; i < strParts.length; i++) {
      String[] subParts = strParts[i].split("\\.");
      for (String subPart : subParts) {
        if (subPart.matches("\\d+")) {
          parts.add(Integer.parseInt(subPart));
        }
      }
    }
    return new MTRVersions<>(parts.stream().mapToInt(i -> i).toArray());
  }

  @Override
  public int compareTo(@NotNull(exception = MTRVersionsNotCompatibleException.class) MTRVersions o) {
    for (int i = 0; i < Math.min(this.parts.length, o.parts.length); i++) {
      if (this.parts[i] != o.parts[i]) {
        return Integer.compare(this.parts[i], o.parts[i]);
      }
    }
    return Integer.compare(this.parts.length, o.parts.length);
  }

  public boolean verification(String path){
    return !(this.compareTo(parse("-" + path)) < 0);
  }

  @Override
  public String toString() {
    int f = 0;
    for (int part : parts) {
      if (f == 0) {
        f = part;
      } else {
        f = f * 10 + part;
      }
    }
    return String.valueOf(f);
  }
}
