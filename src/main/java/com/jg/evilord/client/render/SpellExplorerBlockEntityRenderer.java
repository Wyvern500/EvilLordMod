package com.jg.evilord.client.render;

import com.jg.evilord.entities.block.SpellExplorerBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class SpellExplorerBlockEntityRenderer implements BlockEntityRenderer<SpellExplorerBlockEntity> {

	private BlockEntityRendererProvider.Context ctx;
	
	public SpellExplorerBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		this.ctx = ctx;
	}
	
	@Override
	public void render(SpellExplorerBlockEntity entity, float p_112308_, PoseStack matrix,
			MultiBufferSource buffer, int p_112311_, int p_112312_) {
		/*entity.getLevel().addParticle(ParticleTypes.AMBIENT_ENTITY_EFFECT, 
				entity.getBlockPos().getX() + 0.5f, entity.getBlockPos().getY(), 
				entity.getBlockPos().getZ() + 0.5f, Math.random() * 1f, -1, Math.random() * 1f);
		*/
		/*Minecraft.getInstance().getBlockEntityRenderDispatcher()
			.renderItem(p_112307_, p_112309_, p_112310_, p_112311_, p_112312_);
		Minecraft.getInstance().getBlockRenderer()
			.renderSingleBlock(Blocks.ACACIA_BUTTON.defaultBlockState(), 
					p_112309_, p_112310_, OverlayTexture.NO_OVERLAY, 0, EmptyModelData.INSTANCE);
		RenderUtils.renderBlock(p_112310_.getBuffer(RenderType.lightning()), p_112309_, 
				p_112307_.getBlockPos().above().getX(), p_112307_.getBlockPos().above().getY(), 
				p_112307_.getBlockPos().above().getZ(), 1.0f);*/
	}

}
