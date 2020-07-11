package net.lexonje.dero.prevent;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class PreventionListener implements Listener {

    private PreventionSystem system;

    public PreventionListener(PreventionSystem system) {
        this.system = system;
    }

    @EventHandler
    public void onFarmDestroying(PlayerInteractEvent event) {
        World world = event.getPlayer().getWorld();
        if (system.getWorldProtectionSettings(world)[0]) {
            if (event.getAction().equals(Action.PHYSICAL) &&
                    event.getClickedBlock().getType().equals(Material.FARMLAND))
                event.setCancelled(true); //Lass ich so
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        World world = event.getPlayer().getWorld();
        if (system.getWorldProtectionSettings(world)[1]) {
            if (!system.containsBuilder(event.getPlayer()))
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventorySort(InventoryClickEvent event) {
        if (event.getClickedInventory() != null &&
                event.getClickedInventory().equals(event.getWhoClicked().getInventory())) {
            World world = event.getWhoClicked().getWorld();
            if (system.getWorldProtectionSettings(world)[2]) {
                if (!system.containsBuilder((Player) event.getWhoClicked()))
                    event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemSwap(PlayerSwapHandItemsEvent event) {
        World world = event.getPlayer().getWorld();
        if (system.getWorldProtectionSettings(world)[3]) {
            if (!system.containsBuilder(event.getPlayer()))
                event.setCancelled(true);
        }
    }


    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        World world = event.getEntity().getWorld();
        if (system.getWorldProtectionSettings(world)[4]) {
            if (event.getEntity() instanceof Player) {
                if (!system.containsBuilder((Player) event.getEntity()))
                    event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        World world = event.getPlayer().getWorld();
        if (system.getWorldProtectionSettings(world)[5]) {
            if (!system.containsBuilder(event.getPlayer()))
                event.setCancelled(true);
        }
    }
}
