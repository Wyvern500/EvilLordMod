package com.jg.evilord.client.render;

import com.jg.evilord.entities.blockentities.ArtifactCrafterBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class ArtifactCrafterBlockEntityRenderer implements BlockEntityRenderer<ArtifactCrafterBlockEntity> {
	
	public ArtifactCrafterBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		
	}
	
	@Override
	public void render(ArtifactCrafterBlockEntity entity, float p_112308_, PoseStack matrix,
			MultiBufferSource buffer, int p_112311_, int p_112312_) {
	}

}
