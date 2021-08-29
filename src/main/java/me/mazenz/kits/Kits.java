package me.mazenz.kits;

import org.bukkit.plugin.java.JavaPlugin;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public final class Kits extends JavaPlugin {

    @Override
    public void onEnable() {
        try {
            getConfig().options().copyDefaults(true);
            saveConfig();
            genPerms();
            Objects.requireNonNull(getCommand("kits")).setExecutor(new KitsCommand(this));
            Objects.requireNonNull(getCommand("kit")).setExecutor(new KitCommand(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
