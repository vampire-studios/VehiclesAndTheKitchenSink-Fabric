package io.github.vampirestudios.vks.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.vampirestudios.vks.common.entity.PartPosition;
import io.github.vampirestudios.vks.entity.LandVehicleEntity;
import io.github.vampirestudios.vks.entity.VehicleProperties;
import io.github.vampirestudios.vks.utils.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

/**
 * Author: MrCrayfish
 */
public class RenderLandVehicleWrapper<T extends LandVehicleEntity/* & EntityRaytracer.IEntityRaytraceable*/, R extends AbstractRenderVehicle<T>> extends RenderVehicleWrapper<T, R> {
    public RenderLandVehicleWrapper(R renderVehicle) {
        super(renderVehicle);
    }

    public void render(T entity, MatrixStack matrixStack, VertexConsumerProvider renderTypeBuffer, float partialTicks, int light) {
        if(!entity.isAlive())
            return;

        matrixStack.push();

        VehicleProperties properties = entity.getProperties();
        PartPosition bodyPosition = properties.getBodyPosition();
        matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion((float) bodyPosition.getRotX()));
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion((float) bodyPosition.getRotY()));
        matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion((float) bodyPosition.getRotZ()));

        float additionalYaw = entity.prevAdditionalYaw + (entity.additionalYaw - entity.prevAdditionalYaw) * partialTicks;
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(additionalYaw));

        matrixStack.translate(bodyPosition.getX(), bodyPosition.getY(), bodyPosition.getZ());

        if(entity.canTowTrailer()) {
            matrixStack.push();
            matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180F));
            Vec3d towBarOffset = properties.getTowBarPosition();
            matrixStack.translate(towBarOffset.x * 0.0625, towBarOffset.y * 0.0625 + 0.5, -towBarOffset.z * 0.0625);
//            RenderUtil.renderColoredModel(this.renderVehicle.getTowBarModel().getModel(), ItemCameraTransforms.TransformType.NONE, false, matrixStack, renderTypeBuffer, -1, light, OverlayTexture.DEFAULT_LIGHT);
            matrixStack.pop();
        }

        matrixStack.scale((float) bodyPosition.getScale(), (float) bodyPosition.getScale(), (float) bodyPosition.getScale());
        matrixStack.translate(0.0, 0.5, 0.0);
        matrixStack.translate(0.0, properties.getAxleOffset() * 0.0625, 0.0);
        matrixStack.translate(0.0, properties.getWheelOffset() * 0.0625, 0.0);
        renderVehicle.render(entity, matrixStack, renderTypeBuffer, partialTicks, light);

        if(entity.hasWheels()) {
            matrixStack.push();
            matrixStack.translate(0.0, -8 * 0.0625, 0.0);
            matrixStack.translate(0.0, -properties.getAxleOffset() * 0.0625F, 0.0);
            BakedModel wheelModel = RenderUtil.getWheelModel(entity);
            properties.getWheels().forEach(wheel -> this.renderWheel(entity, wheel, wheelModel, partialTicks, matrixStack, renderTypeBuffer, light));
            matrixStack.pop();
        }

        //Render the engine if the vehicle has explicitly stated it should
        if(entity.shouldRenderEngine() && entity.hasEngine()) {
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
                this.renderKey(properties.getKeyPosition(), RenderUtil.getModel(entity.getKeyStack()), matrixStack, renderTypeBuffer, -1, light, OverlayTexture.DEFAULT_LIGHT);
            }
        }*/

        /*if(Config.CLIENT.renderSteeringDebug.get())
        {
            if(properties.getFrontAxelVec() != null && properties.getRearAxelVec() != null)
            {
                matrixStack.push();
                {
                    matrixStack.translate(0.0, -0.5, 0.0);
                    matrixStack.translate(0.0, -properties.getAxleOffset() * 0.0625, 0.0);
                    matrixStack.translate(0.0, -properties.getWheelOffset() * 0.0625, 0.0);

                    matrixStack.push();
                    {
                        Vec3d frontAxelVec = properties.getFrontAxelVec();
                        frontAxelVec = frontAxelVec.multiply(0.0625);
                        matrixStack.translate(frontAxelVec.x, 0, frontAxelVec.z);
                        this.renderSteeringLine(matrixStack, 0xFFFFFF);
                    }
                    matrixStack.pop();

                    matrixStack.push();
                    {
                        Vec3d frontAxelVec = properties.getFrontAxelVec();
                        frontAxelVec = frontAxelVec.multiply(0.0625);
                        Vec3d nextFrontAxelVec = new Vec3d(0, 0, entity.getSpeed() / 20F).rotateY(entity.renderWheelAngle * 0.017453292F);
                        frontAxelVec = frontAxelVec.add(nextFrontAxelVec);
                        matrixStack.translate(frontAxelVec.x, 0, frontAxelVec.z);
                        this.renderSteeringLine(matrixStack, 0xFFDD00);
                    }
                    matrixStack.pop();

                    matrixStack.push();
                    {
                        Vec3d rearAxelVec = properties.getRearAxelVec();
                        rearAxelVec = rearAxelVec.multiply(0.0625);
                        matrixStack.translate(rearAxelVec.x, 0, rearAxelVec.z);
                        this.renderSteeringLine(matrixStack, 0xFFFFFF);
                    }
                    matrixStack.pop();

                    matrixStack.push();
                    {
                        Vec3d frontAxelVec = properties.getFrontAxelVec();
                        frontAxelVec = frontAxelVec.multiply(0.0625);
                        Vec3d nextFrontAxelVec = new Vec3d(0, 0, entity.getSpeed() / 20F).rotateY(entity.renderWheelAngle * 0.017453292F);
                        frontAxelVec = frontAxelVec.add(nextFrontAxelVec);
                        Vec3d rearAxelVec = properties.getRearAxelVec();
                        rearAxelVec = rearAxelVec.multiply(0.0625);
                        double deltaYaw = Math.toDegrees(Math.atan2(rearAxelVec.z - frontAxelVec.z, rearAxelVec.x - frontAxelVec.x)) + 90;
                        if(entity.isRearWheelSteering())
                        {
                            deltaYaw += 180;
                        }
                        rearAxelVec = rearAxelVec.add(Vec3d.fromPolar(0, (float) deltaYaw).multiply(entity.getSpeed() / 20F));
                        matrixStack.translate(rearAxelVec.x, 0, rearAxelVec.z);
                        this.renderSteeringLine(matrixStack, 0xFFDD00);
                    }
                    matrixStack.pop();

                    matrixStack.push();
                    {
                        Vec3d nextFrontAxelVec = new Vec3d(0, 0, entity.getSpeed() / 20F).rotateY(entity.wheelAngle * 0.017453292F);
                        nextFrontAxelVec = nextFrontAxelVec.add(properties.getFrontAxelVec().multiply(0.0625));
                        Vec3d nextRearAxelVec = new Vec3d(0, 0, entity.getSpeed() / 20F);
                        nextRearAxelVec = nextRearAxelVec.add(properties.getRearAxelVec().multiply(0.0625));
                        Vec3d nextVehicleVec = nextFrontAxelVec.add(nextRearAxelVec).multiply(0.5);
                        nextVehicleVec = nextVehicleVec.subtract(properties.getFrontAxelVec().add(properties.getRearAxelVec()).multiply(0.0625).multiply(0.5));
                        matrixStack.push();
                        {
                            this.renderSteeringLine(matrixStack, 0xFFFFFF);
                        }
                        matrixStack.pop();
                        matrixStack.push();
                        {
                            matrixStack.translate(nextVehicleVec.x, 0, nextVehicleVec.z);
                            this.renderSteeringLine(matrixStack, 0xFFDD00);
                        }
                        matrixStack.pop();
                    }
                    matrixStack.pop();
                }
                matrixStack.pop();
            }
        }*/
        matrixStack.pop();
    }

    private void renderSteeringLine(MatrixStack stack, int color)
    {
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        RenderSystem.disableTexture();
        RenderSystem.lineWidth(Math.max(2.0F, (float) MinecraftClient.getInstance().getWindow().getFramebufferWidth() / 1920.0F * 2.0F));
        RenderSystem.enableDepthTest();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_LINES, VertexFormats.POSITION_COLOR);
        buffer.vertex(stack.peek().getModel(), 0, 0, 0).color(red, green, blue, 1.0F).next();
        buffer.vertex(stack.peek().getModel(), 0, 2, 0).color(red, green, blue, 1.0F).next();
        tessellator.draw();
        RenderSystem.disableDepthTest();
        RenderSystem.enableTexture();
    }

    protected void renderWheel(LandVehicleEntity vehicle, Wheel wheel, BakedModel model, float partialTicks, MatrixStack matrixStack, VertexConsumerProvider renderTypeBuffer, int light)
    {
        if(!wheel.shouldRender())
            return;

        matrixStack.push();
        matrixStack.translate((wheel.getOffsetX() * 0.0625) * wheel.getSide().offset, wheel.getOffsetY() * 0.0625, wheel.getOffsetZ() * 0.0625);
        if(wheel.getPosition() == Wheel.Position.FRONT) {
            float wheelAngle = vehicle.prevRenderWheelAngle + (vehicle.renderWheelAngle - vehicle.prevRenderWheelAngle) * partialTicks;
            matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(wheelAngle));
        }
        if(vehicle.isMoving()) {
            matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-wheel.getWheelRotation(vehicle, partialTicks)));
        }
        matrixStack.translate((((wheel.getWidth() * wheel.getScaleX()) / 2) * 0.0625) * wheel.getSide().offset, 0.0, 0.0);
        matrixStack.scale(wheel.getScaleX(), wheel.getScaleY(), wheel.getScaleZ());
        if(wheel.getSide() == Wheel.Side.RIGHT) {
            matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180F));
        }
        RenderUtil.renderColoredModel(model, ModelTransformation.Mode.NONE, false, matrixStack, renderTypeBuffer, vehicle.getWheelColor(), light, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();
    }
}