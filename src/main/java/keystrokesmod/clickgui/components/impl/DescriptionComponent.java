package keystrokesmod.clickgui.components.impl;

import keystrokesmod.clickgui.components.Component;
import keystrokesmod.module.setting.impl.DescriptionSetting;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.Theme;
import net.minecraft.client.gui.GuiGraphics;

public class DescriptionComponent extends Component {
    public DescriptionSetting desc;
    private ModuleComponent p;
    public int o;
    public int x;
    public int y;

    public DescriptionComponent(DescriptionSetting desc, ModuleComponent b, int o) {
        this.desc = desc;
        this.p = b;
        this.x = b.categoryComponent.getX() + b.categoryComponent.getWidth();
        this.y = b.categoryComponent.getY() + b.yPos;
        this.o = o;
    }

    @Override
    public void render(GuiGraphics context) {
        context.pose().pushMatrix();
        context.pose().scale(0.5f, 0.5f);
        context.drawString(Mc.mc().font, this.desc.getDesc(),
                (this.p.categoryComponent.getX() + 4) * 2,
                (this.p.categoryComponent.getY() + this.o + 4) * 2,
                Theme.getGradient(Theme.descriptor[0], Theme.descriptor[1], 0), true);
        context.pose().popMatrix();
    }

    public void updateHeight(int n) {
        this.o = n;
    }
}
