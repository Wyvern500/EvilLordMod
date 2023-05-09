package com.jg.evilord.utils;

import com.jg.evilord.item.SoulContainerItem;

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
