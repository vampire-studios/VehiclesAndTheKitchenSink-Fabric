package io.github.vampirestudios.vks.utils;

import io.github.vampirestudios.vampirelib.utils.registry.EntityRegistryBuilder;
import io.github.vampirestudios.vks.block.BlockVehicleCrate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.function.Function;

/**
 * Author: MrCrayfish
 */
public class EntityUtil {

    public static <T extends Entity> EntityType<T> buildVehicleType(String id, Function<World, T> function, float width, float height) {
        EntityType<T> type = EntityRegistryBuilder.<T>createBuilder(new Identifier(id))
                .entity((entityType, world) -> function.apply(world))
                .category(EntityCategory.MISC)
                .dimensions(EntityDimensions.fixed(width, height))
                .tracker(256, 1, true)
                .hasEgg(false)
                .build();
        BlockVehicleCrate.registerVehicle(id);
        return type;
    }

}