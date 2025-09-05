package silicondevelopmentplugin.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Утилиты для работы с директориями.
 */
public final class DirectoryUtils {
    /**
     * Получение всех файлов директории, включая файлы в поддиректориях.
     *
     * @param directory Директория
     * @return Файлы в директории и поддиректориях.
     */
    public static List<File> getEntryFiles(File directory) {
        List<File> files = new ArrayList<>();
        if (!directory.exists()) return files;
        File[] entries = directory.listFiles();
        if (entries == null) return files;
        for (File entry : entries) {
            if (entry.isFile()) files.add(entry);
            if (entry.isDirectory()) files.addAll(getEntryFiles(entry));
        }
        return files;
    }
}
