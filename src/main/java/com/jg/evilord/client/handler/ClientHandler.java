package com.jg.evilord.client.handler;

import java.util.List;

import com.jg.evilord.animation.model.JgModel;
import com.jg.evilord.animation.model.JgModelPart;
import com.jg.evilord.animation.model.ModelsHandler;
import com.jg.evilord.spell.Spell;
import com.jg.evilord.spell.handler.SpellsHandler;
import com.jg.evilord.utils.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ClientHandler {
	
	public static float partialTicks = 0.0f;
	
	private JgModel current;
	
	public int part;
	public int mode; // 0 : translate | 1 : rotate | 2 : scale
	private int currentSpellIndex;
	public boolean init;
	public boolean display;
	public boolean renderHitmarker;
	public boolean debugAim;
	public boolean loop;
	private ConnectionManipulatorHandler connectionManipulatorHandler;
	
	public ClientHandler() {
		connectionManipulatorHandler = new ConnectionManipulatorHandler();
	}
	
	// Methods
	
	public void tick() {
		connectionManipulatorHandler.tick();
	}
	
	public void shoot(ItemStack stack, Player player) {
		Spell spell = SpellsHandler.INSTANCE.getSpell(new ResourceLocation("evilord:test"));
		if(spell.canLaunch(stack, player)) {
			spell.onLaunch(stack, player);
			//player.inventoryMenu.getCarried();
			//Evilord.channel.sendToServer(new RunSpellMessage("evilord:test"));
		}
	}
	
	// Rendering
	
	public void render(PoseStack matrix, MultiBufferSource buffer, int light) {
		LocalPlayer player = Minecraft.getInstance().player;
		ItemStack stack = player.getMainHandItem();
		if(current != null) {
			current.tick(player, stack);
			current.render(player, stack, buffer, matrix, light);
		}
		
	}
	
	public void renderOverlayPreLayer(PoseStack matrix) {
		connectionManipulatorHandler.renderOverlayPreLayer(matrix);
	}
	
	public void renderWorld(PoseStack matrix) {
		connectionManipulatorHandler.render(matrix);
	}
	
	// Input
	
	public void onKeyboard(int key, boolean down) {
		connectionManipulatorHandler.onKeyboard(key, down);
	}
	
	public void onClick(int button, int action) {
		connectionManipulatorHandler.onClick(button, action);
	}
	
	// Misc
	
	public void left() {
		if(current != null) {
			part = (part - 1 + current.getParts().length) % current.getParts().length;
			JgModelPart gunPart = current.getParts()[part];
			if(gunPart != null) {
				LogUtils.getLogger().info("Left: " + part + " name: " + gunPart.getName());
			} else {
				LogUtils.getLogger().info("Left: " + part);
			}
		}
	}
	
	public void right() {
		if(current != null) {
			part = (part + 1) % current.getParts().length;
			JgModelPart gunPart = current.getParts()[part];
			if(gunPart != null) {
				LogUtils.getLogger().info("Right: " + part + " name: " + gunPart.getName());
			} else {
				LogUtils.getLogger().info("Right: " + part);
			}
		}
	}
	
	public void inc(int type) {
		float v = 0.01f;
		float vr = 1f;
		if(current != null) {
			if(mode == 0) {
				JgModelPart gunPart = current.getParts()[part];
				if(gunPart != null) {
					if(!display) {
						gunPart.getTransform().pos[type] += v;
						LogUtils.getLogger().info("Transform pos: x: " + current.getParts()[part].getTransform().pos[0] + " y: " + current.getParts()[part].getTransform().pos[1] + " z: " + current.getParts()[part].getTransform().pos[2]);
					} else {
						gunPart.getDTransform().pos[type] += v;
						LogUtils.getLogger().info("Transform pos: x: " + current.getParts()[part].getDTransform().pos[0] + " y: " + current.getParts()[part].getDTransform().pos[1] + " z: " + current.getParts()[part].getDTransform().pos[2]);
					}
				}
			} else if(mode == 1) {
				JgModelPart gunPart = current.getParts()[part];
				if(gunPart != null) {
					if(!display) {
						current.getParts()[part].getTransform().rot[type] += (float)Math.toRadians(vr);
						LogUtils.getLogger().info("Transform Rad x: " + current.getParts()[part].getTransform().rot[0]
								+ " y: " + 
								current.getParts()[part].getTransform().rot[1]
								+ " z: " 
								+ current.getParts()[part].getTransform().rot[2] + 
								" Deg x: " + 
								(float)Math.toDegrees(current.getParts()[part].getTransform().rot[0])
								+ " y: " + 
								(float)Math.toDegrees(current.getParts()[part].getTransform().rot[1]) 
								+ " z: " 
								+ (float)Math.toDegrees(current.getParts()[part].getTransform().rot[2]));
					} else {
						current.getParts()[part].getDTransform().rot[type] += (float)Math.toRadians(vr);
						LogUtils.getLogger().info("Transform Rad x: " + current.getParts()[part].getDTransform().rot[0]
								+ " y: " + 
								current.getParts()[part].getDTransform().rot[1]
								+ " z: " 
								+ current.getParts()[part].getDTransform().rot[2] + 
								" Deg x: " + 
								(float)Math.toDegrees(current.getParts()[part].getDTransform().rot[0])
								+ " y: " + 
								(float)Math.toDegrees(current.getParts()[part].getDTransform().rot[1]) 
								+ " z: " 
								+ (float)Math.toDegrees(current.getParts()[part].getDTransform().rot[2]));
					}
				}
			} else if(mode == 2) {
				JgModelPart gunPart = current.getParts()[part];
				if(gunPart != null) {
					if(!display) {
						gunPart.getTransform().sca[type] += v;
						LogUtils.getLogger().info("Transform sca: x: " + current.getParts()[part].getTransform().sca[0] + " y: " + current.getParts()[part].getTransform().sca[1] + " z: " + current.getParts()[part].getTransform().sca[2]);
					} else {
						gunPart.getDTransform().sca[type] += v;
						LogUtils.getLogger().info("Transform sca: x: " + current.getParts()[part].getDTransform().sca[0] + " y: " + current.getParts()[part].getDTransform().sca[1] + " z: " + current.getParts()[part].getDTransform().sca[2]);
					}
				}
			}
		}
	}
	
	public void dec(int type) {
		float v = 0.01f;
		float vr = 1f;
		if(current != null) {
			if(mode == 0) {
				JgModelPart gunPart = current.getParts()[part];
				if(gunPart != null) {
					if(!display) {
						gunPart.getTransform().pos[type] -= v;
						LogUtils.getLogger().info("Transform pos: x: " + current.getParts()[part].getTransform().pos[0] + " y: " + current.getParts()[part].getTransform().pos[1] + " z: " + current.getParts()[part].getTransform().pos[2]);
					} else {
						gunPart.getDTransform().pos[type] -= v;
						LogUtils.getLogger().info("Transform pos: x: " + current.getParts()[part].getDTransform().pos[0] + " y: " + current.getParts()[part].getDTransform().pos[1] + " z: " + current.getParts()[part].getDTransform().pos[2]);
					}
				}
			} else if(mode == 1) {
				JgModelPart gunPart = current.getParts()[part];
				if(gunPart != null) {
					if(!display) {
						current.getParts()[part].getTransform().rot[type] -= (float)Math.toRadians(vr);
						LogUtils.getLogger().info("Transform Rad x: " + current.getParts()[part].getTransform().rot[0]
								+ " y: " + 
								current.getParts()[part].getTransform().rot[1]
								+ " z: " 
								+ current.getParts()[part].getTransform().rot[2] + 
								" Deg x: " + 
								(float)Math.toDegrees(current.getParts()[part].getTransform().rot[0])
								+ " y: " + 
								(float)Math.toDegrees(current.getParts()[part].getTransform().rot[1]) 
								+ " z: " 
								+ (float)Math.toDegrees(current.getParts()[part].getTransform().rot[2]));
					} else {
						current.getParts()[part].getDTransform().rot[type] -= (float)Math.toRadians(vr);
						LogUtils.getLogger().info("Transform Rad x: " + current.getParts()[part].getDTransform().rot[0]
								+ " y: " + 
								current.getParts()[part].getDTransform().rot[1]
								+ " z: " 
								+ current.getParts()[part].getDTransform().rot[2] + 
								" Deg x: " + 
								(float)Math.toDegrees(current.getParts()[part].getDTransform().rot[0])
								+ " y: " + 
								(float)Math.toDegrees(current.getParts()[part].getDTransform().rot[1]) 
								+ " z: " 
								+ (float)Math.toDegrees(current.getParts()[part].getDTransform().rot[2]));
					}
				}
			} else if(mode == 2) {
				JgModelPart gunPart = current.getParts()[part];
				if(gunPart != null) {
					if(!display) {
						gunPart.getTransform().sca[type] -= v;
						LogUtils.getLogger().info("Transform sca: x: " + current.getParts()[part].getTransform().sca[0] + " y: " + current.getParts()[part].getTransform().sca[1] + " z: " + current.getParts()[part].getTransform().sca[2]);
					} else {
						gunPart.getDTransform().sca[type] -= v;
						LogUtils.getLogger().info("Transform sca: x: " + current.getParts()[part].getDTransform().sca[0] + " y: " + current.getParts()[part].getDTransform().sca[1] + " z: " + current.getParts()[part].getDTransform().sca[2]);
					}
				}
			}
		}
	}
	
	public void selectJgModel() {
		Item gun = Minecraft.getInstance().player.getMainHandItem().getItem();
		this.current = ModelsHandler.get(gun.getDescriptionId());
		//LogUtils.getLogger().info("selecting JgModel");
	}
	
	public void switchRotationMode() {
		mode = (mode + 1) % 3;
		String modeS = "";
		switch(mode) {
		case 0:
			modeS = "Translate";
			break;
		case 1:
			modeS = "Rotate";
			break;
		case 2:
			modeS = "Scale";
			break;
		}
		LogUtils.getLogger().info("Mode: " + modeS);
	}
	
	public void setJgModel(JgModel model) {
		this.current = model;
	}
	
	public JgModel getModel() {
		return current;
	}
	
	public ConnectionManipulatorHandler getConnectionManipulator() {
		return connectionManipulatorHandler;
	}
}
