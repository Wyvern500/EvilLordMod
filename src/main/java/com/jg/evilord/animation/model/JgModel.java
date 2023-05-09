package com.jg.evilord.animation.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jg.evilord.animation.Animation;
import com.jg.evilord.animation.Animator;
import com.jg.evilord.animation.Transform;
import com.jg.evilord.client.handler.ClientHandler;
import com.jg.evilord.client.screens.AnimationScreen;
import com.jg.evilord.staff.FireRate;
import com.jg.evilord.utils.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.mojang.math.Quaternion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class JgModel {

	protected Map<String, Object> data;

	protected ClientHandler client;

	protected JgModelPart[] parts;

	public Item item;

	protected Animator animator;

	protected boolean shouldUpdateAnimation;
	protected boolean hasChanges;
	protected boolean playAnimation;
	protected boolean debugMode;
	protected boolean init;

	public JgModel(JgModelPart[] modelParts, ClientHandler client) {
		data = new HashMap<>();
		this.parts = modelParts;
		this.client = client;
		this.animator = new Animator(this);
		this.hasChanges = true;
		this.playAnimation = true;
	}

	// Transform

	protected void translateAndRotateAndScale(Transform t, PoseStack matrix) {
		matrix.translate(t.pos[0], t.pos[1], t.pos[2]);
		matrix.mulPose(new Quaternion(t.rot[0], t.rot[1], t.rot[2], false));
		matrix.scale(t.sca[0], t.sca[1], t.sca[2]);
	}

	protected void translateAndRotateAndScaleAndScale(Transform t, PoseStack matrix) {
		matrix.translate(t.pos[0], t.pos[1], t.pos[2]);
		matrix.mulPose(new Quaternion(t.rot[0], t.rot[1], t.rot[2], false));
		matrix.scale(t.sca[0], t.sca[1], t.sca[2]);
	}

	protected void lerpTransform(PoseStack matrix, float p, Transform t) {
		matrix.translate(Mth.lerp(p, 0, t.pos[0]), Mth.lerp(p, 0, t.pos[1]), Mth.lerp(p, 0, t.pos[2]));
		matrix.mulPose(new Quaternion(Mth.rotLerp(p, 0, t.rot[0]), Mth.rotLerp(p, 0, t.rot[1]),
				Mth.rotLerp(p, 0, t.rot[2]), false));
	}

	protected void leftArm(PoseStack matrix, LocalPlayer player, MultiBufferSource buffer, int light, int leftarm) {
		matrix.pushPose(); // 2+
		renderArm(player, buffer, matrix, light, HumanoidArm.LEFT, parts[leftarm].getCombined());
		matrix.popPose(); // 2-
	}

	// Rendering

	protected void renderItem(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, PoseStack matrix,
			int light) {
		matrix.pushPose();
		Minecraft.getInstance().gameRenderer.itemInHandRenderer.renderItem(player, stack,
				TransformType.FIRST_PERSON_RIGHT_HAND, false, matrix, buffer, light);
		matrix.popPose();
	}

	protected void renderItem(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, PoseStack matrix,
			int light, Transform transform) {
		matrix.pushPose();
		translateAndRotateAndScale(transform, matrix);
		Minecraft.getInstance().gameRenderer.itemInHandRenderer.renderItem(player, stack,
				TransformType.FIRST_PERSON_RIGHT_HAND, false, matrix, buffer, light);
		matrix.popPose();
	}

	protected void renderModel(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, PoseStack matrix,
			int light, BakedModel model) {
		matrix.pushPose();
		Minecraft.getInstance().getItemRenderer().render(stack, TransformType.FIRST_PERSON_RIGHT_HAND, false, matrix,
				buffer, light, OverlayTexture.NO_OVERLAY, model);
		matrix.popPose();
	}

	protected void renderModel(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, PoseStack matrix,
			int light, BakedModel model, Transform transform) {
		matrix.pushPose();
		translateAndRotateAndScale(transform, matrix);
		Minecraft.getInstance().getItemRenderer().render(stack, TransformType.FIRST_PERSON_RIGHT_HAND, false, matrix,
				buffer, light, OverlayTexture.NO_OVERLAY, model);
		matrix.popPose();
	}

	protected void renderArm(LocalPlayer player, MultiBufferSource buffer, PoseStack matrix, int light,
			HumanoidArm arm) {
		matrix.pushPose();
		RenderUtils.renderPlayerArm(matrix, buffer, light, 0, 0, arm);
		matrix.popPose();
	}

	protected void renderArm(LocalPlayer player, MultiBufferSource buffer, PoseStack matrix, int light, HumanoidArm arm,
			Transform transform) {
		matrix.pushPose();
		translateAndRotateAndScale(transform, matrix);
		RenderUtils.renderPlayerArm(matrix, buffer, light, 0, 0, arm);
		matrix.popPose();
	}

	// Misc

	public JgModelPart getPart(String name) {
		for (JgModelPart part : parts) {
			if (part.getName().equals(name)) {
				return part;
			}
		}
		return null;
	}

	public void markChanges() {
		hasChanges = true;
		LogUtils.getLogger().info("Mark changes");
	}

	public boolean isRepTick(int start, float target, float tick, int cycleDur, int times) {
		float t = tick - start;
		int current = (int) Math.floor(t / cycleDur);
		target = target - start;
		return tick > start && current < times && t - (current * cycleDur) == target;
	}

	// Getters and setters

	public JgModelPart[] getParts() {
		return parts;
	}
	
	public List<JgModelPart> getPartsAsList(){
		return new ArrayList<JgModelPart>(Arrays.asList(parts));
	}
	
	public FireRate getFireRate() {
		return FireRate.SEMI;
	}
	
	public void setItem(Item item) {
		this.item = item;
	}

	public void setAnimation(Animation anim) {
		if (getAnimation() == null) {
			this.animator.setAnimation(anim);
			if (Minecraft.getInstance().screen instanceof AnimationScreen) {
				AnimationScreen screen = (AnimationScreen) Minecraft.getInstance().screen;
				screen.initKeyframes();
			}
		}
	}

	public Animation getAnimation() {
		return animator.getAnimation();
	}

	public boolean canPlayAnimation() {
		return playAnimation;
	}

	public void setPlayAnimation(boolean playAnimation) {
		this.playAnimation = playAnimation;
	}

	public boolean shouldUpdateAnimation() {
		return shouldUpdateAnimation;
	}

	public void setShouldUpdateAnimation(boolean shouldUpdateAnimation) {
		this.shouldUpdateAnimation = shouldUpdateAnimation;
	}

	public boolean isDebugModeEnabled() {
		return debugMode;
	}

	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}

	public Animator getAnimator() {
		return animator;
	}

	// Abstract methods

	public void tick(Player player, ItemStack stack) {
		animator.update();
	}

	public abstract void render(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, PoseStack matrix,
			int light);

	public abstract JgModelPart getModelPart();

}
