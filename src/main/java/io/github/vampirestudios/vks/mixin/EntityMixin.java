package io.github.vampirestudios.vks.mixin;

import io.github.vampirestudios.vks.IForgeEntity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public class EntityMixin implements IForgeEntity {

}
