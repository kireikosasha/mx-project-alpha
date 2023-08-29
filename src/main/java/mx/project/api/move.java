package mx.project.api;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import mx.project.Mx_project;
import org.bukkit.entity.Player;

import static mx.project.api.flag.blocker;
import static mx.project.checks.combat.KillauraForced.packetGetterKillauraForced;
import static mx.project.checks.combat.KillauraVM.packetGetterKillauraVM;

public class killaura extends PacketAdapter {
    public killaura() {
        super(Mx_project.getInstance(),
                PacketType.Play.Client.USE_ENTITY);

    }
    static boolean killauraForced = true;
    static boolean killauraVM = true;

    public void onEnable() {
        killauraForced = Mx_project.getInstance().getConfig().getConfigurationSection("KillauraForced").getBoolean("enable");
        killauraVM = Mx_project.getInstance().getConfig().getConfigurationSection("KillauraVM").getBoolean("enable");
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        if(blocker.containsKey(event.getPlayer())) {
            if(blocker.get(player) > 0) {
                blocker.put(player, blocker.get(player) - 1);
                event.setCancelled(true);
            } else {
                event.setCancelled(false);
            }
        } else { event.setCancelled(false); }
        if (killauraForced) {
            packetGetterKillauraForced(event);
        }
        if (killauraVM) {
            packetGetterKillauraVM(event);
        }
    }

}



