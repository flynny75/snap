package org.snapscript.tree.variable;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.snapscript.core.Context;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.TypeExtractor;
import org.snapscript.core.Value;
import org.snapscript.core.property.ConstantPropertyBuilder;
import org.snapscript.core.property.Property;
import org.snapscript.core.property.PropertyValue;

public class ObjectPointer implements VariablePointer<Object> {
   
   private final AtomicReference<Property> reference;
   private final ConstantPropertyBuilder builder;
   private final ConstantResolver resolver;
   private final String name;
   
   public ObjectPointer(ConstantResolver resolver, String name) {
      this.reference = new AtomicReference<Property>();
      this.builder = new ConstantPropertyBuilder();
      this.resolver = resolver;
      this.name = name;
   }
   
   @Override
   public Value get(Scope scope, Object left) {
      Property accessor = reference.get();
      
      if(accessor == null) {
         Property match = match(scope, left);
         
         if(match != null) {
            reference.set(match);
            return new PropertyValue(match, left, name);
         }
         return null;
      }
      return new PropertyValue(accessor, left, name);
   }
   
   public Property match(Scope scope, Object left) {
      Class type = left.getClass();
      Module module = scope.getModule();
      Type source = module.getType(type);
      
      if(source != null) {
         Context context = module.getContext();
         TypeExtractor extractor = context.getExtractor();
         Set<Type> list = extractor.getTypes(source);
         
         for(Type base : list) {
            Property match = match(scope, left, base);
            
            if(match != null) {
               return match;
            }
         }
      }
      return null;
   }
   
   public Property match(Scope scope, Object left, Type type) {
      List<Property> properties = type.getProperties();
      
      for(Property property : properties){
         String field = property.getName();
         
         if(field.equals(name)) {
            return property;
         }
      }
      Scope outer = type.getScope();
      Object value = resolver.resolve(outer, name);

      if(value != null) {
         return builder.createConstant(name, value);
      }
      return null;
   }
   
}