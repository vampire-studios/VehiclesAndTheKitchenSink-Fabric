package io.github.vampirestudios.vks.utils;

import io.github.vampirestudios.vks.block.BlockVehicleCrate;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.function.Function;

/**
 * Author: MrCrayfish
 */
public class EntityUtil {

    public static <T extends Entity> EntityType<T> buildVehicleType(String id, Function<World, T> function, float width, float height) {
        EntityType<T> type = Registry.register(Registry.ENTITY_TYPE, new Identifier(id),
                FabricEntityTypeBuilder.<T>create(EntityCategory.MISC, (entityType, world) -> function.apply(world))
                        .size(EntityDimensions.fixed(width, height)).trackable(256, 1, true)
                        .disableSummon().setImmuneToFire().build());
        BlockVehicleCrate.registerVehicle(id);
        return type;
    }

}