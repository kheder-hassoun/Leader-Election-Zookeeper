package me.zookeeper.leader_election;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZookeeperConfig {

    @Bean
    public CuratorFramework curatorFramework() {
        // Configure CuratorFramework to connect to Zookeeper
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                "192.168.10.133:2181", // Zookeeper connection string
                new ExponentialBackoffRetry(1000, 3) // Retry policy
        );
        client.start(); // Start the client
        return client;
    }
}
