package com.jg.evilord.client.screens.widgets;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class TestWidget extends AbstractWidget {

	public TestWidget(int x, int y, int w, int h, Component c) {
		super(x, y, w, h, c);
	}

	@Override
	public void render(PoseStack p_93657_, int p_93658_, int p_93659_, float p_93660_) {
		if (this.visible) {
	         this.isHovered = p_93658_ >= this.x && p_93659_ >= this.y && p_93658_ < this.x + this.width && p_93659_ < this.y + this.height;
	    }
	}
	
	@Override
	protected void renderBg(PoseStack p_93661_, Minecraft p_93662_, int p_93663_, int p_93664_) {
		super.renderBg(p_93661_, p_93662_, p_93663_, p_93664_);
	}
	
	@Override
	public void updateNarration(NarrationElementOutput p_169152_) {
		
	}

}
