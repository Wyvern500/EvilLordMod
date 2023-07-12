package com.jg.evilord.client.screens;

import com.jg.evilord.Evilord;
import com.jg.evilord.containers.TestGeneratorContainer;
import com.jg.evilord.network.server.MergeSoulEnergyMessage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TestGeneratorScreen extends AbstractContainerScreen<TestGeneratorContainer> {

	public static ResourceLocation TESTGENERATOR = 
			new ResourceLocation(Evilord.MODID, 
					"textures/gui/container/soul_generator_test.png");
	
	public TestGeneratorScreen(TestGeneratorContainer p_97741_, Inventory p_97742_, Component p_97743_) {
		super(p_97741_, p_97742_, p_97743_);
	}
	
	@Override
	protected void init() {
		super.init();
		
		int i = leftPos;
		int j = topPos;
		
		EditBox value = new EditBox(font, i + 7, j + 14, 37, 13, new TranslatableComponent("hola"));
		value.setValue("20");
		addRenderableWidget(value);
		
		Button insert = new Button(i + 7, j + 30, 34, 14, new TranslatableComponent("Insert"), (btn) -> {
			String text = value.getValue();
			int val = 0;
			if(text.matches("[0-9]+")) {
				val = Integer.parseInt(text);
			} else {
				val = 0;
			}
			Evilord.channel.sendToServer(new MergeSoulEnergyMessage(true, 
					val, menu.getBlockEntity().getBlockPos()));
		});
		addRenderableWidget(insert);
		
		Button extract = new Button(i + 43, j + 30, 34, 14, new TranslatableComponent("Extract"), (btn) -> {
			String text = value.getValue();
			int val = 0;
			if(text.matches("[0-9]+")) {
				val = Integer.parseInt(text);
			} else {
				val = 0;
			}
			Evilord.channel.sendToServer(new MergeSoulEnergyMessage(false, 
					val, menu.getBlockEntity().getBlockPos()));
		});
		addRenderableWidget(extract);
	}

	@Override
	public void render(PoseStack p_97795_, int p_97796_, int p_97797_, float p_97798_) {
		super.render(p_97795_, p_97796_, p_97797_, p_97798_);
	}
	
	@Override
	protected void renderBg(PoseStack matrixStack, float p_97788_, int p_97789_, int p_97790_) {
		super.renderBackground(matrixStack);
		RenderSystem.enableBlend();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, TESTGENERATOR);
		int i = this.leftPos;
		int j = this.topPos;

		this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
	}
	
	@Override
	protected void renderLabels(PoseStack p_97808_, int p_97809_, int p_97810_) {
		super.renderLabels(p_97808_, p_97809_, p_97810_);
	}

}
