package cn.tulingxueyuan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletContextInitializerBeans;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.util.Collection;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你.
 *
 */
@SpringBootApplication // 标记成Springboot应用
public class Application {


    public static void main(String[] args) {
        String[] newArgs = args.clone();
        int defaultPort = 8080;
        boolean needChangePort = false;
        // 1. 判断8080端口是否占用
        if (isPortInUse(defaultPort)) {
            newArgs = new String[args.length + 1];
            System.arraycopy(args, 0, newArgs, 0, args.length);
            // 2. 8088-新
            newArgs[newArgs.length - 1] = "--server.port=8088";
            needChangePort = true;
        }
        // 运行springboot
        ConfigurableApplicationContext run = SpringApplication.run(Application.class, newArgs);

        // 2. kill 8080
        if (needChangePort) {
            String command = String.format("lsof -i :%d | grep LISTEN | awk '{print $2}' | xargs kill -2", defaultPort);
            try {
                Runtime.getRuntime().exec(new String[]{"sh", "-c", command}).waitFor();
                while (isPortInUse(defaultPort)) {
                }
                ServletWebServerFactory webServerFactory = getWebServerFactory(run);
                // 当8080
                ((TomcatServletWebServerFactory) webServerFactory).setPort(defaultPort);
                WebServer webServer = webServerFactory.getWebServer(invokeSelfInitialize(((ServletWebServerApplicationContext) run)));
                // 启动tomcat
                webServer.start();

            } catch (IOException | InterruptedException ignored) {
                ignored.printStackTrace();
            }
        }

    }

    private static ServletContextInitializer invokeSelfInitialize(ServletWebServerApplicationContext context) {
        try {
            Method method = ServletWebServerApplicationContext.class.getDeclaredMethod("getSelfInitializer");
            method.setAccessible(true);
            return (ServletContextInitializer) method.invoke(context);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

    }

    private static boolean isPortInUse(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            return false;
        } catch (IOException e) {
            return true;
        }
    }


    private static ServletWebServerFactory getWebServerFactory(ConfigurableApplicationContext context) {
        String[] beanNames = context.getBeanFactory().getBeanNamesForType(ServletWebServerFactory.class);

        return context.getBeanFactory().getBean(beanNames[0], ServletWebServerFactory.class);
    }

}