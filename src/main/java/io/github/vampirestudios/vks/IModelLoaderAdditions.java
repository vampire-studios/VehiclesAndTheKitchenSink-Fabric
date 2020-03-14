package io.github.vampirestudios.vks;

import net.minecraft.client.util.ModelIdentifier;

import java.util.Set;

public interface IModelLoaderAdditions {

    default Set<ModelIdentifier> getSpecialModels() {
        return java.util.Collections.emptySet();
    }

}