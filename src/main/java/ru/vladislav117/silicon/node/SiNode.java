package ru.vladislav117.silicon.node;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
import ru.vladislav117.silicon.function.SiHandlerFunction;
import ru.vladislav117.silicon.function.SiDoubleHandlerFunction;
import ru.vladislav117.silicon.function.SiFilterFunction;

import org.jetbrains.annotations.Nullable;
import ru.vladislav117.silicon.file.SiFile;
import ru.vladislav117.silicon.json.SiJson;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Узел. Может быть как обычным значением (null, логическое, целочисленное, дробное, символ, строка),
 * так и структурой (список и таблица).
 */
@SuppressWarnings({"unused", "UnusedReturnValue", "SameParameterValue"})
public class SiNode {

    protected Object value;
    //    protected Double value = null;
//    protected String value = null;
//    protected ArrayList<SiNode> value = null;
    protected SiType type = SiType.NULL;

    public SiNode() {
        value = SiType.NULL;
        type = SiType.NULL;
    }

    /**
     * Создание нового узла. Принимает следующие типы данных: null, boolean, short, int,
     * long, float, double, char, String, List<?>, Map<?, ?>.
     *
     * @param object Значение узла
     */

    public SiNode(boolean object) {
        type = SiType.BOOLEAN;
        value = object;
    }

    public SiNode(long object) {//long,integer, short
        type = SiType.INTEGER;
        value = (long) object;
    }


    public SiNode(double object) {
        type = SiType.FLOAT;
        value = (double) object;
    }

    public SiNode(char object) {
        type = SiType.STRING;//SiType.CHARACTER;
        value = object;
    }

    public SiNode(String object) {
        type = SiType.STRING;
        value = object;
    }

    public SiNode(List<?> object) {
        type = SiType.LIST;
        value = object;
    }

    public SiNode(Map<?, SiNode> object) {
        type = SiType.MAP;
        value = object
            .entrySet()
            .stream()
            .map(it -> new AbstractMap.SimpleEntry<>(it.getKey(), new SiNode(it.getValue())))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public SiNode(SiNode object) {
        type = object.type;
        value = object;
    }

    /**
     * Создание узла с пустой таблицей.
     *
     * @return Узел с пустой таблицей.
     */
    public static SiNode emptyMap() {
        return new SiNode(new HashMap<>());
    }

    /**
     * Создание узла с пустым списком.
     *
     * @return Узел с пустым списком.
     */
    public static SiNode emptyList() {
        return new SiNode(new ArrayList<>());
    }

    /**
     * Преобразование JsonElement в узел.
     *
     * @param jsonElement Элемент Json
     * @return Преобразованный узел.
     */
    public static SiNode parseJson(JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            JsonPrimitive primitive = jsonElement.getAsJsonPrimitive();
            if (primitive.isBoolean()) return new SiNode(primitive.getAsBoolean());
            if (primitive.isNumber()) return new SiNode(primitive.getAsNumber().doubleValue());
            if (primitive.isString()) return new SiNode(primitive.getAsString());
        } else if (jsonElement.isJsonArray()) {
            JsonArray array = jsonElement.getAsJsonArray();
            ArrayList<SiNode> list = new ArrayList<>();
            for (JsonElement arrayElement : array) {
                list.add(parseJson(arrayElement));
            }
            return new SiNode(list);
        } else if (jsonElement.isJsonObject()) {
            JsonObject object = jsonElement.getAsJsonObject();
            HashMap<String, SiNode> map = new HashMap<>();
            for (String key : object.keySet()) {
                map.put(key, parseJson(object.get(key)));
            }
            return new SiNode(map);
        }
        return new SiNode();
    }

    /**
     * Преобразование Json-строки в узел.
     *
     * @param jsonString Json-строка
     * @return Преобразованный узел.
     */
    public static SiNode parseJsonString(String jsonString) {
        return parseJson(SiJson.parse(jsonString));
    }

    /**
     * Преобразование Json-файла в узел.
     *
     * @param jsonFile Json-файла
     * @return Преобразованный узел.
     */
    public static SiNode readJson(SiFile jsonFile) {
        return parseJson(jsonFile.readJson());
    }

    /**
     * Преобразование узла в Json-элемент.
     *
     * @return Json-элемент из узла.
     */
    public JsonElement toJson() {
        return switch (type) {

            case NULL -> JsonNull.INSTANCE;
            case BOOLEAN -> new JsonPrimitive(asBoolean());
            case INTEGER, FLOAT -> new JsonPrimitive(asNumber());
            case STRING -> new JsonPrimitive(asString());
            case LIST -> {
                JsonArray array = new JsonArray();
                for (SiNode element : asList()) array.add(element.toJson());
                yield array;
            }
            case MAP -> {
                JsonObject object = new JsonObject();
                for (Map.Entry<String, SiNode> entry : asMap().entrySet()) {
                    object.add(entry.getKey(), entry.getValue().toJson());
                }
                yield object;
            }
        };
    }

    /**
     * Преобразование узла в Json-строку.
     *
     * @return Json-строка из узла.
     */
    public String toJsonString() {
        return toJson().toString();
    }

    /**
     * Запись узла в Json-файл.
     *
     * @param file Файл, в который будет записан узел
     * @param file Файл, в который будет записан узел
     * @return Этот же узел.
     */
    public SiNode writeJson(SiFile file) {
        file.writeNode(this);
        return this;
    }

    /**
     * Получение длины строки, размера списка или размера таблицы.
     *
     * @return Длина строки, размер списка, размер таблицы или -1, если у узла не может быть размера.
     */
    public int getSize() {
        return switch (type) {

            case STRING -> asString().length();
            case LIST -> asList().size();
            case MAP -> asMap().size();
            case NULL, BOOLEAN, INTEGER, FLOAT -> -1;
        };
    }

    /**
     * Проверка, имеет ли узел индекс списка.
     *
     * @param index Индекс, который будет проверяться
     * @return true, если индекс есть. Если узел не является списком или индекс за границами списка, то false.
     */
    public boolean hasIndex(int index) {
        return index < asList().size() && 0 <= index;
    }

    /**
     * Проверка, имеет ли узел дочерний узел.
     *
     * @param child Имя дочернего узла
     * @return true, если дочерний узел есть. Если узел не является таблицей или у него нет дочернего узла, то false.
     */
    public boolean hasChild(String child) {
        return asMap().containsKey(child);
    }


    /**
     * Получение элемента списка.
     *
     * @param index Индекс элемента
     * @return Узел списка или null, если узел не является списком или индекс за границами списка.
     */
    @NotNull
    public SiNode getOrThrow(int index) {
        if (!hasIndex(index)) throw new NoSuckArrayChildException(index, this);
        return asList().get(index);
    }

    /**
     * Получение элемента списка.
     *
     * @param index Индекс элемента
     * @return Узел списка или null, если узел не является списком или индекс за границами списка.
     */
    @Nullable
    public SiNode getOrNull(int index) {
        if (!hasIndex(index)) return null;
        return asList().get(index);
    }

    /**
     * Получение дочернего узла.
     *
     * @param child Имя дочернего узла
     * @return Дочерний узел или null, если узел не является таблицей или у него нет дочернего узла.
     */
    @Nullable
    public SiNode getOrNull(String child) {
        if (!hasChild(child)) return null;
        return asMap().get(child);
    }

    /**
     * Получение дочернего узла.
     *
     * @param child Имя дочернего узла
     * @return Дочерний узел или null, если узел не является таблицей или у него нет дочернего узла.
     */
    @NotNull
    public SiNode getOrThrown(String child) {
        expect(true, SiType.MAP);
        if (!hasChild(child)) throw new NoSuckObjectChildException(child);
        return asMap().get(child);
    }

    /**
     * Добавление элемента в список. Если узел не является списком, то будет exception.
     *
     * @param node Элемент, который будет добавлен
     * @return Этот же узел.
     */
    public SiNode add(SiNode node) {
        expect(true, SiType.LIST);
        asList().add(node);
        return this;
    }

    /**
     * Замена элемента списка в конкретном индексе на новый. Если узел не является списком, то будет exception.
     * Если индекс меньше 0, то будет exception.
     * Если индекс больше или равен размеру списка, то элемент будет добавлен в конец списка.
     *
     * @param index Индекс, в который будет установлен элемент
     * @param node  Элемент, который будет установлен
     * @return Этот же узел.
     */
    public SiNode set(int index, SiNode node) {
        expect(true, SiType.LIST);
        List<SiNode> list = asList();
        if (0 <= index && index < list.size()) {
            list.set(index, node);
        } else {
            list.add(node);
        }
        return this;
    }


    /**
     * Установка дочернего узла с заданным именем. Если узел не является таблицей, то будет exception.
     *
     * @param child Имя дочернего узла
     * @param node  Дочерний узел
     * @return Этот же узел.
     */
    public SiNode set(String child, SiNode node) {
        expect(true, SiType.MAP);
        asMap().put(child, node);
        return this;
    }

    /**
     * Удаление элемента списка из конкретного индекса.
     * Если узел не является списком, то будет exception.
     * Если индекс за границей списка, то будет exception.
     *
     * @param index Индекс, элемент по которому будет удалён
     * @return Этот же узел.
     */
    public SiNode remove(int index) {
        if (!hasIndex(index)) return this;
        asList().remove(index);
        return this;
    }

    /**
     * Удаление элементов списка по фильтру.
     * Если узел не является списком, то будет exception.
     *
     * @param filter Фильтр элементов
     * @return Этот же узел.
     */
    public SiNode removeIf(SiFilterFunction<? super SiNode> filter) {
        //TODO ОООООЧЕНЬ странной решение есть же стандартный класс java.util.function.Predicate
        if (!expect(true, SiType.LIST)) return this;
        asList().removeIf(filter::isSuitable);
        return this;
    }

    /**
     * Удаление дочернего узла с заданным именем. Если узел не является таблицей, то будет exception.
     * Ели дочернего узла с таким именем нет, то будет exception.
     *
     * @param child Имя дочернего узла
     * @return Этот же узел.
     */
    public SiNode remove(String child) {
        if (!expect(true, SiType.MAP)) return this;
        if (!hasChild(child)) return this;
        asMap().remove(child);
        return this;
    }

    /**
     * Получение множества имён дочерних узлов.
     *
     * @return Множество дочерних узлов или пустое множество, если узел не является таблицей.
     */
    public Set<String> getKeySet() {
        if (!expect(true, SiType.MAP)) return Collections.emptySet();
        return asMap().keySet();
    }

    /**
     * Обработка каждого дочернего узла в списке.
     *
     * @param handler Обработчик узлов
     * @return Этот же узел.
     */
    public SiNode forEach(SiHandlerFunction<SiNode> handler) {
        //есть стандартный Consumer
        if (expect(true, SiType.LIST)) asList().forEach(handler::handle);
        return this;
    }

    /**
     * Обработка каждого дочернего узла в таблице.
     *
     * @param handler Обработчик узлов
     * @return Этот же узел.
     */
    public SiNode forEach(SiDoubleHandlerFunction<String, SiNode> handler) {
        if (expect(true, SiType.MAP)) asMap().forEach(handler::handle);
        return this;
    }

    @Override
    public String toString() {
        return Objects.toString(value);
    }

    //region boolean

    /**
     * Проверяет, является ли этот узел Boolean.
     *
     * @return true, если узел является Boolean, иначе false.
     */
    public boolean isBoolean() {
        return expect(true, SiType.BOOLEAN);
    }

    /**
     * Установка boolean значения в узел.
     *
     * @param value boolean значение
     * @return Этот же узел.
     */
    public SiNode set(boolean value) {
        type = SiType.BOOLEAN;
        this.value = value ? 1.0 : 0;
        return this;
    }

    /**
     * Проверяет, является ли узел по индексу Boolean.
     *
     * @param index Индекс узла списка
     * @return true, если узел по индексу является Boolean.false если не является, или если индекс за границей списка, или если узел не является списком.
     */
    public boolean isBoolean(int index) {
        return hasIndex(index) && getOrThrow(index).isBoolean();
    }

    /**
     * Проверяет, является ли дочерний узел Boolean.
     *
     * @param child Имя дочернего узла
     * @return true, если дочерний узел является Boolean.false если не является, или если дочернего узла с таким именем нет, или если узел не является таблицей.
     */
    public boolean isBoolean(String child) {
        return hasChild(child) && getOrThrown(child).isBoolean();
    }

    /**
     * Преобразует узел к boolean.
     * <p>
     * ВАЖНО! Если узел другого типа, то может возникнуть ошибка!
     *
     * @return boolean значение узла.
     */
    public boolean asBoolean() {
        if (expect(true, SiType.BOOLEAN)) return (boolean) value;
        return false;
    }

    /**
     * Получение Boolean значения узла из списка.
     *
     * @param index Индекс узла в списке
     * @return Boolean значение или null, если узел другого типа, или если индекс за границей списка, или если узел не является списком.
     */
    @Nullable
    public Boolean getBoolean(int index) {
        SiNode node = getOrNull(index);
        if (node == null) return null;
        if (!node.isBoolean()) return null;
        return node.asBoolean();
    }

    /**
     * Получение Boolean значения узла из списка.
     *
     * @param index        Индекс узла в списке
     * @param defaultValue Значение по умолчанию
     * @return Boolean значение или значение по умолчанию, если узел другого типа, или если индекс за границей списка, или если узел не является списком.
     */
    public boolean getBoolean(int index, boolean defaultValue) {
        SiNode node = getOrNull(index);
        if (node == null) return defaultValue;
        if (!node.isBoolean()) return defaultValue;
        return node.asBoolean();
    }

    /**
     * Получение Boolean значения дочернего узла.
     *
     * @param child Имя дочернего узла
     * @return Boolean значение или null, если дочерний узел другого типа, или если дочернего узла с таким именем нет, или если узел не является таблицей.
     */
    @Nullable
    public Boolean getBoolean(String child) {
        SiNode node = getOrNull(child);
        if (node == null) return null;
        if (!node.isBoolean()) return null;
        return node.asBoolean();
    }

    /**
     * Получение Boolean значения дочернего узла.
     *
     * @param child        Имя дочернего узла
     * @param defaultValue Значение по умолчанию
     * @return Boolean значение или значение по умолчанию, если дочерний узел другого типа, или если дочернего узла с таким именем нет, или если узел не является таблицей.
     */
    public boolean getBoolean(String child, boolean defaultValue) {
        SiNode node = getOrNull(child);
        if (node == null) return defaultValue;
        if (!node.isBoolean()) return defaultValue;
        return node.asBoolean();
    }

    /**
     * Замена элемента списка в конкретном индексе на новый с boolean значением. Если узел не является списком, то будет exception.
     * Если индекс меньше 0, то будет exception.
     * Если индекс больше или равен размеру списка, то элемент будет добавлен в конец списка.
     *
     * @param index Индекс, в который будет установлен элемент
     * @param value Элемент, который будет установлен
     * @return Этот же узел.
     */
    public SiNode set(int index, boolean value) {
        set(index, new SiNode(value));
        return this;
    }


    /**
     * Добавление boolean элемента в список. Если узел не является списком, то будет exception.
     *
     * @param value Элемент, который будет добавлен
     * @return Этот же узел.
     */
    public SiNode add(boolean value) {
        add(new SiNode(value));
        return this;
    }

    /**
     * Установка дочернего узла с заданным именем. Если узел не является таблицей, то будет exception.
     *
     * @param child Имя дочернего узла
     * @param value boolean значение узла
     * @return Этот же узел.
     */
    public SiNode set(String child, boolean value) {
        set(child, new SiNode(value));
        return this;
    }
    //endregion
    //region short

    /**
     * Проверяет, является ли этот узел Short.
     *
     * @return true, если узел является Short, иначе false.
     */
    public boolean isShort() {
        if (!expect(true, SiType.INTEGER)) return false;
        long number = asNumber().longValue();
        return Short.MIN_VALUE <= number && number <= Short.MAX_VALUE;
    }

    /**
     * Установка short значения в узел.
     *
     * @param value short значение
     * @return Этот же узел.
     */
    public SiNode set(short value) {
        type = SiType.INTEGER;
        this.value = (double) value;
        return this;
    }

    /**
     * Проверяет, является ли узел по индексу Short.
     *
     * @param index Индекс узла списка
     * @return true, если узел по индексу является Short.false если не является, или если индекс за границей списка, или если узел не является списком.
     */
    public boolean isShort(int index) {
        return hasIndex(index) && getOrThrow(index).isShort();
    }

    /**
     * Проверяет, является ли дочерний узел Short.
     *
     * @param child Имя дочернего узла
     * @return true, если дочерний узел является Short.false если не является, или если дочернего узла с таким именем нет, или если узел не является таблицей.
     */
    public boolean isShort(String child) {
        return hasChild(child) && getOrThrown(child).isShort();
    }

    /**
     * Преобразует узел к short.
     * <p>
     * ВАЖНО! Если узел другого типа, то может возникнуть ошибка!
     *
     * @return short значение узла.
     */
    public short asShort() {
        return asNumber().shortValue();
    }

    /**
     * Получение Short значения узла из списка.
     *
     * @param index Индекс узла в списке
     * @return Short значение или null, если узел другого типа, или если индекс за границей списка, или если узел не является списком.
     */
    @Nullable
    public Short getShort(int index) {
        SiNode node = getOrNull(index);
        if (node == null) return null;
        if (!node.isShort()) return null;
        return node.asShort();
    }

    /**
     * Получение Short значения узла из списка.
     *
     * @param index        Индекс узла в списке
     * @param defaultValue Значение по умолчанию
     * @return Short значение или значение по умолчанию, если узел другого типа, или если индекс за границей списка, или если узел не является списком.
     */
    public short getShort(int index, short defaultValue) {
        SiNode node = getOrNull(index);
        if (node == null) return defaultValue;
        if (!node.isShort()) return defaultValue;
        return node.asShort();
    }

    /**
     * Получение Short значения дочернего узла.
     *
     * @param child Имя дочернего узла
     * @return Short значение или null, если дочерний узел другого типа, или если дочернего узла с таким именем нет, или если узел не является таблицей.
     */
    @Nullable
    public Short getShort(String child) {
        SiNode node = getOrNull(child);
        if (node == null) return null;
        if (!node.isShort()) return null;
        return node.asShort();
    }

    /**
     * Получение Short значения дочернего узла.
     *
     * @param child        Имя дочернего узла
     * @param defaultValue Значение по умолчанию
     * @return Short значение или значение по умолчанию, если дочерний узел другого типа, или если дочернего узла с таким именем нет, или если узел не является таблицей.
     */
    public short getShort(String child, short defaultValue) {
        SiNode node = getOrNull(child);
        if (node == null) return defaultValue;
        if (!node.isShort()) return defaultValue;
        return node.asShort();
    }

    /**
     * Замена элемента списка в конкретном индексе на новый с short значением. Если узел не является списком, то будет exception.
     * Если индекс меньше 0, то будет exception.
     * Если индекс больше или равен размеру списка, то элемент будет добавлен в конец списка.
     *
     * @param index Индекс, в который будет установлен элемент
     * @param value Элемент, который будет установлен
     * @return Этот же узел.
     */
    public SiNode set(int index, short value) {
        set(index, new SiNode(value));
        return this;
    }


    /**
     * Добавление short элемента в список. Если узел не является списком, то будет exception.
     *
     * @param value Элемент, который будет добавлен
     * @return Этот же узел.
     */
    public SiNode add(short value) {
        add(new SiNode(value));
        return this;
    }

    /**
     * Установка дочернего узла с заданным именем. Если узел не является таблицей, то будет exception.
     *
     * @param child Имя дочернего узла
     * @param value short значение узла
     * @return Этот же узел.
     */
    public SiNode set(String child, short value) {
        set(child, new SiNode(value));
        return this;
    }
    //endregion
//region integer

    /**
     * Проверяет, является ли этот узел Integer.
     *
     * @return true, если узел является Integer, иначе false.
     */
    public boolean isInteger() {
        if (!expect(true, SiType.INTEGER)) return false;
        long value = asNumber().longValue();
        return Integer.MIN_VALUE <= value && value <= Integer.MAX_VALUE;
    }

    /**
     * Установка int значения в узел.
     *
     * @param value int значение
     * @return Этот же узел.
     */
    public SiNode set(int value) {
        type = SiType.INTEGER;
        this.value = (double) value;
        return this;
    }

    /**
     * Проверяет, является ли узел по индексу Integer.
     *
     * @param index Индекс узла списка
     * @return true, если узел по индексу является Integer.false если не является, или если индекс за границей списка, или если узел не является списком.
     */
    public boolean isInteger(int index) {
        return hasIndex(index) && getOrThrow(index).isInteger();
    }

    /**
     * Проверяет, является ли дочерний узел Integer.
     *
     * @param child Имя дочернего узла
     * @return true, если дочерний узел является Integer.false если не является, или если дочернего узла с таким именем нет, или если узел не является таблицей.
     */
    public boolean isInteger(String child) {
        return hasChild(child) && getOrThrown(child).isInteger();
    }

    /**
     * Преобразует узел к int.
     * <p>
     * ВАЖНО! Если узел другого типа, то может возникнуть ошибка!
     *
     * @return int значение узла.
     */
    public int asInteger() {
        if (!expect(true, SiType.INTEGER)) return 0;
        return asNumber().intValue();
    }

    /**
     * Получение Integer значения узла из списка.
     *
     * @param index Индекс узла в списке
     * @return Integer значение или null, если узел другого типа, или если индекс за границей списка, или если узел не является списком.
     */
    @Nullable
    public Integer getInteger(int index) {
        SiNode node = getOrNull(index);
        if (node == null) return null;
        if (!node.isInteger()) return null;
        return node.asInteger();
    }

    /**
     * Получение Integer значения узла из списка.
     *
     * @param index        Индекс узла в списке
     * @param defaultValue Значение по умолчанию
     * @return Integer значение или значение по умолчанию, если узел другого типа, или если индекс за границей списка, или если узел не является списком.
     */
    public int getInteger(int index, int defaultValue) {
        SiNode node = getOrNull(index);
        if (node == null) return defaultValue;
        if (!node.isInteger()) return defaultValue;
        return node.asInteger();
    }

    /**
     * Получение Integer значения дочернего узла.
     *
     * @param child Имя дочернего узла
     * @return Integer значение или null, если дочерний узел другого типа, или если дочернего узла с таким именем нет, или если узел не является таблицей.
     */
    @Nullable
    public Integer getInteger(String child) {
        SiNode node = getOrNull(child);
        if (node == null) return null;
        if (!node.isInteger()) return null;
        return node.asInteger();
    }

    /**
     * Получение Integer значения дочернего узла.
     *
     * @param child        Имя дочернего узла
     * @param defaultValue Значение по умолчанию
     * @return Integer значение или значение по умолчанию, если дочерний узел другого типа, или если дочернего узла с таким именем нет, или если узел не является таблицей.
     */
    public int getInteger(String child, int defaultValue) {
        SiNode node = getOrNull(child);
        if (node == null) return defaultValue;
        if (!node.isInteger()) return defaultValue;
        return node.asInteger();
    }

    /**
     * Замена элемента списка в конкретном индексе на новый с int значением. Если узел не является списком, то будет exception.
     * Если индекс меньше 0, то будет exception.
     * Если индекс больше или равен размеру списка, то элемент будет добавлен в конец списка.
     *
     * @param index Индекс, в который будет установлен элемент
     * @param value Элемент, который будет установлен
     * @return Этот же узел.
     */
    public SiNode set(int index, int value) {
        set(index, new SiNode(value));
        return this;
    }

    /**
     * Добавление int элемента в список. Если узел не является списком, то будет exception.
     *
     * @param value Элемент, который будет добавлен
     * @return Этот же узел.
     */
    public SiNode addInteger(int value) {
        add(new SiNode(value));
        return this;
    }

    /**
     * Установка дочернего узла с заданным именем. Если узел не является таблицей, то будет exception.
     *
     * @param child Имя дочернего узла
     * @param value int значение узла
     * @return Этот же узел.
     */
    public SiNode set(String child, int value) {
        set(child, new SiNode(value));
        return this;
    }
//endregion


//region long

    /**
     * Проверяет, является ли этот узел Long.
     *
     * @return true, если узел является Long, иначе false.
     */
    public boolean isLong() {
        if (!expect(true, SiType.INTEGER)) return false;
        double value = asNumber().doubleValue();
        return Long.MIN_VALUE <= value && value <= Long.MAX_VALUE;
    }

    /**
     * Установка long значения в узел.
     *
     * @param value long значение
     * @return Этот же узел.
     */
    public SiNode set(long value) {
        type = SiType.INTEGER;
        this.value = (double) value;
        return this;
    }

    /**
     * Проверяет, является ли узел по индексу Long.
     *
     * @param index Индекс узла списка
     * @return true, если узел по индексу является Long.false если не является, или если индекс за границей списка, или если узел не является списком.
     */
    public boolean isLong(int index) {
        return hasIndex(index) && getOrThrow(index).isLong();
    }

    /**
     * Проверяет, является ли дочерний узел Long.
     *
     * @param child Имя дочернего узла
     * @return true, если дочерний узел является Long.false если не является, или если дочернего узла с таким именем нет, или если узел не является таблицей.
     */
    public boolean isLong(String child) {
        return hasChild(child) && getOrThrown(child).isLong();
    }

    /**
     * Преобразует узел к long.
     * <p>
     * ВАЖНО! Если узел другого типа, то может возникнуть ошибка!
     *
     * @return long значение узла.
     */
    public long asLong() {
        if (!expect(true, SiType.INTEGER)) return 0;
        return asNumber().longValue();
    }

    /**
     * Получение Long значения узла из списка.
     *
     * @param index Индекс узла в списке
     * @return Long значение или null, если узел другого типа, или если индекс за границей списка, или если узел не является списком.
     */
    @Nullable
    public Long getLong(int index) {
        SiNode node = getOrNull(index);
        if (node == null) return null;
        if (!node.isLong()) return null;
        return node.asLong();
    }

    /**
     * Получение Long значения узла из списка.
     *
     * @param index        Индекс узла в списке
     * @param defaultValue Значение по умолчанию
     * @return Long значение или значение по умолчанию, если узел другого типа, или если индекс за границей списка, или если узел не является списком.
     */
    public long getLong(int index, long defaultValue) {
        SiNode node = getOrNull(index);
        if (node == null) return defaultValue;
        if (!node.isLong()) return defaultValue;
        return node.asLong();
    }

    /**
     * Получение Long значения дочернего узла.
     *
     * @param child Имя дочернего узла
     * @return Long значение или null, если дочерний узел другого типа, или если дочернего узла с таким именем нет, или если узел не является таблицей.
     */
    @Nullable
    public Long getLong(String child) {
        SiNode node = getOrNull(child);
        if (node == null) return null;
        if (!node.isLong()) return null;
        return node.asLong();
    }

    /**
     * Получение Long значения дочернего узла.
     *
     * @param child        Имя дочернего узла
     * @param defaultValue Значение по умолчанию
     * @return Long значение или значение по умолчанию, если дочерний узел другого типа, или если дочернего узла с таким именем нет, или если узел не является таблицей.
     */
    public long getLong(String child, long defaultValue) {
        SiNode node = getOrNull(child);
        if (node == null) return defaultValue;
        if (!node.isLong()) return defaultValue;
        return node.asLong();
    }

    /**
     * Замена элемента списка в конкретном индексе на новый с long значением. Если узел не является списком, то будет exception.
     * Если индекс меньше 0, то будет exception.
     * Если индекс больше или равен размеру списка, то элемент будет добавлен в конец списка.
     *
     * @param index Индекс, в который будет установлен элемент
     * @param value Элемент, который будет установлен
     * @return Этот же узел.
     */
    public SiNode set(int index, long value) {
        set(index, new SiNode(value));
        return this;
    }

    /**
     * Добавление long элемента в список. Если узел не является списком, то будет exception.
     *
     * @param value Элемент, который будет добавлен
     * @return Этот же узел.
     */
    public SiNode add(long value) {
        add(new SiNode(value));
        return this;
    }

    /**
     * Установка дочернего узла с заданным именем. Если узел не является таблицей, то будет exception.
     *
     * @param child Имя дочернего узла
     * @param value long значение узла
     * @return Этот же узел.
     */
    public SiNode set(String child, long value) {
        set(child, new SiNode(value));
        return this;
    }

//endregion
//region float

    /**
     * Проверяет, является ли этот узел Float.
     *
     * @return true, если узел является Float, иначе false.
     */
    public boolean isFloat() {
        if (!expect(true, SiType.FLOAT, SiType.INTEGER)) return false;
        double value = asNumber().doubleValue();
        return Float.MIN_VALUE <= value && value <= Float.MAX_VALUE;
    }

    /**
     * Установка float значения в узел.
     *
     * @param value float значение
     * @return Этот же узел.
     */
    public SiNode set(float value) {
        type = (value == ((Float) value).longValue()) ? SiType.INTEGER : SiType.FLOAT;
        this.value = (double) value;
        return this;
    }

    /**
     * Проверяет, является ли узел по индексу Float.
     *
     * @param index Индекс узла списка
     * @return true, если узел по индексу является Float.false если не является, или если индекс за границей списка, или если узел не является списком.
     */
    public boolean isFloat(int index) {
        return hasIndex(index) && getOrThrow(index).isFloat();
    }

    /**
     * Проверяет, является ли дочерний узел Float.
     *
     * @param child Имя дочернего узла
     * @return true, если дочерний узел является Float.false если не является, или если дочернего узла с таким именем нет, или если узел не является таблицей.
     */
    public boolean isFloat(String child) {
        return hasChild(child) && getOrThrown(child).isFloat();
    }

    /**
     * Преобразует узел к float.
     * <p>
     * ВАЖНО! Если узел другого типа, то может возникнуть ошибка!
     *
     * @return float значение узла.
     */
    public float asFloat() {
        if (!expect(true, SiType.FLOAT, SiType.INTEGER)) return 0;
        return asNumber().floatValue();
    }

    /**
     * Получение Float значения узла из списка.
     *
     * @param index Индекс узла в списке
     * @return Float значение или null, если узел другого типа, или если индекс за границей списка, или если узел не является списком.
     */
    @Nullable
    public Float getFloat(int index) {
        SiNode node = getOrNull(index);
        if (node == null) return null;
        if (!node.isFloat()) return null;
        return node.asFloat();
    }

    /**
     * Получение Float значения узла из списка.
     *
     * @param index        Индекс узла в списке
     * @param defaultValue Значение по умолчанию
     * @return Float значение или значение по умолчанию, если узел другого типа, или если индекс за границей списка, или если узел не является списком.
     */
    public float getFloat(int index, float defaultValue) {
        SiNode node = getOrNull(index);
        if (node == null) return defaultValue;
        if (!node.isFloat()) return defaultValue;
        return node.asFloat();
    }

    /**
     * Получение Float значения дочернего узла.
     *
     * @param child Имя дочернего узла
     * @return Float значение или null, если дочерний узел другого типа, или если дочернего узла с таким именем нет, или если узел не является таблицей.
     */
    @Nullable
    public Float getFloat(String child) {
        SiNode node = getOrNull(child);
        if (node == null) return null;
        if (!node.isFloat()) return null;
        return node.asFloat();
    }

    /**
     * Получение Float значения дочернего узла.
     *
     * @param child        Имя дочернего узла
     * @param defaultValue Значение по умолчанию
     * @return Float значение или значение по умолчанию, если дочерний узел другого типа, или если дочернего узла с таким именем нет, или если узел не является таблицей.
     */
    public float getFloat(String child, float defaultValue) {
        SiNode node = getOrNull(child);
        if (node == null) return defaultValue;
        if (!node.isFloat()) return defaultValue;
        return node.asFloat();
    }


    /**
     * Добавление float элемента в список. Если узел не является списком, то будет exception.
     *
     * @param value Элемент, который будет добавлен
     * @return Этот же узел.
     */
    public SiNode add(float value) {
        add(new SiNode(value));
        return this;
    }

    /**
     * Установка дочернего узла с заданным именем. Если узел не является таблицей, то будет exception.
     *
     * @param child Имя дочернего узла
     * @param value float значение узла
     * @return Этот же узел.
     */
    public SiNode set(String child, float value) {
        set(child, new SiNode(value));
        return this;
    }

    //endregion
    public Number asNumber() {
        expect(true, SiType.INTEGER, SiType.FLOAT);

        return (Number) value;
    }

//region double

    /**
     * Проверяет, является ли этот узел Double.
     *
     * @return true, если узел является Double, иначе false.
     */
    public boolean isDouble() {
        return expect(true, SiType.FLOAT) || expect(true, SiType.INTEGER);
    }

    /**
     * Установка double значения в узел.
     *
     * @param value double значение
     * @return Этот же узел.
     */
    public SiNode set(double value) {
        type = (value == ((Double) value).longValue()) ? SiType.INTEGER : SiType.FLOAT;
        this.value = value;
        return this;
    }

    /**
     * Проверяет, является ли узел по индексу Double.
     *
     * @param index Индекс узла списка
     * @return true, если узел по индексу является Double.false если не является, или если индекс за границей списка, или если узел не является списком.
     */
    public boolean isDouble(int index) {
        return hasIndex(index) && getOrThrow(index).isDouble();
    }

    /**
     * Проверяет, является ли дочерний узел Double.
     *
     * @param child Имя дочернего узла
     * @return true, если дочерний узел является Double.false если не является, или если дочернего узла с таким именем нет, или если узел не является таблицей.
     */
    public boolean isDouble(String child) {
        return hasChild(child) && getOrThrown(child).isDouble();
    }

    /**
     * Преобразует узел к double.
     * <p>
     * ВАЖНО! Если узел другого типа, то может возникнуть ошибка!
     *
     * @return double значение узла.
     */
    public double asDouble() {
        expect(true, SiType.INTEGER);
        return asNumber().doubleValue();
    }


    /**
     * Получение Double значения узла из списка.
     *
     * @param index Индекс узла в списке
     * @return Double значение или null, если узел другого типа, или если индекс за границей списка, или если узел не является списком.
     */
    @Nullable
    public Double getDouble(int index) {
        SiNode node = getOrNull(index);
        if (node == null) return null;
        if (!node.isDouble()) return null;
        return node.asDouble();
    }

    /**
     * Получение Double значения узла из списка.
     *
     * @param index        Индекс узла в списке
     * @param defaultValue Значение по умолчанию
     * @return Double значение или значение по умолчанию, если узел другого типа, или если индекс за границей списка, или если узел не является списком.
     */
    public double getDouble(int index, double defaultValue) {
        SiNode node = getOrNull(index);
        if (node == null) return defaultValue;
        if (!node.isDouble()) return defaultValue;
        return node.asDouble();
    }

    /**
     * Получение Double значения дочернего узла.
     *
     * @param child Имя дочернего узла
     * @return Double значение или null, если дочерний узел другого типа, или если дочернего узла с таким именем нет, или если узел не является таблицей.
     */
    @Nullable
    public Double getDouble(String child) {
        SiNode node = getOrNull(child);
        if (node == null) return null;
        if (!node.isDouble()) return null;
        return node.asDouble();
    }

    /**
     * Получение Double значения дочернего узла.
     *
     * @param child        Имя дочернего узла
     * @param defaultValue Значение по умолчанию
     * @return Double значение или значение по умолчанию, если дочерний узел другого типа, или если дочернего узла с таким именем нет, или если узел не является таблицей.
     */
    public double getDouble(String child, double defaultValue) {
        SiNode node = getOrNull(child);
        if (node == null) return defaultValue;
        if (!node.isDouble()) return defaultValue;
        return node.asDouble();
    }

    /**
     * Замена элемента списка в конкретном индексе на новый с double значением. Если узел не является списком, то будет exception.
     * Если индекс меньше 0, то будет exception.
     * Если индекс больше или равен размеру списка, то элемент будет добавлен в конец списка.
     *
     * @param index Индекс, в который будет установлен элемент
     * @param value Элемент, который будет установлен
     * @return Этот же узел.
     */
    public SiNode set(int index, double value) {
        set(index, new SiNode(value));
        return this;
    }


    /**
     * Добавление double элемента в список. Если узел не является списком, то будет exception.
     *
     * @param value Элемент, который будет добавлен
     * @return Этот же узел.
     */
    public SiNode add(double value) {
        add(new SiNode(value));
        return this;
    }

    /**
     * Установка дочернего узла с заданным именем. Если узел не является таблицей, то будет exception.
     *
     * @param child Имя дочернего узла
     * @param value double значение узла
     * @return Этот же узел.
     */
    public SiNode set(String child, double value) {
        set(child, new SiNode(value));
        return this;
    }
    //endregion
//region char

    /**
     * Проверяет, является ли этот узел Character.
     *
     * @return true, если узел является Character, иначе false.
     */
    public boolean isCharacter() {
        return expect(false, SiType.STRING) && asString().length() == 1;
    }

    /**
     * Установка char значения в узел.
     *
     * @param value char значение
     * @return Этот же узел.
     */
    public SiNode set(char value) {
        type = SiType.STRING;
        this.value = String.valueOf(value);
        return this;
    }

    /**
     * Проверяет, является ли узел по индексу Character.
     *
     * @param index Индекс узла списка
     * @return true, если узел по индексу является Character.false если не является, или если индекс за границей списка, или если узел не является списком.
     */
    public boolean isCharacter(int index) {
        return hasIndex(index) && getOrThrow(index).isCharacter();
    }

    /**
     * Проверяет, является ли дочерний узел Character.
     *
     * @param child Имя дочернего узла
     * @return true, если дочерний узел является Character.false если не является, или если дочернего узла с таким именем нет, или если узел не является таблицей.
     */
    public boolean isCharacter(String child) {
        return hasChild(child) && getOrThrown(child).isCharacter();
    }

    /**
     * Преобразует узел к char.
     * <p>
     * ВАЖНО! Если узел другого типа, то может возникнуть ошибка!
     *
     * @return char значение узла.
     */
    public char asCharacter() {
        expect(true, SiType.STRING);
        return asString().charAt(0);
    }

    /**
     * Получение Character значения узла из списка.
     *
     * @param index Индекс узла в списке
     * @return Character значение или null, если узел другого типа, или если индекс за границей списка, или если узел не является списком.
     */
    @Nullable
    public Character getCharacter(int index) {
        SiNode node = getOrNull(index);
        if (node == null) return null;
        if (!node.isCharacter()) return null;
        return node.asCharacter();
    }

    /**
     * Получение Character значения узла из списка.
     *
     * @param index        Индекс узла в списке
     * @param defaultValue Значение по умолчанию
     * @return Character значение или значение по умолчанию, если узел другого типа, или если индекс за границей списка, или если узел не является списком.
     */
    public char getCharacter(int index, char defaultValue) {
        SiNode node = getOrNull(index);
        if (node == null) return defaultValue;
        if (!node.isCharacter()) return defaultValue;
        return node.asCharacter();
    }

    /**
     * Получение Character значения дочернего узла.
     *
     * @param child Имя дочернего узла
     * @return Character значение или null, если дочерний узел другого типа, или если дочернего узла с таким именем нет, или если узел не является таблицей.
     */
    @Nullable
    public Character getCharacter(String child) {
        SiNode node = getOrNull(child);
        if (node == null) return null;
        if (!node.isCharacter()) return null;
        return node.asCharacter();
    }

    /**
     * Получение Character значения дочернего узла.
     *
     * @param child        Имя дочернего узла
     * @param defaultValue Значение по умолчанию
     * @return Character значение или значение по умолчанию, если дочерний узел другого типа, или если дочернего узла с таким именем нет, или если узел не является таблицей.
     */
    public char getCharacter(String child, char defaultValue) {
        SiNode node = getOrNull(child);
        if (node == null) return defaultValue;
        if (!node.isCharacter()) return defaultValue;
        return node.asCharacter();
    }

    /**
     * Замена элемента списка в конкретном индексе на новый с char значением. Если узел не является списком, то будет exception.
     * Если индекс меньше 0, то будет exception.
     * Если индекс больше или равен размеру списка, то элемент будет добавлен в конец списка.
     *
     * @param index Индекс, в который будет установлен элемент
     * @param value Элемент, который будет установлен
     * @return Этот же узел.
     */
    public SiNode set(int index, char value) {
        set(index, new SiNode(value));
        return this;
    }


    /**
     * Добавление char элемента в список. Если узел не является списком, то будет exception.
     *
     * @param value Элемент, который будет добавлен
     * @return Этот же узел.
     */
    public SiNode add(char value) {
        add(new SiNode(value));
        return this;
    }

    /**
     * Установка дочернего узла с заданным именем. Если узел не является таблицей, то будет exception.
     *
     * @param child Имя дочернего узла
     * @param value char значение узла
     * @return Этот же узел.
     */
    public SiNode set(String child, char value) {
        set(child, new SiNode(value));
        return this;
    }
    //endregion
//region String

    /**
     * Проверяет, является ли этот узел String.
     *
     * @return true, если узел является String, иначе false.
     */
    public boolean isString() {
        return expect(true, SiType.STRING);
    }

    /**
     * Установка String значения в узел.
     *
     * @param value String значение
     * @return Этот же узел.
     */
    public SiNode set(String value) {
        type = SiType.STRING;
        this.value = value;
        return this;
    }

    /**
     * Проверяет, является ли узел по индексу String.
     *
     * @param index Индекс узла списка
     * @return true, если узел по индексу является String.false если не является, или если индекс за границей списка, или если узел не является списком.
     */
    public boolean isString(int index) {
        return hasIndex(index) && getOrThrow(index).isString();
    }

    /**
     * Проверяет, является ли дочерний узел String.
     *
     * @param child Имя дочернего узла
     * @return true, если дочерний узел является String.false если не является, или если дочернего узла с таким именем нет, или если узел не является таблицей.
     */
    public boolean isString(String child) {
        return hasChild(child) && getOrThrown(child).isString();
    }

    /**
     * Преобразует узел к String.
     * <p>
     * ВАЖНО! Если узел другого типа, то может возникнуть ошибка!
     *
     * @return String значение узла.
     */
    public String asString() {
        expect(true, SiType.STRING);
        return (String) value;
    }

    /**
     * Получение String значения узла из списка.
     *
     * @param index Индекс узла в списке
     * @return String значение или null, если узел другого типа, или если индекс за границей списка, или если узел не является списком.
     */
    @Nullable
    public String getString(int index) {
        SiNode node = getOrNull(index);
        if (node == null) return null;
        if (!node.isString()) return null;
        return node.asString();
    }

    /**
     * Получение String значения узла из списка.
     *
     * @param index        Индекс узла в списке
     * @param defaultValue Значение по умолчанию
     * @return String значение или значение по умолчанию, если узел другого типа, или если индекс за границей списка, или если узел не является списком.
     */
    public String getString(int index, String defaultValue) {
        SiNode node = getOrNull(index);
        if (node == null) return defaultValue;
        if (!node.isString()) return defaultValue;
        return node.asString();
    }

    /**
     * Получение String значения дочернего узла.
     *
     * @param child Имя дочернего узла
     * @return String значение или null, если дочерний узел другого типа, или если дочернего узла с таким именем нет, или если узел не является таблицей.
     */
    @Nullable
    public String getString(String child) {
        SiNode node = getOrNull(child);
        if (node == null) return null;
        if (!node.isString()) return null;
        return node.asString();
    }

    /**
     * Получение String значения дочернего узла.
     *
     * @param child        Имя дочернего узла
     * @param defaultValue Значение по умолчанию
     * @return String значение или значение по умолчанию, если дочерний узел другого типа, или если дочернего узла с таким именем нет, или если узел не является таблицей.
     */
    public String getString(String child, String defaultValue) {
        SiNode node = getOrNull(child);
        if (node == null) return defaultValue;
        if (!node.isString()) return defaultValue;
        return node.asString();
    }

    /**
     * Замена элемента списка в конкретном индексе на новый с String значением. Если узел не является списком, то будет exception.
     * Если индекс меньше 0, то будет exception.
     * Если индекс больше или равен размеру списка, то элемент будет добавлен в конец списка.
     *
     * @param index Индекс, в который будет установлен элемент
     * @param value Элемент, который будет установлен
     * @return Этот же узел.
     */
    public SiNode set(int index, String value) {
        set(index, new SiNode(value));
        return this;
    }


    /**
     * Добавление String элемента в список. Если узел не является списком, то будет exception.
     *
     * @param value Элемент, который будет добавлен
     * @return Этот же узел.
     */
    public SiNode add(String value) {
        add(new SiNode(value));
        return this;
    }

    /**
     * Установка дочернего узла с заданным именем. Если узел не является таблицей, то будет exception.
     *
     * @param child Имя дочернего узла
     * @param value String значение узла
     * @return Этот же узел.
     */
    public SiNode set(String child, String value) {
        set(child, new SiNode(value));
        return this;
    }
    //endregion
//region list

    /**
     * Проверяет, является ли этот узел List.
     *
     * @return true, если узел является List, иначе false.
     */
    public boolean isList() {
        return type == SiType.LIST;
    }

    /**
     * Установка List<SiNode> значения в узел.
     *
     * @param value List<SiNode> значение
     * @return Этот же узел.
     */
    public SiNode set(List<SiNode> value) {
        type = SiType.LIST;
        this.value = new ArrayList<>(value);
        return this;
    }

    /**
     * Проверяет, является ли узел по индексу List.
     *
     * @param index Индекс узла списка
     * @return true, если узел по индексу является List.false если не является, или если индекс за границей списка, или если узел не является списком.
     */
    public boolean isList(int index) {
        return hasIndex(index) && getOrThrow(index).isList();
    }

    /**
     * Проверяет, является ли дочерний узел List.
     *
     * @param child Имя дочернего узла
     * @return true, если дочерний узел является List.false если не является, или если дочернего узла с таким именем нет, или если узел не является таблицей.
     */
    public boolean isList(String child) {
        return hasChild(child) && getOrThrown(child).isList();
    }

    /**
     * Преобразует узел к List<SiNode>.
     * <p>
     * ВАЖНО! Если узел другого типа, то может возникнуть ошибка!
     *
     * @return List<SiNode> значение узла.
     */
    public List<SiNode> asList() {
        expect(true, SiType.LIST);
        //noinspection unchecked
        return (List<SiNode>) value;
    }

    /**
     * Получение List<SiNode> значения узла из списка.
     *
     * @param index Индекс узла в списке
     * @return List<SiNode> значение или null, если узел другого типа, или если индекс за границей списка, или если узел не является списком.
     */
    @Nullable
    public List<SiNode> getList(int index) {
        return getList(index, null);
    }

    /**
     * Получение List<SiNode> значения узла из списка.
     *
     * @param index        Индекс узла в списке
     * @param defaultValue Значение по умолчанию
     * @return List<SiNode> значение или значение по умолчанию, если узел другого типа, или если индекс за границей списка, или если узел не является списком.
     */
    public List<SiNode> getList(int index, List<SiNode> defaultValue) {
        SiNode node = getOrNull(index);
        if (node == null) return defaultValue;
        if (!node.isList()) return defaultValue;
        return node.asList();
    }

    /**
     * Получение List<SiNode> значения дочернего узла.
     *
     * @param child Имя дочернего узла
     * @return List<SiNode> значение или null, если дочерний узел другого типа, или если дочернего узла с таким именем нет, или если узел не является таблицей.
     */
    @Nullable
    public List<SiNode> getList(String child) {
        return getList(child, null);
    }

    /**
     * Получение List<SiNode> значения дочернего узла.
     *
     * @param child        Имя дочернего узла
     * @param defaultValue Значение по умолчанию
     * @return List<SiNode> значение или значение по умолчанию, если дочерний узел другого типа, или если дочернего узла с таким именем нет, или если узел не является таблицей.
     */
    public List<SiNode> getList(String child, List<SiNode> defaultValue) {
        SiNode node = getOrNull(child);
        if (node == null) return defaultValue;
        if (!node.isList()) return defaultValue;
        return node.asList();
    }

    /**
     * Замена элемента списка в конкретном индексе на новый с List<SiNode> значением. Если узел не является списком, то будет exception.
     * Если индекс меньше 0, то будет exception.
     * Если индекс больше или равен размеру списка, то элемент будет добавлен в конец списка.
     *
     * @param index Индекс, в который будет установлен элемент
     * @param value Элемент, который будет установлен
     * @return Этот же узел.
     */
    public SiNode set(int index, List<SiNode> value) {
        set(index, new SiNode(value));
        return this;
    }


    /**
     * Добавление List<SiNode> элемента в список. Если узел не является списком, то будет exception.
     *
     * @param value Элемент, который будет добавлен
     * @return Этот же узел.
     */
    public SiNode add(List<SiNode> value) {
        add(new SiNode(value));
        return this;
    }

    /**
     * Установка дочернего узла с заданным именем. Если узел не является таблицей, то будет exception.
     *
     * @param child Имя дочернего узла
     * @param value List<SiNode> значение узла
     * @return Этот же узел.
     */
    public SiNode set(String child, List<SiNode> value) {
        set(child, new SiNode(value));
        return this;
    }
    //endregion
//region map

    /**
     * Проверяет, является ли этот узел Map.
     *
     * @return true, если узел является Map, иначе false.
     */
    public boolean isMap() {
        return expect(false, SiType.MAP);
    }

    /**
     * Установка Map<String, SiNode> значения в узел.
     *
     * @param value Map<String, SiNode> значение
     * @return Этот же узел.
     */
    public SiNode set(Map<String, SiNode> value) {
        type = SiType.MAP;
        this.value = new HashMap<Object, Object>(value);
        return this;
    }

    /**
     * Проверяет, является ли узел по индексу Map.
     *
     * @param index Индекс узла списка
     * @return true, если узел по индексу является Map.false если не является, или если индекс за границей списка, или если узел не является списком.
     */
    public boolean isMap(int index) {
        return hasIndex(index) && getOrThrow(index).isMap();
    }

    /**
     * Проверяет, является ли дочерний узел Map.
     *
     * @param child Имя дочернего узла
     * @return true, если дочерний узел является Map.false если не является, или если дочернего узла с таким именем нет, или если узел не является таблицей.
     */
    public boolean isMap(String child) {
        return hasChild(child) && getOrThrown(child).isMap();
    }

    /**
     * Преобразует узел к Map<String, SiNode>.
     * <p>
     * ВАЖНО! Если узел другого типа, то может возникнуть ошибка!
     *
     * @return Map<String, SiNode> значение узла.
     */
    @NotNull
    public Map<String, SiNode> asMap() {
        expect(true, SiType.MAP);
        //noinspection unchecked
        return (Map<String, SiNode>) value;
    }

    /**
     * Получение Map<String, SiNode> значения узла из списка.
     *
     * @param index Индекс узла в списке
     * @return Map<String, SiNode> значение или null, если узел другого типа, или если индекс за границей списка, или если узел не является списком.
     */
    @Nullable
    public Map<String, SiNode> getMap(int index) {
        return getMap(index, null);
    }

    /**
     * Получение Map<String, SiNode> значения узла из списка.
     *
     * @param index        Индекс узла в списке
     * @param defaultValue Значение по умолчанию
     * @return Map<String, SiNode> значение или значение по умолчанию, если узел другого типа, или если индекс за границей списка, или если узел не является списком.
     */
    public Map<String, SiNode> getMap(int index, Map<String, SiNode> defaultValue) {
        SiNode node = getOrNull(index);
        if (node == null) return defaultValue;
        if (!node.isMap()) return defaultValue;
        return node.asMap();
    }

    /**
     * Получение Map<String, SiNode> значения дочернего узла.
     *
     * @param child Имя дочернего узла
     * @return Map<String, SiNode> значение или null, если дочерний узел другого типа, или если дочернего узла с таким именем нет, или если узел не является таблицей.
     */
    @Nullable
    public Map<String, SiNode> getMap(String child) {
        return getMap(child, null);
    }

    /**
     * Получение Map<String, SiNode> значения дочернего узла.
     *
     * @param child        Имя дочернего узла
     * @param defaultValue Значение по умолчанию
     * @return Map<String, SiNode> значение или значение по умолчанию, если дочерний узел другого типа, или если дочернего узла с таким именем нет, или если узел не является таблицей.
     */
    public Map<String, SiNode> getMap(String child, Map<String, SiNode> defaultValue) {
        SiNode node = getOrThrown(child);
        if (node == null) return defaultValue;
        if (!node.isMap()) return defaultValue;
        return node.asMap();
    }

    /**
     * Замена элемента списка в конкретном индексе на новый с Map<String, SiNode> значением. Если узел не является списком, то будет exception.
     * Если индекс меньше 0, то будет exception.
     * Если индекс больше или равен размеру списка, то элемент будет добавлен в конец списка.
     *
     * @param index Индекс, в который будет установлен элемент
     * @param value Элемент, который будет установлен
     * @return Этот же узел.
     */
    public SiNode set(int index, Map<String, SiNode> value) {
        set(index, new SiNode(value));
        return this;
    }

    /**
     * Добавление Map<String, SiNode> элемента в список. Если узел не является списком, то будет exception.
     *
     * @param value Элемент, который будет добавлен
     * @return Этот же узел.
     */
    public SiNode add(Map<String, SiNode> value) {
        add(new SiNode(value));
        return this;
    }

    /**
     * Установка дочернего узла с заданным именем. Если узел не является таблицей, то будет exception.
     *
     * @param child Имя дочернего узла
     * @param value Map<String, SiNode> значение узла
     * @return Этот же узел.
     */
    public SiNode set(String child, Map<String, SiNode> value) {
        set(child, new SiNode(value));
        return this;
    }

    //endregion
    protected boolean expect(boolean throwException, SiType expected, SiType... extra) {
        if (expected == type) return true;
        for (SiType siType : extra) {
            if (siType == type) return true;
        }
        if (!throwException) return false;
        throw new UnexpectedTypeException(type, expected, extra);
    }

    protected boolean expect(boolean throwException, SiType expected) {
        if (expected == type) return true;
        if (!throwException) return false;
        throw new UnexpectedTypeException(type, expected);
    }
}