package me.zookeeper.leader_election;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LeaderElectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeaderElectionApplication.class, args);
	}

}
