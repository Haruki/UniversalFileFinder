package com.pimpelkram.uff.test;

import org.yaml.snakeyaml.Yaml;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Homer on 11.06.2017.
 */
public class YamlTest {
    public static void main(String[] args) {
        Path configPath = Paths.get("/testConfig.yml");
/*            WatchService watcher = configPath.getFileSystem().newWatchService();
 */
        Config config;
        Yaml yaml = new Yaml();
        config = yaml.loadAs(MainTest.class.getResourceAsStream("/testConfig.yml"), Config.class);
        System.out.println(config);
        for (String dir : config.directories) {
            System.out.println(dir);

        }


    }
}
