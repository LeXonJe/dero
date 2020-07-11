package net.lexonje.dero.game.spectator.actions;

import net.lexonje.dero.game.GameItems;
import net.lexonje.dero.game.spectator.SpectatorManager;
import net.lexonje.dero.items.InventoryUI;
import net.lexonje.dero.items.ItemAction;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class AdminSettingsItemAction extends ItemAction {

    private SpectatorManager manager;
    private InventoryUI ui;

    public AdminSettingsItemAction(SpectatorManager manager, InventoryUI ui) {
        super(GameItems.adminToolsItem);
        this.manager = manager;
        this.ui = ui;
    }

    @Override
    public void action(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            if (manager.isAdminSpectator(player)) {
                player.openInventory(ui.getInv(player));
            } else
                player.getInventory().remove(event.getItem());
        }
    }
}
