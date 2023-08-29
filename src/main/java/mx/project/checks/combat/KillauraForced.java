package mx.project.checks.combat;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketEvent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.var;
import mx.project.Mx_project;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static mx.project.api.flag.*;
import static mx.project.api.read.readAssets;
import static mx.project.api.read.readCfgSection;

public class KillauraForced {

    static List<Double> data = new ArrayList<>();

    private static ProtocolManager protocolManager;

    private static String ai = "";

    public void onEnable() {
        protocolManager = ProtocolLibrary.getProtocolManager();
        data = new Gson().fromJson(readAssets("killauraforced.ai"), new TypeToken<List<Double>>(){}.getType());
    }

    public static void packetGetterKillauraForced(PacketEvent event) {
        Player player = event.getPlayer();
        float pitch = event.getPlayer().getLocation().getPitch();
        double scale = Math.pow(10, 4);
        double result = Math.ceil(pitch * scale) / scale;
        double scale2 = Math.pow(10, 3);
        double result2 = Math.ceil(pitch * scale2) / scale2;
        // ai = ai.concat(", " + result + ", " + result2);
        // Mx_project.getInstance().getLogger().info(ai);
        double stop = Mx_project.getInstance().getConfig().getConfigurationSection("KillauraForced").getDouble("vlstop");
        if (data.contains(result) || data.contains(result2)) {
            oldposflag.put(player, player.getLocation());
            FlagPlayer("KillauraForced", event.getPlayer(), (float) readCfgSection("KillauraForced").getDouble("vl"), stop);
        } else {
            if (reason.containsKey(event.getPlayer())) {
                if (reason.get(event.getPlayer()).equals("KillauraForced") && vl.get(event.getPlayer()) > 0) {
                    var fader = Mx_project.getInstance().getConfig().getConfigurationSection("KillauraForced").getDouble("vlfader");
                    vl.put(event.getPlayer(), (float) (vl.get(event.getPlayer()) - fader));
                }
            }
        }
    }

}
