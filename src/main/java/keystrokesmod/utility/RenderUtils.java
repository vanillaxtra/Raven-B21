package keystrokesmod.utility;

import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import org.joml.Matrix4f;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;

import java.awt.Color;

public final class RenderUtils {
    private RenderUtils() {
    }

    public static void renderBlock(BlockPos blockPos, int color, boolean outline, boolean shade) {
    }

    public static void renderChest(BlockPos blockPos, int color, boolean outline, boolean shade) {
    }

    public static void renderBlock(BlockPos blockPos, int color, double y2, boolean outline, boolean shade) {
    }

    public static void drawEntityBox(Entity entity, int rgb, boolean twoD) {
    }

    public static void drawBox(AABB box, int rgb) {
    }

    public static void drawTextWithShadow(Font font, String text, float x, float y, int color) {
        Minecraft minecraft = Mc.mc();
        Matrix4f matrix = new Matrix4f();
        font.drawInBatch(text, x, y, color, true, matrix, minecraft.renderBuffers().bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
        minecraft.renderBuffers().bufferSource().endBatch();
    }

    public static void drawSkeleton(Entity entity, int rgb) {
    }

    public static void drawTracer(Entity entity, int rgb, float width, float tickDelta) {
    }

    public static void drawTrajectory(Vec3 start, Vec3 velocity, boolean highlight) {
    }

    public static Vector2f worldToScreen(Vec3 pos) {
        return null;
    }

    public static void scissor(double x, double y, double width, double height) {
    }

    public static boolean isInViewFrustum(final Entity entity) {
        return false;
    }

    public static void drawRect(double left, double top, double right, double bottom, int color) {
    }

    public static void drawPlayerBoundingBox(Vec3 pos, int color) {
    }

    public static void drawOutline(float x, float y, float x2, float y2, float lineWidth, int color) {
    }

    public static void renderBox(double x, double y, double z, double x2, double y2, double z2, int color, boolean outline, boolean shade) {
    }

    public static void renderBPS(final boolean b, final boolean b2) {
    }

    public static void renderEntity(Entity e, int type, double expand, double shift, int color, boolean damage) {
    }

    public static void drawPolygon(final double n, final double n2, final double n3, final int n4, final int n5) {
    }

    public static void drawBoundingBox(AABB abb, float r, float g, float b) {
    }

    public static void drawBoundingBox(AABB abb, float r, float g, float b, float a) {
    }

    public static void renderBlockModel(BlockState blockState, double x, double y, double z, int color) {
    }

    public static void drawTracerLine(Entity e, int color, float lineWidth, float partialTicks) {
    }

    public static void dGR(int left, int top, int right, int bottom, int startColor, int endColor) {
    }

    public static void db(int w, int h, int r) {
    }

    public static void drawColoredString(String text, char lineSplit, int x, int y, long s, long shift, boolean shadow, Font fontRenderer) {
    }

    public static void drawStringWithShadow(String text, int x, int y, int color) {
    }

    public static void d3p(double x, double y, double z, double radius, int sides, float lineWidth, int color, boolean chroma) {
    }

    public static void drawCaret(float x, float y, int color, double width, double length) {
    }

    public static void drawTriangle(double x, double y, double size, double widthDiv, double heightDiv, int color) {
    }

    public static void glColor(final int n) {
    }

    public static void drawRoundedGradientOutlinedRectangle(float x, float y, float x2, float y2, final float radius, final int n6, final int n7, final int n8) {
    }

    public static void draw2DPolygon(final double x, final double y, final double radius, final int sides, final int color) {
    }

    public static RenderTarget createFrameBuffer(RenderTarget framebuffer) {
        return framebuffer;
    }

    public static RenderTarget createFrameBuffer(RenderTarget framebuffer, boolean depth) {
        return framebuffer;
    }

    public static boolean needsNewFramebuffer(RenderTarget framebuffer) {
        return false;
    }

    public static void bindTexture(int texture) {
    }

    public static void setAlphaLimit(float limit) {
    }

    public static Color interpolateColorC(Color color1, Color color2, float amount) {
        return new Color();
    }

    public static int interpolateInt(int oldValue, int newValue, double interpolationValue) {
        return 0;
    }

    public static Double interpolate(double oldValue, double newValue, double interpolationValue) {
        return oldValue;
    }

    public static void resetColor() {
    }

    public static Vec3 convertTo2D(int scaleFactor, double x, double y, double z) {
        return null;
    }

    public static void drawRoundedRectangle(float x, float y, float x2, float y2, float radius, final int color) {
    }

    public static void drawRectangleGL(float x, float y, float x2, float y2, final int color) {
    }

    public static void drawRoundedGradientRect(float x, float y, float x2, float y2, float radius, final int n6, final int n7, final int n8, final int n9) {
    }

    public static int setAlpha(int rgb, double alpha) {
        return rgb;
    }

    public static void drawGradientRect(int left, int top, float right, int bottom, int startColor, int endColor) {
    }

    public static void drawCircle(double x, double y, double z, double radius, int sides, float lineWidth, int color, boolean chroma) {
    }
}
