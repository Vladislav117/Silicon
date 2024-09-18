package ru.vladislav117.silicon.achievement;

import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import ru.vladislav117.silicon.content.SiContent;
import ru.vladislav117.silicon.namespace.SiNamespace;
import ru.vladislav117.silicon.node.SiNode;

import java.util.ArrayList;

/**
 * Ачивка.
 */
public class SiAchievement extends SiContent {
    protected SiAchievement root;
    @Nullable
    protected SiAchievement parent = null;
    protected ArrayList<SiAchievement> children = new ArrayList<>();
    protected SiAchievementDisplay display = null;

    protected Advancement advancement;

    /**
     * Создание корневой ачивки.
     *
     * @param name Имя ачивки
     */
    public SiAchievement(String name) {
        super(name);
        root = this;
        display = new SiAchievementDisplay(name);
        SiAchievements.all.add(this);
    }

    /**
     * Создание дочерней ачивки.
     *
     * @param name   Имя ачивки
     * @param parent Родительская ачивка
     */
    public SiAchievement(String name, SiAchievement parent) {
        super(name);
        this.parent = parent;
        parent.addChild(this);
        root = findRoot();
        display = new SiAchievementDisplay(name);
        SiAchievements.all.add(this);
    }

    /**
     * Получение корневой ачивки.
     *
     * @return Корневая ачивка.
     */
    public SiAchievement getRoot() {
        return root;
    }

    /**
     * Рекурсивный поиск корневой ачивки.
     *
     * @return Корневая ачивка.
     */
    protected SiAchievement findRoot() {
        if (parent == null) return this;
        return parent.findRoot();
    }

    /**
     * Добавление дочерней ачивки.
     *
     * @param child Дочерняя ачивка
     * @return Эта же ачивка.
     */
    protected SiAchievement addChild(SiAchievement child) {
        children.add(child);
        return this;
    }

    /**
     * Получение родительской ачивки.
     *
     * @return Родительская ачивка или null, если эта ачивка корневая.
     */
    @Nullable
    public SiAchievement getParent() {
        return parent;
    }

    /**
     * Получение дочерних ачивок.
     *
     * @return Дочерние ачивки.
     */
    public ArrayList<SiAchievement> getChildren() {
        return children;
    }

    /**
     * Получение отображения ачивки.
     *
     * @return Отображение ачивки.
     */
    public SiAchievementDisplay getDisplay() {
        return display;
    }

    /**
     * Получение достижения.
     *
     * @return Достижение.
     */
    public Advancement getAdvancement() {
        return advancement;
    }

    /**
     * Выдать ачивку игроку
     *
     * @param player Игрок
     * @return Эта же ачивка.
     */
    public SiAchievement grant(Player player) {
        AdvancementProgress advancementProgress = player.getAdvancementProgress(advancement);
        advancementProgress.awardCriteria(name);
        return this;
    }

    /**
     * Отозвать ачивку у игрока.
     *
     * @param player Игрок
     * @return Эта же ачивка.
     */
    public SiAchievement revoke(Player player) {
        AdvancementProgress advancementProgress = player.getAdvancementProgress(advancement);
        advancementProgress.revokeCriteria(name);
        return this;
    }

    /**
     * Создать узел из ачивки.
     *
     * @return Узел.
     */
    protected SiNode buildNode() {
        SiNode node = SiNode.emptyMap();
        node.set("display", display.buildNode());
        if (parent != null) node.set("parent", parent.getAdvancement().getKey().asString());

        SiNode criteriaItems = SiNode.emptyList().add("minecraft:knowledge_book");
        SiNode criteriaItem = SiNode.emptyMap().set("item", criteriaItems);
        SiNode criteriaConditions = SiNode.emptyMap().set("conditions", criteriaItem);
        SiNode criteria = SiNode.emptyMap().set("trigger", "minecraft:using_item").set("conditions", criteriaConditions);
        node.set("criteria", SiNode.emptyMap().set(name, criteria));
        return node;
    }

    /**
     * Создать дерево ачивок.
     *
     * @return Эта же ачивка.
     */
    @SuppressWarnings("deprecated")
    public SiAchievement buildTree() {
        SiNode node = buildNode();
        String json = node.toJsonString();
        advancement = Bukkit.getUnsafe().loadAdvancement(SiNamespace.getKey(name), json);
        for (SiAchievement child : children) {
            child.buildTree();
        }
        return this;
    }
}
