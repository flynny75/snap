package org.snapscript.tree;

import java.util.concurrent.atomic.AtomicInteger;

import org.snapscript.core.Index;
import org.snapscript.core.Local;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Table;
import org.snapscript.core.Type;
import org.snapscript.core.convert.CompatibilityChecker;
import org.snapscript.core.error.ErrorCauseExtractor;
import org.snapscript.core.function.Parameter;
import org.snapscript.tree.function.ParameterDeclaration;

public class CatchBlockList {
   
   private final ErrorCauseExtractor extractor;
   private final CompatibilityChecker checker;
   private final AtomicInteger offset;
   private final CatchBlock[] blocks;
   
   public CatchBlockList(CatchBlock... blocks) {
      this.extractor = new ErrorCauseExtractor();
      this.checker = new CompatibilityChecker();
      this.offset = new AtomicInteger(-1);
      this.blocks = blocks;
   }    
   
   public Result compile(Scope scope) throws Exception {  
      for(CatchBlock block : blocks) {
         Statement statement = block.getStatement();
         
         if(statement != null) {
            Index index = scope.getIndex();
            int size = index.size();
            
            try {
               ParameterDeclaration declaration = block.getDeclaration();
               Parameter parameter = declaration.get(scope);
               String name = parameter.getName();
               int value = index.index(name);
               
               offset.set(value);
               statement.compile(scope);
            }finally {
               index.reset(size);
            }
         }
      }
      return Result.getNormal();
   }

   public Result execute(Scope scope, Result result) throws Exception {
      Object data = result.getValue();
      
      for(CatchBlock block : blocks) {
         ParameterDeclaration declaration = block.getDeclaration();
         Statement statement = block.getStatement();
         Parameter parameter = declaration.get(scope);
         Type type = parameter.getType();
         String name = parameter.getName();

         if(data != null) {
            Object cause = extractor.extract(scope, data);
            
            if(checker.compatible(scope, cause, type)) {
               Table table = scope.getTable();
               Local local = Local.getConstant(cause, name);
               int index = offset.get();
               
               table.add(index, local);

               return statement.execute(scope);
            }
         }
      }
      return result;
   }
}