package org.snapscript.core;

public abstract class Local extends Value {
   
   public static Local getConstant(Object value, String name) {
      return new LocalConstant(value, name, null);
   }
   
   public static Local getConstant(Object value, String name, Type type) {
      return new LocalConstant(value, name, type);
   }

   public static Local getReference(Object value, String name) {
      return new LocalReference(value, name, null);
   }
   
   public static Local getReference(Object value, String name, Type type) {
      return new LocalReference(value, name, type);
   }
   
   public abstract String getName();
}