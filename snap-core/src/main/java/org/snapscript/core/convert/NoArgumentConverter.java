package org.snapscript.core.convert;

import static org.snapscript.core.convert.Score.EXACT;
import static org.snapscript.core.convert.Score.INVALID;

import org.snapscript.core.Type;
import org.snapscript.core.function.ArgumentConverter;

public class NoArgumentConverter implements ArgumentConverter {
   
   public NoArgumentConverter(){
      super();
   }
   
   @Override
   public Score score(Type... list) throws Exception {
      if(list.length == 0) {
         return EXACT;
      }
      return INVALID;
   }

   @Override
   public Score score(Object... list) throws Exception {
      if(list.length == 0) {
         return EXACT;
      }
      return INVALID;
   }
   
   @Override
   public Object[] assign(Object... list) throws Exception {
      return list;
   }

   @Override
   public Object[] convert(Object... list) throws Exception {
      return list;
   }
}