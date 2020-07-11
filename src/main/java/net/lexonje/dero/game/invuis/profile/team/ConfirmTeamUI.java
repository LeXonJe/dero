package net.lexonje.dero.game.invuis.profile.team;

import net.lexonje.dero.Plugin;
import net.lexonje.dero.config.ConfigManager;
import net.lexonje.dero.game.GameItems;
import net.lexonje.dero.items.ActionSystem;
import net.lexonje.dero.items.InventoryUI;
import net.lexonje.dero.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ConfirmTeamUI extends InventoryUI {

    private String teamName;
    private String teamPrefix;
    private Player teamOwner;

    private ActionSystem actionSystem;
    private ConfigManager configManager;

    public ConfirmTeamUI(String teamName, String teamPrefix, Player teamOwner, ActionSystem actionSystem, ConfigManager configManager) {
        super("§6§lTeamerstellung", 0);
        this.teamName = teamName;
        this.teamPrefix = teamPrefix;
        this.teamOwner = teamOwner;
        this.actionSystem = actionSystem;
        this.configManager = configManager;
    }

    @Override
    public Inventory getInv(Player player) {
        Inventory inv = Bukkit.createInventory(null, InventoryType.DROPPER, getName());
        ItemStack infoItem = new ItemBuilder(Material.PAPER)
                .setName("§d§lÜberblick")
                .setLore("§fNeues Team erstellen:", " §7● §eName: §f" + teamName, " §7● §ePrefix: §f" + teamPrefix,
                        " §7● §ferstellt durch §e" + teamOwner.getName())
                .build();
        for (int i = 0; i < 3; i++)
            inv.setItem(i, GameItems.blackItem);
        inv.setItem(3, GameItems.agreeItem);
        inv.setItem(4, infoItem);
        inv.setItem(5, GameItems.disagreeItem);
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
                case RED_CONCRETE: {
                    player.closeInventory();
                    player.sendMessage(Plugin.prefix + " §7| §fDie Teamerstellung wurde §cabgebrochen§f.");
                    actionSystem.removeInventoryAction(this);
                    break;
                }
                case GREEN_CONCRETE: {
                    player.closeInventory();
                    configManager.getTeamConfig().createTeam(teamName, teamPrefix, teamOwner.getUniqueId());
                    player.sendMessage(Plugin.prefix + " §7| §fDein Team §3" + teamName + " §fwurde §2erfolgreich §ferstellt.");
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
