package com.slimeeystudios.orbitalrod.item;

import com.slimeeystudios.orbitalrod.OrbitalRodMod;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

public final class ModItems {
    private static final ResourceKey<Item> ORBITAL_TNT_ROD_KEY = ResourceKey.create(Registries.ITEM, OrbitalRodMod.id("orbital_tnt_rod"));

    public static final Item ORBITAL_TNT_ROD = Registry.register(
            BuiltInRegistries.ITEM,
            ORBITAL_TNT_ROD_KEY,
            new OrbitalRodItem(new Item.Properties().setId(ORBITAL_TNT_ROD_KEY).durability(64))
    );

    private ModItems() {
    }

    public static void register() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register(output -> output.accept(ORBITAL_TNT_ROD));
    }
}
