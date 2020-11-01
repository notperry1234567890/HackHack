package org.spongepowered.tools.obfuscation;

import com.google.common.io.Files;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.TypeElement;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import org.spongepowered.tools.obfuscation.mirror.TypeReference;

public final class TargetMap extends HashMap<TypeReference, Set<TypeReference>> {
  private static final long serialVersionUID = 1L;
  
  private final String sessionId;
  
  private TargetMap() {
    this(String.valueOf(System.currentTimeMillis()));
  }
  
  private TargetMap(String sessionId) {
    this.sessionId = sessionId;
  }
  
  public String getSessionId() {
    return this.sessionId;
  }
  
  public void registerTargets(AnnotatedMixin mixin) {
    registerTargets(mixin.getTargets(), mixin.getHandle());
  }
  
  public void registerTargets(List<TypeHandle> targets, TypeHandle mixin) {
    for (TypeHandle target : targets)
      addMixin(target, mixin); 
  }
  
  public void addMixin(TypeHandle target, TypeHandle mixin) {
    addMixin(target.getReference(), mixin.getReference());
  }
  
  public void addMixin(String target, String mixin) {
    addMixin(new TypeReference(target), new TypeReference(mixin));
  }
  
  public void addMixin(TypeReference target, TypeReference mixin) {
    Set<TypeReference> mixins = getMixinsFor(target);
    mixins.add(mixin);
  }
  
  public Collection<TypeReference> getMixinsTargeting(TypeElement target) {
    return getMixinsTargeting(new TypeHandle(target));
  }
  
  public Collection<TypeReference> getMixinsTargeting(TypeHandle target) {
    return getMixinsTargeting(target.getReference());
  }
  
  public Collection<TypeReference> getMixinsTargeting(TypeReference target) {
    return Collections.unmodifiableCollection(getMixinsFor(target));
  }
  
  private Set<TypeReference> getMixinsFor(TypeReference target) {
    Set<TypeReference> mixins = get(target);
    if (mixins == null) {
      mixins = new HashSet<TypeReference>();
      put(target, mixins);
    } 
    return mixins;
  }
  
  public void readImports(File file) throws IOException {
    if (!file.isFile())
      return; 
    for (String line : Files.readLines(file, Charset.defaultCharset())) {
      String[] parts = line.split("\t");
      if (parts.length == 2)
        addMixin(parts[1], parts[0]); 
    } 
  }
  
  public void write(boolean temp) {
    ObjectOutputStream oos = null;
    FileOutputStream fout = null;
    try {
      File sessionFile = getSessionFile(this.sessionId);
      if (temp)
        sessionFile.deleteOnExit(); 
      fout = new FileOutputStream(sessionFile, true);
      oos = new ObjectOutputStream(fout);
      oos.writeObject(this);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      if (oos != null)
        try {
          oos.close();
        } catch (IOException ex) {
          ex.printStackTrace();
        }  
    } 
  }
  
  private static TargetMap read(File sessionFile) {
    ObjectInputStream objectinputstream = null;
    FileInputStream streamIn = null;
    try {
      streamIn = new FileInputStream(sessionFile);
      objectinputstream = new ObjectInputStream(streamIn);
      return (TargetMap)objectinputstream.readObject();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (objectinputstream != null)
        try {
          objectinputstream.close();
        } catch (IOException ex) {
          ex.printStackTrace();
        }  
    } 
    return null;
  }
  
  public static TargetMap create(String sessionId) {
    if (sessionId != null) {
      File sessionFile = getSessionFile(sessionId);
      if (sessionFile.exists()) {
        TargetMap map = read(sessionFile);
        if (map != null)
          return map; 
      } 
    } 
    return new TargetMap();
  }
  
  private static File getSessionFile(String sessionId) {
    File tempDir = new File(System.getProperty("java.io.tmpdir"));
    return new File(tempDir, String.format("mixin-targetdb-%s.tmp", new Object[] { sessionId }));
  }
}
