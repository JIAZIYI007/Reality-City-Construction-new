package msnj.tcwm.util;

import java.io.PrintStream;

public class Logger {
  private static PrintStream printStream;
  public Logger(PrintStream ps){
    printStream = ps;
  }

  public void info(String value){
    printStream.println("[RCCinfo] " + value);
  }

  public void warn(String value){
    printStream.println("[RCCwarn] " + value);
  }

  public void error(String value){
    printStream.println("[RCCerror] " + value);
  }

  public void infol(String value){
    printStream.print("[RCCinfo] " + value);
  }

  public void warnl(String value){
    printStream.print("[RCCwarn] " + value);
  }

  public void errorl(String value){
    printStream.print("[RCCerror] " + value);
  }
}
