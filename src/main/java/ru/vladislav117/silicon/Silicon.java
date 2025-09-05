package ru.vladislav117.silicon;

/**
 * Фреймворк Silicon.
 */
public final class Silicon {
    static SiliconPlugin plugin;

    /**
     * Получение плагина фреймворка Silicon.
     *
     * @return Плагин фреймворка Silicon.
     */
    public static SiliconPlugin getPlugin() {
        return plugin;
    }

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
