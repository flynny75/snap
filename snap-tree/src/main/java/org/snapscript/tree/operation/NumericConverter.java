package org.snapscript.tree.operation;

import org.snapscript.core.Value;
import org.snapscript.core.ValueCache;

public enum NumericConverter {
   DOUBLE {
      @Override
      public Value convert(Number reference) {
         double value = reference.doubleValue();
         return ValueCache.getDouble(value);
      }
      @Override
      public Value increment(Number reference) {
         double value = reference.doubleValue();
         return ValueCache.getDouble(value + 1.0d);
      }
      @Override
      public Value decrement(Number reference) {
         double value = reference.doubleValue();
         return ValueCache.getDouble(value - 1.0d);
      }
   },
   LONG {
      @Override
      public Value convert(Number reference) {
         long value = reference.longValue();
         return ValueCache.getLong(value);
      }
      @Override
      public Value increment(Number reference) {
         long value = reference.longValue();
         return ValueCache.getLong(value + 1L);
      }
      @Override
      public Value decrement(Number reference) {
         long value = reference.longValue();
         return ValueCache.getLong(value - 1L);
      }
   },
   FLOAT {
      @Override
      public Value convert(Number reference) {
         float value = reference.floatValue();
         return ValueCache.getFloat(value);
      }
      @Override
      public Value increment(Number reference) {
         float value = reference.floatValue();
         return ValueCache.getFloat(value + 1.0f);
      }
      @Override
      public Value decrement(Number reference) {
         float value = reference.floatValue();
         return ValueCache.getFloat(value - 1.0f);
      }
   },
   INTEGER {
      @Override
      public Value convert(Number reference) {
         int value = reference.intValue();
         return ValueCache.getInteger(value);
      }
      @Override
      public Value increment(Number reference) {
         int value = reference.intValue();
         return ValueCache.getInteger(value + 1);
      }
      @Override
      public Value decrement(Number reference) {
         int value = reference.intValue();
         return ValueCache.getInteger(value - 1);
      }
   },
   SHORT {
      @Override
      public Value convert(Number reference) {
         short value = reference.shortValue();
         return ValueCache.getShort(value);
      }
      @Override
      public Value increment(Number reference) {
         short value = reference.shortValue();
         return ValueCache.getShort(value + 1);
      }
      @Override
      public Value decrement(Number reference) {
         short value = reference.shortValue();
         return ValueCache.getShort(value - 1);
      }
   },
   BYTE {
      @Override
      public Value convert(Number reference) {
         byte value = reference.byteValue();
         return ValueCache.getByte(value);
      }
      @Override
      public Value increment(Number reference) {
         byte value = reference.byteValue();
         return ValueCache.getByte(value + 1);
      }
      @Override
      public Value decrement(Number reference) {
         byte value = reference.byteValue();
         return ValueCache.getByte(value - 1);
      }
   };
   
   public abstract Value convert(Number value);
   public abstract Value increment(Number value);
   public abstract Value decrement(Number value);
   
   public static NumericConverter resolveConverter(Number value) {
      Class type = value.getClass();
      
      if (Double.class == type) {
         return DOUBLE;
      }
      if (Long.class == type) {
         return LONG;
      }
      if (Float.class == type) {
         return FLOAT;
      }
      if (Integer.class == type) {
         return INTEGER;
      }
      if (Short.class == type) {
         return SHORT;
      }
      if (Byte.class == type) {
         return BYTE;
      }
      return DOUBLE;
   }
   
   public static NumericConverter resolveConverter(Value left, Value right) {
      Class primary = left.getType();
      Class secondary = right.getType();

      if (Double.class == primary || Double.class == secondary) {
         return DOUBLE;
      }
      if (Long.class == primary || Long.class == secondary) {
         return LONG;
      }
      if (Float.class == primary || Float.class == secondary) {
         return FLOAT;
      }
      if (Integer.class == primary || Integer.class == secondary) {
         return INTEGER;
      }
      if (Short.class == primary || Short.class == secondary) {
         return SHORT;
      }
      if (Byte.class == primary || Byte.class == secondary) {
         return BYTE;
      }
      return DOUBLE;
   }
}