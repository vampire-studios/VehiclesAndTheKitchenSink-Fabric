package io.github.vampirestudios.vks.client.render;

import io.github.vampirestudios.vks.entity.EntityJack;
import io.github.vampirestudios.vks.entity.VehicleEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

/**
 * Author: MrCrayfish
 */
public class RenderEntityVehicle<T extends VehicleEntity/* & EntityRaytracer.IEntityRaytraceable*/, R extends AbstractRenderVehicle<T>> extends EntityRenderer<T>
{
    private final RenderVehicleWrapper<T, R> wrapper;

    public RenderEntityVehicle(EntityRenderDispatcher renderManager, RenderVehicleWrapper<T, R> wrapper)
    {
        super(renderManager);
        this.wrapper = wrapper;
    }

    @Override
    public Identifier getTexture(T entity)
    {
        return null;
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, MatrixStack matrixStack, VertexConsumerProvider renderTypeBuffer, int light)
    {
        if(!entity.isAlive())
            return;

        if(entity.getVehicle() instanceof EntityJack)
            return;

        matrixStack.push();
        wrapper.applyPreRotations(entity, matrixStack, partialTicks);
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-entityYaw));
        this.setupBreakAnimation(entity, matrixStack, partialTicks);
        wrapper.render(entity, matrixStack, renderTypeBuffer, partialTicks, light);
        matrixStack.pop();

//        EntityRaytracer.renderRaytraceElements(entity, matrixStack, entityYaw);
    }

    private void setupBreakAnimation(VehicleEntity vehicle, MatrixStack matrixStack, float partialTicks)
    {
        float timeSinceHit = (float) vehicle.getTimeSinceHit() - partialTicks;
        if(timeSinceHit > 0.0F)
        {
            matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.sin(timeSinceHit) * timeSinceHit));
        }
    }
}