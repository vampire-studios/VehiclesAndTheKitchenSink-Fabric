package io.github.vampirestudios.vks.mixin;

import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.ModelIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ModelLoader.class)
public interface ModelLoaderAddModelAccessor {

    @SuppressWarnings("PublicStaticMixinMember")
    @Invoker("addModel(Lnet/minecraft/client/util/ModelIdentifier;)V")
    static void addModel(ModelIdentifier id) {
        throw new RuntimeException("f");
    }

}
