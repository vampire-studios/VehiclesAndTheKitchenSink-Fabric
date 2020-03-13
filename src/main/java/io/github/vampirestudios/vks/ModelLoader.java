package io.github.vampirestudios.vks;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.util.HashSet;
import java.util.Set;

public class ModelLoader extends net.minecraft.client.render.model.ModelLoader implements IModelLoaderAdditions {
    private static Set<Identifier> specialModels = new HashSet<>();

    public ModelLoader(ResourceManager resourceManager, BlockColors blockColors, Profiler profiler, int i) {
        super(resourceManager, blockColors, profiler, i);
    }

    /**
     * Indicate to vanilla that it should load and bake the given model, even if no blocks or
     * items use it. This is useful if e.g. you have baked models only for entity renderers.
     *
     * @param rl The model, either {@link net.minecraft.client.util.ModelIdentifier} to point to a blockstate variant,
     *           or plain {@link Identifier} to point directly to a json in the models folder.
     */
    public static void addSpecialModel(Identifier rl) {
        specialModels.add(rl);
    }

    @Override
    public Set<Identifier> getSpecialModels() {
        return specialModels;
    }

}