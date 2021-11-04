package me.mazenz.SaulKits;

import me.mazenz.SaulKits.commands.KitCommand;
import me.mazenz.SaulKits.commands.KitsCommand;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.logging.Logger;

public final class Kits extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");
    private Economy econ;

    @Override
    public void onEnable() {
        try {
            getConfig().options().copyDefaults(true);
            saveConfig();
            genPerms();

            new UpdateChecker(this, 88036).getVersion(version -> {
                log.info("Checking for Updates...");
                if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                    log.info("No new version available");
                } else {
                    log.info("There is a new version of SaulKits. https://www.spigotmc.org/resources/saulkits.88036/");
                }
            });

            if (getConfig().getBoolean("vaultEnabled")) {
                if (!setupEconomy()) {
                    log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
                    Bukkit.getPluginManager().disablePlugin(this);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Objects.requireNonNull(getCommand("kits")).setExecutor(new KitsCommand(this));
        Objects.requireNonNull(getCommand("kit")).setExecutor(new KitCommand(this, econ));
    }

    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }

    @Override
    public void onDisable() {
        System.out.println("Kits Stopping");
    }

    public void genPerms() throws IOException {
        try (FileWriter permissions = new FileWriter("./permissions.yml")) {
            for (String kitName : Objects.requireNonNull(getConfig().getConfigurationSection("kits")).getKeys(false)) {
                String permissionsContent = Files.readString(Path.of("./permissions.yml"), StandardCharsets.UTF_8);

                if (!permissionsContent.contains(kitName)) {
                    permissions.write(System.getProperty("line.separator"));
                    permissions.write("kits.kit." + kitName + ":");
                    permissions.write(System.getProperty("line.separator"));
                    permissions.write("       default: op");
                    permissions.write(System.getProperty("line.separator"));
                }
            }
        }
        System.out.println("[Kits] Permissions loaded");
    }
}
