package ru.vladislav117.silicon.cusomModelData;

import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;
import ru.vladislav117.silicon.node.SiNode;

import java.util.Locale;

/**
 * Запись в реестр кастомных моделей.
 */
public class SiCustomModelDataRecord {
    protected int customModelData;
    protected Material material;
    protected String category;
    protected String name;
    @Nullable
    protected SiNode additionalData = null;

    /**
     * Создание новой записи в реестр.
     *
     * @param category       Категория
     * @param name           Имя
     * @param additionalData Дополнительные данные или null
     */
    public SiCustomModelDataRecord(Material material, String category, String name, @Nullable SiNode additionalData) {
        this.material = material;
        this.category = category;
        this.name = name;
        this.additionalData = additionalData;
        customModelData = SiCustomModelDataRegistry.register(this);
    }

    /**
     * Создание новой записи в реестр.
     *
     * @param category Категория
     * @param name     Имя
     */
    public SiCustomModelDataRecord(Material material, String category, String name) {
        this.material = material;
        this.category = category;
        this.name = name;
        customModelData = SiCustomModelDataRegistry.register(this);
    }

    /**
     * Получение CustomModelData.
     *
     * @return CustomModelData.
     */
    public int getCustomModelData() {
        return customModelData;
    }

    /**
     * Получение категории записи.
     *
     * @return Категория записи.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Получение имени записи.
     *
     * @return Имя записи.
     */
    public String getName() {
        return name;
    }

    /**
     * Получение дополнительных данных.
     *
     * @return Дополнительные данные записи или null.
     */
    @Nullable
    public SiNode getAdditionalData() {
        return additionalData;
    }

    /**
     * Преобразование записи в узел.
     *
     * @return Узел из записи.
     */
    public SiNode toNode() {
        return SiNode.emptyMap()
            .set("custom_model_data", customModelData)
            .set("material", material.toString().toLowerCase(Locale.ROOT))
            .set("category", category)
            .set("name", name)
            .set("data", additionalData == null ? SiNode.emptyMap() : additionalData);
    }
}
