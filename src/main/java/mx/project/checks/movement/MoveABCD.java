package mx.project.checks.movement;

import com.comphenix.protocol.events.PacketEvent;
import lombok.var;
import mx.project.Mx_project;
import mx.project.api.flag;
import mx.project.api.read;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static mx.project.api.flag.*;
import static mx.project.api.move.*;
import static mx.project.api.primitives.moveabcd.moveabcdinitialize;
import static mx.project.api.vmp.downByVMP;

public class MoveABCD {

    final static double basicY = 1.2491870787446828;
    public static HashMap<Object, Object> cfg = new HashMap<>();
    public static HashMap<Player, Location> oldconfirmpos = new HashMap<>();
    public static HashMap<Player, Long> ballsbuffer = new HashMap<>();
    public void onEnable() {
        moveabcdinitialize();
    }

    public static void moveCheck(PacketEvent event, int index) throws InterruptedException {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        if (index == 2) {
            if (!oldpos.containsKey(player)) {
                oldpos.put(player, location);
            }
            if (!oldconfirmpos.containsKey(player)) {
                oldconfirmpos.put(player, location);
            }
            if ((boolean) cfg.get("A")) {
                double distY = location.getY() - oldpos.get(player).getY();
                double actualY;
                if(player.hasPotionEffect(PotionEffectType.JUMP)) {
                    actualY = basicY * (player.getPotionEffect(PotionEffectType.JUMP).getAmplifier() + 2);
                } else {
                    actualY = basicY;
                }
                if (distY > actualY + (saveballfalluse.get(player) * 3.5)) {
                    Mx_project.getInstance().getLogger().info(oldpos.get(player).getY() + " " + location.getY());
                    oldposflag.put(player, oldconfirmpos.get(player));
                    flag.FlagPlayer("MoveA", player, (double) cfg.get("Avl"), (double) cfg.get("Avlstop"));
                }
            } else {
                flag.FlagPlayerFader(player, "MoveA", (Double) cfg.get("Avlfader"));
            }
            if ((boolean) cfg.get("B")) {

            }
        }
        oldconfirmpos.put(player, player.getLocation());
    }

}
