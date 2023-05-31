package com.jg.evilord.soul.link;

import java.util.List;

import net.minecraft.nbt.CompoundTag;

public interface ISoulLink {

	void set(boolean input, int[] arr);
	
	void unbind(boolean input, int[] arr);
	
	CompoundTag serializeNBT();
    
    void deserializeNBT(CompoundTag nbt);
	
    List<int[]> getLinks();
    
}
