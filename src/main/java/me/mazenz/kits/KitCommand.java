package me.mazenz.kits;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.Objects;
import static org.bukkit.Sound.*;

public class KitCommand implements CommandExecutor {
    private final Kits kits;


    public KitCommand(Kits kits) {
        this.kits = kits;
    }

    @Override

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            try {
                if (args.length == 1) {
                    for (String name : Objects.requireNonNull(kits.getConfig().getConfigurationSection("kits")).getKeys(false)) {
                        if (args[0].equalsIgnoreCase(name)) {
                            if (sender.hasPermission("kits.kit." + name)) {
                                if (kits.getConfig().getBoolean("clearInventory")) {
                                    player.getInventory().clear();
                                }
                                if (kits.getConfig().getBoolean("kitSounds")) {
                                    Location location = player.getLocation();
                                    player.playSound(location, BLOCK_ANVIL_PLACE, 10, 1);
                                }
                                for (String items : kits.getConfig().getStringList("kits." + name + ".items")) {
                                    String[] ItemInfo = items.split(";");
                                    Material type = Material.getMaterial(ItemInfo[0]);
                                    int amount = Integer.parseInt(ItemInfo[1]);
                                    assert type != null;
                                    ItemStack kit = new ItemStack(type, amount);
                                    player.getInventory().addItem(kit);
                                }

                                sender.sendMessage(ChatColor.GREEN + "Equipped Kit " + name);
                            } else {
                                sender.sendMessage(ChatColor.RED + "Insufficient Permissions");
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "Invalid Kit Name");
                        }
                    }

                } else {
                    player.sendMessage(ChatColor.YELLOW + "Incorrect Syntax. /kit <KitName>");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            sender.sendMessage(ChatColor.RED + "This command can only be run in-game");
        }

        return true;
    }

}
