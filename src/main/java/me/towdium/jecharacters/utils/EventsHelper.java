package me.towdium.jecharacters.utils;

import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;

import java.util.function.Consumer;

public class EventsHelper {
    public static <T extends Event> void registerEvent(IEventBus eventBus, Class<T> eventType, Consumer<T> consumer) {
        eventBus.addListener(EventPriority.HIGHEST, eventType, consumer);
    }
}
