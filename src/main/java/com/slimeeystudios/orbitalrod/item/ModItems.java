package com.slimeeystudios.orbitalrod.item;

import com.slimeeystudios.orbitalrod.OrbitalRodMod;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class ModItems {
    public static final Item ORBITAL_TNT_ROD = Registry.register(
            Registries.ITEM,
            OrbitalRodMod.id("orbital_tnt_rod"),
            new OrbitalRodItem(new FabricItemSettings().maxDamage(64))
    );

    private ModItems() {
    }

    public static void register() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(ORBITAL_TNT_ROD));
    }
}

