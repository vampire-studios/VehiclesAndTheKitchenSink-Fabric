package io.github.vampirestudios.vks.mixin;

import io.github.vampirestudios.vks.IModelLoaderAdditions;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin implements IModelLoaderAdditions {

    @Shadow @Final private Map<Identifier, UnbakedModel> unbakedModels;
    @Shadow @Final private Map<Identifier, UnbakedModel> modelsToBake;

    @Shadow public abstract UnbakedModel getOrLoadModel(Identifier id);

    @Inject(method = "<init>", at=@At("RETURN"))
    public void onInit(CallbackInfo callbackInfo) {
        for (Identifier rl : getSpecialModels()) {
            UnbakedModel iunbakedmodel = this.getOrLoadModel(rl);
            this.unbakedModels.put(rl, iunbakedmodel);
            this.modelsToBake.put(rl, iunbakedmodel);
        }
    }



}