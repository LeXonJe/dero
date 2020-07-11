package net.lexonje.dero.game.shutdown;

import net.lexonje.dero.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Calendar;
import java.util.TimeZone;

public class ShutdownScheduler implements Runnable {

    private Calendar calendar;
    private int shutdownSeconds;

    private int[] leftTime;

    public ShutdownScheduler(int hours, int minutes, int seconds) {
        this.shutdownSeconds = convertIntoSeconds(hours, minutes, seconds);
        broadcastNewTime(hours, minutes);
    }

    public void setShutdownTime(int hours, int minutes, int seconds) {
        this.shutdownSeconds = convertIntoSeconds(hours, minutes, seconds);
        broadcastNewTime(hours, minutes);
    }

    private void broadcastNewTime(int hours, int minutes) {
        Bukkit.broadcastMessage(Plugin.prefix + " §7| §cDer Server schließt um §l" + hours + ":" + minutes + " Uhr§c.");
    }

    @Override
    public void run() {
        this.calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"));
        int currentSeconds = convertIntoSeconds(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND));
        int leftSeconds = getLeftSecondsPerDay(currentSeconds, shutdownSeconds);
        leftTime = convertIntoHMinSec(leftSeconds);
        if (leftSeconds == 0) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.kickPlayer("§cDie Zeit ist abgelaufen!\n§7~*~\n§fBis zum nächsten Mal auf\n§f§lMinecraft §4§lDERO§f.");
            }
            Bukkit.shutdown();
        } else if (leftSeconds < 61) {
            if (leftSeconds < 11) {
                if (leftSeconds == 1) {
                    Bukkit.broadcastMessage(Plugin.prefix + " §7| §cDer Server schließt in §l" + leftSeconds + " Sekunde§c.");
                } else
                    Bukkit.broadcastMessage(Plugin.prefix + " §7| §cDer Server schließt in §l" + leftSeconds + " Sekunden§c.");
            } else if (leftSeconds % 15 == 0) { //60,45,30,15
                Bukkit.broadcastMessage(Plugin.prefix + " §7| §cDer Server schließt in §l" + leftSeconds + " Sekunden§c.");
            }
        } else if (leftTime[1] % 10 == 0 && leftTime[2] == 0) {
            if (leftTime[0] < 1) {
                Bukkit.broadcastMessage(Plugin.prefix + " §7| §cDer Server schließt in §l" + leftTime[1] + " Minuten§c.");
            } else if (leftTime[0] == 1) {
                if (leftTime[1] == 0) {
                    Bukkit.broadcastMessage(Plugin.prefix + " §7| §cDer Server schließt in §l" + leftTime[0] + " Stunde§c.");
                } else
                    Bukkit.broadcastMessage(Plugin.prefix + " §7| §cDer Server schließt in §l" + leftTime[0] + " Stunde §cund §l" +
                            leftTime[1] + " Minuten§c.");
            } else {
                if (leftTime[1] == 0) {
                    Bukkit.broadcastMessage(Plugin.prefix + " §7| §cDer Server schließt in §l" + leftTime[0] + " Stunden§c.");
                } else
                    Bukkit.broadcastMessage(Plugin.prefix + " §7| §cDer Server schließt in §l" + leftTime[0] + " Stunden §cund §l" +
                            leftTime[1] + " Minuten§c.");
            }
        } else if (leftSeconds == 120) {
            Bukkit.broadcastMessage(Plugin.prefix + " §7| §cDer Server schließt in §l" + leftTime[1] + " Minuten§c.");
        }
    }

    public int[] getLeftTime() {
        return leftTime;
    }

    public int convertIntoSeconds(int hours, int minutes, int seconds) {
        return seconds + minutes * 60 + hours * 3600;
    }

    public int[] convertIntoHMinSec(int seconds) {
        int leftSeconds = seconds % 60;
        int minutes = (seconds - leftSeconds) / 60;
        int leftMinutes = minutes % 60;
        int hours = (minutes - leftMinutes) / 60;
        int leftHours = hours % 24;
        return new int[] {leftHours, leftMinutes, leftSeconds};
    }

    public int getLeftSecondsPerDay(int currentSeconds, int goalSeconds) {
        int answer = goalSeconds - currentSeconds;
        if (answer < 0) {
            return 86400 + answer;
        } else
            return answer;
    }
}
