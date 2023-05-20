package com.jg.evilord.soul.energy;

import net.minecraft.nbt.CompoundTag;

public interface ISoulEnergyStorage {

	int receiveEnergy(int maxReceive, boolean simulate);
	
	int extractEnergy(int maxExtract, boolean simulate);
	
	void setSoulEnergy(int energy);
	
	int getEnergyStored();
	
	int getMaxEnergyStored();
	
	boolean canExtract();

    boolean canReceive();
	
    CompoundTag serializeNBT();
    
    void deserializeNBT(CompoundTag nbt);
    
}
