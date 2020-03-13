package io.github.vampirestudios.vks.client.render;

import io.github.vampirestudios.vks.entity.EntityJack;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

/**
 * Author: MrCrayfish
 */
public class JackRenderer extends EntityRenderer<EntityJack> {
    public JackRenderer(EntityRenderDispatcher renderManager)
    {
        super(renderManager);
    }

    @Override
    public Identifier getTexture(EntityJack entity)
    {
        return null;
    }

    @Override
    public void render(EntityJack jack, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {}
}