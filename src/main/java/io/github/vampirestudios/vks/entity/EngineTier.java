package io.github.vampirestudios.vks.entity;

import net.minecraft.util.Formatting;

/**
 * Author: MrCrayfish
 */
public enum EngineTier
{
    WOOD(0.75F, -2F, 0F, "wood", Formatting.WHITE),
    STONE(1.0F, 0F, 0F, "stone", Formatting.DARK_GRAY),
    IRON(1.25F, 1F, 0F, "iron", Formatting.GRAY),
    GOLD(1.5F, 3F, 0F, "gold", Formatting.GOLD),
    DIAMOND(1.1F, 6F, 0F, "diamond", Formatting.AQUA);

    float accelerationMultiplier;
    float additionalMaxSpeed;
    float fuelConsumption;
    String tierName;
    Formatting tierColor;

    EngineTier(float accelerationMultiplier, float additionalMaxSpeed, float fuelConsumption, String tierName, Formatting tierColor)
    {
        this.accelerationMultiplier = accelerationMultiplier;
        this.additionalMaxSpeed = additionalMaxSpeed;
        this.fuelConsumption = fuelConsumption;
        this.tierName = tierName;
        this.tierColor = tierColor;
    }

    public float getAccelerationMultiplier()
    {
        return accelerationMultiplier;
    }

    public float getAdditionalMaxSpeed()
    {
        return additionalMaxSpeed;
    }

    public float getFuelConsumption()
    {
        return fuelConsumption;
    }

    public String getTierName()
    {
        return tierName;
    }

    public Formatting getTierColor()
    {
        return tierColor;
    }

    public static EngineTier getType(int index)
    {
        if(index < 0 || index >= values().length)
        {
            return WOOD;
        }
        return EngineTier.values()[index];
    }
}