package silicondevelopmentplugin.properties;

import org.gradle.api.Project;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

/**
 * Загрузчик свойств.
 */
public class PropertiesLoader {
    protected File file;

    /**
     * Создание загрузчика свойств.
     *
     * @param file Файл свойств
     */
    public PropertiesLoader(File file) {
        this.file = file;
    }

    /**
     * Создание загрузчика свойств.
     *
     * @param directoryPath Путь к директории
     * @param filePath      Путь к файлу
     */
    public PropertiesLoader(File directoryPath, String filePath) {
        file = new File(directoryPath, filePath);
    }

    /**
     * Создание загрузчика свойств.
     *
     * @param filePath Путь к файлу
     */
    public PropertiesLoader(String filePath) {
        file = new File(filePath);
    }

    /**
     * Загрузка свойств в проект.
     * Если произойдёт ошибка ввода-вывода, будет вызвано исключение.
     *
     * @param project Проект
     * @throws RuntimeException Если произойдёт ошибка ввода-вывода, будет вызвано исключение.
     */
    public void load(Project project) {
        if (!file.exists()) {
            project.getLogger().warn("[!] Properties file {} does not exists", file);
            return;
        }
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            properties.load(fileInputStream);
            properties.forEach((key, value) -> project.getExtensions().getExtraProperties().set(Objects.toString(key), Objects.toString(value)));
        } catch (IOException exception) {
            throw new RuntimeException("[!] Failed to load " + file, exception);
        }
    }
}
