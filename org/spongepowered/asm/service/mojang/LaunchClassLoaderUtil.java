package org.spongepowered.asm.service.mojang;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import net.minecraft.launchwrapper.LaunchClassLoader;

final class LaunchClassLoaderUtil {
  private static final String CACHED_CLASSES_FIELD = "cachedClasses";
  
  private static final String INVALID_CLASSES_FIELD = "invalidClasses";
  
  private static final String CLASS_LOADER_EXCEPTIONS_FIELD = "classLoaderExceptions";
  
  private static final String TRANSFORMER_EXCEPTIONS_FIELD = "transformerExceptions";
  
  private final LaunchClassLoader classLoader;
  
  private final Map<String, Class<?>> cachedClasses;
  
  private final Set<String> invalidClasses;
  
  private final Set<String> classLoaderExceptions;
  
  private final Set<String> transformerExceptions;
  
  LaunchClassLoaderUtil(LaunchClassLoader classLoader) {
    this.classLoader = classLoader;
    this.cachedClasses = getField(classLoader, "cachedClasses");
    this.invalidClasses = getField(classLoader, "invalidClasses");
    this.classLoaderExceptions = getField(classLoader, "classLoaderExceptions");
    this.transformerExceptions = getField(classLoader, "transformerExceptions");
  }
  
  LaunchClassLoader getClassLoader() {
    return this.classLoader;
  }
  
  boolean isClassLoaded(String name) {
    return this.cachedClasses.containsKey(name);
  }
  
  boolean isClassExcluded(String name, String transformedName) {
    for (String exception : getClassLoaderExceptions()) {
      if (transformedName.startsWith(exception) || name.startsWith(exception))
        return true; 
    } 
    for (String exception : getTransformerExceptions()) {
      if (transformedName.startsWith(exception) || name.startsWith(exception))
        return true; 
    } 
    return false;
  }
  
  void registerInvalidClass(String name) {
    if (this.invalidClasses != null)
      this.invalidClasses.add(name); 
  }
  
  Set<String> getClassLoaderExceptions() {
    if (this.classLoaderExceptions != null)
      return this.classLoaderExceptions; 
    return Collections.emptySet();
  }
  
  Set<String> getTransformerExceptions() {
    if (this.transformerExceptions != null)
      return this.transformerExceptions; 
    return Collections.emptySet();
  }
  
  private static <T> T getField(LaunchClassLoader classLoader, String fieldName) {
    try {
      Field field = LaunchClassLoader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return (T)field.get(classLoader);
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    } 
  }
}
