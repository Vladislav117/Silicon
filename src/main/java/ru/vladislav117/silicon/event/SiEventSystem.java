package ru.vladislav117.silicon.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Система событий, основанная на классах.
 * Каждый класс - определённый тип события, на который регистрируются обработчики.
 *
 * @param <BaseEvent> Базовый класс события
 */
public class SiEventSystem<BaseEvent> {
    protected Map<Object, List<Consumer<? extends BaseEvent>>> eventHandlers = new HashMap<>();

    /**
     * Добавление обработчика события.
     *
     * @param eventType Тип события
     * @param handler   Обработчик события
     * @param <Event>   Класс события
     * @return Эта же система событий.
     */
    @SuppressWarnings("UnusedReturnValue")
    public <Event extends BaseEvent> SiEventSystem<BaseEvent> addHandler(Class<Event> eventType, Consumer<Event> handler) {
        if (!eventHandlers.containsKey(eventType)) eventHandlers.put(eventType, new ArrayList<>());
        eventHandlers.get(eventType).add(handler);
        return this;
    }

    /**
     * Обработка события. Будут вызваны все обработчики для класса события в порядке добавления.
     *
     * @param event   Событие
     * @param <Event> Класс события
     * @return Эта же система событий.
     */
    @SuppressWarnings({"unchecked", "UnusedReturnValue"})
    public <Event extends BaseEvent> SiEventSystem<BaseEvent> handle(Event event) {
        Class<?> eventClass = getOriginalClass(event.getClass());
        if (!eventHandlers.containsKey(eventClass)) return this;
        eventHandlers.get(eventClass).forEach(handler -> ((Consumer<Event>) handler).accept(event));
        return this;
    }

    /**
     * Получение оригинального класса.
     *
     * @param cls Класс
     * @return Оригинальный класс.
     */
    static Class<?> getOriginalClass(Class<?> cls) {
        if (cls.isAnonymousClass()) {
            return cls.getInterfaces().length == 0 ? cls.getSuperclass() : cls.getInterfaces()[0];
        } else {
            return cls;
        }
    }
}
