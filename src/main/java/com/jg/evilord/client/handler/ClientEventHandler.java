package com.jg.evilord.client.handler;

import java.util.Map.Entry;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.lwjgl.glfw.GLFW;

import com.jg.evilord.Evilord;
import com.jg.evilord.animation.model.ModelsHandler;
import com.jg.evilord.client.model.entity.GrimoireOfEvilModel;
import com.jg.evilord.client.models.items.GrimoireOfEvilItemModel;
import com.jg.evilord.client.render.BasicMinionSkeletonRenderer;
import com.jg.evilord.client.render.DeadMassRenderer;
import com.jg.evilord.client.render.SpellExplorerBlockEntityRenderer;
import com.jg.evilord.client.screens.AnimationScreen;
import com.jg.evilord.client.screens.ModelPartsScreen;
import com.jg.evilord.client.screens.SoulManipulatorScreen;
import com.jg.evilord.client.screens.SpellExplorerScreen;
import com.jg.evilord.event.client.RegisterEasingsEvent;
import com.jg.evilord.event.client.RegisterModelEvent;
import com.jg.evilord.item.GrimoireOfEvilItem;
import com.jg.evilord.item.SoulContainerItem;
import com.jg.evilord.network.common.SyncBlockEntityMessage;
import com.jg.evilord.network.server.ProcessConnectionManipulatorWandMessage;
import com.jg.evilord.network.server.SpawnSkeletonMessage;
import com.jg.evilord.registries.BlockEntityRegistries;
import com.jg.evilord.registries.ContainerRegistries;
import com.jg.evilord.registries.EntityRegistries;
import com.jg.evilord.registries.ItemRegistries;
import com.jg.evilord.spell.handler.SpellsHandler;
import com.jg.evilord.utils.NBTUtils;
import com.jg.evilord.utils.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.logging.LogUtils;
import com.mojang.math.Matrix4f;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class ClientEventHandler {

	private static Minecraft mc = Minecraft.getInstance();
	private static ClientHandler client = new ClientHandler();

	public static final KeyMapping SWITCH = new KeyMapping("key.oldguns.switch", GLFW.GLFW_KEY_C, Evilord.MODID);
	public static final KeyMapping DISPLAY = new KeyMapping("key.oldguns.display", GLFW.GLFW_KEY_KP_ADD, Evilord.MODID);
	
	public static final KeyMapping LEFT = new KeyMapping("key.oldguns.left", GLFW.GLFW_KEY_LEFT, Evilord.MODID);
	public static final KeyMapping UP = new KeyMapping("key.oldguns.up", GLFW.GLFW_KEY_UP, Evilord.MODID);
	public static final KeyMapping DOWN = new KeyMapping("key.oldguns.down", GLFW.GLFW_KEY_DOWN, Evilord.MODID);
	public static final KeyMapping RIGHT = new KeyMapping("key.oldguns.right", GLFW.GLFW_KEY_RIGHT, Evilord.MODID);
	public static final KeyMapping N = new KeyMapping("key.oldguns.n", GLFW.GLFW_KEY_N, Evilord.MODID);
	public static final KeyMapping M = new KeyMapping("key.oldguns.m", GLFW.GLFW_KEY_M, Evilord.MODID);
	public static final KeyMapping H = new KeyMapping("key.oldguns.h", GLFW.GLFW_KEY_H, Evilord.MODID);
	public static final KeyMapping J = new KeyMapping("key.oldguns.x", GLFW.GLFW_KEY_J, Evilord.MODID);
	public static final KeyMapping MINUS = new KeyMapping("key.oldguns.-", GLFW.GLFW_KEY_MINUS, Evilord.MODID);
	public static final KeyMapping Z = new KeyMapping("key.oldguns.z", GLFW.GLFW_KEY_Z, Evilord.MODID);
	public static final KeyMapping X = new KeyMapping("key.oldguns.x", GLFW.GLFW_KEY_X, Evilord.MODID);
	
	private static boolean shift = false;
	
	public static void setup() {
		ClientsHandler.register(mc.getUser(), client);
		
		MenuScreens.register(ContainerRegistries.soulContainerManipulator.get(), SoulManipulatorScreen::new);
		MenuScreens.register(ContainerRegistries.spellExplorer.get(), SpellExplorerScreen::new);
		
		ClientRegistry.registerKeyBinding(Z);
		ClientRegistry.registerKeyBinding(X);
		
		EntityRenderers.register(EntityRegistries.BASICSKELETONMINION.get(),
				man -> new BasicMinionSkeletonRenderer(man));
		EntityRenderers.register(EntityRegistries.DeadMass.get(),
				man -> new DeadMassRenderer(man));
		BlockEntityRenderers.register(BlockEntityRegistries.spellExplorer.get(),
				man -> new SpellExplorerBlockEntityRenderer(man));
	}

	public static void registerModEventListeners(IEventBus bus) {
		bus.addListener(ClientEventHandler::bakeModels);
		bus.addListener(ClientEventHandler::addCustomTexturesPre);
	}

	public static void registerForgeEventListeners(IEventBus bus) {
		// Input Listeners
		bus.addListener(ClientEventHandler::handleKeyboard);
		bus.addListener(ClientEventHandler::handleMouse);

		// Basic events
		bus.addListener(ClientEventHandler::clientTick);
		bus.addListener(ClientEventHandler::renderPlayerHand);
		bus.addListener(ClientEventHandler::onKillEntity);
		bus.addListener(ClientEventHandler::addToTooltip);
		bus.addListener(ClientEventHandler::renderLast);
		bus.addListener(ClientEventHandler::renderStage);
		bus.addListener(ClientEventHandler::registerEntityModels);
		
		// Custom events
		bus.addListener(ClientEventHandler::registerJgModels);
		bus.addListener(ClientEventHandler::registerEasings);
	}

	// Baked Model Event | Uso este evento para lanzar otros eventos que ocupo

	private static void bakeModels(ModelBakeEvent e) {
		if (!ModelsHandler.getInit()) {
			ModelsHandler.setInit(true);

			MinecraftForge.EVENT_BUS.start();
			MinecraftForge.EVENT_BUS.post(new RegisterModelEvent());
			MinecraftForge.EVENT_BUS.post(new RegisterEasingsEvent());
		}
	}

	// Client | Aqui van cosas relacionadas con el cliente

	private static void clientTick(ClientTickEvent e) {
		//client.tick();
		if (e.phase == Phase.START) {
			Player player = mc.player;
			if (player != null) {
				ItemStack stack = player.getMainHandItem();
				
				if(stack.getItem() instanceof GrimoireOfEvilItem) {
					client.tick();
					client.selectJgModel();
				}
			}
		}
		// EntityType.AXOLOTL.create(Minecraft.getInstance().player.level).getAttribute(Attributes.);
	}

	private static void onKillEntity(LivingDeathEvent e) {
		Entity killer = e.getSource().getDirectEntity();
		if (killer instanceof Player) {
			Player player = (Player) killer;
			/*
			 * for(Entry<BlockPos, BlockEntity> entry : player.level.getChunk(0,
			 * 0).getBlockEntities().entrySet()) { entry.getValue(). }
			 */
			LivingEntity killed = e.getEntityLiving();
			Evilord.channel.sendToServer(new SpawnSkeletonMessage());
			Logger.getGlobal().info("Killing on client");
		}
	}

	private static void registerJgModels(RegisterModelEvent e) {
		e.register(ItemRegistries.grimoireOfEvil.get(), new GrimoireOfEvilItemModel(client));
	}

	private static void registerEasings(RegisterEasingsEvent e) {
		e.register("easeInSine", (x) -> (float)(1 - Math.cos((x * Math.PI) / 2)));
		e.register("easeOutSine", (x) -> (float)(Math.sin((x * Math.PI) / 2)));
		e.register("easeInOutSine", (x) -> (float)(-(Math.cos(Math.PI * x) - 1) / 2));
		
		e.register("easeInQuad", (x) -> (float)(x * x));
		e.register("easeOutQuad", (x) -> (float)(1 - (1 - x) * (1 - x)));
		e.register("easeInOutQuad", (x) -> (float)(x < 0.5 ? 2 * x * x : 1 - Math.pow(-2 * x + 2, 2) / 2));
		
		e.register("easeInCubic", (x) -> (float)(x * x * x));
		e.register("easeOutCubic", (x) -> (float)(1 - Math.pow(1 - x, 3)));
		e.register("easeInOutCubic", (x) -> (float)(x < 0.5 ? 4 * x * x * x : 1 - Math.pow(-2 * x + 2, 3) / 2));
		
		e.register("easeInQuart", (x) -> (float)(x * x * x * x));
		e.register("easeOutQuart", (x) -> (float)(1 - Math.pow(1 - x, 4)));
		e.register("easeInOutQuart", (x) -> (float)(x < 0.5 ? 8 * x * x * x * x : 1 - Math.pow(-2 * x + 2, 4) / 2));
		
		e.register("easeInQuint", (x) -> (float)(x * x * x * x * x));
		e.register("easeOutQuint", (x) -> (float)(1 - Math.pow(1 - x, 5)));
		e.register("easeInOutQuint", (x) -> (float)(x < 0.5 ? 16 * x * x * x * x * x : 1 - Math.pow(-2 * x + 2, 5) / 2));
		
		e.register("easeInExpo", (x) -> (float)(x == 0 ? 0 : Math.pow(2, 10 * x - 10)));
		e.register("easeOutExpo", (x) -> (float)(x == 1 ? 1 : 1 - Math.pow(2, -10 * x)));
		e.register("easeInOutExpo", (x) -> (float)(x == 0
				  ? 0
				  : x == 1
				  ? 1
			      : x < 0.5 ? Math.pow(2, 20 * x - 10) / 2
				  : (2 - Math.pow(2, -20 * x + 10)) / 2));
		
		e.register("easeInCirc", (x) -> (float)(1 - Math.sqrt(1 - Math.pow(x, 2))));
		e.register("easeOutCirc", (x) -> (float)(Math.sqrt(1 - Math.pow(x - 1, 2))));
		e.register("easeInOutCirc", (x) -> (float)(x < 0.5
				  ? (1 - Math.sqrt(1 - Math.pow(2 * x, 2))) / 2
						  : (Math.sqrt(1 - Math.pow(-2 * x + 2, 2)) + 1) / 2));
		
		e.register("easeInBack", (x) -> (float)(2.70158 * x * x * x - 1.70158f * x * x));
		e.register("easeOutBack", (x) -> (float)(1 + 2.70158 * Math.pow(x - 1, 3) + 1.70158 * Math.pow(x - 1, 2)));
		e.register("easeInOutBack", (x) -> (float)(x < 0.5
				  ? (Math.pow(2 * x, 2) * ((2.5949095f + 1) * 2 * x - 2.5949095f)) / 2
						  : (Math.pow(2 * x - 2, 2) * ((2.5949095f + 1) * (x * 2 - 2) + 2.5949095f) + 2) / 2));
		
		e.register("easeInElastic", (x) -> (float)(x == 0
				  ? 0
				  : x == 1
				  ? 1
				  : -Math.pow(2, 10 * x - 10) * Math.sin((x * 10 - 10.75) 
						  * ((2 * Math.PI) / 3))));
		e.register("easeOutElastic", (x) -> (float)(x == 0
				  ? 0
				  : x == 1
				  ? 1
				  : Math.pow(2, -10 * x) * Math.sin((x * 10 - 0.75f) * 
						  ((2 * Math.PI) / 3)) + 1));
		e.register("easeInOutElastic", (x) -> (float)(x == 0
				  ? 0
				  : x == 1
				  ? 1
				  : x < 0.5
				  ? -(Math.pow(2, 20 * x - 10) * Math.sin((20 * x - 11.125) * 
						  ((2 * Math.PI) / 4.5))) / 2
				  : (Math.pow(2, -20 * x + 10) * Math.sin((20 * x - 11.125) * 
						  ((2 * Math.PI) / 4.5))) / 2 + 1));
		
		e.register("easeInBounce", (x) -> (float)(1 - e.getEasing("easeOutBounce").get(1 - x)));
		e.register("easeOutBounce", (x) -> {
			float n1 = 7.5625f;
			float d1 = 2.75f;
			
			if (x < 1 / d1) {
			    return n1 * x * x;
			} else if (x < 2 / d1) {
			    return (float)(n1 * (x -= 1.5 / d1) * x + 0.75);
			} else if (x < 2.5 / d1) {
			    return (float)(n1 * (x -= 2.25 / d1) * x + 0.9375);
			} else {
			    return (float)(n1 * (x -= 2.625 / d1) * x + 0.984375);
			}
		});
		e.register("easeInOutBounce", (x) -> (float)(x < 0.5
				  ? (1 - e.getEasing("easeOutBounce").get(1 - 2 * x)) / 2
						  : (1 + e.getEasing("easeOutBounce").get(2 * x - 1)) / 2));
		LogUtils.getLogger().info("Registering easings");
	}

	// Client - Item

	private static void addToTooltip(ItemTooltipEvent e) {
		if (shift) {
			ItemStack stack = e.getItemStack();
			if (stack.getItem() instanceof SoulContainerItem) {
				SoulContainerItem item = (SoulContainerItem) stack.getItem();
				e.getToolTip().add(new TranslatableComponent(
						"Souls: " + NBTUtils.getSouls(stack) + "/" + item.getTier().getMaxSouls()));
				e.getToolTip().add(new TranslatableComponent("Container Tier: " + item.getTier().ordinal()));
			} else if (!(stack.getItem() instanceof BlockItem) && !(stack.getItem() instanceof SpawnEggItem)) {
				if (NBTUtils.hasContainer(stack)) {
					e.getToolTip().add(new TranslatableComponent(
							"Souls: " + NBTUtils.getSouls(stack) + "/" + NBTUtils.getMaxSoulsForTier(stack)));
					e.getToolTip()
							.add(new TranslatableComponent("Container Tier: " + NBTUtils.getContainerTier(stack)));
				} else {
					e.getToolTip().add(new TranslatableComponent("Has Container: " + NBTUtils.hasContainer(stack)));
				}
			}
		}
	}

	// Client - Render | Coasas que tienen que ver con renderizado

	private static void registerEntityModels(RegisterLayerDefinitions e) {
		e.registerLayerDefinition(GrimoireOfEvilModel.LAYER_LOCATION, 
				() -> GrimoireOfEvilModel.createBodyLayer());
	}
	
	// Texture registering
	
	private static void addCustomTexturesPre(TextureStitchEvent.Pre e) {
		LogUtils.getLogger().info("TextureStitchEvent");
		if(e.getAtlas().location() == TextureAtlas.LOCATION_BLOCKS) {
			e.addSprite(new ResourceLocation(Evilord.MODID, "entity/bookmodel"));
			LogUtils.getLogger().info("Adding custom textures");
		}
	}
	
	private static void renderPlayerHand(RenderHandEvent e) {
		Player player = mc.player;
		if (player != null) {
			ClientHandler.partialTicks = e.getPartialTicks();
			if (e.getHand() == InteractionHand.MAIN_HAND) {
				ItemStack stack = player.getMainHandItem();
				if (stack.getItem() instanceof GrimoireOfEvilItem) {
					e.setCanceled(true);
					client.render(e.getPoseStack(), e.getMultiBufferSource(), e.getPackedLight());
				}
			}
		}
	}

	private static void renderLast(RenderLevelLastEvent e) {
		Minecraft minecraft = Minecraft.getInstance();
		Player player = minecraft.player;
		Level world = minecraft.level;
		float pitch = player.getXRot();
		float yaw = player.getYRot();
		
		RenderUtils.renderBeam(e.getPoseStack(), mc.renderBuffers().bufferSource()
				.getBuffer(RenderType.lightning()), player.position(), 
				5, yaw, pitch, 5, 5);
		/*HitResult hitresult = mc.hitResult;
	    if (hitresult != null && hitresult.getType() == HitResult.Type.BLOCK) {
	         BlockPos blockpos1 = ((BlockHitResult)hitresult).getBlockPos();
	         BlockState blockstate = mc.level.getBlockState(blockpos1);
	         LogUtils.getLogger().info(blockstate.getBlock().getRegistryName()
	        		 .toString());
	         RenderUtils.renderBlock(mc.renderBuffers().bufferSource()
	 				.getBuffer(RenderType.lines()), e.getPoseStack(), blockpos1, 1.0f);
	    }*/
		
	}

	private static void renderStage(RenderLevelStageEvent e) {
		if (e.getStage() != RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS)
			return;
		try {

			Minecraft minecraft = Minecraft.getInstance();
			Player player = minecraft.player;
			Level world = minecraft.level;

			// Obtener la posición del jugador
			double playerX = player.getX() + 0.5f;
			double playerY = player.getY();
			double playerZ = player.getZ() + 0.5f;
			
			// Obtener la dirección en la que el jugador está mirando
			float pitch = player.getXRot();
			float yaw = player.getYRot();
			Vec3 direction = Vec3.directionFromRotation(pitch, yaw);
			
			// Definir las coordenadas del rayo del faro
			double rayLength = 25.0;
			float startX = (float) (playerX + (direction.x));
			float startY = (float) (playerY);// + (player.getEyeHeight()));
			float startZ = (float) (playerZ + (direction.z));
			float endX = (float) (startX + (direction.x * rayLength));
			float endY = (float) (startY + (direction.y * rayLength));
			float endZ = (float) (startZ + (direction.z * rayLength));
			
			VertexConsumer buffer = mc.renderBuffers().bufferSource()
					.getBuffer(RenderUtils.TEST);
			float x = 0 + 0.5f;
			float y = -59 + 0.5f;
			float z = 0 + 0.5f;
			float ex = -14 - 0.5f;
			float ey = -58 - 0.5f;
			float ez = -12 - 0.5f;
			float w = 0.5f;
			float h = 0.5f;
			float d = 0.5f;
			float r = 1.0f;
			float g = 1.0f;
			float b = 1.0f;
			float a = 1.0f;
			float n1 = 1.0f;
			float n2 = 1.0f;
			float n3 = 1.0f;

			PoseStack matrix = e.getPoseStack();

			matrix.pushPose();

			Matrix4f matrix4f = RenderUtils.setup(matrix);
			
			y += 4f;
			ey += 4f;
			
			// UP
			buffer.vertex(matrix4f, w + x, h + y, -d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, w + ex, h + y, -d + ez).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, -w + ex, h + y, d + ez).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, -w + x, h + y, d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			/*
				Aqui primero empieza por el vertice de abajo a la izquierda
			*/
			// DOWN
			buffer.vertex(matrix4f, -w + ex, -h + y, d + ez).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, w + ex, -h + y, -d + ez).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, w + x, -h + y, -d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, -w + x, -h + y, d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			
			// LEFT
			/*
			 El vertice 1 define el punto de abajo a la izquierda, el 2 define 
			 el punto de abajo a la derecha, el 3 el punto de arriba a la derecha 
			 y el 4 define el punto de arriba a la izquierda.
			 */
			buffer.vertex(matrix4f, w + x, -h + y, -d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, w + ex, -h + y, -d + ez).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, w + ex, h + y, -d + ez).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, w + x, h + y, -d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();

			// RIGHT
			buffer.vertex(matrix4f, -w + ex, -h + y, d + ez).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, -w + x, -h + y, d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, -w + x, h + y, d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, -w + ex, h + y, d + ez).color(r, g, b, a).normal(n1, n2, n3).endVertex();

			// BACK
			buffer.vertex(matrix4f, -w + ex, -h + y, d + ez).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, -w + ex, h + y, d + ez).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, w + ex, h + y, -d + ez).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, w + ex, -h + y, -d + ez).color(r, g, b, a).normal(n1, n2, n3).endVertex();

			// FRONT
			buffer.vertex(matrix4f, w + x, -h + y, -d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, w + x, h + y, -d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, -w + x, h + y, d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();
			buffer.vertex(matrix4f, -w + x, -h + y, d + z).color(r, g, b, a).normal(n1, n2, n3).endVertex();

			matrix.popPose();

			RenderUtils.renderBlock(mc.renderBuffers().bufferSource()
					.getBuffer(RenderType.lightning()), matrix, x, y, z, -10, 0.5f, 0.5f, a);
			
			client.renderWorld(matrix);
			
			/*
			 * RenderUtils.renderBlockWithTexture(Tesselator.getInstance().getBuilder(),
			 * e.getPoseStack(), new ResourceLocation("textures/block/spruce_planks.png"),
			 * new BlockPos(0, -59, 0), 1f);
			 */
			/*
			 * RenderUtils.renderBlock(mc.renderBuffers().bufferSource()
			 * .getBuffer(RenderType.lineStrip()), e.getPoseStack(), new BlockPos(0, -59,
			 * 0), 0.5f);
			 */

			/*matrix.pushPose();
			
			RenderUtils.renderFromTo(mc.renderBuffers().bufferSource()
					.getBuffer(RenderUtils.TEST), matrix, new BlockPos(startX, 
							startY, startZ), 
						new BlockPos(endX, endY, endZ), 1.0f);
			
			
			RenderUtils.renderFromTo(mc.renderBuffers().bufferSource()
					.getBuffer(RenderUtils.TEST), matrix, (float)startX, 
					(float)startY, (float)startZ, 
					(float)endX, (float)endZ, 0.5f, 0.5f, 0.5f, 1.0f);
			
			matrix.popPose();*/
			
			
			
		} catch (IllegalStateException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
	}

	// Input | Cosas relacionadas con manejar las input

	private static void handleMouse(InputEvent.RawMouseEvent e) {
		client.onClick(e.getButton(), e.getAction());
		if (e.getAction() == GLFW.GLFW_PRESS) {
			if (e.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
				Player player = Minecraft.getInstance().player;
				if(player == null) return;
				//Logger.getGlobal().info("Souls: " + NBTUtils.getSouls(player.getMainHandItem()));
				if (player.getMainHandItem().getItem() instanceof GrimoireOfEvilItem) {
					
					// Testing
					
					LogUtils.getLogger().info("Starting block search");
					
					int areaDiameter = 5;
					for(int x = (int)player.getX(); x < player.getX() + (areaDiameter * 2); x++) {
						for(int z = (int)player.getZ(); z < player.getZ() + (areaDiameter * 2); z++) {
							BlockPos pos = new BlockPos(x, player.getY(), z);
							Block block = player.level.getBlockState(pos).getBlock();
							if(block != Blocks.AIR) {
								if(block instanceof BaseEntityBlock && block
										.getRegistryName().toString()
										.contains("spell_explorer")) {
									LogUtils.getLogger().info("Block: " + block.getRegistryName()
										.toString());
									CompoundTag nbt = new CompoundTag();
									nbt.putInt("test2", 10);
									Evilord.channel.sendToServer(
											new SyncBlockEntityMessage(pos, nbt));
								}
							}
						}
					}
					
					// End of testing block
					
					// if(client.getJgModel().)
					client.shoot(player.getMainHandItem(), player);
					//(Minecraft.getInstance().options.keyDrop.getKey().getValue())
				}
			} else if(e.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT){
				Player player = Minecraft.getInstance().player;
				if(player == null) return;
				
				if (player.getMainHandItem().getItem() == ItemRegistries
						.connectionManipulatorWand.get()) {
				    HitResult hitresult = mc.hitResult;
				    if (hitresult != null && hitresult.getType() == HitResult.Type.BLOCK) {
				         BlockPos blockpos1 = ((BlockHitResult)hitresult).getBlockPos();
				         BlockState blockstate = mc.level.getBlockState(blockpos1);
				         Evilord.channel.sendToServer(
				        		 new ProcessConnectionManipulatorWandMessage(
				        				 blockpos1.getX(), blockpos1.getY(), blockpos1.getZ(),
				        				 0, 0, 0));
				         LogUtils.getLogger().info(blockstate.getBlock().getRegistryName()
				        		 .toString());
				    }
					//Minecraft.getInstance().gameRenderer.getMainCamera().getBlockPosition()
					
					//CompoundTag nbt = player.level.getBlockEntity(player.blockPosition())
					//		.serializeNBT();
					//nbt.put(, nbt)
				}
			}
		}
	}

	private static void handleKeyboard(InputEvent.KeyInputEvent e) {
		Player player = Minecraft.getInstance().player;
		if (player == null)
			return;

		if (e.getAction() == GLFW.GLFW_PRESS) {
			if (e.getKey() == GLFW.GLFW_KEY_B) {
				Skeleton sk = EntityType.SKELETON.create(player.level);
				for (Entry<ResourceKey<Attribute>, Attribute> ae : Registry.ATTRIBUTE.entrySet()) {
					if (ae.getValue().getDescriptionId().contains("attribute")) {
						// Logger.getGlobal().info("Key: " + ae.getKey().toString() + " value: " +
						// ae.getValue().getDescriptionId());
						//Logger.getGlobal().info("Value: " + sk.getAttributeBaseValue(ae.getValue()));
					}
				}

				// Evilord.channel.sendToServer(new SpawnSkeletonMessage());
			} else if (e.getKey() == GLFW.GLFW_KEY_LEFT_SHIFT) {
				shift = true;
			}
		}
		if (e.getAction() == GLFW.GLFW_RELEASE) {
			client.onKeyboard(e.getKey(), false);
			if (e.getKey() == GLFW.GLFW_KEY_LEFT_SHIFT) {
				shift = false;
			}
		}
		if(e.getAction() == GLFW.GLFW_PRESS) {
			client.onKeyboard(e.getKey(), true);
			//Evilord.channel.sendToServer(new DoSomethingMessage());
			//p.level.addParticle(ParticleTypes.ANGRY_VILLAGER.getType(), p.getX(), 
			//		p.getY(), p.getZ(), 0.1f, 0, 0);
			//Evilord.sendToAllAlt(new DoSomethingMessage());
			/*p.getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(cap -> {
				LogUtils.getLogger().info(cap.getMana() + "");
			});
			LogUtils.getLogger().info("Mana: " + client.mana);*/
		}
		
		Screen screen = mc.screen;
		if (screen == null || screen instanceof AnimationScreen || screen instanceof ModelPartsScreen) {
			boolean animEditFocused = false;
			if (screen instanceof AnimationScreen) {
				AnimationScreen animScreen = (AnimationScreen)screen;
				for(EditBox edit : animScreen.getEditBoxes()) {
					if(edit.isFocused()) {
						animEditFocused = true;
						break;
					}
				}
			}
			if (animEditFocused)
				return;
			if(e.getAction() == GLFW.GLFW_PRESS) {
				if(e.getKey() == 83) {
					LogUtils.getLogger().info("Has Spell Test? " + SpellsHandler.INSTANCE
							.hasSpell(new ResourceLocation(Evilord.MODID, "test")));
				}
			}
			
			if (player != null) {
				ItemStack stack = player.getMainHandItem();
				if (stack.getItem() instanceof GrimoireOfEvilItem) {
					if (client.getModel() == null)
						return;
					
					if (e.getAction() == GLFW.GLFW_PRESS) {
						if(true){//Config.CLIENT.doDebugStuff.get()) {
							if (H.getKey().getValue() == e.getKey()) {
								client.right();
							} else if (J.getKey().getValue() == e.getKey()) {
								client.left();
							}
							if (47 == e.getKey() && mc.screen == null) {
								mc.setScreen(new ModelPartsScreen(
										client.getModel()).setAnimScreen());
							} else if (SWITCH.getKey().getValue() == e.getKey()) {
								client.switchRotationMode();
							} else if (DISPLAY.getKey().getValue() == e.getKey()) {
								client.display = !client.display;
								LogUtils.getLogger().info("Display: " + client.display);
							} else if(e.getKey() == 89) { // Y
								client.getModel().setShouldUpdateAnimation(true);
							} else if(e.getKey() == 82) { // R
								MinecraftForge.EVENT_BUS.start();
								MinecraftForge.EVENT_BUS.post(new RegisterModelEvent());
								LogUtils.getLogger().info("Has Spell Test? " + SpellsHandler.INSTANCE
										.hasSpell(new ResourceLocation(Evilord.MODID, "test")));
							}
						}
					}
					if(true){//Config.CLIENT.doDebugStuff.get()) {
						if (LEFT.getKey().getValue() == e.getKey()) {
							client.dec(0);
						} else if (RIGHT.getKey().getValue() == e.getKey()) {
							client.inc(0);
						} else if (UP.getKey().getValue() == e.getKey()) {
							client.inc(1);
						} else if (DOWN.getKey().getValue() == e.getKey()) {
							client.dec(1);
						} else if (M.getKey().getValue() == e.getKey()) {
							client.inc(2);
						} else if (N.getKey().getValue() == e.getKey()) {
							client.dec(2);
						}
					}
				}
			}
		}
	}

}
