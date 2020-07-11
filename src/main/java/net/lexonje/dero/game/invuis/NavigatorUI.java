package net.lexonje.dero.game.invuis;

import net.lexonje.dero.Plugin;
import net.lexonje.dero.config.ConfigManager;
import net.lexonje.dero.config.ConfigUtilities;
import net.lexonje.dero.game.GameItems;
import net.lexonje.dero.items.InventoryUI;
import net.lexonje.dero.utils.Helper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class NavigatorUI extends InventoryUI {

    private ConfigManager configManager;

    public NavigatorUI(ConfigManager configManager) {
        super("§6§lNavigator", 9);
        this.configManager = configManager;
    }

    @Override
    public Inventory getInv(Player player) {
        Inventory inventory = Bukkit.createInventory(null, getRows(), getName());
        inventory.setItem(4, GameItems.spawnItem);
        inventory.setItem(6, GameItems.berndItem);
        return inventory;
    }

    @Override
    public void action(InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getCurrentItem() != null) {
            Player player = (Player) event.getWhoClicked();
            switch (event.getCurrentItem().getType()) {
                case BEACON: {
                    if (configManager.getPluginConfig().getString("dero.location.lobbyspawn.world") != null) {
                        player.teleport(ConfigUtilities.getLocationFromConfig(configManager.getPluginConfig(),
                                "dero.location.lobbyspawn"));
                        Helper.sendActionbar(player, "§fDu wurdest erfolgreich zum §b§lSpawn §fteleportiert.");
                    } else
                        event.getWhoClicked().sendMessage(Plugin.prefix + " §7| §cDer Spawn konnte nicht gefunden werden!");
                    break;
                }
            }
        }
    }

    @Override
    public void close(InventoryCloseEvent event) {

    }
}
