package ru.vladislav117.silicon.region;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import ru.vladislav117.silicon.color.SiPalette;
import ru.vladislav117.silicon.file.SiFile;
import ru.vladislav117.silicon.icon.SiIcon;
import ru.vladislav117.silicon.inventory.SiPlayerInventoryUtils;
import ru.vladislav117.silicon.item.SiItemType;
import ru.vladislav117.silicon.item.SiItemTypes;
import ru.vladislav117.silicon.menu.SiMenu;
import ru.vladislav117.silicon.menu.SiMenuElement;
import ru.vladislav117.silicon.menu.SiMenus;
import ru.vladislav117.silicon.node.SiNode;
import ru.vladislav117.silicon.text.SiText;
import ru.vladislav117.silicon.text.SiTextLike;
import ru.vladislav117.silicon.ticker.SiTicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Регион. Имеет свои флаги, членов и владельцев.
 */
public class SiRegion {
    public static SiIcon removeRegionIcon;
    protected String name;
    protected Location location;
    protected Vector size;
    protected BoundingBox boundingBox;
    protected HashSet<String> owners = new HashSet<>();
    protected HashSet<String> members = new HashSet<>();
    protected HashMap<String, Boolean> flags = new HashMap<>();
    protected @Nullable String itemTypeName = null;

    /**
     * Создание нового региона.
     *
     * @param name     Имя
     * @param location Позиция (нижняя точка)
     * @param size     Размер
     */
    public SiRegion(String name, Location location, Vector size) {
        this.name = name;
        this.location = location;
        this.size = size;
        boundingBox = new BoundingBox(location.getX(), location.getY(), location.getZ(), location.getX() + size.getX(), location.getY() + size.getY(), location.getZ() + size.getZ());
        SiRegions.addRegion(this);
        save();
    }

    /**
     * Загрузка региона из узла
     *
     * @param node Узел
     * @return Загруженный регион.
     */
    public static SiRegion load(SiNode node) {
        SiRegion region = new SiRegion(node.getString("name"), new Location(Bukkit.getWorld(node.getString("world")), node.getDouble("x"), node.getDouble("y"), node.getDouble("z")), new Vector(node.getDouble("width"), node.getDouble("height"), node.getDouble("length")));
        node.getOrThrown("members").forEach(member -> {
            region.getMembers().add(member.asString());
        });
        node.getOrThrown("owners").forEach(owner -> {
            region.getOwners().add(owner.asString());
        });
        node.getOrThrown("flags").forEach((flag, value) -> {
            region.getFlags().put(flag, value.asBoolean());
        });
        if (node.hasChild("item_type")) region.itemTypeName = node.getString("item_type");
        return region.save();
    }

    /**
     * Получение имени региона.
     *
     * @return Имя региона.
     */
    public String getName() {
        return name;
    }

    /**
     * Получение позиции региона.
     *
     * @return Позиция региона.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Получение размера региона.
     *
     * @return Размер региона.
     */
    public Vector getSize() {
        return size;
    }

    /**
     * Получение хитбокса региона.
     *
     * @return Хитбокс региона.
     */
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    /**
     * Получение владельцев региона.
     *
     * @return Владельцы региона.
     */
    public HashSet<String> getOwners() {
        return owners;
    }

    /**
     * Получение членов региона.
     *
     * @return Члены региона.
     */
    public HashSet<String> getMembers() {
        return members;
    }

    /**
     * Получение флагов региона.
     *
     * @return Флаги региона.
     */
    public HashMap<String, Boolean> getFlags() {
        return flags;
    }

    /**
     * Получение значения флага региона.
     *
     * @param flag Флаг
     * @return Значение флага.
     */
    public boolean getFlag(SiRegionFlag flag) {
        return flags.getOrDefault(flag.getName(), flag.getDefaultValue());
    }

    /**
     * Получение значения флага региона для игрока.
     *
     * @param player Игрок
     * @param flag   Флаг
     * @return Значение флага.
     */
    public boolean getFlag(String player, SiRegionFlag flag) {
        return isMember(player) || flags.getOrDefault(flag.getName(), flag.getDefaultValue());
    }

    /**
     * Получение значения флага региона для игрока.
     *
     * @param player Игрок
     * @param flag   Флаг
     * @return Значение флага.
     */
    public boolean getFlag(Player player, SiRegionFlag flag) {
        return isMember(player) || flags.getOrDefault(flag.getName(), flag.getDefaultValue());
    }

    /**
     * Проверка, является ли игрок членом региона.
     *
     * @param player Игрок
     * @return Является ли игрок членом региона.
     */
    public boolean isMember(String player) {
        return members.contains(player);
    }

    /**
     * Проверка, является ли игрок членом региона.
     *
     * @param player Игрок
     * @return Является ли игрок членом региона.
     */
    public boolean isMember(Player player) {
        return members.contains(player.getName());
    }

    /**
     * Проверка, является ли игрок владельцем региона.
     *
     * @param player Игрок
     * @return Является ли игрок владельцем региона.
     */
    public boolean isOwner(String player) {
        return owners.contains(player);
    }

    /**
     * Проверка, является ли игрок владельцем региона.
     *
     * @param player Игрок
     * @return Является ли игрок владельцем региона.
     */
    public boolean isOwner(Player player) {
        return owners.contains(player.getName());
    }

    /**
     * Проверка, находится ли точка внутри региона.
     *
     * @param vector Точка
     * @return Находится ли точка внутри региона.
     */
    public boolean isInRegion(Vector vector) {
        return boundingBox.contains(vector);
    }

    /**
     * Проверка, находится ли позиция внутри региона.
     *
     * @param location Позиция
     * @return Находится ли позиция внутри региона.
     */
    public boolean isInRegion(Location location) {
        return location.getWorld().equals(this.location.getWorld()) && isInRegion(location.toVector());
    }

    /**
     * Получение типа предмета.
     *
     * @return Тип предмета или null, если регион создавался не через предмет.
     */
    @Nullable
    public String getItemTypeName() {
        return itemTypeName;
    }

    /**
     * Установка типа предмета.
     *
     * @param itemTypeName Название типа предмета
     * @return Этот же регион.
     */
    public SiRegion setItemTypeName(@Nullable String itemTypeName) {
        this.itemTypeName = itemTypeName;
        return this;
    }

    /**
     * Преобразование региона в узел.
     *
     * @return Узел.
     */
    public SiNode toNode() {
        SiNode node = SiNode.emptyMap();
        node.set("name", name);
        node.set("owners", new ArrayList<>());
        for (String owner : owners) node.getOrThrown("owners").add(owner);
        node.set("members", new ArrayList<>());
        for (String member : members) node.getOrThrown("members").add(member);
        node.set("flags", new HashMap<>());
        for (String flag : flags.keySet()) node.getOrThrown("flags").set(flag, flags.get(flag));
        node.set("x", location.getX());
        node.set("y", location.getY());
        node.set("z", location.getZ());
        node.set("width", size.getX());
        node.set("height", size.getY());
        node.set("length", size.getZ());
        node.set("world", location.getWorld().getName());
        if (itemTypeName != null) node.set("item_type", itemTypeName);
        return node;
    }

    /**
     * Получение файла региона.
     *
     * @return Файл региона.
     */
    public SiFile getFile() {
        return SiRegions.directory.getChild(name + ".json");
    }

    /**
     * Сохранение региона.
     *
     * @return Этот же регион.
     */
    public SiRegion save() {
        getFile().writeNode(toNode());
        return this;
    }

    /**
     * Удаление региона
     *
     * @return Этот же регион.
     */
    public SiRegion remove() {
        SiRegions.remove(this);
        getFile().delete();
        return this;
    }

    /**
     * Создание меню региона.
     *
     * @param owner Меню для владельца
     * @return Меню управления регионом.
     */
    public SiMenu buildMenu(boolean owner) {
        ArrayList<SiMenuElement> elements = new ArrayList<>();
        if (owner) {
            SiMenuElement removeRegionElement = removeRegionIcon.buildMenuElement(name + "_remove_region");
            removeRegionElement.setDisplayName(SiText.string("Удалить регион", SiPalette.Interface.red));
            removeRegionElement.setDescription(SiText.string("ПКМ для удаления", SiPalette.Interface.red));
            removeRegionElement.setClickHandler((player, itemStack, event) -> {
                if (!event.isRightClick()) return;
                remove();
                if (itemTypeName != null) {
                    SiItemType itemType = SiItemTypes.all.get(itemTypeName);
                    if (itemType != null) {
                        int radius = (int) (size.getX() / 2);
                        SiPlayerInventoryUtils.give(player, itemType.buildItemStack(1, new HashMap<>() {{
                            put("region_size", radius);
                        }}).toItemStack());
                    }
                }
                SiText.string("Вы удалили регион " + name, SiPalette.Interface.yellow).toMessageTask().addPlayer(player).send();
                new SiTicker(ticker -> {
                    player.closeInventory();
                    ticker.kill();
                });
            });
            elements.add(removeRegionElement);
        }
        elements.add(new SiMenuElement(name + "_members") {{
            setItemStack(Material.PLAYER_HEAD);
            setDisplayName(SiText.string("Члены региона"));
            ArrayList<SiTextLike> membersText = new ArrayList<>();
            for (String member : members) {
                membersText.add(SiText.string(member, isOwner(member) ? SiPalette.Interface.gold : SiPalette.Interface.white));
            }
            membersText.add(SiText.string("/rg " + SiRegion.this.name + " addmember <ник> - Добавить игрока", SiPalette.Interface.green));
            membersText.add(SiText.string("/rg " + SiRegion.this.name + " removemember <ник> - Удалить игрока", SiPalette.Interface.green));
            membersText.add(SiText.string("/rg " + SiRegion.this.name + " addowner <ник> - Добавить совладельца", SiPalette.Interface.green));
            membersText.add(SiText.string("/rg " + SiRegion.this.name + " removeowner <ник> - Удалить совладельца", SiPalette.Interface.green));
            setDescription(membersText);
        }});
        for (SiRegionFlag flag : SiRegionFlags.all.getAll()) {
            elements.add(new SiMenuElement(name + "_flag" + (owner ? "_for_owner_" : "_for_others_") + flag.getName()) {{
                setItemStack(flag.getIcon());
                setDisplayName(flag.getDisplayName());
                boolean value = getFlag(flag);
                SiText allowed = value ? SiText.string("[РАЗРЕШЕНО]", SiPalette.Interface.green) : SiText.string("[ЗАПРЕЩЕНО]", SiPalette.Interface.red);
                ArrayList<SiTextLike> description = new ArrayList<>();
                description.add(allowed);
                description.addAll(flag.getDescription());
                if (owner) {
                    description.add(SiText.string("Нажмите, чтобы переключить", SiPalette.Interface.green));
                }
                setDescription(description);
                if (owner) {
                    setClickHandler((player, itemStack1, event) -> {
                        flags.put(flag.getName(), !getFlag(flag));
                        save();
                        openMenu(player);
                    });
                }
                if (value) {
                    itemStack.setEnchantment(Enchantment.LURE, 1); // TODO: Replace to luck
                    itemStack.addFlags(ItemFlag.HIDE_ENCHANTS);
                }
            }});
        }
        return SiMenus.buildMenuCluster(name + "_region_menu", SiText.string("Регион \"" + name + "\""), elements);
    }

    /**
     * Открыть меню региона.
     *
     * @param player Игрок
     * @return Открытое меню.
     */
    public SiMenu openMenu(Player player) {
        return buildMenu(isOwner(player)).open(player);
    }
}
