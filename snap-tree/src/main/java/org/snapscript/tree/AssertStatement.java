package org.snapscript.tree;

import org.snapscript.core.AssertionException;
import org.snapscript.core.Compilation;
import org.snapscript.core.Context;
import org.snapscript.core.Evaluation;
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

public class AssertStatement implements Compilation {
   
   private final Statement assertion;

   public AssertStatement(Evaluation evaluation){
      this.assertion = new CompileResult(evaluation);
   }
   
   @Override
   public Statement compile(Module module, Path path, int line) throws Exception {
      Context context = module.getContext();
      ErrorHandler handler = context.getHandler();
      TraceInterceptor interceptor = context.getInterceptor();
      Trace trace = Trace.getNormal(module, path, line);
      
      return new TraceStatement(interceptor, handler, assertion, trace);
   }
   
   private static class CompileResult extends Statement {
   
      private final Evaluation evaluation;

      public CompileResult(Evaluation evaluation){
         this.evaluation = evaluation;
      }

      @Override
      public Result compile(Scope scope) throws Exception {
         evaluation.compile(scope);
         return Result.getNormal();
      }
      
      @Override
      public Result execute(Scope scope) throws Exception {
         Value value = evaluation.evaluate(scope, null);
         Object object = value.getValue();

         if(!Boolean.TRUE.equals(object)) {
            throw new AssertionException("Assertion failed");
         }
         return Result.getNormal();
      }
   }
}