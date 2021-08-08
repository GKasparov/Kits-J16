package me.mazenz.kits;

import org.bukkit.plugin.java.JavaPlugin;

public final class Kits extends JavaPlugin {

    @Override
    public void onEnable() {
        try {
            getConfig().options().copyDefaults(true);
            saveConfig();
            getCommand("kits").setExecutor(new KitsCommand(this));
            getCommand("kit").setExecutor(new KitCommand(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        System.out.println("Kits+ Shutting Down");
    }
}
