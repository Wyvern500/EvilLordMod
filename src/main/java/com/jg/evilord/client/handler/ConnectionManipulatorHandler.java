package com.jg.evilord.client.handler;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.jg.evilord.Evilord;
import com.jg.evilord.client.helpers.Timer;
import com.jg.evilord.network.client.ShowInfoBlocksMessage;
import com.jg.evilord.network.server.ProcessConnectionManipulatorWandMessage;
import com.jg.evilord.registries.ItemRegistries;
import com.jg.evilord.utils.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ConnectionManipulatorHandler {

	private List<InfoBlock> positions;
	private List<InfoBlock> visual;
	private List<Component> info;
	SoulLinkHandler linkHandler;
	private int mode;
	private boolean leftClick;
	
	private boolean render;
	private boolean renderInfo;
	
	private final int ADD = 0;
	private final int REMOVE = 1;
	private final int VISUALIZE = 2;
	
	Timer renderTimer;
	Timer renderInfoTimer;
	
	public ConnectionManipulatorHandler() {
		positions = new ArrayList<>();
		visual = new ArrayList<>();
		info = new ArrayList<>();
		linkHandler = new SoulLinkHandler();
		renderInfoTimer = new Timer(0, 20, this::onTimerEnd).start();
		renderTimer = new Timer(1, 20, this::onTimerEnd).start();
	}
	
	public void onTimerEnd(int id, Timer timer) {
		if(id == 0) {
			renderInfo = false;
			clearVisualInfoBlocks();
			LogUtils.getLogger().info("renderInfo Ended");
		} else if(id == 1) {
			positions.clear();
		}
	}
	
	public void onClick(int button, int action) {
		if(action == GLFW.GLFW_PRESS) {
			if(button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
				if(Minecraft.getInstance().player.getMainHandItem().getItem() 
						== ItemRegistries.connectionManipulatorWand.get()) {
					HitResult hitresult = Minecraft.getInstance().hitResult;
				    if (hitresult != null && hitresult.getType() == HitResult.Type.BLOCK) {
				         BlockPos blockpos1 = ((BlockHitResult)hitresult).getBlockPos();
				         BlockState blockstate = Minecraft.getInstance().level.getBlockState(blockpos1);
				         addPos(blockpos1);
				         LogUtils.getLogger().info(blockstate.getBlock().getRegistryName()
				        		 .toString());
				         renderTimer.start();
				    }
				}
			} else if(button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
				if(leftClick) {
					process();
					clear();
					leftClick = false;
				}
			}
		}
	}
	
	public void onKeyboard(int key, boolean down) {
		if(down) {
			if(key == GLFW.GLFW_KEY_LEFT_SHIFT) {
				leftClick = true;
			} else if(key == GLFW.GLFW_KEY_X) {
				if(mode < 2) {
					mode++;
				} else {
					mode = 0;
				}
			}
		} else {
			if(key == GLFW.GLFW_KEY_LEFT_SHIFT) {
				leftClick = false;
			}
		}
	}
	
	public void process() {
		if(positions.size() == 2) {
			InfoBlock pos1 = positions.get(1);
			double dist = positions.get(0).pos.distSqr(new Vec3i(pos1.pos.getX(), 
					pos1.pos.getY(), 
					pos1.pos.getZ()));
			LogUtils.getLogger().info("Distance: " + dist);
			if(dist < 64) { // Proximamente anadirlo a la configuracion, la distancia de conexion
				if(mode == ADD) {
					Evilord.channel.sendToServer(
							new ProcessConnectionManipulatorWandMessage(true,
							positions.get(0).pos, pos1.pos));
					LogUtils.getLogger().info("ADD");
				} else if(mode == REMOVE) {
					Evilord.channel.sendToServer(
							new ProcessConnectionManipulatorWandMessage(false,
							positions.get(0).pos, pos1.pos));
					LogUtils.getLogger().info("REMOVE");
				}
				
			}
		}
	}
	
	public void tick() {
		renderInfoTimer.tick();
		renderTimer.tick();
	}
	
	public void render(PoseStack matrix) {
		for(InfoBlock pos : positions) {
			if(pos != null) {
				RenderUtils.renderBlock(Minecraft.getInstance().renderBuffers().bufferSource()
		 				.getBuffer(RenderType.lines()), matrix, pos.pos, 1.0f);
			}
		}
		if(renderInfo) {
			for(InfoBlock pos : visual) {
				if(pos != null) {
					RenderUtils.renderBlock(Minecraft.getInstance().renderBuffers().bufferSource()
			 				.getBuffer(RenderType.lineStrip()), matrix, pos.pos, 
			 				pos.color.r, pos.color.g, pos.color.b, 1.0f);
				}
			}
		}
	}
	
	public void renderOverlayPreLayer(PoseStack matrix) {
		if(renderInfo) {
			RenderUtils.renderTooltip(matrix, info, -8, 16);
		}
	}
	
	public void addPos(BlockPos pos) {
		if(mode == ADD || mode == REMOVE) {
			if(positions.size() == 2) {
				positions.remove(0);
			}
			Color c = mode == ADD ? new Color(0, 1, 0) : new Color(1, 0, 0);
			if(positions.size() == 1) {
				if(!positions.get(0).pos.equals(pos)) {
					positions.add(new InfoBlock(pos, c));
				}
			} else {
				positions.add(new InfoBlock(pos, c));
			}
		} else {
			positions.clear();
			positions.add(new InfoBlock(pos, new Color(1, 1, 0)));
			Evilord.channel.sendToServer(new ShowInfoBlocksMessage(pos, 
					new CompoundTag()));
			LogUtils.getLogger().info("sending message");
		}
	}
	
	public void clearVisualInfoBlocks() {
		visual.clear();
	}
	
	public void fillVisualInfoBlocks(List<int[]> inputs, List<int[]> outputs) {
		for(int i = 0; i < inputs.size(); i++) {
			visual.add(new InfoBlock(inputs.get(i), new Color(0, 1, 0)));
		}
		for(int i = 0; i < outputs.size(); i++) {
			visual.add(new InfoBlock(outputs.get(i), new Color(1, 0, 0)));
		}
		renderInfo = true;
		renderInfoTimer.start();
		LogUtils.getLogger().info("Filling blocks");
	}
	
	public void fillInfo(byte inputs, byte outputs, byte remainingInputSlots, 
			byte remainingOutputSlots) {
		info.clear();
		info.add(new TranslatableComponent("Input Slots: " + inputs));
		info.add(new TranslatableComponent("Output Slots: " + outputs));
		info.add(new TranslatableComponent("Remaining Input Slots: " 
				+ remainingInputSlots));
		info.add(new TranslatableComponent("Remaining Output Slots: " 
				+ remainingOutputSlots));
	}
	
	public void clear() {
		positions.clear();
	}

	public int getMode() {
		return mode;
	}
	
	public class InfoBlock {
		
		public BlockPos pos;
		public Color color;
		
		public InfoBlock(int[] pos, Color c) {
			this(new BlockPos(pos[0], pos[1], pos[2]), c);
		}
		
		public InfoBlock(BlockPos pos, Color c) {
			this.pos = pos;
			this.color = c;
		}
		
	}
	
	public class Color {
		
		public float r;
		public float g;
		public float b;
		
		public Color(float r, float g, float b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}
		
	}
	
}
