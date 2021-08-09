package me.mazenz.kits;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Objects;

public class KitsCommand implements CommandExecutor {
    private final Kits kits;

    public KitsCommand(Kits kits) {
        this.kits = kits;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.YELLOW + "====================");
            sender.sendMessage(ChatColor.YELLOW + "List of available kits:");
            for (String name : Objects.requireNonNull(kits.getConfig().getConfigurationSection("kits")).getKeys(false)) {
                sender.sendMessage(ChatColor.BOLD + "" +
                        ChatColor.valueOf(kits.getConfig().getString("kitsListColor")) +
                        name + ChatColor.WHITE + ": " + ChatColor.RESET + "" +
                        ChatColor.valueOf(kits.getConfig().getString("kitsListColor")) +
                        Objects.requireNonNull(kits.getConfig().getConfigurationSection("kits." + name)).getString("description"));
            }
            sender.sendMessage(ChatColor.YELLOW + "====================");
        } else {
            sender.sendMessage("This command can only be run in-game");
        }

        return true;
    }
}
