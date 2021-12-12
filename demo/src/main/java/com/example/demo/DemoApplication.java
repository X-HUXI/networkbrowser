package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	ConfigurableApplicationContext application = SpringApplication.run(ChatApplication.class, args);
	Environment env = application.getEnvironment();
	String host = InetAddress.getLocalHost().getHostAddress();
	String port = env.getProperty("server.port");
        System.out.println("[----------------------------------------------------------]");
        System.out.println("聊天室启动成功！点击进入:\t http://" + host + ":" + port);
        System.out.println("[----------------------------------------------------------");
        SocketServer.inst().run(53134);
        }
}
