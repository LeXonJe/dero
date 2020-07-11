package net.lexonje.dero.items;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;

public class ActionListener implements Listener {

    private ActionSystem system;
    private HashMap<HumanEntity, InventoryUI> currentUIs;

    public ActionListener(ActionSystem system) {
        this.system = system;
        this.currentUIs = new HashMap<>();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerItemAction(PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL) {
            if (event.getPlayer().getInventory().getItemInMainHand() != null && system.getItemAction(event.getItem()) != null) {
                system.getItemAction(event.getItem()).action(event);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInventoryAction(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            InventoryUI ui = system.getInventoryUI(event.getView());
            if (ui != null) {
                currentUIs.put(event.getWhoClicked(), ui);
                ui.action(event);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerCloseInventory(InventoryCloseEvent event) {
        HumanEntity player = event.getPlayer();
        if (event.getInventory() != null && player != null && currentUIs.get(player) != null) {
            currentUIs.get(event.getPlayer()).close(event);
        }
    }
}
