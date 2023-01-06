# LccUtils - Auto-Generated Documentation

## Category: Mythic Mechanic

### Mythic Mechanic: `slash`

Aliases: `runslash`

Performs a slash. This slash is executed by calculating points along a circle.
This circle is calculated by fields like `points` and `radius`.
When used effectively, this mechanic can simulate a weapon's slash.
This mechanic also provides entity targeting and allows you to specify actions performed on targeted entities through the `onhitskill` field.
However, if you want more lenient targeting, you can use the `@EntitiesInCone` targeter provided by Mythic.

#### Examples

```yaml
SlashTest:
  Skills:
    - slash{onpointskill=SlashTestTick;points=80;r=5;rot=<random.1to180>} @forward{f=0;uel=true}

SlashTestTick:
  Skills:
    - e:p{p=flame} @Origin
```

Author(s): 0TickPulse

### Fields

* `xoffset`, `xo`, `ox`, `xoff` - Additional offset in the X-axis.
  Default value: `0`
* `yoffset`, `yo`, `oy`, `yoff` - Additional offset in the Y-axis.
  Default value: `0`
* `zoffset`, `zo`, `oz`, `zoff` - Additional offset in the Z-axis.
  Default value: `0`
* `forwardoffset`, `fo`, `of`, `foff` - Additional forward offset. This is based on the caster's yaw and pitch.
  Default value: `0`
* `rightoffset`, `ro`, `or`, `roff` - Additional right offset. This is based on the caster's yaw and pitch. This is equivalent to `forwardoffset` but with pitch set to 0 and yaw rotated by -90.
  Default value: `0`
* `verticalOffset`, `vo`, `ov`, `voff` - Additional vertical offset. This is based on the caster's yaw and pitch. This is equivalent to `forwardoffset` but with pitch rotated by +90.
  Default value: `0`
* `scale` - This mechanic takes the points and calculates a center point. Then, for each of the points, it calculates a vector from the center to that point. The size field simply multiplies this vector.
  Default value: `1`
* `rotation`, `rot` - The rotation of the slash in degrees.
  Default value: `0`
* `radians`, `rad`, `useradians`, `ur` - Whether to use radians instead of degrees for the rotation.
  Default value: `false`
* `inferdirection`, `inferdir`, `id` - If set to true, instead of getting the direction from the target location, it gets the direction of an arbitrary vector from the caster to the target location.
  Default value: `false`
* `onpointskill`, `onpoint`, `op` - The skill to perform for every point in the slash.
* `onhitskill`, `onhit`, `oh` - The skill to perform for every entity hit by the slash.
* `radius`, `r` - The radius of the slash.
  Default value: `2`
* `points`, `p` - The number of points in the slash.
  Default value: `5`
* `arc`, `a` - The arc of the slash.
  Default value: `180`
* `interval`, `i` - The interval between each iteration in the slash.
  Default value: `0`
* `iterationCount`, `count`, `ic`, `c` - The number of points each iteration will have.
  Default value: `1`
* `lineDistance`, `ld` - When slashing, sometimes the target entities are in between the caster and the points of the slash, causing the entity not to be hit. In order to circumvent this, the slash mechanic also takes points in between each slash point and checks for entities there. This is the distance between each line point. Set to 0 to disable.
  Default value: `0`
* `hitRadius`, `hr` - Each point in the slash checks for entities within a certain radius to determine if the entity was hit. This is the horizontal radius of each point.
  Default value: `0.2`
* `verticalHitRadius`, `vhr`, `vr` - Each point in the slash checks for entities within a certain radius to determine if the entity was hit. This is the vertical radius of each point.
  Default value: `(The hitRadius field)`

## Category: Mythic Placeholder

### Mythic Placeholder: `[caster/target/trigger/parent].standing_on`

Returns the title of the block the entity is standing on.

Author(s): 0TickPulse

### Mythic Placeholder: `[caster/target/trigger/parent].attribute`

Returns the value of the specified attribute of the entity. Attributes: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/attribute/Attribute.html

Author(s): 0TickPulse

### Mythic Placeholder: `[caster/target/trigger/parent].mcmmo.party`

Returns the McMMO party of the player.

#### See also

* [Mythic Condition: `sameparty`](#mythic-condition-sameparty)

Author(s): 0TickPulse

### Mythic Placeholder: `target.worldguard.flag.<flag>`

Returns the value of the specified WorldGuard flag at the target location.

Author(s): 0TickPulse

### Mythic Placeholder: `[caster/target/trigger/parent].worldguard.flag`

Returns the value of the specified WorldGuard flag at the target entity's location.

Author(s): 0TickPulse

## Category: Bridge

### Bridge: `McMMO`

LCCUtils has compatibility with McMMO. This bridge enables certain things like the [Mythic Placeholder: `[caster/target/trigger/parent].mcmmo.party`](#mythic-placeholder-castertargettriggerparentmcmmoparty).

Author(s): 0TickPulse

### Bridge: `WorldGuard`

Adds several things that hooks into WorldGuard.

Author(s): 0TickPulse

### Bridge: `PlaceholderAPI`

This plugin adds a 'mythic' placeholder to PlaceholderAPI.

Author(s): 0TickPulse

## Category: Minecraft Command

### Minecraft Command: `mmrun`

Aliases: `runmm`, `mythicrun`, `runmythic`, `mythicmobsrun`, `runmythicmobs`, `mythicmobrun`, `runmythicmob`, `mmtest`, `testmm`, `mythictest`, `testmythic`, `mythicmobstest`, `testmythicmobs`, `mythicmobtest`, `testmythicmob`

Runs a MythicMobs skill line.

Author(s): 0TickPulse

### Minecraft Command: `lccutils`

Aliases: `lccu`, `lccutilities`, `lccutil`, `lccmm`, `lccmythicmobs`, `lccmythic`, `lcc`

The main command for LccUtils.

#### Examples

```yaml
/lccutils component command slash
```

Author(s): 0TickPulse

## Category: PAPI Placeholder

### PAPI Placeholder: `mythic`

Parses a mythic expression and return the result. To get the player, use 'caster'.

Author(s): 0TickPulse

## Category: Mythic Condition

### Mythic Condition: `canattack`

Aliases: `canattackentity`, `candamage`, `candamageentity`

Checks if an entity can attack another entity by simulating a damage event.

Author(s): 0TickPulse

### Fields

* `cause`, `damagecause`, `c` - The cause of the damage event.
  Default value: `CUSTOM`

### Mythic Condition: `sameparty`

Aliases: `samemcmmoparty`, `partymatches`, `mcmmopartymatches`

Checks if the player is in the same McMMO party as the target.

#### See also

* [Mythic Placeholder: `[caster/target/trigger/parent].mcmmo.party`](#mythic-placeholder-castertargettriggerparentmcmmoparty)

Author(s): 0TickPulse

### Fields

## Category: Miscellaneous

### Miscellaneous: `Worldguard custom flags`

Allows the registration of custom user-defined WorldGuard flags. These are dummy flags that do not actually do anything, but can be used in WorldGuard flag checks.

Author(s): 0TickPulse

