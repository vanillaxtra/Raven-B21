package keystrokesmod.module.impl.combat;

import keystrokesmod.event.RenderTickEvent;
import keystrokesmod.event.SubscribeEvent;
import keystrokesmod.module.Module;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.module.setting.impl.DescriptionSetting;
import keystrokesmod.module.setting.impl.SliderSetting;
import keystrokesmod.utility.BindUtil;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.Utils;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.InteractionHand;
import org.lwjgl.glfw.GLFW;

import java.util.Random;

public class AutoClicker extends Module {
    public SliderSetting minCPS;
    public SliderSetting maxCPS;
    public static ButtonSetting leftClick;
    public ButtonSetting rightClick;
    public ButtonSetting breakBlocks;
    public ButtonSetting weaponOnly;
    public ButtonSetting blocksOnly;
    public ButtonSetting disableCreative;

    private long nextClick;
    private Random rand = new Random();

    public AutoClicker() {
        super("AutoClicker", category.combat, 0);
        this.registerSetting(new DescriptionSetting("Best with delay remover."));
        this.registerSetting(minCPS = new SliderSetting("Min CPS", 9.0, 1.0, 20.0, 0.5));
        this.registerSetting(maxCPS = new SliderSetting("Max CPS", 12.0, 1.0, 20.0, 0.5));
        this.registerSetting(leftClick = new ButtonSetting("Left click", true));
        this.registerSetting(rightClick = new ButtonSetting("Right click", false));
        this.registerSetting(breakBlocks = new ButtonSetting("Break blocks", false));
        this.registerSetting(weaponOnly = new ButtonSetting("Weapon only", false));
        this.registerSetting(blocksOnly = new ButtonSetting("Blocks only", true));
        this.registerSetting(disableCreative = new ButtonSetting("Disable in creative", false));
        this.closetModule = true;
    }

    @SubscribeEvent
    public void onRender(RenderTickEvent e) {
        if (e.phase != RenderTickEvent.Phase.END || !Mc.nullCheck() || mc.screen instanceof AbstractContainerScreen) {
            return;
        }
        if (disableCreative.isToggled() && mc.player.isCreative()) {
            return;
        }
        if (System.currentTimeMillis() < nextClick) {
            return;
        }
        double cps = Utils.randomizeDouble(minCPS.getInput(), maxCPS.getInput());
        nextClick = System.currentTimeMillis() + (long) (1000.0 / cps);
        if (leftClick.isToggled() && BindUtil.isBindDown(GLFW.GLFW_MOUSE_BUTTON_LEFT + 1000)) {
            if (weaponOnly.isToggled() && !Utils.holdingWeapon()) {
                return;
            }
            if (mc.hitResult != null && mc.hitResult.getType() == net.minecraft.world.phys.HitResult.Type.ENTITY) {
                mc.gameMode.attack(mc.player, ((net.minecraft.world.phys.EntityHitResult) mc.hitResult).getEntity());
                mc.player.swing(InteractionHand.MAIN_HAND);
            }
        }
        if (rightClick.isToggled() && BindUtil.isBindDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT + 1000)) {
            if (blocksOnly.isToggled() && !(mc.player.getMainHandItem().getItem() instanceof BlockItem)) {
                return;
            }
            mc.gameMode.useItem(mc.player, InteractionHand.MAIN_HAND);
        }
    }
}
