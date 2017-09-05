package org.snapscript.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.snapscript.common.CompoundIterator;

public class LocalState implements State {
   
   private final Map<String, Integer> locals;
   private final Map<String, Value> values;
   private final List<Local> stack;
   private final Scope scope;

   public LocalState(Scope scope) {
      this(scope, null);
   }
   
   public LocalState(Scope scope, List<Local> stack) {
      this.locals = new ConcurrentHashMap<String, Integer>();
      this.values = new ConcurrentHashMap<String, Value>();
      this.stack = stack == null ?new ArrayList<Local>() :stack;
      this.scope = scope;
   }
   
   public List<Local> getStack(){
      return stack;
   }
   
   @Override
   public Iterator<String> iterator() {
      Set<String> keys = values.keySet();
      Iterator<String> inner = keys.iterator();
      
      if(scope != null) {
         State state = scope.getState();
         Iterator<String> outer = state.iterator();
         
         return new CompoundIterator<String>(inner, outer);
      }
      return inner;
   }

   @Override
   public Value getScope(String name) {
      Value value = values.get(name);
      
      if(value == null && scope != null) {
         State state = scope.getState();
         
         if(state == null) {
            throw new InternalStateException("Scope for '" + name + "' does not exist");
         }
         value = state.getScope(name);
         
         if(value != null) {
            if(!value.isProperty()) { // this does not really work
               Object object = value.getValue();
               Value constant = Value.getConstant(object);
               
               values.put(name, constant); // cache as constant
            }
         }
      }
      return value;
   }
   
   @Override
   public void addScope(String name, Value value) {
      Value variable = values.get(name);

      if(variable != null) {
         throw new InternalStateException("Variable '" + name + "' already exists");
      }
      values.put(name, value);  
   }
   
   @Bug("fix local value get")
   @Override
   public Local getLocal(int index) {
      return stack.get(index);
   }
   
   @Override
   public void addLocal(int index, Local value) {
      if(value == null) {
         throw new IllegalStateException("Local was null");
      }
      int size = stack.size();
      if(index >= size) {
         for(int i = size; i <= index; i++){
            stack.add(null);
         }
      }
      stack.set(index, value);
   }
   
   @Bug("fix local value get")
   @Override
   public int getLocal(String name) {
      Integer index = locals.get(name);
      if(index != null){
         return index;
      }
      return -1;
   }
   
   @Override
   public int addLocal(String name) {
      int index = locals.size();
      locals.put(name, index);
      return index;
   }
   
   @Override
   public Set<String> getLocals(){
      return locals.keySet();
   }
   
   @Override
   public int getDepth(){
      return locals.size();
   }
   
   @Override
   public String toString() {
      return String.valueOf(values);
   }
}