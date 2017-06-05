package com.pimpelkram.uff.test;

import io.reactivex.Observable;
import org.yaml.snakeyaml.Yaml;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchService;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

/**
 * Created by Homer on 05.06.2017.
 */
public class MainTest {
    public static void main(String[] args) {

        Observable<DirectoryEvent> directoryStream = Observable.create(o -> {
            Path configPath = Paths.get("/testConfig.yml");
            WatchService watcher = configPath.getFileSystem().newWatchService();
            Config config;
            Yaml yaml = new Yaml();
            config = yaml.loadAs(MainTest.class.getResourceAsStream(configPath.toString()), Config.class);
            System.out.println(config);
            for (String dir : config.directories) {
                System.out.println(dir);
                o.onNext(new DirectoryEvent(ChangeTyp.I, dir));
            }
            configPath.register(watcher, ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

        });

        directoryStream.subscribe(event -> System.out.println(event.getChangeTyp() + "  " + event.getPathName()));
    }
}
