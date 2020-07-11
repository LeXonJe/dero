package net.lexonje.dero.game;

import net.lexonje.dero.Plugin;
import net.lexonje.dero.config.ConfigManager;
import net.lexonje.dero.game.actions.JoinAction;
import net.lexonje.dero.game.actions.NavigatorAction;
import net.lexonje.dero.game.actions.ProfilAction;
import net.lexonje.dero.game.commands.*;
import net.lexonje.dero.game.invuis.NavigatorUI;
import net.lexonje.dero.game.invuis.profile.ProfileUI;
import net.lexonje.dero.game.listeners.*;
import net.lexonje.dero.game.scoreboard.LobbyScoreboard;
import net.lexonje.dero.game.shutdown.ShutdownManager;
import net.lexonje.dero.game.spectator.SpectatorManager;
import net.lexonje.dero.items.ActionSystem;
import net.lexonje.dero.prevent.PreventionSystem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class GameSystem {

    private JavaPlugin plugin;
    private ConfigManager configManager;
    private PreventionSystem preventionSystem;
    private LobbyScoreboard lobbyScoreboard;
    private ActionSystem actionSystem;
    private SpectatorManager spectatorManager;
    private Transporter transporter;
    private ShutdownManager shutdownManager;

    private ArrayList<Player> playersInGame;

    public GameSystem(JavaPlugin plugin) {
        this.plugin = plugin;
        System.out.println("Init Configs.");
        this.configManager = new ConfigManager();
        System.out.println("Init PreventionSystem.");
        this.preventionSystem = new PreventionSystem(plugin);
        this.playersInGame = new ArrayList<>();
        this.lobbyScoreboard = new LobbyScoreboard(plugin);
        System.out.println("Init ActionSystem.");
        this.actionSystem = new ActionSystem(plugin);
        System.out.println("Init SpectatorManager.");
        this.spectatorManager = new SpectatorManager(configManager, actionSystem, this, plugin);
        System.out.println("Init Transporter.");
        this.transporter = new Transporter(this, configManager, spectatorManager);
        System.out.println("Init ShutdownManager.");
        this.shutdownManager = new ShutdownManager(configManager, (Plugin) plugin);
        System.out.println("Init Locations, Actions, other Listeners and Commands.");
        setupLocations();
        setupLobbyActions();
        registerListeners();
        registerCommands();
    }

    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isInGame(player)) {
                if (spectatorManager.isSpectator(player)) {
                    configManager.getPlayerConfig().setSpectatorPosition(player);
                    spectatorManager.unsetSpectator(player);
                } else {
                    configManager.getPlayerConfig().saveLogout(player);
                }
                System.out.println(player.getName() + " was saved!");
            }
        }
    }

    public void addPlayerToGame(Player player) {
        this.playersInGame.add(player);
    }

    public void removePlayerFromGame(Player player) {
        this.playersInGame.remove(player);
    }

    public boolean isInGame(Player player) {
        return playersInGame.contains(player);
    }

    private void setupLocations() {
        if (configManager.getPluginConfig().getString("dero.location.lobbyspawn.world") != null) {
            preventionSystem.setWorldProtection(Bukkit.getWorld(
                    configManager.getPluginConfig().getString("dero.location.lobbyspawn.world")),
                    true, true, true, true,
                    true, true);
        }
    }

    private void setupLobbyActions() {
        NavigatorUI navigatorUI = new NavigatorUI(configManager);
        ProfileUI profileUI = new ProfileUI(configManager, actionSystem, plugin);
        actionSystem.registerInventoryAction(navigatorUI);
        actionSystem.registerInventoryAction(profileUI);

        actionSystem.registerItemAction(new NavigatorAction(navigatorUI));
        actionSystem.registerItemAction(new JoinAction(transporter, this, configManager, spectatorManager));
        actionSystem.registerItemAction(new ProfilAction(profileUI));
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new JoinListener(configManager, transporter), plugin);
        pm.registerEvents(new QuitListener(this, configManager, spectatorManager), plugin);
        pm.registerEvents(new ChatListener(configManager), plugin);
        pm.registerEvents(new TeamPVPListener(configManager), plugin);
        pm.registerEvents(new DeathListener(this, configManager, spectatorManager, (Plugin) plugin), plugin);
    }

    private void registerCommands() {
        plugin.getCommand("setup").setExecutor(new SetupCommand(configManager));
        plugin.getCommand("join").setExecutor(new JoinCommand(transporter, this, configManager, spectatorManager));
        plugin.getCommand("hub").setExecutor(new HubCommand(transporter, this, configManager));
        plugin.getCommand("lastposition").setExecutor(new LastPositionCommand(configManager, this, spectatorManager));
        plugin.getCommand("revive").setExecutor(new ReviveCommand(configManager, this, spectatorManager, transporter));
        plugin.getCommand("shutdown").setExecutor(new ShutdownCommand(shutdownManager));
        plugin.getCommand("clock").setExecutor(new ClockCommand(shutdownManager));
        plugin.getCommand("rinv").setExecutor(new AdminInventoryCommand(spectatorManager));
        plugin.getCommand("spec").setExecutor(new SpectatorCommand(spectatorManager, transporter, this));
    }
}
