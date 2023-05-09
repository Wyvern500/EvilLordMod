package com.jg.evilord.client.render;

import com.jg.evilord.Evilord;
import com.jg.evilord.client.model.entity.BasicMinionSkeletonModel;
import com.jg.evilord.entities.MinionSkeleton;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BasicMinionSkeletonRenderer extends MobRenderer<MinionSkeleton, BasicMinionSkeletonModel<MinionSkeleton>>{

	public static final ResourceLocation TEXTURE = new ResourceLocation(Evilord.MODID, 
			"textures/entity/evilord.png");
	
	public BasicMinionSkeletonRenderer(Context p_174304_) {
		super(p_174304_, new BasicMinionSkeletonModel<MinionSkeleton>(BasicMinionSkeletonModel
				.createBodyLayer().bakeRoot()), 1.0f);
	}

	@Override
	public ResourceLocation getTextureLocation(MinionSkeleton p_115812_) {
		return TEXTURE;
	}
	
}
