package com.slimeeystudios.orbitalrod;

import com.slimeeystudios.orbitalrod.item.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(OrbitalRodMod.MOD_ID)
public class OrbitalRodMod {
    public static final String MOD_ID = "orbitalrod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public OrbitalRodMod(IEventBus modEventBus) {
        LOGGER.info("OrbitalRod mod initializing...");

        // Register deferred registries
        ModItems.register(modEventBus);

        // Register event listeners
        modEventBus.addListener(this::onClientSetup);

        LOGGER.info("OrbitalRod initialized");
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        LOGGER.debug("Client setup for OrbitalRod");
    }
}
