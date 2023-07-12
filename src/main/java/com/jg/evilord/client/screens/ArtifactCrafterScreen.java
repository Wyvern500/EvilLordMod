package com.jg.evilord.client.screens;

import java.util.List;

import com.jg.evilord.Evilord;
import com.jg.evilord.client.recipe.CraftingTab;
import com.jg.evilord.client.recipe.EvilordIngredient;
import com.jg.evilord.client.recipe.EvilordRecipe;
import com.jg.evilord.client.recipe.EvilordRecipeManager;
import com.jg.evilord.client.recipe.RecipeHandler;
import com.jg.evilord.client.screen.SoulEnergyCapableScreen;
import com.jg.evilord.client.screens.widgets.Button;
import com.jg.evilord.client.screens.widgets.JgListWidget;
import com.jg.evilord.client.screens.widgets.JgListWidget.JgListKey;
import com.jg.evilord.containers.ArtifactCrafterContainer;
import com.jg.evilord.registries.ItemRegistries;
import com.jg.evilord.utils.RenderUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ArtifactCrafterScreen extends SoulEnergyCapableScreen<ArtifactCrafterContainer> {

	public static ResourceLocation ARTIFACTCRAFTER = 
			new ResourceLocation(Evilord.MODID, 
					"textures/gui/container/artifact_crafter.png");
	
	private CraftingTab currentTab;
	private JgListWidget<ArtifactListKey> artifacts;
	private JgListWidget<MaterialListKey> materials;
	private JgListWidget<TabKey> tabs;
	
	public ArtifactCrafterScreen(ArtifactCrafterContainer p_97741_, Inventory p_97742_, Component p_97743_) {
		super(p_97741_, p_97742_, p_97743_);
	}
	
	@Override
	protected void init() {
		super.init();
		
		int i = this.leftPos;
		int j = this.topPos;

		this.artifacts = new JgListWidget<>(i + 8, j + 15, 64, 48, 16, ARTIFACTCRAFTER);
		this.artifacts.getSideBar().setXOffset(3);
		this.materials = new JgListWidget<>(i + 90, j + 15, 32, 48, 16, ARTIFACTCRAFTER);
		this.materials.getSideBar().setXOffset(3);
		this.tabs = new JgListWidget<>(i + 181, j + 15, 16, 48, 16, ARTIFACTCRAFTER);
		this.tabs.getSideBar().setXOffset(3);
		
		addRenderableWidget(tabs);
		addRenderableWidget(materials);
		addRenderableWidget(artifacts);
		
		for(RecipeHandler recipeHandler : EvilordRecipeManager.INSTANCE
				.getRecipeHandlers().values()) {
			tabs.addItem(new TabKey(recipeHandler.getTab()));
			/*for(EvilordRecipe recipe : recipeHandler.getRecipes().values()) {
				artifacts.addItem(new ArtifactListKey(recipe.getResultItem(), 
						recipeHandler.getTab(), recipe));
			}*/
		}
		
		Button craft = new Button(this, i + 141, j + 46, 178, 26, 26, 18, 26, ARTIFACTCRAFTER, 
				(btn) -> {
					ArtifactListKey selected = artifacts.getSelectedItem();
					if(selected != null) {
						LogUtils.getLogger().info("canCraftApple: " + EvilordRecipeManager
								.INSTANCE.getRecipeHandler(selected.getRecipeHandlerKey())
								.checkRecipe(selected.getItem(), Minecraft.getInstance()
										.player).canCraft());
						Player player = Minecraft.getInstance().player;
						// Crafting
						if(menu.getBlockEntity().canCraft(player)) {
							EvilordRecipeManager
							.INSTANCE.getRecipeHandler(selected.getRecipeHandlerKey())
								.processSoulRecipe(selected.getItem(), 
									player, menu.getData()
									.get(1), 
									menu.containerId, menu.getBlockEntity()
									.getBlockPos());	
						}
					}
				});
		addRenderableWidget(craft);
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
		RenderSystem.setShaderTexture(0, ARTIFACTCRAFTER);
		int i = this.leftPos;
		int j = this.topPos;

		this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
		
		// Tabs
		this.blit(matrixStack, i + 176, j + 10, 178, 60, 41, 58);
		
		// Soul storage
		this.blit(matrixStack, i + 143, j + 30, 178, 46, 11, 12);
		this.font.draw(matrixStack, this.menu.getData().get(1) + "", i + 149, 
				j + 36, 0xffffff);
		
		
	}
	
	@Override
	protected void renderLabels(PoseStack p_97808_, int p_97809_, int p_97810_) {
		this.font.draw(p_97808_, this.title, (float)this.titleLabelX, 
				(float)this.titleLabelY - 2, 4210752);
	    this.font.draw(p_97808_, this.playerInventoryTitle, (float)this.inventoryLabelX, 
	    		(float)this.inventoryLabelY, 4210752);
	}
	
	@Override
	public boolean mouseDragged(double p_97752_, double p_97753_, int p_97754_, double p_97755_, double p_97756_) {
		artifacts.mouseDragged(p_97752_, p_97753_, p_97754_, p_97755_, p_97756_);
		materials.mouseDragged(p_97752_, p_97753_, p_97754_, p_97755_, p_97756_);
		tabs.mouseDragged(p_97752_, p_97753_, p_97754_, p_97755_, p_97756_);
		return super.mouseDragged(p_97752_, p_97753_, p_97754_, p_97755_, p_97756_);
	}
	
	@Override
	public boolean mouseClicked(double p_97748_, double p_97749_, int p_97750_) {
		return super.mouseClicked(p_97748_, p_97749_, p_97750_);
	}
	
	public void setCurrentTab(CraftingTab tab) {
		if(tab != null) {
			if(tab != this.currentTab) {
				this.currentTab = tab;
				artifacts.clearItems();
				materials.clearItems();
				for(EvilordRecipe recipe : EvilordRecipeManager.INSTANCE
						.getRecipeHandler(tab).getRecipes().values()) {
					artifacts.addItem(new ArtifactListKey(recipe.getResultItem(), 
							tab, recipe));
				}
			}
		} else {
			artifacts.clearItems();
			materials.clearItems();
		}
	}
	
	public class ArtifactListKey extends JgListKey {

		List<EvilordIngredient> ingredients;
		ItemStack item;
		CraftingTab tab;
		
		public ArtifactListKey(ItemStack item, CraftingTab tab, 
				EvilordRecipe recipe) {
			this.item = item;
			this.tab = tab;
			this.ingredients = recipe.getIngredients();
		}
		
		@Override
		public void render(PoseStack matrix, int x, int y, 
				int mouseX, int mouseY, int index, boolean selected) {
			RenderSystem.enableBlend();
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderTexture(0, ARTIFACTCRAFTER);
			int offset = selected ? 16 : 0;
			blit(matrix, x, y, 0, 168 + offset, 16, 16);
			Minecraft.getInstance().getItemRenderer()
			.renderGuiItemDecorations(font, item, x, y);
			Minecraft.getInstance().getItemRenderer()
			.renderGuiItem(item, x, y);
		}
		
		@Override
		public void renderHovered(PoseStack matrix, int x, int y, int mouseX, int mouseY, int index, boolean selected) {
			if(mouseX > x && mouseX < x + 16 && mouseY > y && mouseY < y + 16) {
				renderTooltip(matrix, item, mouseX, mouseY);
			}
		}

		@Override
		public void onClick(double mx, double my, int index) {
			materials.clearItems();
			for(EvilordIngredient ing : ingredients) {
				materials.addItem(new MaterialListKey(ing.getItem(), ing.getCount()));
			}
		}

		@Override
		public void onUnselect(double mx, double my, int index) {
			materials.clearItems();
		}
		
		public Item getItem() {
			return item.getItem();
		}
		
		public CraftingTab getRecipeHandlerKey() {
			return tab;
		}
		
		public ItemStack getItemStack() {
			return item;
		}
		
	}
	
	public class MaterialListKey extends JgListKey {

		ItemStack stack;
		
		public MaterialListKey(Item item, int count) {
			this.stack = new ItemStack(item, count);
		}
		
		@Override
		public void render(PoseStack matrix, int x, int y, 
				int mouseX, int mouseY, int index, boolean selected) {
			RenderSystem.enableBlend();
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderTexture(0, ARTIFACTCRAFTER);
			blit(matrix, x, y, 0, 200, 16, 16);
			if(stack.getItem() != ItemRegistries.soul.get()) {
				Minecraft.getInstance().getItemRenderer()
					.renderGuiItemDecorations(font, stack, x, y);
			}
			Minecraft.getInstance().getItemRenderer().renderGuiItem(stack, x, y);
		}

		@Override
		public void renderHovered(PoseStack matrix, int x, int y, int mouseX, int mouseY, int index, boolean selected) {
			if(mouseX > x && mouseX < x + 16 && mouseY > y && mouseY < y + 16) {
				List<Component> components;
				if(stack.getItem() == ItemRegistries.soul.get()) {
					components = List.of(new TranslatableComponent(stack.getItem()
							.getDescriptionId()), 
							new TranslatableComponent("Souls: " + stack.getCount()));
				} else {
					components = List.of(new TranslatableComponent(stack.getItem()
							.getDescriptionId()));
				}
				renderTooltip(matrix, components, 
						stack.getTooltipImage(), mouseX, mouseY);
				//renderTooltip(matrix, stack, mouseX, mouseY);
			}
		}
		
		@Override
		public void onClick(double mx, double my, int index) {
			
		}

		@Override
		public void onUnselect(double mx, double my, int index) {
			
		}
		
	}
	
	public class TabKey extends JgListKey {
		
		CraftingTab tab;
		ItemStack stack;
		
		public TabKey(CraftingTab tab) {
			this.tab = tab;
			this.stack = new ItemStack(tab.getIcon().get());
		}

		@Override
		public void onClick(double mx, double my, int index) {
			setCurrentTab(tab);
		}

		@Override
		public void onUnselect(double mx, double my, int index) {
			setCurrentTab(null);
		}

		@Override
		public void render(PoseStack matrix, int x, int y, int mouseX, int mouseY, int index, boolean selected) {
			RenderSystem.enableBlend();
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderTexture(0, ARTIFACTCRAFTER);
			int offset = selected ? 16 : 0;
			blit(matrix, x, y, 0, 216 + offset, 16, 16);
			Minecraft.getInstance().getItemRenderer()
			.renderGuiItem(stack, x, y);
		}

		@Override
		public void renderHovered(PoseStack matrix, int x, int y, int mouseX, int mouseY, int index, boolean selected) {
			if(mouseX > x && mouseX < x + 16 && mouseY > y && mouseY < y + 16) {
				RenderUtils.renderTooltip(matrix, List
						.of(new TranslatableComponent(tab.getName())), mouseX, 
						mouseY);
			}
		}
		
		public CraftingTab getTab() {
			return tab;
		}
		
	}

}
