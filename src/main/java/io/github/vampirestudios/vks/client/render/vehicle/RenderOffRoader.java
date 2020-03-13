package io.github.vampirestudios.vks.client.render.vehicle;

import io.github.vampirestudios.vks.client.render.AbstractRenderVehicle;
import io.github.vampirestudios.vks.client.render.SpecialModels;
import io.github.vampirestudios.vks.entity.vehicles.OffRoaderEntity;
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
public class RenderOffRoader extends AbstractRenderVehicle<OffRoaderEntity> {
    
    @Override
    public void render(OffRoaderEntity entity, MatrixStack matrixStack, VertexConsumerProvider renderTypeBuffer, float partialTicks, int light) {
        this.renderDamagedPart(entity, SpecialModels.OFF_ROADER_BODY.getModel(), matrixStack, renderTypeBuffer, light);

        //Render the handles bars
        matrixStack.push();
            // Positions the steering wheel in the correct position
        matrixStack.translate(-0.3125, 0.35, 0.2);
        matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-45F));
        matrixStack.translate(0, -0.02, 0);
        matrixStack.scale(0.75F, 0.75F, 0.75F);

        // Rotates the steering wheel based on the wheel angle
        float wheelAngle = entity.prevRenderWheelAngle + (entity.renderWheelAngle - entity.prevRenderWheelAngle) * partialTicks;
        float wheelAngleNormal = wheelAngle / 45F;
        float turnRotation = wheelAngleNormal * 25F;
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(turnRotation));

        RenderUtil.renderColoredModel(SpecialModels.GO_KART_STEERING_WHEEL.getModel(), ModelTransformation.Mode.NONE, false, matrixStack, renderTypeBuffer, -1, light, OverlayTexture.DEFAULT_UV);

        matrixStack.pop();
    }

    @Override
    public void applyPlayerModel(OffRoaderEntity entity, PlayerEntity player, PlayerEntityModel<AbstractClientPlayerEntity> model, float partialTicks) {
        int index = entity.getSeatTracker().getSeatIndex(player.getUuid());
        if(index < 2) //Sitting in the front
        {
            model.rightLeg.pivotX = (float) Math.toRadians(-80F);
            model.rightLeg.pivotY = (float) Math.toRadians(15F);
            model.leftLeg.pivotX = (float) Math.toRadians(-80F);
            model.leftLeg.pivotY = (float) Math.toRadians(-15F);

            if(index == 1) {
                model.leftArm.pivotX = (float) Math.toRadians(-75F);
                model.leftArm.pivotY = (float) Math.toRadians(-25F);
                model.leftArm.pivotZ = 0F;
            }
        }
        else
        {
            if(index == 3)
            {
                model.rightLeg.pivotX = (float) Math.toRadians(-90F);
                model.rightLeg.pivotY = (float) Math.toRadians(15F);
                model.leftLeg.pivotX = (float) Math.toRadians(-90F);
                model.leftLeg.pivotY = (float) Math.toRadians(-15F);
                model.rightArm.pivotX = (float) Math.toRadians(-75F);
                model.rightArm.pivotY = (float) Math.toRadians(110F);
                model.rightArm.pivotZ = (float) Math.toRadians(0F);
                model.leftArm.pivotX = (float) Math.toRadians(-105F);
                model.leftArm.pivotY = (float) Math.toRadians(-20F);
                model.leftArm.pivotZ = 0F;
            }
            else
            {
                model.rightLeg.pivotX = (float) Math.toRadians(0F);
                model.rightLeg.pivotY = (float) Math.toRadians(0F);
                model.leftLeg.pivotX = (float) Math.toRadians(0F);
                model.leftLeg.pivotY = (float) Math.toRadians(0F);
                model.rightArm.pivotX = (float) Math.toRadians(-10F);
                model.rightArm.pivotZ = (float) Math.toRadians(25F);
                model.leftArm.pivotX = (float) Math.toRadians(-80F);
                model.leftArm.pivotZ = 0F;
                model.leftLeg.pivotX = (float) Math.toRadians(-20F);
                model.rightLeg.pivotX = (float) Math.toRadians(20F);
            }
        }

        if(entity.getControllingPassenger() == player)
        {
            float wheelAngle = entity.prevRenderWheelAngle + (entity.renderWheelAngle - entity.prevRenderWheelAngle) * partialTicks;
            float wheelAngleNormal = wheelAngle / 45F;
            float turnRotation = wheelAngleNormal * 6F;
            model.rightArm.pivotX = (float) Math.toRadians(-65F - turnRotation);
            model.rightArm.pivotY = (float) Math.toRadians(-7F);
            model.leftArm.pivotX = (float) Math.toRadians(-65F + turnRotation);
            model.leftArm.pivotY = (float) Math.toRadians(7F);
        }
    }
}