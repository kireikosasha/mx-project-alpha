package mx.project.api.primitives;

import java.util.HashMap;
import java.util.Map;

import static mx.project.api.read.readCfgSection;
import static mx.project.checks.movement.MoveABCD.cfg;

public class moveabcd {

    public static void moveabcdinitialize() {
        // я знаю что это костыль! мне лень менять
        cfg.clear();
        cfg.put("A", readCfgSection("Move").getConfigurationSection("A").getBoolean("enable"));
        cfg.put("Avl", readCfgSection("Move").getConfigurationSection("A").getDouble("vl"));
        cfg.put("Avlstop", readCfgSection("Move").getConfigurationSection("A").getDouble("vlstop"));
        cfg.put("Avlfader", readCfgSection("Move").getConfigurationSection("A").getDouble("vlfader"));

        cfg.put("B", readCfgSection("Move").getConfigurationSection("B").getBoolean("enable"));
        cfg.put("Bvl", readCfgSection("Move").getConfigurationSection("B").getDouble("vl"));
        cfg.put("Bvlstop", readCfgSection("Move").getConfigurationSection("B").getDouble("vlstop"));
        cfg.put("Bvlfader", readCfgSection("Move").getConfigurationSection("B").getDouble("vlfader"));
    }
}
