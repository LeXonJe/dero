package net.lexonje.dero.game.invuis.profile.settings;

import net.lexonje.dero.game.GameItems;
import net.lexonje.dero.items.InventoryUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class SettingsUI extends InventoryUI {

    private InventoryUI profileUI;

    public SettingsUI(InventoryUI profileUI) {
        super("§4§lEinstellungen", 9*3);
        this.profileUI = profileUI;
    }

    @Override
    public Inventory getInv(Player player) {
        Inventory inv = Bukkit.createInventory(null, getRows(), getName());
        inv.setItem(18, GameItems.backItem);
        inv.setItem(13, GameItems.comingSoon);
        return inv;
    }

    @Override
    public void action(InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getCurrentItem() != null) {
            Player player = (Player) event.getWhoClicked();
            switch (event.getCurrentItem().getType()) {
                case EMERALD: {
                    player.openInventory(profileUI.getInv(player));
                    break;
                }
            }
        }
    }

    @Override
    public void close(InventoryCloseEvent event) {

    }
}
