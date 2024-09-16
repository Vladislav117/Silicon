package ru.vladislav117.silicon.item;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.Nullable;
import ru.vladislav117.silicon.namespace.SiNamespace;

import java.util.UUID;

/**
 * Модификатор атрибутов.
 */
public class SiAttributeModifier {
    @Nullable
    protected String name = null;
    protected Attribute attribute;
    protected double value = 1;
    protected AttributeModifier.Operation operation = AttributeModifier.Operation.MULTIPLY_SCALAR_1;
    @Nullable
    protected EquipmentSlotGroup slot = null;

    /**
     * Создание нового модификатора атрибутов.
     *
     * @param name      Имя или null. Если задано null, то будет взято имя атрибута
     * @param attribute Атрибут
     * @param value     Значение атрибута
     * @param operation Операция со значением
     * @param slot      Слот применения атрибута или null. Если задано null, атрибут применяется во всех слотах
     */
    public SiAttributeModifier(@Nullable String name, Attribute attribute, double value, AttributeModifier.Operation operation, @Nullable EquipmentSlotGroup slot) {
        this.name = name;
        this.attribute = attribute;
        this.value = value;
        this.operation = operation;
        this.slot = slot;
    }

    /**
     * Создание нового модификатора атрибутов.
     *
     * @param attribute Атрибут
     * @param value     Значение атрибута
     * @param operation Операция со значением
     * @param slot      Слот применения атрибута или null. Если задано null, атрибут применяется во всех слотах
     */
    public SiAttributeModifier(Attribute attribute, double value, AttributeModifier.Operation operation, @Nullable EquipmentSlotGroup slot) {
        this.attribute = attribute;
        this.value = value;
        this.operation = operation;
        this.slot = slot;
    }

    /**
     * Создание нового модификатора атрибутов. Будет применена операция "Multiply Scalar 1".
     *
     * @param attribute Атрибут
     * @param value     Значение атрибута
     * @param slot      Слот применения атрибута или null. Если задано null, атрибут применяется во всех слотах
     */
    public SiAttributeModifier(Attribute attribute, double value, @Nullable EquipmentSlotGroup slot) {
        this.attribute = attribute;
        this.value = value;
        this.slot = slot;
    }

    /**
     * Создание нового модификатора атрибутов. Будет применена операция "Multiply Scalar 1".
     * Модификатор будет применяться в любом слоту.
     *
     * @param attribute Атрибут
     * @param value     Значение атрибута
     */
    public SiAttributeModifier(Attribute attribute, double value) {
        this.attribute = attribute;
        this.value = value;
    }

    /**
     * Получение имени модификатора атрибутов.
     *
     * @return Имя модификатора атрибутов.
     */
    public String getName() {
        return name == null ? attribute.name() : name;
    }

    /**
     * Установка имени модификатора атрибутов.
     *
     * @param name Имя или null. Если задано null, то будет взято имя атрибута
     * @return Этот же модификатор атрибутов.
     */
    public SiAttributeModifier setName(@Nullable String name) {
        this.name = name;
        return this;
    }

    /**
     * Получение атрибута модификатора атрибутов.
     *
     * @return Атрибут модификатора атрибутов.
     */
    public Attribute getAttribute() {
        return attribute;
    }

    /**
     * Установка атрибута модификатора атрибутов.
     *
     * @param attribute Атрибут
     * @return Этот же модификатор атрибутов.
     */
    public SiAttributeModifier setAttribute(Attribute attribute) {
        this.attribute = attribute;
        return this;
    }

    /**
     * Получение значения модификатора атрибутов.
     *
     * @return Значение модификатора атрибутов.
     */
    public double getValue() {
        return value;
    }

    /**
     * Установка значения модификатора атрибутов.
     *
     * @param value Значение
     * @return Этот же модификатор атрибутов.
     */
    public SiAttributeModifier setValue(double value) {
        this.value = value;
        return this;
    }

    /**
     * Получение операции модификатора атрибутов.
     *
     * @return Операция модификатора атрибутов.
     */
    public AttributeModifier.Operation getOperation() {
        return operation;
    }

    /**
     * Установка операции модификатора атрибутов.
     *
     * @param operation Операция
     * @return Этот же модификатор атрибутов.
     */
    public SiAttributeModifier setOperation(AttributeModifier.Operation operation) {
        this.operation = operation;
        return this;
    }

    /**
     * Получение слота модификатора атрибутов.
     *
     * @return Слот или null, если не задан.
     */
    @Nullable
    public EquipmentSlotGroup getSlot() {
        return slot;
    }

    /**
     * Установка слота модификатора атрибутов.
     *
     * @param slot Слот или null, если нужно задать любой слот
     * @return Этот же модификатор атрибутов.
     */
    public SiAttributeModifier setSlot(@Nullable EquipmentSlotGroup slot) {
        this.slot = slot;
        return this;
    }

    /**
     * Создание модификатора на основе объекта.
     *
     * @return Модификатор атрибутов.
     */
    public AttributeModifier buildAttributeModifier() {
        String name = this.name;
        if (name == null) name = attribute.name();
        if (slot == null) return new AttributeModifier(SiNamespace.getKey(name), value, operation);
        return new AttributeModifier(SiNamespace.getKey(name), value, operation, slot);
    }
}
