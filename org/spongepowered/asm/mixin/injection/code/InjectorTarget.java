package org.spongepowered.asm.mixin.injection.code;

import java.util.HashMap;
import java.util.Map;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.spongepowered.asm.mixin.injection.struct.Target;

public class InjectorTarget {
  private final ISliceContext context;
  
  private final Map<String, ReadOnlyInsnList> cache = new HashMap<String, ReadOnlyInsnList>();
  
  private final Target target;
  
  public InjectorTarget(ISliceContext context, Target target) {
    this.context = context;
    this.target = target;
  }
  
  public Target getTarget() {
    return this.target;
  }
  
  public MethodNode getMethod() {
    return this.target.method;
  }
  
  public InsnList getSlice(String id) {
    ReadOnlyInsnList slice = this.cache.get(id);
    if (slice == null) {
      MethodSlice sliceInfo = this.context.getSlice(id);
      if (sliceInfo != null) {
        slice = sliceInfo.getSlice(this.target.method);
      } else {
        slice = new ReadOnlyInsnList(this.target.method.instructions);
      } 
      this.cache.put(id, slice);
    } 
    return slice;
  }
  
  public InsnList getSlice(InjectionPoint injectionPoint) {
    return getSlice(injectionPoint.getSlice());
  }
  
  public void dispose() {
    for (ReadOnlyInsnList insns : this.cache.values())
      insns.dispose(); 
    this.cache.clear();
  }
}
