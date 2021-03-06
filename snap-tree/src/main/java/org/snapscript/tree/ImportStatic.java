package org.snapscript.tree;

import java.util.List;

import org.snapscript.core.Compilation;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.ModifierType;
import org.snapscript.core.Module;
import org.snapscript.core.NameBuilder;
import org.snapscript.core.Path;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;
import org.snapscript.core.TypeNameBuilder;
import org.snapscript.core.function.Function;

public class ImportStatic implements Compilation {   
   
   private final Qualifier qualifier;    
   
   public ImportStatic(Qualifier qualifier) {
      this.qualifier = qualifier;
   }

   @Override
   public Object compile(Module module, Path path, int line) throws Exception {
      String location = qualifier.getLocation();
      String target = qualifier.getTarget();
      String name = qualifier.getName();
      
      return new CompileResult(location, target, name);
   }
   
   private static class CompileResult extends Statement {
      
      private final NameBuilder builder;
      private final String location;
      private final String target;
      private final String prefix;
      
      public CompileResult(String location, String target, String prefix) {
         this.builder = new TypeNameBuilder();
         this.location = location;
         this.target = target;
         this.prefix = prefix;
      }
      
      @Override
      public Result compile(Scope scope) throws Exception {
         Module module = scope.getModule();
         String parent = builder.createFullName(location, target);
         Type type = module.getType(parent); // this is a type name
         
         if(type == null) {
            throw new InternalStateException("Could not import '" + parent + "'");
         }
         List<Function> methods = type.getFunctions();
         List<Function> functions = module.getFunctions();
         
         for(Function method : methods){
            int modifiers = method.getModifiers();
            
            if(ModifierType.isStatic(modifiers) && ModifierType.isPublic(modifiers)){
               String name = method.getName();
               
               if(prefix == null || prefix.equals(name)) {
                  functions.add(method);
               }
            }
         }
         return Result.getNormal();
      }
      
   }
}