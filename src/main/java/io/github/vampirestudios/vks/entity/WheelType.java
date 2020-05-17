package io.github.vampirestudios.vks.entity;

/**
 * Author: MrCrayfish
 */
public enum WheelType {
    STANDARD("standard", 0.9F, 0.8F, 0.5F),
    SPORTS("sports", 1.0F, 0.75F, 0.5F),
    RACING("racing", 1.1F, 0.7F, 0.5F),
    OFF_ROAD("off_road", 0.75F, 1.0F, 0.85F),
    SNOW("snow", 0.75F, 0.75F, 0.95F),
    ALL_TERRAIN("all_terrain", 0.85F, 0.85F, 0.85F),
    PLASTIC("plastic", 0.5F, 0.5F, 0.5F),
    STANDARD_SAND_BUS("standard_sand_bus", 0.5F, 0.8F, 0.2F);

    String id;
    float roadMultiplier;
    float dirtMultiplier;
    float snowMultiplier;

    WheelType(String id, float roadMultiplier, float dirtMultiplier, float snowMultiplier)
    {
        this.id = id;
        this.roadMultiplier = roadMultiplier;
        this.dirtMultiplier = dirtMultiplier;
        this.snowMultiplier = snowMultiplier;
    }

    public String getId()
    {
        return id;
    }

    public float getRoadMultiplier()
    {
        return roadMultiplier;
    }

    public float getDirtMultiplier()
    {
        return dirtMultiplier;
    }

    public float getSnowMultiplier()
    {
        return snowMultiplier;
    }

    /*public void applyPhysics(EntityPoweredVehicle vehicle) {}*/

    public static WheelType getType(String name) {
        switch (name) {
            case "standard":
                return STANDARD;
            case "sports":
                return SPORTS;
            case "racing":
                return RACING;
            case "off_road":
                return OFF_ROAD;
            case "snow":
                return SNOW;
            case "all_terrain":
                return ALL_TERRAIN;
            case "plastic":
                return PLASTIC;
            case "standard_sand_bus":
                return STANDARD_SAND_BUS;
        }
        return STANDARD;
    }

}