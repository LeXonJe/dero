package net.lexonje.dero.game.actions;

import net.lexonje.dero.game.GameItems;
import net.lexonje.dero.items.InventoryUI;
import net.lexonje.dero.items.ItemAction;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class NavigatorAction extends ItemAction {

    private InventoryUI ui;

    public NavigatorAction(InventoryUI inventoryUI) {
        super(GameItems.navigator);
        this.ui = inventoryUI;
    }

    @Override
    public void action(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            event.getPlayer().openInventory(ui.getInv(event.getPlayer()));
        }
    }
}
