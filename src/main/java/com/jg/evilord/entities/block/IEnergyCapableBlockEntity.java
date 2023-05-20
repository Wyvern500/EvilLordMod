package com.jg.evilord.entities.block;

import com.jg.evilord.soul.energy.ISoulEnergyStorage;

public interface IEnergyCapableBlockEntity<T extends BinderBlockEntity> {

	public ISoulEnergyStorage getSoulEnergyStorage();
	
}
