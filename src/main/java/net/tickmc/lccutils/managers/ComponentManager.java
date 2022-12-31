package net.tickmc.lccutils.managers;

import net.tickmc.lccutils.components.LccComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for registering and unregistering components.
 */
public class ComponentManager {
    /**
     * Represents an error that occurs when attempting to manage components.
     */
    public static class ComponentManagementError extends Error {
        public ComponentManagementError(String message) {
            super(message);
        }
    }
    private static final Map<String, List<LccComponent<?>>> components = new HashMap<>();

    /**
     * Registers a component.
     *
     * @param component The component to register.
     */
    public static void registerComponent(LccComponent<?> component) {
        String category = component.getCategory();
        if (!components.containsKey(category)) {
            components.put(category, new ArrayList<>());
        }
        components.get(category).add(component);
        component.onEnable();
    }

    /**
     * Unregisters a component.
     *
     * @param component The component to unregister.
     */
    public static void unregisterComponent(LccComponent<?> component) {
        String category = component.getCategory();
        if (components.containsKey(category)) {
            components.get(category).remove(component);
            component.onDisable();
        }
        throw new ComponentManagementError("LccComponent " + component.getName() + " is not registered.");
    }

    /**
     * Returns a list of all registered components.
     */
    public static List<LccComponent<?>> getAllComponents() {
        List<LccComponent<?>> allComponents = new ArrayList<>();
        for (List<LccComponent<?>> categoryComponents : components.values()) {
            allComponents.addAll(categoryComponents);
        }
        return allComponents;
    }

    /**
     * Returns whether this component is currently registered.
     *
     * @param component The component to check.
     */
    public static boolean isRegistered(LccComponent<?> component) {
        return components.containsKey(component.getCategory()) && components.get(component.getCategory()).contains(component);
    }

    /**
     * Returns a list of all registered components' categories.
     */
    public static List<String> getCategories() {
        return new ArrayList<>(components.keySet());
    }

    /**
     * Returns a list of all registered components in the specified category.
     */
    public static List<LccComponent<?>> getComponentsByCategory(String category) {
        List<LccComponent<?>> list = components.get(category);
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }

    public static List<LccComponent<?>> getComponentsByName(String category, String name) {
        List<LccComponent<?>> list = components.get(category);
        if (list == null) {
            return new ArrayList<>();
        }
        List<LccComponent<?>> components = new ArrayList<>();
        for (LccComponent<?> component : list) {
            if (component.getName().equalsIgnoreCase(name)) {
                components.add(component);
            }
        }
        return components;
    }

    /**
     * Returns the raw map of components.
     */
    public static Map<String, List<LccComponent<?>>> getComponents() {
        return components;
    }
}