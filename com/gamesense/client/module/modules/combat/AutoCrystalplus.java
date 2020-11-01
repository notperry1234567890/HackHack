//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.combat;

import com.gamesense.api.event.events.EntityRemovedEvent;
import com.gamesense.api.event.events.MotionUpdateEvent;
import com.gamesense.api.event.events.PacketEvent;
import com.gamesense.api.event.events.RenderEvent;
import com.gamesense.api.players.friends.Friends;
import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.EntityUtil;
import com.gamesense.api.util.MathUtil;
import com.gamesense.api.util.RenderUtil;
import com.gamesense.api.util.Timer;
import com.gamesense.api.util.WurstplusCrystalUtil;
import com.gamesense.api.util.WurstplusPair;
import com.gamesense.api.util.WurstplusPosManager;
import com.gamesense.api.util.WurstplusRotationUtil;
import com.gamesense.api.util.world.BlockUtils;
import com.gamesense.client.command.Command;
import com.gamesense.client.module.Module;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class AutoCrystalplus extends Module {
  Setting.Boolean debug;
  
  Setting.Boolean place_crystal;
  
  Setting.Boolean break_crystal;
  
  Setting.Integer break_trys;
  
  Setting.Boolean anti_weakness;
  
  Setting.Double hit_range;
  
  Setting.Double place_range;
  
  Setting.Double hit_range_wall;
  
  Setting.Integer place_delay;
  
  Setting.Integer break_delay;
  
  Setting.Double min_player_place;
  
  Setting.Double min_player_break;
  
  Setting.Double max_self_damage;
  
  Setting.Mode rotatemode;
  
  Setting.Boolean raytrace;
  
  Setting.Boolean auto_switch;
  
  Setting.Boolean anti_suicide;
  
  Setting.Boolean fast_mode;
  
  Setting.Boolean client_side;
  
  Setting.Boolean jumpy_mode;
  
  Setting.Boolean anti_stuck;
  
  Setting.Boolean endcrystal;
  
  Setting.Boolean faceplace_mode;
  
  Setting.Double faceplace_mode_damage;
  
  Setting.Boolean fuck_armor_mode;
  
  Setting.Double fuck_armor_mode_precent;
  
  Setting.Boolean stop_while_mining;
  
  Setting.Boolean faceplace_check;
  
  Setting.Boolean swing;
  
  Setting.Mode rendermode;
  
  Setting.Boolean old_render;
  
  Setting.Boolean slabRender;
  
  Setting.Integer r;
  
  Setting.Integer g;
  
  Setting.Integer b;
  
  Setting.Integer a;
  
  Setting.Integer rO;
  
  Setting.Integer gO;
  
  Setting.Integer bO;
  
  Setting.Integer outlineW;
  
  Setting.Boolean rainbow;
  
  Setting.Boolean future_render;
  
  Setting.Boolean top_block;
  
  Setting.Double sat;
  
  Setting.Double brightness;
  
  Setting.Double height;
  
  Setting.Boolean render_damage;
  
  Setting.Integer chain_length;
  
  private final ConcurrentHashMap<EntityEnderCrystal, Integer> attacked_crystals;
  
  private final Timer remove_visual_timer;
  
  private final Timer chain_timer;
  
  private EntityPlayer autoez_target;
  
  private String detail_name;
  
  private int detail_hp;
  
  private BlockPos render_block_init;
  
  private BlockPos render_block_old;
  
  private double render_damage_value;
  
  private float yaw;
  
  private float pitch;
  
  private boolean already_attacking;
  
  private boolean place_timeout_flag;
  
  private boolean is_rotating;
  
  private boolean did_anything;
  
  private boolean outline;
  
  private boolean solid;
  
  private boolean swing2;
  
  private int chain_step;
  
  private int current_chain_index;
  
  private int place_timeout;
  
  private int break_timeout;
  
  private int break_delay_counter;
  
  private int place_delay_counter;
  
  @EventHandler
  private Listener<EntityRemovedEvent> on_entity_removed;
  
  @EventHandler
  private final Listener<PacketEvent.Send> packetSendListener;
  
  @EventHandler
  private Listener<MotionUpdateEvent> on_movement;
  
  @EventHandler
  private final Listener<PacketEvent.Receive> packetReceiveListener;
  
  public AutoCrystalplus() {
    super("AutoCrystal+", Module.Category.Combat);
    this.attacked_crystals = new ConcurrentHashMap<>();
    this.remove_visual_timer = new Timer();
    this.chain_timer = new Timer();
    this.autoez_target = null;
    this.detail_name = null;
    this.detail_hp = 0;
    this.already_attacking = false;
    this.place_timeout_flag = false;
    this.swing2 = this.swing.getValue();
    this.chain_step = 0;
    this.current_chain_index = 0;
    this.on_entity_removed = new Listener(event -> {
          if (event.get_entity() instanceof EntityEnderCrystal)
            this.attacked_crystals.remove(event.get_entity()); 
        }new java.util.function.Predicate[0]);
    this.packetSendListener = new Listener(event -> {
          if (event.getPacket() instanceof CPacketPlayer && this.is_rotating && this.rotatemode.getValue().equalsIgnoreCase("Old")) {
            if (this.debug.getValue())
              Command.sendClientMessage("Rotating"); 
            CPacketPlayer p = (CPacketPlayer)event.getPacket();
            p.yaw = this.yaw;
            p.pitch = this.pitch;
            this.is_rotating = false;
          } 
          if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && this.is_rotating && this.rotatemode.getValue().equalsIgnoreCase("Old")) {
            if (this.debug.getValue())
              Command.sendClientMessage("Rotating"); 
            CPacketPlayerTryUseItemOnBlock p = (CPacketPlayerTryUseItemOnBlock)event.getPacket();
            p.facingX = this.render_block_init.getX();
            p.facingY = this.render_block_init.getY();
            p.facingZ = this.render_block_init.getZ();
            this.is_rotating = false;
          } 
        }new java.util.function.Predicate[0]);
    this.on_movement = new Listener(event -> {
          if (event.stage == 0 && (this.rotatemode.getValue().equalsIgnoreCase("Good") || this.rotatemode.getValue().equalsIgnoreCase("Const"))) {
            if (this.debug.getValue())
              Command.sendClientMessage("updating rotation"); 
            WurstplusPosManager.updatePosition();
            WurstplusRotationUtil.updateRotations();
            do_ca();
          } 
          if (event.stage == 1 && (this.rotatemode.getValue().equalsIgnoreCase("Good") || this.rotatemode.getValue().equalsIgnoreCase("Const"))) {
            if (this.debug.getValue())
              Command.sendClientMessage("resetting rotation"); 
            WurstplusPosManager.restorePosition();
            WurstplusRotationUtil.restoreRotations();
          } 
        }new java.util.function.Predicate[0]);
    this.packetReceiveListener = new Listener(event -> {
          if (event.getPacket() instanceof SPacketSoundEffect) {
            SPacketSoundEffect packet = (SPacketSoundEffect)event.getPacket();
            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE)
              for (Entity e : (Minecraft.getMinecraft()).world.loadedEntityList) {
                if (e instanceof EntityEnderCrystal && e.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0D)
                  e.setDead(); 
              }  
          } 
        }new java.util.function.Predicate[0]);
  }
  
  public void setup() {
    ArrayList<String> rotate_mode = new ArrayList<>();
    rotate_mode.add("Old");
    rotate_mode.add("Off");
    rotate_mode.add("Const");
    rotate_mode.add("Good");
    ArrayList<String> render_mode = new ArrayList<>();
    render_mode.add("Pretty");
    render_mode.add("Solid");
    render_mode.add("Outline");
    render_mode.add("None");
    this.debug = registerBoolean("Debug", "Debug", false);
    this.place_crystal = registerBoolean("Place", "Place", true);
    this.break_crystal = registerBoolean("Break", "Break", true);
    this.break_trys = registerInteger("Break Attempts", "CaBreakAttempts", 2, 1, 6);
    this.anti_weakness = registerBoolean("Anti-Weakness", "CaAntiWeakness", true);
    this.hit_range = registerDouble("Hit Range", "CaHitRange", 5.199999809265137D, 1.0D, 6.0D);
    this.place_range = registerDouble("Place Range", "CaPlaceRange", 5.199999809265137D, 1.0D, 6.0D);
    this.hit_range_wall = registerDouble("Range Wall", "CaRangeWall", 4.0D, 1.0D, 6.0D);
    this.place_delay = registerInteger("Place Delay", "CaPlaceDelay", 0, 0, 10);
    this.break_delay = registerInteger("Break Delay", "CaBreakDelay", 2, 0, 10);
    this.min_player_place = registerDouble("Min Enemy Place", "CaMinEnemyPlace", 8.0D, 0.0D, 20.0D);
    this.min_player_break = registerDouble("Min Enemy Break", "CaMinEnemyBreak", 6.0D, 0.0D, 20.0D);
    this.max_self_damage = registerDouble("Max Self Damage", "CaMaxSelfDamage", 6.0D, 0.0D, 20.0D);
    this.rotatemode = registerMode("Rotate", "CaRotateMode", rotate_mode, "Old");
    this.raytrace = registerBoolean("Raytrace", "CaRaytrace", false);
    this.auto_switch = registerBoolean("Auto Switch", "CaAutoSwitch", true);
    this.anti_suicide = registerBoolean("Anti Suicide", "CaAntiSuicide", true);
    this.fast_mode = registerBoolean("Fast Mode", "CaSpeed", true);
    this.client_side = registerBoolean("Client Side", "CaClientSide", false);
    this.jumpy_mode = registerBoolean("Jumpy Mode", "CaJumpyMode", false);
    this.anti_stuck = registerBoolean("Anti Stuck", "CaAntiStuck", false);
    this.endcrystal = registerBoolean("1.13 Mode", "CaThirteen", false);
    this.faceplace_mode = registerBoolean("Faceplace Mode", "CaTabbottMode", true);
    this.faceplace_mode_damage = registerDouble("F Health", "CaTabbottModeHealth", 8.0D, 0.0D, 36.0D);
    this.fuck_armor_mode = registerBoolean("Armor Destroy", "CaArmorDestory", true);
    this.fuck_armor_mode_precent = registerDouble("Armor %", "CaArmorPercent", 25.0D, 0.0D, 100.0D);
    this.stop_while_mining = registerBoolean("Stop While Mining", "CaStopWhileMining", false);
    this.faceplace_check = registerBoolean("No Sword FP", "CaJumpyFaceMode", false);
    this.old_render = registerBoolean("Old Render", "CaOldRender", false);
    this.slabRender = registerBoolean("Slab", "Slab", true);
    this.r = registerInteger("Red", "Red", 255, 0, 255);
    this.g = registerInteger("Green", "Green", 255, 0, 255);
    this.b = registerInteger("Blue", "Blue", 255, 0, 255);
    this.a = registerInteger("Alpha", "Alpha", 255, 0, 255);
    this.rO = registerInteger("RedO", "RedO", 255, 0, 255);
    this.gO = registerInteger("GreenO", "GreenO", 255, 0, 255);
    this.bO = registerInteger("BlueO", "BlueO", 255, 0, 255);
    this.outlineW = registerInteger("OutlineW", "OutlineW", 2, 1, 10);
    this.rainbow = registerBoolean("Rainbow", "Rainbow", false);
    this.swing = registerBoolean("Swing", "CaSwing", true);
    this.chain_length = registerInteger("Chain Length", "CaChainLength", 3, 1, 6);
  }
  
  public void do_ca() {
    this.did_anything = false;
    if (mc.player == null || mc.world == null)
      return; 
    if (this.remove_visual_timer.passed(1000L)) {
      this.remove_visual_timer.reset();
      this.attacked_crystals.clear();
    } 
    if (check_pause())
      return; 
    if (this.place_crystal.getValue() && this.place_delay_counter > this.place_timeout)
      place_crystal(); 
    if (this.break_crystal.getValue() && this.break_delay_counter > this.break_timeout)
      break_crystal(); 
    if (!this.did_anything)
      this.is_rotating = false; 
    if (this.chain_timer.passed(1000L)) {
      this.chain_timer.reset();
      this.chain_step = 0;
    } 
    this.render_block_old = this.render_block_init;
    this.break_delay_counter++;
    this.place_delay_counter++;
  }
  
  public void onUpdate() {
    if (this.rotatemode.getValue().equalsIgnoreCase("Off") || this.rotatemode.getValue().equalsIgnoreCase("Old"))
      do_ca(); 
  }
  
  public EntityEnderCrystal get_best_crystal() {
    double best_damage = 0.0D;
    double maximum_damage_self = this.max_self_damage.getValue();
    double best_distance = 0.0D;
    EntityEnderCrystal best_crystal = null;
    for (Entity c : mc.world.loadedEntityList) {
      if (!(c instanceof EntityEnderCrystal))
        continue; 
      EntityEnderCrystal crystal = (EntityEnderCrystal)c;
      if (mc.player.getDistance((Entity)crystal) > (!mc.player.canEntityBeSeen((Entity)crystal) ? this.hit_range_wall.getValue() : this.hit_range.getValue()))
        continue; 
      if (!mc.player.canEntityBeSeen((Entity)crystal) && this.raytrace.getValue())
        continue; 
      if (crystal.isDead)
        continue; 
      if (this.attacked_crystals.containsKey(crystal) && ((Integer)this.attacked_crystals.get(crystal)).intValue() > 5 && this.anti_stuck.getValue())
        continue; 
      for (Entity player : mc.world.playerEntities) {
        double minimum_damage;
        if (player == mc.player || !(player instanceof EntityPlayer))
          continue; 
        if (Friends.isFriend(player.getName()))
          continue; 
        if (player.getDistance((Entity)mc.player) >= 11.0F)
          continue; 
        EntityPlayer target = (EntityPlayer)player;
        if (target.isDead || target.getHealth() <= 0.0F)
          continue; 
        boolean no_place = (this.faceplace_check.getValue() && mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD);
        if ((target.getHealth() < this.faceplace_mode_damage.getValue() && this.faceplace_mode.getValue() && !no_place) || (get_armor_fucker(target) && !no_place)) {
          minimum_damage = 2.0D;
        } else {
          minimum_damage = this.min_player_break.getValue();
        } 
        double target_damage = WurstplusCrystalUtil.calculateDamage(crystal, (Entity)target);
        if (target_damage < minimum_damage)
          continue; 
        double self_damage = WurstplusCrystalUtil.calculateDamage(crystal, (Entity)mc.player);
        if (self_damage > maximum_damage_self || (this.anti_suicide.getValue() && (mc.player.getHealth() + mc.player.getAbsorptionAmount()) - self_damage <= 0.5D))
          continue; 
        if (target_damage > best_damage && !this.jumpy_mode.getValue()) {
          best_damage = target_damage;
          best_crystal = crystal;
        } 
      } 
      if (this.jumpy_mode.getValue() && mc.player.getDistanceSq((Entity)crystal) > best_distance) {
        best_distance = mc.player.getDistanceSq((Entity)crystal);
        best_crystal = crystal;
      } 
    } 
    return best_crystal;
  }
  
  public BlockPos get_best_block() {
    if (get_best_crystal() != null && !this.fast_mode.getValue()) {
      this.place_timeout_flag = true;
      return null;
    } 
    if (this.place_timeout_flag) {
      this.place_timeout_flag = false;
      return null;
    } 
    List<WurstplusPair<Double, BlockPos>> damage_blocks = new ArrayList<>();
    double best_damage = 0.0D;
    double maximum_damage_self = this.max_self_damage.getValue();
    BlockPos best_block = null;
    List<BlockPos> blocks = WurstplusCrystalUtil.possiblePlacePositions((float)this.place_range.getValue(), this.endcrystal.getValue(), true);
    for (Entity player : mc.world.playerEntities) {
      if (Friends.isFriend(player.getName()))
        continue; 
      for (BlockPos block : blocks) {
        double minimum_damage;
        if (player == mc.player || !(player instanceof EntityPlayer))
          continue; 
        if (player.getDistance((Entity)mc.player) >= 11.0F)
          continue; 
        if (!BlockUtils.rayTracePlaceCheck(block, this.raytrace.getValue()))
          continue; 
        if (!BlockUtils.canSeeBlock(block) && mc.player.getDistance(block.getX(), block.getY(), block.getZ()) > this.hit_range_wall.getValue())
          continue; 
        EntityPlayer target = (EntityPlayer)player;
        if (target.isDead || target.getHealth() <= 0.0F)
          continue; 
        boolean no_place = (this.faceplace_check.getValue() && mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD);
        if ((target.getHealth() < this.faceplace_mode_damage.getValue() && this.faceplace_mode.getValue() && !no_place) || (get_armor_fucker(target) && !no_place)) {
          minimum_damage = 2.0D;
        } else {
          minimum_damage = this.min_player_place.getValue();
        } 
        double target_damage = WurstplusCrystalUtil.calculateDamage(block.getX() + 0.5D, block.getY() + 1.0D, block.getZ() + 0.5D, (Entity)target);
        if (target_damage < minimum_damage)
          continue; 
        double self_damage = WurstplusCrystalUtil.calculateDamage(block.getX() + 0.5D, block.getY() + 1.0D, block.getZ() + 0.5D, (Entity)mc.player);
        if (self_damage > maximum_damage_self || (this.anti_suicide.getValue() && (mc.player.getHealth() + mc.player.getAbsorptionAmount()) - self_damage <= 0.5D))
          continue; 
        if (target_damage > best_damage) {
          best_damage = target_damage;
          best_block = block;
        } 
      } 
    } 
    blocks.clear();
    if (this.chain_step == 1) {
      this.current_chain_index = this.chain_length.getValue();
    } else if (this.chain_step > 1) {
      this.current_chain_index--;
    } 
    this.render_damage_value = best_damage;
    this.render_block_init = best_block;
    damage_blocks = sort_best_blocks(damage_blocks);
    return best_block;
  }
  
  public List<WurstplusPair<Double, BlockPos>> sort_best_blocks(List<WurstplusPair<Double, BlockPos>> list) {
    List<WurstplusPair<Double, BlockPos>> new_list = new ArrayList<>();
    double damage_cap = 1000.0D;
    for (int i = 0; i < list.size(); i++) {
      double biggest_dam = 0.0D;
      WurstplusPair<Double, BlockPos> best_pair = null;
      for (WurstplusPair<Double, BlockPos> pair : list) {
        if (((Double)pair.getKey()).doubleValue() > biggest_dam && ((Double)pair.getKey()).doubleValue() < damage_cap)
          best_pair = pair; 
      } 
      if (best_pair != null) {
        damage_cap = ((Double)best_pair.getKey()).doubleValue();
        new_list.add(best_pair);
      } 
    } 
    return new_list;
  }
  
  public void place_crystal() {
    BlockPos target_block = get_best_block();
    if (target_block == null)
      return; 
    this.place_delay_counter = 0;
    this.already_attacking = false;
    boolean offhand_check = false;
    if (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
      if (mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && this.auto_switch.getValue()) {
        if (find_crystals_hotbar() == -1)
          return; 
        mc.player.inventory.currentItem = find_crystals_hotbar();
        return;
      } 
    } else {
      offhand_check = true;
    } 
    if (this.debug.getValue())
      Command.sendClientMessage("placing"); 
    this.chain_step++;
    this.did_anything = true;
    rotate_to_pos(target_block);
    this.chain_timer.reset();
    BlockUtils.placeCrystalOnBlock(target_block, offhand_check ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
  }
  
  public boolean get_armor_fucker(EntityPlayer p) {
    for (ItemStack stack : p.getArmorInventoryList()) {
      if (stack == null || stack.getItem() == Items.AIR)
        return true; 
      float armor_percent = (stack.getMaxDamage() - stack.getItemDamage()) / stack.getMaxDamage() * 100.0F;
      if (this.fuck_armor_mode.getValue() && this.fuck_armor_mode_precent.getValue() >= armor_percent)
        return true; 
    } 
    return false;
  }
  
  public void break_crystal() {
    EntityEnderCrystal crystal = get_best_crystal();
    if (crystal == null)
      return; 
    if (this.anti_weakness.getValue() && mc.player.isPotionActive(MobEffects.WEAKNESS)) {
      boolean should_weakness = true;
      if (mc.player.isPotionActive(MobEffects.STRENGTH))
        if (((PotionEffect)Objects.<PotionEffect>requireNonNull(mc.player.getActivePotionEffect(MobEffects.STRENGTH))).getAmplifier() == 2)
          should_weakness = false;  
      if (should_weakness) {
        if (!this.already_attacking)
          this.already_attacking = true; 
        int new_slot = -1;
        for (int j = 0; j < 9; j++) {
          ItemStack stack = mc.player.inventory.getStackInSlot(j);
          if (stack.getItem() instanceof net.minecraft.item.ItemSword || stack.getItem() instanceof net.minecraft.item.ItemTool) {
            new_slot = j;
            mc.playerController.updateController();
            break;
          } 
        } 
        if (new_slot != -1)
          mc.player.inventory.currentItem = new_slot; 
      } 
    } 
    if (this.debug.getValue())
      Command.sendClientMessage("attacking"); 
    this.did_anything = true;
    rotate_to((Entity)crystal);
    for (int i = 0; i < this.break_trys.getValue(); i++)
      EntityUtil.attackEntity((Entity)crystal, false, this.swing2); 
    add_attacked_crystal(crystal);
    if (this.client_side.getValue() && crystal.isEntityAlive())
      crystal.setDead(); 
    this.break_delay_counter = 0;
  }
  
  public boolean check_pause() {
    if (find_crystals_hotbar() == -1 && mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL)
      return true; 
    if (this.stop_while_mining.getValue() && mc.gameSettings.keyBindAttack.isKeyDown() && mc.player.getHeldItemMainhand().getItem() instanceof net.minecraft.item.ItemPickaxe) {
      if (this.old_render.getValue())
        this.render_block_init = null; 
      return true;
    } 
    return false;
  }
  
  private int find_crystals_hotbar() {
    for (int i = 0; i < 9; i++) {
      if (mc.player.inventory.getStackInSlot(i).getItem() == Items.END_CRYSTAL)
        return i; 
    } 
    return -1;
  }
  
  private void add_attacked_crystal(EntityEnderCrystal crystal) {
    if (this.attacked_crystals.containsKey(crystal)) {
      int value = ((Integer)this.attacked_crystals.get(crystal)).intValue();
      this.attacked_crystals.put(crystal, Integer.valueOf(value + 1));
    } else {
      this.attacked_crystals.put(crystal, Integer.valueOf(1));
    } 
  }
  
  public void rotate_to_pos(BlockPos pos) {
    float[] angle;
    if (this.rotatemode.getValue().equalsIgnoreCase("Const")) {
      angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((pos.getX() + 0.5F), (pos.getY() + 0.5F), (pos.getZ() + 0.5F)));
    } else {
      angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((pos.getX() + 0.5F), (pos.getY() - 0.5F), (pos.getZ() + 0.5F)));
    } 
    if (this.rotatemode.getValue().equalsIgnoreCase("Off"))
      this.is_rotating = false; 
    if (this.rotatemode.getValue().equalsIgnoreCase("Good") || this.rotatemode.getValue().equalsIgnoreCase("Const"))
      WurstplusRotationUtil.setPlayerRotations(angle[0], angle[1]); 
    if (this.rotatemode.getValue().equalsIgnoreCase("Old")) {
      this.yaw = angle[0];
      this.pitch = angle[1];
      this.is_rotating = true;
    } 
  }
  
  public void rotate_to(Entity entity) {
    float[] angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), entity.getPositionVector());
    if (this.rotatemode.getValue().equalsIgnoreCase("Off"))
      this.is_rotating = false; 
    if (this.rotatemode.getValue().equalsIgnoreCase("Good"))
      WurstplusRotationUtil.setPlayerRotations(angle[0], angle[1]); 
    if (this.rotatemode.getValue().equalsIgnoreCase("Old") || this.rotatemode.getValue().equalsIgnoreCase("Cont")) {
      this.yaw = angle[0];
      this.pitch = angle[1];
      this.is_rotating = true;
    } 
  }
  
  public void onWorldRender(RenderEvent event) {
    if (this.render_block_old != null) {
      float[] hue = { (float)(System.currentTimeMillis() % 2520L) / 2520.0F };
      int rgb = Color.HSBtoRGB(hue[0], 1.0F, 1.0F);
      int rr = rgb >> 16 & 0xFF;
      int gg = rgb >> 8 & 0xFF;
      int bbb = rgb & 0xFF;
      AxisAlignedBB bb = new AxisAlignedBB(this.render_block_old.getX() - (mc.getRenderManager()).viewerPosX, this.render_block_old.getY() - (mc.getRenderManager()).viewerPosY + 1.0D, this.render_block_old.getZ() - (mc.getRenderManager()).viewerPosZ, (this.render_block_old.getX() + 1) - (mc.getRenderManager()).viewerPosX, this.render_block_old.getY() + (this.slabRender.getValue() ? 1.1D : 0.0D) - (mc.getRenderManager()).viewerPosY, (this.render_block_old.getZ() + 1) - (mc.getRenderManager()).viewerPosZ);
      if (RenderUtil.isInViewFrustrum(new AxisAlignedBB(bb.minX + (mc.getRenderManager()).viewerPosX, bb.minY + (mc.getRenderManager()).viewerPosY, bb.minZ + (mc.getRenderManager()).viewerPosZ, bb.maxX + (mc.getRenderManager()).viewerPosX, bb.maxY + (mc.getRenderManager()).viewerPosY, bb.maxZ + (mc.getRenderManager()).viewerPosZ))) {
        RenderUtil.drawESP(bb, this.rainbow.getValue() ? rr : this.r.getValue(), this.rainbow.getValue() ? gg : this.g.getValue(), this.rainbow.getValue() ? bbb : this.b.getValue(), this.a.getValue());
        RenderUtil.drawESPOutline(bb, this.rainbow.getValue() ? rr : this.rO.getValue(), this.rainbow.getValue() ? gg : this.gO.getValue(), this.rainbow.getValue() ? bbb : this.bO.getValue(), 255.0F, this.outlineW.getValue());
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        RenderHelper.disableStandardItemLighting();
      } 
    } 
  }
  
  public void onEnable() {
    this.place_timeout = this.place_delay.getValue();
    this.break_timeout = this.break_delay.getValue();
    this.place_timeout_flag = false;
    this.is_rotating = false;
    this.chain_step = 0;
    this.current_chain_index = 0;
    this.chain_timer.reset();
    this.remove_visual_timer.reset();
    this.detail_name = null;
    this.detail_hp = 20;
    Command.sendClientMessage("§aAutoCrystal+ turned ON!");
  }
  
  public void onDisable() {
    this.render_block_init = null;
    Command.sendClientMessage("§cAutoCrystal+ turned OFF!");
  }
}
