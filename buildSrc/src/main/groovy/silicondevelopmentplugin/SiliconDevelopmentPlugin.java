package silicondevelopmentplugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import silicondevelopmentplugin.properties.PropertiesLoader;
import silicondevelopmentplugin.tasks.deployment.LocalDeployTask;

/**
 * Плагин для разработки фреймворка Silicon.
 */
public class SiliconDevelopmentPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getTasks().register("localDeploy", LocalDeployTask.class);
        new PropertiesLoader(project.getRootProject().file("local.properties")).load(project);
    }
}