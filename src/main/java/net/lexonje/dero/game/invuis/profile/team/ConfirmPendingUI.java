package net.lexonje.dero.game.invuis.profile.team;

import net.lexonje.dero.Plugin;
import net.lexonje.dero.config.ConfigManager;
import net.lexonje.dero.config.TeamConfig;
import net.lexonje.dero.game.GameItems;
import net.lexonje.dero.items.ActionSystem;
import net.lexonje.dero.items.InventoryUI;
import net.lexonje.dero.items.SkullBuilder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ConfirmPendingUI extends InventoryUI {

    private OfflinePlayer offlinePlayer;
    private TeamConfig.Team team;

    private ListPendingUI pendingUI;
    private ConfigManager configManager;
    private ActionSystem actionSystem;

    public ConfirmPendingUI(UUID uuid, TeamConfig.Team team, ListPendingUI listPendingUI, ConfigManager configManager, ActionSystem actionSystem) {
        super("§6§lSpieleranfrage", 0);
        this.offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        this.team = team;
        this.configManager = configManager;
        this.actionSystem = actionSystem;
        this.pendingUI = listPendingUI;
    }

    @Override
    public Inventory getInv(Player player) {
        Inventory inv = Bukkit.createInventory(null, InventoryType.DROPPER, getName());
        for (int i = 0; i < 3; i++)
            inv.setItem(i, GameItems.blackItem);
        inv.setItem(3, GameItems.acceptPendingItem);
        ItemStack playerInfo = new SkullBuilder(offlinePlayer)
                .setName("§e§l" + offlinePlayer.getName())
                .build();
        inv.setItem(4, playerInfo);
        inv.setItem(5, GameItems.deletePendingItem);
        for (int i = 6; i < 9; i++)
            inv.setItem(i, GameItems.blackItem);
        return inv;
    }

    @Override
    public void action(InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getCurrentItem() != null) {
            Player player = (Player) event.getWhoClicked();
            switch (event.getCurrentItem().getType()) {
                case GREEN_CONCRETE: {
                    team.removePending(offlinePlayer.getUniqueId());
                    team.addMember(offlinePlayer.getUniqueId());
                    player.openInventory(pendingUI.getInv(player, 0));
                    player.sendMessage(Plugin.prefix + " §7| §fDie Spieleranfrage von §3" + offlinePlayer.getName() +
                            " §fwurde §2angenommen§f.");
                    if (Bukkit.getPlayer(offlinePlayer.getUniqueId()) != null)
                        Bukkit.getPlayer(offlinePlayer.getUniqueId()).sendMessage(Plugin.prefix + " §7| §fDeine Spieleranfrage wurde §2angenommen§f.");
                    actionSystem.removeInventoryAction(this);
                    break;
                }
                case RED_CONCRETE: {
                    team.removePending(offlinePlayer.getUniqueId());
                    player.openInventory(pendingUI.getInv(player, 0));
                    player.sendMessage(Plugin.prefix + " §7| §fDie Spieleranfrage von §3" + offlinePlayer.getName() +
                            " §fwurde §cabgelehnt§f.");
                    if (Bukkit.getPlayer(offlinePlayer.getUniqueId()) != null)
                        Bukkit.getPlayer(offlinePlayer.getUniqueId()).sendMessage(Plugin.prefix + " §7| §fDeine Spieleranfrage wurde §cabgelehnt§f.");
                    actionSystem.removeInventoryAction(this);
                    break;
                }
            }
        }
    }

    @Override
    public void close(InventoryCloseEvent event) {
        actionSystem.removeInventoryAction(this);
    }
}
