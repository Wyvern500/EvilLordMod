package com.jg.evilord.client.render;

import com.jg.evilord.Evilord;
import com.jg.evilord.client.model.entity.DeadMassModel;
import com.jg.evilord.entities.DeadMassEntity;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class DeadMassRenderer extends MobRenderer<DeadMassEntity, DeadMassModel>{

	public static final ResourceLocation TEXTURE = new ResourceLocation(Evilord.MODID, 
			"textures/entity/deadmass.png");
	
	public DeadMassRenderer(Context p_174304_) {
		super(p_174304_, new DeadMassModel(DeadMassModel
				.createBodyLayer().bakeRoot()), 1.0f);
	}

	@Override
	public void render(DeadMassEntity p_115455_, float p_115456_, float p_115457_, PoseStack p_115458_,
			MultiBufferSource p_115459_, int p_115460_) {
		super.render(p_115455_, p_115456_, p_115457_, p_115458_, p_115459_, p_115460_);
		/*RenderUtils.renderBlock(p_115459_.getBuffer(RenderType.lineStrip()), p_115458_, 
				(float)p_115455_.position().x + 1f, (float)p_115455_.position().y+4f, 
				(float)p_115455_.position().z, 1f, 1f, 1f, 1.0f, 1.0f, 1.0f, 1.0f);*/
		/*Minecraft.getInstance().level.addParticle(ParticleTypes.LAVA, 
				p_115455_.position().x, p_115455_.position().y + 1f, p_115455_.position().z, 
				0, -0f, 0);*/
	}
	
	@Override
	public ResourceLocation getTextureLocation(DeadMassEntity p_115812_) {
		return TEXTURE;
	}
	
}
