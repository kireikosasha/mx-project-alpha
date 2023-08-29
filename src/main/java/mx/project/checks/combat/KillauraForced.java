package mx.project.checks.combat;

import com.comphenix.protocol.*;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import mx.project.Mx_project;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import static mx.project.api.read.readAssets;

public class KillauraB {

    static List<Number> data = new ArrayList<>();

    private static ProtocolManager protocolManager;

    public void onEnable() {
        protocolManager = ProtocolLibrary.getProtocolManager();
        data = new Gson().fromJson(readAssets("killauraB.ai"), new TypeToken<List<Number>>(){}.getType());
    }

    public static void packetGetterKillauraB(PacketEvent a) {
        float pitch = a.getPlayer().getLocation().getPitch();
        double scale = Math.pow(10, 4);
        double result = Math.ceil(pitch * scale) / scale;
        double scale2 = Math.pow(10, 3);
        double result2 = Math.ceil(pitch * scale2) / scale2;
        if (data.contains(result) || data.contains(result2)) {

        }
    }

}
