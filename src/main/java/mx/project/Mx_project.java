package mx.project;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import mx.project.api.killaura;
import mx.project.api.move;
import mx.project.checks.combat.KillauraForced;
import mx.project.checks.combat.KillauraVM;
import mx.project.checks.movement.MoveABCD;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.logging.Logger;

import static mx.project.api.read.readAssets;

public class Mx_project extends JavaPlugin {

    public static HashMap<String, Object> cfg = new HashMap<>();
    public static boolean killauraForced = false;
    final FileConfiguration getCfg = this.getConfig();
    private static Mx_project instance;
    public static Mx_project getInstance() {
        return instance;
    }
    public static String version;
    public static String branch;

    public static HashMap<String, Object> mx = new HashMap<>();
    private ProtocolManager protocolManager;

    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable() {

        instance = this;

        Logger log = getLogger();
        this.getLogger().info("Starting MX...");
        this.saveDefaultConfig();
        final FileConfiguration getCfg = this.getConfig();
        try {
            preBoot();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ProtocolLibrary.getProtocolManager().addPacketListener(new killaura());
        ProtocolLibrary.getProtocolManager().addPacketListener(new move());
    }

    @Override
    public void onDisable() {

    }
    public void preBoot() throws InterruptedException {
        mx = new Gson().fromJson(readAssets("mx.json"), new TypeToken<HashMap<String, Object>>(){}.getType());
        version = (String) mx.get("version");
        branch = (String) mx.get("branch");
        this.getLogger().info("Version: " + version);
        this.getLogger().info("Branch: " + branch);
        if (!this.getConfig().getBoolean("IKnowWhatImDo")) {
            if (branch.equals("beta")) {
                this.getLogger().warning("Beta Release can be unstable! Keep it up to date");
                this.getLogger().warning("6 second to start MX...");
                Thread.sleep(6000);
                this.getLogger().warning("Enabling...");
            }
            if (branch.equals("debug")) {
                this.getLogger().warning("Debug Release can be super-unstable! Keep it up to date!");
                this.getLogger().warning("15 second to start MX...");
                Thread.sleep(15000);
                this.getLogger().warning("Enabling...");
            }
        }
        bootMX();
    }
    public static void bootMX() {
        killaura killaura = new killaura();
        killaura.onEnable();
        move move = new move();
        move.onEnable();
        KillauraForced KillauraForced = new KillauraForced();
        KillauraForced.onEnable();
        KillauraVM KillauraVM = new KillauraVM();
        KillauraVM.initialize();
        MoveABCD MoveABCD = new MoveABCD();
        MoveABCD.onEnable();
    }

}
