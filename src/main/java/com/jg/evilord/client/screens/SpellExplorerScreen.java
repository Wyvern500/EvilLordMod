package com.jg.evilord.client.screens;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.lwjgl.glfw.GLFW;

import com.jg.evilord.Evilord;
import com.jg.evilord.client.render.spellIcon.SpellIconRenderer;
import com.jg.evilord.containers.SpellExplorerContainer;
import com.jg.evilord.spell.Spell;
import com.jg.evilord.spell.handler.SpellsHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SpellExplorerScreen extends AbstractContainerScreen<SpellExplorerContainer> {

	public static ResourceLocation SPELLEXPLORER = 
			new ResourceLocation(Evilord.MODID, 
					"textures/gui/container/spell_explorer.png");
	private List<Spell> spells;
	private int cols = 8;
	private int cap = 32;
	private boolean click;
	
	public SpellExplorerScreen(SpellExplorerContainer p_97741_, Inventory p_97742_, Component p_97743_) {
		super(p_97741_, p_97742_, p_97743_);
		this.imageWidth = 201;
		spells = new ArrayList<>(SpellsHandler.INSTANCE.getSpells());
		//addRenderableWidget(null);
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
		RenderSystem.setShaderTexture(0, SPELLEXPLORER);
		int i = this.leftPos;
		int j = this.topPos;
		
		this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
		
		renderSpells(matrixStack, i, j, p_97789_, p_97790_);
	}

	private void renderSpells(PoseStack matrix, int i, int j, int x, int y) {
		int t = 0;
		boolean should = false;
		String name = "";
		
		for(int g = 0; g < spells.size(); g++) {
			Spell spell = spells.get(g);
			int wg = g - (t * cap);
			
			boolean hover = false;
			
			float color = 1.0f;
			int sx = (i + 8) + (wg % cols) * 16;
			int sy = (j + 12) + (int) Math.floor(wg / cols) * 16;
			
			if(x > sx && x < sx + 16 && y > sy && y < sy + 16) {
				name = spell.getName();
				should = true;
				hover = true;
				if(click) {
					color -= 0.3f;
				}
			}
			
			SpellIconRenderer icon = spell.getIcon(); 
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(color, color, color, 1.0f);
			RenderSystem.setShaderTexture(0, icon.path);
			
			this.blit(matrix, sx, sy, icon.sourceX == 0 ? 32 : 16, 
					icon.sourceY, icon.size, icon.size);
			
			if(hover) {
				RenderSystem.setShaderColor(color, color, color, 0.2f);
				this.blit(matrix, sx, sy, 0, 0, icon.size, icon.size);
			}
			
			if (wg > cap - 2) {
				t++;
			}
		}
		
		if(should) {
			this.renderTooltip(matrix, List.of(new TranslatableComponent(name)), 
					Optional.empty(), x, y);
		}
		/*for(int g = 0; g < spells.size(); g++) {
			Spell spell = spells.get(g);
			int wg = g - (t * cap);
			
			int sx = (i + 8) + (wg % cols) * 16;
			int sy = (j + 12) + (int) Math.floor(wg / cols) * 16;
			
			if(x > sx && x < sx + 16 && y > sy && y < sy + 16) {
				this.renderTooltip(matrix, List.of(new TranslatableComponent(spell.getName())), 
						Optional.empty(), x, y);
				if(click) {
					
				}
			}
			
			if (wg > cap - 2) {
				t++;
			}
		}*/
	}
	
	@Override
	public boolean mouseClicked(double x, double y, int button) {
		if(button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			click = true;
		}
		return super.mouseClicked(x, y, button);
	}
	
	@Override
	public boolean mouseReleased(double x, double y, int button) {
		if(button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			click = false;
		}
		return super.mouseReleased(x, y, button);
	}
	
}
