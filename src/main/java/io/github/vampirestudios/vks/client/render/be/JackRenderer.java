package io.github.vampirestudios.vks.client.render.be;

import io.github.vampirestudios.vks.block.entity.JackTileEntity;
import io.github.vampirestudios.vks.client.render.RenderVehicleWrapper;
import io.github.vampirestudios.vks.client.render.SpecialModels;
import io.github.vampirestudios.vks.client.render.VehicleRenderRegistry;
import io.github.vampirestudios.vks.entity.VehicleEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

/**
 * Author: MrCrayfish
 */
public class JackRenderer extends BlockEntityRenderer<JackTileEntity> {

    public JackRenderer(BlockEntityRenderDispatcher dispatcher)
    {
        super(dispatcher);
    }

    @Override
    public void render(JackTileEntity jack, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider renderTypeBuffer, int light, int overlay) {
        if(!jack.hasWorld())
            return;

        matrixStack.push();

        BlockPos pos = jack.getPos();
        BlockState state = jack.getWorld().getBlockState(pos);

        matrixStack.push();
        {
            matrixStack.translate(0.5, 0.0, 0.5);
            matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180F));
            matrixStack.translate(-0.5, 0.0, -0.5);
            BlockRenderManager dispatcher = MinecraftClient.getInstance().getBlockRenderManager();
            BakedModel model = dispatcher.getModel(state);
            VertexConsumer builder = renderTypeBuffer.getBuffer(TexturedRenderLayers.getEntityCutout());
            dispatcher.getModelRenderer().render(jack.getWorld(), model, state, pos, matrixStack, builder, true, new Random(), state.getRenderingSeed(pos), OverlayTexture.DEFAULT_UV);
        }
        matrixStack.pop();

        matrixStack.push();
        {
            matrixStack.translate(0, -2 * 0.0625, 0);
            float progress = (jack.prevLiftProgress + (jack.liftProgress - jack.prevLiftProgress) * tickDelta) / (float) JackTileEntity.MAX_LIFT_PROGRESS;
            matrixStack.translate(0, 0.5 * progress, 0);

            //Render the head
            BlockRenderManager dispatcher = MinecraftClient.getInstance().getBlockRenderManager();
            BakedModel model = SpecialModels.JACK_PISTON_HEAD.getModel();
            VertexConsumer builder = renderTypeBuffer.getBuffer(TexturedRenderLayers.getEntityCutout());
            dispatcher.getModelRenderer().render(jack.getWorld(), model, state, pos, matrixStack, builder, false, new Random(), state.getRenderingSeed(pos), OverlayTexture.DEFAULT_UV);
        }
        matrixStack.pop();

        matrixStack.push();
        {
            Entity jackEntity = jack.getJack();
            if(jackEntity != null && jackEntity.getPassengerList().size() > 0)
            {
                Entity passenger = jackEntity.getPassengerList().get(0);
                if(passenger instanceof VehicleEntity && passenger.isAlive())
                {
                    matrixStack.translate(0.5, 0.5, 0.5);
                    matrixStack.translate(0, -1 * 0.0625, 0);
                    float progress = (jack.prevLiftProgress + (jack.liftProgress - jack.prevLiftProgress) * tickDelta) / (float) JackTileEntity.MAX_LIFT_PROGRESS;
                    matrixStack.translate(0, 0.5 * progress, 0);

                    VehicleEntity vehicle = (VehicleEntity) passenger;
                    Vec3d heldOffset = vehicle.getProperties().getHeldOffset().rotateY(passenger.yaw * 0.017453292F);
                    matrixStack.translate(-heldOffset.z * 0.0625, -heldOffset.y * 0.0625, -heldOffset.x * 0.0625);
                    matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-passenger.yaw));

                    RenderVehicleWrapper wrapper = VehicleRenderRegistry.getRenderWrapper((EntityType<? extends VehicleEntity>) vehicle.getType());
                    if(wrapper != null) {
                        wrapper.render(vehicle, matrixStack, renderTypeBuffer, tickDelta, light);
                    }
                }
            }
        }
        matrixStack.pop();

        matrixStack.pop();
    }

}