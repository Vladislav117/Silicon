package ru.vladislav117.silicon.item;

import com.google.common.collect.Multimap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.Nullable;
import ru.vladislav117.silicon.color.SiColor;
import ru.vladislav117.silicon.color.SiPalette;
import ru.vladislav117.silicon.tags.SiTagsManager;
import ru.vladislav117.silicon.text.SiTextLike;

import java.util.*;

/**
 * Класс для более удобной работы со стаками предметов.
 */
public class SiItemStack {
    protected ItemStack stack;
    protected ItemMeta meta;
    protected SiTagsManager tagsManager;

    /**
     * Создание стака на основе существующего стака.
     *
     * @param itemStack Стак
     */
    public SiItemStack(@Nullable ItemStack itemStack) {
        if (itemStack == null) itemStack = new ItemStack(Material.AIR);
        stack = itemStack.clone();
        meta = stack.getItemMeta() == null ? new ItemStack(Material.STONE).getItemMeta() : stack.getItemMeta();
        tagsManager = new SiTagsManager(meta.getPersistentDataContainer());
    }

    /**
     * Создание стака из материала и количества предметов.
     *
     * @param material Материал стака
     * @param amount   Количество предметов
     */
    public SiItemStack(Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    /**
     * Создание стака из материала с количеством предметов 1.
     *
     * @param material Материал стака
     */
    public SiItemStack(Material material) {
        this(material, 1);
    }

    /**
     * Получение материала стака.
     *
     * @return Материал стака.
     */
    public Material getMaterial() {
        return stack.getType();
    }

    /**
     * Получение количества предметов в стаке.
     *
     * @return Количество предметов в стаке.
     */
    public int getAmount() {
        return stack.getAmount();
    }

    /**
     * Установка количества предметов в стаке.
     *
     * @param amount Количество предметов в стаке
     * @return Этот же стак.
     */
    public SiItemStack setAmount(int amount) {
        stack.setAmount(amount);
        return this;
    }

    /**
     * Получение меты стака.
     *
     * @return Мета стака.
     */
    public ItemMeta getMeta() {
        return meta;
    }

    /**
     * Получение менеджера тегов стака.
     *
     * @return Менеджер тегов стака.
     */
    public SiTagsManager getTagsManager() {
        return tagsManager;
    }

    /**
     * Установка отображаемого имени стака.
     *
     * @param displayName Отображаемое имя стака
     * @return Этот же стак.
     */
    public SiItemStack setDisplayName(SiTextLike displayName) {
        meta.displayName(Component.empty().append(displayName.toComponent()).style(Style.empty().decoration(TextDecoration.ITALIC, false)));
        return this;
    }

    /**
     * Установка описания стака.
     *
     * @param description Описание стака
     * @return Этот же стак.
     */
    public SiItemStack setDescription(SiTextLike... description) {
        ArrayList<Component> lore = new ArrayList<>();
        for (SiTextLike text : description) {
            lore.add(Component.empty().append(text.toComponent()).style(Style.empty().decoration(TextDecoration.ITALIC, false).color(SiPalette.Defaults.white.toTextColor())));
        }
        meta.lore(lore);
        return this;
    }

    /**
     * Установка описания стака.
     *
     * @param description Описание стака
     * @return Этот же стак.
     */
    public SiItemStack setDescription(Collection<SiTextLike> description) {
        return setDescription(description.toArray(new SiTextLike[0]));
    }

    /**
     * Установка CustomModelData.
     *
     * @param customModelData CustomModelData или null, если её нужно убрать
     * @return Этот же стак.
     */
    public SiItemStack setCustomModelData(Integer customModelData) {
        meta.setCustomModelData(customModelData);
        return this;
    }

    /**
     * Проверка на наличие зачарований у стака.
     *
     * @param enchantments Зачарования
     * @return Наличие всех указанных зачарований.
     */
    public boolean hasEnchantments(Enchantment... enchantments) {
        for (Enchantment enchantment : enchantments) {
            if (!meta.hasEnchant(enchantment)) return false;
        }
        return true;
    }

    /**
     * Проверка на наличие зачарований у стака.
     *
     * @param enchantments Зачарования
     * @return Наличие всех указанных зачарований.
     */
    public boolean hasEnchantments(Collection<Enchantment> enchantments) {
        return hasEnchantments(enchantments.toArray(new Enchantment[0]));
    }

    /**
     * Получение уровня зачарования.
     *
     * @param enchantment Зачарование
     * @return Уровень зачарования или 0, если зачарования нет.
     */
    public int getEnchantmentLevel(Enchantment enchantment) {
        return meta.getEnchantLevel(enchantment);
    }

    /**
     * Установка зачарования.
     *
     * @param enchantment Зачарование
     * @param level       Уровень
     * @return Этот же стак.
     */
    public SiItemStack setEnchantment(Enchantment enchantment, int level) {
        if (meta.hasEnchant(enchantment)) meta.removeEnchant(enchantment);
        meta.addEnchant(enchantment, level, true);
        return this;
    }

    /**
     * Установка зачарований.
     *
     * @param enchantments Таблица вида "зачарование: уровень"
     * @return Этот же стак.
     */
    public SiItemStack setEnchantments(Map<Enchantment, Integer> enchantments) {
        for (Enchantment enchantment : enchantments.keySet()) {
            setEnchantment(enchantment, enchantments.get(enchantment));
        }
        return this;
    }

    /**
     * Удаление зачарования.
     *
     * @param enchantment Зачарование
     * @return Этот же стак.
     */
    public SiItemStack removeEnchantment(Enchantment enchantment) {
        meta.removeEnchant(enchantment);
        return this;
    }

    /**
     * Получение модификаторов атрибутов стака.
     *
     * @return Список модификаторов атрибутов стака.
     */
    public ArrayList<SiAttributeModifier> getAttributeModifiers() {
        ArrayList<SiAttributeModifier> attributeModifiers = new ArrayList<>();
        Multimap<Attribute, AttributeModifier> attributeModifierMultimap = meta.getAttributeModifiers();
        if (attributeModifierMultimap == null) return attributeModifiers;
        for (Attribute attribute : attributeModifierMultimap.keySet()) {
            for (AttributeModifier attributeModifier : attributeModifierMultimap.get(attribute)) {
                attributeModifiers.add(new SiAttributeModifier(attributeModifier.getName(), attribute, attributeModifier.getAmount(), attributeModifier.getOperation(), attributeModifier.getSlotGroup()));
            }
        }
        return attributeModifiers;
    }

    /**
     * Добавление модификаторов атрибутов в стак.
     *
     * @param attributeModifiers Модификаторы атрибутов
     * @return Этот же стак.
     */
    public SiItemStack addAttributeModifiers(SiAttributeModifier... attributeModifiers) {
        for (SiAttributeModifier attributeModifier : attributeModifiers) {
            meta.addAttributeModifier(attributeModifier.getAttribute(), attributeModifier.buildAttributeModifier());
        }
        return this;
    }

    /**
     * Добавление модификаторов атрибутов в стак.
     *
     * @param attributeModifiers Модификаторы атрибутов
     * @return Этот же стак.
     */
    public SiItemStack addAttributeModifiers(Collection<SiAttributeModifier> attributeModifiers) {
        return addAttributeModifiers(attributeModifiers.toArray(new SiAttributeModifier[0]));
    }

    /**
     * Удаление модификаторов атрибутов по атрибуту.
     *
     * @param attributes Атрибуты, модификаторы которых будут удалены
     * @return Этот же стак.
     */
    public SiItemStack removeAttributeModifiers(Attribute... attributes) {
        for (Attribute attribute : attributes) meta.removeAttributeModifier(attribute);
        return this;
    }

    /**
     * Удаление модификаторов атрибутов по атрибуту.
     *
     * @param attributes Атрибуты, модификаторы которых будут удалены
     * @return Этот же стак.
     */
    public SiItemStack removeAttributeModifiers(Collection<Attribute> attributes) {
        return removeAttributeModifiers(attributes.toArray(new Attribute[0]));
    }

    /**
     * Удаление модификаторов атрибутов по слоту.
     *
     * @param slots Слоты, модификаторы атрибутов которых будут удалены
     * @return Этот же стак.
     */
    public SiItemStack removeAttributeModifiersBySlots(EquipmentSlot... slots) {
        for (EquipmentSlot slot : slots) meta.removeAttributeModifier(slot);
        return this;
    }

    /**
     * Удаление модификаторов атрибутов по слоту.
     *
     * @param slots Слоты, модификаторы атрибутов которых будут удалены
     * @return Этот же стак.
     */
    public SiItemStack removeAttributeModifiersBySlots(Collection<EquipmentSlot> slots) {
        return removeAttributeModifiersBySlots(slots.toArray(new EquipmentSlot[0]));
    }

    /**
     * Получение всех флагов стака.
     *
     * @return Флаги стака.
     */
    public Set<ItemFlag> getFlags() {
        return meta.getItemFlags();
    }

    /**
     * Добавление флагов стаку.
     *
     * @param flags Флаги
     * @return Этот же стак.
     */
    public SiItemStack addFlags(ItemFlag... flags) {
        meta.addItemFlags(flags);
        return this;
    }

    /**
     * Добавление флагов стаку.
     *
     * @param flags Флаги
     * @return Этот же стак.
     */
    public SiItemStack addFlags(Collection<ItemFlag> flags) {
        return addFlags(flags.toArray(new ItemFlag[0]));
    }

    /**
     * Удаление флагов.
     *
     * @param flags Флаги
     * @return Этот же стак.
     */
    public SiItemStack removeFlags(ItemFlag... flags) {
        meta.removeItemFlags(flags);
        return this;
    }

    /**
     * Удаление флагов.
     *
     * @param flags Флаги
     * @return Этот же стак.
     */
    public SiItemStack removeFlags(Collection<ItemFlag> flags) {
        return removeFlags(flags.toArray(new ItemFlag[0]));
    }

    /**
     * Получение типа предмета.
     *
     * @return Тип предмета или unknown тип.
     */
    public SiItemType getItemType() {
        return SiItemTypes.all.get(tagsManager.getString("item_type", SiItemTypes.unknown.getName()), SiItemTypes.unknown);
    }

    /**
     * Обновить имя, описание и CustomModelData в зависимости от типа предмета.
     *
     * @return Этот же стак.
     */
    public SiItemStack updateDisplay() {
        SiItemType itemType = getItemType();
        if (itemType.isUnknown()) return this;
        List<SiTextLike> description = itemType.getDisplay() == null ? new ArrayList<>() : new ArrayList<>(itemType.getDisplay().getDescription(this));
        if (itemType.getMainCategory().isDisplays()){
            description.add(0, itemType.getMainCategory().getDisplayNameForItemDescription());
        }
        setDescription(description);
        if (itemType.getDisplay() != null) {
            setDisplayName(itemType.getDisplay().getDisplayName(this));
            if (meta instanceof PotionMeta) {
                SiColor color = itemType.getDisplay().getColor(this);
                if (color != null) setPotionColor(color);
            }
            if (meta instanceof ColorableArmorMeta) {
                SiColor color = itemType.getDisplay().getColor(this);
                if (color != null) setArmorColor(color);
            }
        }
        if (itemType.getTexture() != null) {
            setCustomModelData(itemType.getTexture().getCustomModelData(this));
        }
        return this;
    }

    /**
     * Установить цвет зелья. Если предмет не является зельем, то ничего не произойдёт.
     *
     * @param color Цвет
     * @return Этот же стак.
     */
    public SiItemStack setPotionColor(SiColor color) {
        if (!(meta instanceof PotionMeta potionMeta)) return this;
        potionMeta.setColor(color.toBukkitColor());
        return this;
    }

    /**
     * Установить цвет брони. Если предмет не является бронёй, то ничего не произойдёт.
     *
     * @param color Цвет
     * @return Этот же стак.
     */
    public SiItemStack setArmorColor(SiColor color) {
        if (!(meta instanceof ColorableArmorMeta colorableArmorMeta)) return this;
        colorableArmorMeta.setColor(color.toBukkitColor());
        return this;
    }

    /**
     * Установить снаряд арбалета. Если предмет не является арбалетом, то ничего не произойдёт.
     *
     * @param itemStack Снаряд
     * @return Этот же стак.
     */
    public SiItemStack setCrossbowProjectile(ItemStack itemStack) {
        if (!(meta instanceof CrossbowMeta crossbowMeta)) return this;
        crossbowMeta.addChargedProjectile(itemStack);
        return this;
    }

    /**
     * Установить снаряд арбалета. Если предмет не является арбалетом, то ничего не произойдёт.
     *
     * @param itemStack Снаряд
     * @return Этот же стак.
     */
    public SiItemStack setCrossbowProjectile(SiItemStack itemStack) {
        return setCrossbowProjectile(itemStack.toItemStack());
    }

    /**
     * Получение прочности предмета.
     *
     * @return Прочность предмета или -1, если предмет не имеет прочности.
     */
    public int getDurability() {
        if (!(meta instanceof Damageable damageable)) return -1;
        return damageable.getDamage();
    }

    /**
     * Установка прочности предмета. Если предмет не имеет прочности, то ничего не произойдёт.
     *
     * @param durability Прочность предмета.
     * @return Этот же стак.
     */
    public SiItemStack setDurability(int durability) {
        if (!(meta instanceof Damageable damageable)) return this;
        damageable.setDamage(stack.getType().getMaxDurability() - durability);
        return this;
    }

    /**
     * Преобразование стака к ItemStack.
     *
     * @return ItemStack из этого стака.
     */
    public ItemStack toItemStack() {
        stack.setItemMeta(meta);
        return stack;
    }
}
