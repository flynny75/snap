package org.snapscript.core;

import java.util.List;

import org.snapscript.common.Progress;
import org.snapscript.core.annotation.Annotation;
import org.snapscript.core.function.Function;
import org.snapscript.core.property.Property;

public interface Type extends Any {
   Progress<Phase> getProgress();
   List<Annotation> getAnnotations();
   List<Property> getProperties();
   List<Function> getFunctions();
   List<Type> getTypes();
   Category getCategory();
   Module getModule();
   Scope getScope();
   Class getType();
   Type getOuter();
   Type getEntry();
   String getName();
   int getOrder();
}