package io.github.vampirestudios.vks.client.render.vehicle;

import io.github.vampirestudios.vks.client.render.AbstractRenderVehicle;
import io.github.vampirestudios.vks.client.render.SpecialModels;
import io.github.vampirestudios.vks.entity.vehicles.DuneBuggyEntity;
import io.github.vampirestudios.vks.utils.RenderUtil;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Author: MrCrayfish
 */
public class RenderDuneBuggy extends AbstractRenderVehicle<DuneBuggyEntity> {

    @Override
    public void render(DuneBuggyEntity entity, MatrixStack matrixStack, VertexConsumerProvider renderTypeBuffer, float partialTicks, int light) {
        this.renderDamagedPart(entity, SpecialModels.DUNE_BUGGY_BODY.getModel(), matrixStack, renderTypeBuffer, light);

        float wheelAngle = entity.prevRenderWheelAngle + (entity.renderWheelAngle - entity.prevRenderWheelAngle) * partialTicks;
        double wheelScale = 1.0F;

        //Render the handles bars
        matrixStack.push();

        matrixStack.translate(0.0, 0.0, 3.125 * 0.0625);
        matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-22.5F));
        float wheelAngleNormal = wheelAngle / 45F;
        float turnRotation = wheelAngleNormal * 15F;
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(turnRotation));
        matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(22.5F));
        matrixStack.translate(0.0, 0.0, -0.2);

        this.renderDamagedPart(entity, SpecialModels.DUNE_BUGGY_HANDLES.getModel(), matrixStack, renderTypeBuffer, light);

        if(entity.hasWheels()) {
            matrixStack.push();
            matrixStack.translate(0.0, -0.355, 0.33);
            float frontWheelSpin = entity.prevFrontWheelRotation + (entity.frontWheelRotation - entity.prevFrontWheelRotation) * partialTicks;
            if(entity.isMoving()) {
                matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-frontWheelSpin));
            }
            matrixStack.scale((float) wheelScale, (float) wheelScale, (float) wheelScale);
            matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180F));
            BakedModel wheelModel = RenderUtil.getWheelModel(entity);
            RenderUtil.renderColoredModel(wheelModel, ModelTransformation.Mode.NONE, false, matrixStack, renderTypeBuffer, entity.getWheelColor(), light, OverlayTexture.DEFAULT_UV);
            matrixStack.pop();
        }

        matrixStack.pop();
    }

    @Override
    public void applyPlayerModel(DuneBuggyEntity entity, PlayerEntity player, PlayerEntityModel<AbstractClientPlayerEntity> model, float partialTicks)
    {
        float wheelAngle = entity.prevRenderWheelAngle + (entity.renderWheelAngle - entity.prevRenderWheelAngle) * partialTicks;
        float wheelAngleNormal = wheelAngle / 45F;
        float turnRotation = wheelAngleNormal * 8F;
        model.rightArm.pivotX = (float) Math.toRadians(-50F - turnRotation);
        model.leftArm.pivotX = (float) Math.toRadians(-50F + turnRotation);
        model.rightLeg.pivotX = (float) Math.toRadians(-65F);
        model.rightLeg.pivotY = (float) Math.toRadians(30F);
        model.leftLeg.pivotX = (float) Math.toRadians(-65F);
        model.leftLeg.pivotY = (float) Math.toRadians(-30F);
    }
}