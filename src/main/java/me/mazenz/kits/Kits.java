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
        System.out.println("Kits+ Shutting Down");
    }

    public void genPerms() throws IOException {
        try (FileWriter genPerms = new FileWriter("./permissions.yml")) {
            for (String kitName : Objects.requireNonNull(getConfig().getConfigurationSection("kits")).getKeys(false)) {
                String permissionsContent = Files.readString(Path.of("./permissions.yml"), StandardCharsets.UTF_8);

                if (!permissionsContent.contains(kitName)) {
                    genPerms.write(System.getProperty("line.separator"));
                    genPerms.write("kits.kit." + kitName + ":");
                    genPerms.write(System.getProperty("line.separator"));
                    genPerms.write("       default: op");
                    genPerms.write(System.getProperty("line.separator"));
                }
            }
        }
        System.out.println("[Kits] Permissions loaded");
    }
}
