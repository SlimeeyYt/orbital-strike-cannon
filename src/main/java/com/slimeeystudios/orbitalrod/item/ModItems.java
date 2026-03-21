package com.slimeeystudios.orbitalrod.item;

import com.slimeeystudios.orbitalrod.OrbitalRodMod;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class ModItems {
    private static final RegistryKey<Item> ORBITAL_TNT_ROD_KEY = RegistryKey.of(RegistryKeys.ITEM, OrbitalRodMod.id("orbital_tnt_rod"));

    public static final Item ORBITAL_TNT_ROD = Registry.register(
            Registries.ITEM,
            ORBITAL_TNT_ROD_KEY,
            new OrbitalRodItem(new Item.Settings().registryKey(ORBITAL_TNT_ROD_KEY).maxDamage(64).repairable(Items.NETHER_STAR))
    );

    private ModItems() {
    }

    public static void register() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(ORBITAL_TNT_ROD));
    }
}

