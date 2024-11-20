package me.zookeeper.leader_election;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ZookeeperConfig {

    @Bean
    public CuratorFramework curatorFramework(@Value("${zookeeper.connection}") String zookeeperConnection) {
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                zookeeperConnection,
                new ExponentialBackoffRetry(1000, 3) // Retry policy
        );
        client.start();
        return client;
    }
}
