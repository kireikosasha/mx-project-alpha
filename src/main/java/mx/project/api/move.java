package mx.project.api;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import mx.project.Mx_project;
import mx.project.checks.movement.MoveABCD;
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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static mx.project.checks.movement.MoveABCD.moveCheck;
import static org.bukkit.Material.*;

public class move extends PacketAdapter implements Listener {


    public static HashMap<Player, Location> oldpos = new HashMap<>();
    public static HashMap<Player, Location> oldposflydown = new HashMap<>();
    public static HashMap<Player, Location> silentoldpos = new HashMap<>();
    public static HashMap<Player, Location> keepground = new HashMap<>();
    public static HashMap<Player, Float> saveballfalluse = new HashMap<>();
    public static HashMap<Player, Boolean> slimesession = new HashMap<>();

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
                        Player player = event.getPlayer();
                        try {
                            if (event.getPacketType() == PacketType.Play.Server.ENTITY_STATUS
                                    && event.getPlayer().equals(ProtocolLibrary.getProtocolManager().getEntityFromID(event.getPlayer().getWorld(), event.getPacket().getIntegers().read(0)))) {
                                int entityId = event.getPacket().getIntegers().read(0);
                                byte status = event.getPacket().getBytes().read(0);
                                if (status == 2) {
                                    oldpos.put(event.getPlayer(), event.getPlayer().getLocation());
                                    if (saveballfalluse.get(player).equals(0F)) {
                                        saveballfalluse.put(player, 1F);
                                    }
                                }
                            }
                        } finally {  }
                    }
                    @Override
                    public void onPacketReceiving(PacketEvent event) {
                        Player player = event.getPlayer();
                        BlockPosition blockPos = event.getPacket().getBlockPositionModifier().read(0);

                        if (playerIsFallingOnSlimeBlock(player, blockPos)) {
                            double distOldToPosition = (keepground.get(player).getY() - player.getLocation().getY()) / 1.7;
                            if(distOldToPosition > 0) {
                                saveballfalluse.put(player, (float) Math.ceil(distOldToPosition));
                            }
                        }
                    }
                }
        );
    }



    @EventHandler
    public void on(PlayerJoinEvent event) {
        saveballfalluse.put(event.getPlayer(), (float) 0L);
        slimesession.put(event.getPlayer(), false);
        keepground.put(event.getPlayer(), event.getPlayer().getLocation());
        silentoldpos.put(event.getPlayer(), event.getPlayer().getLocation());
        MoveABCD.oldconfirmpos.put(event.getPlayer(), event.getPlayer().getLocation());
        MoveABCD.oldfallpos.put(event.getPlayer(), false);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        Material m = location.getBlock().getType();

        if(m.equals(Material.STATIONARY_WATER) || m.equals(Material.WATER) || player.isGliding()) {
            MoveABCD.oldfallpos.put(event.getPlayer(), false);
            if (saveballfalluse.get(player).equals(0F)) {
                saveballfalluse.put(player, 1F);
            }
            oldpos.put(player, location);
        }
        if(player.isFlying()) {
            if (saveballfalluse.get(player).equals(0F)) {
                saveballfalluse.put(player, 10F);
            }
            oldpos.put(player, location);
        }
        if(player.isOnGround() || m.isSolid()) {
            MoveABCD.oldfallpos.put(event.getPlayer(), false);
            MoveABCD.oldconfirmpos.put(event.getPlayer(), event.getPlayer().getLocation());
            MoveABCD.oldfallpos.put(event.getPlayer(), false);
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
        MoveABCD.oldfallpos.put(event.getPlayer(), false);
        oldpos.put(event.getPlayer(), event.getPlayer().getLocation());
        MoveABCD.oldconfirmpos.put(event.getPlayer(), event.getPlayer().getLocation());
        MoveABCD.oldfallpos.put(event.getPlayer(), false);
    }
    private boolean playerIsFallingOnSlimeBlock(Player player, BlockPosition blockPos) {
        Material blockType = player.getWorld().getBlockAt(blockPos.getX(), blockPos.getY(), blockPos.getZ()).getType();
        return blockType == Material.SLIME_BLOCK;
    }
    private boolean playerInBlock(Player player, BlockPosition blockPos) {
        Material blockType = player.getWorld().getBlockAt(blockPos.getX(), blockPos.getY(), blockPos.getZ()).getType();
        return blockType.isSolid();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.isFlying()) {
            oldpos.put(event.getPlayer(), event.getPlayer().getLocation());
            MoveABCD.oldfallpos.put(event.getPlayer(), false);
        }
        if (player.getLocation().getBlock().getType().equals(VINE) || player.getLocation().getBlock().getType().equals(LADDER)) {
            oldpos.put(event.getPlayer(), event.getPlayer().getLocation());
            MoveABCD.oldfallpos.put(event.getPlayer(), false);
        }
        if (playerIsFallingOnSlimeBlock(player, new BlockPosition(player.getLocation().getBlockX(), player.getLocation().getBlockY() - 1, player.getLocation().getBlockZ()))) {
            slimesession.put(event.getPlayer(), true);
            double distOldToPosition = (keepground.get(player).getY() - player.getLocation().getY()) / 1.7;
            MoveABCD.oldconfirmpos.put(event.getPlayer(), event.getPlayer().getLocation());
            MoveABCD.oldfallpos.put(event.getPlayer(), false);
            if(distOldToPosition > 0) {
                saveballfalluse.put(player, (float) Math.ceil(distOldToPosition));
            }
            MoveABCD.oldfallpos.put(event.getPlayer(), false);
        } else if (player.isOnGround() && !playerIsFallingOnSlimeBlock(player, new BlockPosition(player.getLocation().getBlockX(), (player.getLocation().getBlockY() - 2), player.getLocation().getBlockZ()))|| player.isFlying() || player.isGliding()) {
            keepground.put(player, player.getLocation());
            saveballfalluse.put(player, 0F);
            slimesession.put(event.getPlayer(), false);
            if (playerInBlock(player, new BlockPosition(player.getLocation().getBlockX(), player.getLocation().getBlockY() - 1, player.getLocation().getBlockZ()))) {
                MoveABCD.oldconfirmpos.put(event.getPlayer(), event.getPlayer().getLocation());
                MoveABCD.oldfallpos.put(event.getPlayer(), false);
            }
        }
        silentoldpos.put(player, player.getLocation());
    }
}



