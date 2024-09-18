package ru.vladislav117.silicon.shop;

import org.bukkit.inventory.ItemStack;
import ru.vladislav117.silicon.node.SiNode;

import java.util.Base64;

/**
 * Позиция магазина.
 */
public class SiShopPosition {
    protected String uuid;
    protected String owner;
    protected long date;
    protected int cost;
    protected ItemStack itemStack;
    protected String world;

    /**
     * Создание позиции магазина.
     *
     * @param uuid      UUID
     * @param owner     Владелец
     * @param date      Дата
     * @param cost      Стоимость
     * @param itemStack Предмет
     * @param world     Мир
     */
    public SiShopPosition(String uuid, String owner, long date, int cost, ItemStack itemStack, String world) {
        this.uuid = uuid;
        this.owner = owner;
        this.date = date;
        this.cost = cost;
        this.itemStack = itemStack;
        this.world = world;
    }

    /**
     * Создание позиции магазина из узла.
     *
     * @param node Узел
     * @return Позиция из узла.
     */
    public static SiShopPosition parseNode(SiNode node) {
        return new SiShopPosition(node.getString("uuid"), node.getString("owner"), Long.parseLong(node.getString("date")), node.getInteger("cost"), ItemStack.deserializeBytes(Base64.getDecoder().decode(node.getString("item_stack"))), node.getString("world"));
    }

    /**
     * Получение UUID.
     *
     * @return UUID.
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Получение владельца.
     *
     * @return владелец.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Получение даты.
     *
     * @return дата.
     */
    public long getDate() {
        return date;
    }

    /**
     * Получение стоимости.
     *
     * @return стоимость.
     */
    public int getCost() {
        return cost;
    }

    /**
     * Получение предмета.
     *
     * @return предмет.
     */
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * Получение мира.
     *
     * @return мир.
     */
    public String getWorld() {
        return world;
    }

    /**
     * Преобразование позиции к узлу.
     *
     * @return Узел из позиции.
     */
    public SiNode toNode() {
        SiNode node = SiNode.emptyMap();
        node.set("uuid", uuid);
        node.set("owner", owner);
        node.set("date", String.valueOf(date));
        node.set("cost", cost);
        node.set("item_stack", Base64.getEncoder().encodeToString(itemStack.serializeAsBytes()));
        node.set("world", world);
        return node;
    }
}
