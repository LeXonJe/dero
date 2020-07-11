package net.lexonje.dero.game.invuis.profile.coins;

import net.lexonje.dero.config.ConfigManager;
import net.lexonje.dero.game.GameItems;
import net.lexonje.dero.items.InventoryUI;
import net.lexonje.dero.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class CoinsUI extends InventoryUI {

    private InventoryUI profileUI;
    private ConfigManager configManager;

    public CoinsUI(InventoryUI profileUI, ConfigManager configManager) {
        super("§6§lMünzenverwaltung", 9*3);
        this.profileUI = profileUI;
        this.configManager = configManager;
    }

    @Override
    public Inventory getInv(Player player) {
        Inventory inv = Bukkit.createInventory(null, getRows(), getName());
        inv.setItem(18, GameItems.backItem);
        inv.setItem(11, new ItemBuilder(Material.GOLD_INGOT).setName("§c§lKontostand").setLore("§e" +
                configManager.getPlayerConfig().getCoins(player) + " §fMünzen").setMagic(true).build());
        inv.setItem(15, GameItems.transactionItem);
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
