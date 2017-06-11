package com.pimpelkram.uff.test;

import io.reactivex.Observable;
import org.yaml.snakeyaml.Yaml;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Homer on 05.06.2017.
 */
public class MainTest {
    public static void main(String[] args) {

        Observable<DirectoryEvent> directoryStream = Observable.create(o -> {
            Path configPath = Paths.get("/testConfig.yml");
/*            WatchService watcher = configPath.getFileSystem().newWatchService();
 */
            Config config;
            Yaml yaml = new Yaml();
            config = yaml.loadAs(MainTest.class.getResourceAsStream(configPath.toString()), Config.class);
            System.out.println(config);
            for (String dir : config.directories) {
                try {
                    System.out.println(dir);
                    o.onNext(new DirectoryEvent(ChangeTyp.I, dir));
                } catch (Exception e) {
                    o.onError(e.fillInStackTrace());
                }
            }
/*            WatchKey key = configPath.register(watcher, ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
*//*/*            for (;;) {
                key.take
            }
 */
        });

        directoryStream.subscribe(event -> System.out.println(event.getChangeTyp() + "  " + event.getPathName()));
/*        directoryStream.flatMap(directoryEvent ->
                Observable.just(directoryEvent
                ).subscribeOn(Schedulers.computation())
        ).blockingSubscribe(s -> System.out.println(s));
 */
    }
}
