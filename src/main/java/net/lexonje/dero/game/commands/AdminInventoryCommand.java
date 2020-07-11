package net.lexonje.dero.game.commands;

import net.lexonje.dero.Plugin;
import net.lexonje.dero.game.GameItems;
import net.lexonje.dero.game.spectator.SpectatorManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class AdminInventoryCommand implements CommandExecutor {

    private SpectatorManager manager;

    public AdminInventoryCommand(SpectatorManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (manager.isAdminSpectator(player)) {
                Inventory inv = player.getInventory();
                inv.clear();
                inv.setItem(4, GameItems.adminToolsItem);
            } else
                sender.sendMessage(Plugin.prefix + " §7| §cDu bist kein Spectator!");
        } else
            sender.sendMessage(Plugin.prefix + " §7| §cDiesen Command kann nur ein Spieler ausführen!");
        return true;
    }
}
