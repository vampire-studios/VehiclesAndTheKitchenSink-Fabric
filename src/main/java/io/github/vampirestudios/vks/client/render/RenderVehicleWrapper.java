package io.github.vampirestudios.vks.client.render;

import io.github.vampirestudios.vks.common.entity.PartPosition;
import io.github.vampirestudios.vks.entity.PoweredVehicleEntity;
import io.github.vampirestudios.vks.entity.VehicleEntity;
import io.github.vampirestudios.vks.entity.VehicleProperties;
import io.github.vampirestudios.vks.utils.RenderUtil;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public class RenderVehicleWrapper<T extends VehicleEntity/* & EntityRaytracer.IEntityRaytraceable*/, R extends AbstractRenderVehicle<T>>
{
    protected final R renderVehicle;

    public RenderVehicleWrapper(R renderVehicle)
    {
        this.renderVehicle = renderVehicle;
    }

    public R getRenderVehicle()
    {
        return renderVehicle;
    }

    public void render(T entity, MatrixStack matrixStack, VertexConsumerProvider renderTypeBuffer, float partialTicks, int light)
    {
        if(!entity.isAlive())
            return;

        matrixStack.push();

        VehicleProperties properties = entity.getProperties();
        PartPosition bodyPosition = properties.getBodyPosition();
        matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion((float) bodyPosition.getRotX()));
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion((float) bodyPosition.getRotY()));
        matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion((float) bodyPosition.getRotZ()));

        if(entity.canTowTrailer())
        {
            matrixStack.push();
            matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180F));
            Vec3d towBarOffset = properties.getTowBarPosition();
            matrixStack.translate(towBarOffset.x * 0.0625, towBarOffset.y * 0.0625 + 0.5, -towBarOffset.z * 0.0625);
            RenderUtil.renderColoredModel(SpecialModels.TOW_BAR.getModel(), ModelTransformation.Mode.NONE, false, matrixStack, renderTypeBuffer, -1, light, OverlayTexture.DEFAULT_UV);
            matrixStack.pop();
        }

        matrixStack.translate(bodyPosition.getX(), bodyPosition.getY(), bodyPosition.getZ());
        matrixStack.scale((float) bodyPosition.getScale(), (float) bodyPosition.getScale(), (float) bodyPosition.getScale());
        matrixStack.translate(0.0, 0.5, 0.0);
        matrixStack.translate(0.0, properties.getAxleOffset() * 0.0625, 0.0);
        matrixStack.translate(0.0, properties.getWheelOffset() * 0.0625, 0.0);
        this.renderVehicle.render(entity, matrixStack, renderTypeBuffer, partialTicks, light);

        matrixStack.pop();
    }

    /**
     *
     * @param entity
     * @param partialTicks
     */
    public void applyPreRotations(T entity, MatrixStack stack, float partialTicks) {}

    /**
     * Renders a part (ItemStack) on the vehicle using the specified PartPosition. The rendering
     * will be cancelled if the PartPosition parameter is null.
     *
     * @param position the render definitions to apply to the part
     * @param model the part to render onto the vehicle
     */
    protected void renderPart(@Nullable PartPosition position, BakedModel model, MatrixStack matrixStack, VertexConsumerProvider buffer, int color, int lightTexture, int overlayTexture)
    {
        if(position == null)
            return;

        matrixStack.push();
        matrixStack.translate(position.getX() * 0.0625, position.getY() * 0.0625, position.getZ() * 0.0625);
        matrixStack.translate(0.0, -0.5, 0.0);
        matrixStack.scale((float) position.getScale(), (float) position.getScale(), (float) position.getScale());
        matrixStack.translate(0.0, 0.5, 0.0);
        matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion((float) position.getRotX()));
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion((float) position.getRotY()));
        matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion((float) position.getRotZ()));
        RenderUtil.renderColoredModel(model, ModelTransformation.Mode.NONE, false, matrixStack, buffer, color, lightTexture, overlayTexture);
        matrixStack.pop();
    }

    protected void renderKey(@Nullable PartPosition position, BakedModel model, MatrixStack matrixStack, VertexConsumerProvider buffer, int color, int lightTexture, int overlayTexture)
    {
        if(position == null)
            return;

        matrixStack.push();
        matrixStack.translate(position.getX() * 0.0625, position.getY() * 0.0625, position.getZ() * 0.0625);
        matrixStack.translate(0.0, -0.25, 0.0);
        matrixStack.scale((float) position.getScale(), (float) position.getScale(), (float) position.getScale());
        matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion((float) position.getRotX()));
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion((float) position.getRotY()));
        matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion((float) position.getRotZ()));
        matrixStack.translate(0.0, 0.0, -0.05);
        RenderUtil.renderColoredModel(model, ModelTransformation.Mode.NONE, false, matrixStack, buffer, color, lightTexture, overlayTexture);
        matrixStack.pop();
    }


    /**
     * Renders the engine (ItemStack) on the vehicle using the specified PartPosition. It adds a
     * subtle shake to the render to simulate it being powered.
     *
     * @param position the render definitions to apply to the part
     */
    protected void renderEngine(PoweredVehicleEntity entity, @Nullable PartPosition position, BakedModel model, MatrixStack matrixStack, VertexConsumerProvider buffer, int light)
    {
        matrixStack.push();
        if(entity.isFueled() && entity.getControllingPassenger() != null)
        {
            matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(0.5F * (entity.age % 2)));
            matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(0.5F * (entity.age % 2)));
            matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-0.5F * (entity.age % 2)));
        }
        this.renderPart(position, model, matrixStack, buffer, -1, light, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();
    }
}