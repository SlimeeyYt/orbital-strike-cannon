package com.slimeeystudios.orbitalrod.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.slimeeystudios.orbitalrod.OrbitalRodMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), OrbitalRodMod.MOD_ID + ".json");
    
    public static ConfigData INSTANCE = new ConfigData();
    
    public static class ConfigData {
        public boolean craftingEnabled = true;
    }
    
    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                INSTANCE = GSON.fromJson(reader, ConfigData.class);
            } catch (IOException e) {
                OrbitalRodMod.LOGGER.error("Failed to load config file for {}", OrbitalRodMod.MOD_ID, e);
            }
        } else {
            save();
        }
    }
    
    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(INSTANCE, writer);
        } catch (IOException e) {
            OrbitalRodMod.LOGGER.error("Failed to save config file for {}", OrbitalRodMod.MOD_ID, e);
        }
    }
}
