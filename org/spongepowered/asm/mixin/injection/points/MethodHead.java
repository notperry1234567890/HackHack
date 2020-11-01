package org.spongepowered.asm.mixin.injection.points;

import java.util.Collection;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.spongepowered.asm.mixin.injection.InjectionPoint.AtCode;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;

@AtCode("HEAD")
public class MethodHead extends InjectionPoint {
  public MethodHead(InjectionPointData data) {
    super(data);
  }
  
  public boolean find(String desc, InsnList insns, Collection<AbstractInsnNode> nodes) {
    nodes.add(insns.getFirst());
    return true;
  }
}
