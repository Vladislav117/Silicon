package ru.vladislav117.silicon;

import ru.vladislav117.silicon.logger.SiLog;

/**
 * Фреймворк Silicon.
 */
public final class Silicon {
    /**
     * Название фреймворка Silicon.
     */
    public static final String NAME = "Silicon";
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
        initModules();
    }

    /**
     * Инициализация модулей.
     */
    static void initModules() {
        SiLog.init();
    }
}
