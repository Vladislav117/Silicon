package ru.vladislav117.silicon;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Плагин фреймворка Silicon.
 * При вызове {@link SiliconPlugin#onEnable()} инициализирует фреймворк.
 *
 * @see Silicon
 * @see Silicon#init(SiliconPlugin)
 */
public final class SiliconPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("SILICON IS UNDER DEVELOPMENT! DO NOT USE IT IN PRODUCTION!"); // TODO 05.09.2025: Remove this before publishing first version
        Silicon.init(this);
    }
}
