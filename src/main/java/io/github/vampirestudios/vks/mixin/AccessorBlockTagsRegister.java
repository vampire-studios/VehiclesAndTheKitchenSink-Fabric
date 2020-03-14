package io.github.vampirestudios.vks.mixin;

import net.minecraft.block.Block;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockTags.class)
public interface AccessorBlockTagsRegister {
    @SuppressWarnings("PublicStaticMixinMember")
    @Invoker("register")
    static Tag<Block> register(String id) {
        throw new RuntimeException("f");
    }
}