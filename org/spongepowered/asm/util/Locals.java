package org.spongepowered.asm.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.lib.tree.FrameNode;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.lib.tree.LabelNode;
import org.spongepowered.asm.lib.tree.LocalVariableNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.lib.tree.VarInsnNode;
import org.spongepowered.asm.lib.tree.analysis.Analyzer;
import org.spongepowered.asm.lib.tree.analysis.AnalyzerException;
import org.spongepowered.asm.lib.tree.analysis.BasicValue;
import org.spongepowered.asm.lib.tree.analysis.Frame;
import org.spongepowered.asm.lib.tree.analysis.Interpreter;
import org.spongepowered.asm.mixin.transformer.ClassInfo;
import org.spongepowered.asm.util.asm.MixinVerifier;
import org.spongepowered.asm.util.throwables.LVTGeneratorException;

public final class Locals {
  private static final Map<String, List<LocalVariableNode>> calculatedLocalVariables = new HashMap<String, List<LocalVariableNode>>();
  
  public static void loadLocals(Type[] locals, InsnList insns, int pos, int limit) {
    for (; pos < locals.length && limit > 0; pos++) {
      if (locals[pos] != null) {
        insns.add((AbstractInsnNode)new VarInsnNode(locals[pos].getOpcode(21), pos));
        limit--;
      } 
    } 
  }
  
  public static LocalVariableNode[] getLocalsAt(ClassNode classNode, MethodNode method, AbstractInsnNode node) {
    for (int i = 0; i < 3 && (node instanceof LabelNode || node instanceof org.spongepowered.asm.lib.tree.LineNumberNode); i++)
      node = nextNode(method.instructions, node); 
    ClassInfo classInfo = ClassInfo.forName(classNode.name);
    if (classInfo == null)
      throw new LVTGeneratorException("Could not load class metadata for " + classNode.name + " generating LVT for " + method.name); 
    ClassInfo.Method methodInfo = classInfo.findMethod(method);
    if (methodInfo == null)
      throw new LVTGeneratorException("Could not locate method metadata for " + method.name + " generating LVT in " + classNode.name); 
    List<ClassInfo.FrameData> frames = methodInfo.getFrames();
    LocalVariableNode[] frame = new LocalVariableNode[method.maxLocals];
    int local = 0, index = 0;
    if ((method.access & 0x8) == 0)
      frame[local++] = new LocalVariableNode("this", classNode.name, null, null, null, 0); 
    for (Type argType : Type.getArgumentTypes(method.desc)) {
      frame[local] = new LocalVariableNode("arg" + index++, argType.toString(), null, null, null, local);
      local += argType.getSize();
    } 
    int initialFrameSize = local;
    int frameIndex = -1, locals = 0;
    for (Iterator<AbstractInsnNode> iter = method.instructions.iterator(); iter.hasNext(); ) {
      AbstractInsnNode insn = iter.next();
      if (insn instanceof FrameNode) {
        frameIndex++;
        FrameNode frameNode = (FrameNode)insn;
        ClassInfo.FrameData frameData = (frameIndex < frames.size()) ? frames.get(frameIndex) : null;
        locals = (frameData != null && frameData.type == 0) ? Math.min(locals, frameData.locals) : frameNode.local.size();
        for (int localPos = 0, framePos = 0; framePos < frame.length; framePos++, localPos++) {
          Object localType = (localPos < frameNode.local.size()) ? frameNode.local.get(localPos) : null;
          if (localType instanceof String) {
            frame[framePos] = getLocalVariableAt(classNode, method, node, framePos);
          } else if (localType instanceof Integer) {
            boolean isMarkerType = (localType == Opcodes.UNINITIALIZED_THIS || localType == Opcodes.NULL);
            boolean is32bitValue = (localType == Opcodes.INTEGER || localType == Opcodes.FLOAT);
            boolean is64bitValue = (localType == Opcodes.DOUBLE || localType == Opcodes.LONG);
            if (localType != Opcodes.TOP)
              if (isMarkerType) {
                frame[framePos] = null;
              } else if (is32bitValue || is64bitValue) {
                frame[framePos] = getLocalVariableAt(classNode, method, node, framePos);
                if (is64bitValue) {
                  framePos++;
                  frame[framePos] = null;
                } 
              } else {
                throw new LVTGeneratorException("Unrecognised locals opcode " + localType + " in locals array at position " + localPos + " in " + classNode.name + "." + method.name + method.desc);
              }  
          } else if (localType == null) {
            if (framePos >= initialFrameSize && framePos >= locals && locals > 0)
              frame[framePos] = null; 
          } else {
            throw new LVTGeneratorException("Invalid value " + localType + " in locals array at position " + localPos + " in " + classNode.name + "." + method.name + method.desc);
          } 
        } 
      } else if (insn instanceof VarInsnNode) {
        VarInsnNode varNode = (VarInsnNode)insn;
        frame[varNode.var] = getLocalVariableAt(classNode, method, node, varNode.var);
      } 
      if (insn == node)
        break; 
    } 
    for (int l = 0; l < frame.length; l++) {
      if (frame[l] != null && (frame[l]).desc == null)
        frame[l] = null; 
    } 
    return frame;
  }
  
  public static LocalVariableNode getLocalVariableAt(ClassNode classNode, MethodNode method, AbstractInsnNode node, int var) {
    return getLocalVariableAt(classNode, method, method.instructions.indexOf(node), var);
  }
  
  private static LocalVariableNode getLocalVariableAt(ClassNode classNode, MethodNode method, int pos, int var) {
    LocalVariableNode localVariableNode = null;
    LocalVariableNode fallbackNode = null;
    for (LocalVariableNode local : getLocalVariableTable(classNode, method)) {
      if (local.index != var)
        continue; 
      if (isOpcodeInRange(method.instructions, local, pos)) {
        localVariableNode = local;
        continue;
      } 
      if (localVariableNode == null)
        fallbackNode = local; 
    } 
    if (localVariableNode == null && !method.localVariables.isEmpty())
      for (LocalVariableNode local : getGeneratedLocalVariableTable(classNode, method)) {
        if (local.index == var && isOpcodeInRange(method.instructions, local, pos))
          localVariableNode = local; 
      }  
    return (localVariableNode != null) ? localVariableNode : fallbackNode;
  }
  
  private static boolean isOpcodeInRange(InsnList insns, LocalVariableNode local, int pos) {
    return (insns.indexOf((AbstractInsnNode)local.start) < pos && insns.indexOf((AbstractInsnNode)local.end) > pos);
  }
  
  public static List<LocalVariableNode> getLocalVariableTable(ClassNode classNode, MethodNode method) {
    if (method.localVariables.isEmpty())
      return getGeneratedLocalVariableTable(classNode, method); 
    return method.localVariables;
  }
  
  public static List<LocalVariableNode> getGeneratedLocalVariableTable(ClassNode classNode, MethodNode method) {
    String methodId = String.format("%s.%s%s", new Object[] { classNode.name, method.name, method.desc });
    List<LocalVariableNode> localVars = calculatedLocalVariables.get(methodId);
    if (localVars != null)
      return localVars; 
    localVars = generateLocalVariableTable(classNode, method);
    calculatedLocalVariables.put(methodId, localVars);
    return localVars;
  }
  
  public static List<LocalVariableNode> generateLocalVariableTable(ClassNode classNode, MethodNode method) {
    List<Type> interfaces = null;
    if (classNode.interfaces != null) {
      interfaces = new ArrayList<Type>();
      for (String iface : classNode.interfaces)
        interfaces.add(Type.getObjectType(iface)); 
    } 
    Type objectType = null;
    if (classNode.superName != null)
      objectType = Type.getObjectType(classNode.superName); 
    Analyzer<BasicValue> analyzer = new Analyzer((Interpreter)new MixinVerifier(Type.getObjectType(classNode.name), objectType, interfaces, false));
    try {
      analyzer.analyze(classNode.name, method);
    } catch (AnalyzerException ex) {
      ex.printStackTrace();
    } 
    Frame[] arrayOfFrame = analyzer.getFrames();
    int methodSize = method.instructions.size();
    List<LocalVariableNode> localVariables = new ArrayList<LocalVariableNode>();
    LocalVariableNode[] localNodes = new LocalVariableNode[method.maxLocals];
    BasicValue[] locals = new BasicValue[method.maxLocals];
    LabelNode[] labels = new LabelNode[methodSize];
    String[] lastKnownType = new String[method.maxLocals];
    for (int i = 0; i < methodSize; i++) {
      Frame<BasicValue> f = arrayOfFrame[i];
      if (f != null) {
        LabelNode labelNode = null;
        for (int j = 0; j < f.getLocals(); j++) {
          BasicValue local = (BasicValue)f.getLocal(j);
          if (local != null || locals[j] != null)
            if (local == null || !local.equals(locals[j])) {
              if (labelNode == null) {
                AbstractInsnNode existingLabel = method.instructions.get(i);
                if (existingLabel instanceof LabelNode) {
                  labelNode = (LabelNode)existingLabel;
                } else {
                  labels[i] = labelNode = new LabelNode();
                } 
              } 
              if (local == null && locals[j] != null) {
                localVariables.add(localNodes[j]);
                (localNodes[j]).end = labelNode;
                localNodes[j] = null;
              } else if (local != null) {
                if (locals[j] != null) {
                  localVariables.add(localNodes[j]);
                  (localNodes[j]).end = labelNode;
                  localNodes[j] = null;
                } 
                String desc = (local.getType() != null) ? local.getType().getDescriptor() : lastKnownType[j];
                localNodes[j] = new LocalVariableNode("var" + j, desc, null, labelNode, null, j);
                if (desc != null)
                  lastKnownType[j] = desc; 
              } 
              locals[j] = local;
            }  
        } 
      } 
    } 
    LabelNode label = null;
    for (int k = 0; k < localNodes.length; k++) {
      if (localNodes[k] != null) {
        if (label == null) {
          label = new LabelNode();
          method.instructions.add((AbstractInsnNode)label);
        } 
        (localNodes[k]).end = label;
        localVariables.add(localNodes[k]);
      } 
    } 
    for (int n = methodSize - 1; n >= 0; n--) {
      if (labels[n] != null)
        method.instructions.insert(method.instructions.get(n), (AbstractInsnNode)labels[n]); 
    } 
    return localVariables;
  }
  
  private static AbstractInsnNode nextNode(InsnList insns, AbstractInsnNode insn) {
    int index = insns.indexOf(insn) + 1;
    if (index > 0 && index < insns.size())
      return insns.get(index); 
    return insn;
  }
}
