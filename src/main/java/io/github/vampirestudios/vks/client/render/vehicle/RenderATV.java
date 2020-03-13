package io.github.vampirestudios.vks.client.render.vehicle;

import io.github.vampirestudios.vks.client.render.AbstractRenderVehicle;
import io.github.vampirestudios.vks.client.render.SpecialModels;
import io.github.vampirestudios.vks.entity.vehicles.ATVEntity;
import io.github.vampirestudios.vks.utils.RenderUtil;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Author: MrCrayfish
 */
public class RenderATV extends AbstractRenderVehicle<ATVEntity> {

    /*@Override
    protected boolean shouldRenderFuelLid()
    {
        return false;
    }*/

    @Override
    public void render(ATVEntity entity, MatrixStack matrixStack, VertexConsumerProvider renderTypeBuffer, float partialTicks, int light) {
        //Body
        this.renderDamagedPart(entity, SpecialModels.ATV_BODY.getModel(), matrixStack, renderTypeBuffer, light);

        //Handle bar transformations
        matrixStack.push();
        matrixStack.translate(0.0, 0.3375, 0.25);
        matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-45F));
        matrixStack.translate(0.0, -0.025, 0);

        float wheelAngle = entity.prevRenderWheelAngle + (entity.renderWheelAngle - entity.prevRenderWheelAngle) * partialTicks;
        float wheelAngleNormal = wheelAngle / 45F;
        float turnRotation = wheelAngleNormal * 15F;
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(turnRotation));

        RenderUtil.renderColoredModel(SpecialModels.ATV_HANDLES.getModel(), ModelTransformation.Mode.NONE, false, matrixStack, renderTypeBuffer, entity.getColor(), light, OverlayTexture.DEFAULT_UV);

        matrixStack.pop();
    }

    @Override
    public void applyPlayerModel(ATVEntity entity, PlayerEntity player, PlayerEntityModel<AbstractClientPlayerEntity> model, float partialTicks) {
        float wheelAngle = entity.prevRenderWheelAngle + (entity.renderWheelAngle - entity.prevRenderWheelAngle) * partialTicks;
        float wheelAngleNormal = wheelAngle / 45F;
        float turnRotation = wheelAngleNormal * 12F;
        model.rightArm.pivotX = (float) Math.toRadians(-65F - turnRotation);
        model.rightArm.pivotY = (float) Math.toRadians(15F);
        model.leftArm.pivotX = (float) Math.toRadians(-65F + turnRotation);
        model.leftArm.pivotY = (float) Math.toRadians(-15F);

        if(entity.getControllingPassenger() != player)
        {
            model.rightArm.pivotX = (float) Math.toRadians(-20F);
            model.rightArm.pivotY = (float) Math.toRadians(0F);
            model.rightArm.pivotZ = (float) Math.toRadians(15F);
            model.leftArm.pivotX = (float) Math.toRadians(-20F);
            model.leftArm.pivotY = (float) Math.toRadians(0F);
            model.leftArm.pivotZ = (float) Math.toRadians(-15F);
            model.rightLeg.pivotX = (float) Math.toRadians(-85F);
            model.rightLeg.pivotY = (float) Math.toRadians(30F);
            model.leftLeg.pivotX = (float) Math.toRadians(-85F);
            model.leftLeg.pivotY = (float) Math.toRadians(-30F);
            return;
        }

        model.rightLeg.pivotX = (float) Math.toRadians(-65F);
        model.rightLeg.pivotY = (float) Math.toRadians(30F);
        model.leftLeg.pivotX = (float) Math.toRadians(-65F);
        model.leftLeg.pivotY = (float) Math.toRadians(-30F);
    }
}