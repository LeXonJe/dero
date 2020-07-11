package net.lexonje.dero.game.spectator;

import net.lexonje.dero.config.ConfigManager;
import net.lexonje.dero.game.GameSystem;
import net.lexonje.dero.game.GameItems;
import net.lexonje.dero.game.spectator.actions.AdminSettingsItemAction;
import net.lexonje.dero.game.spectator.actions.SpectatorTeleportItemAction;
import net.lexonje.dero.game.spectator.ui.AdminSettingsUI;
import net.lexonje.dero.game.spectator.ui.AdminTeleportUI;
import net.lexonje.dero.game.spectator.ui.SpectatorTeleportInventoryUI;
import net.lexonje.dero.items.ActionSystem;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;

public class SpectatorManager {

    private ArrayList<Player> adminSpectators;
    private ArrayList<Player> spectators;
    private HashMap<Player, Boolean> interactiveAllow;
    private HashMap<Player, Boolean> visibleSpec;
    private SpectatorListener listener;
    private Plugin plugin;

    public SpectatorManager(ConfigManager configManager, ActionSystem actionSystem, GameSystem gameSystem, Plugin plugin) {
        this.adminSpectators = new ArrayList<>();
        this.spectators = new ArrayList<>();
        this.interactiveAllow = new HashMap<>();
        this.visibleSpec = new HashMap<>();
        this.plugin = plugin;
        this.listener = new SpectatorListener(this, plugin);
        Bukkit.getPluginManager().registerEvents(listener, plugin);
        SpectatorTeleportInventoryUI specTelUI = new SpectatorTeleportInventoryUI(configManager, this, gameSystem);
        AdminTeleportUI teleportUI = new AdminTeleportUI(gameSystem, this);
        AdminSettingsUI settingsUI = new AdminSettingsUI(this);
        actionSystem.registerInventoryAction(specTelUI);
        actionSystem.registerInventoryAction(teleportUI);
        actionSystem.registerInventoryAction(settingsUI);
        actionSystem.registerItemAction(new SpectatorTeleportItemAction(specTelUI, this));
        actionSystem.registerItemAction(new AdminSettingsItemAction(this, settingsUI));
    }

    public void setAdminSpectator(Player player, boolean setItems) {
        if (!isAdminSpectator(player))
            adminSpectators.add(player);
        player.setGameMode(GameMode.CREATIVE);
        player.setSilent(true);
        player.setCanPickupItems(false);
        player.setSleepingIgnored(true);
        player.setHealth(20);
        player.setFoodLevel(20);
        hideSpectator(player);
        playerShowSpectators(player);
        if (setItems) {
            Inventory inv = player.getInventory();
            inv.clear();
            inv.setItem(4, GameItems.adminToolsItem);
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                player.setAllowFlight(true);
        }, 10);
    }

    public void unsetAdminSpectator(Player player) {
        player.setSilent(false);
        player.setCanPickupItems(true);
        player.setSleepingIgnored(false);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.getInventory().clear();
        showSpectator(player);
        playerHideSpectators(player);
        adminSpectators.remove(player);
    }

    public void setSpectator(Player player, boolean setItems) {
        if (!isSpectator(player))
            spectators.add(player); //TODO Antworten
        player.setGameMode(GameMode.ADVENTURE);
        player.setInvulnerable(true);
        player.setSilent(true);
        player.setCanPickupItems(false);
        player.setSleepingIgnored(true);
        player.setHealth(20);
        player.setFoodLevel(20);
        hideSpectator(player);
        playerShowSpectators(player);
        if (setItems) {
            player.getInventory().clear();
            player.getInventory().setItem(0, GameItems.teamTeleporterItem);
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () ->
                player.setAllowFlight(true)
        , 10);
    }

    public void unsetSpectator(Player player) {
        player.setFlying(false);
        player.setAllowFlight(false);
        player.setInvulnerable(false);
        player.setSilent(false);
        player.setCanPickupItems(true);
        player.setSleepingIgnored(false);
        spectators.remove(player);
    }

    public boolean isInteractiveAllow(Player player) {
        boolean bool = false;
        if (interactiveAllow.containsKey(player))
            bool = interactiveAllow.get(player);
        return bool;
    }

    public void setAllowInteractive(Player player, boolean bool) {
        interactiveAllow.put(player, bool);
    }

    public boolean isSpecVisible(Player player) {
        boolean bool = false;
        if (visibleSpec.containsKey(player))
            bool = visibleSpec.get(player);
        return bool;
    }

    public void setSpecVisible(Player player, boolean bool) {
        visibleSpec.put(player, bool);
    }

    public void showSpectator(Player spectator) {
        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            if (otherPlayer  != spectator && !spectators.contains(otherPlayer)) {
                otherPlayer.showPlayer(plugin, spectator);
            }
        }
    }

    public void hideSpectator(Player spectator) {
        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            if (otherPlayer  != spectator && !spectators.contains(otherPlayer)) {
                otherPlayer.hidePlayer(plugin, spectator);
            }
        }
    }

    public void playerShowSpectators(Player player) {
        for (Player otherPlayer : spectators) {
            player.showPlayer(plugin, otherPlayer);
        }
    }

    public void playerHideSpectators(Player player) {
        for (Player otherPlayer : spectators) {
            player.hidePlayer(plugin, otherPlayer);
        }
    }

    public boolean isAdminSpectator(Player player) {
        return adminSpectators.contains(player);
    }

    public boolean isSpectator(Player player) {
        return spectators.contains(player);
    }

    public ArrayList<Player> getAdminSpectators() {
        return adminSpectators;
    }

    public ArrayList<Player> getSpectators() {
        return spectators;
    }
}
