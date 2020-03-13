package io.github.vampirestudios.vks.client.render.vehicle;

import io.github.vampirestudios.vks.client.render.AbstractRenderVehicle;
import io.github.vampirestudios.vks.client.render.SpecialModels;
import io.github.vampirestudios.vks.entity.vehicles.BumperCarEntity;
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
public class RenderBumperCar extends AbstractRenderVehicle<BumperCarEntity> {

    @Override
    public void render(BumperCarEntity entity, MatrixStack matrixStack, VertexConsumerProvider renderTypeBuffer, float partialTicks, int light) {
        //Render body
        this.renderDamagedPart(entity, SpecialModels.BUMPER_CAR_BODY.getModel(), matrixStack, renderTypeBuffer, light);

        //Render the handles bars
        matrixStack.push();
        matrixStack.translate(0, 0.2, 0);
        matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-45F));
        matrixStack.translate(0, -0.02, 0);
        matrixStack.scale(0.9F, 0.9F, 0.9F);

        float wheelAngle = entity.prevRenderWheelAngle + (entity.renderWheelAngle - entity.prevRenderWheelAngle) * partialTicks;
        float wheelAngleNormal = wheelAngle / 45F;
        float turnRotation = wheelAngleNormal * 25F;
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(turnRotation));

        RenderUtil.renderColoredModel(SpecialModels.GO_KART_STEERING_WHEEL.getModel(), ModelTransformation.Mode.NONE, false, matrixStack, renderTypeBuffer, entity.getColor(), light, OverlayTexture.DEFAULT_UV);

        matrixStack.pop();
    }

    @Override
    public void applyPlayerModel(BumperCarEntity entity, PlayerEntity player, PlayerEntityModel<AbstractClientPlayerEntity> model, float partialTicks) {
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