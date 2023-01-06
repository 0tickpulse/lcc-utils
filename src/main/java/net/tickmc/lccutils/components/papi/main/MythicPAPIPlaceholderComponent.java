package net.tickmc.lccutils.components.papi.main;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.mobs.GenericCaster;
import io.lumine.mythic.api.skills.SkillCaster;
import io.lumine.mythic.api.skills.placeholders.PlaceholderString;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.core.skills.placeholders.PlaceholderMeta;
import io.lumine.mythic.core.skills.placeholders.parsers.PlaceholderStringImpl;
import net.tickmc.lccutils.components.papi.PAPIPlaceholderComponent;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;

public class MythicPAPIPlaceholderComponent extends PAPIPlaceholderComponent {

    public MythicPAPIPlaceholderComponent() {
        super();
        addNames("mythic");
        setDescription("Parses a mythic expression and return the result. To get the player, use 'caster'.");
        addAuthors("0TickPulse");
    }

    @Nullable
    @Override
    public String get(OfflinePlayer player, String arg) {
        if (!player.isOnline()) {
            return null;
        }
        PlaceholderString placeholderString = PlaceholderStringImpl.of(arg);
        PlaceholderMeta meta = new PlaceholderMeta() {
            @Override
            public SkillCaster getCaster() {
                return new GenericCaster(BukkitAdapter.adapt(player.getPlayer()));
            }

            @Override
            public AbstractEntity getTrigger() {
                return BukkitAdapter.adapt(player.getPlayer());
            }
        };
        return placeholderString.get(meta);
    }
}
