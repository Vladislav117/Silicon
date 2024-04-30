package ru.vladislav117.silicon.cooldown;

import ru.vladislav117.silicon.function.SiFunction;
import ru.vladislav117.silicon.text.SiText;
import ru.vladislav117.silicon.text.SiTextLike;
import ru.vladislav117.silicon.utils.SiNumberUtils;

/**
 * Менеджер кулдаунов, работающих на времени.
 */
public final class SiTimeCooldown {
    static SiCooldownManager manager = new SiCooldownManager() {
        @Override
        public long getCurrentTime() {
            return System.currentTimeMillis();
        }

        @Override
        public SiTextLike getFormattedRemainingTime(long remainingTime) {
            return SiText.string(SiNumberUtils.doubleToString2Digits(remainingTime / 1000.0) + " сек.");
        }
    };

    /**
     * Получение времени окончания кулдауна.
     *
     * @param name Имя записи кулдауна
     * @return Время окончания кулдауна.
     */
    public static long getEndTime(String name) {
        return manager.getEndTime(name);
    }

    /**
     * Проверка, имеется ли кулдаун у записи по имени.
     *
     * @param name Имя записи кулдауна
     * @return имеется ли кулдаун у записи.
     */
    public static boolean hasCooldown(String name) {
        return manager.hasCooldown(name);
    }

    /**
     * Получение остатка времени кулдауна.
     *
     * @param name Имя записи кулдауна
     * @return Остаток времени кулдауна.
     */
    public static long getRemainingTime(String name) {
        return manager.getRemainingTime(name);
    }

    /**
     * Установка кулдауна.
     *
     * @param name    Имя записи кулдауна
     * @param seconds Секунды, по прошествии которых кулдаун спадёт
     */
    public static void setCooldown(String name, double seconds) {
        manager.setCooldown(name, (long) (seconds * 1000));
    }

    /**
     * Получение оставшегося времени кулдауна в читабельном виде.
     *
     * @param name Имя записи кулдауна
     * @return Оставшееся время кулдауна в читабельном виде.
     */
    public static SiTextLike getFormattedRemainingTime(String name) {
        return manager.getFormattedRemainingTime(name);
    }

    /**
     * Запуск функции, если кулдаун закончился.
     *
     * @param name     Имя записи кулдауна
     * @param function Функция, которая будет выполнена
     */
    public static void runIfCooldownEnd(String name, SiFunction function) {
        manager.runIfCooldownEnd(name, function);
    }

    /**
     * Запуск функции, если кулдаун закончился.
     *
     * @param name     Имя записи кулдауна
     * @param seconds  Секунды, которые будут установлены в кулдаун после выполнения функции
     * @param function Функция, которая будет выполнена
     */
    public static void runIfCooldownEnd(String name, long seconds, SiFunction function) {
        manager.runIfCooldownEnd(name, seconds, function);
    }
}
