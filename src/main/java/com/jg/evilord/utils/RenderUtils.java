package com.jg.evilord.utils;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.jg.evilord.client.handler.ClientHandler;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RenderUtils {

	/*
	 HitResult hitresult = this.minecraft.hitResult;
      if (p_109603_ && hitresult != null && hitresult.getType() == HitResult.Type.BLOCK) {
         profilerfiller.popPush("outline");
         BlockPos blockpos1 = ((BlockHitResult)hitresult).getBlockPos();
         BlockState blockstate = this.level.getBlockState(blockpos1);
         if (!net.minecraftforge.client.ForgeHooksClient.onDrawHighlight(this, p_109604_, hitresult, p_109601_, p_109600_, multibuffersource$buffersource))
         if (!blockstate.isAir() && this.level.getWorldBorder().isWithinBounds(blockpos1)) {
            VertexConsumer vertexconsumer2 = multibuffersource$buffersource.getBuffer(RenderType.lines());
            this.renderHitOutline(p_109600_, vertexconsumer2, p_109604_.getEntity(), d0, d1, d2, blockpos1, blockstate);
         }
      }
	 */
	
	public static RenderType TEST = RenderType.create("test", DefaultVertexFormat.POSITION_COLOR,
			VertexFormat.Mode.QUADS, 256, false, true,
			RenderType.CompositeState.builder()
					.setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getRendertypeLightningShader))
					.setWriteMaskState(new RenderStateShard.WriteMaskStateShard(true, true))
					.setTransparencyState(new RenderStateShard.TransparencyStateShard("lightning_transparency", () -> {
						RenderSystem.enableBlend();
						RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
					}, () -> {
						RenderSystem.disableBlend();
						RenderSystem.defaultBlendFunc();
					})).setOutputState(new RenderStateShard.OutputStateShard("weather_target", () -> {
						if (Minecraft.useShaderTransparency()) {
							Minecraft.getInstance().levelRenderer.getWeatherTarget().bindWrite(false);
						}

					}, () -> {
						if (Minecraft.useShaderTransparency()) {
							Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
						}

					})).createCompositeState(false));

	public static void renderPlayerArm(PoseStack matrix, MultiBufferSource buffer, int light, float p_109350_,
			float p_109351_, HumanoidArm p_109352_) {
		boolean flag = p_109352_ != HumanoidArm.LEFT;
		float f = flag ? 1.0F : -1.0F;
		float f1 = Mth.sqrt(p_109351_);
		float f2 = -0.3F * Mth.sin(f1 * (float) Math.PI);
		float f3 = 0.4F * Mth.sin(f1 * ((float) Math.PI * 2F));
		float f4 = -0.4F * Mth.sin(p_109351_ * (float) Math.PI);
		matrix.translate((double) (f * (f2 + 0.64000005F)), (double) (f3 + -0.6F + p_109350_ * -0.6F),
				(double) (f4 + -0.71999997F));
		matrix.mulPose(Vector3f.YP.rotationDegrees(f * 45.0F));
		float f5 = Mth.sin(p_109351_ * p_109351_ * (float) Math.PI);
		float f6 = Mth.sin(f1 * (float) Math.PI);
		matrix.mulPose(Vector3f.YP.rotationDegrees(f * f6 * 70.0F));
		matrix.mulPose(Vector3f.ZP.rotationDegrees(f * f5 * -20.0F));
		AbstractClientPlayer abstractclientplayer = Minecraft.getInstance().player;
		RenderSystem.setShaderTexture(0, abstractclientplayer.getSkinTextureLocation());
		matrix.translate((double) (f * -1.0F), (double) 3.6F, 3.5D);
		matrix.mulPose(Vector3f.ZP.rotationDegrees(f * 120.0F));
		matrix.mulPose(Vector3f.XP.rotationDegrees(200.0F));
		matrix.mulPose(Vector3f.YP.rotationDegrees(f * -135.0F));
		matrix.translate((double) (f * 5.6F), 0.0D, 0.0D);
		PlayerRenderer playerrenderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher()
				.<AbstractClientPlayer>getRenderer(abstractclientplayer);
		if (flag) {
			playerrenderer.renderRightHand(matrix, buffer, light, abstractclientplayer);
		} else {
			playerrenderer.renderLeftHand(matrix, buffer, light, abstractclientplayer);
		}
	}

	// GUI

	public static void blit(PoseStack p_93201_, int p_93202_, int p_93203_, int p_93204_, int p_93205_, int p_93206_,
			TextureAtlasSprite p_93207_) {
		innerBlit(p_93201_.last().pose(), p_93202_, p_93202_ + p_93205_, p_93203_, p_93203_ + p_93206_, p_93204_,
				p_93207_.getU0(), p_93207_.getU1(), p_93207_.getV0(), p_93207_.getV1());
	}

	public void blit(PoseStack p_93229_, int p_93230_, int p_93231_, int p_93232_, int p_93233_, int p_93234_,
			int p_93235_) {
		blit(p_93229_, p_93230_, p_93231_, -90, (float) p_93232_, (float) p_93233_, p_93234_, p_93235_, 256, 256);
	}

	public static void blit(PoseStack p_93144_, int p_93145_, int p_93146_, int p_93147_, float p_93148_,
			float p_93149_, int p_93150_, int p_93151_, int p_93152_, int p_93153_) {
		innerBlit(p_93144_, p_93145_, p_93145_ + p_93150_, p_93146_, p_93146_ + p_93151_, p_93147_, p_93150_, p_93151_,
				p_93148_, p_93149_, p_93152_, p_93153_);
	}

	public static void blit(PoseStack p_93161_, int p_93162_, int p_93163_, int p_93164_, int p_93165_, float p_93166_,
			float p_93167_, int p_93168_, int p_93169_, int p_93170_, int p_93171_) {
		innerBlit(p_93161_, p_93162_, p_93162_ + p_93164_, p_93163_, p_93163_ + p_93165_, 0, p_93168_, p_93169_,
				p_93166_, p_93167_, p_93170_, p_93171_);
	}

	public static void blit(PoseStack p_93134_, int p_93135_, int p_93136_, float p_93137_, float p_93138_,
			int p_93139_, int p_93140_, int p_93141_, int p_93142_) {
		blit(p_93134_, p_93135_, p_93136_, p_93139_, p_93140_, p_93137_, p_93138_, p_93139_, p_93140_, p_93141_,
				p_93142_);
	}

	private static void innerBlit(PoseStack p_93188_, int p_93189_, int p_93190_, int p_93191_, int p_93192_,
			int p_93193_, int p_93194_, int p_93195_, float p_93196_, float p_93197_, int p_93198_, int p_93199_) {
		innerBlit(p_93188_.last().pose(), p_93189_, p_93190_, p_93191_, p_93192_, p_93193_,
				(p_93196_ + 0.0F) / (float) p_93198_, (p_93196_ + (float) p_93194_) / (float) p_93198_,
				(p_93197_ + 0.0F) / (float) p_93199_, (p_93197_ + (float) p_93195_) / (float) p_93199_);
	}

	private static void innerBlit(Matrix4f p_93113_, int p_93114_, int p_93115_, int p_93116_, int p_93117_,
			int p_93118_, float p_93119_, float p_93120_, float p_93121_, float p_93122_) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
		bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferbuilder.vertex(p_93113_, (float) p_93114_, (float) p_93117_, (float) p_93118_).uv(p_93119_, p_93122_)
				.endVertex();
		bufferbuilder.vertex(p_93113_, (float) p_93115_, (float) p_93117_, (float) p_93118_).uv(p_93120_, p_93122_)
				.endVertex();
		bufferbuilder.vertex(p_93113_, (float) p_93115_, (float) p_93116_, (float) p_93118_).uv(p_93120_, p_93121_)
				.endVertex();
		bufferbuilder.vertex(p_93113_, (float) p_93114_, (float) p_93116_, (float) p_93118_).uv(p_93119_, p_93121_)
				.endVertex();
		bufferbuilder.end();
		BufferUploader.end(bufferbuilder);
	}

	// Item rendering

	public static void renderGuiItem(ItemStack p_191962_1_, int p_191962_2_, int p_191962_3_, BakedModel p_191962_4_) {
		Minecraft.getInstance().textureManager.getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
		RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		PoseStack posestack = RenderSystem.getModelViewStack();
		posestack.pushPose();
		posestack.translate((double) p_191962_2_, (double) p_191962_3_,
				(double) (100.0F + Minecraft.getInstance().getItemRenderer().blitOffset));
		posestack.translate(8.0D, 8.0D, 0.0D);
		posestack.scale(1.0F, -1.0F, 1.0F);
		posestack.scale(16.0F, 16.0F, 16.0F);
		RenderSystem.applyModelViewMatrix();
		PoseStack posestack1 = new PoseStack();
		MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers()
				.bufferSource();
		boolean flag = !p_191962_4_.usesBlockLight();
		if (flag) {
			Lighting.setupForFlatItems();
		}

		Minecraft.getInstance().getItemRenderer().render(p_191962_1_, ItemTransforms.TransformType.GUI, false,
				posestack1, multibuffersource$buffersource, 15728880, OverlayTexture.NO_OVERLAY, p_191962_4_);
		
		multibuffersource$buffersource.endBatch();
		RenderSystem.enableDepthTest();
		if (flag) {
			Lighting.setupFor3DItems();
		}

		posestack.popPose();
		RenderSystem.applyModelViewMatrix();
	}

	// Render State Shards

	public static final RenderStateShard.TransparencyStateShard ADDITIVE_TRANSPARENCY = new RenderStateShard.TransparencyStateShard(
			"lightning_transparency", () -> {
				RenderSystem.enableBlend();
				RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
			}, () -> {
				RenderSystem.disableBlend();
				RenderSystem.defaultBlendFunc();
			});

	public static final RenderStateShard.TransparencyStateShard NORMAL_TRANSPARENCY = new RenderStateShard.TransparencyStateShard(
			"lightning_transparency", () -> {
				RenderSystem.enableBlend();
				RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
						GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			}, () -> {
				RenderSystem.disableBlend();
				RenderSystem.defaultBlendFunc();
			});

	// Level Render Methods

	// BufferBuilder type

	public static void renderBlockWithTexture(BufferBuilder builder, PoseStack stack, ResourceLocation texture,
			BlockPos pos, float a) {
		renderBlockWithTexture(builder, stack, texture, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, 0.5f,
				0.5f, 0.5f, 1.0f, 1.0f, 1.0f, a, 1.0f, 1.0f, 1.0f);
	}

	public static void renderBlockWithTexture(BufferBuilder builder, PoseStack stack, ResourceLocation texture, float x,
			float y, float z, float a) {
		renderBlockWithTexture(builder, stack, texture, x, y, z, 0.5f, 0.5f, 0.5f, 1.0f, 1.0f, 1.0f, a, 1.0f, 1.0f,
				1.0f);
	}

	public static void renderBlockWithTexture(BufferBuilder builder, PoseStack stack, ResourceLocation texture, float x,
			float y, float z, float w, float h, float d, float a) {
		renderBlockWithTexture(builder, stack, texture, x, y, z, w, h, d, 1.0f, 1.0f, 1.0f, a, 1.0f, 1.0f, 1.0f);
	}

	public static void renderBlockWithTexture(BufferBuilder builder, PoseStack stack, ResourceLocation texture, float x,
			float y, float z, float w, float h, float d, float r, float g, float b, float a, float u, float v) {
		renderBlockWithTexture(builder, stack, texture, x, y, z, w, h, d, r, g, b, a, 1.0f, 1.0f, 1.0f);
	}

	public static void renderBlockWithTexture(BufferBuilder builder, PoseStack stack, ResourceLocation texture, float x,
			float y, float z, float w, float h, float d, float r, float g, float b, float a, float n1, float n2,
			float n3) {
		stack.pushPose();
		setup(stack);

		// Setting the texture
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, texture);
		// new ResourceLocation("textures/block/spruce_planks.png")

		// Starting the buffer builder
		builder.begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL);
		// UP
		builder.vertex(-w + x, h + y, -d + z).uv(1, 1).color(r, g, b, a).normal(n1, n2, n3).endVertex();
		builder.vertex(-w + x, h + y, d + z).uv(1, 0).color(r, g, b, a).normal(n1, n2, n3).endVertex();
		builder.vertex(w + x, h + y, d + z).uv(0, 0).color(r, g, b, a).normal(n1, n2, n3).endVertex();
		builder.vertex(w + x, h + y, -d + z).uv(0, 1).color(r, g, b, a).normal(n1, n2, n3).endVertex();

		// DOWN
		builder.vertex(-w + x, -h + y, d + z).uv(0, 0).color(r, g, b, a).normal(n1, n2, n3).endVertex();
		builder.vertex(-w + x, -h + y, -d + z).uv(0, 1).color(r, g, b, a).normal(n1, n2, n3).endVertex();
		builder.vertex(w + x, -h + y, -d + z).uv(1, 1).color(r, g, b, a).normal(n1, n2, n3).endVertex();
		builder.vertex(w + x, -h + y, d + z).uv(1, 0).color(r, g, b, a).normal(n1, n2, n3).endVertex();

		// LEFT
		builder.vertex(w + x, -h + y, d + z).uv(1, 0).color(r, g, b, a).normal(n1, n2, n3).endVertex();
		builder.vertex(w + x, -h + y, -d + z).uv(1, 1).color(r, g, b, a).normal(n1, n2, n3).endVertex();
		builder.vertex(w + x, h + y, -d + z).uv(0, 1).color(r, g, b, a).normal(n1, n2, n3).endVertex();
		builder.vertex(w + x, h + y, d + z).uv(0, 0).color(r, g, b, a).normal(n1, n2, n3).endVertex();

		// RIGHT
		builder.vertex(-w + x, -h + y, -d + z).uv(0, 1).color(r, g, b, a).normal(n1, n2, n3).endVertex();
		builder.vertex(-w + x, -h + y, d + z).uv(0, 0).color(r, g, b, a).normal(n1, n2, n3).endVertex();
		builder.vertex(-w + x, h + y, d + z).uv(1, 0).color(r, g, b, a).normal(n1, n2, n3).endVertex();
		builder.vertex(-w + x, h + y, -d + z).uv(1, 1).color(r, g, b, a).normal(n1, n2, n3).endVertex();

		// BACK
		builder.vertex(-w + x, -h + y, -d + z).uv(0, 1).color(r, g, b, a).normal(n1, n2, n3).endVertex();
		builder.vertex(-w + x, h + y, -d + z).uv(0, 0).color(r, g, b, a).normal(n1, n2, n3).endVertex();
		builder.vertex(w + x, h + y, -d + z).uv(1, 0).color(r, g, b, a).normal(n1, n2, n3).endVertex();
		builder.vertex(w + x, -h + y, -d + z).uv(1, 1).color(r, g, b, a).normal(n1, n2, n3).endVertex();

		// FRONT
		builder.vertex(w + x, -h + y, d + z).uv(1, 0).color(r, g, b, a).normal(n1, n2, n3).endVertex();
		builder.vertex(w + x, h + y, d + z).uv(1, 1).color(r, g, b, a).normal(n1, n2, n3).endVertex();
		builder.vertex(-w + x, h + y, d + z).uv(0, 1).color(r, g, b, a).normal(n1, n2, n3).endVertex();
		builder.vertex(-w + x, -h + y, d + z).uv(0, 0).color(r, g, b, a).normal(n1, n2, n3).endVertex();

		builder.end();

		// Renderizar el BufferBuilder
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
		RenderSystem.enableDepthTest();
		RenderSystem.depthMask(true);
		BufferUploader.end(builder);
		RenderSystem.enableTexture();
		RenderSystem.disableBlend();
		stack.popPose();
	}

	// Vertex Consumer type

	public static void renderBlock(VertexConsumer buffer, PoseStack matrix, BlockPos pos, float a) {
		renderBlock(buffer, matrix, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, 0.5f, 0.5f, 0.5f, 0.0f,
				1.0f, 0.0f, a);
	}

	public static void renderBlock(VertexConsumer buffer, PoseStack matrix, float x, float y, float z, float a) {
		renderBlock(buffer, matrix, x, y, z, 0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, a);
	}

	public static void renderBlock(VertexConsumer buffer, PoseStack matrix, float x, float y, float z, float w, float h,
			float d, float a) {
		renderBlock(buffer, matrix, x, y, z, w, h, d, 0.0f, 1.0f, 0.0f, a);
	}

	public static void renderBlock(VertexConsumer buffer, PoseStack matrix, float x, float y, float z, float w, float h,
			float d, float r, float g, float b, float a) {
		renderBlock(buffer, matrix, x, y, z, w, h, d, r, g, b, a, 1.0f, 1.0f, 1.0f);
	}

	public static void renderBlock(VertexConsumer buffer, PoseStack matrix, float x, float y, float z, float w, float h,
			float d, float r, float g, float b, float a, float n1, float n2, float n3) {

		boolean useTranslation = false;

		if (useTranslation) {
			matrix.pushPose();
			Matrix4f matrix4f = setup(matrix);

			Minecraft minecraft = Minecraft.getInstance();
			Player player = minecraft.player;
			Level world = minecraft.level;

			// Obtener la posición del jugador
			double playerX = player.getX() + 0.5f;
			double playerY = player.getY();
			double playerZ = player.getZ() + 0.5f;
			
			// Obtener la dirección en la que el jugador está mirando
			float pitch = player.getXRot();
			float yaw = player.getYRot();
			
			matrix.translate(x, y, z);
			
			/*
			 	Con esto medio puedo seguir a la mira con un laser
				matrix.translate(x, y, z);
				matrix.mulPose(new Quaternion(90, 0, 0, true));
				matrix.mulPose(new Quaternion(0, 0, yaw - 90f, true));
				matrix.mulPose(new Quaternion(0, pitch, 0, true));
			*/
			
			//matrix.mulPose(new Quaternion(90, -pitch, yaw - 90f, true));
			//matrix.mulPose(new Quaternion(0, 0, yaw - 90f, true));
			//matrix.mulPose(new Quaternion(0, pitch, 0, true));
			
			// UP
			buffer.vertex(matrix4f, -w, h, -d).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, -w, h, d).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, w, h, d).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, w, h, -d).color(r, g, b, a).normal(n1, n2, n3).endVertex();

			// DOWN
			buffer.vertex(matrix4f, -w, -h, d).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, -w, -h, -d).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, w, -h, -d).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, w, -h, d).color(r, g, b, a).normal(n1, n2, n3).endVertex();

			// LEFT
			buffer.vertex(matrix4f, w, -h, d).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, w, -h, -d).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, w, h, -d).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, w, h, d).color(r, g, b, a).normal(n1, n2, n3).endVertex();

			// RIGHT
			buffer.vertex(matrix4f, -w, -h, -d).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, -w, -h, d).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, -w, h, d).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, -w, h, -d).color(r, g, b, a).normal(n1, n2, n3).endVertex();

			// BACK
			buffer.vertex(matrix4f, -w, -h, -d).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, -w, h, -d).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, w, h, -d).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, w, -h, -d).color(r, g, b, a).normal(n1, n2, n3).endVertex();

			// FRONT
			buffer.vertex(matrix4f, w, -h, d).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, w, h, d).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, -w, h, d).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, -w, -h, d).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			matrix.popPose();
		} else {
			matrix.pushPose();
			Matrix4f matrix4f = setup(matrix);

			// UP
			buffer.vertex(matrix4f, -w + x, h + y, -d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, -w + x, h + y, d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, w + x, h + y, d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, w + x, h + y, -d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();

			// DOWN
			buffer.vertex(matrix4f, -w + x, -h + y, d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, -w + x, -h + y, -d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, w + x, -h + y, -d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, w + x, -h + y, d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();

			// LEFT
			buffer.vertex(matrix4f, w + x, -h + y, d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, w + x, -h + y, -d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, w + x, h + y, -d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, w + x, h + y, d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();

			// RIGHT
			buffer.vertex(matrix4f, -w + x, -h + y, -d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, -w + x, -h + y, d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, -w + x, h + y, d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, -w + x, h + y, -d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();

			// BACK
			buffer.vertex(matrix4f, -w + x, -h + y, -d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, -w + x, h + y, -d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, w + x, h + y, -d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, w + x, -h + y, -d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();

			// FRONT
			buffer.vertex(matrix4f, w + x, -h + y, d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, w + x, h + y, d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, -w + x, h + y, d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, -w + x, -h + y, d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			matrix.popPose();
		}
	}
	
	public static void renderFromTo(VertexConsumer buffer, PoseStack stack, BlockPos from, 
			BlockPos to, float a) {
		renderFromTo(buffer, stack, from.getX(), from.getY(), from.getZ(), to.getX(), 
				to.getZ(), a);
	}
	
	public static void renderFromTo(VertexConsumer buffer, PoseStack stack, float x, float y, float z, float toX,
			float toZ, float a) {
		renderFromTo(buffer, stack, x, y, z, toX, toZ, 0.5f, 0.5f, 0.5f, a);
	}
	
	public static void renderFromTo(VertexConsumer buffer, PoseStack stack, float x, float y, float z, float toX,
			float toZ, float w, float h, float d, float a) {
		renderFromTo(buffer, stack, x, y, z, toX, toZ, w, h, d, 1.0f, 1.0f, 1.0f, a);
	}

	public static void renderFromTo(VertexConsumer buffer, PoseStack stack, float x, float y, float z, float toX,
			float toZ, float w, float h, float d, float r, float g, float b, 
			float a) {
		stack.pushPose();

		Matrix4f matrix4f = RenderUtils.setup(stack);
		
		// UP
		buffer.vertex(matrix4f, w + x, h + y, -d + z).color(r, g, b, a).endVertex();
		buffer.vertex(matrix4f, w + toX, h + y, -d + toZ).color(r, g, b, a).endVertex();
		buffer.vertex(matrix4f, -w + toX, h + y, d + toZ).color(r, g, b, a).endVertex();
		buffer.vertex(matrix4f, -w + x, h + y, d + z).color(r, g, b, a).endVertex();
		/*
		 * Aqui primero empieza por el vertice de abajo a la izquierda
		 */
		// DOWN
		buffer.vertex(matrix4f, -w + toX, -h + y, d + toZ).color(r, g, b, a).endVertex();
		buffer.vertex(matrix4f, w + toX, -h + y, -d + toZ).color(r, g, b, a).endVertex();
		buffer.vertex(matrix4f, w + x, -h + y, -d + z).color(r, g, b, a).endVertex();
		buffer.vertex(matrix4f, -w + x, -h + y, d + z).color(r, g, b, a).endVertex();

		// LEFT
		buffer.vertex(matrix4f, w + x, -h + y, -d + z).color(r, g, b, a).endVertex();
		buffer.vertex(matrix4f, w + toX, -h + y, -d + toZ).color(r, g, b, a).endVertex();
		buffer.vertex(matrix4f, w + toX, h + y, -d + toZ).color(r, g, b, a).endVertex();
		buffer.vertex(matrix4f, w + x, h + y, -d + z).color(r, g, b, a).endVertex();

		// RIGHT
		buffer.vertex(matrix4f, -w + toX, -h + y, d + toZ).color(r, g, b, a).endVertex();
		buffer.vertex(matrix4f, -w + x, -h + y, d + z).color(r, g, b, a).endVertex();
		buffer.vertex(matrix4f, -w + x, h + y, d + z).color(r, g, b, a).endVertex();
		buffer.vertex(matrix4f, -w + toX, h + y, d + toZ).color(r, g, b, a).endVertex();

		// BACK
		buffer.vertex(matrix4f, -w + toX, -h + y, d + toZ).color(r, g, b, a).endVertex();
		buffer.vertex(matrix4f, -w + toX, h + y, d + toZ).color(r, g, b, a).endVertex();
		buffer.vertex(matrix4f, w + toX, h + y, -d + toZ).color(r, g, b, a).endVertex();
		buffer.vertex(matrix4f, w + toX, -h + y, -d + toZ).color(r, g, b, a).endVertex();

		// FRONT
		buffer.vertex(matrix4f, w + x, -h + y, -d + z).color(r, g, b, a).endVertex();
		buffer.vertex(matrix4f, w + x, h + y, -d + z).color(r, g, b, a).endVertex();
		buffer.vertex(matrix4f, -w + x, h + y, d + z).color(r, g, b, a).endVertex();
		buffer.vertex(matrix4f, -w + x, -h + y, d + z).color(r, g, b, a).endVertex();

		stack.popPose();
	}

	public static void renderBeam(PoseStack matrixStackIn, VertexConsumer builder, 
			Vec3 pos, float length, float yaw, float pitch, float radius, float offset) {
		matrixStackIn.pushPose();
		matrixStackIn.translate(pos.x, pos.y-4f, pos.y);
        //matrixStackIn.mulPose(new Quaternion(90, 0, 0, true));
        //matrixStackIn.mulPose(new Quaternion(0, 0, yaw - 90f, true));
        //matrixStackIn.mulPose(new Quaternion(-pitch, 0, 0, true));
        drawBeam(builder, matrixStackIn.last().pose(), radius, offset, length);
        matrixStackIn.popPose();
	}
	
	public static void drawBeam(VertexConsumer buffer, Matrix4f matrix4f, float radius, 
			float offset, float length) {
		buffer.vertex(matrix4f, -radius, offset, -length).color(1.0f, 0.0f, 0.0f, 1.0f);
		buffer.vertex(matrix4f, -radius, offset, length).color(1.0f, 0.0f, 0.0f, 1.0f);
		buffer.vertex(matrix4f, radius, offset, length).color(1.0f, 0.0f, 0.0f, 1.0f);
		buffer.vertex(matrix4f, radius, offset, -length).color(1.0f, 0.0f, 0.0f, 1.0f);
		
		buffer.vertex(matrix4f, -radius, offset, 1).color(1.0f, 0.0f, 0.0f, 1.0f);
		buffer.vertex(matrix4f, -radius, length, 1).color(1.0f, 0.0f, 0.0f, 1.0f);
		buffer.vertex(matrix4f, radius, length, 1).color(1.0f, 0.0f, 0.0f, 1.0f);
		buffer.vertex(matrix4f, radius, offset, 1).color(1.0f, 0.0f, 0.0f, 1.0f);
	}
	
	public static void quadFace(VertexConsumer buffer, Matrix4f mat, float x, float y, float z, float w, float h,
			float d, float r, float g, float b, float a, float n1, float n2, float n3) {

	}

	public static Matrix4f setup(PoseStack matrix) {
		Camera cam = Minecraft.getInstance().gameRenderer.getMainCamera();
		matrix.translate(-cam.getPosition().x(), -cam.getPosition().y(), -cam.getPosition().z());

		// Aplicar la transformación inversa de la camara del jugador
		Matrix4f matrix4f = matrix.last().pose();
		matrix4f.multiply(RenderSystem.getModelViewMatrix());
		return matrix4f;
	}

	// Experimental methods

	public static void litQuad(PoseStack mStack, MultiBufferSource buffer, double x, double y, double w, double h,
			float r, float g, float b, float a, TextureAtlasSprite sprite) {
		VertexConsumer builder = buffer.getBuffer(RenderType.lightning());

		float f7 = sprite.getU0();
		float f8 = sprite.getU1();
		float f5 = sprite.getV0();
		float f6 = sprite.getV1();
		Matrix4f mat = mStack.last().pose();
		builder.vertex(mat, (float) x, (float) y + (float) h, 0).uv(f7, f6).color(r, g, b, a).endVertex();
		builder.vertex(mat, (float) x + (float) w, (float) y + (float) h, 0).uv(f8, f6).color(r, g, b, a).endVertex();
		builder.vertex(mat, (float) x + (float) w, (float) y, 0).uv(f8, f5).color(r, g, b, a).endVertex();
		builder.vertex(mat, (float) x, (float) y, 0).uv(f7, f5).color(r, g, b, a).endVertex();
	}

	public static void litQuad(PoseStack mStack, MultiBufferSource buffer, double x, double y, double w, double h,
			float r, float g, float b, TextureAtlasSprite sprite) {
		litQuad(mStack, buffer, x, y, w, h, r, g, b, 1.0f, sprite);
	}

	public static void litQuad(PoseStack mStack, MultiBufferSource buffer, double x, double y, double w, double h,
			float r, float g, float b, float u, float v, float uw, float vh) {
		VertexConsumer builder = buffer.getBuffer(RenderType.lightning());

		Matrix4f mat = mStack.last().pose();
		builder.vertex(mat, (float) x, (float) y + (float) h, 0).uv(u, v + vh).color(r, g, b, 1.0f).endVertex();
		builder.vertex(mat, (float) x + (float) w, (float) y + (float) h, 0).uv(u + uw, v + vh).color(r, g, b, 1.0f)
				.endVertex();
		builder.vertex(mat, (float) x + (float) w, (float) y, 0).uv(u + uw, v).color(r, g, b, 1.0f).endVertex();
		builder.vertex(mat, (float) x, (float) y, 0).uv(u, v).color(r, g, b, 1.0f).endVertex();
	}

	public static void litBillboard(PoseStack mStack, MultiBufferSource buffer, double x, double y, double z, float r,
			float g, float b, TextureAtlasSprite sprite) {
		VertexConsumer builder = buffer.getBuffer(RenderType.lightning());
		Camera renderInfo = Minecraft.getInstance().gameRenderer.getMainCamera();
		Vec3 vector3d = renderInfo.getPosition();
		float partialTicks = Minecraft.getInstance().getFrameTime();
		float f = (float) (x);
		float f1 = (float) (y);
		float f2 = (float) (z);
		Quaternion quaternion = renderInfo.rotation();

		Vector3f[] avector3f = new Vector3f[] { new Vector3f(-0.5f, -0.5f, 0.0f), new Vector3f(-0.5f, 0.5f, 0.0f),
				new Vector3f(0.5f, 0.5f, 0.0f), new Vector3f(0.5f, -0.5f, 0.0f) };
		float f4 = 1.0f;

		for (int i = 0; i < 4; ++i) {
			Vector3f vector3f = avector3f[i];
			vector3f.transform(quaternion);
			vector3f.mul(f4);
			vector3f.add(f, f1, f2);
		}

		float f7 = sprite.getU0();
		float f8 = sprite.getU1();
		float f5 = sprite.getV0();
		float f6 = sprite.getV1();
		Matrix4f mat = mStack.last().pose();
		builder.vertex(mat, avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).uv(f8, f6).color(r, g, b, 1.0f)
				.endVertex();
		builder.vertex(mat, avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).uv(f8, f5).color(r, g, b, 1.0f)
				.endVertex();
		builder.vertex(mat, avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).uv(f7, f5).color(r, g, b, 1.0f)
				.endVertex();
		builder.vertex(mat, avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).uv(f7, f6).color(r, g, b, 1.0f)
				.endVertex();
	}

	public static void dragon(PoseStack mStack, MultiBufferSource buf, double x, double y, double z, float radius,
			float r, float g, float b) {
		float f5 = 0.5f; // max number of beams
		float f7 = Math.min(f5 > 0.8F ? (f5 - 0.8F) / 0.2F : 0.0F, 1.0F);
		Random random = new Random(432L);
		VertexConsumer builder = buf.getBuffer(RenderType.lightning());
		mStack.pushPose();
		mStack.translate(x, y, z);

		float rotation = (float) (ClientHandler.partialTicks / 200);

		for (int i = 0; (float) i < (f5 + f5 * f5) / 2.0F * 60.0F; ++i) {
			mStack.mulPose(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0F));
			mStack.mulPose(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0F));
			mStack.mulPose(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0F));
			mStack.mulPose(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0F));
			mStack.mulPose(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0F));
			mStack.mulPose(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0F + rotation * 90.0F));
			float f3 = random.nextFloat() * 20.0F + 5.0F + f7 * 10.0F;
			float f4 = random.nextFloat() * 2.0F + 1.0F + f7 * 2.0F;
			f3 *= 0.05f * radius;
			f4 *= 0.05f * radius;
			Matrix4f mat = mStack.last().pose();
			float alpha = 1 - f7;

			builder.vertex(mat, 0.0F, 0.0F, 0.0F).color(r, g, b, alpha).endVertex();
			builder.vertex(mat, 0.0F, 0.0F, 0.0F).color(r, g, b, alpha).endVertex();
			builder.vertex(mat, -ROOT_3 * f4, f3, -0.5F * f4).color(r, g, b, 0).endVertex();
			builder.vertex(mat, ROOT_3 * f4, f3, -0.5F * f4).color(r, g, b, 0).endVertex();
			builder.vertex(mat, 0.0F, 0.0F, 0.0F).color(r, g, b, alpha).endVertex();
			builder.vertex(mat, 0.0F, 0.0F, 0.0F).color(r, g, b, alpha).endVertex();
			builder.vertex(mat, ROOT_3 * f4, f3, -0.5F * f4).color(r, g, b, 0).endVertex();
			builder.vertex(mat, 0.0F, f3, 1.0F * f4).color(r, g, b, 0).endVertex();
			builder.vertex(mat, 0.0F, 0.0F, 0.0F).color(r, g, b, alpha).endVertex();
			builder.vertex(mat, 0.0F, 0.0F, 0.0F).color(r, g, b, alpha).endVertex();
			builder.vertex(mat, 0.0F, f3, 1.0F * f4).color(r, g, b, 0).endVertex();
			builder.vertex(mat, -ROOT_3 * f4, f3, -0.5F * f4).color(r, g, b, 0).endVertex();
		}

		mStack.popPose();
	}

	// copied from EnderDragonRenderer

	private static final float ROOT_3 = (float) (Math.sqrt(3.0D) / 2.0D);

	public static void vaporCube(PoseStack mStack, VertexConsumer builder, TextureAtlasSprite sprite, float x1,
			float y1, float z1, float x2, float y2, float z2, int r, int g, int b, int a, int light, boolean nx,
			boolean px, boolean ny, boolean py, boolean nz, boolean pz) {
		Matrix4f mat = mStack.last().pose();
		if (py) {
			builder.vertex(mat, x1, y2, z1).color(r, g, b, a).uv(sprite.getU(z1 * 16), sprite.getV(x1 * 16)).uv2(light)
					.normal(0, 1, 0).endVertex();
			builder.vertex(mat, x1, y2, z2).color(r, g, b, a).uv(sprite.getU(z2 * 16), sprite.getV(x1 * 16)).uv2(light)
					.normal(0, 1, 0).endVertex();
			builder.vertex(mat, x2, y2, z2).color(r, g, b, a).uv(sprite.getU(z2 * 16), sprite.getV(x2 * 16)).uv2(light)
					.normal(0, 1, 0).endVertex();
			builder.vertex(mat, x2, y2, z1).color(r, g, b, a).uv(sprite.getU(z1 * 16), sprite.getV(x2 * 16)).uv2(light)
					.normal(0, 1, 0).endVertex();
		}
		if (ny) {
			builder.vertex(mat, x1, y1, z2).color(r, g, b, a).uv(sprite.getU(z1 * 16), sprite.getV(x1 * 16)).uv2(light)
					.normal(0, -1, 0).endVertex();
			builder.vertex(mat, x1, y1, z1).color(r, g, b, a).uv(sprite.getU(z2 * 16), sprite.getV(x1 * 16)).uv2(light)
					.normal(0, -1, 0).endVertex();
			builder.vertex(mat, x2, y1, z1).color(r, g, b, a).uv(sprite.getU(z2 * 16), sprite.getV(x2 * 16)).uv2(light)
					.normal(0, -1, 0).endVertex();
			builder.vertex(mat, x2, y1, z2).color(r, g, b, a).uv(sprite.getU(z1 * 16), sprite.getV(x2 * 16)).uv2(light)
					.normal(0, -1, 0).endVertex();
		}
		if (nz) {
			builder.vertex(mat, x2, y1, z1).color(r, g, b, a).uv(sprite.getU(x1 * 16), sprite.getV(y1 * 16)).uv2(light)
					.normal(0, 0, -1).endVertex();
			builder.vertex(mat, x1, y1, z1).color(r, g, b, a).uv(sprite.getU(x2 * 16), sprite.getV(y1 * 16)).uv2(light)
					.normal(0, 0, -1).endVertex();
			builder.vertex(mat, x1, y2, z1).color(r, g, b, a).uv(sprite.getU(x2 * 16), sprite.getV(y2 * 16)).uv2(light)
					.normal(0, 0, -1).endVertex();
			builder.vertex(mat, x2, y2, z1).color(r, g, b, a).uv(sprite.getU(x1 * 16), sprite.getV(y2 * 16)).uv2(light)
					.normal(0, 0, -1).endVertex();
		}
		if (pz) {
			builder.vertex(mat, x1, y1, z2).color(r, g, b, a).uv(sprite.getU(x1 * 16), sprite.getV(y1 * 16)).uv2(light)
					.normal(0, 0, 1).endVertex();
			builder.vertex(mat, x2, y1, z2).color(r, g, b, a).uv(sprite.getU(x2 * 16), sprite.getV(y1 * 16)).uv2(light)
					.normal(0, 0, 1).endVertex();
			builder.vertex(mat, x2, y2, z2).color(r, g, b, a).uv(sprite.getU(x2 * 16), sprite.getV(y2 * 16)).uv2(light)
					.normal(0, 0, 1).endVertex();
			builder.vertex(mat, x1, y2, z2).color(r, g, b, a).uv(sprite.getU(x1 * 16), sprite.getV(y2 * 16)).uv2(light)
					.normal(0, 0, 1).endVertex();
		}
		if (nx) {
			builder.vertex(mat, x1, y1, z1).color(r, g, b, a).uv(sprite.getU(z1 * 16), sprite.getV(y1 * 16)).uv2(light)
					.normal(-1, 0, 0).endVertex();
			builder.vertex(mat, x1, y1, z2).color(r, g, b, a).uv(sprite.getU(z2 * 16), sprite.getV(y1 * 16)).uv2(light)
					.normal(-1, 0, 0).endVertex();
			builder.vertex(mat, x1, y2, z2).color(r, g, b, a).uv(sprite.getU(z2 * 16), sprite.getV(y2 * 16)).uv2(light)
					.normal(-1, 0, 0).endVertex();
			builder.vertex(mat, x1, y2, z1).color(r, g, b, a).uv(sprite.getU(z1 * 16), sprite.getV(y2 * 16)).uv2(light)
					.normal(-1, 0, 0).endVertex();
		}
		if (px) {
			builder.vertex(mat, x2, y1, z2).color(r, g, b, a).uv(sprite.getU(z1 * 16), sprite.getV(y1 * 16)).uv2(light)
					.normal(1, 0, 0).endVertex();
			builder.vertex(mat, x2, y1, z1).color(r, g, b, a).uv(sprite.getU(z2 * 16), sprite.getV(y1 * 16)).uv2(light)
					.normal(1, 0, 0).endVertex();
			builder.vertex(mat, x2, y2, z1).color(r, g, b, a).uv(sprite.getU(z2 * 16), sprite.getV(y2 * 16)).uv2(light)
					.normal(1, 0, 0).endVertex();
			builder.vertex(mat, x2, y2, z2).color(r, g, b, a).uv(sprite.getU(z1 * 16), sprite.getV(y2 * 16)).uv2(light)
					.normal(1, 0, 0).endVertex();
		}
	}
}
