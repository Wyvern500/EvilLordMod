package com.jg.evilord.entities;

import com.jg.evilord.registries.EntityRegistries;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class DeadMassEntity extends Mob {

	public DeadMassEntity(EntityType<DeadMassEntity> type, Level p_21369_) {
		super(type, p_21369_);
	}
	
	public DeadMassEntity(Level p_21369_) {
		super(EntityRegistries.DeadMass.get(), p_21369_);
	}

}
