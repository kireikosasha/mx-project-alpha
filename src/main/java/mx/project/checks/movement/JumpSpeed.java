package mx.project.checks.movement;

import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import mx.project.Mx_project;
import mx.project.api.flag;
import mx.project.api.math;
import mx.project.api.ml.learning;
import mx.project.api.move;
import mx.project.api.read;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static mx.project.Mx_project.*;
import static mx.project.api.flag.oldposflag;
import static mx.project.api.move.*;

public class JumpSpeed {

    public static List<Number> machine = new ArrayList<>();
    public static HashMap<Player, Double> local_vl_data = new HashMap<>();
    public static boolean enable = false;
    static double vl;
    static double vl_stop;
    static double vl_fader;
    static double local_vl_stop;
    static double local_vl_fader;
    static double local_vl;
    static boolean advanced_support;

    public void onEnable() {
        enable = read.readCfgSection("MachineLearning").getConfigurationSection("JumpSpeed").getBoolean("enable");
        vl = read.readCfgSection("MachineLearning").getConfigurationSection("JumpSpeed").getDouble("vl");
        vl_stop = read.readCfgSection("MachineLearning").getConfigurationSection("JumpSpeed").getDouble("vlstop");
        vl_fader = read.readCfgSection("MachineLearning").getConfigurationSection("JumpSpeed").getDouble("vlfader");

        local_vl = read.readCfgSection("MachineLearning").getConfigurationSection("JumpSpeed").getDouble("localvl");
        local_vl_stop = read.readCfgSection("MachineLearning").getConfigurationSection("JumpSpeed").getDouble("localvlstop");
        local_vl_fader = read.readCfgSection("MachineLearning").getConfigurationSection("JumpSpeed").getDouble("localvlfader");

        advanced_support = read.readCfgSection("MachineLearning").getConfigurationSection("JumpSpeed").getBoolean("advanced_support");

    }

    public static void jumpSpeedEvent(PacketEvent event) {
        Player player = event.getPlayer();
        Location location = event.getPlayer().getLocation();
        double y_factor = math.scaleVal(moveYdist.get(player), 2);
        List<Number> proceed_val;
        List<Number> proceed_val_ap;
        Material m = location.getBlock().getType();
        if (airsession.get(player).longValue() < jumpspeed.size() && !player.isOnGround()
         && !m.equals(Material.STATIONARY_WATER) && !m.equals(Material.WATER) && !player.isFlying() && !player.isGliding()
         && saveballfalluse.get(player) < 1 && !playerIsFallingOnSlimeBlock(player,
                new BlockPosition(player.getLocation().getBlockX(), (player.getLocation().getBlockY() - 1), player.getLocation().getBlockZ()))) {
            Long id = airsession.get(player).longValue();
            proceed_val = jumpspeed.get(id);
            proceed_val_ap = jumpspeedap.get(id);
            // learning.putLearn(1, (Long) airsession.get(player), y_factor);
            if (player.hasPotionEffect(PotionEffectType.JUMP)) {
                if (read.realContains(proceed_val, y_factor) || read.realContains(proceed_val_ap, y_factor)) {
                    LocalFlagPlayerFader(player);
                } else {
                    // player.sendMessage(proceed_val.toString());
                    // learning.putLearn(1, (Long) airsession.get(player), y_factor);
                    if (advanced_support && y_factor < 1.2) {
                        LocalFlagPlayer(player);
                    } else {
                        if (!advanced_support) {
                            LocalFlagPlayer(player);
                        }
                    }

                }
            } else {
                if (read.realContains(proceed_val, y_factor)) {
                    LocalFlagPlayerFader(player);
                } else {
                    // player.sendMessage(proceed_val.toString());
                    // learning.putLearn(1, (Long) airsession.get(player), y_factor);
                    if (advanced_support && y_factor < 1.2) {
                        LocalFlagPlayer(player);
                    } else {
                        if (!advanced_support) {
                            LocalFlagPlayer(player);
                        }
                    }

                }
            }
        }

    }
    private static boolean calculateVal(double val, double target, double brutality, double balancer) {

        double getValue = Math.abs(target - val);
        double getBrutality = brutality + balancer;
        return getValue <= getBrutality;

    }
    public static void LocalFlagPlayer(Player player) {
        double vl_add = (float) (local_vl_data.get(player) + local_vl);
        local_vl_data.put(player, vl_add);
        if (vl_add >= local_vl_stop) {
            Location location = player.getLocation();
            oldposflag.put(player, location.set(location.getX(), Math.floor(location.getY()), location.getZ()));
            flag.FlagPlayer("JumpSpeed", player, vl, vl_stop);
        }
    }
    public static void LocalFlagPlayerFader(Player player) {
        if (local_vl_data.get(player) > 0) {
            local_vl_data.put(player, local_vl_data.get(player) - local_vl_fader);
            flag.FlagPlayerFader(player, "JumpSpeed", vl_fader);
        }
    }
}
