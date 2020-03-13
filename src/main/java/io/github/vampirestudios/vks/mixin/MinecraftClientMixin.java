package io.github.vampirestudios.vks.mixin;

import io.github.vampirestudios.vks.ForgeHooks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.Objects;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow @Nullable public HitResult crosshairTarget;
    @Shadow @Nullable public ClientPlayerEntity player;
    @Shadow @Nullable public ClientWorld world;
    /**
     * @author Olivia
     */
    @Overwrite
    private void doItemPick() {
        if (this.crosshairTarget != null && this.crosshairTarget.getType() != HitResult.Type.MISS) {
            ForgeHooks.onPickBlock(this.crosshairTarget, Objects.requireNonNull(this.player), this.world);
        }
    }

}