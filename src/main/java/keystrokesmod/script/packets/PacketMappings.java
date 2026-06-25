package keystrokesmod.script.packets;

import java.util.LinkedHashMap;
import java.util.Map;

import keystrokesmod.script.packets.clientbound.*;
import keystrokesmod.script.packets.serverbound.*;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
import net.minecraft.network.protocol.common.ServerboundPongPacket;
import net.minecraft.network.protocol.game.*;

public class PacketMappings {
    public static final Map<Class<? extends Packet<?>>, Class<? extends CPacket>> minecraftToScriptC = new LinkedHashMap<>();
    public static final Map<Class<? extends Packet<?>>, Class<? extends SPacket>> minecraftToScriptS = new LinkedHashMap<>();

    static {
        minecraftToScriptC.put(ServerboundSwingPacket.class, C0A.class);
        minecraftToScriptC.put(ServerboundPlayerCommandPacket.class, C0B.class);
        minecraftToScriptC.put(ServerboundChatPacket.class, C01.class);
        minecraftToScriptC.put(ServerboundInteractPacket.class, C02.class);
        minecraftToScriptC.put(ServerboundPongPacket.class, C0F.class);
        minecraftToScriptC.put(ServerboundContainerClickPacket.class, C0E.class);
        minecraftToScriptC.put(ServerboundMovePlayerPacket.class, C03.class);
        minecraftToScriptC.put(ServerboundPlayerActionPacket.class, C07.class);
        minecraftToScriptC.put(ServerboundUseItemOnPacket.class, C08.class);
        minecraftToScriptC.put(ServerboundUseItemPacket.class, C08.class);
        minecraftToScriptC.put(ServerboundSetCarriedItemPacket.class, C09.class);
        minecraftToScriptC.put(ServerboundSetCreativeModeSlotPacket.class, C10.class);
        minecraftToScriptC.put(ServerboundPlayerAbilitiesPacket.class, C13.class);
        minecraftToScriptC.put(ServerboundClientCommandPacket.class, C16.class);
        minecraftToScriptC.put(ServerboundContainerClosePacket.class, C0D.class);

        minecraftToScriptS.put(ClientboundSetEntityMotionPacket.class, S12.class);
        minecraftToScriptS.put(ClientboundExplodePacket.class, S27.class);
        minecraftToScriptS.put(ClientboundSetPlayerTeamPacket.class, S3E.class);
        minecraftToScriptS.put(ClientboundPlayerPositionPacket.class, S08.class);
        minecraftToScriptS.put(ClientboundLevelParticlesPacket.class, S2A.class);
        minecraftToScriptS.put(ClientboundBlockDestructionPacket.class, S25.class);
        minecraftToScriptS.put(ClientboundSetHealthPacket.class, S06.class);
        minecraftToScriptS.put(ClientboundBlockUpdatePacket.class, S23.class);
        minecraftToScriptS.put(ClientboundSoundPacket.class, S29.class);
        minecraftToScriptS.put(ClientboundContainerSetSlotPacket.class, S2F.class);
        minecraftToScriptS.put(ClientboundResourcePackPushPacket.class, S48.class);
        minecraftToScriptS.put(ClientboundCommandSuggestionsPacket.class, S3A.class);
        minecraftToScriptS.put(ClientboundSystemChatPacket.class, S02.class);
        minecraftToScriptS.put(ClientboundPlayerChatPacket.class, S02.class);
        minecraftToScriptS.put(ClientboundSetSubtitleTextPacket.class, S45.class);
        minecraftToScriptS.put(ClientboundSetTitleTextPacket.class, S45.class);
        minecraftToScriptS.put(ClientboundAnimatePacket.class, S0B.class);
        minecraftToScriptS.put(ClientboundMoveEntityPacket.class, S14.class);
        minecraftToScriptS.put(ClientboundSetEquipmentPacket.class, S04.class);
    }
}
