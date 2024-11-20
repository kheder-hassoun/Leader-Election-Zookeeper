# Leader Election with ZooKeeper

This project demonstrates a leader election mechanism using ZooKeeper in two different approaches:
1. **Curator Framework**: A high-level library for working with ZooKeeper.
2. **Custom Implementation**: A manually implemented leader election mechanism directly using the ZooKeeper API.

---

``` css
*-- Add README.md (master & origin)
|
|    *-- My leader Election (my_leader_election branch)
|    |
*----- Improvement
|
*----- Dependency Injection
|
*----- First Commit
```

## Project Structure
The project is divided into two branches, each implementing a leader election mechanism:

### 1. **Curator Framework Branch**
This branch (`curator-framework`) uses the [Apache Curator Framework](https://curator.apache.org/) to simplify interaction with ZooKeeper for leader election.

- **LeaderElectionService.java**: Uses `LeaderSelector` from Curator to handle leader election logic.
- **StatusController.java**: A REST controller that exposes the `/status` endpoint to check whether the service is the leader or a worker.
- **How It Works**:
    1. Curator creates an ephemeral sequential node in the `/leader-election` ZNode.
    2. The `LeaderSelector` determines the leader based on the lowest node ID.
    3. Leadership is relinquished when the node disconnects, triggering a re-election.
    4. The `/status` endpoint displays whether the instance is the leader or a worker.

### 2. **Custom Implementation Branch**
This branch (`custom-implementation`) implements leader election directly using the ZooKeeper API.

- **LeaderElectionService.java**:
    - Directly creates ephemeral sequential nodes in ZooKeeper.
    - Watches for changes in the leader node to trigger re-election.
- **StatusController.java**: Similar to the Curator branch but works with the custom leader election logic.
- **How It Works**:
    1. A ZooKeeper client connects to the ZooKeeper server.
    2. Each instance creates an ephemeral sequential node under the `/leader-election` ZNode.
    3. The instance with the smallest node name becomes the leader.
    4. If the leader node is deleted, ZooKeeper triggers a watch, initiating a new leader election.
    5. The `/status` endpoint indicates whether the instance is a leader or a worker.

---

## Running the Project

### Prerequisites
1. ZooKeeper server installed and running.
2. Java 17+ installed.
3. Maven installed for building the project.

### Steps to Run
1. **Clone the repository**:
   ```bash
   git clone "https://github.com/kheder-hassoun/Leader-Election-Zookeeper.git"
   cd Leader-Election-Zookeeper
   ```

2. **Checkout a Branch**:
    - For Curator Framework:
      ```bash
      git checkout master
      ```
    - For Custom Implementation:
      ```bash
      git checkout my_leader_election
      ```

3. **Update `application.properties`**:
   Ensure the following properties are set:
   ```properties
   spring.application.name=service1  # Change for each service
   zookeeper.connection=192.168.10.132:2181  # ZooKeeper connection string
   ```

4. **Build and Run**:
   ```bash
   mvn clean install
   java -jar target/leader-election-<version>.jar
   ```

5. **Run Multiple Instances**:
    - Launch multiple instances of the application.
    - Use different ports for each instance:
      ```bash
      java -Dserver.port=8081 -jar target/leader-election-<version>.jar
      java -Dserver.port=8082 -jar target/leader-election-<version>.jar
      ```

6. **Check the Status**:
    - Access the `/status` endpoint of each instance:
      ```bash
      http://localhost:8080/status
      http://localhost:8081/status
      http://localhost:8082/status
      ```
    - The output will indicate whether the instance is the leader or a worker.

---

## Key Differences Between the Approaches

| Feature                     | Curator Framework                              | Custom Implementation                       |
|-----------------------------|-----------------------------------------------|-------------------------------------------|
| **Ease of Use**              | High-level APIs simplify leader election.    | Requires manual handling of leader logic. |
| **Leader Election Logic**    | Handled by `LeaderSelector`.                 | Implemented with ZooKeeper API.           |
| **Error Handling**           | Built-in resilience and retries.             | Must handle edge cases manually.          |
| **Performance**              | Optimized for distributed applications.      | Slightly lower-level control.             |

---

## Lombok
is a Java library that automatically plugs into the compiler to generate common boilerplate such as getters, setters, equals methods, hashCode methods, and toString methods at compile time.
Generally, Lombok is used in Spring Boot projects to make the code structure very simple and easy to read.
Repetitive code can be avoided since most of the code will be automatically generated using annotations
like @Getter, @Setter, @ToString, and @NoArgsConstructor. In the project,
Lombok is used to reduce the development of model and service classes by generating methods and also by simplifying the injection of dependencies. This allowed us to concentrate on the core business logic without being bogged down with verbose and repetitive code.


by 

kheder hassoun 
---

