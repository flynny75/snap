package org.snapscript.compile.define;

import java.util.ArrayList;
import java.util.List;

import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.TypeDescription;
import org.snapscript.core.TypeScope;
import org.snapscript.core.Type;
import org.snapscript.core.annotation.Annotation;
import org.snapscript.core.function.Function;
import org.snapscript.core.property.Property;

public class MockType implements Type {

   private final TypeDescription description;
   private final List<Annotation> annotations;
   private final List<Property> properties;
   private final List<Function> functions;
   private final List<Type> types;
   private final Module module;
   private final Scope scope;
   private final Class type;
   private final Type entry;
   private final String name;

   public MockType(Module module, String name, Type entry, Class type){
      this.description = new TypeDescription(this);
      this.annotations = new ArrayList<Annotation>();
      this.properties = new ArrayList<Property>();
      this.functions = new ArrayList<Function>();
      this.types = new ArrayList<Type>();
      this.scope = new TypeScope(this);
      this.module = module;
      this.entry = entry;
      this.type = type;
      this.name = name;
   }
   
   @Override
   public List<Annotation> getAnnotations() {
      return annotations;
   }
   
   @Override
   public List<Property> getProperties() {
      return properties;
   }
   
   @Override
   public List<Function> getFunctions(){
      return functions;
   }
   
   @Override
   public List<Type> getTypes(){
      return types;
   }
   
   @Override
   public Module getModule(){
      return module;
   }
   
   @Override
   public Scope getScope(){
      return scope;
   }
   
   @Override
   public Class getType() {
      return type;
   }
   
   @Override
   public Type getOuter(){
      return null;
   }
   
   @Override
   public Type getEntry(){
      return entry;
   }
   
   @Override
   public String getName(){
      return name;
   }
   
   @Override
   public int getOrder(){
      return 0;
   }
   
   @Override
   public String toString() {
      return description.toString();
   }
}
