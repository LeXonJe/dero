package net.lexonje.dero.game;

import net.lexonje.dero.Plugin;
import net.lexonje.dero.config.ConfigManager;
import net.lexonje.dero.config.ConfigUtilities;
import net.lexonje.dero.config.PlayerConfig;
import net.lexonje.dero.config.PluginConfig;
import net.lexonje.dero.game.spectator.SpectatorManager;
import net.lexonje.dero.utils.Helper;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Calendar;
import java.util.TimeZone;


public class Transporter {

    private PlayerConfig playerConfig;
    private PluginConfig pluginConfig;
    private GameSystem gameSystem;
    private SpectatorManager spectatorManager;

    public Transporter(GameSystem gameSystem, ConfigManager configManager, SpectatorManager spectatorManager) {
        this.playerConfig = configManager.getPlayerConfig();
        this.pluginConfig = configManager.getPluginConfig();
        this.gameSystem = gameSystem;
        this.spectatorManager = spectatorManager;

    }

    public void transportPlayerToGame(Player player) {
        if (playerConfig.isDead(player)) {
            int[] date = ConfigUtilities.getDateFromConfig(playerConfig, player.getUniqueId() + ".dead_time");
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CEST"));;
            if (calendar.get(Calendar.DAY_OF_MONTH) > date[0] || calendar.get(Calendar.MONTH) > date[1] || calendar.get(Calendar.YEAR) > date[2]) {
                createNewPlayer(player);
                player.sendMessage(Plugin.prefix + " §7| §fDu wurdest aufgrund des nächsten Tages §awiederbelebt§f.");
            } else {
                player.teleport(playerConfig.getSpectatorLocation(player));
                spectatorManager.setSpectator(player, true);
            }
        } else {
            playerConfig.login(player);
            initGameSettings(player);
        }
        if (!gameSystem.isInGame(player))
            gameSystem.addPlayerToGame(player);
    }

    public void createNewPlayer(Player player) {
        if (pluginConfig.get("dero.location.worldspawn.world") != null) {
            playerConfig.set(player.getUniqueId() + ".data.hearts", (double) 20);
            playerConfig.set(player.getUniqueId() + ".data.foodlevel", 20);
            playerConfig.set(player.getUniqueId() + ".data.xplevel", 0);
            playerConfig.set(player.getUniqueId() + ".data.xpbar", 0f);
            playerConfig.set(player.getUniqueId() + ".dead", false);
            player.getInventory().clear();
            Helper.clearAllEffects(player);
            if (player.getBedSpawnLocation() != null) {
                ConfigUtilities.setLocationToConfig(player.getBedSpawnLocation(), playerConfig, player.getUniqueId() + ".data.location");
            } else {
                ConfigUtilities.setLocationToConfig(ConfigUtilities.getLocationFromConfig(pluginConfig,
                        "dero.location.worldspawn"), playerConfig, player.getUniqueId() + ".data.location");
            }
            ConfigUtilities.setInventoryToConfig(player.getInventory(), playerConfig,
                    player.getUniqueId() + ".data.inventory");
            ConfigUtilities.setEffectsToConfig(player.getActivePotionEffects(), playerConfig,
                    player.getUniqueId() + ".data.effects");
            playerConfig.set(player.getUniqueId() + ".data.newuser", false);
            playerConfig.save();
            transportPlayerToGame(player);
        } else {
            player.sendMessage(Plugin.prefix + " §7| §cUps! Da ist etwas schiefgelaufen. Bitte kontaktiere einen " +
                    "Admin zur Lösung des Problems. (Fehler: Weltspawn wurde nicht gesetzt)");
        }
    }

    public void transportPlayerToHub(Player player) {
        if (gameSystem.isInGame(player)) {
            if (spectatorManager.isSpectator(player)) {
                playerConfig.setSpectatorPosition(player);
                spectatorManager.unsetSpectator(player);
            } else {
                playerConfig.saveLogout(player);
            }
            gameSystem.removePlayerFromGame(player);
            initHubUI(player, false);
        } else
            player.sendMessage(Plugin.prefix + " §7| §cUps! Da ist etwas schiefgelaufen. Bitte kontaktiere einen " +
                    "Admin zur Lösung des Problems. (Fehler: Spieler ist nicht im Spiel)");
    }

    public void initGameSettings(Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        player.setInvulnerable(false);
        player.setSilent(false);
        player.setCanPickupItems(true);
        player.setSleepingIgnored(false);
    }

    public void initHubUI(Player player, boolean visitor) {
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
        Helper.clearAllEffects(player);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setExp(0);
        player.setLevel(0);
        player.setInvulnerable(true);
        player.setSilent(true);
        player.setCanPickupItems(false);
        player.setSleepingIgnored(true);
        if (pluginConfig.getString("dero.location.lobbyspawn.world") != null) {
            player.teleport(ConfigUtilities.getLocationFromConfig(pluginConfig,
                    "dero.location.lobbyspawn"));
        } else {
            player.sendMessage(Plugin.prefix + " §7| §cUps! Da ist etwas schiefgelaufen. Bitte kontaktiere einen " +
                    "Admin zur Lösung des Problems. (Fehler: Lobbyspawn konnte nicht gefunden werden)");
        }
        if (!visitor) {
            player.getInventory().setItem(0, GameItems.joinItem);
            player.getInventory().setItem(8, GameItems.profilItem);
        }
        player.getInventory().setItem(4, GameItems.navigator);
        player.getInventory().setItem(22, GameItems.infoItem);
        player.getInventory().setHeldItemSlot(4);
    }
}
