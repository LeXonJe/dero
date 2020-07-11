package net.lexonje.dero.game.commands;

import net.lexonje.dero.Plugin;
import net.lexonje.dero.config.ConfigManager;
import net.lexonje.dero.game.GameSystem;
import net.lexonje.dero.game.Transporter;
import net.lexonje.dero.utils.Helper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HubCommand implements CommandExecutor {

    private ConfigManager configManager;
    private Transporter transporter;
    private GameSystem gameSystem;

    public HubCommand(Transporter transporter, GameSystem gameSystem, ConfigManager configManager) {
        this.transporter = transporter;
        this.configManager = configManager;
        this.gameSystem = gameSystem;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("dero.join")) {
                if (gameSystem.isInGame(player)) {
                    transporter.transportPlayerToHub(player);
                    Helper.sendActionbar(player, "§fDu wurdest erfolgreich zum §b§lSpawn §fteleportiert.");
                } else
                    player.sendMessage(Plugin.prefix + " §7| §cDu bist bereits in der Lobby!");
            } else
                player.sendMessage(Plugin.prefix + " §7| §cDu hast nicht die Berechtigung (§ldero.join§c) dazu!");
        } else
            sender.sendMessage(Plugin.prefix + " §7| §cDiesen Kommand kann nur ein Spieler ausführen!");
        return true;
    }
}
