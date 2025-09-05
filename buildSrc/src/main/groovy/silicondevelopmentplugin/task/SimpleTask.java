package silicondevelopmentplugin.task;

import org.gradle.api.Project;

/**
 * Простая задача.
 */
public abstract class SimpleTask extends Task {
    public SimpleTask() {
        addRunnable(this::run);
    }

    /**
     * Метод, выполняющийся при запуске задачи.
     *
     * @param project Проект, в котором выполняется задача
     */
    public abstract void run(Project project);
}
