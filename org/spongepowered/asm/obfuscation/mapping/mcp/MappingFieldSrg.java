package org.spongepowered.asm.obfuscation.mapping.mcp;

import org.spongepowered.asm.obfuscation.mapping.common.MappingField;

public class MappingFieldSrg extends MappingField {
  private final String srg;
  
  public MappingFieldSrg(String srg) {
    super(getOwnerFromSrg(srg), getNameFromSrg(srg), null);
    this.srg = srg;
  }
  
  public MappingFieldSrg(MappingField field) {
    super(field.getOwner(), field.getName(), null);
    this.srg = field.getOwner() + "/" + field.getName();
  }
  
  public String serialise() {
    return this.srg;
  }
  
  private static String getNameFromSrg(String srg) {
    if (srg == null)
      return null; 
    int pos = srg.lastIndexOf('/');
    return (pos > -1) ? srg.substring(pos + 1) : srg;
  }
  
  private static String getOwnerFromSrg(String srg) {
    if (srg == null)
      return null; 
    int pos = srg.lastIndexOf('/');
    return (pos > -1) ? srg.substring(0, pos) : null;
  }
}
