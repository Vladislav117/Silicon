package ru.vladislav117.silicon.store;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import ru.vladislav117.silicon.color.SiPalette;
import ru.vladislav117.silicon.function.SiDoubleHandlerFunction;
import ru.vladislav117.silicon.item.SiItemStack;
import ru.vladislav117.silicon.menu.SiMenuElement;
import ru.vladislav117.silicon.node.SiNode;
import ru.vladislav117.silicon.text.SiText;
import ru.vladislav117.silicon.text.SiTextLike;
import ru.vladislav117.silicon.text.pattern.feature.SiFeaturePattern;
import ru.vladislav117.silicon.text.structure.SiLinedText;

import java.util.ArrayList;

/**
 * Позиция лавки.
 */
public class SiStorePosition {
    protected String name;
    protected int cost;
    protected SiItemStack itemStack;
    protected SiTextLike displayName;
    protected ArrayList<SiTextLike> description;
    protected boolean singlePurchase = false;
    protected SiDoubleHandlerFunction<SiStorePosition, Player> buyHandler;
    protected SiStore store;

    /**
     * Создание позиции лавки.
     *
     * @param store     Лавка
     * @param name      Имя
     * @param cost      Стоимость
     * @param itemStack Отображаемый предмет
     */
    public SiStorePosition(SiStore store, String name, int cost, SiItemStack itemStack) {
        this.store = store;
        store.getPositions().put(name, this);
        store.getOrderedPositions().add(this);
        this.name = name;
        this.cost = cost;
        this.itemStack = itemStack;
    }

    /**
     * Создание позиции лавки.
     *
     * @param store    Лавка
     * @param name     Имя
     * @param cost     Стоимость
     * @param material Отображаемый предмет
     */
    public SiStorePosition(SiStore store, String name, int cost, Material material) {
        this.store = store;
        store.getPositions().put(name, this);
        store.getOrderedPositions().add(this);
        this.name = name;
        this.cost = cost;
        itemStack = new SiItemStack(material);
    }

    /**
     * Получение имени.
     *
     * @return Имя.
     */
    public String getName() {
        return name;
    }

    /**
     * Получение отображаемого имени.
     *
     * @return Отображаемое имя.
     */
    public SiTextLike getDisplayName() {
        return displayName;
    }

    /**
     * Установка отображаемого имени.
     *
     * @param displayName Отображаемое имя
     * @return Эта же позиция.
     */
    protected SiStorePosition setDisplayName(SiTextLike displayName) {
        this.displayName = displayName;
        return this;
    }

    /**
     * Установка отображаемого имени.
     *
     * @param displayName Отображаемое имя
     * @return Эта же позиция.
     */
    protected SiStorePosition setDisplayName(String displayName) {
        this.displayName = SiText.string(displayName);
        return this;
    }

    /**
     * Получение описания.
     *
     * @return Описание.
     */
    public ArrayList<SiTextLike> getDescription() {
        return description;
    }

    /**
     * Установка описания.
     *
     * @param description Описание
     * @return Эта же позиция.
     */
    protected SiStorePosition setDescription(String description) {
        this.description = new SiLinedText(description).getCompleteTextParts();
        return this;
    }

    /**
     * Установка описания.
     *
     * @param description Описание
     * @return Эта же позиция.
     */
    protected SiStorePosition setDescription(ArrayList<SiTextLike> description) {
        this.description = description;
        return this;
    }

    /**
     * Получение стоимости.
     *
     * @return Стоимость.
     */
    public int getCost() {
        return cost;
    }

    /**
     * Единичная покупка.
     *
     * @return Является ли товар единичной покупкой.
     */
    public boolean isSinglePurchase() {
        return singlePurchase;
    }

    /**
     * Получение количества покупок игроком.
     *
     * @param player Игрок
     * @return Количество покупок игроком.
     */
    public int getBuyCount(Player player) {
        SiNode positionBuys = store.getData().getOrNull(name);
        if (positionBuys == null) return 0;
        return positionBuys.getInteger(player.getName(), 0);
    }

    /**
     * Получение обработчика покупки.
     *
     * @return Обработчик покупки.
     */
    public SiDoubleHandlerFunction<SiStorePosition, Player> getBuyHandler() {
        return buyHandler;
    }

    /**
     * Установка обработчика покупки.
     *
     * @param buyHandler Обработчик покупки
     * @return Эта же позиция.
     */
    protected SiStorePosition setBuyHandler(SiDoubleHandlerFunction<SiStorePosition, Player> buyHandler) {
        this.buyHandler = buyHandler;
        return this;
    }

    /**
     * Создание элемента меню.
     *
     * @param player Игрок
     * @return Элемент меню.
     */
    public SiMenuElement toElement(Player player) {
        SiItemStack stack = new SiItemStack(itemStack.toItemStack().clone());
        stack.setDisplayName(displayName);
        ArrayList<SiTextLike> desc = new ArrayList<>(description);
        if (getBuyCount(player) > 0 && singlePurchase) {
            desc.add(SiText.string("Вы уже купили это", SiPalette.Interface.red));
        } else {
            desc.add(new SiFeaturePattern("Стоимость", cost + store.getCurrency().getSymbol()).build());
        }
        stack.setDescription(desc);
        return new SiMenuElement(player.getName() + "_buy_" + name) {{
            setItemStack(stack);
            setClickHandler((p, i, event) -> {
                if (getBuyCount(p) > 0 && singlePurchase) {
                    SiText.string("Вы уже купили это", SiPalette.Interface.red).toMessageTask().addPlayer(p).send();
                    return;
                }
                if (store.getCurrency().getBalance(p.getName()) < cost) {
                    SiText.string("У вас недостаточно средств", SiPalette.Interface.red).toMessageTask().addPlayer(p).send();
                    return;
                }
                store.getCurrency().addBalance(p.getName(), -cost);
                buyHandler.handle(SiStorePosition.this, p);
                SiNode positionBuys = store.getData().getOrNull(SiStorePosition.this.name);
                if (positionBuys == null) store.getData().set(SiStorePosition.this.name, SiNode.emptyMap());
                store.getData().getOrNull(SiStorePosition.this.name).set(p.getName(), store.getData().getOrThrown(SiStorePosition.this.name).getInteger(p.getName(), 0) + 1);
                store.save();
                store.open(p);
            });
        }};
    }
}
