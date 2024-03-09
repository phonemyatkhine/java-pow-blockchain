package pow_blockchain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import pow_blockchain.hooks.ShutdownHook;
import pow_blockchain.services.RMIBasedP2PService;
import pow_blockchain.common.BlockData;
import pow_blockchain.services.StringUtil;
import pow_blockchain.services.Block;

import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/")

public class Main {
    
    private static RMIBasedP2PService P2PService;
    
    @GetMapping("/participants")
    public String checkParticipants() {
        return "Participants: " + P2PService.getParticipants();
    }

    @PostMapping("/block")
    public String addBlock(@RequestBody String blockData) {
        System.out.println("Data: " + blockData);
        P2PService.mineBlock(blockData);
        return "Block added successfully.";
    }

    @GetMapping("/blockchain")
    public List<Block> getBlockchain() {
        return P2PService.getBlockchain();
    }
    
    @GetMapping("/topic/{topicName}/consumption")
    public List<BlockData> getTopicConsumption(@PathVariable(name = "topicName") String topicName) {
        return P2PService.getTopicConsumption(topicName);
    }
    

    @GetMapping("/topic/{topicName}/daily-consumption/{date}")
    public List<BlockData> getTopicDailyConsumption(@PathVariable(name = "topicName") String topicName, @PathVariable(name = "date") String date) {
        System.out.println("Daily Consumption Date: " + date);
        return P2PService.getTopicDailyConsumption(topicName, date);
    }

    @GetMapping("/topic/{topicName}/monthly-consumption/{date}")
    public List<BlockData> getTopicMonthlyConsumption(@PathVariable(name = "topicName") String topicName, @PathVariable(name = "date") String date) {
        System.out.println("Monthly Consumption Date: " + date);
        return P2PService.getTopicMonthlyConsumption(topicName, date);
    }
    
    @GetMapping("/device/{deviceId}/consumption")
    public List<BlockData> getDeviceConsumption(@PathVariable(name = "deviceId") String deviceId) {
        return P2PService.getDeviceConsumption(deviceId);
    }

    @GetMapping("/device/{deviceId}/daily-consumption/{date}")
    public List<BlockData> getDeviceDailyConsumption(@PathVariable(name = "deviceId") String deviceId, @PathVariable(name = "date") String date) {
        System.out.println("Daily Consumption Date: " + date + " DeviceId: " + deviceId);
        return P2PService.getDeviceDailyConsumption(deviceId, date);
    }

    @GetMapping("/device/{deviceId}/monthly-consumption/{date}")
    public List<BlockData> getDeviceMonthlyConsumption(@PathVariable(name = "deviceId") String deviceId, @PathVariable(name = "date") String date) {
        System.out.println("Monthly Consumption Date: " + date + " DeviceId: " + deviceId);
        return P2PService.getDeviceMonthlyConsumption(deviceId, date);
    }
    

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Port number is missing!");
            System.err.println("Usage : mvn spring-boot:run -D spring-boot.run.arguments=\"<rmiPort> <port>\"");
            // System.exit(1);
            //mvn spring-boot:run -D spring-boot.run.arguments="10051 8080"
        }
        int rmiPort = args[0].equals("null") ? 10051 : Integer.valueOf(args[0]);
        int webPort = args[1].equals("null") ? 8080 : Integer.valueOf(args[1]);
        System.out.println("RMI_PORT: " + rmiPort);
        System.out.println("PORT: " + webPort);
    
        System.getProperties().put( "server.port", webPort );
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
        ShutdownHook shutdownHook = context.getBean(ShutdownHook.class);
        shutdownHook.registerShutdownHook(rmiPort);
    
        P2PService = new RMIBasedP2PService(rmiPort);
    }
   
}
