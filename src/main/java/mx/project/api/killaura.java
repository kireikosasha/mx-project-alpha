package mx.project.api;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import mx.project.Mx_project;

import static mx.project.Mx_project.cfg;
import static mx.project.checks.combat.KillauraB.packetGetterKillauraB;

public class hit_write extends PacketAdapter {
    public hit_write() {
        super(Mx_project.getInstance(),
                PacketType.Play.Client.USE_ENTITY);

    }
    public static boolean killauraB = (boolean) cfg.get("KillauraB");

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Mx_project.getInstance().getLogger().info("lox");
        if (killauraB) {
            packetGetterKillauraB(event);
        }
        super.onPacketReceiving(event);
    }
}
