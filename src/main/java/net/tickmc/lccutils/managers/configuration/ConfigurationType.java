package net.tickmc.lccutils.managers.configuration;

import java.util.List;
import java.util.Map;

/**
 * Configuration types for YAML.
 */
public abstract class ConfigurationType<D> {
    public interface Visitor<R> {
        R visit(StringConfigurationType type, Object value);

        R visit(BooleanConfigurationType type, Object value);

        R visit(IntegerConfigurationType type, Object value);

        R visit(DoubleConfigurationType type, Object value);

        R visit(ListConfigurationType<?, ?> type, Object value);

        R visit(MapConfigurationType<?, ?> type, Object value);

        R visit(CustomMapConfigurationType<?> type, Object value);
    }

    public static class StringConfigurationType extends ConfigurationType<String> {

        public StringConfigurationType(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        @Override
        public <R> R accept(Visitor<R> visitor, Object value) {
            return visitor.visit(this, value);
        }
    }

    public static class IntegerConfigurationType extends ConfigurationType<Integer> {

        public IntegerConfigurationType(Integer defaultValue) {
            this.defaultValue = defaultValue;
        }

        @Override
        public <R> R accept(Visitor<R> visitor, Object value) {
            return visitor.visit(this, value);
        }
    }

    public static class DoubleConfigurationType extends ConfigurationType<Double> {

        public DoubleConfigurationType(Double defaultValue) {
            this.defaultValue = defaultValue;
        }

        @Override
        public <R> R accept(Visitor<R> visitor, Object value) {
            return visitor.visit(this, value);
        }
    }

    public static class BooleanConfigurationType extends ConfigurationType<Boolean> {

        public BooleanConfigurationType(Boolean defaultValue) {
            this.defaultValue = defaultValue;
        }

        @Override
        public <R> R accept(Visitor<R> visitor, Object value) {
            return visitor.visit(this, value);
        }
    }

    public static class ListConfigurationType<T extends ConfigurationType<VD>, VD> extends ConfigurationType<List<VD>> {

        public ListConfigurationType(List<VD> defaultValue) {
            this.defaultValue = defaultValue;
        }

        @Override
        public <R> R accept(Visitor<R> visitor, Object value) {
            return visitor.visit(this, value);
        }
    }

    public static class MapConfigurationType<T extends ConfigurationType<VD>, VD> extends ConfigurationType<Map<String, VD>> {

        public MapConfigurationType(Map<String, VD> defaultValue) {
            this.defaultValue = defaultValue;
        }

        @Override
        public <R> R accept(Visitor<R> visitor, Object value) {
            return visitor.visit(this, value);
        }
    }

    public static class CustomMapConfigurationType<VD> extends ConfigurationType<Map<String, VD>> {

        public final Map<String, ConfigurationType<?>> types;

        public CustomMapConfigurationType(Map<String, VD> defaultValue, Map<String, ConfigurationType<?>> types) {
            this.defaultValue = defaultValue;
            this.types = types;
        }

        @Override
        public <R> R accept(Visitor<R> visitor, Object value) {
            return visitor.visit(this, value);
        }
    }

    public abstract <R> R accept(Visitor<R> visitor, Object value);

    public D defaultValue;
}
