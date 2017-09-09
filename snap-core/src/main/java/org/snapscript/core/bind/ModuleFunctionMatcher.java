package org.snapscript.core.bind;

import java.util.List;

import org.snapscript.common.CopyOnWriteSparseArray;
import org.snapscript.common.SparseArray;
import org.snapscript.core.Module;
import org.snapscript.core.TypeExtractor;
import org.snapscript.core.function.Function;
import org.snapscript.core.stack.ThreadStack;

public class ModuleFunctionMatcher {
   
   private final SparseArray<FunctionTable> table;
   private final FunctionTableBuilder builder;
   private final ThreadStack stack;
   
   public ModuleFunctionMatcher(TypeExtractor extractor, ThreadStack stack) {
      this(extractor, stack, 10000);
   }
   
   public ModuleFunctionMatcher(TypeExtractor extractor, ThreadStack stack, int capacity) {
      this.table = new CopyOnWriteSparseArray<FunctionTable>(capacity);
      this.builder = new FunctionTableBuilder(extractor);
      this.stack = stack;
   }
   
   public FunctionPointer match(Module module, String name, Object... values) throws Exception { 
      Function function = resolve(module, name, values);
      
      if(function != null) {
         return new FunctionPointer(function, stack, values);
      }
      return null;
   }

   private Function resolve(Module module, String name, Object... values) throws Exception { 
      int index = module.getOrder();
      FunctionTable cache = table.get(index);
      
      if(cache == null) {
         List<Function> functions = module.getFunctions();
         FunctionTable group = builder.create(module);
         
         for(Function function : functions){
            group.update(function);
         }
         table.set(index, group);
         return group.resolve(name, values);
      }
      return cache.resolve(name, values);
   }
}