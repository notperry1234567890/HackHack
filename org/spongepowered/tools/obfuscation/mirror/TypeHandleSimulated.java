package org.spongepowered.tools.obfuscation.mirror;

import java.lang.annotation.Annotation;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.asm.util.SignaturePrinter;

public class TypeHandleSimulated extends TypeHandle {
  private final TypeElement simulatedType;
  
  public TypeHandleSimulated(String name, TypeMirror type) {
    this(TypeUtils.getPackage(type), name, type);
  }
  
  public TypeHandleSimulated(PackageElement pkg, String name, TypeMirror type) {
    super(pkg, name);
    this.simulatedType = (TypeElement)((DeclaredType)type).asElement();
  }
  
  protected TypeElement getTargetElement() {
    return this.simulatedType;
  }
  
  public boolean isPublic() {
    return true;
  }
  
  public boolean isImaginary() {
    return false;
  }
  
  public boolean isSimulated() {
    return true;
  }
  
  public AnnotationHandle getAnnotation(Class<? extends Annotation> annotationClass) {
    return null;
  }
  
  public TypeHandle getSuperclass() {
    return null;
  }
  
  public String findDescriptor(MemberInfo memberInfo) {
    return (memberInfo != null) ? memberInfo.desc : null;
  }
  
  public FieldHandle findField(String name, String type, boolean caseSensitive) {
    return new FieldHandle(null, name, type);
  }
  
  public MethodHandle findMethod(String name, String desc, boolean caseSensitive) {
    return new MethodHandle(null, name, desc);
  }
  
  public MappingMethod getMappingMethod(String name, String desc) {
    String signature = (new SignaturePrinter(name, desc)).setFullyQualified(true).toDescriptor();
    String rawSignature = TypeUtils.stripGenerics(signature);
    MethodHandle method = findMethodRecursive(this, name, signature, rawSignature, true);
    return (method != null) ? method.asMapping(true) : super.getMappingMethod(name, desc);
  }
  
  private static MethodHandle findMethodRecursive(TypeHandle target, String name, String signature, String rawSignature, boolean matchCase) {
    TypeElement elem = target.getTargetElement();
    if (elem == null)
      return null; 
    MethodHandle method = TypeHandle.findMethod(target, name, signature, rawSignature, matchCase);
    if (method != null)
      return method; 
    for (TypeMirror iface : elem.getInterfaces()) {
      method = findMethodRecursive(iface, name, signature, rawSignature, matchCase);
      if (method != null)
        return method; 
    } 
    TypeMirror superClass = elem.getSuperclass();
    if (superClass == null || superClass.getKind() == TypeKind.NONE)
      return null; 
    return findMethodRecursive(superClass, name, signature, rawSignature, matchCase);
  }
  
  private static MethodHandle findMethodRecursive(TypeMirror target, String name, String signature, String rawSignature, boolean matchCase) {
    if (!(target instanceof DeclaredType))
      return null; 
    TypeElement element = (TypeElement)((DeclaredType)target).asElement();
    return findMethodRecursive(new TypeHandle(element), name, signature, rawSignature, matchCase);
  }
}
