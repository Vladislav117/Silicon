package ru.vladislav117.silicon.achievement;

import io.papermc.paper.advancement.AdvancementDisplay;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import ru.vladislav117.silicon.icon.SiIcon;
import ru.vladislav117.silicon.item.SiItemStack;
import ru.vladislav117.silicon.item.SiItemType;
import ru.vladislav117.silicon.node.SiNode;
import ru.vladislav117.silicon.text.SiText;
import ru.vladislav117.silicon.text.SiTextLike;

import java.util.Locale;

/**
 * Отображение ачивки.
 */
public class SiAchievementDisplay {
    protected SiTextLike displayName;
    protected SiTextLike description;
    protected Material icon = Material.BARRIER;
    @Nullable
    protected Integer customModelData = null;
    protected AdvancementDisplay.Frame frame = AdvancementDisplay.Frame.TASK;

    protected boolean doesShowToast = true;
    protected boolean doesAnnounceToChat = false;
    protected boolean hidden = false;

    @Nullable
    protected NamespacedKey backgroundPath = null;

    /**
     * Создание нового отображения ачивки.
     *
     * @param displayName Отображаемое имя
     * @param description Описание
     */
    public SiAchievementDisplay(SiTextLike displayName, SiTextLike description) {
        this.displayName = displayName;
        this.description = description;
    }

    /**
     * Создание нового отображения ачивки.
     *
     * @param displayName Отображаемое имя
     * @param description Описание
     */
    public SiAchievementDisplay(SiTextLike displayName, String description) {
        this.displayName = displayName;
        this.description = SiText.string(description);
    }

    /**
     * Создание нового отображения ачивки.
     *
     * @param displayName Отображаемое имя
     */
    public SiAchievementDisplay(SiTextLike displayName) {
        this.displayName = displayName;
        this.description = SiText.string("");
    }

    /**
     * Создание нового отображения ачивки.
     *
     * @param displayName Отображаемое имя
     * @param description Описание
     */
    public SiAchievementDisplay(String displayName, SiTextLike description) {
        this.displayName = SiText.string(displayName);
        this.description = description;
    }

    /**
     * Создание нового отображения ачивки.
     *
     * @param displayName Отображаемое имя
     * @param description Описание
     */
    public SiAchievementDisplay(String displayName, String description) {
        this.displayName = SiText.string(displayName);
        this.description = SiText.string(description);
    }

    /**
     * Создание нового отображения ачивки.
     *
     * @param displayName Отображаемое имя
     */
    public SiAchievementDisplay(String displayName) {
        this.displayName = SiText.string(displayName);
        this.description = SiText.string("");
    }

    /**
     * Получение отображаемого имени ачивки.
     *
     * @return Отображаемое имя ачивки.
     */
    public SiTextLike getDisplayName() {
        return displayName;
    }

    /**
     * Установка отображаемого имени ачивки.
     *
     * @param displayName Отображаемое имя
     * @return Это же отображение ачивки.
     */
    public SiAchievementDisplay setDisplayName(SiTextLike displayName) {
        this.displayName = displayName;
        return this;
    }

    /**
     * Получение описания ачивки.
     *
     * @return Описание ачивки.
     */
    public SiTextLike getDescription() {
        return description;
    }

    /**
     * Установка описания ачивки.
     *
     * @param description Описание ачивки
     * @return Это же отображение ачивки.
     */
    public SiAchievementDisplay setDescription(SiTextLike description) {
        this.description = description;
        return this;
    }

    /**
     * Получение рамки ачивки.
     *
     * @return Рамка ачивки.
     */
    public AdvancementDisplay.Frame getFrame() {
        return frame;
    }

    /**
     * Установка рамки ачивки.
     *
     * @param frame Рамка ачивки
     * @return Это же отображение ачивки.
     */
    public SiAchievementDisplay setFrame(AdvancementDisplay.Frame frame) {
        this.frame = frame;
        return this;
    }

    /**
     * Получение иконки ачивки.
     *
     * @return Иконка ачивки.
     */
    public Material getIcon() {
        return icon;
    }

    /**
     * Установка иконки.
     *
     * @param icon Иконка
     * @return Это же отображение ачивки.
     */
    public SiAchievementDisplay setIcon(SiItemStack icon) {
        this.icon = icon.getMaterial();
        if (!icon.getMeta().hasCustomModelData()) return this;
        customModelData = icon.getMeta().getCustomModelData();
        return this;
    }

    /**
     * Установка иконки.
     *
     * @param icon Иконка
     * @return Это же отображение ачивки.
     */
    public SiAchievementDisplay setIcon(ItemStack icon) {
        SiItemStack stack = new SiItemStack(icon);
        this.icon = stack.getMaterial();
        if (!stack.getMeta().hasCustomModelData()) return this;
        customModelData = stack.getMeta().getCustomModelData();
        return this;
    }

    /**
     * Установка иконки.
     *
     * @param icon Иконка
     * @return Это же отображение ачивки.
     */
    public SiAchievementDisplay setIcon(Material icon) {
        this.icon = icon;
        return this;
    }

    /**
     * Установка иконки.
     *
     * @param icon Иконка
     * @return Это же отображение ачивки.
     */
    public SiAchievementDisplay setIcon(SiItemType icon) {
        SiItemStack stack = icon.buildItemStack();
        this.icon = stack.getMaterial();
        if (!stack.getMeta().hasCustomModelData()) return this;
        customModelData = stack.getMeta().getCustomModelData();
        return this;
    }

    /**
     * Установка иконки.
     *
     * @param icon Иконка
     * @return Это же отображение ачивки.
     */
    public SiAchievementDisplay setIcon(SiIcon icon) {
        SiItemStack stack = icon.buildItemStack();
        this.icon = stack.getMaterial();
        if (!stack.getMeta().hasCustomModelData()) return this;
        customModelData = stack.getMeta().getCustomModelData();
        return this;
    }

    /**
     * Показывается ли выполнение ачивки в правом верхнем углу.
     *
     * @return Показывается ли выполнение ачивки в правом верхнем углу.
     */
    public boolean doesShowToast() {
        return doesShowToast;
    }

    /**
     * Установить, показывается ли выполнение ачивки в правом верхнем углу.
     *
     * @param doesShowToast Показывается ли выполнение ачивки в правом верхнем углу
     * @return Это же отображение ачивки.
     */
    public SiAchievementDisplay setDoesShowToast(boolean doesShowToast) {
        this.doesShowToast = doesShowToast;
        return this;
    }

    /**
     * Отображается ли выполнение ачивки в чате.
     *
     * @return Отображается ли выполнение ачивки в чате.
     */
    public boolean doesAnnounceToChat() {
        return doesAnnounceToChat;
    }

    /**
     * Установить, отображается ли выполнение ачивки в чате.
     *
     * @param doesAnnounceToChat Отображается ли выполнение ачивки в чате
     * @return Это же отображение ачивки.
     */
    public SiAchievementDisplay setDoesAnnounceToChat(boolean doesAnnounceToChat) {
        this.doesAnnounceToChat = doesAnnounceToChat;
        return this;
    }

    /**
     * Скрыто ли достижение.
     *
     * @return Скрыто ли достижение.
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Установить, скрыто ли достижение.
     *
     * @param hidden Скрыто ли достижение
     * @return Это же отображение ачивки.
     */
    public SiAchievementDisplay setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    /**
     * Получение имени ключа заднего фона.
     *
     * @return Имя ключа заднего фона или null.
     */
    @Nullable
    public NamespacedKey getBackgroundPath() {
        return backgroundPath;
    }

    /**
     * Установка имени ключа заднего фона.
     *
     * @param backgroundPath Имя ключа заднего фона
     * @return Это же отображение ачивки.
     */
    public SiAchievementDisplay setBackgroundPath(NamespacedKey backgroundPath) {
        this.backgroundPath = backgroundPath;
        return this;
    }

    /**
     * Преобразовать ачивку в узел.
     *
     * @return Ачивка в формате узла.
     */
    public SiNode buildNode() {
        SiNode node = SiNode.emptyMap();
        SiNode iconNode = SiNode.emptyMap().set("item", icon.name().toLowerCase(Locale.ROOT));
        if (customModelData != null) iconNode.set("nbt", "{\"CustomModelData\":" + customModelData + "}");
        node.set("icon", iconNode);
        node.set("title", displayName.toJsonComponentNode());
        node.set("description", displayName.toJsonComponentNode());
        node.set("frame", frame.name().toLowerCase(Locale.ROOT));
        node.set("show_toast", doesShowToast);
        node.set("announce_to_chat", doesAnnounceToChat);
        node.set("hidden", hidden);
        return node;
    }
}
