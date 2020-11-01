//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.combat;

import com.gamesense.api.settings.Setting;
import com.gamesense.client.command.Command;
import com.gamesense.client.module.Module;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class HoleFill extends Module {
  private ArrayList<BlockPos> holes;
  
  private final List<Block> obbyonly;
  
  private final List<Block> bothonly;
  
  private final List<Block> echestonly;
  
  private final List<Block> webonly;
  
  private List<Block> list;
  
  Setting.Double range;
  
  Setting.Integer yRange;
  
  Setting.Integer waitTick;
  
  Setting.Boolean chat;
  
  Setting.Boolean rotate;
  
  Setting.Mode type;
  
  BlockPos pos;
  
  private int waitCounter;
  
  public HoleFill() {
    super("HoleFill", Module.Category.Combat);
    this.holes = new ArrayList<>();
    this.obbyonly = Arrays.asList(new Block[] { Blocks.OBSIDIAN });
    this.bothonly = Arrays.asList(new Block[] { Blocks.OBSIDIAN, Blocks.ENDER_CHEST });
    this.echestonly = Arrays.asList(new Block[] { Blocks.ENDER_CHEST });
    this.webonly = Arrays.asList(new Block[] { Blocks.WEB });
    this.list = Arrays.asList(new Block[0]);
  }
  
  public void setup() {
    ArrayList<String> blockmode = new ArrayList<>();
    blockmode.add("Obby");
    blockmode.add("EChest");
    blockmode.add("Both");
    blockmode.add("Web");
    this.type = registerMode("Block", "BlockMode", blockmode, "Obby");
    this.range = registerDouble("Place Range", "PlaceRange", 5.0D, 0.0D, 10.0D);
    this.yRange = registerInteger("Y Range", "YRange", 2, 0, 10);
    this.waitTick = registerInteger("Tick Delay", "TickDelay", 1, 0, 20);
    this.rotate = registerBoolean("Rotate", "Rotate", false);
    this.chat = registerBoolean("Toggle Msg", "ToggleMsg", false);
  }
  
  public void onUpdate() {
    // Byte code:
    //   0: aload_0
    //   1: new java/util/ArrayList
    //   4: dup
    //   5: invokespecial <init> : ()V
    //   8: putfield holes : Ljava/util/ArrayList;
    //   11: aload_0
    //   12: getfield type : Lcom/gamesense/api/settings/Setting$Mode;
    //   15: invokevirtual getValue : ()Ljava/lang/String;
    //   18: ldc 'Obby'
    //   20: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   23: ifeq -> 34
    //   26: aload_0
    //   27: aload_0
    //   28: getfield obbyonly : Ljava/util/List;
    //   31: putfield list : Ljava/util/List;
    //   34: aload_0
    //   35: getfield type : Lcom/gamesense/api/settings/Setting$Mode;
    //   38: invokevirtual getValue : ()Ljava/lang/String;
    //   41: ldc 'EChest'
    //   43: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   46: ifeq -> 57
    //   49: aload_0
    //   50: aload_0
    //   51: getfield echestonly : Ljava/util/List;
    //   54: putfield list : Ljava/util/List;
    //   57: aload_0
    //   58: getfield type : Lcom/gamesense/api/settings/Setting$Mode;
    //   61: invokevirtual getValue : ()Ljava/lang/String;
    //   64: ldc 'Both'
    //   66: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   69: ifeq -> 80
    //   72: aload_0
    //   73: aload_0
    //   74: getfield bothonly : Ljava/util/List;
    //   77: putfield list : Ljava/util/List;
    //   80: aload_0
    //   81: getfield type : Lcom/gamesense/api/settings/Setting$Mode;
    //   84: invokevirtual getValue : ()Ljava/lang/String;
    //   87: ldc 'Web'
    //   89: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   92: ifeq -> 103
    //   95: aload_0
    //   96: aload_0
    //   97: getfield webonly : Ljava/util/List;
    //   100: putfield list : Ljava/util/List;
    //   103: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   106: getfield player : Lnet/minecraft/client/entity/EntityPlayerSP;
    //   109: invokevirtual getPosition : ()Lnet/minecraft/util/math/BlockPos;
    //   112: aload_0
    //   113: getfield range : Lcom/gamesense/api/settings/Setting$Double;
    //   116: invokevirtual getValue : ()D
    //   119: dneg
    //   120: aload_0
    //   121: getfield yRange : Lcom/gamesense/api/settings/Setting$Integer;
    //   124: invokevirtual getValue : ()I
    //   127: ineg
    //   128: i2d
    //   129: aload_0
    //   130: getfield range : Lcom/gamesense/api/settings/Setting$Double;
    //   133: invokevirtual getValue : ()D
    //   136: dneg
    //   137: invokevirtual add : (DDD)Lnet/minecraft/util/math/BlockPos;
    //   140: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   143: getfield player : Lnet/minecraft/client/entity/EntityPlayerSP;
    //   146: invokevirtual getPosition : ()Lnet/minecraft/util/math/BlockPos;
    //   149: aload_0
    //   150: getfield range : Lcom/gamesense/api/settings/Setting$Double;
    //   153: invokevirtual getValue : ()D
    //   156: aload_0
    //   157: getfield yRange : Lcom/gamesense/api/settings/Setting$Integer;
    //   160: invokevirtual getValue : ()I
    //   163: i2d
    //   164: aload_0
    //   165: getfield range : Lcom/gamesense/api/settings/Setting$Double;
    //   168: invokevirtual getValue : ()D
    //   171: invokevirtual add : (DDD)Lnet/minecraft/util/math/BlockPos;
    //   174: invokestatic getAllInBox : (Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;)Ljava/lang/Iterable;
    //   177: astore_1
    //   178: aload_1
    //   179: invokeinterface iterator : ()Ljava/util/Iterator;
    //   184: astore_2
    //   185: aload_2
    //   186: invokeinterface hasNext : ()Z
    //   191: ifeq -> 629
    //   194: aload_2
    //   195: invokeinterface next : ()Ljava/lang/Object;
    //   200: checkcast net/minecraft/util/math/BlockPos
    //   203: astore_3
    //   204: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   207: getfield world : Lnet/minecraft/client/multiplayer/WorldClient;
    //   210: aload_3
    //   211: invokevirtual getBlockState : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;
    //   214: invokeinterface getMaterial : ()Lnet/minecraft/block/material/Material;
    //   219: invokevirtual blocksMovement : ()Z
    //   222: ifne -> 626
    //   225: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   228: getfield world : Lnet/minecraft/client/multiplayer/WorldClient;
    //   231: aload_3
    //   232: iconst_0
    //   233: iconst_1
    //   234: iconst_0
    //   235: invokevirtual add : (III)Lnet/minecraft/util/math/BlockPos;
    //   238: invokevirtual getBlockState : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;
    //   241: invokeinterface getMaterial : ()Lnet/minecraft/block/material/Material;
    //   246: invokevirtual blocksMovement : ()Z
    //   249: ifne -> 626
    //   252: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   255: getfield world : Lnet/minecraft/client/multiplayer/WorldClient;
    //   258: aload_3
    //   259: iconst_1
    //   260: iconst_0
    //   261: iconst_0
    //   262: invokevirtual add : (III)Lnet/minecraft/util/math/BlockPos;
    //   265: invokevirtual getBlockState : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;
    //   268: invokeinterface getBlock : ()Lnet/minecraft/block/Block;
    //   273: getstatic net/minecraft/init/Blocks.BEDROCK : Lnet/minecraft/block/Block;
    //   276: if_acmpne -> 283
    //   279: iconst_1
    //   280: goto -> 284
    //   283: iconst_0
    //   284: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   287: getfield world : Lnet/minecraft/client/multiplayer/WorldClient;
    //   290: aload_3
    //   291: iconst_1
    //   292: iconst_0
    //   293: iconst_0
    //   294: invokevirtual add : (III)Lnet/minecraft/util/math/BlockPos;
    //   297: invokevirtual getBlockState : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;
    //   300: invokeinterface getBlock : ()Lnet/minecraft/block/Block;
    //   305: getstatic net/minecraft/init/Blocks.OBSIDIAN : Lnet/minecraft/block/Block;
    //   308: if_acmpne -> 315
    //   311: iconst_1
    //   312: goto -> 316
    //   315: iconst_0
    //   316: ior
    //   317: ifeq -> 609
    //   320: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   323: getfield world : Lnet/minecraft/client/multiplayer/WorldClient;
    //   326: aload_3
    //   327: iconst_0
    //   328: iconst_0
    //   329: iconst_1
    //   330: invokevirtual add : (III)Lnet/minecraft/util/math/BlockPos;
    //   333: invokevirtual getBlockState : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;
    //   336: invokeinterface getBlock : ()Lnet/minecraft/block/Block;
    //   341: getstatic net/minecraft/init/Blocks.BEDROCK : Lnet/minecraft/block/Block;
    //   344: if_acmpne -> 351
    //   347: iconst_1
    //   348: goto -> 352
    //   351: iconst_0
    //   352: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   355: getfield world : Lnet/minecraft/client/multiplayer/WorldClient;
    //   358: aload_3
    //   359: iconst_0
    //   360: iconst_0
    //   361: iconst_1
    //   362: invokevirtual add : (III)Lnet/minecraft/util/math/BlockPos;
    //   365: invokevirtual getBlockState : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;
    //   368: invokeinterface getBlock : ()Lnet/minecraft/block/Block;
    //   373: getstatic net/minecraft/init/Blocks.OBSIDIAN : Lnet/minecraft/block/Block;
    //   376: if_acmpne -> 383
    //   379: iconst_1
    //   380: goto -> 384
    //   383: iconst_0
    //   384: ior
    //   385: ifeq -> 609
    //   388: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   391: getfield world : Lnet/minecraft/client/multiplayer/WorldClient;
    //   394: aload_3
    //   395: iconst_m1
    //   396: iconst_0
    //   397: iconst_0
    //   398: invokevirtual add : (III)Lnet/minecraft/util/math/BlockPos;
    //   401: invokevirtual getBlockState : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;
    //   404: invokeinterface getBlock : ()Lnet/minecraft/block/Block;
    //   409: getstatic net/minecraft/init/Blocks.BEDROCK : Lnet/minecraft/block/Block;
    //   412: if_acmpne -> 419
    //   415: iconst_1
    //   416: goto -> 420
    //   419: iconst_0
    //   420: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   423: getfield world : Lnet/minecraft/client/multiplayer/WorldClient;
    //   426: aload_3
    //   427: iconst_m1
    //   428: iconst_0
    //   429: iconst_0
    //   430: invokevirtual add : (III)Lnet/minecraft/util/math/BlockPos;
    //   433: invokevirtual getBlockState : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;
    //   436: invokeinterface getBlock : ()Lnet/minecraft/block/Block;
    //   441: getstatic net/minecraft/init/Blocks.OBSIDIAN : Lnet/minecraft/block/Block;
    //   444: if_acmpne -> 451
    //   447: iconst_1
    //   448: goto -> 452
    //   451: iconst_0
    //   452: ior
    //   453: ifeq -> 609
    //   456: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   459: getfield world : Lnet/minecraft/client/multiplayer/WorldClient;
    //   462: aload_3
    //   463: iconst_0
    //   464: iconst_0
    //   465: iconst_m1
    //   466: invokevirtual add : (III)Lnet/minecraft/util/math/BlockPos;
    //   469: invokevirtual getBlockState : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;
    //   472: invokeinterface getBlock : ()Lnet/minecraft/block/Block;
    //   477: getstatic net/minecraft/init/Blocks.BEDROCK : Lnet/minecraft/block/Block;
    //   480: if_acmpne -> 487
    //   483: iconst_1
    //   484: goto -> 488
    //   487: iconst_0
    //   488: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   491: getfield world : Lnet/minecraft/client/multiplayer/WorldClient;
    //   494: aload_3
    //   495: iconst_0
    //   496: iconst_0
    //   497: iconst_m1
    //   498: invokevirtual add : (III)Lnet/minecraft/util/math/BlockPos;
    //   501: invokevirtual getBlockState : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;
    //   504: invokeinterface getBlock : ()Lnet/minecraft/block/Block;
    //   509: getstatic net/minecraft/init/Blocks.OBSIDIAN : Lnet/minecraft/block/Block;
    //   512: if_acmpne -> 519
    //   515: iconst_1
    //   516: goto -> 520
    //   519: iconst_0
    //   520: ior
    //   521: ifeq -> 609
    //   524: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   527: getfield world : Lnet/minecraft/client/multiplayer/WorldClient;
    //   530: aload_3
    //   531: iconst_0
    //   532: iconst_0
    //   533: iconst_0
    //   534: invokevirtual add : (III)Lnet/minecraft/util/math/BlockPos;
    //   537: invokevirtual getBlockState : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;
    //   540: invokeinterface getMaterial : ()Lnet/minecraft/block/material/Material;
    //   545: getstatic net/minecraft/block/material/Material.AIR : Lnet/minecraft/block/material/Material;
    //   548: if_acmpne -> 609
    //   551: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   554: getfield world : Lnet/minecraft/client/multiplayer/WorldClient;
    //   557: aload_3
    //   558: iconst_0
    //   559: iconst_1
    //   560: iconst_0
    //   561: invokevirtual add : (III)Lnet/minecraft/util/math/BlockPos;
    //   564: invokevirtual getBlockState : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;
    //   567: invokeinterface getMaterial : ()Lnet/minecraft/block/material/Material;
    //   572: getstatic net/minecraft/block/material/Material.AIR : Lnet/minecraft/block/material/Material;
    //   575: if_acmpne -> 609
    //   578: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   581: getfield world : Lnet/minecraft/client/multiplayer/WorldClient;
    //   584: aload_3
    //   585: iconst_0
    //   586: iconst_2
    //   587: iconst_0
    //   588: invokevirtual add : (III)Lnet/minecraft/util/math/BlockPos;
    //   591: invokevirtual getBlockState : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;
    //   594: invokeinterface getMaterial : ()Lnet/minecraft/block/material/Material;
    //   599: getstatic net/minecraft/block/material/Material.AIR : Lnet/minecraft/block/material/Material;
    //   602: if_acmpne -> 609
    //   605: iconst_1
    //   606: goto -> 610
    //   609: iconst_0
    //   610: istore #4
    //   612: iload #4
    //   614: ifeq -> 626
    //   617: aload_0
    //   618: getfield holes : Ljava/util/ArrayList;
    //   621: aload_3
    //   622: invokevirtual add : (Ljava/lang/Object;)Z
    //   625: pop
    //   626: goto -> 185
    //   629: iconst_m1
    //   630: istore_2
    //   631: iconst_0
    //   632: istore_3
    //   633: iload_3
    //   634: bipush #9
    //   636: if_icmpge -> 717
    //   639: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   642: getfield player : Lnet/minecraft/client/entity/EntityPlayerSP;
    //   645: getfield inventory : Lnet/minecraft/entity/player/InventoryPlayer;
    //   648: iload_3
    //   649: invokevirtual getStackInSlot : (I)Lnet/minecraft/item/ItemStack;
    //   652: astore #4
    //   654: aload #4
    //   656: getstatic net/minecraft/item/ItemStack.EMPTY : Lnet/minecraft/item/ItemStack;
    //   659: if_acmpeq -> 711
    //   662: aload #4
    //   664: invokevirtual getItem : ()Lnet/minecraft/item/Item;
    //   667: instanceof net/minecraft/item/ItemBlock
    //   670: ifne -> 676
    //   673: goto -> 711
    //   676: aload #4
    //   678: invokevirtual getItem : ()Lnet/minecraft/item/Item;
    //   681: checkcast net/minecraft/item/ItemBlock
    //   684: invokevirtual getBlock : ()Lnet/minecraft/block/Block;
    //   687: astore #5
    //   689: aload_0
    //   690: getfield list : Ljava/util/List;
    //   693: aload #5
    //   695: invokeinterface contains : (Ljava/lang/Object;)Z
    //   700: ifne -> 706
    //   703: goto -> 711
    //   706: iload_3
    //   707: istore_2
    //   708: goto -> 717
    //   711: iinc #3, 1
    //   714: goto -> 633
    //   717: iload_2
    //   718: iconst_m1
    //   719: if_icmpne -> 723
    //   722: return
    //   723: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   726: getfield player : Lnet/minecraft/client/entity/EntityPlayerSP;
    //   729: getfield inventory : Lnet/minecraft/entity/player/InventoryPlayer;
    //   732: getfield currentItem : I
    //   735: istore_3
    //   736: aload_0
    //   737: getfield waitTick : Lcom/gamesense/api/settings/Setting$Integer;
    //   740: invokevirtual getValue : ()I
    //   743: ifle -> 805
    //   746: aload_0
    //   747: getfield waitCounter : I
    //   750: aload_0
    //   751: getfield waitTick : Lcom/gamesense/api/settings/Setting$Integer;
    //   754: invokevirtual getValue : ()I
    //   757: if_icmpge -> 800
    //   760: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   763: getfield player : Lnet/minecraft/client/entity/EntityPlayerSP;
    //   766: getfield inventory : Lnet/minecraft/entity/player/InventoryPlayer;
    //   769: iload_2
    //   770: putfield currentItem : I
    //   773: aload_0
    //   774: getfield holes : Ljava/util/ArrayList;
    //   777: aload_0
    //   778: <illegal opcode> accept : (Lcom/gamesense/client/module/modules/combat/HoleFill;)Ljava/util/function/Consumer;
    //   783: invokevirtual forEach : (Ljava/util/function/Consumer;)V
    //   786: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   789: getfield player : Lnet/minecraft/client/entity/EntityPlayerSP;
    //   792: getfield inventory : Lnet/minecraft/entity/player/InventoryPlayer;
    //   795: iload_3
    //   796: putfield currentItem : I
    //   799: return
    //   800: aload_0
    //   801: iconst_0
    //   802: putfield waitCounter : I
    //   805: return
    // Line number table:
    //   Java source line number -> byte code offset
    //   #72	-> 0
    //   #73	-> 11
    //   #74	-> 26
    //   #76	-> 34
    //   #77	-> 49
    //   #79	-> 57
    //   #80	-> 72
    //   #82	-> 80
    //   #83	-> 95
    //   #85	-> 103
    //   #86	-> 178
    //   #87	-> 204
    //   #88	-> 252
    //   #89	-> 262
    //   #90	-> 330
    //   #91	-> 398
    //   #92	-> 466
    //   #93	-> 534
    //   #94	-> 561
    //   #95	-> 588
    //   #96	-> 612
    //   #97	-> 617
    //   #100	-> 626
    //   #103	-> 629
    //   #104	-> 631
    //   #106	-> 639
    //   #107	-> 649
    //   #109	-> 654
    //   #110	-> 673
    //   #113	-> 676
    //   #114	-> 689
    //   #115	-> 703
    //   #118	-> 706
    //   #119	-> 708
    //   #104	-> 711
    //   #123	-> 717
    //   #124	-> 722
    //   #127	-> 723
    //   #130	-> 736
    //   #131	-> 746
    //   #133	-> 760
    //   #134	-> 773
    //   #135	-> 786
    //   #136	-> 799
    //   #138	-> 800
    //   #141	-> 805
    // Local variable table:
    //   start	length	slot	name	descriptor
    //   612	14	4	solidNeighbours	Z
    //   204	422	3	pos	Lnet/minecraft/util/math/BlockPos;
    //   654	57	4	stack	Lnet/minecraft/item/ItemStack;
    //   689	22	5	block	Lnet/minecraft/block/Block;
    //   633	84	3	i	I
    //   0	806	0	this	Lcom/gamesense/client/module/modules/combat/HoleFill;
    //   178	628	1	blocks	Ljava/lang/Iterable;
    //   631	175	2	newSlot	I
    //   736	70	3	oldSlot	I
    // Local variable type table:
    //   start	length	slot	name	signature
    //   178	628	1	blocks	Ljava/lang/Iterable<Lnet/minecraft/util/math/BlockPos;>;
  }
  
  public void onEnable() {
    if (mc.player != null && this.chat.getValue())
      Command.sendRawMessage("§aHolefill turned ON!"); 
  }
  
  public void onDisable() {
    if (mc.player != null && this.chat.getValue())
      Command.sendRawMessage("§cHolefill turned OFF!"); 
  }
  
  private void place(BlockPos blockPos) {
    for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(blockPos))) {
      if (entity instanceof net.minecraft.entity.EntityLivingBase)
        return; 
    } 
    placeBlockScaffold(blockPos, this.rotate.getValue());
    this.waitCounter++;
  }
  
  public static boolean placeBlockScaffold(BlockPos pos, boolean rotate) {
    Vec3d eyesPos = new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
    EnumFacing[] arrayOfEnumFacing;
    int i;
    byte b;
    for (arrayOfEnumFacing = EnumFacing.values(), i = arrayOfEnumFacing.length, b = 0; b < i; ) {
      EnumFacing side = arrayOfEnumFacing[b];
      BlockPos neighbor = pos.offset(side);
      EnumFacing side2 = side.getOpposite();
      if (!canBeClicked(neighbor)) {
        b++;
        continue;
      } 
      Vec3d hitVec = (new Vec3d((Vec3i)neighbor)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(side2.getDirectionVec())).scale(0.5D));
      if (rotate)
        faceVectorPacketInstant(hitVec); 
      mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.START_SNEAKING));
      processRightClickBlock(neighbor, side2, hitVec);
      mc.player.swingArm(EnumHand.MAIN_HAND);
      mc.rightClickDelayTimer = 0;
      mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
      return true;
    } 
    return false;
  }
  
  public static boolean canBeClicked(BlockPos pos) {
    return getBlock(pos).canCollideCheck(getState(pos), false);
  }
  
  public static IBlockState getState(BlockPos pos) {
    return mc.world.getBlockState(pos);
  }
  
  public static Block getBlock(BlockPos pos) {
    return getState(pos).getBlock();
  }
  
  public static void faceVectorPacketInstant(Vec3d vec) {
    float[] rotations = getNeededRotations2(vec);
    mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(rotations[0], rotations[1], mc.player.onGround));
  }
  
  private static float[] getNeededRotations2(Vec3d vec) {
    Vec3d eyesPos = getEyesPos();
    double diffX = vec.x - eyesPos.x;
    double diffY = vec.y - eyesPos.y;
    double diffZ = vec.z - eyesPos.z;
    double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
    float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
    float pitch = (float)-Math.toDegrees(Math.atan2(diffY, diffXZ));
    return new float[] { mc.player.rotationYaw + 
        
        MathHelper.wrapDegrees(yaw - mc.player.rotationYaw), mc.player.rotationPitch + 
        
        MathHelper.wrapDegrees(pitch - mc.player.rotationPitch) };
  }
  
  public static Vec3d getEyesPos() {
    return new Vec3d(mc.player.posX, mc.player.posY + mc.player
        .getEyeHeight(), mc.player.posZ);
  }
  
  public static void processRightClickBlock(BlockPos pos, EnumFacing side, Vec3d hitVec) {
    getPlayerController().processRightClickBlock(mc.player, mc.world, pos, side, hitVec, EnumHand.MAIN_HAND);
  }
  
  private static PlayerControllerMP getPlayerController() {
    return mc.playerController;
  }
}
