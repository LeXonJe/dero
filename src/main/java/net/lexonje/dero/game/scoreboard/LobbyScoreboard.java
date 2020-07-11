package net.lexonje.dero.game.scoreboard;

import net.lexonje.dero.game.Ranks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

public class LobbyScoreboard {

    private Scoreboard sortTabListBoard;
    private ScoreboardJoinListener joinListener;

    public LobbyScoreboard(JavaPlugin plugin) {
        this.sortTabListBoard = Bukkit.getScoreboardManager().getMainScoreboard();
        if (sortTabListBoard.getTeam("00000Admin") == null)
            this.sortTabListBoard.registerNewTeam("00000Admin");
        if (sortTabListBoard.getTeam("00001Player") == null)
            this.sortTabListBoard.registerNewTeam("00001Player");
        if (sortTabListBoard.getTeam("00002Guest") == null)
            this.sortTabListBoard.registerNewTeam("00002Guest");
        this.joinListener = new ScoreboardJoinListener(this);
        Bukkit.getPluginManager().registerEvents(joinListener, plugin);
    }

    public void registerPlayer(Player player) {
        String team = "";
        switch (Ranks.getRank(player)) {
            case ADMIN: {
                team = "00000Admin";
                break;
            }
            case PLAYER: {
                team = "00001Player";
                break;
            }
            case GUEST: {
                team = "00002Guest";
                break;
            }
        }
        sortTabListBoard.getTeam(team).addEntry(player.getName());
    }
}
