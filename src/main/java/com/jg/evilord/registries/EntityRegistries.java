package com.jg.evilord.registries;

import com.jg.evilord.Evilord;
import com.jg.evilord.entities.DeadMassEntity;
import com.jg.evilord.entities.MinionSkeleton;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistries {
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister
			.create(ForgeRegistries.ENTITIES, Evilord.MODID);
	
	public static final RegistryObject<EntityType<MinionSkeleton>> BASICSKELETONMINION = ENTITIES
			.register("basic_skeleton_minion", () -> EntityType.Builder
			.<MinionSkeleton>of(MinionSkeleton::new, MobCategory.MISC).sized(0.375F, 0.375F)
			.build(new ResourceLocation(Evilord.MODID, "basic_minion_skeleton").toString()));
	
	public static final RegistryObject<EntityType<DeadMassEntity>> DeadMass = ENTITIES
			.register("deadmass", () -> EntityType.Builder
			.<DeadMassEntity>of(DeadMassEntity::new, MobCategory.MISC).sized(0.375F, 0.375F)
			.build(new ResourceLocation(Evilord.MODID, "deadmass").toString()));
}
