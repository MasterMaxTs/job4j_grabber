package ru.job4j;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import ru.job4j.grabber.Grabber;
import ru.job4j.model.Post;
import ru.job4j.parser.Parse;
import ru.job4j.parser.html.SqlRuDateTimeParser;
import ru.job4j.parser.html.SqlRuParse;
import ru.job4j.store.PsqlStore;
import ru.job4j.store.Store;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.Properties;

public class Starter {

    private static final String SERVER_PORT = "server.port";

    public static void main(String[] args)
            throws IOException, SQLException, SchedulerException, ClassNotFoundException {
        Properties cfg = load();
        Grabber grab = new Grabber(cfg);
        Parse parse = new SqlRuParse(new SqlRuDateTimeParser());
        Store store = new PsqlStore(cfg);
        Scheduler scheduler = grab.scheduler();
        grab.init(parse, store, scheduler);
        web(store);
    }

    private static Properties load() throws IOException {
        Properties cfg = new Properties();
        try (InputStream in =
                     Starter.class.getClassLoader().getResourceAsStream("app.properties")) {
            cfg.load(in);
        }
        return cfg;
    }

    private static void web(Store store) {
        new Thread(() -> {
            try (ServerSocket server = new ServerSocket(
                    Integer.parseInt(load().getProperty(SERVER_PORT)))) {
                while (!server.isClosed()) {
                    Socket socket = server.accept();
                    try (OutputStream out = socket.getOutputStream()) {
                        out.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
                        for (Post post
                                : store.getAll()) {
                            out.write(post.toString().getBytes(
                                    Charset.forName("Windows-1251")));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
