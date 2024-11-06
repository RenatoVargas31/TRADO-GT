package com.app.tradogt.config;

import com.pusher.rest.Pusher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PusherConfig {

    @Bean
    public Pusher pusher() {
        Pusher pusher = new Pusher("1891554", "3e94d4390b9041cae871", "3bea26a1a3b5ee378cbb");
        pusher.setCluster("us2");
        pusher.setEncrypted(true);
        return pusher;
    }
}
