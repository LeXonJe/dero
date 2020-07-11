package net.lexonje.dero.game.spectator.ui;

import net.lexonje.dero.game.GameItems;
import net.lexonje.dero.game.spectator.SpectatorManager;
import net.lexonje.dero.items.InventoryUI;
import net.lexonje.dero.utils.Helper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class AdminSettingsUI extends InventoryUI {

    private SpectatorManager manager;

    public AdminSettingsUI(SpectatorManager manager) {
        super("Spectatortools", 9*3);
        this.manager = manager;
    }

    @Override
    public Inventory getInv(Player player) {
        Inventory inv = Bukkit.createInventory(null, getRows(), getName());
        if (manager.isSpecVisible(player)) {
            inv.setItem(0, GameItems.visibleOnItem);
        } else
            inv.setItem(0, GameItems.visibleOffItem);
        if (manager.isInteractiveAllow(player)) {
            inv.setItem(9, GameItems.buildOnItem);
        } else
            inv.setItem(9, GameItems.buildOffItem);
        if (player.getCanPickupItems()) {
            inv.setItem(18, GameItems.pickupOnItem);
        } else
            inv.setItem(18, GameItems.pickupOffItem);
        inv.setItem(2, GameItems.clearWeatherItem);
        inv.setItem(11, GameItems.rainWeatherItem);
        inv.setItem(20, GameItems.thunderWeatherItem);
        for (int i=1; i < 28; i+=9)
            inv.setItem(i, GameItems.blackItem);
        for (int i=3; i < 30; i+=9)
            inv.setItem(i, GameItems.blackItem);
        for (int i=12; i < 18; i++)
            inv.setItem(i, GameItems.blackItem);
        inv.setItem(22, GameItems.morningTimeItem);
        inv.setItem(23, GameItems.noonTimeItem);
        inv.setItem(24, GameItems.afternoonTimeItem);
        inv.setItem(25, GameItems.sunshineTimeItem);
        inv.setItem(26, GameItems.nightTimeItem);
        return inv;
    }

    @Override
    public void action(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (manager.isAdminSpectator(player)) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null) {
                switch (event.getCurrentItem().getType()) {
                    case GLOWSTONE_DUST: {
                        manager.hideSpectator(player);
                        manager.setSpecVisible(player, false);
                        player.openInventory(getInv(player));
                        Helper.sendActionbar(player, "§fDu bist nun §8§lunsichtbar§f.");
                        break;
                    }
                    case GUNPOWDER: {
                        manager.showSpectator(player);
                        manager.setSpecVisible(player, true);
                        player.openInventory(getInv(player));
                        Helper.sendActionbar(player, "§fDu bist nun für andere Spieler §e§lsichtbar§f.");
                        break;
                    }
                    case LIME_DYE: {
                        manager.setAllowInteractive(player, false);
                        player.openInventory(getInv(player));
                        Helper.sendActionbar(player, "§fDer interaktive Modus ist nun §c§ldeaktiviert§f.");
                        break;
                    }
                    case GRAY_DYE: {
                        manager.setAllowInteractive(player, true);
                        player.openInventory(getInv(player));
                        Helper.sendActionbar(player, "§fDer interaktive Modus ist nun §a§laktiviert§f.");
                        break;
                    }
                    case HONEY_BOTTLE: {
                        player.setCanPickupItems(false);
                        player.openInventory(getInv(player));
                        Helper.sendActionbar(player, "§fDas Aufheben von Items wurde §c§ldeaktiviert§f.");
                        break;
                    }
                    case GLASS_BOTTLE: {
                        player.setCanPickupItems(true);
                        player.openInventory(getInv(player));
                        Helper.sendActionbar(player, "§fDas Aufheben von Items wurde §a§laktiviert§f.");
                        break;
                    }
                    case PUFFERFISH_SPAWN_EGG: {
                        player.getWorld().setStorm(false);
                        player.getWorld().setThundering(false);
                        Helper.sendActionbar(player, "§fDas Wetter wurde auf §6§lSonnenschein §fgesetzt.");
                        break;
                    }
                    case VEX_SPAWN_EGG: {
                        player.getWorld().setStorm(true);
                        player.getWorld().setThundering(false);
                        Helper.sendActionbar(player, "§fDas Wetter wurde auf §3§lRegen §fgesetzt.");
                        break;
                    }
                    case PHANTOM_SPAWN_EGG: {
                        player.getWorld().setStorm(true);
                        player.getWorld().setThundering(true);
                        Helper.sendActionbar(player, "§fDas Wetter wurde auf §9§lGewitter §fgesetzt.");
                        break;
                    }
                    case OXEYE_DAISY: {
                        player.getWorld().setTime(1000L);
                        Helper.sendActionbar(player, "§fDie Zeit wurde auf §d§lMorgen §7(1000) §fgesetzt.");
                        break;
                    }
                    case DANDELION: {
                        player.getWorld().setTime(6000L);
                        Helper.sendActionbar(player, "§fDie Zeit wurde auf §d§lMittag §7(6000) §fgesetzt.");
                        break;
                    }
                    case BLUE_ORCHID: {
                        player.getWorld().setTime(9000L);
                        Helper.sendActionbar(player, "§fDie Zeit wurde auf §d§lNachmittag §7(9000) §fgesetzt.");
                        break;
                    }
                    case CORNFLOWER: {
                        player.getWorld().setTime(12400L);
                        Helper.sendActionbar(player, "§fDie Zeit wurde auf §d§lSonnenuntergang §7(12400) §fgesetzt.");
                        break;
                    }
                    case WITHER_ROSE: {
                        player.getWorld().setTime(16000L);
                        Helper.sendActionbar(player, "§fDie Zeit wurde auf §d§lNacht §7(16000) §fgesetzt.");
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void close(InventoryCloseEvent event) {

    }
}
