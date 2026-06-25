package keystrokesmod.utility;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import keystrokesmod.module.Module;
import keystrokesmod.module.ModuleManager;
import keystrokesmod.module.impl.client.Settings;
import keystrokesmod.module.impl.combat.AutoClicker;
import keystrokesmod.module.impl.movement.NoSlow;
import keystrokesmod.module.impl.player.Freecam;
import keystrokesmod.module.setting.impl.SliderSetting;
import net.minecraft.world.level.block.*;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.CameraType;
import net.minecraft.client.Camera;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.*;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerScoreEntry;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.ClipContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;
import java.util.*;
import java.util.Optional;
import java.util.stream.IntStream;

public class Utils {
    private static final Random rand = new Random();
    public static HashSet<String> friends = new HashSet<>();
    public static HashSet<String> enemies = new HashSet<>();
    public static final Logger log = LogManager.getLogger();

    private static final int darkRed = new Color(189, 0, 1).getRGB();
    private static final int red = new Color(253, 63, 63).getRGB();
    private static final int gold = new Color(215, 162, 50).getRGB();
    private static final int yellow = new Color(254, 254, 62).getRGB();
    private static final int darkGreen = new Color(0, 191, 4).getRGB();
    private static final int green = new Color(64, 253, 62).getRGB();
    private static final int aqua = new Color(65, 255, 254).getRGB();
    private static final int darkAqua = new Color(0, 190, 189).getRGB();
    private static final int darkBlue = new Color(1, 1, 187).getRGB();
    private static final int blue = new Color(61, 64, 255).getRGB();
    private static final int lightPurple = new Color(254, 63, 255).getRGB();
    private static final int darkPurple = new Color(190, 0, 190).getRGB();
    private static final int gray = new Color(190, 190, 190).getRGB();
    private static final int darkGray = new Color(63, 63, 63).getRGB();
    private static final int black = new Color(17, 17, 17).getRGB();

    public static float getMovementForward() {
        if (Mc.player() == null) {
            return 0.0F;
        }
        return Mc.player().input.getMoveVector().y;
    }

    public static float getMovementSideways() {
        if (Mc.player() == null) {
            return 0.0F;
        }
        return Mc.player().input.getMoveVector().x;
    }

    public static boolean addEnemy(String name) {
        if (enemies.add(name.toLowerCase())) {
            sendMessage("&7Added enemy&7: &b" + name);
            return true;
        }
        return false;
    }

    public static boolean removeEnemy(String name) {
        if (enemies.remove(name.toLowerCase())) {
            sendMessage("&Removed enemy&7: &b" + name);
            return true;
        }
        return false;
    }

    public static float getCameraYaw() {
        Camera camera = Mc.mc().gameRenderer.getMainCamera();
        return camera.yRot();
    }

    public static float getCameraPitch() {
        Camera camera = Mc.mc().gameRenderer.getMainCamera();
        return camera.xRot();
    }

    public static Vec3 getCameraPos(double renderPartialTicks) {
        if (Mc.mc().options.getCameraType() == CameraType.FIRST_PERSON) {
            return Mc.player().getEyePosition();
        }
        float cameraDistance = 4.0F;
        if (ModuleManager.extendCamera != null && ModuleManager.extendCamera.isEnabled()) {
            cameraDistance = (float) ModuleManager.extendCamera.distance.getInput();
        }

        Entity renderEntity = Mc.mc().getCameraEntity();
        float entityEyeHeight = renderEntity.getEyeHeight();

        double interpolatedX = Mth.lerp(renderPartialTicks, renderEntity.xOld, renderEntity.getX());
        double interpolatedY = Mth.lerp(renderPartialTicks, renderEntity.yOld, renderEntity.getY()) + entityEyeHeight;
        double interpolatedZ = Mth.lerp(renderPartialTicks, renderEntity.zOld, renderEntity.getZ());

        double adjustedDistance = cameraDistance;
        float cameraYaw = getCameraYaw();
        float cameraPitch = getCameraPitch();

        double offsetX = -Mth.sin(cameraYaw * ((float) Math.PI / 180F)) * Mth.cos(cameraPitch * ((float) Math.PI / 180F)) * adjustedDistance;
        double offsetZ = Mth.cos(cameraYaw * ((float) Math.PI / 180F)) * Mth.cos(cameraPitch * ((float) Math.PI / 180F)) * adjustedDistance;
        double offsetY = -Mth.sin(cameraPitch * ((float) Math.PI / 180F)) * adjustedDistance;

        if (ModuleManager.noCameraClip == null || !ModuleManager.noCameraClip.isEnabled()) {
            for (int i = 0; i < 8; i++) {
                float cornerOffsetX = (float) ((i & 1) * 2 - 1) * 0.1F;
                float cornerOffsetY = (float) ((i >> 1 & 1) * 2 - 1) * 0.1F;
                float cornerOffsetZ = (float) ((i >> 2 & 1) * 2 - 1) * 0.1F;

                Vec3 start = new Vec3(interpolatedX + cornerOffsetX, interpolatedY + cornerOffsetY, interpolatedZ + cornerOffsetZ);
                Vec3 end = new Vec3(
                        interpolatedX - offsetX + cornerOffsetX + cornerOffsetZ,
                        interpolatedY - offsetY + cornerOffsetY,
                        interpolatedZ - offsetZ + cornerOffsetZ
                );
                BlockHitResult rayTraceResult = Mc.level().clip(new ClipContext(
                        start,
                        end,
                        ClipContext.Block.OUTLINE,
                        ClipContext.Fluid.NONE,
                        renderEntity
                ));

                if (rayTraceResult.getType() != HitResult.Type.MISS) {
                    double blockHitDistance = rayTraceResult.getLocation().distanceTo(new Vec3(interpolatedX, interpolatedY, interpolatedZ));
                    if (blockHitDistance < adjustedDistance) {
                        adjustedDistance = blockHitDistance;
                    }
                }
            }
        }

        double finalCameraX = interpolatedX - offsetX * (adjustedDistance / cameraDistance);
        double finalCameraY = interpolatedY - offsetY * (adjustedDistance / cameraDistance);
        double finalCameraZ = interpolatedZ - offsetZ * (adjustedDistance / cameraDistance);

        return new Vec3(finalCameraX, finalCameraY, finalCameraZ);
    }

    public static String getServerName() {
        return Mc.nullCheck() ? Mc.player().getGameProfile().name() : "";
    }

    public static boolean tabbedIn() {
        return Mc.mc().screen == null && Mc.mc().isWindowActive();
    }

    public static String getHardwareIdForLoad(String url) {
        String hashedId = "";
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(((System.currentTimeMillis() / 20000L + 29062381L) + "J{LlrPhHgj8zy:uB").getBytes("UTF-8"));
            hashedId = String.format("%032x", new BigInteger(1, instance.digest()));
            instance.update((System.getenv("COMPUTERNAME") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL") + Runtime.getRuntime().availableProcessors() + url).getBytes("UTF-8"));
            return hashedId;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return hashedId;
    }

    public static boolean isConsuming(Entity entity) {
        if (!(entity instanceof Player player)) {
            return false;
        }
        return player.isUsingItem() && holdingFood(player);
    }

    public static boolean holdingFood(LivingEntity entity) {
        ItemStack stack = entity.getMainHandItem();
        return !stack.isEmpty() && stack.get(DataComponents.FOOD) != null;
    }

    public static int getColorFromEntity(Entity entity) {
        if (entity instanceof Player player) {
            Team team = player.getTeam();
            if (team != null && team.getColor() != ChatFormatting.RESET) {
                Integer color = team.getColor().getColor();
                if (color != null) {
                    return color | 0xFF000000;
                }
            }
        }
        String displayName = entity.getDisplayName().getString();
        displayName = removeFormatCodes(displayName);
        if (displayName.isEmpty() || !displayName.startsWith("\u00A7") || displayName.charAt(1) == 'f') {
            return -1;
        }
        return switch (displayName.charAt(1)) {
            case '0' -> black;
            case '1' -> darkBlue;
            case '2' -> darkGreen;
            case '3' -> darkAqua;
            case '4' -> darkRed;
            case '5' -> darkPurple;
            case '6' -> gold;
            case '7' -> gray;
            case '8' -> darkGray;
            case '9' -> blue;
            case 'a' -> green;
            case 'b' -> aqua;
            case 'c' -> red;
            case 'd' -> lightPurple;
            case 'e' -> yellow;
            default -> -1;
        };
    }

    public static boolean overVoid(double posX, double posY, double posZ) {
        for (int i = (int) posY; i > -1; i--) {
            if (!Mc.level().getBlockState(BlockPos.containing(posX, i, posZ)).isAir()) {
                return false;
            }
        }
        return true;
    }

    public static Block getBlockFromName(String name) {
        return net.minecraft.core.registries.BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath("minecraft", name));
    }

    public static boolean canPlayerBeSeen(LivingEntity player) {
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();
        Vec3 vecPlayer = Mc.player().getEyePosition();
        double shoulderHeight = player.getEyeHeight() - 0.2;
        if (canSeeVec(vecPlayer, new Vec3(x + 0.3, shoulderHeight, z))) {
            return true;
        }
        if (canSeeVec(vecPlayer, new Vec3(x - 0.3, shoulderHeight, z))) {
            return true;
        }
        if (canSeeVec(vecPlayer, new Vec3(x, shoulderHeight, z + 0.3))) {
            return true;
        }
        if (canSeeVec(vecPlayer, new Vec3(x, shoulderHeight, z - 0.3))) {
            return true;
        }
        for (double d = player.getEyeHeight() + 0.2; d > 0.0; d -= 0.2) {
            Vec3 vecPoint = new Vec3(x, y + d, z);
            if (canSeeVec(vecPlayer, vecPoint)) {
                return true;
            }
        }
        return false;
    }

    public static boolean canSeeVec(Vec3 vecPlayer, Vec3 vecTarget) {
        BlockHitResult mop = Mc.level().clip(new ClipContext(
                vecPlayer,
                vecTarget,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                Mc.player()
        ));
        return mop.getType() == HitResult.Type.MISS;
    }

    public static List<PlayerInfo> getTablist(boolean removeSelf) {
        final ArrayList<PlayerInfo> list = new ArrayList<>(Mc.mc().getConnection().getOnlinePlayers());
        removeDuplicates(list);
        if (removeSelf) {
            list.remove(Mc.mc().getConnection().getPlayerInfo(Mc.player().getUUID()));
        }
        return list;
    }

    public static void removeDuplicates(final ArrayList list) {
        final HashSet set = new HashSet(list);
        list.clear();
        list.addAll(set);
    }

    public static boolean removeFriend(String name) {
        if (friends.remove(name.toLowerCase())) {
            sendMessage("&7Removed &afriend&7: &b" + name);
            return true;
        }
        return false;
    }

    public static String getCompilerDirectory() {
        String tempDirStr = System.getProperty("java.io.tmpdir") + "cmF2ZW5fc2NyaXB0cw";
        if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            File tempDir = new File(Mc.mc().gameDirectory + File.separator + "keystrokes" + File.separator + "scripts", "compiler_temp");
            if (!tempDir.exists()) {
                if (!tempDir.mkdirs()) {
                    return tempDirStr;
                }
            }
            return tempDir.getAbsolutePath();
        }
        return tempDirStr;
    }

    public static boolean addFriend(String name) {
        if (friends.add(name.toLowerCase())) {
            sendMessage("&7Added &afriend&7: &b" + name);
            if (enemies.contains(name.toLowerCase())) {
                enemies.remove(name.toLowerCase());
            }
            return true;
        }
        return false;
    }

    public static boolean isWholeNumber(double num) {
        return num == Math.floor(num);
    }

    public static String asWholeNum(double input) {
        return isWholeNumber(input) ? (int) input + "" : String.valueOf(input);
    }

    public static int randomizeInt(int min, int max) {
        return rand.nextInt(max - min + 1) + min;
    }

    public static double randomizeDouble(double min, double max) {
        return min + (max - min) * rand.nextDouble();
    }

    public static boolean inFov(float fov, BlockPos blockPos) {
        return inFov(fov, blockPos.getX(), blockPos.getZ());
    }

    public static boolean inFov(float fov, Entity entity) {
        return inFov(fov, entity.getX(), entity.getZ());
    }

    public static boolean inFov(float fov, final double posX, final double posZ) {
        return inFov(Mc.player(), fov, posX, posZ);
    }

    public static boolean inFov(Entity viewPoint, float fov, final double posX, final double posZ) {
        fov *= 0.5;
        final double wrapAngle = Mth.wrapDegrees((viewPoint.getYRot() - RotationUtils.angle(posX, posZ)) % 360.0f);
        if (wrapAngle > 0.0) {
            return wrapAngle < fov;
        }
        return wrapAngle > -fov;
    }

    public static Vec3 getLookVec(float yaw, float pitch) {
        float f = Mth.cos(-yaw * ((float) Math.PI / 180F) - (float) Math.PI);
        float f1 = Mth.sin(-yaw * ((float) Math.PI / 180F) - (float) Math.PI);
        float f2 = -Mth.cos(-pitch * ((float) Math.PI / 180F));
        float f3 = Mth.sin(-pitch * ((float) Math.PI / 180F));
        return new Vec3(f1 * f2, f3, f * f2);
    }

    public static boolean holdingBow() {
        return !Mc.player().getMainHandItem().isEmpty()
                && Mc.player().getMainHandItem().getItem() instanceof BowItem;
    }

    public static boolean bowBackwards() {
        return holdingBow()
                && Utils.getMovementSideways() == 0
                && Utils.getMovementForward() <= 0
                && isMoving();
    }

    public static boolean noSlowingBackWithBow() {
        return NoSlow.noSlow && bowBackwards();
    }

    public static void sendMessage(String txt) {
        if (nullCheck()) {
            String m = formatColor("&7[&dR&7]&r " + txt);
            Mc.player().displayClientMessage(net.minecraft.network.chat.Component.literal(m), false);
        }
    }

    public static void sendMessage(Object object) {
        sendMessage(String.valueOf(object));
    }

    public static void sendDebugMessage(String message) {
        if (nullCheck()) {
            Mc.player().displayClientMessage(net.minecraft.network.chat.Component.literal("\u00A77[\u00A7dR\u00A77]\u00A7r " + message), false);
        }
    }

    public static void attack(Entity e, boolean clientSwing, boolean silentSwing) {
        if (clientSwing) {
            Mc.player().swing(InteractionHand.MAIN_HAND);
        } else if (silentSwing || (!silentSwing && !clientSwing)) {
            Mc.mc().getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
        }
        Mc.mc().gameMode.attack(Mc.player(), e);
    }

    public static void sendRawMessage(String txt) {
        if (nullCheck()) {
            Mc.player().displayClientMessage(net.minecraft.network.chat.Component.literal(formatColor(txt)), false);
        }
    }

    public static float getCompleteHealth(LivingEntity entity) {
        return entity.getHealth() + entity.getAbsorptionAmount();
    }

    public static String getHealthStr(LivingEntity entity, boolean accountDead) {
        float completeHealth = getCompleteHealth(entity);
        if (accountDead && !entity.isAlive()) {
            completeHealth = 0;
        }
        return getColorForHealth(entity.getHealth() / entity.getMaxHealth(), completeHealth);
    }

    public static int getTool(Block block) {
        float n = 1.0f;
        int n2 = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = Mc.player().getInventory().getItem(i);
            if (!stack.isEmpty()) {
                final float a = getEfficiency(stack, block);
                if (a > n) {
                    n = a;
                    n2 = i;
                }
            }
        }
        return n2;
    }

    public static boolean onLadder(Entity entity) {
        int posX = Mth.floor(entity.getX());
        int posY = Mth.floor(entity.getY() - 0.20000000298023224D);
        int posZ = Mth.floor(entity.getZ());
        BlockPos blockpos = new BlockPos(posX, posY, posZ);
        Block block1 = Mc.level().getBlockState(blockpos).getBlock();
        return block1 instanceof LadderBlock && !entity.onGround();
    }

    public static float getEfficiency(final ItemStack itemStack, final Block block) {
        float getStrVsBlock = itemStack.getDestroySpeed(block.defaultBlockState());
        if (getStrVsBlock > 1.0f) {
            var reg = Mc.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
            final int getEnchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(reg.get(Enchantments.EFFICIENCY).orElseThrow(), itemStack);
            if (getEnchantmentLevel > 0) {
                getStrVsBlock += getEnchantmentLevel * getEnchantmentLevel + 1;
            }
        }
        return getStrVsBlock;
    }

    public static boolean isEnemy(Player entityPlayer) {
        return !enemies.isEmpty() && enemies.contains(entityPlayer.getGameProfile().name().toLowerCase());
    }

    public static boolean isEnemy(String name) {
        return !enemies.isEmpty() && enemies.contains(name.toLowerCase());
    }

    public static String getColorForHealth(double n, double n2) {
        double health = round(n2, 1);
        return ((n < 0.3) ? "\u00A7c" : ((n < 0.5) ? "\u00A76" : ((n < 0.7) ? "\u00A7e" : "\u00A7a"))) + asWholeNum(health);
    }

    public static int getColorForHealth(double health) {
        return ((health < 0.3) ? -43691 : ((health < 0.5) ? -22016 : ((health < 0.7) ? -171 : -11141291)));
    }

    public static String formatColor(String txt) {
        return txt.replaceAll("&", "\u00A7");
    }

    public static String getFirstColorCode(String input) {
        if (input == null || input.length() < 2) {
            return "";
        }
        for (int i = 0; i < input.length() - 1; i++) {
            if (input.charAt(i) == '\u00A7') {
                char c = input.charAt(i + 1);
                if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F')) {
                    return "\u00A7" + c;
                }
            }
        }
        return "";
    }

    public static int getBoldWidth(String string) {
        boolean bold = false;
        int additionalWidth = 0;
        for (int i = 0; i < string.length(); ++i) {
            char c0 = string.charAt(i);
            if (c0 == '\u00A7' && i + 1 < string.length()) {
                int i2 = "0123456789abcdefklmnor".indexOf(string.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                if (i2 == 17) {
                    bold = true;
                }
                ++i;
            } else if (bold) {
                ++additionalWidth;
            }
        }
        return additionalWidth;
    }

    public static void correctValue(SliderSetting c, SliderSetting d) {
        if (c.getInput() > d.getInput()) {
            double p = c.getInput();
            c.setValueWithEvent(d.getInput());
            d.setValueWithEvent(p);
        }
    }

    public static String generateRandomString(final int n) {
        final char[] array = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        final StringBuilder sb = new StringBuilder();
        IntStream.range(0, n).forEach(p2 -> sb.append(array[rand.nextInt(array.length)]));
        return sb.toString();
    }

    public static boolean isFriended(Player entityPlayer) {
        return !friends.isEmpty() && friends.contains(entityPlayer.getGameProfile().name().toLowerCase());
    }

    public static boolean isFriended(String name) {
        return !friends.isEmpty() && friends.contains(name.toLowerCase());
    }

    public static double getRandomValue(SliderSetting a, SliderSetting b, Random r) {
        return a.getInput() == b.getInput() ? a.getInput() : a.getInput() + r.nextDouble() * (b.getInput() - a.getInput());
    }

    public static boolean nullCheck() {
        return Mc.nullCheck();
    }

    public static boolean isHypixel() {
        return !Mc.mc().hasSingleplayerServer()
                && Mc.mc().getCurrentServer() != null
                && Mc.mc().getCurrentServer().ip.contains("hypixel.net");
    }

    public static String getHitsToKillStr(final Player entityPlayer, final ItemStack itemStack) {
        final int n = (int) Math.ceil(getHitsToKill(entityPlayer, itemStack));
        return "\u00A7" + ((n <= 1) ? "c" : ((n <= 3) ? "6" : ((n <= 5) ? "e" : "a"))) + n;
    }

    public static double getHitsToKill(final Player target, final ItemStack usedItem) {
        double heldItemDamageLevel = 1.0;
        if (!usedItem.isEmpty() && (usedItem.getItem() == Items.DIAMOND_SWORD || usedItem.getItem() instanceof AxeItem)) {
            heldItemDamageLevel += getDamageLevel(usedItem);
        }
        double armorProtPercentage = 0.0;
        double totalEPF = 0.0;
        var reg = Mc.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        for (net.minecraft.world.entity.EquipmentSlot slot : net.minecraft.world.entity.EquipmentSlot.values()) {
            if (!slot.isArmor()) {
                continue;
            }
            final ItemStack stack = target.getItemBySlot(slot);
            if (!stack.isEmpty()) {
                armorProtPercentage += getArmorValue(stack) * 0.04;
                final int protLevel = EnchantmentHelper.getItemEnchantmentLevel(reg.get(Enchantments.PROTECTION).orElseThrow(), stack);
                if (protLevel != 0) {
                    final double epf = Math.floor(0.75 * (6 + protLevel * protLevel) / 3.0);
                    totalEPF += epf;
                }
            }
        }
        totalEPF = 0.04 * Math.min(Math.ceil(Math.min(totalEPF, 25.0) * 0.75), 20.0);
        final double armorReduction = armorProtPercentage + totalEPF * (1.0 - armorProtPercentage);
        final double damage = heldItemDamageLevel * (1.0 - armorReduction);
        return round(getCompleteHealth(target) / damage, 1);
    }

    public static float n() {
        return ae(Mc.player().getYRot(), Utils.getMovementForward(), Utils.getMovementSideways());
    }

    public static String extractFileName(String name) {
        int firstIndex = name.indexOf("_");
        int lastIndex = name.lastIndexOf("_");

        if (firstIndex != -1 && lastIndex != -1 && lastIndex > firstIndex) {
            return name.substring(firstIndex + 1, lastIndex);
        }
        return name;
    }

    public static int mergeAlpha(int color, int alpha) {
        return (color & 0xFFFFFF) | alpha << 24;
    }

    public static int clamp(int n) {
        if (n > 255) {
            return 255;
        }
        if (n < 4) {
            return 4;
        }
        return n;
    }

    public static boolean hasArrows(ItemStack stack) {
        var reg = Mc.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        final boolean flag = Mc.player().isCreative()
                || EnchantmentHelper.getItemEnchantmentLevel(reg.get(Enchantments.INFINITY).orElseThrow(), stack) > 0;
        return flag || Mc.player().getInventory().contains(stack1 -> stack1.is(Items.ARROW));
    }

    public static int darkenColor(int color, double percent) {
        int alpha = (color >> 24) & 0xFF;
        int redVal = (color >> 16) & 0xFF;
        int greenVal = (color >> 8) & 0xFF;
        int blueVal = color & 0xFF;

        percent = (100 - percent) / 100;

        redVal = (int) (redVal * percent);
        greenVal = (int) (greenVal * percent);
        blueVal = (int) (blueVal * percent);

        redVal = clamp(redVal);
        greenVal = clamp(greenVal);
        blueVal = clamp(blueVal);

        return (alpha << 24) | (redVal << 16) | (greenVal << 8) | blueVal;
    }

    public static boolean isTeamMate(Entity entity) {
        try {
            if (false && Mc.player().getDisplayName().getString().startsWith(entity.getDisplayName().getString().substring(0, 2))
                    || getNetworkDisplayName().startsWith(entity.getDisplayName().getString().substring(0, 2))) {
                return true;
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    public static String getNetworkDisplayName() {
        try {
            PlayerInfo playerInfo = Mc.mc().getConnection().getPlayerInfo(Mc.player().getUUID());
            if (playerInfo != null) {
                return PlayerTeam.formatNameForTeam(playerInfo.getTeam(), net.minecraft.network.chat.Component.literal(playerInfo.getProfile().name())).getString();
            }
        } catch (Exception ignored) {
        }
        return "";
    }

    public static void setSpeed(double n) {
        if (n == 0.0) {
            Mc.player().setDeltaMovement(0.0, Mc.player().getDeltaMovement().y, 0.0);
            return;
        }
        float n3 = n();
        Mc.player().setDeltaMovement(-Math.sin(n3) * n, Mc.player().getDeltaMovement().y, Math.cos(n3) * n);
    }

    public static void resetTimer() {
        TimerHelper.reset();
    }

    public static boolean inInventory() {
        if (!nullCheck()) {
            return false;
        }
        return Mc.mc().screen instanceof InventoryScreen
                && Mc.player().containerMenu instanceof InventoryMenu;
    }

    public static int getSkyWarsStatus() {
        List<String> sidebar = getSidebarLines();
        if (sidebar == null || sidebar.isEmpty()) {
            return -1;
        }
        if (stripColor(sidebar.get(0)).startsWith("SKYWARS")) {
            for (String line : sidebar) {
                line = stripColor(line);
                if (line.equals("Waiting...") || line.startsWith("Starting in ")) {
                    return 1;
                } else if (line.startsWith("Players left: ")) {
                    return 2;
                }
            }
            return 0;
        }
        return -1;
    }

    public static String getString(final JsonObject type, final String member) {
        try {
            return type.get(member).getAsString();
        } catch (Exception er) {
            return "";
        }
    }

    public static int getBedwarsStatus() {
        if (!nullCheck()) {
            return -1;
        }
        final Scoreboard scoreboard = Mc.level().getScoreboard();
        if (scoreboard == null) {
            return -1;
        }
        final Objective objective = scoreboard.getDisplayObjective(DisplaySlot.SIDEBAR);
        if (objective == null || !stripString(objective.getDisplayName().getString()).contains("BED WARS")) {
            return -1;
        }
        for (String line : getSidebarLines()) {
            line = stripString(line);
            String[] parts = line.split("  ");
            if (parts.length > 1) {
                if (parts[1].startsWith("L")) {
                    return 0;
                }
            } else if (line.equals("Waiting...") || line.startsWith("Starting in")) {
                return 1;
            } else if (line.startsWith("R Red:") || line.startsWith("B Blue:")) {
                return 2;
            }
        }
        return -1;
    }

    public static String stripString(final String s) {
        final char[] nonValidatedString = s.replaceAll("§.", "").toCharArray();
        final StringBuilder validated = new StringBuilder();
        for (final char c : nonValidatedString) {
            if (c < '\u007f' && c > '\u0014') {
                validated.append(c);
            }
        }
        return validated.toString();
    }

    public static List<String> getSidebarLines() {
        final List<String> lines = new ArrayList<>();
        if (Mc.level() == null) {
            return lines;
        }
        final Scoreboard scoreboard = Mc.level().getScoreboard();
        if (scoreboard == null) {
            return lines;
        }
        final Objective objective = scoreboard.getDisplayObjective(DisplaySlot.SIDEBAR);
        if (objective == null) {
            return lines;
        }
        Collection<PlayerScoreEntry> scores = scoreboard.listPlayerScores(objective);
        final List<PlayerScoreEntry> list = new ArrayList<>();
        for (final PlayerScoreEntry input : scores) {
            if (input.owner() != null && !input.owner().startsWith("#")) {
                list.add(input);
            }
        }
        if (list.size() > 15) {
            scores = new ArrayList<>(Lists.newArrayList(Iterables.skip(list, list.size() - 15)));
        } else {
            scores = list;
        }
        int index = 0;
        for (final PlayerScoreEntry score : scores) {
            ++index;
            final Team team = scoreboard.getPlayerTeam(score.owner());
            lines.add(PlayerTeam.formatNameForTeam(team, net.minecraft.network.chat.Component.literal(score.owner())).getString());
            if (index == scores.size()) {
                lines.add(objective.getDisplayName().getString());
            }
        }
        Collections.reverse(lines);
        return lines;
    }

    public static Random getRandom() {
        return rand;
    }

    public static boolean isMoving() {
        return Utils.getMovementForward() != 0.0F || Utils.getMovementSideways() != 0.0F;
    }

    public static void aim(Entity en, float ps, boolean pc) {
        if (en != null) {
            float[] t = getRotationsOld(en);
            if (t != null) {
                float y = t[0];
                float p = t[1] + 4.0F + ps;
                if (pc) {
                    Mc.mc().getConnection().send(new ServerboundMovePlayerPacket.Rot(y, p, Mc.player().onGround(), false));
                } else {
                    Mc.player().setYRot(y);
                    Mc.player().setXRot(p);
                }
            }
        }
    }

    public static float[] getRotationsOld(Entity q) {
        if (q == null) {
            return null;
        }
        double diffX = q.getX() - Mc.player().getX();
        double diffY;
        if (q instanceof LivingEntity en) {
            diffY = en.getY() + (double) en.getEyeHeight() * 0.9D - (Mc.player().getY() + (double) Mc.player().getEyeHeight());
        } else {
            diffY = (q.getBoundingBox().minY + q.getBoundingBox().maxY) / 2.0D - (Mc.player().getY() + (double) Mc.player().getEyeHeight());
        }

        double diffZ = q.getZ() - Mc.player().getZ();
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) (-(Math.atan2(diffY, dist) * 180.0D / Math.PI));
        return new float[]{
                Mc.player().getYRot() + Mth.wrapDegrees(yaw - Mc.player().getYRot()),
                Mc.player().getXRot() + Mth.wrapDegrees(pitch - Mc.player().getXRot())
        };
    }

    public static double n(Entity en) {
        return ((Mc.player().getYRot() - getYaw(en)) % 360.0D + 540.0D) % 360.0D - 180.0D;
    }

    public static float getYaw(Entity ent) {
        double x = ent.getX() - Mc.player().getX();
        double z = ent.getZ() - Mc.player().getZ();
        double yaw = Math.atan2(x, z) * 57.29577951308232;
        return (float) (yaw * -1.0D);
    }

    public static void ss(double s, boolean m) {
        if (!m || isMoving()) {
            Mc.player().setDeltaMovement(-Math.sin(gd()) * s, Mc.player().getDeltaMovement().y, Math.cos(gd()) * s);
        }
    }

    public static boolean keysDown() {
        return isBindDown(Mc.mc().options.keyUp)
                || isBindDown(Mc.mc().options.keyDown)
                || isBindDown(Mc.mc().options.keyLeft)
                || isBindDown(Mc.mc().options.keyRight);
    }

    public static boolean jumpDown() {
        return isBindDown(Mc.mc().options.keyJump);
    }

    private static boolean isBindDown(KeyMapping bind) {
        return bind.isDown();
    }

    public static double distanceToGround(Entity entity) {
        if (entity.onGround()) {
            return 0;
        }
        double fallDistance = -1;
        double y = entity.getY();
        if (entity.getY() % 1 == 0) {
            y--;
        }
        for (int i = (int) Math.floor(y); i > -1; i--) {
            if (!isPlaceable(new BlockPos(Mth.floor(entity.getX()), i, Mth.floor(entity.getZ())))) {
                fallDistance = y - i;
                break;
            }
        }
        return fallDistance - 1;
    }

    public static float gd() {
        float yw = Mc.player().getYRot();
        if (Utils.getMovementForward() < 0.0F) {
            yw += 180.0F;
        }

        float f;
        if (Utils.getMovementForward() < 0.0F) {
            f = -0.5F;
        } else if (Utils.getMovementForward() > 0.0F) {
            f = 0.5F;
        } else {
            f = 1.0F;
        }

        if (Utils.getMovementSideways() > 0.0F) {
            yw -= 90.0F * f;
        }

        if (Utils.getMovementSideways() < 0.0F) {
            yw += 90.0F * f;
        }

        yw *= 0.017453292F;
        return yw;
    }

    public static float ae(float n, float n2, float n3) {
        float n4 = 1.0f;
        if (n2 < 0.0f) {
            n += 180.0f;
            n4 = -0.5f;
        } else if (n2 > 0.0f) {
            n4 = 0.5f;
        }
        if (n3 > 0.0f) {
            n -= 90.0f * n4;
        } else if (n3 < 0.0f) {
            n += 90.0f * n4;
        }
        return n * 0.017453292f;
    }

    public static double getHorizontalSpeed() {
        return getHorizontalSpeed(Mc.player());
    }

    public static double getHorizontalSpeed(Entity entity) {
        Vec3 vel = entity.getDeltaMovement();
        return Math.sqrt(vel.x * vel.x + vel.z * vel.z);
    }

    public static List<String> getTopLevelLines(String fileContents) {
        List<String> topLevelLines = new ArrayList<>();
        String[] lines = fileContents.split("\\r?\\n");
        int braceLevel = 0;
        boolean inBlockComment = false;

        for (String line : lines) {
            String originalLine = line;
            String processedLine = line.trim();

            if (inBlockComment) {
                if (processedLine.contains("*/")) {
                    inBlockComment = false;
                    processedLine = processedLine.substring(processedLine.indexOf("*/") + 2).trim();
                } else {
                    continue;
                }
            }

            if (processedLine.startsWith("//")) {
                continue;
            }

            if (processedLine.contains("/*")) {
                inBlockComment = true;
                processedLine = processedLine.substring(0, processedLine.indexOf("/*")).trim();
                if (processedLine.isEmpty()) {
                    continue;
                }
            }

            if (processedLine.contains("//")) {
                processedLine = processedLine.substring(0, processedLine.indexOf("//")).trim();
            }

            if (processedLine.contains("/*") && processedLine.contains("*/")) {
                processedLine = processedLine.substring(0, processedLine.indexOf("/*")) + processedLine.substring(processedLine.indexOf("*/") + 2);
                processedLine = processedLine.trim();
            }

            if (processedLine.isEmpty()) {
                continue;
            }

            String lineWithoutStrings = removeStringLiterals(processedLine);

            int openBraces = 0;
            int closeBraces = 0;
            for (char ch : lineWithoutStrings.toCharArray()) {
                if (ch == '{') {
                    openBraces++;
                } else if (ch == '}') {
                    closeBraces++;
                }
            }
            braceLevel += openBraces - closeBraces;

            if (braceLevel == 0 && !processedLine.contains("{") && !processedLine.contains("}") && !processedLine.startsWith("@")) {
                topLevelLines.add(originalLine.trim());
            }
        }

        return topLevelLines;
    }

    public static boolean holdingEdible(ItemStack stack) {
        FoodProperties food = stack.get(DataComponents.FOOD);
        if (food != null && Mc.player().getFoodData().getFoodLevel() == 20) {
            return food.canAlwaysEat();
        }
        return true;
    }

    private static String removeStringLiterals(String line) {
        StringBuilder sb = new StringBuilder();
        boolean inString = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '\"' && (i == 0 || line.charAt(i - 1) != '\\')) {
                inString = !inString;
                continue;
            }
            if (!inString) {
                sb.append(ch);
            }
        }

        return sb.toString();
    }

    public static boolean blockAbove() {
        return !Mc.level().getBlockState(BlockPos.containing(Mc.player().getX(), Mc.player().getY() + 2, Mc.player().getZ())).isAir();
    }

    public static boolean onEdge() {
        return onEdge(Mc.player());
    }

    public static boolean onEdge(Entity entity) {
        Vec3 vel = entity.getDeltaMovement();
        return Mc.level().getEntityCollisions(entity, entity.getBoundingBox().inflate(vel.x / 3.0D, -1.0D, vel.z / 3.0D)).isEmpty();
    }

    public static boolean lookingAtBlock() {
        return Mc.mc().hitResult != null
                && Mc.mc().hitResult.getType() == HitResult.Type.BLOCK
                && Mc.mc().hitResult instanceof BlockHitResult blockHit
                && blockHit.getBlockPos() != null;
    }

    public static boolean isDiagonal(boolean strict) {
        float yaw = ((Mc.player().getYRot() % 360) + 360) % 360;
        yaw = yaw > 180 ? yaw - 360 : yaw;
        boolean isYawDiagonal = inBetween(-170, 170, yaw) && !inBetween(-10, 10, yaw) && !inBetween(80, 100, yaw) && !inBetween(-100, -80, yaw);
        if (strict) {
            isYawDiagonal = inBetween(-178.5, 178.5, yaw) && !inBetween(-1.5, 1.5, yaw) && !inBetween(88.5, 91.5, yaw) && !inBetween(-91.5, -88.5, yaw);
        }
        boolean isStrafing = isBindDown(Mc.mc().options.keyLeft) || isBindDown(Mc.mc().options.keyRight);
        return isYawDiagonal || isStrafing;
    }

    public static double gbps(Entity en, int d) {
        double x = en.getX() - en.xOld;
        double z = en.getZ() - en.zOld;
        double sp = Math.sqrt(x * x + z * z) * 20.0D;
        if (d == 0) {
            return sp;
        }
        return round(sp, d);
    }

    public static boolean inBetween(double min, double max, double value) {
        return value >= min && value <= max;
    }

    public static String removeFormatCodes(String str) {
        return str.replace("\u00A7k", "").replace("\u00A7l", "").replace("\u00A7m", "").replace("\u00A7n", "").replace("\u00A7o", "").replace("\u00A7r", "");
    }

    public static boolean isClicking() {
        if (ModuleManager.autoClicker.isEnabled() && AutoClicker.leftClick.isToggled()) {
            return GLFW.glfwGetMouseButton(Mc.mc().getWindow().handle(), GLFW.GLFW_MOUSE_BUTTON_1) == GLFW.GLFW_PRESS;
        }
        return CPSCalculator.f() > 1 && System.currentTimeMillis() - CPSCalculator.LL < 300L;
    }

    public static boolean isEdgeOfBlock(final double posX, final double posY, final double posZ) {
        BlockPos pos = new BlockPos(Mth.floor(posX), Mth.floor(posY - ((posY % 1.0 == 0.0) ? 1 : 0)), Mth.floor(posZ));
        return Mc.level().getBlockState(pos).isAir();
    }

    public static boolean isEdgeOfBlock() {
        BlockPos pos = new BlockPos(
                Mth.floor(Mc.player().getX()),
                Mth.floor(Mc.player().getY() - ((Mc.player().getY() % 1.0 == 0.0) ? 1 : 0)),
                Mth.floor(Mc.player().getZ())
        );
        return Mc.level().getBlockState(pos).isAir();
    }

    public static long timeBetween(long val, long val2) {
        return Math.abs(val2 - val);
    }

    public static void sendModuleMessage(Module module, String s) {
        sendRawMessage("&3" + module.getName() + "&7: &r" + s);
    }

    public static LivingEntity raytrace(int range) {
        return null;
    }

    public static int getChroma(long speed, long... delay) {
        long time = System.currentTimeMillis() + (delay.length > 0 ? delay[0] : 0L);
        return Color.getHSBColor((float) (time % (15000L / speed)) / (15000.0F / (float) speed), 1.0F, 1.0F).getRGB();
    }

    public static double round(double n, int d) {
        if (d == 0) {
            return Math.round(n);
        }
        double p = Math.pow(10.0D, d);
        return Math.round(n * p) / p;
    }

    public static String stripColor(final String s) {
        if (s.isEmpty()) {
            return s;
        }
        final char[] array = s.replaceAll("§.", "").toCharArray();
        final StringBuilder sb = new StringBuilder();
        for (final char c : array) {
            if (c < '\u007f' && c > '\u0014') {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static void addToClipboard(String string) {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection stringSelection = new StringSelection(string);
            clipboard.setContents(stringSelection, null);
        } catch (Exception e) {
            sendMessage("&cFailed to copy &b" + string);
        }
    }

    public static List<String> gsl() {
        List<String> lines = new ArrayList<>();
        if (Mc.level() == null) {
            return lines;
        }
        Scoreboard scoreboard = Mc.level().getScoreboard();
        if (scoreboard == null) {
            return lines;
        }
        Objective objective = scoreboard.getDisplayObjective(DisplaySlot.SIDEBAR);
        if (objective == null) {
            return lines;
        }
        Collection<PlayerScoreEntry> scores = scoreboard.listPlayerScores(objective);
        List<PlayerScoreEntry> list = new ArrayList<>();
        for (PlayerScoreEntry score : scores) {
            if (score.owner() != null && !score.owner().startsWith("#")) {
                list.add(score);
            }
        }

        if (list.size() > 15) {
            scores = Lists.newArrayList(Iterables.skip(list, scores.size() - 15));
        } else {
            scores = list;
        }

        for (PlayerScoreEntry score : scores) {
            Team team = scoreboard.getPlayerTeam(score.owner());
            lines.add(PlayerTeam.formatNameForTeam(team, net.minecraft.network.chat.Component.literal(score.owner())).getString());
        }

        return lines;
    }

    public static void rsa() {
        LocalPlayer p = Mc.player();
        int armSwingEnd = p.hasEffect(net.minecraft.world.effect.MobEffects.HASTE)
                ? 6 - (1 + p.getEffect(net.minecraft.world.effect.MobEffects.HASTE).getAmplifier())
                : (p.hasEffect(net.minecraft.world.effect.MobEffects.MINING_FATIGUE)
                ? 6 + (1 + p.getEffect(net.minecraft.world.effect.MobEffects.MINING_FATIGUE).getAmplifier()) * 2
                : 6);
        if (!p.swinging || p.swingTime >= armSwingEnd / 2 || p.swingTime < 0) {
            p.swingTime = -1;
            p.swinging = true;
        }
    }

    public static String uf(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static boolean isPlaceable(BlockPos blockPos) {
        return Mc.level().getBlockState(blockPos).canBeReplaced();
    }

    public static boolean spectatorCheck() {
        ItemStack slot8 = Mc.player().getInventory().getItem(8);
        return !slot8.isEmpty() && slot8.getHoverName().getString().contains("Return");
    }

    public static boolean holdingWeapon() {
        return holdingWeapon(Mc.player());
    }

    public static boolean holdingWeapon(LivingEntity entityLivingBase) {
        ItemStack stack = entityLivingBase.getMainHandItem();
        if (stack.isEmpty()) {
            return false;
        }
        Item getItem = stack.getItem();
        return stack.is(ItemTags.SWORDS)
                || (Settings.weaponAxe.isToggled() && getItem instanceof AxeItem)
                || (Settings.weaponRod.isToggled() && getItem instanceof FishingRodItem)
                || (Settings.weaponStick.isToggled() && getItem == Items.STICK);
    }

    public static boolean holdingSword() {
        return !Mc.player().getMainHandItem().isEmpty()
                && Mc.player().getMainHandItem().is(ItemTags.SWORDS);
    }

    public static double getDamageLevel(ItemStack itemStack) {
        double baseDamage = getAttackDamage(itemStack);
        var reg = Mc.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        int sharpLevel = EnchantmentHelper.getItemEnchantmentLevel(reg.get(Enchantments.SHARPNESS).orElseThrow(), itemStack);
        int fireLevel = EnchantmentHelper.getItemEnchantmentLevel(reg.get(Enchantments.FIRE_ASPECT).orElseThrow(), itemStack);
        return baseDamage + sharpLevel * 1.25 + (fireLevel * 4 - 1);
    }

    public static boolean canBePlaced(BlockItem itemBlock) {
        Block block = itemBlock.getBlock();
        return block != null && block.defaultBlockState().canBeReplaced();
    }

    public static <E extends Enum<E>> E getEnum(Class<E> enumClass, String value) {
        for (E enumConstant : enumClass.getEnumConstants()) {
            if (enumConstant.name().equals(value)) {
                return enumConstant;
            }
        }
        return null;
    }

    public static int getSpeedAmplifier() {
        if (Mc.player().hasEffect(net.minecraft.world.effect.MobEffects.SPEED)) {
            return 1 + Mc.player().getEffect(net.minecraft.world.effect.MobEffects.SPEED).getAmplifier();
        }
        return 0;
    }

    public static ItemStack getSpoofedItem(ItemStack original) {
        return original;
    }

    public static boolean scaffoldDiagonal(boolean strict) {
        float back = Mth.wrapDegrees(Mc.player().getYRot());
        float yaw = ((back % 360) + 360) % 360;
        yaw = yaw > 180 ? yaw - 360 : yaw;
        boolean isYawDiagonal = inBetween(-170, 170, yaw) && !inBetween(-10, 10, yaw) && !inBetween(80, 100, yaw) && !inBetween(-100, -80, yaw);
        if (strict) {
            isYawDiagonal = inBetween(-178.5, 178.5, yaw) && !inBetween(-1.5, 1.5, yaw) && !inBetween(88.5, 91.5, yaw) && !inBetween(-91.5, -88.5, yaw);
        }
        return isYawDiagonal;
    }

    public static String readInputStream(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static boolean isLobby() {
        if (isHypixel()) {
            List<String> sidebarLines = getSidebarLines();
            if (!sidebarLines.isEmpty()) {
                String[] parts = stripColor(sidebarLines.get(1)).split("  ");
                if (parts.length > 1 && parts[1].charAt(0) == 'L') {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isBedwarsPracticeOrReplay() {
        if (isHypixel()) {
            if (!nullCheck()) {
                return false;
            }
            final Scoreboard scoreboard = Mc.level().getScoreboard();
            if (scoreboard == null) {
                return false;
            }
            final Objective objective = scoreboard.getDisplayObjective(DisplaySlot.SIDEBAR);
            if (objective == null) {
                return false;
            }
            String stripped = stripString(objective.getDisplayName().getString());
            return stripped.contains("BED WARS PRACTICE") || stripped.contains("REPLAY");
        }
        return false;
    }

    private static double getAttackDamage(ItemStack stack) {
        ItemAttributeModifiers component = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);
        if (component != null) {
            for (ItemAttributeModifiers.Entry entry : component.modifiers()) {
                if (entry.attribute().is(Attributes.ATTACK_DAMAGE)) {
                    return entry.modifier().amount();
                }
            }
        }
        return 0.0;
    }

    private static double getArmorValue(ItemStack stack) {
        ItemAttributeModifiers component = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);
        if (component != null) {
            for (ItemAttributeModifiers.Entry entry : component.modifiers()) {
                if (entry.attribute().is(Attributes.ARMOR)) {
                    return entry.modifier().amount();
                }
            }
        }
        return 0.0;
    }

    public static org.joml.Vector2f worldToScreen(Vec3 pos) {
        return RenderUtils.worldToScreen(pos);
    }

    public static java.util.List<net.minecraft.world.level.block.entity.BlockEntity> getLoadedBlockEntities() {
        if (!nullCheck()) {
            return java.util.Collections.emptyList();
        }
        return java.util.Collections.emptyList();
    }
}
