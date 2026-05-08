package com.slimeeystudios.orbitalrod.config;

import com.slimeeystudios.orbitalrod.OrbitalRodMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class ModConfig {
    private static final String FILE_NAME = OrbitalRodMod.MOD_ID + ".properties";
    private static final String ENABLE_CRAFTING_RECIPE = "enableCraftingRecipe";
    private static boolean craftingRecipeEnabled = true;

    private ModConfig() {
    }

    public static void load() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve(FILE_NAME);
        Properties properties = new Properties();
        properties.setProperty(ENABLE_CRAFTING_RECIPE, Boolean.toString(craftingRecipeEnabled));

        if (Files.notExists(configPath)) {
            save(properties, configPath);
        }

        try (InputStream inputStream = Files.newInputStream(configPath)) {
            properties.load(inputStream);
            String value = properties.getProperty(ENABLE_CRAFTING_RECIPE, "true");
            craftingRecipeEnabled = !"false".equalsIgnoreCase(value.trim());
        } catch (IOException exception) {
            OrbitalRodMod.LOGGER.warn("Failed to load config file {}, using defaults.", configPath, exception);
            craftingRecipeEnabled = true;
        }
    }

    private static void save(Properties properties, Path configPath) {
        try {
            Files.createDirectories(configPath.getParent());
        } catch (IOException exception) {
            OrbitalRodMod.LOGGER.warn("Failed to create config directory for {}", configPath, exception);
            return;
        }

        try (OutputStream outputStream = Files.newOutputStream(configPath)) {
            properties.store(outputStream, "OrbitalRod configuration");
        } catch (IOException exception) {
            OrbitalRodMod.LOGGER.warn("Failed to create default config file {}", configPath, exception);
        }
    }

    public static boolean isCraftingRecipeEnabled() {
        return craftingRecipeEnabled;
    }
}
