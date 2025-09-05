package silicondevelopmentplugin.tasks.deployment;

import org.gradle.api.Project;
import silicondevelopmentplugin.task.SimpleTask;
import silicondevelopmentplugin.utils.FileUtils;

import java.io.File;

/**
 * Задача локального развёртывания плагина.
 */
public class LocalDeployTask extends SimpleTask {
    /**
     * Создание задачи локального развёртывания плагина.
     */
    public LocalDeployTask() {
        dependsOn("build");
    }

    @Override
    public void run(Project project) {
        if (!project.hasProperty("localServerDirectory")) {
            throw new RuntimeException("Property \"localServerDirectory\" does not specified");
        }
        String serverDirectory = (String) project.property("localServerDirectory");
        if (serverDirectory == null) return;
        FileUtils.copy(findJarTask().getArchiveFile().get().getAsFile(), new File(serverDirectory, "plugins"));
    }
}
