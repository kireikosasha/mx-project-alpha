package mx.project;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import mx.project.api.killaura;
import mx.project.api.move;
import mx.project.checks.combat.KillauraForced;
import mx.project.checks.combat.KillauraVM;
import mx.project.checks.movement.JumpSpeed;
import mx.project.checks.movement.MoveABCD;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public static Boolean fork;

    public static HashMap<String, Object> mx = new HashMap<>();
    public static HashMap<String, HashMap<Long, Double>> emulation = new HashMap<>();
    public static HashMap<Long, List<Number>> jumpspeed = new HashMap<>();
    public static HashMap<Long, List<Number>> jumpspeedap = new HashMap<>();

    public void onLoad() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
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
        emulation = new Gson().fromJson(readAssets("emulation.ai"), new TypeToken<HashMap<String, HashMap<Long, List<Number>>>>(){}.getType());
        jumpspeed = new Gson().fromJson(String.valueOf(emulation.get("JumpSpeed")), new TypeToken<HashMap<Long, List<Number>>>(){}.getType());
        jumpspeedap = new Gson().fromJson(String.valueOf(emulation.get("JumpSpeedAp")), new TypeToken<HashMap<Long, List<Number>>>(){}.getType());
        version = (String) mx.get("version");
        branch = (String) mx.get("branch");
        fork = (Boolean) mx.get("fork?");
        this.getLogger().info("Version: " + version);
        this.getLogger().info("Branch: " + branch);
        if (fork) {
            this.getLogger().warning("Please note: you are using a modified build of the original MX ANTICHEAT. We are not responsible for problems caused in this build. ");
        }
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
        JumpSpeed JumpSpeed = new JumpSpeed();
        JumpSpeed.onEnable();
    }

}
