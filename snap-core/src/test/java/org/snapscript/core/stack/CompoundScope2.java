package org.snapscript.core.stack;

import org.snapscript.core.Index;
import org.snapscript.core.MapState;
import org.snapscript.core.Model;
import org.snapscript.core.Module;
import org.snapscript.core.Scope;
import org.snapscript.core.State;
import org.snapscript.core.Type;

public class CompoundScope2 implements Scope2 {

   private final State state;
   private final Scope outer;
   private final Model model;
   
   public CompoundScope2(Model model, Scope inner, Scope outer) {
      this.state = new MapState(inner);  
      this.outer = outer;
      this.model = model;
   } 
  
   @Override
   public State2 getStack() {
      return null;//new StateScope(model, this, outer);
   }  
   
   @Override
   public State2 getState() {
      return null;
   }
   
   @Override
   public Scope getObject() {
      return outer;
   }  
   
   @Override
   public Type getHandle() {
      return outer.getType();
   }
   
   @Override
   public Type getType() {
      return outer.getType();
   }
  
   @Override
   public Module getModule() {
      return outer.getModule();
   } 

   @Override
   public Model getModel() {
      return model;
   }
   
   @Override
   public String toString() {
      return String.valueOf(state);
   }
}
