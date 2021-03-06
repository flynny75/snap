package org.snapscript.core.convert;

import static org.snapscript.core.convert.Score.EXACT;
import static org.snapscript.core.convert.Score.INVALID;
import static org.snapscript.core.convert.Score.POSSIBLE;
import static org.snapscript.core.convert.Score.SIMILAR;

import org.snapscript.core.Type;

public class EnumConverter extends ConstraintConverter {
   
   private final Type type;
   
   public EnumConverter(Type type) {
      this.type = type;
   }
   
   @Override
   public Score score(Type actual) throws Exception {
      if(actual != null) {
         Class real = actual.getType();
         Class require = type.getType();
         
         if(real != require) {
            if(require.isAssignableFrom(real)) {
               return EXACT;
            }
            if(real == String.class) {
               return SIMILAR;
            }
            return INVALID;
         }
         return EXACT;
      }
      return POSSIBLE;
   }

   @Override
   public Score score(Object value) throws Exception {
      if(value != null) {
         Class real = value.getClass();
         Class require = type.getType();
         
         if(require.isAssignableFrom(real)) {
            return EXACT;
         }
         if(real == String.class) {
            return SIMILAR;
         }
         return INVALID;
      }
      return POSSIBLE;
   }
   
   @Override
   public Object convert(Object value) throws Exception {
      if(value != null) {
         Class require = type.getType();
         
         if(require.isInstance(value)) {
            return value;
         }
         String text = String.valueOf(value);
         
         return Enum.valueOf(require, text);
      }
      return null;
   }
}