package com.jg.evilord.client.handler;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.jg.evilord.Evilord;
import com.jg.evilord.network.server.ProcessConnectionManipulatorWandMessage;
import com.jg.evilord.registries.ItemRegistries;
import com.jg.evilord.utils.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ConnectionManipulatorHandler {

	private List<BlockPos> positions;
	private boolean leftClick;
	
	public ConnectionManipulatorHandler() {
		positions = new ArrayList<>();
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
			}
		} else {
			if(key == GLFW.GLFW_KEY_LEFT_SHIFT) {
				leftClick = false;
			}
		}
	}
	
	public void process() {
		if(positions.size() == 2) {
			BlockPos pos1 = positions.get(1);
			double dist = positions.get(0).distSqr(new Vec3i(pos1.getX(), pos1.getY(), 
					pos1.getZ()));
			if(dist < 15) { // Proximamente anadirlo a la configuracion, la distancia de conexion
				Evilord.channel.sendToServer(new ProcessConnectionManipulatorWandMessage(
						positions.get(0), pos1));
			}
		}
	}
	
	public void render(PoseStack matrix) {
		for(BlockPos pos : positions) {
			if(pos != null) {
				RenderUtils.renderBlock(Minecraft.getInstance().renderBuffers().bufferSource()
		 				.getBuffer(RenderType.lines()), matrix, pos, 1.0f);
			}
		}
	}
	
	public void addPos(BlockPos pos) {
		if(positions.size() == 2) {
			positions.remove(0);
		}
		if(positions.size() == 1) {
			if(!positions.get(0).equals(pos)) {
				positions.add(pos);
			}
		} else {
			positions.add(pos);
		}
	}
	
	public void clear() {
		positions.clear();
	}
	
}
