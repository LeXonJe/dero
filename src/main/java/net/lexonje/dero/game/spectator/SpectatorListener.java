package net.lexonje.dero.game.spectator;

import net.lexonje.dero.utils.Helper;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class SpectatorListener implements Listener {

    private Plugin plugin;
    private SpectatorManager manager;
    private HashMap<Player, Boolean> specPlayer;

    public SpectatorListener(SpectatorManager spectatorManager, Plugin plugin) {
        this.manager = spectatorManager;
        this.plugin = plugin;
        this.specPlayer = new HashMap<>();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (Player otherPlayer : Bukkit.getOnlinePlayers()) { //RESET
            player.showPlayer(plugin, otherPlayer);
        }
        for (Player otherPlayer : manager.getSpectators()) {
            player.hidePlayer(plugin, otherPlayer);
        }
    }

    @EventHandler
    public void onEntityClick(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() != null) {
            Player player = event.getPlayer();
            if (manager.isSpectator(player) || manager.isAdminSpectator(player)) {
                specPlayer.put(player, true);
                player.setSneaking(false);
                player.setGameMode(GameMode.SPECTATOR);
                player.setSpectatorTarget(event.getRightClicked());
            }
        }
    }

    @EventHandler
    public void onSpectatorExit(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (specPlayer.containsKey(player) && specPlayer.get(player)) {
           if (manager.isAdminSpectator(player)) {
               manager.setAdminSpectator(player, false);
           } else {
               manager.setSpectator(player, false);
           }
           specPlayer.remove(player);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (manager.isSpectator(player))
                event.setCancelled(true);
            else if (manager.isAdminSpectator(player) && !manager.isInteractiveAllow(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (manager.isSpectator(player) || manager.isAdminSpectator(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (manager.isSpectator((Player) event.getWhoClicked())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventorySecondHand(PlayerSwapHandItemsEvent event) {
        if (manager.isSpectator(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (manager.isSpectator(player)) {
            event.setCancelled(true);
        } else if (manager.isAdminSpectator(player) && !player.getCanPickupItems()) {
            event.setCancelled(true);
            Helper.sendActionbar(player, "§cDas Aufheben und Droppen von Items ist deaktiviert!");
        }
    }

    @EventHandler
    public void onXpOrbPush(EntityTargetEvent event) {
        if (!event.isCancelled() && event.getTarget() instanceof Player) {
            Player player = (Player) event.getTarget();
            Entity entity = event.getEntity();
            if (entity instanceof ExperienceOrb) {
                if (manager.isSpectator(player)) {
                    repellExp(player, (ExperienceOrb) entity);
                    event.setCancelled(true);
                    event.setTarget(null);
                } else if (manager.isAdminSpectator(player) && !player.getCanPickupItems()) {
                    repellExp(player, (ExperienceOrb) entity);
                    event.setCancelled(true);
                    event.setTarget(null);
                }
            }
        }
    }

    private void repellExp(Player player, ExperienceOrb orb) { //Moves XP away from player
        Location playerLoc = player.getLocation();
        Location orbLoc = orb.getLocation();
        Vector direction = orbLoc.toVector().subtract(playerLoc.toVector());
        double dx = Math.abs(direction.getX());
        double dz = Math.abs(direction.getZ());
        if ( (dx == 0.0) && (dz == 0.0)){
            direction.setX(0.001);
        }
        if ((dx < 3.0) && (dz < 3.0)){
            final Vector newDir = direction.normalize();
            final Vector newVector = newDir.clone().multiply(0.3);
            newVector.setY(0);
            orb.setVelocity(newVector);
            if ((dx < 1.0) && (dz < 1.0)){
                orb.teleport(orbLoc.clone().add(newDir.multiply(1.0)), PlayerTeleportEvent.TeleportCause.PLUGIN);
            }
            if ((dx < 0.5) && (dz < 0.5)){
                orb.remove();
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (manager.isSpectator(player)) {
            event.setCancelled(true);
            if (event.getMaterial().isEdible() || event.getMaterial().isInteractable())
                Helper.sendActionbar(player, "§cAls Spectator kannst du das nicht tun!");
        } else if (manager.isAdminSpectator(player) && !manager.isInteractiveAllow(player)) {
            event.setCancelled(true);
        }
    }
}
