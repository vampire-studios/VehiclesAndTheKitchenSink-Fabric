package io.github.vampirestudios.vks.client.render;

import io.github.vampirestudios.vks.common.entity.PartPosition;
import io.github.vampirestudios.vks.entity.PlaneEntity;
import io.github.vampirestudios.vks.entity.VehicleProperties;
import io.github.vampirestudios.vks.utils.RenderUtil;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;

/**
 * Author: MrCrayfish
 */
public class RenderPlaneWrapper<T extends PlaneEntity/* & EntityRaytracer.IEntityRaytraceable*/, R extends AbstractRenderVehicle<T>> extends RenderVehicleWrapper<T, R>
{
    public RenderPlaneWrapper(R renderVehicle)
    {
        super(renderVehicle);
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

        matrixStack.translate(0.0, 0.5, 0.0);

        float bodyPitch = entity.prevBodyRotationX + (entity.bodyRotationX - entity.prevBodyRotationX) * partialTicks;
        matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-bodyPitch));

        float bodyRoll = entity.prevBodyRotationZ + (entity.bodyRotationZ - entity.prevBodyRotationZ) * partialTicks;
        matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(-bodyRoll));

        matrixStack.translate(0.0, -0.5, 0.0);

        //Translate the body
        matrixStack.translate(bodyPosition.getX(), bodyPosition.getY(), bodyPosition.getZ());

        //Apply vehicle scale
        matrixStack.scale((float) bodyPosition.getScale(), (float) bodyPosition.getScale(), (float) bodyPosition.getScale());
        matrixStack.translate(0.0, 0.5, 0.0);

        //Translate the vehicle so it's axles are half way into the ground
        matrixStack.translate(0.0, properties.getAxleOffset() * 0.0625, 0.0);

        //Translate the vehicle so it's actually riding on it's wheels
        matrixStack.translate(0.0, properties.getWheelOffset() * 0.0625, 0.0);

        //Render body
        renderVehicle.render(entity, matrixStack, renderTypeBuffer, partialTicks, light);

        //Render the engine if the vehicle has explicitly stated it should
        if(entity.shouldRenderEngine() && entity.hasEngine())
        {
            BakedModel engineModel = RenderUtil.getEngineModel(entity);
            this.renderEngine(entity, properties.getEnginePosition(), engineModel, matrixStack, renderTypeBuffer, light);
        }

        //Render the fuel port of the vehicle
        /*if(entity.shouldRenderFuelPort() && entity.requiresFuel())
        {
            PoweredVehicleEntity.FuelPortType fuelPortType = entity.getFuelPortType();
            EntityRaytracer.RayTraceResultRotated result = EntityRaytracer.getContinuousInteraction();
            if(result != null && result.getType() == RayTraceResult.Type.ENTITY && result.getEntity() == entity && result.equalsContinuousInteraction(EntityRaytracer.FUNCTION_FUELING))
            {
                this.renderPart(properties.getFuelPortPosition(), fuelPortType.getOpenModel().getModel(), matrixStack, renderTypeBuffer, entity.getColor(), light, OverlayTexture.DEFAULT_LIGHT);
                if(renderVehicle.shouldRenderFuelLid())
                {
                    //this.renderPart(properties.getFuelPortLidPosition(), entity.fuelPortLid);
                }
                entity.playFuelPortOpenSound();
            }
            else
            {
                this.renderPart(properties.getFuelPortPosition(), fuelPortType.getClosedModel().getModel(), matrixStack, renderTypeBuffer, entity.getColor(), light, OverlayTexture.DEFAULT_LIGHT);
                entity.playFuelPortCloseSound();
            }
        }*/

        /*if(entity.isKeyNeeded())
        {
            this.renderPart(properties.getKeyPortPosition(), renderVehicle.getKeyHoleModel().getModel(), matrixStack, renderTypeBuffer, entity.getColor(), light, OverlayTexture.DEFAULT_LIGHT);
            if(!entity.getKeyStack().isEmpty())
            {
                this.renderKey(properties.getKeyPosition(), entity.getKeyStack(), RenderUtil.getModel(entity.getKeyStack()), matrixStack, renderTypeBuffer, -1, light, OverlayTexture.DEFAULT_LIGHT);
            }
        }*/

        matrixStack.pop();
    }
}