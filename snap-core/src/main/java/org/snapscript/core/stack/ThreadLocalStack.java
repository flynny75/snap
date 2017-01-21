package org.snapscript.core.stack;

import org.snapscript.common.ArrayStack;
import org.snapscript.common.Stack;

public class ThreadLocalStack extends ThreadLocal<Stack> {
   
   @Override
   public Stack initialValue() {
      return new ArrayStack();
   }
}