package keystrokesmod.clickgui.components.impl;

import keystrokesmod.Raven;
import keystrokesmod.clickgui.components.Component;
import keystrokesmod.module.Module;
import keystrokesmod.module.setting.Setting;
import keystrokesmod.module.setting.impl.*;
import keystrokesmod.utility.Mc;
import keystrokesmod.utility.RenderUtils;
import keystrokesmod.utility.Timer;
import keystrokesmod.utility.Utils;
import keystrokesmod.utility.profile.Manager;
import keystrokesmod.utility.profile.ProfileModule;
import net.minecraft.client.gui.GuiGraphics;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class ModuleComponent extends Component {
    private int originalHoverAlpha = 120;
    private final int hoverColor = (new Color(0, 0, 0, originalHoverAlpha)).getRGB();
    private final int unsavedColor = new Color(114, 188, 250).getRGB();
    private final int invalidColor = new Color(255, 80, 80).getRGB();
    private final int enabledColor = new Color(24, 154, 255).getRGB();
    private final int disabledColor = new Color(192, 192, 192).getRGB();
    public Module mod;
    public CategoryComponent categoryComponent;
    public int yPos;
    public ArrayList<Component> settings;
    public boolean isOpened;
    private boolean hovering;
    private Timer hoverTimer;
    private boolean hoverStarted;
    private Timer smoothTimer;
    private int smoothingY = 16;

    public ModuleComponent(Module mod, CategoryComponent p, int yPos) {
        this.mod = mod;
        this.categoryComponent = p;
        this.yPos = yPos;
        this.settings = new ArrayList<>();
        this.isOpened = false;
        int y = yPos + 12;
        if (mod != null && !mod.getSettings().isEmpty()) {
            for (Setting v : mod.getSettings()) {
                if (!v.visible) {
                    continue;
                }
                if (v instanceof SliderSetting n) {
                    SliderComponent s = new SliderComponent(n, this, y);
                    this.settings.add(s);
                    y += 12;
                } else if (v instanceof ButtonSetting b) {
                    ButtonComponent c = new ButtonComponent(mod, b, this, y);
                    this.settings.add(c);
                    y += 12;
                } else if (v instanceof DescriptionSetting d) {
                    DescriptionComponent m = new DescriptionComponent(d, this, y);
                    this.settings.add(m);
                    y += 12;
                } else if (v instanceof KeySetting setting) {
                    BindComponent keyComponent = new BindComponent(this, setting, y);
                    this.settings.add(keyComponent);
                    y += 12;
                } else if (v instanceof GroupSetting b) {
                    GroupComponent c = new GroupComponent(b, this, y);
                    this.settings.add(c);
                    y += 12;
                }
            }
        }
        this.settings.add(new BindComponent(this, y));
    }

    public void updateHeight(int newY) {
        this.yPos = newY;
        int y = this.yPos + 16;
        for (Component co : this.settings) {
            if (!isVisible(co)) {
                continue;
            }
            co.updateHeight(y);
            if (co instanceof SliderComponent) {
                y += 16;
            } else if (co instanceof ButtonComponent || co instanceof BindComponent || co instanceof DescriptionComponent || co instanceof GroupComponent) {
                y += 12;
            }
        }
    }

    @Override
    public void render(GuiGraphics context) {
        var textRenderer = Mc.mc().font;
        if (hovering || hoverTimer != null) {
            double hoverAlpha = (hovering && hoverTimer != null) ? hoverTimer.getValueFloat(0, originalHoverAlpha, 1)
                    : (hoverTimer != null && !hovering) ? originalHoverAlpha - hoverTimer.getValueFloat(0, originalHoverAlpha, 1)
                    : originalHoverAlpha;
            if (hoverAlpha == 0) {
                hoverTimer = null;
            }
            RenderUtils.drawRoundedRectangle(this.categoryComponent.getX(), this.categoryComponent.getY() + yPos,
                    this.categoryComponent.getX() + this.categoryComponent.getWidth(), this.categoryComponent.getY() + 16 + this.yPos,
                    8, Utils.mergeAlpha(hoverColor, (int) hoverAlpha));
        }

        int buttonRgb = this.mod.isEnabled() ? enabledColor : disabledColor;
        if (this.mod.script != null && this.mod.script.error) {
            buttonRgb = invalidColor;
        }
        if (this.mod.moduleCategory() == Module.category.profiles && !(this.mod instanceof Manager)
                && !((ProfileModule) this.mod).saved && Raven.currentProfile.getModule() == this.mod) {
            buttonRgb = unsavedColor;
        }

        if (smoothTimer != null && System.currentTimeMillis() - smoothTimer.last >= 300) {
            smoothTimer = null;
        }
        if (smoothTimer != null) {
            int height = getModuleHeight();
            if (isOpened) {
                smoothingY = smoothTimer.getValueInt(16, height, 1);
                if (smoothingY == height) {
                    smoothTimer = null;
                }
            } else {
                smoothingY = smoothTimer.getValueInt(height, 16, 1);
                if (smoothingY == 16) {
                    smoothTimer = null;
                }
            }
            this.categoryComponent.updateHeight();
        }

        int nameX = this.categoryComponent.getX() + this.categoryComponent.getWidth() / 2 - textRenderer.width(this.mod.getName()) / 2;
        context.drawString(textRenderer, this.mod.getName(), nameX, this.categoryComponent.getY() + this.yPos + 4, buttonRgb);

        boolean scissorRequired = smoothTimer != null;
        if (scissorRequired) {
            RenderUtils.scissor(this.categoryComponent.getX() - 2, this.categoryComponent.getY() + this.yPos + 4,
                    this.categoryComponent.getWidth() + 4, smoothingY + 4);
        }

        if (this.isOpened || smoothTimer != null) {
            for (Component settingComponent : this.settings) {
                if (!isVisible(settingComponent)) {
                    continue;
                }
                settingComponent.render(context);
            }
        }
    }

    public int getHeight() {
        if (smoothTimer != null) {
            return smoothingY;
        }
        if (!this.isOpened) {
            return 16;
        }
        int h = 16;
        for (Component c : this.settings) {
            if (!isVisible(c)) {
                continue;
            }
            if (c instanceof SliderComponent) {
                h += 16;
            } else if (c instanceof ButtonComponent || c instanceof BindComponent || c instanceof DescriptionComponent || c instanceof GroupComponent) {
                h += 12;
            }
        }
        return h;
    }

    public void onSliderChange() {
        for (Component c : this.settings) {
            if (c instanceof SliderComponent slider) {
                slider.onSliderChange();
            }
        }
    }

    public int getModuleHeight() {
        int h = 16;
        for (Component c : this.settings) {
            if (!isVisible(c)) {
                continue;
            }
            if (c instanceof SliderComponent) {
                h += 16;
            } else if (c instanceof ButtonComponent || c instanceof BindComponent || c instanceof DescriptionComponent || c instanceof GroupComponent) {
                h += 12;
            }
        }
        return h;
    }

    @Override
    public void drawScreen(int x, int y) {
        for (Component c : this.settings) {
            c.drawScreen(x, y);
        }
        if (overModuleName(x, y) && this.categoryComponent.opened) {
            hovering = true;
            if (hoverTimer == null) {
                (hoverTimer = new Timer(75)).start();
                hoverStarted = true;
            }
        } else {
            if (hovering && hoverStarted) {
                (hoverTimer = new Timer(75)).start();
            }
            hoverStarted = false;
            hovering = false;
        }
    }

    public String getName() {
        return mod.getName();
    }

    @Override
    public boolean onClick(int x, int y, int mouse) {
        if (this.overModuleName(x, y) && mouse == 0 && this.mod.canBeEnabled()) {
            this.mod.toggle();
            if (this.mod.moduleCategory() != Module.category.profiles) {
                if (Raven.currentProfile != null) {
                    ((ProfileModule) Raven.currentProfile.getModule()).saved = false;
                }
            }
        }

        if (this.overModuleName(x, y) && mouse == 1) {
            this.isOpened = !this.isOpened;
            (this.smoothTimer = new Timer(200)).start();
            this.categoryComponent.updateHeight();
            return true;
        }

        for (Component settingComponent : this.settings) {
            settingComponent.onClick(x, y, mouse);
        }
        return false;
    }

    @Override
    public void mouseReleased(int x, int y, int m) {
        for (Component c : this.settings) {
            c.mouseReleased(x, y, m);
        }
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        for (Component c : this.settings) {
            c.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public void onScroll(int scroll) {
        for (Component component : this.settings) {
            component.onScroll(scroll);
        }
    }

    @Override
    public void onGuiClosed() {
        for (Component c : this.settings) {
            c.onGuiClosed();
        }
        smoothTimer = null;
        hoverTimer = null;
        smoothingY = getHeight();
    }

    public boolean overModuleName(int x, int y) {
        return x > this.categoryComponent.getX() && x < this.categoryComponent.getX() + this.categoryComponent.getWidth()
                && y > this.categoryComponent.getModuleY() + this.yPos && y < this.categoryComponent.getModuleY() + 16 + this.yPos;
    }

    public void updateSettingPositions(int xOffset) {
        int y = this.yPos + 12;
        for (Component c : this.settings) {
            if (!isVisible(c)) {
                continue;
            }
            if (c instanceof DescriptionComponent desc) {
                desc.o = y;
                y += 12;
            } else if (c instanceof BindComponent bind) {
                bind.o = y;
                if (bind.keySetting != null) {
                    if (xOffset != 0 & isGroupOpened(c, false)) {
                        bind.x += xOffset;
                        bind.xOffset = xOffset;
                    }
                    y += 12;
                }
            } else if (c instanceof SliderComponent slider) {
                slider.o = y;
                if (xOffset != 0 & isGroupOpened(c, false)) {
                    slider.x += xOffset;
                    slider.xOffset = xOffset;
                    slider.renderLine = true;
                } else {
                    slider.renderLine = false;
                }
                y += 16;
            } else if (c instanceof ButtonComponent button) {
                button.o = y;
                if (xOffset != 0 & isGroupOpened(c, false)) {
                    button.x += xOffset;
                    button.xOffset = xOffset;
                    button.renderLine = true;
                } else {
                    button.renderLine = false;
                }
                y += 12;
            }
        }
        this.categoryComponent.updateHeight();
    }

    public boolean isVisible(Component component) {
        if (component instanceof SliderComponent slider) {
            return isGroupOpened(component, slider.sliderSetting.visible);
        }
        if (component instanceof ButtonComponent button) {
            return isGroupOpened(component, button.buttonSetting.visible);
        }
        if (component instanceof DescriptionComponent desc) {
            return desc.desc.visible;
        }
        if (component instanceof BindComponent bind) {
            if (bind.keySetting != null) {
                return isGroupOpened(component, bind.keySetting.visible);
            }
        }
        return true;
    }

    public boolean isGroupOpened(Component component, boolean defaultBool) {
        String groupName = "";
        if (component instanceof SliderComponent slider && slider.sliderSetting.groupSetting != null) {
            groupName = slider.sliderSetting.groupSetting.getName();
        }
        if (component instanceof ButtonComponent button && button.buttonSetting.group != null) {
            groupName = button.buttonSetting.group.getName();
        }
        if (component instanceof BindComponent bind && bind.keySetting != null && bind.keySetting.group != null) {
            groupName = bind.keySetting.group.getName();
        }
        if (groupName.isEmpty()) {
            return defaultBool;
        }
        for (Component c : this.settings) {
            if (c instanceof GroupComponent group) {
                if (group.setting.getName().equals(groupName)) {
                    return group.opened;
                }
            }
        }
        return defaultBool;
    }
}
