package org.snapscript.core.index;

import static org.snapscript.core.ModifierType.CONSTANT;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.snapscript.core.ModifierType;
import org.snapscript.core.PrimitivePromoter;
import org.snapscript.core.Type;
import org.snapscript.core.annotation.Annotation;
import org.snapscript.core.annotation.AnnotationExtractor;
import org.snapscript.core.property.Property;

public class PropertyIndexer {
   
   private final AnnotationExtractor extractor;
   private final ClassPropertyBuilder builder;
   private final ModifierConverter converter;
   private final PropertyGenerator generator;
   private final PrimitivePromoter promoter;
   private final MethodMatcher matcher;
   private final TypeIndexer indexer;
   
   public PropertyIndexer(TypeIndexer indexer){
      this.builder = new ClassPropertyBuilder(indexer);
      this.extractor = new AnnotationExtractor();
      this.converter = new ModifierConverter();
      this.generator = new PropertyGenerator();
      this.promoter = new PrimitivePromoter();
      this.matcher = new MethodMatcher();
      this.indexer = indexer;
   }

   public List<Property> index(Class source) throws Exception {
      List<Property> properties = builder.create(source);
      Method[] methods = source.getDeclaredMethods();
      Field[] fields = source.getDeclaredFields();
      Class[] types = source.getDeclaredClasses();
      Type type = indexer.loadType(source);

      if(fields.length > 0 || methods.length > 0 || types.length >0) {
         Set<String> done = new HashSet<String>();
         
         if(fields.length > 0) {
            List<Property> indexed = index(type, fields);
            
            for(Property property : indexed) {
               String name = property.getName();
               
               if(done.add(name)) {
                  properties.add(property);
               }
            }
         }
         if(methods.length > 0) {
            List<Property> indexed = index(type, methods);
            
            for(Property property : indexed) {
               String name = property.getName();
               
               if(done.add(name)) {
                  properties.add(property);
               }
            }
         }
         if(types.length > 0) {
            List<Property> indexed = index(type, types);
            
            for(Property property : indexed) {
               String name = property.getName();
               
               if(done.add(name)) {
                  properties.add(property);
               }
            }
         }
      }
      return properties;
   }
   
   private List<Property> index(Type type, Class[] types) throws Exception {
      List<Property> properties = new ArrayList<Property>();
      
      for(Class entry : types) {
         int modifiers = converter.convert(entry);
         
         if(ModifierType.isPublic(modifiers) || ModifierType.isProtected(modifiers)) {
            String name = entry.getSimpleName();
            Type match = indexer.loadType(entry);
            Type constraint = indexer.loadType(Type.class);
            Property property = generator.generate(match, constraint, name, modifiers | CONSTANT.mask); 
            List<Annotation> extracted = extractor.extract(entry);
            List<Annotation> actual = property.getAnnotations();
            
            properties.add(property);
            actual.addAll(extracted);
         }
      }
      return properties;
   }
   
   private List<Property> index(Type type, Field[] fields) throws Exception {
      List<Property> properties = new ArrayList<Property>();
      
      for(Field field : fields) {
         int modifiers = converter.convert(field);
         
         if(ModifierType.isPublic(modifiers) || ModifierType.isProtected(modifiers)) {
            String name = field.getName();
            Class declaration = field.getType();
            Type constraint = indexer.loadType(declaration);
            Property property = generator.generate(field, type, constraint, name, modifiers); 
            List<Annotation> extracted = extractor.extract(field);
            List<Annotation> actual = property.getAnnotations();
            
            properties.add(property);
            actual.addAll(extracted);
         }
      }
      return properties;
   }

   private List<Property> index(Type type, Method[] methods) throws Exception {
      List<Property> properties = new ArrayList<Property>();
      
      for(Method method : methods){
         int modifiers = converter.convert(method);
         
         if(ModifierType.isPublic(modifiers) && !ModifierType.isStatic(modifiers)) {
            Class[] parameters = method.getParameterTypes();
            Class returns = method.getReturnType();
            
            if(parameters.length == 0 && returns != void.class) {
               String name = PropertyNameExtractor.getProperty(method);
               Class declaration = method.getReturnType();
               Method write = matcher.match(methods, declaration, name);
               
               if(write == null) {
                  modifiers |= CONSTANT.mask;
               }
               Class normal = promoter.promote(declaration);
               Type constraint = indexer.loadType(normal);
               Property property = generator.generate(method, write, type, constraint, name, modifiers);                
               List<Annotation> extracted = extractor.extract(method);
               List<Annotation> actual = property.getAnnotations();
               
               if(write != null){
                  write.setAccessible(true);
               }
               method.setAccessible(true);
               properties.add(property);
               actual.addAll(extracted);
            }
         }
      }
      return properties;
   }

}