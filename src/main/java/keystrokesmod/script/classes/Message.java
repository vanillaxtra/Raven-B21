package keystrokesmod.script.classes;

import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;

import java.net.URI;

public class Message {
    public MutableComponent component;

    public Message(String message) {
        this.component = net.minecraft.network.chat.Component.literal(message);
    }

    public void appendStyle(String style, String action, String styleMessage, String message) {
        MutableComponent styled = net.minecraft.network.chat.Component.literal(message);
        if (style.equals("HOVER")) {
            styled = styled.withStyle(s -> s.withHoverEvent(new HoverEvent.ShowText(net.minecraft.network.chat.Component.literal(styleMessage))));
        }
        else if (style.equals("CLICK")) {
            styled = styled.withStyle(s -> s.withClickEvent(createClickEvent(action, styleMessage)));
        }
        component = component.copy().append(styled);
    }

    public void append(String append) {
        component = component.copy().append(net.minecraft.network.chat.Component.literal(append));
    }

    private static ClickEvent createClickEvent(String action, String value) {
        return switch (action.toUpperCase()) {
            case "SUGGEST_COMMAND" -> new ClickEvent.SuggestCommand(value);
            case "OPEN_URL" -> new ClickEvent.OpenUrl(URI.create(value));
            case "CHANGE_PAGE" -> new ClickEvent.ChangePage(Integer.parseInt(value));
            case "COPY_TO_CLIPBOARD" -> new ClickEvent.CopyToClipboard(value);
            default -> new ClickEvent.RunCommand(value);
        };
    }
}
