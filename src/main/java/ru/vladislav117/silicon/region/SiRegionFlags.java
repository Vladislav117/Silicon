package ru.vladislav117.silicon.region;

import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Lectern;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTakeLecternBookEvent;
import org.bukkit.inventory.ItemStack;
import ru.vladislav117.silicon.color.SiPalette;
import ru.vladislav117.silicon.comparator.SiComparator;
import ru.vladislav117.silicon.content.SiContentList;
import ru.vladislav117.silicon.content.SiContentLoaders;
import ru.vladislav117.silicon.event.SiEvents;
import ru.vladislav117.silicon.text.SiText;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Класс-контейнер для флагов регионов.
 */
public final class SiRegionFlags {
    static final HashSet<Material> storages = new HashSet<>() {{
        add(Material.CHEST);
        add(Material.BARREL);
        add(Material.FURNACE);
        add(Material.SMOKER);
        add(Material.BLAST_FURNACE);
        add(Material.BREWING_STAND);
        add(Material.DECORATED_POT);
        add(Material.CHISELED_BOOKSHELF);
        // add(Material.LECTERN);
        add(Material.DISPENSER);
        add(Material.DROPPER);
        add(Material.HOPPER);

        add(Material.SHULKER_BOX);
        add(Material.WHITE_SHULKER_BOX);
        add(Material.LIGHT_GRAY_SHULKER_BOX);
        add(Material.GRAY_SHULKER_BOX);
        add(Material.BLACK_SHULKER_BOX);
        add(Material.BROWN_SHULKER_BOX);
        add(Material.RED_SHULKER_BOX);
        add(Material.ORANGE_SHULKER_BOX);
        add(Material.YELLOW_SHULKER_BOX);
        add(Material.LIME_SHULKER_BOX);
        add(Material.GREEN_SHULKER_BOX);
        add(Material.CYAN_SHULKER_BOX);
        add(Material.LIGHT_BLUE_SHULKER_BOX);
        add(Material.BLUE_SHULKER_BOX);
        add(Material.PURPLE_SHULKER_BOX);
        add(Material.MAGENTA_SHULKER_BOX);
        add(Material.PINK_SHULKER_BOX);
    }};

    static final HashSet<EntityType> entityStorages = new HashSet<>() {{
        add(EntityType.CHEST_MINECART);
        add(EntityType.HOPPER_MINECART);
        add(EntityType.CHEST_BOAT);
    }};

    static final HashSet<Material> doors = new HashSet<>() {{
        add(Material.OAK_DOOR);
        add(Material.SPRUCE_DOOR);
        add(Material.BIRCH_DOOR);
        add(Material.JUNGLE_DOOR);
        add(Material.ACACIA_DOOR);
        add(Material.DARK_OAK_DOOR);
        add(Material.MANGROVE_DOOR);
        add(Material.CHERRY_DOOR);
        add(Material.BAMBOO_DOOR);
        add(Material.CRIMSON_DOOR);
        add(Material.WARPED_DOOR);

        add(Material.IRON_DOOR);

        add(Material.OAK_TRAPDOOR);
        add(Material.SPRUCE_TRAPDOOR);
        add(Material.BIRCH_TRAPDOOR);
        add(Material.JUNGLE_TRAPDOOR);
        add(Material.ACACIA_TRAPDOOR);
        add(Material.DARK_OAK_TRAPDOOR);
        add(Material.MANGROVE_TRAPDOOR);
        add(Material.CHERRY_TRAPDOOR);
        add(Material.BAMBOO_TRAPDOOR);
        add(Material.CRIMSON_TRAPDOOR);
        add(Material.WARPED_TRAPDOOR);

        add(Material.IRON_TRAPDOOR);

        add(Material.OAK_FENCE_GATE);
        add(Material.SPRUCE_FENCE_GATE);
        add(Material.BIRCH_FENCE_GATE);
        add(Material.JUNGLE_FENCE_GATE);
        add(Material.ACACIA_FENCE_GATE);
        add(Material.DARK_OAK_FENCE_GATE);
        add(Material.MANGROVE_FENCE_GATE);
        add(Material.CHERRY_FENCE_GATE);
        add(Material.BAMBOO_FENCE_GATE);
        add(Material.CRIMSON_FENCE_GATE);
        add(Material.WARPED_FENCE_GATE);
    }};

    static final HashSet<Material> buttons = new HashSet<>() {{
        add(Material.LEVER);

        add(Material.OAK_BUTTON);
        add(Material.SPRUCE_BUTTON);
        add(Material.BIRCH_BUTTON);
        add(Material.JUNGLE_BUTTON);
        add(Material.ACACIA_BUTTON);
        add(Material.DARK_OAK_BUTTON);
        add(Material.MANGROVE_BUTTON);
        add(Material.CHERRY_BUTTON);
        add(Material.BAMBOO_BUTTON);
        add(Material.CRIMSON_BUTTON);
        add(Material.WARPED_BUTTON);
        add(Material.STONE_BUTTON);
        add(Material.POLISHED_BLACKSTONE_BUTTON);

        add(Material.OAK_PRESSURE_PLATE);
        add(Material.SPRUCE_PRESSURE_PLATE);
        add(Material.BIRCH_PRESSURE_PLATE);
        add(Material.JUNGLE_PRESSURE_PLATE);
        add(Material.ACACIA_PRESSURE_PLATE);
        add(Material.DARK_OAK_PRESSURE_PLATE);
        add(Material.MANGROVE_PRESSURE_PLATE);
        add(Material.CHERRY_PRESSURE_PLATE);
        add(Material.BAMBOO_PRESSURE_PLATE);
        add(Material.CRIMSON_PRESSURE_PLATE);
        add(Material.WARPED_PRESSURE_PLATE);
        add(Material.STONE_PRESSURE_PLATE);
        add(Material.POLISHED_BLACKSTONE_PRESSURE_PLATE);

        add(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
        add(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);

        add(Material.TRIPWIRE);
        add(Material.TRIPWIRE_HOOK);
    }};

    static final HashSet<Material> signs = new HashSet<>() {{
        add(Material.OAK_SIGN);
        add(Material.SPRUCE_SIGN);
        add(Material.BIRCH_SIGN);
        add(Material.JUNGLE_SIGN);
        add(Material.ACACIA_SIGN);
        add(Material.DARK_OAK_SIGN);
        add(Material.MANGROVE_SIGN);
        add(Material.CHERRY_SIGN);
        add(Material.BAMBOO_SIGN);
        add(Material.CRIMSON_SIGN);
        add(Material.WARPED_SIGN);

        add(Material.OAK_WALL_SIGN);
        add(Material.SPRUCE_WALL_SIGN);
        add(Material.BIRCH_WALL_SIGN);
        add(Material.JUNGLE_WALL_SIGN);
        add(Material.ACACIA_WALL_SIGN);
        add(Material.DARK_OAK_WALL_SIGN);
        add(Material.MANGROVE_WALL_SIGN);
        add(Material.CHERRY_WALL_SIGN);
        add(Material.BAMBOO_WALL_SIGN);
        add(Material.CRIMSON_WALL_SIGN);
        add(Material.WARPED_WALL_SIGN);

        add(Material.OAK_HANGING_SIGN);
        add(Material.SPRUCE_HANGING_SIGN);
        add(Material.BIRCH_HANGING_SIGN);
        add(Material.JUNGLE_HANGING_SIGN);
        add(Material.ACACIA_HANGING_SIGN);
        add(Material.DARK_OAK_HANGING_SIGN);
        add(Material.MANGROVE_HANGING_SIGN);
        add(Material.CHERRY_HANGING_SIGN);
        add(Material.BAMBOO_HANGING_SIGN);
        add(Material.CRIMSON_HANGING_SIGN);
        add(Material.WARPED_HANGING_SIGN);

        add(Material.OAK_WALL_HANGING_SIGN);
        add(Material.SPRUCE_WALL_HANGING_SIGN);
        add(Material.BIRCH_WALL_HANGING_SIGN);
        add(Material.JUNGLE_WALL_HANGING_SIGN);
        add(Material.ACACIA_WALL_HANGING_SIGN);
        add(Material.DARK_OAK_WALL_HANGING_SIGN);
        add(Material.MANGROVE_WALL_HANGING_SIGN);
        add(Material.CHERRY_WALL_HANGING_SIGN);
        add(Material.BAMBOO_WALL_HANGING_SIGN);
        add(Material.CRIMSON_WALL_HANGING_SIGN);
        add(Material.WARPED_WALL_HANGING_SIGN);
    }};

    static final HashSet<EntityType> blockEntities = new HashSet<>() {{
        add(EntityType.ITEM_FRAME);
        add(EntityType.GLOW_ITEM_FRAME);
        add(EntityType.PAINTING);
        add(EntityType.ARMOR_STAND);
    }};

    static final HashSet<Material> blockEntitiesMaterials = new HashSet<>() {{
        add(Material.ITEM_FRAME);
        add(Material.GLOW_ITEM_FRAME);
        add(Material.PAINTING);
        add(Material.ARMOR_STAND);
    }};

    static final HashSet<EntityType> boats = new HashSet<>() {{
        add(EntityType.BOAT);
        add(EntityType.CHEST_BOAT);
    }};

    static final HashSet<Material> anvils = new HashSet<>() {{
        add(Material.ANVIL);
        add(Material.CHIPPED_ANVIL);
        add(Material.DAMAGED_ANVIL);
    }};

    static final HashSet<Material> campfires = new HashSet<>() {{
        add(Material.CAMPFIRE);
        add(Material.SOUL_CAMPFIRE);
    }};

    static final HashSet<Material> cauldrons = new HashSet<>() {{
        add(Material.CAULDRON);
        add(Material.WATER_CAULDRON);
        add(Material.LAVA_CAULDRON);
        add(Material.POWDER_SNOW_CAULDRON);
    }};

    static final HashSet<Material> beehives = new HashSet<>() {{
        add(Material.BEE_NEST);
        add(Material.BEEHIVE);
    }};

    static final HashSet<Material> mechanisms = new HashSet<>() {{
        add(Material.REPEATER);
        add(Material.COMPARATOR);
        add(Material.DAYLIGHT_DETECTOR);
    }};

    static final HashSet<Material> saplings = new HashSet<>() {{
        add(Material.OAK_SAPLING);
        add(Material.SPRUCE_SAPLING);
        add(Material.BIRCH_SAPLING);
        add(Material.JUNGLE_SAPLING);
        add(Material.ACACIA_SAPLING);
        add(Material.DARK_OAK_SAPLING);
        add(Material.MANGROVE_PROPAGULE);
        add(Material.CHERRY_SAPLING);
        add(Material.AZALEA);
        add(Material.FLOWERING_AZALEA);
        add(Material.BROWN_MUSHROOM);
        add(Material.RED_MUSHROOM);
        add(Material.CRIMSON_FUNGUS);
        add(Material.WARPED_FUNGUS);
    }};

    static final HashSet<Material> buckets = new HashSet<>() {{
        add(Material.BUCKET);
        add(Material.WATER_BUCKET);
        add(Material.LAVA_BUCKET);
        add(Material.POWDER_SNOW_BUCKET);
        add(Material.COD_BUCKET);
        add(Material.SALMON_BUCKET);
        add(Material.TROPICAL_FISH_BUCKET);
        add(Material.PUFFERFISH_BUCKET);
        add(Material.AXOLOTL_BUCKET);
        add(Material.TADPOLE_BUCKET);
    }};

    public static final SiContentList<SiRegionFlag> all = new SiContentList<>();
    public static SiRegionFlag breakBlocks, placeBlocks,
            accessStorages, openDoors, pushButtons, explodeBlocks, explodeMobs, explodePlayers,
            editSigns, editFrames, rotateFrames, editArmorStands,
            damageMobs, damagePlayers, tradeWithVillagers,
            editMechanisms, growSaplings,
            editNoteBlocks, useJukeboxes, useEnchantingTable,
            useAnvils;
    public static final SiContentLoaders loaders = new SiContentLoaders();

    /**
     * Инициализация.
     */
    public static void init() {
        loaders.addPrimaryLoader(() -> {
            breakBlocks = new SiRegionFlag("break_blocks", false) {{
                setDisplayName(SiText.string("Разрушение блоков"));
                setIcon(Material.IRON_PICKAXE);
                setDescription("Разрешено ли игрокам ломать блоки");
            }};
            placeBlocks = new SiRegionFlag("place_blocks", false) {{
                setDisplayName(SiText.string("Установка блоков"));
                setIcon(Material.CRAFTING_TABLE);
                setDescription("Разрешено ли игрокам устанавливать блоки");
            }};
            accessStorages = new SiRegionFlag("access_storages", false) {{
                setDisplayName(SiText.string("Доступ к хранилищам"));
                setIcon(Material.CHEST);
                setDescription("Могут ли игроки иметь доступ к сундукам, бочкам, вазам, книжным полкам и т.п. Этот флаг так же действует на кафедры, котлы и маяки.");
            }};
            openDoors = new SiRegionFlag("open_doors", true) {{
                setDisplayName(SiText.string("Открытие дверей"));
                setIcon(Material.OAK_DOOR);
                setDescription("Разрешено ли игрокам открывать двери, калитки и люки");
            }};
            pushButtons = new SiRegionFlag("push_buttons", true) {{
                setDisplayName(SiText.string("Нажимания кнопок"));
                setIcon(Material.LEVER);
                setDescription("Разрешено ли игрокам нажимать на кнопки, рычаги, нажимные плиты и растяжки");
            }};
            explodeBlocks = new SiRegionFlag("explode_blocks", false) {{
                setDisplayName(SiText.string("Взрывы блоков"));
                setIcon(Material.TNT);
                setDescription("Могут ли взрывы разрушать блоки");
            }};
            explodeMobs = new SiRegionFlag("explode_mobs", false) {{
                setDisplayName(SiText.string("Взрывы мобов"));
                setIcon(Material.TNT);
                setDescription("Могут ли взрывы наносить урон мобам (кроме игрока)");
            }};
            explodePlayers = new SiRegionFlag("explode_players", false) {{
                setDisplayName(SiText.string("Взрывы игроков"));
                setIcon(Material.TNT);
                setDescription("Могут ли взрывы наносить урон игрокам");
            }};
            editSigns = new SiRegionFlag("edit_signs", false) {{
                setDisplayName(SiText.string("Редактирование табличек"));
                setIcon(Material.OAK_SIGN);
                setDescription("Разрешено ли игрокам редактировать таблички");
            }};
            editFrames = new SiRegionFlag("edit_frames", false) {{
                setDisplayName(SiText.string("Редактирование рамок"));
                setIcon(Material.ITEM_FRAME);
                setDescription("Разрешено ли игрокам убирать и вставлять предметы в рамки");
            }};
            rotateFrames = new SiRegionFlag("rotate_frames", false) {{
                setDisplayName(SiText.string("Вращение в рамках"));
                setIcon(Material.ITEM_FRAME);
                setDescription("Разрешено ли игрокам вращать предметы в рамках");
            }};
            editArmorStands = new SiRegionFlag("edit_armor_stands", false) {{
                setDisplayName(SiText.string("Редактирование стоек для брони"));
                setIcon(Material.ARMOR_STAND);
                setDescription("Разрешено ли игрокам забирать и давать предметы стойкам для брони");
            }};
            damageMobs = new SiRegionFlag("damage_mobs", true) {{
                setDisplayName(SiText.string("Урон мобам"));
                setIcon(Material.IRON_SWORD);
                setDescription("Разрешено ли игрокам наносить урон всем мобам, кроме игроков");
            }};
            damagePlayers = new SiRegionFlag("damage_players", true) {{
                setDisplayName(SiText.string("Урон игрокам"));
                setIcon(Material.NETHERITE_SWORD);
                setDescription("Разрешено ли игрокам наносить урон игрокам");
            }};
            tradeWithVillagers = new SiRegionFlag("trade_with_villagers", false) {{
                setDisplayName(SiText.string("Торговля с жителями"));
                setIcon(Material.EMERALD);
                setDescription("Разрешено ли игрокам торговать с жителями");
            }};
            editMechanisms = new SiRegionFlag("edit_mechanisms", false) {{
                setDisplayName(SiText.string("Редактирование механизмов"));
                setIcon(Material.REPEATER);
                setDescription("Разрешено ли игрокам изменять повторители, компараторы и датчики дневного света");
            }};
            growSaplings = new SiRegionFlag("grow_saplings", false) {{
                setDisplayName(SiText.string("Выращивание саженцев"));
                setIcon(Material.OAK_SAPLING);
                setDescription("Разрешено ли игрокам выращивать саженцы");
            }};
//
//            editFlowerPots = new SiRegionFlag("edit_flower_pots", false) {{
//                setDisplayName(SiText.string("Цветочные горшки"));
//                setIcon(Material.FLOWER_POT);
//                setDescription("Разрешено ли игрокам менять цветы в горшках");
//            }};
            editNoteBlocks = new SiRegionFlag("edit_note_blocks", false) {{
                setDisplayName(SiText.string("Нотные блоки"));
                setIcon(Material.NOTE_BLOCK);
                setDescription("Разрешено ли игрокам настраивать нотные блоки");
            }};
            useJukeboxes = new SiRegionFlag("use_jukeboxes", false) {{
                setDisplayName(SiText.string("Проигрыватели"));
                setIcon(Material.JUKEBOX);
                setDescription("Разрешено ли игрокам использовать проигрыватели");
            }};
            useEnchantingTable = new SiRegionFlag("use_enchanting_tables", false) {{
                setDisplayName(SiText.string("Столы зачарований"));
                setIcon(Material.ENCHANTING_TABLE);
                setDescription("Разрешено ли игрокам использовать столы зачарований");
            }};
            useAnvils = new SiRegionFlag("use_anvils", false) {{
                setDisplayName(SiText.string("Наковальни"));
                setIcon(Material.ANVIL);
                setDescription("Разрешено ли игрокам использовать наковальни");
            }};
        });

        SiEvents.registerBukkitEvents(new Listener() {
            @EventHandler(priority = EventPriority.LOWEST)
            public void onBlockBreakEvent(BlockBreakEvent event) {
                SiRegionSet regionSet = SiRegions.getRegionsAt(event.getBlock().getLocation());
                if (regionSet.isMember(event.getPlayer())) return;
                if (regionSet.getFlag(event.getPlayer(), breakBlocks)) return;
                event.setCancelled(true);
                SiText.string("Вы не можете ломать блоки в этом регионе", SiPalette.Interface.red).toMessageTask().addPlayer(event.getPlayer()).send();
            }

            @EventHandler(priority = EventPriority.LOWEST)
            public void onBlockPlaceEvent(BlockPlaceEvent event) {
                SiRegionSet regionSet = SiRegions.getRegionsAt(event.getBlock().getLocation());
                if (regionSet.isMember(event.getPlayer())) return;
                if (regionSet.getFlag(event.getPlayer(), placeBlocks)) return;
                event.setCancelled(true);
                SiText.string("Вы не можете ставить блоки в этом регионе", SiPalette.Interface.red).toMessageTask().addPlayer(event.getPlayer()).send();
            }

            @EventHandler(priority = EventPriority.LOWEST)
            public void onPlayerItemFrameChangeEvent(PlayerItemFrameChangeEvent event) {
                SiRegionSet regionSet = SiRegions.getRegionsAt(event.getItemFrame().getLocation());
                if (regionSet.isMember(event.getPlayer())) return;
                if (event.getAction().equals(PlayerItemFrameChangeEvent.ItemFrameChangeAction.ROTATE)) {
                    if (regionSet.getFlag(event.getPlayer(), rotateFrames)) return;
                    event.setCancelled(true);
                    SiText.string("Вы не можете вращать предметы в рамке в этом регионе", SiPalette.Interface.red).toMessageTask().addPlayer(event.getPlayer()).send();
                    return;
                }
                if (regionSet.getFlag(event.getPlayer(), editFrames)) return;
                event.setCancelled(true);
                SiText.string("Вы не можете менять предметы в рамке в этом регионе", SiPalette.Interface.red).toMessageTask().addPlayer(event.getPlayer()).send();
            }

            @EventHandler(priority = EventPriority.LOWEST)
            public void onPlayerArmorStandManipulateEvent(PlayerArmorStandManipulateEvent event) {
                SiRegionSet regionSet = SiRegions.getRegionsAt(event.getRightClicked().getLocation());
                if (regionSet.isMember(event.getPlayer())) return;
                if (regionSet.getFlag(event.getPlayer(), editArmorStands)) return;
                event.setCancelled(true);
                SiText.string("Вы не можете менять предметы в стойке для брони в этом регионе", SiPalette.Interface.red).toMessageTask().addPlayer(event.getPlayer()).send();
            }

            @EventHandler(priority = EventPriority.LOWEST)
            public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
                if (event.getRightClicked().getType().equals(EntityType.VILLAGER)) {
                    SiRegionSet regionSet = SiRegions.getRegionsAt(event.getRightClicked().getLocation());
                    if (regionSet.isMember(event.getPlayer())) return;
                    if (regionSet.getFlag(event.getPlayer(), tradeWithVillagers)) return;
                    event.setCancelled(true);
                    SiText.string("Вы не можете торговать с жителями в этом регионе", SiPalette.Interface.red).toMessageTask().addPlayer(event.getPlayer()).send();
                    return;
                }
                if (entityStorages.contains(event.getRightClicked().getType())) {
                    SiRegionSet regionSet = SiRegions.getRegionsAt(event.getRightClicked().getLocation());
                    if (regionSet.isMember(event.getPlayer())) return;
                    if (regionSet.getFlag(event.getPlayer(), tradeWithVillagers)) return;
                    event.setCancelled(true);
                    SiText.string("Вы не можете открывать хранилища в этом регионе", SiPalette.Interface.red).toMessageTask().addPlayer(event.getPlayer()).send();
                    return;
                }
            }

            @EventHandler(priority =  EventPriority.LOWEST)
            public void onPlayerTakeLecternBookEvent(PlayerTakeLecternBookEvent event){
                SiRegionSet regionSet = SiRegions.getRegionsAt(event.getLectern().getLocation());
                if (regionSet.isMember(event.getPlayer())) return;
                if (regionSet.getFlag(event.getPlayer(), accessStorages)) return;
                event.setCancelled(true);
                SiText.string("Вы не можете забирать книги с кафедры в этом регионе", SiPalette.Interface.red).toMessageTask().addPlayer(event.getPlayer()).send();
                return;
            }

            @EventHandler(priority = EventPriority.LOWEST)
            public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
                if (!(event.getDamager() instanceof Player player)) return;
                SiRegionSet regionSet = SiRegions.getRegionsAt(event.getEntity().getLocation());
                if (regionSet.isMember(player)) return;
                if (blockEntities.contains(event.getEntityType())) {
                    if (event.getEntity() instanceof ItemFrame itemFrame && !SiComparator.isAir(itemFrame.getItem())) {
                        if (regionSet.isMember(player)) return;
                        if (regionSet.getFlag(player, editFrames)) return;
                        event.setCancelled(true);
                        SiText.string("Вы не можете менять предметы в рамке в этом регионе", SiPalette.Interface.red).toMessageTask().addPlayer(player).send();
                        return;
                    }
                    if (regionSet.getFlag(player, breakBlocks)) return;
                    event.setCancelled(true);
                    SiText.string("Вы не можете ломать блоки в этом регионе", SiPalette.Interface.red).toMessageTask().addPlayer(player).send();
                    return;
                }
                if (event.getEntityType().equals(EntityType.PLAYER)) {
                    if (regionSet.getFlag(player, damagePlayers)) return;
                    event.setCancelled(true);
                    SiText.string("Вы не можете атаковать игроков в этом регионе", SiPalette.Interface.red).toMessageTask().addPlayer(player).send();
                    return;
                }
                if (event.getEntityType().equals(EntityType.BOAT)) {
                    return;
                }
                if (!regionSet.getFlag(player, damageMobs)) {
                    event.setCancelled(true);
                    SiText.string("Вы не можете атаковать мобов в этом регионе", SiPalette.Interface.red).toMessageTask().addPlayer(player).send();
                    return;
                }
            }

            @EventHandler(priority = EventPriority.LOWEST)
            public void onPlayerInteractEventForItems(PlayerInteractEvent event) {
                if (event.getInteractionPoint() == null) return;
                ItemStack itemStack = event.getItem();
                if (itemStack == null) return;
                if (event.getAction().isRightClick() && buckets.contains(itemStack.getType())){
                    SiRegionSet regionSet = SiRegions.getRegionsAt(event.getInteractionPoint());
                    if (regionSet.isMember(event.getPlayer())) return;
                    if (regionSet.getFlag(event.getPlayer(), placeBlocks)) return;
                    event.setCancelled(true);
                    SiText.string("Вы не можете использовать вёдра в этом регионе (установка блоков отключена)", SiPalette.Interface.red).toMessageTask().addPlayer(event.getPlayer()).send();
                    return;
                }
            }

            @EventHandler(priority = EventPriority.LOWEST)
            public void onPlayerInteractEvent(PlayerInteractEvent event) {
                Block block = event.getClickedBlock();
                if (block == null) return;
                if (event.getAction().isRightClick() && storages.contains(block.getType())) {
                    SiRegionSet regionSet = SiRegions.getRegionsAt(block.getLocation());
                    if (regionSet.isMember(event.getPlayer())) return;
                    if (regionSet.getFlag(event.getPlayer(), accessStorages)) return;
                    event.setCancelled(true);
                    SiText.string("Вы не можете открывать хранилища в этом регионе", SiPalette.Interface.red).toMessageTask().addPlayer(event.getPlayer()).send();
                    return;
                }
                if (event.getAction().isRightClick() && doors.contains(block.getType())) {
                    SiRegionSet regionSet = SiRegions.getRegionsAt(block.getLocation());
                    if (regionSet.isMember(event.getPlayer())) return;
                    if (regionSet.getFlag(event.getPlayer(), openDoors)) return;
                    event.setCancelled(true);
                    SiText.string("Вы не можете открывать двери, люки и калитки в этом регионе", SiPalette.Interface.red).toMessageTask().addPlayer(event.getPlayer()).send();
                    return;
                }
                if (event.getAction().isRightClick() && signs.contains(block.getType())) {
                    SiRegionSet regionSet = SiRegions.getRegionsAt(block.getLocation());
                    if (regionSet.isMember(event.getPlayer())) return;
                    if (regionSet.getFlag(event.getPlayer(), editSigns)) return;
                    event.setCancelled(true);
                    SiText.string("Вы не можете редактировать таблички в этом регионе", SiPalette.Interface.red).toMessageTask().addPlayer(event.getPlayer()).send();
                }
                if ((event.getAction().isRightClick() || event.getAction().equals(Action.PHYSICAL)) && buttons.contains(block.getType())) {
                    SiRegionSet regionSet = SiRegions.getRegionsAt(block.getLocation());
                    if (regionSet.isMember(event.getPlayer())) return;
                    if (regionSet.getFlag(event.getPlayer(), pushButtons)) return;
                    event.setCancelled(true);
                    SiText.string("Вы не можете нажимать на кнопки, рычаги, нажимные плиты и растяжки в этом регионе", SiPalette.Interface.red).toMessageTask().addPlayer(event.getPlayer()).send();
                    return;
                }
                if (event.getAction().isRightClick() && mechanisms.contains(block.getType())) {
                    SiRegionSet regionSet = SiRegions.getRegionsAt(block.getLocation());
                    if (regionSet.isMember(event.getPlayer())) return;
                    if (regionSet.getFlag(event.getPlayer(), editMechanisms)) return;
                    event.setCancelled(true);
                    SiText.string("Вы не можете изменять механизмы в этом регионе", SiPalette.Interface.red).toMessageTask().addPlayer(event.getPlayer()).send();
                    return;
                }
                if (event.getAction().isRightClick() && block.getType().equals(Material.LECTERN)) {
                    Lectern lectern = (Lectern) block.getState();
                    if (SiComparator.isAir(lectern.getInventory().getItem(0))) return;
                    SiRegionSet regionSet = SiRegions.getRegionsAt(block.getLocation());
                    if (regionSet.isMember(event.getPlayer())) return;
                    if (regionSet.getFlag(event.getPlayer(), accessStorages)) return;
                    event.setCancelled(true);
                    SiText.string("Вы не можете класть книги на кафедру в этом регионе", SiPalette.Interface.red).toMessageTask().addPlayer(event.getPlayer()).send();
                    return;
                }
                if (event.getAction().isRightClick() && cauldrons.contains(block.getType())){
                    SiRegionSet regionSet = SiRegions.getRegionsAt(block.getLocation());
                    if (regionSet.isMember(event.getPlayer())) return;
                    if (regionSet.getFlag(event.getPlayer(), accessStorages)) return;
                    event.setCancelled(true);
                    SiText.string("Вы не можете взаимодействовать с котлами в этом регионе", SiPalette.Interface.red).toMessageTask().addPlayer(event.getPlayer()).send();
                    return;
                }
                if (event.getAction().isRightClick() && block.getType().equals(Material.BEACON)){
                    SiRegionSet regionSet = SiRegions.getRegionsAt(block.getLocation());
                    if (regionSet.isMember(event.getPlayer())) return;
                    if (regionSet.getFlag(event.getPlayer(), accessStorages)) return;
                    event.setCancelled(true);
                    SiText.string("Вы не можете взаимодействовать с маяками в этом регионе", SiPalette.Interface.red).toMessageTask().addPlayer(event.getPlayer()).send();
                    return;
                }
                if (event.getAction().isRightClick() && saplings.contains(block.getType())){
                    SiRegionSet regionSet = SiRegions.getRegionsAt(block.getLocation());
                    if (regionSet.isMember(event.getPlayer())) return;
                    if (regionSet.getFlag(event.getPlayer(), growSaplings)) return;
                    event.setCancelled(true);
                    SiText.string("Вы не можете взаимодействовать с саженцами в этом регионе", SiPalette.Interface.red).toMessageTask().addPlayer(event.getPlayer()).send();
                    return;
                }
                if (event.getAction().isRightClick() && block.getType().equals(Material.RESPAWN_ANCHOR)) {
                    SiRegionSet regionSet = SiRegions.getRegionsAt(block.getLocation());
                    if (regionSet.isMember(event.getPlayer())) return;
                    if (regionSet.getFlag(event.getPlayer(), accessStorages)) return;
                    event.setCancelled(true);
                    SiText.string("Вы не можете изменять якоря возрождения в этом регионе", SiPalette.Interface.red).toMessageTask().addPlayer(event.getPlayer()).send();
                    return;
                }
                if (event.getAction().isRightClick() && block.getType().equals(Material.NOTE_BLOCK)) {
                    SiRegionSet regionSet = SiRegions.getRegionsAt(block.getLocation());
                    if (regionSet.isMember(event.getPlayer())) return;
                    if (regionSet.getFlag(event.getPlayer(), editNoteBlocks)) return;
                    event.setCancelled(true);
                    SiText.string("Вы не можете настраивать нотные блоки в этом регионе", SiPalette.Interface.red).toMessageTask().addPlayer(event.getPlayer()).send();
                    return;
                }
                if (event.getAction().isRightClick() && block.getType().equals(Material.JUKEBOX)) {
                    SiRegionSet regionSet = SiRegions.getRegionsAt(block.getLocation());
                    if (regionSet.isMember(event.getPlayer())) return;
                    if (regionSet.getFlag(event.getPlayer(), useJukeboxes)) return;
                    event.setCancelled(true);
                    SiText.string("Вы не можете использовать проигрыватели в этом регионе", SiPalette.Interface.red).toMessageTask().addPlayer(event.getPlayer()).send();
                    return;
                }
                if (event.getAction().isRightClick() && block.getType().equals(Material.ENCHANTING_TABLE)) {
                    SiRegionSet regionSet = SiRegions.getRegionsAt(block.getLocation());
                    if (regionSet.isMember(event.getPlayer())) return;
                    if (regionSet.getFlag(event.getPlayer(), useEnchantingTable)) return;
                    event.setCancelled(true);
                    SiText.string("Вы не можете использовать столы зачарований в этом регионе", SiPalette.Interface.red).toMessageTask().addPlayer(event.getPlayer()).send();
                    return;
                }
                if (event.getAction().isRightClick() && anvils.contains(block.getType())) {
                    SiRegionSet regionSet = SiRegions.getRegionsAt(block.getLocation());
                    if (regionSet.isMember(event.getPlayer())) return;
                    if (regionSet.getFlag(event.getPlayer(), useAnvils)) return;
                    event.setCancelled(true);
                    SiText.string("Вы не можете использовать наковальни в этом регионе", SiPalette.Interface.red).toMessageTask().addPlayer(event.getPlayer()).send();
                    return;
                }
                if (event.getItem() != null && event.getAction().isRightClick() && blockEntitiesMaterials.contains(event.getItem().getType())) {
                    SiRegionSet regionSet = SiRegions.getRegionsAt(block.getLocation());
                    if (regionSet.isMember(event.getPlayer())) return;
                    if (regionSet.getFlag(event.getPlayer(), placeBlocks)) return;
                    event.setCancelled(true);
                    SiText.string("Вы не можете ставить блоки в этом регионе", SiPalette.Interface.red).toMessageTask().addPlayer(event.getPlayer()).send();
                    return;
                }
            }

            /**
             * Найти блоки в регионах, где запрещены взрывы.
             *
             * @param blocks Блоки, которые затронул взрыв
             * @return Блоки, которые не должны взорваться.
             */
            ArrayList<Block> findBlocksToIgnore(Collection<Block> blocks) {
                ArrayList<Block> ignoredBlocks = new ArrayList<>();
                for (Block block : blocks) {
                    SiRegionSet regionSet = SiRegions.getRegionsAt(block.getLocation());
                    if (!regionSet.getFlag(explodeBlocks)) ignoredBlocks.add(block);
                }
                return ignoredBlocks;
            }

            @EventHandler(priority = EventPriority.LOWEST)
            public void onBlockExplodeEvent(BlockExplodeEvent event) {
                event.blockList().removeAll(findBlocksToIgnore(event.blockList()));
            }

            @EventHandler(priority = EventPriority.LOWEST)
            public void onEntityExplodeEvent(EntityExplodeEvent event) {
                event.blockList().removeAll(findBlocksToIgnore(event.blockList()));
            }

            @EventHandler(priority = EventPriority.LOWEST)
            public void onEntityDamageEvent(EntityDamageEvent event) {
                if (event.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) || event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
                    SiRegionSet regionSet = SiRegions.getRegionsAt(event.getEntity().getLocation());
                    if (event.getEntityType().equals(EntityType.PLAYER)) {
                        if (!regionSet.getFlag(explodePlayers)) event.setCancelled(true);
                    } else {
                        if (!regionSet.getFlag(explodeMobs)) event.setCancelled(true);
                    }
                }
            }
        });
    }
}
