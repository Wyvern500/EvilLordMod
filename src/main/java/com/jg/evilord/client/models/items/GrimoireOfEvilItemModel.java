package com.jg.evilord.client.models.items;

import com.jg.evilord.Evilord;
import com.jg.evilord.animation.model.JgModel;
import com.jg.evilord.animation.model.JgModelPart;
import com.jg.evilord.client.handler.ClientHandler;
import com.jg.evilord.client.model.entity.GrimoireOfEvilModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.RandomSource;

public class GrimoireOfEvilItemModel extends JgModel {
	
	public static final Material BOOK_LOCATION = 
			new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(Evilord.MODID, 
					"entity/bookmodel"));
	
	public int time;
	public float flip;
	public float oFlip;
	public float flipT;
	public float flipA;
	public float open;
	public float oOpen;
	public float rot;
	public float oRot;
	public float tRot;
	private static final RandomSource RANDOM = new LegacyRandomSource(0L);
	private GrimoireOfEvilModel jgBookModel;
	private BookModel bookModel;
	
	public GrimoireOfEvilItemModel(ClientHandler client) {
		super(new JgModelPart[] { new JgModelPart("rightarm", 0f, -0.4f, 0f, 0f, 0f, 0f),
				new JgModelPart("leftarm", 0.08f, 0.32f, -0.18f, -0.47123882f, 0, 0),
				new JgModelPart("item", 0.56999975f, -0.64f, -0.9999994f, 
						0.6457719f, -0.7679451f, 0.0f),
				new JgModelPart("rightarmitem"), 
				new JgModelPart("all"), 
				new JgModelPart("sprint"), 
				},
				client);
		/*spBookModel = new SpBookModel(Minecraft.getInstance().getEntityModels()
				.bakeLayer(SpBookModel.LAYER_LOCATION));*/
	}

	public void tickBookData() {
		oOpen = open;
		oRot = rot;
		boolean canDo = true;
		if(canDo) {
			tRot = (float) Mth.atan2(1, 1);
			open += 0.1F;
			if (open < 0.5F || RANDOM.nextInt(40) == 0) {
				float f1 = flipT;
	
				do {
					flipT += (float) (RANDOM.nextInt(4) - RANDOM.nextInt(4));
				} while (f1 == flipT);
			}
		} else {
			//tRot += 0.02F;
			open -= 0.1F;
		}

		while (rot >= (float) Math.PI) {
			rot -= ((float) Math.PI * 2F);
		}

		while (rot < -(float) Math.PI) {
			rot += ((float) Math.PI * 2F);
		}

		while (tRot >= (float) Math.PI) {
			tRot -= ((float) Math.PI * 2F);
		}

		while (tRot < -(float) Math.PI) {
			tRot += ((float) Math.PI * 2F);
		}

		float f2;
		for (f2 = tRot - rot; f2 >= (float) Math.PI; f2 -= ((float) Math.PI * 2F)) {
		}

		while (f2 < -(float) Math.PI) {
			f2 += ((float) Math.PI * 2F);
		}

		rot += f2 * 0.4F;
		open = Mth.clamp(open, 0.0F, 1.0F);
		++time;
		oFlip = flip;
		float f = (flipT - flip) * 0.4F;
		f = Mth.clamp(f, -0.2F, 0.2F);
		flipA += (f - flipA) * 0.9F;
		flip += flipA;
		/*LogUtils.getLogger().info("Flip: " + flip + " oFlip: " + oFlip + " flipA: " + 
				flipA + " flipT: " + flipT + " open: " + open + " rot: " + rot + " tRot: " + 
				tRot);*/
	}

	public void tickAnimation(BookModel model, PoseStack matrix) {
		float f = (float) time + ClientHandler.partialTicks;

		float f1;
		for (f1 = rot - oRot; f1 >= (float) Math.PI; f1 -= ((float) Math.PI * 2F)) {
		}

		while (f1 < -(float) Math.PI) {
			f1 += ((float) Math.PI * 2F);
		}

		float f2 = oRot + f1 * ClientHandler.partialTicks;
		matrix.mulPose(Vector3f.YP.rotation(-f2));
		matrix.mulPose(Vector3f.ZP.rotationDegrees(80.0F));
		float f3 = Mth.lerp(ClientHandler.partialTicks, oFlip, flip);
		float f4 = Mth.frac(f3 + 0.25F) * 1.6F - 0.3F;
		float f5 = Mth.frac(f3 + 0.75F) * 1.6F - 0.3F;
		float f6 = Mth.lerp(ClientHandler.partialTicks, oOpen, open);
		model.setupAnim(f, Mth.clamp(f4, 0.0F, 1.0F), Mth.clamp(f5, 0.0F, 1.0F), f6);
	}
	
	public void tick(BookModel model, PoseStack matrix) {
		tickBookData();
		tickAnimation(model, matrix);
	}
	
	public void renderBook(PoseStack matrix, MultiBufferSource buffer, int light) {
		VertexConsumer vertexconsumer = new Material(TextureAtlas.LOCATION_BLOCKS, 
				new ResourceLocation(Evilord.MODID, "entity/bookmodel"))
				.buffer(buffer, RenderType::entitySolid);
		matrix.pushPose();
		translateAndRotateAndScale(parts[2].getCombined(), matrix);
		if(bookModel == null) {
			bookModel = new BookModel(Minecraft.getInstance().getEntityModels()
					.bakeLayer(ModelLayers.BOOK));
		}
		tick(bookModel, matrix);
		bookModel.render(matrix, vertexconsumer, light, OverlayTexture.NO_OVERLAY, 
				1.0F, 1.0F, 1.0F, 1.0F);
		matrix.popPose();
	}

	@Override
	public void render(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, PoseStack matrix, int light) {
		matrix.pushPose();
		// lerpTransform(matrix, light, null);
		translateAndRotateAndScale(parts[4].getCombined(), matrix);
		renderArm(player, buffer, matrix, light, HumanoidArm.LEFT, parts[1].getCombined());
		matrix.pushPose();
		translateAndRotateAndScale(parts[3].getTransform(), matrix);
		renderArm(player, buffer, matrix, light, HumanoidArm.RIGHT, parts[0].getCombined());
		renderBook(matrix, buffer, light);
		//renderItem(player, stack, buffer, matrix, light, parts[2].getCombined());
		matrix.popPose();
		matrix.popPose();
	}

	@Override
	public JgModelPart getModelPart() {
		return parts[2];
	}
	
}
