package io.github.vampirestudios.vks.client.render.vehicle;

import io.github.vampirestudios.vks.client.render.AbstractRenderVehicle;
import io.github.vampirestudios.vks.client.render.SpecialModels;
import io.github.vampirestudios.vks.entity.vehicles.GolfCartEntity;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Author: MrCrayfish
 */
public class RenderGolfCart extends AbstractRenderVehicle<GolfCartEntity> {

    @Override
    public void render(GolfCartEntity entity, MatrixStack matrixStack, VertexConsumerProvider renderTypeBuffer, float partialTicks, int light) {
        //Render the body
        this.renderDamagedPart(entity, SpecialModels.GOLF_CART_BODY.getModel(), matrixStack, renderTypeBuffer, light);

        //Render the handles bars
        matrixStack.push();

        // Positions the steering wheel in the correct position
        matrixStack.translate(-0.345, 0.425, 0.1);
        matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-45F));
        matrixStack.translate(0, -0.02, 0);
        matrixStack.scale(0.95F, 0.95F, 0.95F);

        // Rotates the steering wheel based on the wheel angle
        float wheelAngle = entity.prevRenderWheelAngle + (entity.renderWheelAngle - entity.prevRenderWheelAngle) * partialTicks;
        float wheelAngleNormal = wheelAngle / 45F;
        float turnRotation = wheelAngleNormal * 25F;
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(turnRotation));

        this.renderDamagedPart(entity, SpecialModels.GO_KART_STEERING_WHEEL.getModel(), matrixStack, renderTypeBuffer, light);

        matrixStack.pop();
    }

    @Override
    public void applyPlayerModel(GolfCartEntity entity, PlayerEntity player, PlayerEntityModel<AbstractClientPlayerEntity> model, float partialTicks) {
        model.rightLeg.pivotX = (float) Math.toRadians(-80F);
        model.rightLeg.pivotY = (float) Math.toRadians(15F);
        model.leftLeg.pivotX = (float) Math.toRadians(-80F);
        model.leftLeg.pivotY = (float) Math.toRadians(-15F);

        if(entity.getControllingPassenger() == player) {
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