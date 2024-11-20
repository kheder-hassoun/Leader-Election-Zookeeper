package me.zookeeper.leader_election;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {
    private final LeaderElectionService leaderElectionService;

    public StatusController(LeaderElectionService leaderElectionService) {
        this.leaderElectionService = leaderElectionService;
    }

    @GetMapping("/status")
    public String getStatus() {
        if (leaderElectionService.isLeader()) {
            return "I am the leader!";
        } else {
            return "I am a worker.";
        }
    }
}
