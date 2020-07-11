package net.lexonje.dero.game.commands;

import net.lexonje.dero.Plugin;
import net.lexonje.dero.config.ConfigManager;
import net.lexonje.dero.config.ConfigUtilities;
import net.lexonje.dero.config.PlayerConfig;
import net.lexonje.dero.game.GameSystem;
import net.lexonje.dero.game.Transporter;
import net.lexonje.dero.game.spectator.SpectatorManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReviveCommand implements CommandExecutor {

    private ConfigManager configManager;
    private GameSystem gameSystem;
    private SpectatorManager spectatorManager;
    private Transporter transporter;

    public ReviveCommand(ConfigManager configManager, GameSystem gameSystem, SpectatorManager spectatorManager, Transporter transporter) {
        this.configManager = configManager;
        this.gameSystem = gameSystem;
        this.spectatorManager = spectatorManager;
        this.transporter = transporter;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //sender.sendMessage(Plugin.prefix + " §7| §fDieser Command ist zurzeit ausgeschalten.");
        if (sender.hasPermission("dero.revive")) {
            if (args.length == 1) {
                Player player = Bukkit.getPlayer(args[0]);
                if (player != null) {
                    PlayerConfig config = configManager.getPlayerConfig();
                    if (config.isDead(player)) {
                        config.set(player.getUniqueId() + ".dead", false);
                        config.save();
                        if (gameSystem.isInGame(player)) {
                            spectatorManager.unsetSpectator(player);
                            transporter.createNewPlayer(player);
                            player.sendTitle("§a§lWiederbelebt", "§fViel Spaß beim Spielen!", 20, 50, 20);
                            player.sendMessage(Plugin.prefix + " §7| §fDu wurdest §awiederbelebt§f.");
                        } else  {
                            if (player.getBedSpawnLocation() != null) {
                                ConfigUtilities.setLocationToConfig(player.getBedSpawnLocation(), configManager.getPlayerConfig(), player.getUniqueId() + ".data.location");
                            } else { //TODO ABFRAGE WEGEN NULL
                                ConfigUtilities.setLocationToConfig(ConfigUtilities.getLocationFromConfig(configManager.getPluginConfig(),
                                        "dero.location.worldspawn"), config, player.getUniqueId() + ".data.location");
                            }
                            player.sendMessage(Plugin.prefix + " §7| §fDu wurdest §awiederbelebt §fund kannst nun wieder mitspielen! Benutze einfach die Feder.");
                        }
                        sender.sendMessage(Plugin.prefix + " §7| §fDer Spieler §6§l" + args[0] + " §fwurde §awiederbelebt§f.");
                    } else
                        sender.sendMessage(Plugin.prefix + " §7| §cDer Spieler §l" + args[0] + " §cist nicht tot.");
                } else
                    sender.sendMessage(Plugin.prefix + " §7| §cDer Spieler §l" + args[0] + " §cist nicht online.");
            } else
                sender.sendMessage(Plugin.prefix + " §7| §6§lSyntax: §f/revive <Spieler>");
        } else
            sender.sendMessage(Plugin.prefix + " §7| §cDu hast nicht die Berechtigung (§ldero.revive§c) dazu!");
        return true;
    }
}
