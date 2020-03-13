package io.github.vampirestudios.vks;

import net.minecraft.util.Identifier;

import java.util.Set;

public interface IModelLoaderAdditions {

    default Set<Identifier> getSpecialModels() {
        return java.util.Collections.emptySet();
    }

}