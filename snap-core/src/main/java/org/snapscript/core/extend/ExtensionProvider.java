package org.snapscript.core.extend;

import org.snapscript.core.Bug;
import org.snapscript.core.Type;
import org.snapscript.core.TypeCache;
import org.snapscript.core.TypeExtractor;
import org.snapscript.core.bind.FunctionResolver;
import org.snapscript.core.bind.ObjectFunctionMatcher;
import org.snapscript.core.stack.ThreadStack;

@Bug("this is a total mess - we need to determine the platform type")
public class ExtensionProvider {
   
   //private volatile TypeExtender extender;
   private final TypeCache<TypeExtender> cache;
   private final FunctionResolver resolver;
   
   public ExtensionProvider(TypeExtractor extractor, ThreadStack stack) {
      this.resolver = new ObjectFunctionMatcher(extractor, stack);
      this.cache = new TypeCache<TypeExtender>();
   }

   public TypeExtender create(Type type) {
      if(type != null) {
         TypeExtender extender = cache.fetch(type);
         
        if(extender == null) {
            boolean android = false;
            try {
               Class.forName("android.os.Build"); // check if this is android
               android = true;
            }catch(Exception e){}
            try {
               if(android) {
                  extender = (TypeExtender)Class.forName("org.snapscript.extend.android.AndroidExtender").getDeclaredConstructor(FunctionResolver.class, Type.class).newInstance(resolver, type);
               } else {
                  extender = (TypeExtender)Class.forName("org.snapscript.extend.normal.NormalExtender").getDeclaredConstructor(FunctionResolver.class, Type.class).newInstance(resolver, type);
               }
               cache.cache(type, extender);
            }catch(Exception e) {
               throw new IllegalStateException("Could not extend " + type, e);
            }
         }
         return extender;
      }
      return null;
   }
}
