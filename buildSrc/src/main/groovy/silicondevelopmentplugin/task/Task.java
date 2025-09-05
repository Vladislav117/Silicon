package silicondevelopmentplugin.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.jvm.tasks.Jar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Задача.
 */
public abstract class Task extends DefaultTask {
    private final List<TaskRunnable> runnable = new ArrayList<>();

    /**
     * Добавление выполняемой функции в задачу.
     *
     * @param runnable Выполняемая функция.
     */
    public void addRunnable(TaskRunnable runnable) {
        this.runnable.add(runnable);
    }

    /**
     * Выполнение задачи.
     */
    @TaskAction
    public void runTask() {
        runnable.forEach(runnable -> runnable.run(getProject()));
    }

    /**
     * Получение файла из директории проекта.
     *
     * @param path Путь к файлу
     * @return Файл.
     */
    protected File file(String path) {
        return new File(getProject().getRootDir(), path);
    }

    /**
     * Поиск задачи "jar".
     * Если задача не найдена, будет вызвано исключение.
     *
     * @return Задача "jar".
     * @throws RuntimeException Если задача не найдена, будет вызвано исключение.
     */
    protected Jar findJarTask() {
        for (org.gradle.api.Task task : getProject().getTasksByName("jar", false)) {
            if (task instanceof Jar jar) return jar;
        }
        throw new RuntimeException("[!] Jar task does not exists");
    }
}
