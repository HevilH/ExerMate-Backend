package ExerMate.ExerMate;

import ExerMate.ExerMate.Frame.Config.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

/** SpringBoot메인실행클래스*/

@SpringBootApplication
public class ExerMateApplication implements CommandLineRunner {

    @Autowired
    NettyServer nettyServer;


    public static void main(String[] args) {
        try {
            SpringApplication.run(ExerMateApplication.class, args);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(String... args){
        /** 시간설정 */
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+9"));

        new Thread(() -> {
            try {
                nettyServer.startHttp();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                nettyServer.startWebSocket();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

}
