package org.spongepowered.asm.mixin.injection.invoke;

import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.lib.tree.InsnNode;
import org.spongepowered.asm.lib.tree.JumpInsnNode;
import org.spongepowered.asm.lib.tree.VarInsnNode;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.spongepowered.asm.util.Bytecode;

public class ModifyConstantInjector extends RedirectInjector {
  private static final int OPCODE_OFFSET = 6;
  
  public ModifyConstantInjector(InjectionInfo info) {
    super(info, "@ModifyConstant");
  }
  
  protected void inject(Target target, InjectionNodes.InjectionNode node) {
    if (!preInject(node))
      return; 
    if (node.isReplaced())
      throw new UnsupportedOperationException("Target failure for " + this.info); 
    AbstractInsnNode targetNode = node.getCurrentTarget();
    if (targetNode instanceof JumpInsnNode) {
      checkTargetModifiers(target, false);
      injectExpandedConstantModifier(target, (JumpInsnNode)targetNode);
      return;
    } 
    if (Bytecode.isConstant(targetNode)) {
      checkTargetModifiers(target, false);
      injectConstantModifier(target, targetNode);
      return;
    } 
    throw new InvalidInjectionException(this.info, this.annotationType + " annotation is targetting an invalid insn in " + target + " in " + this);
  }
  
  private void injectExpandedConstantModifier(Target target, JumpInsnNode jumpNode) {
    int opcode = jumpNode.getOpcode();
    if (opcode < 155 || opcode > 158)
      throw new InvalidInjectionException(this.info, this.annotationType + " annotation selected an invalid opcode " + 
          Bytecode.getOpcodeName(opcode) + " in " + target + " in " + this); 
    InsnList insns = new InsnList();
    insns.add((AbstractInsnNode)new InsnNode(3));
    AbstractInsnNode invoke = invokeConstantHandler(Type.getType("I"), target, insns, insns);
    insns.add((AbstractInsnNode)new JumpInsnNode(opcode + 6, jumpNode.label));
    target.replaceNode((AbstractInsnNode)jumpNode, invoke, insns);
    target.addToStack(1);
  }
  
  private void injectConstantModifier(Target target, AbstractInsnNode constNode) {
    Type constantType = Bytecode.getConstantType(constNode);
    InsnList before = new InsnList();
    InsnList after = new InsnList();
    AbstractInsnNode invoke = invokeConstantHandler(constantType, target, before, after);
    target.wrapNode(constNode, invoke, before, after);
  }
  
  private AbstractInsnNode invokeConstantHandler(Type constantType, Target target, InsnList before, InsnList after) {
    String handlerDesc = Bytecode.generateDescriptor(constantType, new Object[] { constantType });
    boolean withArgs = checkDescriptor(handlerDesc, target, "getter");
    if (!this.isStatic) {
      before.insert((AbstractInsnNode)new VarInsnNode(25, 0));
      target.addToStack(1);
    } 
    if (withArgs) {
      pushArgs(target.arguments, after, target.getArgIndices(), 0, target.arguments.length);
      target.addToStack(Bytecode.getArgsSize(target.arguments));
    } 
    return invokeHandler(after);
  }
}
