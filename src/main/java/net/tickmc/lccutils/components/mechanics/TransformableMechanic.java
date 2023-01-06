package net.tickmc.lccutils.components.mechanics;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.ITargetedLocationSkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.placeholders.PlaceholderDouble;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.SkillMechanic;
import net.tickmc.lccutils.components.ComponentWithFields;
import net.tickmc.lccutils.utilities.LocationUtilities;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.List;

/**
 * A transformable mechanic is a mechanic that performs certain operations on a set of locations.
 * This abstract class gives Mythic users the ability to arbitrarily transform the locations before they are used, increasing server owners' freedom.
 * When extending this class, several new fields are available to the user:
 * <ul>
 *     <li>{@link #xOffset} - Additional offset in the X-axis.</li>
 *     <li>{@link #yOffset} - Additional offset in the Y-axis.</li>
 *     <li>{@link #zOffset} - Additional offset in the Z-axis.</li>
 *     <li>{@link #forwardOffset} - Additional forward offset. This is based on the caster's yaw and pitch.</li>
 *     <li>{@link #rightOffset} - Additional right offset. This is based on the caster's yaw and pitch. This is equivalent to {@link #forwardOffset} but with pitch set to 0 and yaw rotated by -90.</li>
 *     <li>{@link #verticalOffset} - Additional vertical offset. This is based on the caster's yaw and pitch. This is equivalent to {@link #forwardOffset} but with pitch rotated by +90.</li>
 *     <li>{@link #scale} - This mechanic takes the points and calculates a center point. Then, for each of the points, it calculates a vector from the center to that point. The size field simply multiplies this vector.</li>
 *     <li>{@link #rotation} - The rotation of the slash in degrees.</li>
 *     <li>{@link #radians} - Whether to use radians instead of degrees for the rotation.</li>
 *     <li>{@link #inferDirection} - If set to true, instead of getting the direction from the target location, it gets the direction of an arbitrary vector from the caster to the target location.</li>
 * </ul>
 * Keep in mind that mechanics extending this class can be run targeting either a set of locations or a set of entities.
 * When targeting entities, the class will get their locations and use those instead.
 * <p>
 * To generate a list of points to perform your mechanic on, override the {@link #getPoints(SkillMetadata, Location)} method.
 * This method will be called for each location every time the skill is used. As mentioned before, when targeting entities, their locations will be used instead.
 * <p>
 * Before running the skill, the {@link #transform(SkillMetadata, List)} method will be called on the locations from {@link #getPoints(SkillMetadata, Location)}.
 * This will take the aforementioned fields and use them to transform the points.
 * <p>
 * Finally, the {@link #cast(SkillMetadata, List)} method will be called on the transformed points.
 * Classes which extend this class should override this method to perform their mechanic.
 * <p>
 * In summary, in order to make use of this class, do the following:
 * <ol>
 *     <li>Override the {@link #getPoints(SkillMetadata, Location)} method to generate a list of points to perform your mechanic on.</li>
 *     <li>Override the {@link #cast(SkillMetadata, List)} method to perform your mechanic on the transformed points.</li>
 * </ol>
 *
 * @author 0TickPulse
 */
public abstract class TransformableMechanic extends SkillMechanic implements ITargetedLocationSkill, ITargetedEntitySkill {

    /**
     * A list of fields that can be used via {@link ComponentWithFields#addFields(ComponentWithFields.ComponentField...)}
     */
    public static ComponentWithFields.ComponentField[] FIELDS = {
        new ComponentWithFields.ComponentField().addNames("xoffset", "xo", "ox", "xoff").setDescription("Additional offset in the X-axis.").setDefaultValue("0"),
        new ComponentWithFields.ComponentField().addNames("yoffset", "yo", "oy", "yoff").setDescription("Additional offset in the Y-axis.").setDefaultValue("0"),
        new ComponentWithFields.ComponentField().addNames("zoffset", "zo", "oz", "zoff").setDescription("Additional offset in the Z-axis.").setDefaultValue("0"),
        new ComponentWithFields.ComponentField().addNames("forwardoffset", "fo", "of", "foff").setDescription("Additional forward offset. This is based on the caster's yaw and pitch.").setDefaultValue("0"),
        new ComponentWithFields.ComponentField().addNames("rightoffset", "ro", "or", "roff").setDescription("Additional right offset. This is based on the caster's yaw and pitch. This is equivalent to `forwardoffset` but with pitch set to 0 and yaw rotated by -90.").setDefaultValue("0"),
        new ComponentWithFields.ComponentField().addNames("verticalOffset", "vo", "ov", "voff").setDescription("Additional vertical offset. This is based on the caster's yaw and pitch. This is equivalent to `forwardoffset` but with pitch rotated by +90.").setDefaultValue("0"),
        new ComponentWithFields.ComponentField().addNames("scale").setDescription("This mechanic takes the points and calculates a center point. Then, for each of the points, it calculates a vector from the center to that point. The size field simply multiplies this vector.").setDefaultValue("1"),
        new ComponentWithFields.ComponentField().addNames("rotation", "rot").setDescription("The rotation of the slash in degrees.").setDefaultValue("0"),
        new ComponentWithFields.ComponentField().addNames("radians", "rad", "useradians", "ur").setDescription("Whether to use radians instead of degrees for the rotation.").setDefaultValue("false"),
        new ComponentWithFields.ComponentField().addNames("inferdirection", "inferdir", "id").setDescription("If set to true, instead of getting the direction from the target location, it gets the direction of an arbitrary vector from the caster to the target location.").setDefaultValue("false")
    };

    protected final PlaceholderDouble xOffset;
    protected final PlaceholderDouble yOffset;
    protected final PlaceholderDouble zOffset;
    protected final PlaceholderDouble forwardOffset;
    protected final PlaceholderDouble rightOffset;
    protected final PlaceholderDouble verticalOffset;
    protected final PlaceholderDouble scale;
    protected final PlaceholderDouble rotation;
    protected final boolean radians;
    protected final boolean inferDirection;

    public TransformableMechanic(SkillExecutor manager, File file, String line, MythicLineConfig mlc) {
        super(manager, file, line, mlc);
        this.xOffset = mlc.getPlaceholderDouble(new String[]{"xoffset", "xo", "ox", "xoff"}, 0);
        this.yOffset = mlc.getPlaceholderDouble(new String[]{"yoffset", "yo", "oy", "yoff"}, 0);
        this.zOffset = mlc.getPlaceholderDouble(new String[]{"zoffset", "zo", "oz", "zoff"}, 0);
        this.forwardOffset = mlc.getPlaceholderDouble(new String[]{"forwardoffset", "fo", "of", "foff"}, 0);
        this.rightOffset = mlc.getPlaceholderDouble(new String[]{"rightoffset", "ro", "or", "roff"}, 0);
        this.verticalOffset = mlc.getPlaceholderDouble(new String[]{"verticaloffset", "vo", "ov", "voff"}, 0);
        this.scale = mlc.getPlaceholderDouble(new String[]{"scale", "s"}, 1);
        this.rotation = mlc.getPlaceholderDouble(new String[]{"rotation", "rot"}, 0);
        this.radians = mlc.getBoolean(new String[]{"radians", "rad", "useradians", "ur"}, false);
        this.inferDirection = mlc.getBoolean(new String[]{"inferdirection", "inferdir", "id"}, false);
    }

    /**
     * Converts a specified angle to radians. If {@link #radians} is true, this method simply returns the angle.
     * This method should be used before using any functions involving angle fields (such as {@link #rotation}).
     *
     * @param angle The angle to convert.
     */
    public double toRadians(double angle) {
        return radians ? angle : Math.toRadians(angle);
    }

    /**
     * A method that will be called before the {@link #cast(SkillMetadata, List)} method.
     *
     * @param skillMetadata The metadata of the skill.
     * @param locations     The list of locations to transform.
     */
    protected List<Location> transform(SkillMetadata skillMetadata, List<Location> locations) {
        double averageX = locations.stream().map(Location::getX).reduce(0.0, Double::sum) / locations.size();
        double averageY = locations.stream().map(Location::getY).reduce(0.0, Double::sum) / locations.size();
        double averageZ = locations.stream().map(Location::getZ).reduce(0.0, Double::sum) / locations.size();
        float averageYaw = locations.stream().map(Location::getYaw).reduce(0.0F, Float::sum) / locations.size();
        float averagePitch = locations.stream().map(Location::getPitch).reduce(0.0F, Float::sum) / locations.size();
        Location center = new Location(locations.get(0).getWorld(), averageX, averageY, averageZ, averageYaw, averagePitch);
        Location casterLocation = BukkitAdapter.adapt(skillMetadata.getCaster().getLocation());
        return locations.stream().map(location -> {
            // add offsets
            location.add(xOffset.get(), yOffset.get(), zOffset.get());
            // add relative offsets
            location = LocationUtilities.relativeOffset(
                location.clone().setDirection(
                    casterLocation.getDirection()
                ),
                forwardOffset.get(),
                rightOffset.get(),
                verticalOffset.get()
            );
            // rotations
            Vector casterToTarget = location.toVector().subtract(casterLocation.toVector());
            casterToTarget = casterToTarget.rotateAroundAxis(casterLocation.getDirection().normalize(), toRadians(rotation.get()));
            location = casterLocation.clone().add(casterToTarget);
            // size transformation
            Vector vector = location.clone().subtract(center).toVector();
            Location finalLocation = center.clone().add(vector.multiply(scale.get()));
            if (inferDirection) {
                finalLocation.setDirection(casterToTarget);
            }
            return finalLocation;
        }).toList();
    }

    /**
     * This method should return a list of locations based on the targeted location.
     *
     * @param skillMetadata The metadata of the skill.
     * @param target        The targeted location.
     */
    public abstract List<Location> getPoints(SkillMetadata skillMetadata, Location target);

    public abstract SkillResult cast(SkillMetadata skillMetadata, List<Location> locations);

    @Override
    public SkillResult castAtLocation(SkillMetadata skillMetadata, AbstractLocation location) {
        return this.cast(skillMetadata, this.transform(skillMetadata, this.getPoints(skillMetadata, BukkitAdapter.adapt(location))));
    }

    @Override
    public SkillResult castAtEntity(SkillMetadata skillMetadata, AbstractEntity entity) {
        return this.cast(skillMetadata, this.transform(skillMetadata, this.getPoints(skillMetadata, BukkitAdapter.adapt(entity.getLocation()))));
    }
}


