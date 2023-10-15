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
import static org.bukkit.Material.*;


public class MoveABCD {


    final static double basicY = 1.253;
    public static HashMap<Object, Object> cfg = new HashMap<>();
    public static HashMap<Player, Location> oldconfirmpos = new HashMap<>();
    public static HashMap<Player, Boolean> oldfallpos = new HashMap<>();
    public static HashMap<Player, Double> oldydist = new HashMap<>();
    public static HashMap<Player, Integer> local_c = new HashMap<>();

    public void onEnable() {
        moveabcdinitialize();
    }

    public static void moveCheck(PacketEvent event, int index) throws InterruptedException {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        boolean flagged = false;
        if (index == 2 || index == 1) {
            if ((boolean) cfg.get("B")) {
                Material material = location.add(0, -1, 0).getBlock().getType();
                double y = location.getY() + 1;
                double old_y = oldconfirmpos.get(player).getY();
                if (player.isOnGround() && y == old_y) {
                    oldfallpos.put(player, false);
                }
                if (y > old_y + (saveballfalluse.get(player) * 1.3)) {
                    if (oldfallpos.get(player)) {
                        flag.FlagPlayer("MoveB", player, (double) cfg.get("Bvl"), (double) cfg.get("Bvlstop"));
                        flagged = true;
                    }
                } else {
                    if (y != old_y) {
                        oldposflag.put(player, oldconfirmpos.get(player));
                        oldfallpos.put(player, true);
                        flag.FlagPlayerFader(player, "MoveB", (Double) cfg.get("Bvlfader"));
                    }
                }
            }
        }
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
                    actualY = basicY * (player.getPotionEffect(PotionEffectType.JUMP).getAmplifier() + 1.9) * 1.3;
                } else {
                    actualY = basicY;
                }
                if (distY > actualY + (saveballfalluse.get(player)) && !player.isOnGround()) {
                    oldposflag.put(player, oldconfirmpos.get(player));
                    flag.FlagPlayer("MoveA", player, (double) cfg.get("Avl"), (double) cfg.get("Avlstop"));
                    flagged = true;
                }
            } else {
                flag.FlagPlayerFader(player, "MoveA", (Double) cfg.get("Avlfader"));
            }

            if ((boolean) cfg.get("C") && !player.isFlying()) {
                double distY = location.getY() - oldpos.get(player).getY();
                if (distY == oldydist.get(player) && !location.getBlock().getType().equals(WATER)
                        && !location.getBlock().getType().equals(STATIONARY_WATER) &&
                        !player.isGliding() && !location.getBlock().getType().isSolid() &&
                !location.getBlock().getType().equals(VINE) && !location.getBlock().getType().equals(LADDER)) {
                    local_c.put(player, local_c.get(player) + 1);
                    if (local_c.get(player) > (Integer) cfg.get("Cpackets")) {
                        Location oldcfpos = oldconfirmpos.get(player);
                        oldposflag.put(player, oldcfpos.set(oldcfpos.getX(), Math.floor(oldcfpos.getY()), oldcfpos.getZ()));
                        flag.FlagPlayer("MoveC", player, (double) cfg.get("Cvl"), (double) cfg.get("Cvlstop"));
                    }
                } else {
                    if (local_c.get(player) > 0) {
                        local_c.put(player, local_c.get(player) - 1);
                        flag.FlagPlayerFader(player, "MoveС", (Double) cfg.get("Cvlfader"));
                    }
                }

                oldydist.put(player, distY);
            }
        }
        if (!flagged) {
            oldconfirmpos.put(player, player.getLocation());
        }
    }

}
