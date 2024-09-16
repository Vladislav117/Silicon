package ru.vladislav117.silicon.liquid;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import ru.vladislav117.silicon.range.SiRange;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Класс-контейнер для работы с жидкостями.
 */
public class SiLiquids {
    public static final String celsiusDegrees = "°C";
    public static final int bucketVolume = 1000;
    public static final int bottleVolume = 333;
    public static final double absoluteZeroTemperature = -273.15;
    public static final double averageTemperature = 20;
    public static final SiRange averageSafeTemperature = new SiRange(-70, 70);
    public static final HashSet<EntityType> averageTemperatureSensitiveEntities = new HashSet<>() {{
        add(EntityType.PLAYER);

        add(EntityType.ALLAY);
        add(EntityType.AXOLOTL);
        add(EntityType.BAT);
        add(EntityType.BEE);
        add(EntityType.BLAZE);
        add(EntityType.CAMEL);
        add(EntityType.CAT);
        add(EntityType.CAVE_SPIDER);
        add(EntityType.CHICKEN);
        add(EntityType.COD);
        add(EntityType.COW);
        add(EntityType.CREEPER);
        add(EntityType.DOLPHIN);
        add(EntityType.DONKEY);
        add(EntityType.DROWNED);
        // add(EntityType.ELDER_GUARDIAN);
        add(EntityType.ENDERMAN);
        add(EntityType.ENDERMITE);
        add(EntityType.EVOKER);
        add(EntityType.FOX);
        add(EntityType.FROG);
        // add(EntityType.GHAST);
        add(EntityType.GLOW_SQUID);
        add(EntityType.GOAT);
        add(EntityType.GUARDIAN);
        add(EntityType.HOGLIN);
        add(EntityType.HORSE);
        add(EntityType.HUSK);
        add(EntityType.LLAMA);
        add(EntityType.MAGMA_CUBE);
        add(EntityType.MOOSHROOM);
        add(EntityType.MULE);
        add(EntityType.OCELOT);
        add(EntityType.PANDA);
        add(EntityType.PARROT);
        add(EntityType.PHANTOM);
        add(EntityType.PIG);
        add(EntityType.PIGLIN);
        add(EntityType.PIGLIN_BRUTE);
        add(EntityType.PILLAGER);
        add(EntityType.POLAR_BEAR);
        add(EntityType.PUFFERFISH);
        add(EntityType.RABBIT);
        add(EntityType.RAVAGER);
        add(EntityType.SALMON);
        add(EntityType.SHEEP);
        add(EntityType.SHULKER);
        add(EntityType.SILVERFISH);
        add(EntityType.SKELETON);
        add(EntityType.SKELETON_HORSE);
        add(EntityType.SLIME);
        add(EntityType.SNIFFER);
        add(EntityType.SNOW_GOLEM);
        add(EntityType.SPIDER);
        add(EntityType.SQUID);
        add(EntityType.STRAY);
        add(EntityType.STRIDER);
        add(EntityType.TADPOLE);
        add(EntityType.TRADER_LLAMA);
        add(EntityType.TROPICAL_FISH);
        add(EntityType.TURTLE);
        add(EntityType.VEX);
        add(EntityType.VILLAGER);
        add(EntityType.VINDICATOR);
        add(EntityType.WANDERING_TRADER);
        // add(EntityType.WARDEN);
        add(EntityType.WITCH);
        // add(EntityType.WITHER_SKELETON);
        add(EntityType.WOLF);
        add(EntityType.ZOGLIN);
        add(EntityType.ZOMBIE);
        add(EntityType.ZOMBIE_HORSE);
        add(EntityType.ZOMBIE_VILLAGER);
        add(EntityType.ZOMBIFIED_PIGLIN);
    }};

    public static HashSet<EntityType> highTemperatureIgnoreEntities = new HashSet<>() {{
        add(EntityType.BLAZE);
        add(EntityType.MAGMA_CUBE);
        add(EntityType.STRIDER);
    }};

    public static HashSet<EntityType> lowTemperatureIgnoreEntities = new HashSet<>() {{
        add(EntityType.SNOW_GOLEM);
        add(EntityType.STRIDER);
    }};

    /**
     * Проверка, может ли сущность получить урон от низкой температуры.
     *
     * @param entityType Сущность
     * @return Может ли сущность получить урон от низкой температуры.
     */
    public static boolean takesDamageFromLowTemperature(EntityType entityType) {
        return averageTemperatureSensitiveEntities.contains(entityType) && !lowTemperatureIgnoreEntities.contains(entityType);
    }

    /**
     * Проверка, может ли сущность получить урон от высокой температуры.
     *
     * @param entityType Сущность
     * @return Может ли сущность получить урон от высокой температуры.
     */
    public static boolean takesDamageFromHighTemperature(EntityType entityType) {
        return averageTemperatureSensitiveEntities.contains(entityType) && !highTemperatureIgnoreEntities.contains(entityType);
    }

    /**
     * Установить температуру в жидкостный стак. При установке учитываются преобразования жидкостей.
     *
     * @param liquidStack Стак
     * @param temperature Температура
     * @param items       Предметы на выходе предыдущей трансформации
     * @return Результат трансформации.
     */
    public static SiLiquidTransformation.TransformationResult setTemperature(SiLiquidStack liquidStack, double temperature, ArrayList<ItemStack> items) {
        liquidStack.setTemperature(Math.max(temperature, SiLiquids.absoluteZeroTemperature));
        if (liquidStack.getTemperature() < liquidStack.getLiquidType().getLowTemperatureTransformation().getThresholdTemperature()) {
            SiLiquidType newLiquidType = liquidStack.getLiquidType().getLowTemperatureTransformation().getNewLiquidType();
            if (newLiquidType == null) {
                return new SiLiquidTransformation.TransformationResult(null, new ArrayList<>(liquidStack.getLiquidType().getLowTemperatureTransformation().getItems(liquidStack.getVolume())) {{
                    addAll(items);
                }});
            }
            liquidStack.setLiquidType(newLiquidType);
            return setTemperature(liquidStack, temperature, items);
        }
        if (liquidStack.getTemperature() > liquidStack.getLiquidType().getHighTemperatureTransformation().getThresholdTemperature()) {
            SiLiquidType newLiquidType = liquidStack.getLiquidType().getHighTemperatureTransformation().getNewLiquidType();
            if (newLiquidType == null) {
                return new SiLiquidTransformation.TransformationResult(null, new ArrayList<>(liquidStack.getLiquidType().getHighTemperatureTransformation().getItems(liquidStack.getVolume())) {{
                    addAll(items);
                }});
            }
            liquidStack.setLiquidType(newLiquidType);
            return setTemperature(liquidStack, temperature, items);
        }
        return new SiLiquidTransformation.TransformationResult(liquidStack, new ArrayList<>(items));
    }

    /**
     * Установить температуру в жидкостный стак. При установке учитываются преобразования жидкостей.
     *
     * @param liquidStack Стак
     * @param temperature Температура
     * @return Результат трансформации.
     */
    public static SiLiquidTransformation.TransformationResult setTemperature(SiLiquidStack liquidStack, double temperature) {
        return setTemperature(liquidStack, temperature, new ArrayList<>());
    }
}
