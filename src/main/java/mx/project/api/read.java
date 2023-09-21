package mx.project.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import mx.project.Mx_project;
import org.apache.commons.io.IOUtils;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

public class read {

    public static String readAssets(String filename) {
        try {
            InputStream inputStream = Mx_project.class.getResourceAsStream("/" + filename);

            if (inputStream != null) {
                return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            } else {
                return "Failed!";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filename;
    }
    public static ConfigurationSection readCfgSection(Object key) {
        return Mx_project.getInstance().getConfig().getConfigurationSection((String) key);
    }
    public static boolean realContains(List<Number> list, double value) {
        for (Number number : list) {
            String value_s = String.valueOf(Math.abs(value));
            double listval = Math.abs(number.doubleValue());
            if (String.valueOf(listval).equals(value_s) && listval == Math.abs(value)) {
                return true;
            }
        }
        return false;
    }
}
