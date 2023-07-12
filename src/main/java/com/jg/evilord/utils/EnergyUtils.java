package com.jg.evilord.utils;

import com.jg.evilord.entities.block.AbstractBinderBlockEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.energy.EnergyStorage;

public class EnergyUtils {
	
	// Code from: Author: BluSunrize Mod: Inmersive Enginering Class: EnergyHelper
	
	public static final String LEGACY_ENERGY_KEY = "ifluxEnergy";
	public static final String ENERGY_KEY = "energy";

	public static void deserializeFrom(EnergyStorage storage, CompoundTag mainTag) {
		Tag subtag;
		if(mainTag.contains(LEGACY_ENERGY_KEY, Tag.TAG_INT))
			subtag = mainTag.get(LEGACY_ENERGY_KEY);
		else if(mainTag.contains(ENERGY_KEY, Tag.TAG_INT))
			subtag = mainTag.get(ENERGY_KEY);
		else
			subtag = IntTag.valueOf(0);
		storage.deserializeNBT(subtag);
	}

	public static void serializeTo(EnergyStorage storage, CompoundTag mainTag) {
		mainTag.put(ENERGY_KEY, storage.serializeNBT());
	}
	
	public static void extract(int amount, boolean simulate, 
			AbstractBinderBlockEntity<?> be) {
		
	}
	
}
