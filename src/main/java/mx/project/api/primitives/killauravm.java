package mx.project.api.primitives;

import static mx.project.api.read.readCfgSection;
import static mx.project.checks.combat.KillauraVM.buffer;
import static mx.project.checks.combat.KillauraVM.cfg;
import static mx.project.checks.combat.KillauraVM.basic;
import static mx.project.checks.combat.KillauraVM.point;

public class killauravm {

    public static void killauravminitialize() {

        // я знаю что это костыль! мне лень менять
        cfg.clear();
        buffer.clear();
        basic = readCfgSection("KillauraVM").getConfigurationSection("default").getBoolean("enable");
        point = readCfgSection("KillauraVM").getConfigurationSection("point").getBoolean("enable");
        cfg.put("defaultVL", readCfgSection("KillauraVM").getConfigurationSection("default").getDouble("vl"));
        cfg.put("pointVL", readCfgSection("KillauraVM").getConfigurationSection("point").getDouble("vl"));
        cfg.put("defaultVLFader", readCfgSection("KillauraVM").getConfigurationSection("default").getDouble("vlfader"));
        cfg.put("pointVLFader", readCfgSection("KillauraVM").getConfigurationSection("point").getDouble("vlfader"));
        cfg.put("defaultVLStop", readCfgSection("KillauraVM").getConfigurationSection("default").getDouble("vlstop"));
        cfg.put("pointVLStop", readCfgSection("KillauraVM").getConfigurationSection("point").getDouble("vlstop"));

        cfg.put("defaultVLfactor", readCfgSection("KillauraVM").getConfigurationSection("default").getDouble("displacement factor"));
        cfg.put("pointVLfactor", readCfgSection("KillauraVM").getConfigurationSection("point").getDouble("displacement factor"));

        cfg.put("defaultBrutality", readCfgSection("KillauraVM").getConfigurationSection("default").getDouble("brutality"));
        cfg.put("pointBrutality", readCfgSection("KillauraVM").getConfigurationSection("point").getDouble("brutality"));

        cfg.put("defaultExpress", readCfgSection("KillauraVM").getConfigurationSection("default").getDouble("point express"));
        cfg.put("defaultAirExpress", readCfgSection("KillauraVM").getConfigurationSection("default").getDouble("air express offset"));

        cfg.put("defaultOffset", readCfgSection("KillauraVM").getConfigurationSection("default").getDouble("center offset"));
        cfg.put("pointOffset", readCfgSection("KillauraVM").getConfigurationSection("point").getDouble("center offset"));

        cfg.put("defaultBalancer", readCfgSection("KillauraVM").
                getConfigurationSection("default").getConfigurationSection("balancer").getBoolean("enable"));
        cfg.put("pointBalancer", readCfgSection("KillauraVM").
                getConfigurationSection("point").getConfigurationSection("balancer").getBoolean("enable"));

        cfg.put("defaultBalancerincreace", readCfgSection("KillauraVM").
                getConfigurationSection("default").getConfigurationSection("balancer").getDouble("increace"));
        cfg.put("pointBalancerincreace", readCfgSection("KillauraVM").
                getConfigurationSection("point").getConfigurationSection("balancer").getDouble("increace"));
        cfg.put("defaultBalancerdecreace", readCfgSection("KillauraVM").
                getConfigurationSection("default").getConfigurationSection("balancer").getDouble("decreace"));
        cfg.put("pointBalancerdecreace", readCfgSection("KillauraVM").
                getConfigurationSection("point").getConfigurationSection("balancer").getDouble("decreace"));

        cfg.put("defaultBalancerincreacemulti", readCfgSection("KillauraVM").
                getConfigurationSection("default").getConfigurationSection("balancer").getDouble("increace_multi"));
        cfg.put("pointBalancerincreacemulti", readCfgSection("KillauraVM").
                getConfigurationSection("point").getConfigurationSection("balancer").getDouble("increace_multi"));
        cfg.put("defaultBalancerdecreacemulti", readCfgSection("KillauraVM").
                getConfigurationSection("default").getConfigurationSection("balancer").getDouble("decreace_multi"));
        cfg.put("pointBalancerdecreacemulti", readCfgSection("KillauraVM").
                getConfigurationSection("point").getConfigurationSection("balancer").getDouble("decreace_multi"));
    }
}
