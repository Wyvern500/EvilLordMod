package com.jg.evilord.client.screens;

import org.lwjgl.system.MathUtil;

import com.jg.evilord.Evilord;
import com.jg.evilord.containers.SoulStorageContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class SoulStorageScreen extends AbstractContainerScreen<SoulStorageContainer> {

	public static ResourceLocation SOULSTORAGE = 
			new ResourceLocation(Evilord.MODID, 
					"textures/gui/container/soul_storage.png");
	
	public SoulStorageScreen(SoulStorageContainer p_97741_, Inventory p_97742_, Component p_97743_) {
		super(p_97741_, p_97742_, p_97743_);
	}

	@Override
	protected void init() {
		super.init();
	}
	
	@Override
	public void render(PoseStack p_97795_, int p_97796_, int p_97797_, float p_97798_) {
		super.render(p_97795_, p_97796_, p_97797_, p_97798_);
		
		this.renderTooltip(p_97795_, p_97796_, p_97797_);
	}
	
	@Override
	protected void renderBg(PoseStack matrixStack, float p_97788_, int p_97789_, int p_97790_) {
		super.renderBackground(matrixStack);
		RenderSystem.enableBlend();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, SOULSTORAGE);
		int i = this.leftPos;
		int j = this.topPos;

		this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
		
		// Render Bar Progress
		
		float progress = ((SoulStorageContainer)menu).getData().get(0) / 300.0f;
		float offset = (int)Mth.lerp(progress, 0, 42);
		this.blit(matrixStack, i + 81, j + 65 - (int) offset, 192, 42 - 
				(int) offset, 12, (int) offset);
	}
	
	@Override
	protected void renderLabels(PoseStack p_97808_, int p_97809_, int p_97810_) {
		/*this.font.draw(p_97808_, this.title, (float)this.titleLabelX, 
				(float)this.titleLabelY - 2, 4210752);
	    this.font.draw(p_97808_, this.playerInventoryTitle, (float)this.inventoryLabelX, 
	    		(float)this.inventoryLabelY, 4210752);*/
		super.renderLabels(p_97808_, p_97809_, p_97810_);
	}
	
}
