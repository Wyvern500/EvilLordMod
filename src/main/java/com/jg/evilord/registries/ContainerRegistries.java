package com.jg.evilord.registries;

import com.jg.evilord.Evilord;
import com.jg.evilord.container.SoulManipulatorContainer;
import com.jg.evilord.container.SpellExplorerContainer;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ContainerRegistries {

	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(
			ForgeRegistries.CONTAINERS, Evilord.MODID);

	public static final RegistryObject<MenuType<SoulManipulatorContainer>> soulContainerManipulator = 
			CONTAINERS.register("soul_container_manipulator", 
					() -> new MenuType<>(SoulManipulatorContainer::new));
	
	public static final RegistryObject<MenuType<SpellExplorerContainer>> spellExplorer = 
			CONTAINERS.register("spell_explorer", 
					() -> new MenuType<>(SpellExplorerContainer::new));
	
}
