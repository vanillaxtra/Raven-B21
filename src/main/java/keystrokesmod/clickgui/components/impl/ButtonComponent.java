package keystrokesmod.clickgui.components.impl;

import keystrokesmod.Raven;
import keystrokesmod.clickgui.components.Component;
import keystrokesmod.module.setting.impl.ButtonSetting;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.profile.ProfileModule;
import net.minecraft.client.gui.GuiGraphics;

import java.awt.*;

public class ButtonComponent extends Component {
    private final int enabledColor = (new Color(20, 255, 0)).getRGB();
    private keystrokesmod.module.Module mod;
    public ButtonSetting buttonSetting;
    private ModuleComponent p;
    public int o;
    public int x;
    private int y;
    public int xOffset;
    public boolean renderLine;

    public ButtonComponent(keystrokesmod.module.Module mod, ButtonSetting op, ModuleComponent b, int o) {
        this.mod = mod;
        this.buttonSetting = op;
        this.p = b;
        this.x = b.categoryComponent.getX() + b.categoryComponent.getWidth();
        this.y = b.categoryComponent.getY() + b.yPos;
        this.o = o;
    }

    @Override
    public void render(GuiGraphics context) {
        var textRenderer = Mc.mc().font;
        context.pose().pushMatrix();
        context.pose().scale(0.5f, 0.5f);
        String prefix = this.buttonSetting.isMethodButton ? "[=]  " : (this.buttonSetting.isToggled() ? "[+]  " : "[-]  ");
        context.drawString(textRenderer, prefix + this.buttonSetting.getName(),
                (this.p.categoryComponent.getX() + 4) * 2 + xOffset,
                (this.p.categoryComponent.getY() + this.o + 4) * 2,
                this.buttonSetting.isToggled() ? this.enabledColor : -1, false);
        context.pose().popMatrix();
    }

    public void updateHeight(int n) {
        this.o = n;
    }

    @Override
    public void drawScreen(int x, int y) {
        this.y = this.p.categoryComponent.getModuleY() + this.o;
        this.x = this.p.categoryComponent.getX();
    }

    @Override
    public boolean onClick(int x, int y, int b) {
        if (this.i(x, y) && b == 0 && this.p.isOpened && this.visible && this.buttonSetting.visible) {
            if (this.buttonSetting.isMethodButton) {
                this.buttonSetting.runMethod();
                return false;
            }
            this.buttonSetting.toggle();
            this.mod.guiButtonToggled(this.buttonSetting);
            if (Raven.currentProfile != null) {
                ((ProfileModule) Raven.currentProfile.getModule()).saved = false;
            }
        }
        return false;
    }

    public boolean i(int x, int y) {
        return x > this.x && x < this.x + this.p.categoryComponent.getWidth() && y > this.y && y < this.y + 11;
    }
}
