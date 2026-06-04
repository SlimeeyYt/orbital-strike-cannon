package com.slimeeystudios.orbitalrod.item;

import com.slimeeystudios.orbitalrod.OrbitalRodMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(OrbitalRodMod.MOD_ID);

    public static final DeferredItem<Item> ORBITAL_TNT_ROD = ITEMS.register("orbital_tnt_rod", 
            () -> {
                ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(OrbitalRodMod.MOD_ID, "orbital_tnt_rod"));
                return new OrbitalRodItem(new Item.Properties().setId(itemKey).durability(64));
            });

    private ModItems() {
    }

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        modEventBus.addListener(ModItems::addCreative);
    }

    private static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ORBITAL_TNT_ROD);
        }
    }
}
