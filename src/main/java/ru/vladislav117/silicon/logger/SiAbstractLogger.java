package ru.vladislav117.silicon.logger;

/**
 * Абстрактный логгер.
 */
public abstract class SiAbstractLogger {
    protected boolean enabled = true;

    /**
     * Получение статуса активности логгера.
     *
     * @return Статус активности логгера.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Установка статуса активности логгера.
     *
     * @param enabled Статус активности логгера
     * @return Этот же логгер.
     */
    @SuppressWarnings("UnusedReturnValue")
    public SiAbstractLogger setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /**
     * Вывод сообщения.
     *
     * @param message Сообщение
     */
    protected abstract void printMessage(String message);

    /**
     * Обработка сообщения.
     * После этой стадии сообщение будет выведено.
     *
     * @param message Исходное сообщение
     * @return Обработанное сообщение.
     */
    protected String processMessage(String message) {
        return message;
    }

    /**
     * Преобразование объектов к строке.
     *
     * @param objects Объекты
     * @return Строка
     */
    protected String objectToString(Object[] objects) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object object : objects) stringBuilder.append(object).append(" ");
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    /**
     * Обработка массива объектов.
     * После этой стадии объекты будут преобразованы в строку.
     *
     * @param objects Исходный массив объектов
     * @return Обработанный массив объектов.
     */
    protected Object[] processObjects(Object... objects) {
        return objects;
    }

    /**
     * Вывод сообщения в лог.
     *
     * @param objects Объекты
     * @return Этот же логгер.
     */
    @SuppressWarnings("UnusedReturnValue")
    public SiAbstractLogger log(Object... objects) {
        objects = processObjects(objects);
        String message = objectToString(objects);
        message = processMessage(message);
        printMessage(message);
        return this;
    }
}
