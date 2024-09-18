package ru.vladislav117.silicon.displayEntity;

import ru.vladislav117.silicon.cusomModelData.SiCustomModelDataRecord;
import ru.vladislav117.silicon.node.SiNode;

import java.util.Collection;
import java.util.HashMap;

/**
 * Текстура для DisplayEntity.
 */
public abstract class SiDisplayEntityTexture {
    public static final String customModelDataCategory = "display_entity";
    protected SiDisplayEntityType displayEntityType;

    /**
     * Создание текстуры DisplayEntity.
     *
     * @param displayEntityType Тип DisplayEntity
     */
    public SiDisplayEntityTexture(SiDisplayEntityType displayEntityType) {
        this.displayEntityType = displayEntityType;
    }

    /**
     * Получение типа DisplayEntity.
     *
     * @return Тип DisplayEntity.
     */
    public SiDisplayEntityType getDisplayEntityType() {
        return displayEntityType;
    }

    /**
     * Получение CustomModelData в зависимости от DisplayEntity.
     *
     * @param displayEntity DisplayEntity
     * @return CustomModelData.
     */
    public abstract int getCustomModelData(SiDisplayEntity displayEntity);

    /**
     * Статическая текстура DisplayEntity.
     */
    public static class StaticTexture extends SiDisplayEntityTexture {
        protected SiCustomModelDataRecord customModelDataRecord;

        /**
         * Создание текстуры DisplayEntity.
         *
         * @param displayEntityType Тип DisplayEntity
         */
        public StaticTexture(SiDisplayEntityType displayEntityType) {
            super(displayEntityType);
            customModelDataRecord = new SiCustomModelDataRecord(displayEntityType.getMaterial(), customModelDataCategory, displayEntityType.getName());
        }

        @Override
        public int getCustomModelData(SiDisplayEntity itemStack) {
            return customModelDataRecord.getCustomModelData();
        }
    }

    /**
     * Текстура DisplayEntity, зависящая от свойства.
     */
    public static class FeatureTexture extends SiDisplayEntityTexture {
        protected String feature;
        protected String defaultValue;
        protected HashMap<String, SiCustomModelDataRecord> customModelDataRecords = new HashMap<>();

        /**
         * Создание текстуры предмета.
         *
         * @param displayEntityType Тип DisplayEntity
         * @param feature           Название свойства
         * @param defaultValue      Значение свойства по умолчанию
         * @param featureValues     Все возможные значения свойств
         */
        public FeatureTexture(SiDisplayEntityType displayEntityType, String feature, String defaultValue, Collection<String> featureValues) {
            super(displayEntityType);
            this.feature = feature;
            this.defaultValue = defaultValue;
            if (!featureValues.contains(defaultValue)) {
                customModelDataRecords.put(defaultValue, new SiCustomModelDataRecord(displayEntityType.getMaterial(), customModelDataCategory, displayEntityType.getName() + "." + feature + "." + defaultValue, SiNode.emptyMap().set(feature, defaultValue)));
            }
            for (String featureValue : featureValues) {
                customModelDataRecords.put(featureValue, new SiCustomModelDataRecord(displayEntityType.getMaterial(), customModelDataCategory, displayEntityType.getName() + "." + feature + "." + featureValue, SiNode.emptyMap().set(feature, featureValue)));
            }
        }

        @Override
        public int getCustomModelData(SiDisplayEntity displayEntity) {
            String featureValue = displayEntity.getTags().getString(feature);
            if (featureValue == null || !customModelDataRecords.containsKey(featureValue)) featureValue = defaultValue;
            return customModelDataRecords.get(featureValue).getCustomModelData();
        }
    }
}
