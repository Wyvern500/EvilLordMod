package com.jg.evilord.soul.energy;

import com.mojang.logging.LogUtils;

import net.minecraft.nbt.CompoundTag;

public class SoulEnergyStorage implements ISoulEnergyStorage {

	public static final String SOULENERGYKEY = "soulenergy";
	
	protected int capacity;
	protected int energy;
	protected int maxReceive;
    protected int maxExtract;
	
    public SoulEnergyStorage(int capacity)
    {
        this(capacity, capacity, capacity, 0);
    }

    public SoulEnergyStorage(int capacity, int maxTransfer)
    {
        this(capacity, maxTransfer, maxTransfer, 0);
    }

    public SoulEnergyStorage(int capacity, int maxReceive, int maxExtract)
    {
        this(capacity, maxReceive, maxExtract, 0);
    }

    public SoulEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy)
    {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energy = Math.max(0 , Math.min(capacity, energy));
    }
	
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate)
    {
        if (!canReceive())
            return 0;

        int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            energy += energyReceived;
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
        if (!canExtract())
            return 0;

        int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            energy -= energyExtracted;
        LogUtils.getLogger().info("Current energy");
        return energyExtracted;
    }

    @Override
    public void setSoulEnergy(int energy) {
    	this.energy = energy;
    }
    
    @Override
    public int getEnergyStored()
    {
        return energy;
    }

    @Override
    public int getMaxEnergyStored()
    {
        return capacity;
    }

    @Override
    public boolean canExtract()
    {
        return this.maxExtract > 0;
    }

    @Override
    public boolean canReceive()
    {
        return this.maxReceive > 0;
    }

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();
		nbt.putInt(SOULENERGYKEY, energy);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		this.energy = nbt.getInt(SOULENERGYKEY);
	}
	
}
