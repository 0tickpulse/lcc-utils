package net.tickmc.lccutils.managers.configuration;

import java.util.Map;

public class ConfigurationValidator implements ConfigurationType.Visitor<Boolean> {

    public boolean validate(ConfigurationType<?> type, Object value) {
        return type.accept(this, value);
    }

    @Override
    public Boolean visit(ConfigurationType.StringConfigurationType type, Object value) {
        return value instanceof String;
    }

    @Override
    public Boolean visit(ConfigurationType.BooleanConfigurationType type, Object value) {
        return value instanceof Boolean;
    }

    @Override
    public Boolean visit(ConfigurationType.IntegerConfigurationType type, Object value) {
        return value instanceof Integer;
    }

    @Override
    public Boolean visit(ConfigurationType.DoubleConfigurationType type, Object value) {
        return value instanceof Double;
    }

    @Override
    public Boolean visit(ConfigurationType.ListConfigurationType<?, ?> type, Object value) {
        if (!(value instanceof Iterable)) {
            return false;
        }

        for (Object element : (Iterable<?>) value) {
            if (!validate(type, element)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Boolean visit(ConfigurationType.MapConfigurationType<?, ?> type, Object value) {
        if (!(value instanceof Map)) {
            return false;
        }

        for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
            if (!(entry.getKey() instanceof String)) {
                return false;
            }

            if (!validate(type, entry.getValue())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Boolean visit(ConfigurationType.CustomMapConfigurationType<?> type, Object value) {
        if (!(value instanceof Map<?, ?> map)) {
            return false;
        }
        for (Map.Entry<String, ConfigurationType<?>> entry : type.types.entrySet()) {
            if (!map.containsKey(entry.getKey())) {
                return false;
            }
            if (!validate(entry.getValue(), map.get(entry.getKey()))) {
                return false;
            }
        }
        return true;
    }
}
