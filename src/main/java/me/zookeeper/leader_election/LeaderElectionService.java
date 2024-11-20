package me.zookeeper.leader_election;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LeaderElectionService extends LeaderSelectorListenerAdapter {

    private boolean isLeader = false;

    public LeaderElectionService(CuratorFramework client, @Value("${spring.application.name}") String serviceName) {
        String path = "/leader-election"; // Zookeeper path for leader election
        LeaderSelector leaderSelector = new LeaderSelector(client, path, this);
        leaderSelector.autoRequeue();
        leaderSelector.start();

        System.out.println(serviceName + " has joined the leader election at path: " + path);
    }

    @Override
    public void takeLeadership(CuratorFramework client) throws Exception {
        isLeader = true;
        System.out.println("This instance is now the leader!");
        try {
           // Thread.sleep(1000);
            Thread.sleep(Long.MAX_VALUE); // Simulate holding leadership
        } finally {
            isLeader = false;
            System.out.println("Leadership relinquished.");
        }
    }

    public boolean isLeader() {
        return isLeader;
    }
}
