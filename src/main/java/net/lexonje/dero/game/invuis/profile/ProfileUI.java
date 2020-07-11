package net.lexonje.dero.game.invuis.profile;

import net.lexonje.dero.config.ConfigManager;
import net.lexonje.dero.game.GameItems;
import net.lexonje.dero.game.invuis.profile.coins.CoinsUI;
import net.lexonje.dero.game.invuis.profile.settings.SettingsUI;
import net.lexonje.dero.game.invuis.profile.team.TeamsUI;
import net.lexonje.dero.items.ActionSystem;
import net.lexonje.dero.items.InventoryUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public class ProfileUI extends InventoryUI {

    private ConfigManager configManager;
    private CoinsUI coinsUI;
    private TeamsUI teamsUI;
    private SettingsUI settingsUI;

    public ProfileUI(ConfigManager configManager, ActionSystem actionSystem, Plugin plugin) {
        super("§6§lProfil", 9*3);
        this.configManager = configManager;
        this.teamsUI = new TeamsUI(this, actionSystem, configManager, plugin);
        this.coinsUI = new CoinsUI(this, configManager);
        this.settingsUI = new SettingsUI(this);
        actionSystem.registerInventoryAction(coinsUI);
        actionSystem.registerInventoryAction(teamsUI);
    }

    @Override
    public Inventory getInv(Player player) {
        Inventory inv = Bukkit.createInventory(null, getRows(), getName());
        inv.setItem(10, GameItems.coinsItem);
        inv.setItem(13, GameItems.teamItem);
        inv.setItem(16, GameItems.settingsItem);
        return inv;
    }

    @Override
    public void action(InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getCurrentItem() != null) {
            Player player = (Player) event.getWhoClicked();
            switch (event.getCurrentItem().getType()) {
                case GOLD_INGOT: {
                    player.openInventory(coinsUI.getInv(player));
                    break;
                }
                case IRON_HELMET: {
                    player.openInventory(teamsUI.getInv(player));
                    break;
                }
                case REDSTONE_TORCH: {
                    player.openInventory(settingsUI.getInv(player));
                    break;
                }
            }
        }
    }

    @Override
    public void close(InventoryCloseEvent event) {

    }
}
