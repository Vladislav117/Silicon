package ru.vladislav117.silicon.emoji;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ru.vladislav117.silicon.content.SiContentList;
import ru.vladislav117.silicon.content.SiContentLoaders;
import ru.vladislav117.silicon.event.SiBuiltinEvents;
import ru.vladislav117.silicon.event.SiEvents;
import ru.vladislav117.silicon.node.SiNode;
import ru.vladislav117.silicon.resourcepack.SiResourcepack;

import java.util.ArrayList;

/**
 * Класс-контейнер для эмоджи.
 */
public class SiEmojis {
    public static final SiContentList<SiEmoji> all = new SiContentList<>();
    public static final SiContentLoaders loaders = new SiContentLoaders();

    static ArrayList<TextReplacementConfig> replacers = new ArrayList<>();

    /**
     * Обработка сообщения.
     *
     * @param message Сообщение
     * @return Новое сообщение с подставленными эмоджи.
     */
    public static Component handleMessage(Component message) {
        for (TextReplacementConfig replacer : replacers) {
            message = message.replaceText(replacer);
        }
        return message;
    }

    /**
     * Инициализация.
     */
    public static void init() {
        SiEvents.addHandler(SiBuiltinEvents.ResourcepackWriteStartEvent.class, event -> {
            SiNode registry = SiNode.emptyList();
            for (SiEmoji emoji : all.getAll()) {
                registry.add(SiNode.emptyMap().set("symbol", emoji.getSymbol()).set("name", emoji.getName()));
            }
            SiResourcepack.addGroup("emoji", registry);
        });

        loaders.addSecondaryLoader(() -> {
            for (SiEmoji emoji : all.getAll()) {
                replacers.add(TextReplacementConfig.builder().matchLiteral(":" + emoji.getName() + ":").replacement(emoji.getSymbol()).build());
            }
        });
    }
}
