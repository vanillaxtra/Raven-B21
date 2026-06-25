package keystrokesmod.utility.shader;

import com.mojang.blaze3d.pipeline.RenderTarget;
import keystrokesmod.utility.Mc;

public final class ShaderUtils {
    private ShaderUtils() {}

    public static void drawQuads() {
        drawQuads(0, 0, 0, 0);
    }

    public static void drawQuads(double x, double y, double width, double height) {
    }

    public static int getFramebufferTextureId(RenderTarget framebuffer) {
        return framebuffer != null ? framebuffer.getColorTextureId() : 0;
    }

    public static void clearFramebuffer(RenderTarget framebuffer) {
        if (framebuffer != null) {
            framebuffer.clear(Mc.mc().isSameThread());
        }
    }

    public static void bindFramebuffer(RenderTarget framebuffer, boolean setViewport) {
        if (framebuffer != null) {
            framebuffer.bindWrite(setViewport);
        }
    }
}
