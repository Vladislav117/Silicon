package ru.vladislav117.silicon.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import ru.vladislav117.silicon.Silicon;

import java.util.function.Consumer;

/**
 * Основная система событий фреймворка Silicon.
 */
public final class SiEvents {
    static SiEventSystem<SiEvent> eventSystem = new SiEventSystem<>();

    /**
     * Добавление обработчика события.
     *
     * @param eventType Тип события
     * @param handler   Обработчик события
     * @param <Event>   Класс события
     */
    public <Event extends SiEvent> void addHandler(Class<Event> eventType, Consumer<Event> handler) {
        eventSystem.addHandler(eventType, handler);
    }

    /**
     * Обработка события. Будут вызваны все обработчики для класса события в порядке добавления.
     *
     * @param event   Событие
     * @param <Event> Класс события
     */
    public <Event extends SiEvent> void handle(Event event) {
        eventSystem.handle(event);
    }

    /**
     * Регистрация обработчика Bukkit событий.
     *
     * @param plugin   Плагин, на который регистрируется обработчик
     * @param listener Обработчик Bukkit событий
     */
    public void registerBukkitEventListener(Plugin plugin, Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    /**
     * Регистрация обработчика Bukkit событий на плагин фреймворка Silicon.
     *
     * @param listener Обработчик Bukkit событий
     */
    public void registerBukkitEventListener(Listener listener) {
        registerBukkitEventListener(Silicon.getPlugin(), listener);
    }
}
