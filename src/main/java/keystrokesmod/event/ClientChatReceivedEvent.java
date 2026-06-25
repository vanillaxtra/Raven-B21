package keystrokesmod.event;

import net.minecraft.network.chat.Component;

public class ClientChatReceivedEvent extends CancelableEvent {
    public Component message;

    public ClientChatReceivedEvent(Component message) {
        this.message = message;
    }
}
