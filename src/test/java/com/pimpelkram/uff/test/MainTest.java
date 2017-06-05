package com.pimpelkram.uff.test;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

/**
 * Created by Homer on 05.06.2017.
 */
public class MainTest {
    public static void  main(String[] args) {
      Config config;
        Yaml yaml = new Yaml(new Constructor(Config.class));
        config = (Config) yaml.load(MainTest.class.getResourceAsStream("/testConfig.yml"));
        System.out.println(config);
        for (String dir : config.directories) {
            System.out.println(dir);
        }
    }
}
