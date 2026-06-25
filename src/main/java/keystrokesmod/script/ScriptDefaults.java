package keystrokesmod.script;

import keystrokesmod.Raven;
import keystrokesmod.module.Module;
import keystrokesmod.script.classes.ScriptVec3;
import keystrokesmod.utility.Mc;

import java.util.LinkedHashMap;

public class ScriptDefaults {
    public static final Bridge bridge = new Bridge();
    private static final LinkedHashMap<String, Module> modulesMap = new LinkedHashMap<>();

    public static void reloadModules() {
        modulesMap.clear();
        if (Raven.getModuleManager() == null) {
            return;
        }
        for (Module module : Raven.getModuleManager().getModules()) {
            modulesMap.put(module.getName(), module);
        }
        if (Raven.scriptManager != null) {
            for (Module module : Raven.scriptManager.scripts.values()) {
                modulesMap.put(module.getName(), module);
            }
        }
    }

    public static class Bridge {
    }

    public static class client {
        public static void async(Runnable runnable) {
            if (runnable != null) {
                runnable.run();
            }
        }

        public static boolean allowFlying() {
            return Mc.player() != null && Mc.player().getAbilities().mayfly;
        }

        public static void removePotionEffect(int id) {
        }

        public static int getUID() {
            return 4;
        }

        public static String getUsername() {
            return Mc.player() != null ? Mc.player().getName().getString() : "";
        }
    }

    public static class world {
        public static ScriptVec3 getBlock(ScriptVec3 pos) {
            return pos;
        }
    }

    public static class modules {
        public final String scriptName;

        public modules(String scriptName) {
            this.scriptName = scriptName;
        }

        public Module get(String name) {
            return modulesMap.get(name);
        }
    }

    public static class gl {
        public static void color(float r, float g, float b, float a) {
        }
    }

    public static class config {
        public static String get(String key) {
            return "";
        }

        public static void set(String key, String value) {
        }
    }

    public static class render {
        public static void text(String text, float x, float y, int color, boolean shadow) {
        }

        public static class blur {
            public static void draw(float strength) {
            }
        }

        public static class bloom {
            public static void draw(float strength) {
            }
        }
    }

    public static class inventory {
        public static int getSlot() {
            return Mc.player() != null ? Mc.player().getInventory().getSelectedSlot() : 0;
        }
    }

    public static class keybinds {
        public static boolean isKeyDown(int key) {
            return false;
        }
    }

    public static class util {
        public static void print(String message) {
        }

        public static void attack() {
            if (Mc.mc() != null) {
                ((keystrokesmod.mixin.impl.accessor.IAccessorMinecraft) Mc.mc()).callStartAttack();
            }
        }

        public static void useItem() {
            if (Mc.mc() != null) {
                ((keystrokesmod.mixin.impl.accessor.IAccessorMinecraft) Mc.mc()).callStartUseItem();
            }
        }
    }
}
