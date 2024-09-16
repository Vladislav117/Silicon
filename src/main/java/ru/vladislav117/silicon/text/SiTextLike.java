package ru.vladislav117.silicon.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.impl.JSONComponentSerializerProviderImpl;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import org.jetbrains.annotations.Nullable;
import ru.vladislav117.silicon.message.SiMessageTask;
import ru.vladislav117.silicon.node.SiNode;
import ru.vladislav117.silicon.text.style.SiStyleLike;
import ru.vladislav117.silicon.type.SiCloneable;

/**
 * Интерфейс, экземпляры реализаций которого могут быть использованы в качестве текста.
 */
public interface SiTextLike extends SiCloneable {
    JSONComponentSerializer componentSerializer = new JSONComponentSerializerProviderImpl().instance();

    @Override
    SiTextLike clone();

    /**
     * Получение стиля текста.
     *
     * @return Стиль текста или null, если не задан.
     */
    @Nullable
    SiStyleLike getStyle();

    /**
     * Установка стиля текста.
     *
     * @param style Стиль текста или null, если не задан
     * @return Этот же текст.
     */
    SiTextLike setStyle(SiStyleLike style);

    /**
     * Преобразование текста к запланированному сообщению.
     *
     * @return Запланированное сообщение.
     */
    default SiMessageTask toMessageTask() {
        return new SiMessageTask(this);
    }

    /**
     * Преобразование объекта к компоненту текста.
     *
     * @return Компонент текста, основанный на экземпляре.
     */
    Component toComponent();

    /**
     * Преобразование текста к строке Json.
     *
     * @return Json строка.
     */
    default String toJsonComponentString() {
        return componentSerializer.serialize(toComponent());
    }

    /**
     * Преобразование текста к Json-формату в узле.
     *
     * @return Текст в Json-формате в узле.
     */
    default SiNode toJsonComponentNode() {
        return SiNode.parseJsonString(toJsonComponentString());
    }
}
