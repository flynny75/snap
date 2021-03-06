package org.snapscript.core;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.snapscript.core.annotation.Annotation;
import org.snapscript.core.function.Function;
import org.snapscript.core.link.ImportManager;

public class EmptyModule implements Module {
   
   private final List<Function> functions;
   private final Context context;
   private final Scope scope;
   
   public EmptyModule(Context context) {
      this.functions = new ArrayList<Function>();
      this.scope = new ModelScope(null, this);
      this.context = context;
   }

   @Override
   public Scope getScope() {
      return scope;
   }

   @Override
   public Context getContext() {
      return context;
   }

   @Override
   public ImportManager getManager() {
      return null;
   }

   @Override
   public Type getType(Class type) {
      return null;
   }

   @Override
   public Type getType(String name) {
      return null;
   }

   @Override
   public Type addType(String name, Category category) {
      return null;
   }

   @Override
   public Module getModule(String module) {
      return null;
   }

   @Override
   public InputStream getResource(String path) {
      return null;
   }

   @Override
   public List<Annotation> getAnnotations() {
      return Collections.emptyList();
   }

   @Override
   public List<Function> getFunctions() {
      return functions;
   }

   @Override
   public List<Type> getTypes() {
      return Collections.emptyList();
   }

   @Override
   public String getName() {
      return null;
   }

   @Override
   public Path getPath() {
      return null;
   }

   @Override
   public int getOrder() {
      return 0;
   }

}