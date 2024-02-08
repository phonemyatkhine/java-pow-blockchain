package pow_blockchain.hooks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import pow_blockchain.components.ParticipantsCleanUp;

@Component
public class ShutdownHook {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ParticipantsCleanUp cleanupComponent;

    public void registerShutdownHook(int port) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Execute cleanup logic when the application shuts down
            cleanupComponent.performCleanup(port);
        }));
    }
}
