package io.github.vampirestudios.vks.client.render.vehicle;

import io.github.vampirestudios.vks.client.render.AbstractRenderVehicle;
import io.github.vampirestudios.vks.entity.vehicles.GoKartEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Author: MrCrayfish
 */
public class RenderGoKart extends AbstractRenderVehicle<GoKartEntity>
{
    @Override
    public void render(GoKartEntity entity, MatrixStack matrixStack, VertexConsumerProvider renderTypeBuffer, float partialTicks, int light)
    {
//        this.renderDamagedPart(entity, SpecialModels.GO_KART_BODY.getModel(), matrixStack, renderTypeBuffer, light);

        //Render the handles bars
        matrixStack.push();
        matrixStack.translate(0, 0.09, 0.49);
        matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-45F));
        matrixStack.translate(0, -0.02, 0);
        matrixStack.scale(0.9F, 0.9F, 0.9F);

        float wheelAngle = entity.prevRenderWheelAngle + (entity.renderWheelAngle - entity.prevRenderWheelAngle) * partialTicks;
        float wheelAngleNormal = wheelAngle / 45F;
        float turnRotation = wheelAngleNormal * 25F;
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(turnRotation));

//        this.renderDamagedPart(entity, SpecialModels.GO_KART_STEERING_WHEEL.getModel(), matrixStack, renderTypeBuffer, light);

        matrixStack.pop();
    }

    @Override
    public void applyPlayerModel(GoKartEntity entity, PlayerEntity player, PlayerEntityModel model, float partialTicks)
    {
        model.rightLeg.pivotX = (float) Math.toRadians(-85F);
        model.rightLeg.pivotY = (float) Math.toRadians(10F);
        model.leftLeg.pivotX = (float) Math.toRadians(-85F);
        model.leftLeg.pivotY = (float) Math.toRadians(-10F);

        float wheelAngle = entity.prevRenderWheelAngle + (entity.renderWheelAngle - entity.prevRenderWheelAngle) * partialTicks;
        float wheelAngleNormal = wheelAngle / 45F;
        float turnRotation = wheelAngleNormal * 6F;

        model.rightArm.pivotX = (float) Math.toRadians(-65F - turnRotation);
        model.rightArm.pivotY = (float) Math.toRadians(-7F);
        model.leftArm.pivotX = (float) Math.toRadians(-65F + turnRotation);
        model.leftArm.pivotY = (float) Math.toRadians(7F);
    }
}