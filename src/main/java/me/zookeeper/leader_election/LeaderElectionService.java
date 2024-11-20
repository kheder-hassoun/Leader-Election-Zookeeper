package me.zookeeper.leader_election;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.springframework.stereotype.Service;

@Service
public class LeaderElectionService  extends LeaderSelectorListenerAdapter {

    private boolean isLeader = false;

    public LeaderElectionService (CuratorFramework client) {

        LeaderSelector leaderSelector = new LeaderSelector(client, "/leaderElection", this);
        leaderSelector.autoRequeue(); // Automatically requeue after losing leadership
        leaderSelector.start(); // Start the leader election process
    }

    @Override
    public void takeLeadership(CuratorFramework client) throws Exception {
        isLeader = true;
        System.out.println("This instance is now the leader!");
        try {
            Thread.sleep(Long.MAX_VALUE); // Hold leadership until interrupted
        } finally {
            isLeader = false;
            System.out.println("Leadership relinquished.");
        }
    }

    public boolean isLeader() {
        return isLeader;
    }
}
