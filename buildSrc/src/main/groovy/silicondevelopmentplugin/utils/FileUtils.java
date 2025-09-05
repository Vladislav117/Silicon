package silicondevelopmentplugin.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

/**
 * Утилиты для работы с файлами.
 */
public final class FileUtils {
    /**
     * Чтение строки из файла.
     * Если произойдёт ошибка ввода-вывода, будет вызвано исключение.
     *
     * @param file Файл
     * @return Строка
     * @throws RuntimeException Если произойдёт ошибка ввода-вывода, будет вызвано исключение.
     */
    public static String readString(File file) {
        String string;
        try {
            string = Files.readString(file.toPath(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new RuntimeException("[!] Error reading file " + file, exception);
        }
        return string;
    }

    /**
     * Запись строки в файл.
     * Если произойдёт ошибка ввода-вывода, будет вызвано исключение.
     *
     * @param file    Файл
     * @param content Строка
     * @throws RuntimeException Если произойдёт ошибка ввода-вывода, будет вызвано исключение.
     */
    public static void writeString(File file, String content) {
        try {
            Files.writeString(file.toPath(), content, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException exception) {
            throw new RuntimeException("[!] Error writing file " + file, exception);
        }
    }

    /**
     * Копирование файла.
     * Если произойдёт ошибка ввода-вывода, будет вызвано исключение.
     *
     * @param file      Файл
     * @param directory Директория назначения
     * @throws RuntimeException Если произойдёт ошибка ввода-вывода, будет вызвано исключение.
     */
    public static void copy(File file, File directory) {
        try {
            Files.copy(file.toPath(), directory.toPath().resolve(file.toPath().getFileName()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            throw new RuntimeException("[!] Error copying file " + file, exception);
        }
    }
}
