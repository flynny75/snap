package org.snapscript.tree.define;

import static org.snapscript.core.Reserved.DEFAULT_PACKAGE;

import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.ModuleRegistry;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.define.Instance;
import org.snapscript.core.define.PrimitiveInstance;

public class AnyInstanceBuilder {
   
   private Module module;
   
   public AnyInstanceBuilder() {
      super();
   }

   public Instance create(Scope scope, Type real) throws Exception {
      Scope inner = real.getScope();
      
      if(module == null) {
         Module parent = scope.getModule();
         Context context = parent.getContext();
         ModuleRegistry registry = context.getRegistry();
         
         module = registry.addModule(DEFAULT_PACKAGE);
      }
      return new PrimitiveInstance(module, inner, real); 
   }
}