package mx.project.checks.combat;


import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketEvent;
import lombok.var;
import mx.project.Mx_project;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import mx.project.api.*;

import java.util.HashMap;

import static mx.project.api.flag.*;
import static mx.project.api.math.getD;
import static mx.project.api.primitives.killauravm.killauravminitialize;

public class KillauraVM {

    public static boolean basic = true;
    public static boolean point = true;
    public static HashMap<Object, Object> cfg = new HashMap<>();
    public static HashMap<Player, Double> buffer = new HashMap<>();
    public static HashMap<Player, Double> buffer2 = new HashMap<>();

    public static HashMap<Player, Double> bufferexpress = new HashMap<>();


    public void initialize() {
        killauravminitialize();
    }

    public static void packetGetterKillauraVM(PacketEvent event) {
        Entity entity = ProtocolLibrary.getProtocolManager().getEntityFromID(event.getPlayer().getWorld(), event.getPacket().getIntegers().read(0));
        Player player = event.getPlayer();
        double victimY = entity.getLocation().getY();
        double Y = event.getPlayer().getLocation().getY();
        double pitch = event.getPlayer().getLocation().getPitch();
        double yaw = event.getPlayer().getLocation().getYaw();
        double d = getD(player.getLocation().getX(), entity.getLocation().getX(), player.getLocation().getZ(), entity.getLocation().getZ());
        if (basic) {
            double stop = (double) cfg.get("defaultVLStop");
            double offset = (double) cfg.get("defaultOffset");
            var calculate = victimY - Y;
            var result = 14.19941 * calculate;
            if (!buffer2.containsKey(event.getPlayer())) {
                buffer2.put(event.getPlayer(), result);
                bufferexpress.put(event.getPlayer(), pitch);
            } else {
                double centerOP = Math.abs(pitch);
                if (calculateVal(pitch, result, (double) cfg.get("defaultBrutality"), (double) cfg.get("defaultBalancerincreace"))
                        && !calculateVal(yaw, buffer2.get(player), (double) cfg.get("defaultVLfactor"), 0) && centerOP > offset &&
                        !calculateVal(pitch, bufferexpress.get(player), (double) cfg.get("defaultExpress"), 0)
                ) {
                    boolean allowed_express = false;
                    if (Math.abs(pitch) >= (double) cfg.get("defaultAirExpress") && !player.isOnGround()) {
                        allowed_express = true;
                    }
                    if (vl.containsKey(player)) {
                        if (vl.get(player) > 5) {
                            allowed_express = true;
                        }
                    }
                    if (allowed_express) {
                        if ((boolean) cfg.get("defaultBalancer")) {
                            double vl_final;
                            float brutality = (float) cfg.get("defaultBrutality");
                            float balancerIncreace = (float) cfg.get("defaultBalancerincreace");
                            float balancerDecreace = (float) cfg.get("defaultBalancerdecreace");
                            float balancerIncreaceMulti = (float) cfg.get("defaultBalancerincreacemulti");
                            float balancerDecreaceMulti = (float) cfg.get("defaultBalancerdecreacemulti");

                            var getValue = Math.abs(result - pitch);
                            if (getValue > brutality) {
                                var getResult = getValue - brutality;
                                var per = getResult / balancerIncreace;
                                double multipler;
                                if ((balancerIncreaceMulti * per) < 1) {
                                    multipler = 1;
                                } else {
                                    multipler = balancerIncreaceMulti * per;
                                }
                                var final_vl = (float) cfg.get("defaultVL") / multipler;
                                oldposflag.put(player, player.getLocation());
                                flag.FlagPlayer("KillauraVMdefault", event.getPlayer(), (float) final_vl, stop);
                            } else {
                                if (brutality > balancerDecreace) {
                                    oldposflag.put(player, player.getLocation());
                                    flag.FlagPlayer("KillauraVMdefault", event.getPlayer(), (float) cfg.get("defaultVL"), stop);
                                } else {
                                    var getResult = balancerDecreace - brutality;
                                    var per = balancerDecreace / brutality;
                                    double multipler;
                                    if ((balancerDecreaceMulti * per) < 1) {
                                        multipler = 1;
                                    } else {
                                        multipler = balancerDecreaceMulti * per;
                                    }
                                    var final_vl = (float) cfg.get("defaultVL") * multipler;
                                    oldposflag.put(player, player.getLocation());
                                    flag.FlagPlayer("KillauraVMdefault", event.getPlayer(), (float) final_vl, stop);
                                }
                            }
                        } else {
                            oldposflag.put(player, player.getLocation());
                            flag.FlagPlayer("KillauraVMdefault", event.getPlayer(), (double) cfg.get("defaultVL"), stop);
                        }
                     }
                    }  else {
                    flag.FlagPlayerFader(player, "KillauraVMdefault", (Double) cfg.get("defaultVLFader"));
                }
                buffer2.put(event.getPlayer(), yaw);
                bufferexpress.put(event.getPlayer(), pitch);
            }
        }

        if (point) {
            double stop = (double) cfg.get("pointVLStop");
            double calculate = victimY - Y;
            double myX = player.getLocation().getX();
            double myZ = player.getLocation().getZ();
            double X = entity.getLocation().getX();
            double Z = entity.getLocation().getZ();
            double dx = -(myX - X);
            double dy = myZ - Z;
            double angleRad = Math.atan2(dy, dx);
            double result = Math.toDegrees(angleRad) - 90;
            double offset = (double) cfg.get("pointOffset");
            // Mx_project.getInstance().getLogger().warning("Proceed: " + yaw + " Emulate: " + result);

            if (!buffer.containsKey(event.getPlayer())) {
                buffer.put(event.getPlayer(), pitch);
            } else {
                double centerOP = Math.abs(pitch);
                if (calculateVal(yaw, result, (double) cfg.get("pointBrutality"), (double) cfg.get("pointBalancerincreace"))
                        && !calculateVal(yaw, buffer.get(player), (double) cfg.get("pointVLfactor"), 0)  && centerOP > offset) {
                    if ((boolean) cfg.get("pointBalancer")) {
                        double vl_final;
                        float brutality = (float) cfg.get("pointBrutality");
                        float balancerIncreace = (float) cfg.get("pointBalancerincreace");
                        float balancerDecreace = (float) cfg.get("pointBalancerdecreace");
                        float balancerIncreaceMulti = (float) cfg.get("pointBalancerincreacemulti");
                        float balancerDecreaceMulti = (float) cfg.get("pointBalancerdecreacemulti");

                        double getValue = Math.abs(result - yaw);
                        if (getValue > brutality) {
                            double getResult = getValue - brutality;
                            double per = getResult / balancerIncreace;
                            double multipler;
                            if ((balancerIncreaceMulti * per) < 1) {
                                multipler = 1;
                            } else {
                                multipler = balancerIncreaceMulti * per;
                            }
                            double final_vl = (float) cfg.get("pointVL") / multipler;
                            oldposflag.put(player, player.getLocation());
                            flag.FlagPlayer("KillauraVMpoint", event.getPlayer(), (float) final_vl, stop);
                        } else {
                            if (brutality > (double) balancerDecreace) {
                                oldposflag.put(player, player.getLocation());
                                flag.FlagPlayer("KillauraVMpoint", event.getPlayer(), (float) cfg.get("pointVL"), stop);
                            } else {
                                double getResult = balancerDecreace - brutality;
                                double per = balancerDecreace / brutality;
                                double multipler;
                                if (((double) balancerDecreaceMulti * per) < 1) {
                                    multipler = 1;
                                } else {
                                    multipler = balancerDecreaceMulti * per;
                                }
                                double final_vl = (float) cfg.get("pointVL") * multipler;
                                oldposflag.put(player, player.getLocation());
                                flag.FlagPlayer("KillauraVMpoint", event.getPlayer(), (float) final_vl, stop);
                            }
                        }
                    } else {
                        oldposflag.put(player, player.getLocation());
                        flag.FlagPlayer("KillauraVMpoint", event.getPlayer(), (double) cfg.get("defaultVL"), stop);
                    }
                } else {
                    flag.FlagPlayerFader(player, "KillauraVMpoint", (Double) cfg.get("pointVLFader"));
                }
                buffer.put(event.getPlayer(), pitch);
            }
        }

    }
    private static boolean calculateVal(double pitch, double target, double brutality, double balancer) {

        double getValue = Math.abs(target - pitch);
        double getBrutality = brutality + balancer;
        return getValue <= getBrutality;

    }
}
