package mx.project.api;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static mx.project.api.flag.oldposflag;

public class vmp {

    public static void downByVMP(Player player) throws InterruptedException {
        Location location = player.getLocation();
        double y = location.getY();
        double time = 30;
        while (!player.isOnGround()) {
            location = player.getLocation();
            double locationFloor = Math.floor(y);
            player.teleport(location.set(location.getX(), y, location.getZ()));
            Location location_down = location.add(0, -0.1, 0);
            Material m_down = location_down.getBlock().getType();
            if (!m_down.isSolid()) {
                player.teleport(player.getLocation().add(0, -0.1, 0));
                y -= 0.1;
                if (time <= 30 && time > 5) {
                    time -= 5;
                }
            } else {
                break;
            }
            Thread.sleep((long) time);
        }
    }
}
