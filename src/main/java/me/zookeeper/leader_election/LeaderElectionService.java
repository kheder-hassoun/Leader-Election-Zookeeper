package me.zookeeper.leader_election;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
@Component
public class LeaderElectionService implements Watcher {
    private final ZooKeeper zk;
    private final String serviceName;
    private String currentNodePath;
    private String leaderNodePath;
    private boolean isLeader = false;
    @Value("${zookeeper.connection}")
    private String zkConnectionString;

    private static final String LEADER_ELECTION_PATH = "/leader-election";

    public LeaderElectionService(@Value("${spring.application.name}") String serviceName) throws IOException, KeeperException, InterruptedException {
        this.serviceName = serviceName;
        this.zk = new ZooKeeper("192.168.10.133:2181", 10000, this);

        // Ensure the leader-election path exists
        if (zk.exists(LEADER_ELECTION_PATH, false) == null) {
            zk.create(LEADER_ELECTION_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }

        startLeaderElection();
    }

    private void startLeaderElection() throws KeeperException, InterruptedException {
        // Create an ephemeral sequential node
        currentNodePath = zk.create(LEADER_ELECTION_PATH + "/node_", new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(serviceName + " has joined the leader election, node path: " + currentNodePath);

        // Check if this node is the leader
        checkLeader();
    }

    /// **************  this to prevent herd effect  *********************
    private void checkLeader() throws KeeperException, InterruptedException {
        List<String> children = zk.getChildren(LEADER_ELECTION_PATH, false);
        children.sort(String::compareTo);

        int index = children.indexOf(currentNodePath.substring(LEADER_ELECTION_PATH.length() + 1));
        if (index == 0) {
            // This node is the leader
            leaderNodePath = currentNodePath;
            becomeLeader();
        } else {
            // Watch the immediate predecessor
            String predecessorNodePath = LEADER_ELECTION_PATH + "/" + children.get(index - 1);
            System.out.println(serviceName + " is not the leader. Watching node: " + predecessorNodePath);
            zk.exists(predecessorNodePath, true);  // Watch the immediate predecessor
        }
    }


//    private void checkLeader() throws KeeperException, InterruptedException {
//        List<String> children = zk.getChildren(LEADER_ELECTION_PATH, false);
//        children.sort(String::compareTo);
//
//
//        leaderNodePath = LEADER_ELECTION_PATH + "/" + children.get(0);
//
//        if (currentNodePath.equals(leaderNodePath)) {
//            becomeLeader();
//        } else {
//            System.out.println(serviceName + " is not the leader. Watching node: " + leaderNodePath);
//            zk.exists(leaderNodePath, true);  // Watch leader node for deletion
//        }
//    }

    private void becomeLeader() {
        isLeader = true;
        System.out.println(serviceName + " is now the leader!");

        // Start a separate thread to hold leadership
        new Thread(() -> {
            try {
                while (isLeader) {
                    Thread.sleep(1000);  // Simulate doing leader work
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            } finally {
                relinquishLeadership();
            }
        }).start();
    }

    private void relinquishLeadership() {
        isLeader = false;
        System.out.println(serviceName + " is relinquishing leadership.");
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getType() == Event.EventType.NodeDeleted && event.getPath().equals(leaderNodePath)) {
            try {
                checkLeader();  // Re-check leader election if the leader node is deleted
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isLeader() {
        return isLeader;
    }

    public String getLeaderNodePath() {
        return leaderNodePath;
    }

    public String getCurrentNodePath() {
        return currentNodePath;
    }

    public void close() throws InterruptedException {
        zk.close();
    }
}
