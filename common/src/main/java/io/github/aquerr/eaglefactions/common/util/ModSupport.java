package io.github.aquerr.eaglefactions.common.util;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;

public class ModSupport
{
    /**
     * Checks if the given class comes from TechGuns
     * @param clazz class to check
     * @param <T> the type of class
     * @return <tt>true</tt> if class comes from TechGuns, <tt>false</tt> if not.
     */
    public static <T> boolean isTechGuns(final Class<T> clazz)
    {
        return clazz.getName().contains("techguns");
    }

    /**
     * Checks if the given class comes from AncientWarfare
     * @param clazz class to check
     * @param <T> the type of class
     * @return <tt>true</tt> if class comes from AncientWarfare, <tt>false</tt> if not.
     */
    public static <T> boolean isAncientWarfare(final Class<T> clazz)
    {
        return clazz.getName().contains("ancientwarfare");
    }

    /**
     * Gets the attacking entity from the EntityDamageSource. EntityDamageSource must come from TechGuns.
     * @param entityDamageSource the source
     * @return attacking/source entity.
     */
    public static Entity getAttackerFromTechGuns(final EntityDamageSource entityDamageSource)
    {
        Object attacker = null;
        try
        {
            attacker = entityDamageSource.getClass().getField("attacker").get(entityDamageSource);
        }
        catch (IllegalAccessException | NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        if (attacker instanceof Entity)
            return (Entity)attacker;
        else return null;
    }

    /**
     * Checks if the given class comes from Mekanism
     * @param clazz class to check
     * @param <T> the type of class
     * @return <tt>true</tt> if class comes from Mekanism, <tt>false</tt> if not.
     */
    public static <T> boolean isMekenism(Class<T> clazz)
    {
        return clazz.getName().contains("mekanism");
    }

    /**
     * Gets the entity owner from the Entity object. Entity must come from Mekanism.
     * @param entity the entity
     * @return attacking/source entity.
     */
    public static Entity getEntityOwnerFromMekanism(final Entity entity)
    {
        Object owner = null;
        try
        {
            owner = entity.getClass().getField("owner").get(entity);
        }
        catch (IllegalAccessException | NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        if (owner instanceof Entity)
            return (Entity)owner;
        else return null;
    }

    /**
     * Checks if the given entity is IC2's mining laser.
     * @param entity the entity
     * @return <tt>true</tt> if the entity is mining laser, <tt>false</tt> if not.
     */
    public static boolean isIndustrialCraftMiningLaser(Entity entity)
    {
        return entity.getType().getId().contains("mininglaser");
    }
}