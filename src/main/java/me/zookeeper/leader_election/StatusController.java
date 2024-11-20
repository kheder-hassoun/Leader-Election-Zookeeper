package me.zookeeper.leader_election;
import org.apache.zookeeper.KeeperException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {
    private final LeaderElectionService leaderElectionService;

    public StatusController(LeaderElectionService leaderElectionService) {
        this.leaderElectionService = leaderElectionService;
    }

    @GetMapping("/status")
    public String getStatus() throws KeeperException, InterruptedException {
        // Retrieve the leader node path from LeaderElectionService
        String leaderNodePath = leaderElectionService.getLeaderNodePath();

        if (leaderNodePath != null && leaderNodePath.equals(leaderElectionService.getCurrentNodePath())) {
            return "I am the leader!";
        } else {
            return "I am a worker.";
        }
    }
}
