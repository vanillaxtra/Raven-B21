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
        return 0;
    }

    public static void clearFramebuffer(RenderTarget framebuffer) {
    }

    public static void bindFramebuffer(RenderTarget framebuffer, boolean setViewport) {
    }
}
