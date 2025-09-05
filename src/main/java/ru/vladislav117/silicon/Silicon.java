package ru.vladislav117.silicon;

/**
 * Фреймворк Silicon.
 */
public final class Silicon {
    static SiliconPlugin plugin;

    /**
     * Инициализация фреймворка Silicon.
     *
     * @param plugin Плагин фреймворка Silicon.
     * @see SiliconPlugin
     */
    public static void init(SiliconPlugin plugin) {
        Silicon.plugin = plugin;
    }
}
