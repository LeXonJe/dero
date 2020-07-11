package net.lexonje.dero.game.scoreboard;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ScoreboardJoinListener implements Listener {

    private LobbyScoreboard scoreboard;

    public ScoreboardJoinListener(LobbyScoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        scoreboard.registerPlayer(event.getPlayer());
    }
}
