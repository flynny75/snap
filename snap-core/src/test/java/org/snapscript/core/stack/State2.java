package org.snapscript.core.stack;

import org.snapscript.core.Value;

// This should be on the ThreadStack
// new ObjectInstance(state2....) ... pass the stack in to the object
//
//  Scope getInner() --> does this need to be a Scope???
//    1) if this is a stack, it can be created on the ObjectInstance only!!!... which has a reference to the stack
//
//
//
//  Scope getOuter() --> this expands the stack... if we have a State2 do we need it?
//
// interface Scope {
//
//    State2 getStack(); // this is inner.... this is controlled by the TraceStatement and TraceEvaluation....
//    State2 getObject(); // this is outer
// }
//
//
//  interface Scope {
//    State2 getInner();
//    Scope getObject() {
//        
//    }
//  }
//
//
//
interface State2 {
   State2 create();
   Address address(String name);
   Value get(Address address);
   void set(Address address, Value value);
   void add(String name, Value value);
   void clear();
}
