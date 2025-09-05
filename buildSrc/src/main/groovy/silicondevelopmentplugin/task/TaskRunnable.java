package silicondevelopmentplugin.task;

import org.gradle.api.Project;

/**
 * Функция, запускаемая задачей.
 */
@FunctionalInterface
public interface TaskRunnable {
    /**
     * Выполнение функции.
     *
     * @param project Проект, в котором выполняется функция
     */
    void run(Project project);
}
