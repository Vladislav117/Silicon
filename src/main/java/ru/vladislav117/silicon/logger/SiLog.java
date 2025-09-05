package ru.vladislav117.silicon.logger;

import org.bukkit.Bukkit;
import ru.vladislav117.silicon.Silicon;

/**
 * Основной логгер фреймворка Silicon.
 */
public final class SiLog {
    static String prefix = Silicon.NAME;
    static SiAbstractLogger logger = new SiPrefixedAbstractLogger(prefix) {
        @Override
        protected void printMessage(String message) {
            Bukkit.getLogger().info(message); // TODO 06.09.2025: Suppress warning or find another way
        }
    };

    /**
     * Вывод сообщения в лог.
     *
     * @param objects Объекты
     */
    public static void log(Object... objects) {
        logger.log(objects);
    }

    /**
     * Инициализация.
     */
    public static void init() {
        // TODO 06.09.2025: Add reloading, loaders, etc.
    }
}
