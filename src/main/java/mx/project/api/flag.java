package mx.project.api;

import lombok.var;
import mx.project.Mx_project;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashMap;

import static mx.project.api.flag.reason;
import static mx.project.api.flag.vl;

public class flag {

    public static HashMap<Player, Float> vl = new HashMap<>();
    public static HashMap<Player, String> reason = new HashMap<>();
    public static HashMap<Player, Long> blocker = new HashMap<>();
    public static HashMap<Player, Location> oldposflag = new HashMap<>();


    public static void FlagPlayer(String reason_s, Player player, double vl_plus, double vl_stop) {
        if (!vl.containsKey(player)) {
            vl.put(player, 0F);
        }
        if (!reason.containsKey(player)) {
            reason.put(player, "dishonest actions");
        }
        float vl_add = (float) (vl.get(player) + vl_plus);
        vl.put(player, vl_add);
        reason.put(player, reason_s);
        if (vl_add > vl_stop) {
            Location location = oldposflag.get(player);
            move.ignored_locations.add(location);
            player.teleport(location);
            Mx_project.getInstance().getLogger().warning(player.getName() + " failed " + reason_s + "! VL: " + vl.get(player));
            if(blocker.containsKey(player) && reason_s.toLowerCase().contains("killaura")) {
                blocker.put(player, blocker.get(player) + 6);
            } else {
                blocker.put(player, 0L);
            }

        }
        if (vl.get(player) >= (float) Mx_project.getInstance().getConfig().getDouble("PunishVL")) {
            String command = (String) Mx_project.getInstance().getConfig().get("PunishCommand");
            command = command.replace("%player%", player.getName());
            command = command.replace("%reason%", reason_s);

            try {
                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                Bukkit.dispatchCommand(console, command);
            } catch (Throwable e) {
                e.printStackTrace();
            }

        }
    }
    public static void FlagPlayerFader(Player player, String reason_text, double vl_fader) {
        if (!reason.containsKey(player)) {
            reason.put(player, reason_text);
        }
        if (!vl.containsKey(player)) {
            vl.put(player, 0F);
        }
        if (reason.get(player).equals(reason_text) && vl.get(player) > 0) {
            vl.put(player, (float) (vl.get(player) - vl_fader));
            reason.put(player, reason_text);
        }
    }

}
