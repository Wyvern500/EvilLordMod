package com.jg.evilord.client.screens;

import com.jg.evilord.Evilord;
import com.jg.evilord.containers.SoulManipulatorContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SoulManipulatorScreen extends AbstractContainerScreen<SoulManipulatorContainer> {

	public static ResourceLocation SOULMANIPULATOR = 
			new ResourceLocation(Evilord.MODID, 
					"textures/gui/container/soul_manipulator.png");
	
	public SoulManipulatorScreen(SoulManipulatorContainer p_97741_, Inventory p_97742_, Component p_97743_) {
		super(p_97741_, p_97742_, p_97743_);
	}

	@Override
	public void render(PoseStack p_97795_, int p_97796_, int p_97797_, float p_97798_) {
		super.render(p_97795_, p_97796_, p_97797_, p_97798_);
		
		this.renderTooltip(p_97795_, p_97796_, p_97797_);
	}
	
	@Override
	protected void renderBg(PoseStack matrixStack, float p_97788_, int p_97789_, int p_97790_) {
		RenderSystem.enableBlend();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, SOULMANIPULATOR);
		int i = this.leftPos;
		int j = this.topPos;

		this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
	}

}
