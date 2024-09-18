package ru.vladislav117.silicon.item;

import ru.vladislav117.silicon.cusomModelData.SiCustomModelDataRecord;
import ru.vladislav117.silicon.node.SiNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Текстура предмета.
 */
public abstract class SiItemTexture {
    public static final String customModelDataCategory = "item";
    protected SiItemType itemType;

    /**
     * Создание текстуры предмета.
     *
     * @param itemType Тип предмета
     */
    public SiItemTexture(SiItemType itemType) {
        this.itemType = itemType;
    }

    /**
     * Получение типа предмета.
     *
     * @return Тип предмета.
     */
    public SiItemType getItemType() {
        return itemType;
    }

    /**
     * Получение CustomModelData в зависимости от стака.
     *
     * @param itemStack Стак
     * @return CustomModelData.
     */
    public abstract int getCustomModelData(SiItemStack itemStack);

    /**
     * Статическая текстура предмета.
     */
    public static class StaticTexture extends SiItemTexture {
        protected SiCustomModelDataRecord customModelDataRecord;

        /**
         * Создание текстуры предмета.
         *
         * @param itemType Тип предмета
         */
        public StaticTexture(SiItemType itemType) {
            super(itemType);
            customModelDataRecord = new SiCustomModelDataRecord(itemType.getMaterial(), customModelDataCategory, itemType.getName());
        }

        @Override
        public int getCustomModelData(SiItemStack itemStack) {
            return customModelDataRecord.getCustomModelData();
        }
    }

    /**
     * Текстура предмета, зависящая от свойства.
     */
    public static class FeatureTexture extends SiItemTexture {
        protected String feature;
        protected String defaultValue;
        protected HashMap<String, SiCustomModelDataRecord> customModelDataRecords = new HashMap<>();

        /**
         * Создание текстуры предмета.
         *
         * @param itemType      Тип предмета
         * @param feature       Название свойства
         * @param defaultValue  Значение свойства по умолчанию
         * @param featureValues Все возможные значения свойств
         */
        public FeatureTexture(SiItemType itemType, String feature, String defaultValue, Collection<String> featureValues) {
            super(itemType);
            this.feature = feature;
            this.defaultValue = defaultValue;
            featureValues = new ArrayList<>(featureValues);
            if (!featureValues.contains(defaultValue)) featureValues.add(defaultValue);
            for (String featureValue : featureValues) {
                customModelDataRecords.put(featureValue, new SiCustomModelDataRecord(itemType.getMaterial(), customModelDataCategory, itemType.getName() + "." + feature + "." + featureValue, SiNode.emptyMap().set(feature, featureValue)));
            }
        }

        @Override
        public int getCustomModelData(SiItemStack itemStack) {
            String featureValue = itemStack.getTagsManager().getString(feature);
            if (featureValue == null || !customModelDataRecords.containsKey(featureValue)) featureValue = defaultValue;
            return customModelDataRecords.get(featureValue).getCustomModelData();
        }
    }
}
