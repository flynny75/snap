
package org.snapscript.tree.define;

import java.util.concurrent.atomic.AtomicBoolean;

import org.snapscript.core.Result;
import org.snapscript.core.ResultType;
import org.snapscript.core.Scope;
import org.snapscript.core.Statement;
import org.snapscript.core.Type;
import org.snapscript.core.TypeFactory;
import org.snapscript.tree.annotation.AnnotationList;

public class ClassDefinition extends Statement {   
   
   private final FunctionPropertyGenerator generator;
   private final DefaultConstructor constructor;
   private final TypeFactoryCollector collector;
   private final TypeFactory constants;
   private final AtomicBoolean compile;
   private final AtomicBoolean define;
   private final ClassBuilder builder;
   private final TypePart[] parts;
   
   public ClassDefinition(AnnotationList annotations, TypeName name, TypeHierarchy hierarchy, TypePart... parts) {
      this.builder = new ClassBuilder(annotations, name, hierarchy);
      this.generator = new FunctionPropertyGenerator(); 
      this.constants = new StaticConstantFactory();
      this.collector = new TypeFactoryCollector();
      this.constructor = new DefaultConstructor();
      this.compile = new AtomicBoolean(true);
      this.define = new AtomicBoolean(true);
      this.parts = parts;
   }
   
   @Override
   public Result define(Scope outer) throws Exception {
      if(!define.compareAndSet(false, true)) {
         Result result = builder.define(outer);
         Type type = result.getValue();
         
         for(TypePart part : parts) {
            TypeFactory factory = part.define(collector, type);
            collector.update(factory);
         } 
         return result;
      }
      return ResultType.getNormal();
   }

   @Override
   public Result compile(Scope outer) throws Exception {
      if(!compile.compareAndSet(false, true)) {
         Result result = builder.compile(outer);
         Type type = result.getValue();
         
         collector.update(constants); // collect static constants first
         
         for(TypePart part : parts) {
            TypeFactory factory = part.compile(collector, type);
            collector.update(factory);
         } 
         constructor.compile(collector, type);
         generator.generate(type);
         
         return result;
      }
      return ResultType.getNormal();
   }

}
