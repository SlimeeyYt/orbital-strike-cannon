package com.slimeeystudios.orbitalrod;

import com.slimeeystudios.orbitalrod.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrbitalRodMod implements ModInitializer {
    public static final String MOD_ID = "orbitalrod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModItems.register();
        LOGGER.info("OrbitalRod initialized");
    }

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
}

