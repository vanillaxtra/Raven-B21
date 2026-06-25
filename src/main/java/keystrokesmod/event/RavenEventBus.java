package keystrokesmod.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RavenEventBus {
    private static final Map<Class<?>, List<Listener>> listeners = new HashMap<>();

    private record Listener(Object target, Method method, int priority) {}

    public static void register(Object target) {
        for (Method method : target.getClass().getDeclaredMethods()) {
            SubscribeEvent sub = method.getAnnotation(SubscribeEvent.class);
            if (sub == null) {
                continue;
            }
            Class<?>[] params = method.getParameterTypes();
            if (params.length != 1) {
                continue;
            }
            method.setAccessible(true);
            Class<?> eventType = params[0];
            listeners.computeIfAbsent(eventType, k -> new ArrayList<>())
                    .add(new Listener(target, method, sub.priority()));
        }
    }

    public static void unregister(Object target) {
        for (List<Listener> list : listeners.values()) {
            list.removeIf(l -> l.target() == target);
        }
    }

    public static void post(Object event) {
        List<Listener> list = listeners.get(event.getClass());
        if (list == null) {
            return;
        }
        List<Listener> sorted = list.stream()
                .sorted((a, b) -> Integer.compare(b.priority(), a.priority()))
                .toList();
        for (Listener listener : sorted) {
            try {
                listener.method().invoke(listener.target(), event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
