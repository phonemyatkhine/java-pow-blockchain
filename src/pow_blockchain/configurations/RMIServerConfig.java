// package pow_blockchain.configurations;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Import;

// @Configuration
// public class RmiServerConfig {
    
//     @Bean
//     public MyService myService() {
//         return new MyServiceImpl();
//     }

//     @Bean
//     public RmiServiceExporter rmiServiceExporter(MyService myService) {
//         RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
//         rmiServiceExporter.setServiceInterface(MyService.class);
//         rmiServiceExporter.setService(myService);
//         rmiServiceExporter.setServiceName("MyService");
//         rmiServiceExporter.setRegistryPort(1099); // RMI registry port
//         return rmiServiceExporter;
//     }
// }
