package org.snapscript.tree.condition;

import org.snapscript.core.Compilation;
import org.snapscript.core.Context;
import org.snapscript.core.Evaluation;
import org.snapscript.core.Index;
import org.snapscript.core.Module;
import org.snapscript.core.Path;
import org.snapscript.core.Result;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Value;
import org.snapscript.core.error.ErrorHandler;
import org.snapscript.core.trace.Trace;
import org.snapscript.core.trace.TraceInterceptor;
import org.snapscript.core.trace.TraceStatement;

public class ForStatement implements Compilation {
   
   private final Statement loop;
   
   public ForStatement(Statement declaration, Evaluation evaluation, Statement statement) {
      this(declaration, evaluation, null, statement);
   }
   
   public ForStatement(Statement declaration, Evaluation evaluation, Evaluation assignment, Statement statement) {
      this.loop = new CompileResult(declaration, evaluation, assignment, statement);
   }
   
   @Override
   public Statement compile(Module module, Path path, int line) throws Exception {
      Context context = module.getContext();
      ErrorHandler handler = context.getHandler();
      TraceInterceptor interceptor = context.getInterceptor();
      Trace trace = Trace.getNormal(module, path, line);
      
      return new TraceStatement(interceptor, handler, loop, trace);
   }
   
   private static class CompileResult extends Statement {

      private final Evaluation condition;
      private final Statement declaration;
      private final Evaluation assignment;
      private final Statement body;
      private final Result normal;

      public CompileResult(Statement declaration, Evaluation condition, Evaluation assignment, Statement body) {
         this.normal = Result.getNormal();
         this.declaration = declaration;
         this.assignment = assignment;
         this.condition = condition;
         this.body = body;
      }
      
      @Override
      public Result compile(Scope scope) throws Exception {
         Index index = scope.getIndex();
         int size = index.size();
         
         try {
            declaration.compile(scope);
            condition.compile(scope);
            
            if(assignment != null) {
               assignment.compile(scope);
            }
            return body.compile(scope);
         } finally {
            index.reset(size);
         }
      }
      
      @Override
      public Result execute(Scope scope) throws Exception {
         declaration.execute(scope);
         
         while(true) {
            Value result = condition.evaluate(scope, null);
            Boolean value = result.getBoolean();
            
            if(value.booleanValue()) {
               Result next = body.execute(scope);
               
               if(next.isReturn()) {
                  return next;
               }
               if(next.isBreak()) {
                  return normal;
               }
            } else {
               return normal;
            } 
            if(assignment != null) {
               assignment.evaluate(scope, null);
            }
         }
      }
   }
}