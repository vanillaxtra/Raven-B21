package keystrokesmod.utility;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;

public class PlayerData {
    public double speed;
    public int aboveVoidTicks;
    public int fastTick;
    public int autoBlockTicks;
    public int ticksExisted;
    public int lastSneakTick;
    public double posZ;
    public int sneakTicks;
    public int noSlowTicks;
    public double posY;
    public boolean sneaking;
    public double posX;
    public double serverPosX;
    public double serverPosY;
    public double serverPosZ;

    public void update(Player player) {
        int age = player.tickCount;
        this.posX = player.getX() - player.xOld;
        this.posY = player.getY() - player.yOld;
        this.posZ = player.getZ() - player.zOld;
        this.speed = Math.max(Math.abs(this.posX), Math.abs(this.posZ));
        if (this.speed >= 0.07) {
            ++this.fastTick;
            this.ticksExisted = age;
        } else {
            this.fastTick = 0;
        }
        if (Math.abs(this.posY) >= 0.1) {
            this.aboveVoidTicks = age;
        }
        if (player.isCrouching()) {
            this.lastSneakTick = age;
        }
        if (player.swinging && player.isBlocking()) {
            ++this.autoBlockTicks;
        } else {
            this.autoBlockTicks = 0;
        }
        if (player.isSprinting() && player.isUsingItem()) {
            ++this.noSlowTicks;
        } else {
            this.noSlowTicks = 0;
        }
        if (player.getXRot() >= 70.0f && !player.getMainHandItem().isEmpty() && player.getMainHandItem().getItem() instanceof BlockItem) {
            if (player.attackAnim == 0.1f) {
                if (!this.sneaking && player.isCrouching()) {
                    ++this.sneakTicks;
                } else {
                    this.sneakTicks = 0;
                }
            }
        } else {
            this.sneakTicks = 0;
        }
    }

    public void updateSneak(Player player) {
        this.sneaking = player.isCrouching();
    }

    public void updateServerPos(Player player) {
        this.serverPosX = player.getX();
        this.serverPosY = player.getY();
        this.serverPosZ = player.getZ();
    }
}
