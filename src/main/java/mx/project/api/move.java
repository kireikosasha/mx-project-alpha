package mx.project.api;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import mx.project.Mx_project;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static mx.project.checks.movement.MoveABCD.moveCheck;
import static org.bukkit.Material.SLIME_BLOCK;

public class move extends PacketAdapter implements Listener {


    public static HashMap<Player, Location> oldpos = new HashMap<>();
    public static HashMap<Player, Location> oldposflydown = new HashMap<>();
    public static HashMap<Player, Location> silentflydown = new HashMap<>();
    public static HashMap<Player, Location> fadersilentflydown = new HashMap<>();
    public static HashMap<Player, Byte> keepground = new HashMap<>();
    public static HashMap<Player, Long> ballfall = new HashMap<>();
    public static HashMap<Player, Long> saveballfall = new HashMap<>();
    public static HashMap<Player, Float> saveballfalluse = new HashMap<>();

    public move() {
        super(Mx_project.getInstance(),
                PacketType.Play.Client.POSITION);

    }

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, Mx_project.getInstance());
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(Mx_project.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Server.ENTITY_STATUS) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        try {
                            if (event.getPacketType() == PacketType.Play.Server.ENTITY_STATUS
                                    && event.getPlayer().equals(ProtocolLibrary.getProtocolManager().getEntityFromID(event.getPlayer().getWorld(), event.getPacket().getIntegers().read(0)))) {
                                int entityId = event.getPacket().getIntegers().read(0);
                                byte status = event.getPacket().getBytes().read(0);

                                if (status == 2) {
                                    Mx_project.getInstance().getLogger().info(event.getPlayer().getLocation().toString());
                                }
                            }
                        } finally {

                        }

                    }
                }
        );
    }



    @EventHandler
    public void on(PlayerJoinEvent event) {
        oldposflydown.put(event.getPlayer(), event.getPlayer().getLocation());
        silentflydown.put(event.getPlayer(), event.getPlayer().getLocation());
        fadersilentflydown.put(event.getPlayer(), event.getPlayer().getLocation());
        ballfall.put(event.getPlayer(), 0L);
        saveballfall.put(event.getPlayer(), 0L);
        saveballfalluse.put(event.getPlayer(), (float) 0L);
        keepground.put(event.getPlayer(), (byte) 0);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        Location location_down = location.add(0, -0.2, 0);
        Material m = location.getBlock().getType();
        Material m_down = location_down.getBlock().getType();
        double oldY = oldposflydown.get(player).getY();
        double changeY = oldY - location.getY();
        if (changeY < 0) {
            double silentoldY = silentflydown.get(player).getY();
            double silentchangeY = silentoldY - location.getY();
            if (silentchangeY < -1) {
                ballfall.put(player, ballfall.get(player) + 1);
                silentflydown.put(player, location);
            }
        } else {
            if (ballfall.get(player) != 0) {
                saveballfall.put(player, ballfall.get(player));
            }
            ballfall.put(player, 0L);
            if (player.isOnGround() && !m_down.equals(SLIME_BLOCK)) {
                saveballfalluse.put(player, (float) 0L);
            }
        }
        if (m_down.equals(SLIME_BLOCK)) {
            saveballfalluse.put(player, Float.valueOf(saveballfall.get(player)));
        }
        if(player.isFlying() || m.equals(Material.STATIONARY_WATER) || m.equals(Material.WATER) || player.isGliding() || m_down.isSolid() || m.isSolid()) {
            oldpos.put(player, location);
        }
        if(player.isOnGround()) {
            oldpos.put(player, location);
            try {
                moveCheck(event, 1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                moveCheck(event, 2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        oldposflydown.put(player, location);
    }
    @EventHandler
    public void on(PlayerJumpEvent event) {
        oldpos.put(event.getPlayer(), event.getPlayer().getLocation());
    }

}



