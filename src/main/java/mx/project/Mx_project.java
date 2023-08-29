package mx.mxproject;

import org.bukkit.plugin.java.JavaPlugin;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import lombok.var;

public final class Mx_project extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getLogger().info("Starting MX...");
    }

    @Override
    public void onDisable() {

    }
    private static void readInfo() {
        String filename = "example.txt";

        try {
            // Получаем InputStream для файла с помощью getResourceAsStream.
            InputStream inputStream = Mx_project.class.getResourceAsStream("/" + filename);

            if (inputStream != null) {
                // Используем метод из Apache Commons IO для чтения содержимого файла в виде строки.
                String fileContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                System.out.println(fileContent);
            } else {
                System.out.println("Файл не найден: " + filename);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
