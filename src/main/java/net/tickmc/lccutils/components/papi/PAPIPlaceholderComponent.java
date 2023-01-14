package net.tickmc.lccutils.components.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.tickmc.lccutils.LccUtils;
import net.tickmc.lccutils.components.ComponentCategory;
import net.tickmc.lccutils.components.LccComponent;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PAPIPlaceholderComponent extends LccComponent<PAPIPlaceholderComponent> {

    public abstract @Nullable String get(OfflinePlayer player, String arg);

    public PAPIPlaceholderComponent() {
        super();
        setCategory(ComponentCategory.PAPI_PLACEHOLDER);
    }

    @Override
    public void onEnable() {
        generateExpansion().register();
    }

    @Override
    public void onDisable() {
        generateExpansion().unregister();
    }

    private PlaceholderExpansion generateExpansion() {
        PAPIPlaceholderComponent component = this;
        class GeneratedExpansion extends PlaceholderExpansion {

            @Override
            public @NotNull String getIdentifier() {
                return component.getName();
            }

            @Override
            public @NotNull String getAuthor() {
                return component.getAuthorsString();
            }

            @Override
            public @NotNull String getVersion() {
                return LccUtils.getVersion().toString();
            }

            @Override
            public boolean persist() {
                return true;
            }

            @Override
            public boolean canRegister() {
                return true;
            }

            @Override
            public String onRequest(OfflinePlayer player, @NotNull String params) {
                return component.get(player, params);
            }
        }
        return new GeneratedExpansion();
    }

}
