package org.snapscript.core.index;

import java.util.List;

import org.snapscript.common.CompleteProgress;
import org.snapscript.common.Progress;
import org.snapscript.core.Category;
import org.snapscript.core.Module;
import org.snapscript.core.Phase;
import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.TypeDescription;
import org.snapscript.core.TypeScope;
import org.snapscript.core.annotation.Annotation;
import org.snapscript.core.function.Function;
import org.snapscript.core.property.Property;

public class ClassType implements Type {

   private final TypeDescription description;
   private final Progress<Phase> progress;
   private final ClassIndex index;
   private final Scope scope;
   private final Class type;
   private final String name;
   private final int order;
   
   public ClassType(ClassIndexer indexer, Class type, String name, int order) {
      this.progress = new CompleteProgress<Phase>();
      this.description = new TypeDescription(this);
      this.index = new ClassIndex(indexer, this);
      this.scope = new TypeScope(this);
      this.name = name;
      this.type = type;
      this.order = order;
   }
   
   @Override
   public Progress<Phase> getProgress() {
      return progress;
   }
   
   @Override
   public List<Annotation> getAnnotations() {
      return index.getAnnotations();
   }
   
   @Override
   public List<Property> getProperties() {
      return index.getProperties();
   }

   @Override
   public List<Function> getFunctions() {
      return index.getFunctions();
   }

   @Override
   public List<Type> getTypes() {
      return index.getTypes();
   }
   
   @Override
   public Category getCategory() {
      return index.getCategory();
   }
   
   @Override
   public Module getModule() {
      return index.getModule();
   }
   
   @Override
   public Type getOuter() {
      return index.getOuter();
   }

   @Override
   public Type getEntry() {
      return index.getEntry();
   }
   
   @Override
   public Scope getScope() {
      return scope;
   }

   @Override
   public String getName() {
      return name;
   }

   @Override
   public Class getType() {
      return type;
   }
   
   @Override
   public int getOrder(){
      return order;
   }
   
   @Override
   public String toString() {
      return description.toString();
   }

}