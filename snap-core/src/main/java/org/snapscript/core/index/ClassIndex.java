package org.snapscript.core.index;

import java.util.List;

import org.snapscript.core.Category;
import org.snapscript.core.InternalStateException;
import org.snapscript.core.Module;
import org.snapscript.core.Type;
import org.snapscript.core.annotation.Annotation;
import org.snapscript.core.function.Function;
import org.snapscript.core.property.Property;

public class ClassIndex {
   
   private List<Annotation> annotations;
   private List<Property> properties;
   private List<Function> functions;
   private ClassIndexer indexer;
   private List<Type> types;
   private ClassType require;
   private Category category;
   private Module module;
   private Type outer;
   private Type entry;
   
   public ClassIndex(ClassIndexer indexer, ClassType require) {      
      this.indexer = indexer;
      this.require = require;
   }
   
   public List<Annotation> getAnnotations() {
      if(annotations == null) {
         try {
            annotations = indexer.indexAnnotations(require);
         } catch(Exception e) {
            throw new InternalStateException("Could not index " + require, e);
         }
      }
      return annotations;
   }

   public List<Property> getProperties() {
      if(properties == null) {
         try {
            properties = indexer.indexProperties(require);
         } catch(Exception e) {
            throw new InternalStateException("Could not index " + require, e);
         }
      }
      return properties;
   }
   
   public List<Function> getFunctions() {
      if(functions == null) {
         try {
            functions = indexer.indexFunctions(require);
         } catch(Exception e) {
            throw new InternalStateException("Could not index " + require, e);
         }
      }
      return functions;
   }
   
   public List<Type> getTypes() {
      if(types == null) {
         try {
            types = indexer.indexTypes(require);
         } catch(Exception e) {
            throw new InternalStateException("Could not index " + require, e);
         }
      }
      return types;
   }
   
   public Module getModule() {
      if(module == null) {
         try {
            module = indexer.indexModule(require);
         } catch(Exception e) {
            throw new InternalStateException("Could not index " + require, e);
         }
      }
      return module;
   }
   
   public Type getOuter() {
      if(entry == null) {
         try {
            entry = indexer.indexOuter(require);
         } catch(Exception e) {
            throw new InternalStateException("Could not index " + require, e);
         }
      }
      return entry;
   }

   public Type getEntry() {
      if(entry == null) {
         try {
            entry = indexer.indexEntry(require);
         } catch(Exception e) {
            throw new InternalStateException("Could not index " + require, e);
         }
      }
      return entry;
   }
   
   public Category getCategory() {
      if(category == null) {
         try {
            category = indexer.indexCategory(require);
         } catch(Exception e) {
            throw new InternalStateException("Could not index " + require, e);
         }
      }
      return category;
   }
}