package net.lexonje.dero.game.spectator.actions;

import net.lexonje.dero.game.GameItems;
import net.lexonje.dero.game.spectator.SpectatorManager;
import net.lexonje.dero.game.spectator.ui.SpectatorTeleportInventoryUI;
import net.lexonje.dero.items.ItemAction;
import net.lexonje.dero.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SpectatorTeleportItemAction extends ItemAction {

    private SpectatorTeleportInventoryUI ui;
    private SpectatorManager spectatorManager;

    public SpectatorTeleportItemAction(SpectatorTeleportInventoryUI spectatorTeleportInventoryUI, SpectatorManager spectatorManager) {
        super(GameItems.teamTeleporterItem);
        this.ui = spectatorTeleportInventoryUI;
        this.spectatorManager = spectatorManager;
    }

    @Override
    public void action(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            Player player = event.getPlayer();
            if (spectatorManager.isSpectator(player)) {
                player.openInventory(ui.getInv(player, 0));
            } else {
                player.getInventory().remove(event.getItem());
            }
        }
    }
}
