package me.mazenz.kits.commands;

import me.mazenz.kits.Kits;
import net.milkbowl.vault.economy.Economy;
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
    private final Kits plugin;
    private final Economy econ;

    public KitCommand(Kits plugin, Economy econ) {
        this.plugin = plugin;
        this.econ = econ;
    }

    @Override

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1 || args.length > 2) {
            return true;
        }

        String kitName = "";

        for (String possibleKitName : Objects.requireNonNull(plugin.getConfig().getConfigurationSection("kits")).getKeys(false)) {
            if (args[0].equalsIgnoreCase(possibleKitName)) {
                kitName = args[0];
                break;
            }
        }

        boolean vaultEnabled = false;
        double price = 0;

        if (plugin.getConfig().getBoolean("vaultEnabled")) {
            vaultEnabled = true;
            price = plugin.getConfig().getDouble("kits." + kitName + ".price");
            if (!(econ.hasAccount(player))) {
                sender.sendMessage(ChatColor.RED + "You do not have a bank account that is accessible");
            }

            if (!econ.has(player, price)) {
                sender.sendMessage(ChatColor.RED + "You do not have enough money to purchase this kit");
            }
        }

        if (kitName.isEmpty()) { return true; }

        if (vaultEnabled && args.length < 2) {
                sender.sendMessage(new String[]{"Kit " + kitName + " costs $" + price + " Do you wish to complete this purchase? []", "To confirm type /kit " + kitName + " confirm"});
                return true;
        }

        if (vaultEnabled && !(args[1].equalsIgnoreCase("confirm"))) {

            sender.sendMessage(ChatColor.RED + "Invalid Syntax");
            return true;
        }

        if (plugin.getConfig().getBoolean("kitSounds")) {
            Location loc = player.getLocation();
            player.playSound(loc, BLOCK_ANVIL_PLACE, 10, 1);
        }

        if (plugin.getConfig().getBoolean("clearInventory")) {
            player.getInventory().clear();
        }

        for (String items : plugin.getConfig().getStringList("kits." + kitName + ".items")) {
            String[] itemInfo = items.split(";");
            Material type = Material.getMaterial(itemInfo[0]);
            int count = Integer.parseInt(itemInfo[1]);
            assert type != null;
            ItemStack item = new ItemStack(type, count);
            player.getInventory().addItem(item);
        }

        if (vaultEnabled) {
            econ.withdrawPlayer(player, price);
            sender.sendMessage(ChatColor.WHITE + "You have purchased kit " + ChatColor.GREEN + kitName + ChatColor.WHITE + " for " + ChatColor.GREEN + "$" + price);
            return true;
        }

        sender.sendMessage(ChatColor.WHITE + "Selected kit " + ChatColor.GREEN + kitName);

        return true;
    }
}
