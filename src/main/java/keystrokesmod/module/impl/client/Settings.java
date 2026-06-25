package keystrokesmod.module.impl.client;

import keystrokesmod.Raven;
import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.DescriptionSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.Identifier;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Settings extends Module {
    public static SliderSetting customCapes;
    public static ButtonSetting weaponAxe;
    public static ButtonSetting weaponRod;
    public static ButtonSetting weaponStick;
    public static ButtonSetting middleClickFriends;
    public static ButtonSetting setChatAsInventory;
    public static ButtonSetting rotateBody;
    public static ButtonSetting fullBody;
    public static ButtonSetting loadGuiPositions;
    public static ButtonSetting sendMessage;
    public static SliderSetting offset;
    public static SliderSetting timeMultiplier;
    public static SliderSetting customThemeColor1;
    public static SliderSetting customThemeColor2;

    private final String[] capes = new String[]{"None", "Anime", "Aqua", "Green", "Purple", "Red", "White", "Yellow", "Myau", "Astolfo", "Vape", "Dash", "Anime2", "DreamySky", "RUBBAH", "Zambos"};
    public static List<Identifier> loadedCapes = new ArrayList<>();

    public Settings() {
        super("Settings", category.client, 0);
        this.registerSetting(new DescriptionSetting("General"));
        this.registerSetting(customCapes = new SliderSetting("Custom cape", 0, capes));
        this.registerSetting(weaponAxe = new ButtonSetting("Set axe as weapon", false));
        this.registerSetting(weaponRod = new ButtonSetting("Set rod as weapon", false));
        this.registerSetting(weaponStick = new ButtonSetting("Set stick as weapon", false));
        this.registerSetting(middleClickFriends = new ButtonSetting("Middle click friends", false));
        this.registerSetting(setChatAsInventory = new ButtonSetting("Set chat as inventory", false));
        this.registerSetting(new DescriptionSetting("Rotations"));
        this.registerSetting(rotateBody = new ButtonSetting("Rotate body", true));
        this.registerSetting(fullBody = new ButtonSetting("Full body", false));
        this.registerSetting(new DescriptionSetting("Profiles"));
        this.registerSetting(loadGuiPositions = new ButtonSetting("Load gui state", false));
        this.registerSetting(sendMessage = new ButtonSetting("Send message on enable", true));
        this.registerSetting(new DescriptionSetting("Theme colors"));
        this.registerSetting(offset = new SliderSetting("Offset", 0.5, -3.0, 3.0, 0.1));
        this.registerSetting(timeMultiplier = new SliderSetting("Time multiplier", 0.5, 0.1, 4.0, 0.1));
        this.registerSetting(customThemeColor1 = new SliderSetting("Custom theme color 1", 0xFFFFFF, 0x000000, 0xFFFFFF, 1));
        this.registerSetting(customThemeColor2 = new SliderSetting("Custom theme color 2", 0xFFFFFF, 0x000000, 0xFFFFFF, 1));
        this.canBeEnabled = false;
        loadCapes();
    }

    public void loadCapes() {
        try {
            for (int i = 1; i < capes.length; i++) {
                String name = capes[i].toLowerCase();
                if (i > 1) {
                    name = "rvn_" + name;
                }
                InputStream stream = Raven.class.getResourceAsStream("/assets/keystrokesmod/textures/capes/" + name + ".png");
                if (stream == null) {
                    continue;
                }
                Identifier id = Identifier.fromNamespaceAndPath("keystrokesmod", "capes/" + name);
                com.mojang.blaze3d.platform.NativeImage nativeImage = com.mojang.blaze3d.platform.NativeImage.read(stream);
                mc.getTextureManager().register(id, new net.minecraft.client.renderer.texture.DynamicTexture(() -> id.toString(), nativeImage));
                loadedCapes.add(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean inInventory() {
        if (mc.screen instanceof InventoryScreen) {
            return true;
        }
        return mc.screen instanceof ChatScreen && setChatAsInventory.isToggled();
    }
}
