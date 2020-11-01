//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.misc;

import com.gamesense.api.event.events.DestroyBlockEvent;
import com.gamesense.api.event.events.PacketEvent;
import com.gamesense.api.event.events.PlayerJumpEvent;
import com.gamesense.api.settings.Setting;
import com.gamesense.client.GameSenseMod;
import com.gamesense.client.command.Command;
import com.gamesense.client.module.Module;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

public class Announcer extends Module {
  public Announcer() {
    super("Announcer", Module.Category.Misc);
    this.heldItem = "";
    this.blocksPlaced = 0;
    this.blocksBroken = 0;
    this.eaten = 0;
    this.eatListener = new Listener(event -> {
          int randomNum = ThreadLocalRandom.current().nextInt(1, 11);
          if (event.getEntity() == mc.player && (event.getItem().getItem() instanceof net.minecraft.item.ItemFood || event.getItem().getItem() instanceof net.minecraft.item.ItemAppleGold)) {
            this.eaten++;
            if (eattingDelay >= 300 * this.delay.getValue() && this.eat.getValue() && this.eaten > randomNum) {
              Random random = new Random();
              if (this.clientSide.getValue()) {
                Command.sendClientMessage(eatMessages[random.nextInt(eatMessages.length)].replace("{amount}", this.eaten + "").replace("{name}", mc.player.getHeldItemMainhand().getDisplayName()));
              } else {
                mc.player.sendChatMessage(eatMessages[random.nextInt(eatMessages.length)].replace("{amount}", this.eaten + "").replace("{name}", mc.player.getHeldItemMainhand().getDisplayName()));
              } 
              this.eaten = 0;
              eattingDelay = 0;
            } 
          } 
        }new java.util.function.Predicate[0]);
    this.sendListener = new Listener(event -> {
          if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock && mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof net.minecraft.item.ItemBlock) {
            this.blocksPlaced++;
            int randomNum = ThreadLocalRandom.current().nextInt(1, 11);
            if (blockPlacedDelay >= 150 * this.delay.getValue() && this.place.getValue() && this.blocksPlaced > randomNum) {
              Random random = new Random();
              String msg = placeMessages[random.nextInt(placeMessages.length)].replace("{amount}", this.blocksPlaced + "").replace("{name}", mc.player.getHeldItemMainhand().getDisplayName());
              if (this.clientSide.getValue()) {
                Command.sendClientMessage(msg);
              } else {
                mc.player.sendChatMessage(msg);
              } 
              this.blocksPlaced = 0;
              blockPlacedDelay = 0;
            } 
          } 
        }new java.util.function.Predicate[0]);
    this.destroyListener = new Listener(event -> {
          this.blocksBroken++;
          int randomNum = ThreadLocalRandom.current().nextInt(1, 11);
          if (blockBrokeDelay >= 300 * this.delay.getValue() && this.breaking.getValue() && this.blocksBroken > randomNum) {
            Random random = new Random();
            String msg = breakMessages[random.nextInt(breakMessages.length)].replace("{amount}", this.blocksBroken + "").replace("{name}", mc.world.getBlockState(event.getBlockPos()).getBlock().getLocalizedName());
            if (this.clientSide.getValue()) {
              Command.sendClientMessage(msg);
            } else {
              mc.player.sendChatMessage(msg);
            } 
            this.blocksBroken = 0;
            blockBrokeDelay = 0;
          } 
        }new java.util.function.Predicate[0]);
    this.attackListener = new Listener(event -> {
          if (this.attack.getValue() && !(event.getTarget() instanceof net.minecraft.entity.item.EntityEnderCrystal) && attackDelay >= 300 * this.delay.getValue()) {
            String msg = attackMessage.replace("{name}", event.getTarget().getName()).replace("{item}", mc.player.getHeldItemMainhand().getDisplayName());
            if (this.clientSide.getValue()) {
              Command.sendClientMessage(msg);
            } else {
              mc.player.sendChatMessage(msg);
            } 
            attackDelay = 0;
          } 
        }new java.util.function.Predicate[0]);
    this.jumpListener = new Listener(event -> {
          if (this.jump.getValue() && jumpDelay >= 300 * this.delay.getValue()) {
            if (this.clientSide.getValue()) {
              Random random = new Random();
              Command.sendClientMessage(jumpMessages[random.nextInt(jumpMessages.length)]);
            } else {
              Random random = new Random();
              mc.player.sendChatMessage(jumpMessages[random.nextInt(jumpMessages.length)]);
            } 
            jumpDelay = 0;
          } 
        }new java.util.function.Predicate[0]);
  }
  
  public static int blockBrokeDelay = 0;
  
  static int blockPlacedDelay = 0;
  
  static int jumpDelay = 0;
  
  static int attackDelay = 0;
  
  static int eattingDelay = 0;
  
  static long lastPositionUpdate;
  
  static double lastPositionX;
  
  static double lastPositionY;
  
  static double lastPositionZ;
  
  private static double speed;
  
  String heldItem;
  
  int blocksPlaced;
  
  int blocksBroken;
  
  int eaten;
  
  public Setting.Boolean clientSide;
  
  Setting.Boolean walk;
  
  Setting.Boolean place;
  
  Setting.Boolean jump;
  
  Setting.Boolean breaking;
  
  Setting.Boolean attack;
  
  Setting.Boolean eat;
  
  public Setting.Boolean clickGui;
  
  Setting.Integer delay;
  
  public static String walkMessage = "I just walked {blocks} meters thanks to GameSense!";
  
  public static String placeMessage = "I just inserted {amount} {name} into the muliverse thanks to GameSense!";
  
  public static String jumpMessage = "I just hovered in the air thanks to GameSense!";
  
  public static String breakMessage = "I just snapped {amount} {name} out of existance thanks to GameSense!";
  
  public static String attackMessage = "I just disembowed {name} with a {item} thanks to GameSense!";
  
  public static String eatMessage = "I just gobbled up {amount} {name} thanks to GameSense!";
  
  public static String guiMessage = "I just opened my advanced hacking console thanks to GameSense!";
  
  public static String[] walkMessages = new String[] { 
      "I just walked {blocks} meters thanks to GameSense!", "!لقد مشيت للتو على بعد {blocks} متر من الأمتار ب�?ضل GameSense!", "¡Acabo de caminar {blocks} metros gracias a GameSense!", "Je viens de marcher {blocks} mètres grâce à GameSense!", "פשוט הלכתי {blocks} מטרי�? בזכות GameSense!", "Ich bin gerade {blocks} Meter dank GameSense gelaufen!\n", "GameSense�?��?��?��?��?�{blocks}メートル歩�?��?��?��?��?�?��?�!", "Ik heb net {blocks} gelopen met dank aan GameSense!", "Μώλις πε�?πάτησα {blocks} μέτ�?α χά�?η το GameSense!", "GameSense sayesinde {blocks} metre yürüdüm!", 
      "Właśnie przeszedłem {blocks} metry dzięki GameSense!", "Я про�?то прошел {blocks} метров благодар�? GameSense!", "Jeg gik lige {blocks} meter takket være GameSense!", "Unë vetëm eca {blocks} metra falë GameSense!", "多�?了GameSense，我�?走了{blocks}米�?", "Kävelin juuri {blocks} metriä GameSensen ansiosta!" };
  
  public static String[] placeMessages = new String[] { 
      "I just inserted {amount} {name} into the muliverse thanks to GameSense!", "لقد أدرجت للتو {amount} {name} �?ي muliverse ب�?ضل GameSense!", "¡Acabo de insertar {amount} {name} en el universo gracias a GameSense!", "Je viens d'insérer {amount} {name} dans le mulivers grâce à GameSense!", "הרגע הכנסתי �?ת {amount} {name} למוליברס בזכות GameSense!", "Ich habe gerade dank GameSense {amount} {name} in das Multiversum eingefÃ¼gt! \n", "GameSense�?��?��?��?��?��?{amount} {name}をマル�?�?ース�?�挿入�?��?��?��?��?", "Ik heb zojuist {amount} {name} in het muliversum ingevoegd dankzij GameSense!", "Μώλις χ�?ησιμοποιήσα {amount} {name} χά�?η το GameSense", "GameSense sayesinde birden fazla kişiye {amount} {name} ekledim!", 
      "Właśnie wstawiłem {amount} {name} do wielu dzięki GameSense!", "Я только что в�?тавил {amount} {name} во в�?еленную благодар�? GameSense!", "Jeg har lige indsat {amount} {name} i muliversen takket være GameSense!", "多�?了GameSense，我刚刚将{amount} {name}�?�入了多人游�?�?", "Unë vetëm futa {amount} {name} në muliverse falë GameSense!" };
  
  public static String[] jumpMessages = new String[] { 
      "I just hovered in the air thanks to GameSense!", "لقد حومت للتو �?ي الهواء ب�?ضل GameSense!", "¡Acabo de volar en el aire gracias a GameSense!", "Je viens de planer dans les airs grâce à GameSense!", "פשוט ריחפתי ב�?וויר בזכות GameSense!", "Ich habe gerade dank GameSense in der Luft geschwebt!\n", "GameSense�?��?��?��?��?�宙�?�浮�?��?��?��?��?��?��?", "Dankzij GameSense zweefde ik gewoon in de lucht!", "Μόλις αιω�?ήθηκα στον αέ�?α χά�?ης το GameSense!", "GameSense sayesinde havada asılı kaldım!", 
      "Po prostu unosiłem się w powietrzu dzięki GameSense!", "Я про�?то зави�? в воздухе благодар�? GameSense!", "Unë thjesht u fiksova në ajër falë GameSense!", "多�?了GameSense，我�?徘徊在空中�?", "Minä vain leijuin ilmassa GameSensen ansiosta!" };
  
  public static String[] breakMessages = new String[] { 
      "I just snapped {amount} {name} out of existance thanks to GameSense!", "لقد قطعت للتو {amount} {name} من خارج ب�?ضل GameSense!", "¡Acabo de sacar {amount} {name} de la existencia gracias a GameSense!", "Je viens de casser {amount} {name} hors de l'existence grâce à GameSense!", "פשוט חטפתי �?ת {amount} {name} מההתקיי�? בזכות GameSense!", "Ich habe gerade {amount} {name} dank GameSense aus der Existenz gerissen!", "GameSense�?��?��?��?��?��?{amount} {name}�?�存在�?��?��??�?�り�?��?��?�。", "Ik heb zojuist {amount} {name} uit het bestaan ​​gehaald dankzij GameSense!", "Μώλις έσπασα το {amount} {name} από την �?πα�?ξη χά�?η στο GameSense!", "GameSense sayesinde {amount} {name} varlığını yeni çıkardım!", 
      "Właśnie wyskoczyłem z gry dzięki {amount} {name} dzięki GameSense!", "Я только что отключил {amount} {name} из �?уще�?твовани�? благодар�? GameSense!", "Jeg har lige slået {amount} {name} ud af eksistens takket være GameSense!", "多�?了GameSense，我�?将{amount} {name}淘汰了�?", "Napsautin juuri {amount} {name} olemassaolosta GameSensen ansiosta!" };
  
  public static String[] eatMessages = new String[] { 
      "I just ate {amount} {name} thanks to GameSense!", "لقد أكلت للتو {amount} {name} ب�?ضل GameSense!", "¡Acabo de comer {amount} {name} gracias a GameSense!", "Je viens de manger {amount} {name} grâce à GameSense!", "פשוט �?כלתי {amount} {name} בזכות GameSense!", "Ich habe gerade dank GameSense {amount} {name} gegessen!", "GameSense�?��?��?��?��?�{amount} {name}を食�?��?��?��?�。", "Ik heb zojuist {amount} {name} gegeten dankzij GameSense!", "Μόλις έφαγα {amount} {name} χά�?η στο GameSense!", "GameSense sayesinde sadece {amount} {name} yedim!", 
      "Właśnie zjadłem {amount} {name} dzięki GameSense!", "Jeg spiste lige {amount} {name} takket være GameSense!", "Я только что �?ъел {amount} {name} благодар�? GameSense!", "Unë thjesht hëngra {amount} {name} falë GameSense!", "感谢GameSense，我刚�?�了{amount} {name}�?", "Söin juuri {amount} {name} Gamessenin ansiosta!" };
  
  @EventHandler
  private final Listener<LivingEntityUseItemEvent.Finish> eatListener;
  
  @EventHandler
  private final Listener<PacketEvent.Send> sendListener;
  
  @EventHandler
  private final Listener<DestroyBlockEvent> destroyListener;
  
  @EventHandler
  private final Listener<AttackEntityEvent> attackListener;
  
  @EventHandler
  private final Listener<PlayerJumpEvent> jumpListener;
  
  public void setup() {
    this.clientSide = registerBoolean("Client Side", "ClientSide", false);
    this.walk = registerBoolean("Walk", "Walk", true);
    this.place = registerBoolean("Place", "Place", true);
    this.jump = registerBoolean("Jump", "Jump", true);
    this.breaking = registerBoolean("Breaking", "Breaking", true);
    this.attack = registerBoolean("Attack", "Attack", true);
    this.eat = registerBoolean("Eat", "Eat", true);
    this.clickGui = registerBoolean("DevGUI", "DevGUI", true);
    this.delay = registerInteger("Delay", "Delay", 1, 1, 20);
  }
  
  public void onUpdate() {
    blockBrokeDelay++;
    blockPlacedDelay++;
    jumpDelay++;
    attackDelay++;
    eattingDelay++;
    this.heldItem = mc.player.getHeldItemMainhand().getDisplayName();
    if (this.walk.getValue() && lastPositionUpdate + 5000L * this.delay.getValue() < System.currentTimeMillis()) {
      double d0 = lastPositionX - mc.player.lastTickPosX;
      double d2 = lastPositionY - mc.player.lastTickPosY;
      double d3 = lastPositionZ - mc.player.lastTickPosZ;
      speed = Math.sqrt(d0 * d0 + d2 * d2 + d3 * d3);
      if (speed > 1.0D && speed <= 5000.0D) {
        String walkAmount = (new DecimalFormat("0.00")).format(speed);
        Random random = new Random();
        if (this.clientSide.getValue()) {
          Command.sendClientMessage(walkMessage.replace("{blocks}", walkAmount));
        } else {
          mc.player.sendChatMessage(walkMessages[random.nextInt(walkMessages.length)].replace("{blocks}", walkAmount));
        } 
        lastPositionUpdate = System.currentTimeMillis();
        lastPositionX = mc.player.lastTickPosX;
        lastPositionY = mc.player.lastTickPosY;
        lastPositionZ = mc.player.lastTickPosZ;
      } 
    } 
  }
  
  public void onEnable() {
    GameSenseMod.EVENT_BUS.subscribe(this);
    this.blocksPlaced = 0;
    this.blocksBroken = 0;
    this.eaten = 0;
    speed = 0.0D;
    blockBrokeDelay = 0;
    blockPlacedDelay = 0;
    jumpDelay = 0;
    attackDelay = 0;
    eattingDelay = 0;
  }
  
  public void onDisable() {
    GameSenseMod.EVENT_BUS.unsubscribe(this);
  }
}
