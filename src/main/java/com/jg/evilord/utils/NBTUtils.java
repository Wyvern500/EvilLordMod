package com.jg.evilord.utils;

import java.util.List;

import com.jg.evilord.item.SoulContainerItem;
import com.mojang.logging.LogUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class NBTUtils {

	public static final String EMPTY = "";
	public static final String ID = "id";
	public static final String MODIFIED = "modified";
	public static final String HASCONTAINER = "hassoulcontainer";
	public static final String SOULS = "souls";
	public static final String CONTAINERTIER = "containertier";
	
	public static CompoundTag get(ItemStack stack) {
		return stack.getOrCreateTag();
	}
	
	public static CompoundTag intListToNbt(List<int[]> list) {
		CompoundTag tag = new CompoundTag();
		for(int i = 0; i < list.size(); i++) {
			tag.putIntArray(String.valueOf(i), list.get(i));
		}
		return tag;
	}
	
	public static CompoundTag writeInfoDataToNbt(CompoundTag tag, List<Pos> inputs, 
			List<Pos> outputs) {
		for(int i = 0; i < inputs.size(); i++) {
			tag.putIntArray("input" + String.valueOf(i), inputs.get(i).pos);
			//LogUtils.getLogger().info("i: " + i);
		}
		for(int i = 0; i < outputs.size(); i++) {
			tag.putIntArray("output" + String.valueOf(i), outputs.get(i).pos);
			//LogUtils.getLogger().info("i: " + i);
		}
		tag.putByte("inputs", (byte) inputs.size());
		tag.putByte("outputs", (byte) outputs.size());
		LogUtils.getLogger().info("inputs: " + inputs.size());
		//LogUtils.getLogger().info("new data: " + tag.getAsString());
		return tag;
	}
	
	public static String getId(ItemStack stack) {
		return get(stack).getString(ID);
	}
	
	public static void setId(ItemStack stack, String id) {
		get(stack).putString(ID, id);
	}
	
	public static boolean getModified(ItemStack stack) {
		return get(stack).getBoolean(MODIFIED);
	}
	
	public static void setModified(ItemStack stack, boolean modified) {
		get(stack).putBoolean(MODIFIED, modified);
	}
	
	public static void setHasContainer(ItemStack stack, boolean has) {
		get(stack).putBoolean(HASCONTAINER, has);
	}
	
	public static boolean hasContainer(ItemStack stack) {
		return get(stack).getBoolean(HASCONTAINER);
	}
	
	public static void setSouls(ItemStack stack, int souls) {
		get(stack).putInt(SOULS, souls);
	}
	
	public static int getSouls(ItemStack stack) {
		return get(stack).getInt(SOULS);
	}
	
	public static void setContainerTier(ItemStack stack, int tier) {
		get(stack).putInt(CONTAINERTIER, tier);
	}
	
	public static int getContainerTier(ItemStack stack) {
		return get(stack).getInt(CONTAINERTIER);
	}
	
	// Misc
	
	public static int getMaxSoulsForTier(ItemStack stack) {
		return SoulContainerItem.ContainerTier.values()[Math.max(getContainerTier(stack), 0)].getMaxSouls();
	}
	
}
