package io.github.vampirestudios.vks.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.vampirestudios.vks.common.ItemLookup;
import io.github.vampirestudios.vks.entity.PoweredVehicleEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Direction;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Author: MrCrayfish
 */
public class RenderUtil
{
    /**
     * Draws a textured modal rectangle with more precision than GuiScreen's methods. This will only
     * work correctly if the bound texture is 256x256.
     */
    public static void drawTexturedModalRect(double x, double y, int textureX, int textureY, double width, double height)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, VertexFormats.POSITION_TEXTURE);
        bufferbuilder.vertex(x, y + height, 0).texture(((float) textureX * 0.00390625F), ((float) (textureY + height) * 0.00390625F)).next();
        bufferbuilder.vertex(x + width, y + height, 0).texture(((float) (textureX + width) * 0.00390625F), ((float) (textureY + height) * 0.00390625F)).next();
        bufferbuilder.vertex(x + width, y, 0).texture(((float) (textureX + width) * 0.00390625F), ((float) textureY * 0.00390625F)).next();
        bufferbuilder.vertex(x + 0, y, 0).texture(((float) textureX * 0.00390625F), ((float) textureY * 0.00390625F)).next();
        tessellator.draw();
    }

    /**
     * Draws a rectangle with a horizontal gradient between the specified colors (ARGB format).
     */
    public static void drawGradientRectHorizontal(int left, int top, int right, int bottom, int leftColor, int rightColor)
    {
        float redStart = (float)(leftColor >> 24 & 255) / 255.0F;
        float greenStart = (float)(leftColor >> 16 & 255) / 255.0F;
        float blueStart = (float)(leftColor >> 8 & 255) / 255.0F;
        float alphaStart = (float)(leftColor & 255) / 255.0F;
        float redEnd = (float)(rightColor >> 24 & 255) / 255.0F;
        float greenEnd = (float)(rightColor >> 16 & 255) / 255.0F;
        float blueEnd = (float)(rightColor >> 8 & 255) / 255.0F;
        float alphaEnd = (float)(rightColor & 255) / 255.0F;
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR);
        bufferbuilder.vertex(right, top, 0).color(greenEnd, blueEnd, alphaEnd, redEnd).next();
        bufferbuilder.vertex(left, top, 0).color(greenStart, blueStart, alphaStart, redStart).next();
        bufferbuilder.vertex(left, bottom, 0).color(greenStart, blueStart, alphaStart, redStart).next();
        bufferbuilder.vertex(right, bottom, 0).color(greenEnd, blueEnd, alphaEnd, redEnd).next();
        tessellator.draw();
        RenderSystem.shadeModel(7424);
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableTexture();
    }

    public static void scissor(int x, int y, int width, int height) //TODO might need fixing. I believe I rewrote this in a another mod
    {
        MinecraftClient mc = MinecraftClient.getInstance();
        int scale = (int) mc.getWindow().getScaleFactor();
        GL11.glScissor(x * scale, mc.getWindow().getHeight() - y * scale - height * scale, Math.max(0, width * scale), Math.max(0, height * scale));
    }

    public static BakedModel getModel(ItemStack stack) {
        return MinecraftClient.getInstance().getItemRenderer().getModels().getModel(stack);
    }

    public static void renderColoredModel(BakedModel model, ModelTransformation.Mode transformType, boolean leftHanded, MatrixStack matrixStack, VertexConsumerProvider renderTypeBuffer, int color, int lightTexture, int overlayTexture)
    {
        matrixStack.push();
//        net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(matrixStack, model, transformType, leftHanded);
        matrixStack.translate(-0.5, -0.5, -0.5);
        if(!model.isBuiltin()) {
            VertexConsumer vertexBuilder = renderTypeBuffer.getBuffer(TexturedRenderLayers.getEntityCutout());
            renderModel(model, ItemStack.EMPTY, color, lightTexture, overlayTexture, matrixStack, vertexBuilder);
        }
        matrixStack.pop();
    }

    public static void renderDamagedVehicleModel(BakedModel model, ModelTransformation.Mode transformType, boolean leftHanded, MatrixStack matrixStack, int stage, int color, int lightTexture, int overlayTexture)
    {
        matrixStack.push();
//        net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(matrixStack, model, transformType, leftHanded);
        matrixStack.translate(-0.5, -0.5, -0.5);
        if(!model.isBuiltin())
        {
            MinecraftClient mc = MinecraftClient.getInstance();
            VertexConsumer vertexBuilder = new TransformingVertexConsumer(mc.getBufferBuilders().getEffectVertexConsumers().getBuffer(ModelLoader.BLOCK_DESTRUCTION_RENDER_LAYERS.get(stage)), matrixStack.peek());
            renderModel(model, ItemStack.EMPTY, color, lightTexture, overlayTexture, matrixStack, vertexBuilder);
        }
        matrixStack.pop();
    }

    public static void renderModel(ItemStack stack, ModelTransformation.Mode transformType, boolean leftHanded, MatrixStack matrixStack, VertexConsumerProvider renderTypeBuffer, int lightTexture, int overlayTexture, BakedModel model) {
        if(!stack.isEmpty())
        {
            matrixStack.push();
            boolean isGui = transformType == ModelTransformation.Mode.GUI;
            boolean tridentFlag = isGui || transformType == ModelTransformation.Mode.GROUND || transformType == ModelTransformation.Mode.FIXED;
            if(stack.getItem() == Items.TRIDENT && tridentFlag)
            {
                model = MinecraftClient.getInstance().getBakedModelManager().getModel(new ModelIdentifier("minecraft:trident#inventory"));
            }

            matrixStack.translate(-0.5, -0.5, -0.5);
            if(!model.isBuiltin() && (stack.getItem() != Items.TRIDENT || tridentFlag))
            {
                RenderLayer renderType = RenderLayers.getItemLayer(stack);
                if(isGui && Objects.equals(renderType, TexturedRenderLayers.getEntityTranslucent()))
                {
                    renderType = TexturedRenderLayers.getEntityTranslucentCull();
                }
                VertexConsumer vertexBuilder = ItemRenderer.getArmorVertexConsumer(renderTypeBuffer, renderType, true, stack.hasEnchantmentGlint());
                renderModel(model, stack, -1, lightTexture, overlayTexture, matrixStack, vertexBuilder);
            }


            matrixStack.pop();
        }
    }

    private static void renderModel(BakedModel model, ItemStack stack, int color, int lightTexture, int overlayTexture, MatrixStack matrixStack, VertexConsumer vertexBuilder) {
        Random random = new Random();
        for(Direction direction : Direction.values()) {
            random.setSeed(42L);
            renderQuads(matrixStack, vertexBuilder, model.getQuads(null, direction, random), stack, color, lightTexture, overlayTexture);
        }
        random.setSeed(42L);
        renderQuads(matrixStack, vertexBuilder, model.getQuads(null, null, random), stack, color, lightTexture, overlayTexture);
    }

    private static void renderQuads(MatrixStack matrixStack, VertexConsumer vertexBuilder, List<BakedQuad> quads, ItemStack stack, int color, int lightTexture, int overlayTexture) {
        MatrixStack.Entry entry = matrixStack.peek();
        for(BakedQuad quad : quads) {
            int tintColor = 0xFFFFFF;
            if(quad.hasColor()) {
                tintColor = color;
            }
            float red = (float) (tintColor >> 16 & 255) / 255.0F;
            float green = (float) (tintColor >> 8 & 255) / 255.0F;
            float blue = (float) (tintColor & 255) / 255.0F;
            vertexBuilder.quad(entry, quad, red, green, blue, lightTexture, overlayTexture);
        }
    }

    /**
     * Gets an IBakedModel of the wheel currently on a powered vehicle.
     * If there are no wheels installed on the vehicle, a null model will be returned.
     *
     * @param entity the powered vehicle to get the wheel model from
     * @return an IBakedModel of the wheel or null if wheels are not present
     */
    @Nullable
    public static BakedModel getWheelModel(PoweredVehicleEntity entity) {
        ItemStack stack = ItemLookup.getWheel(entity);
        if(!stack.isEmpty()) {
            return RenderUtil.getModel(stack);
        }
        return MinecraftClient.getInstance().getBakedModelManager().getMissingModel();
    }

    /**
     * Gets an IBakedModel of the engine currently on a powered vehicle.
     * If there is no engine installed in the vehicle, a null model will be returned.
     *
     * @param entity the powered vehicle to get the engine model from
     * @return an IBakedModel of the engine or null if the engine is not present
     */
    @Nullable
    public static BakedModel getEngineModel(PoweredVehicleEntity entity) {
        ItemStack stack = ItemLookup.getEngine(entity);
        if(!stack.isEmpty()) {
            return RenderUtil.getModel(stack);
        }
        return MinecraftClient.getInstance().getBakedModelManager().getMissingModel();
    }
}