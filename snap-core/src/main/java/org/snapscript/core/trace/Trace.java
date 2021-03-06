package org.snapscript.core.trace;

import static org.snapscript.core.trace.TraceType.CONSTRUCT;
import static org.snapscript.core.trace.TraceType.INVOKE;
import static org.snapscript.core.trace.TraceType.NATIVE;
import static org.snapscript.core.trace.TraceType.NORMAL;

import org.snapscript.core.Module;
import org.snapscript.core.Path;

public class Trace {
   
   public static Trace getNative(Module module, Path path) {
      return new Trace(NATIVE, module, path, -2); // see StackTraceElement.isNativeMethod
   }
   
   public static Trace getConstruct(Module module, Path path, int line) {
      return new Trace(CONSTRUCT, module, path, line);
   }
   
   public static Trace getInvoke(Module module, Path path, int line) {
      return new Trace(INVOKE, module, path, line);
   }
   
   public static Trace getNormal(Module module, Path path, int line) {
      return new Trace(NORMAL, module, path, line);
   }
   
   private final TraceType type;
   private final Module module;
   private final Path path;
   private final int line;
   
   public Trace(TraceType type, Module module, Path path, int line) {
      this.module = module;
      this.path = path;
      this.line = line;
      this.type = type;
   }

   public TraceType getType() {
      return type;
   }
   
   public Module getModule(){
      return module;
   }

   public Path getPath() {
      return path;
   }

   public int getLine() {
      return line;
   }
   
   @Override
   public String toString() {
      return String.format("%s at line %s", path, line);
   }
}