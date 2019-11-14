package ustccq.console.master;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;

public class DubboGenericCall {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>();

		ApplicationConfig application = new ApplicationConfig();
        application.setName("Meowlomo-test-consumer");
        RegistryConfig registry = new RegistryConfig();
        String zkAddress = "zookeeper://10.0.100.204:2181";
    	registry.setAddress(zkAddress);

        String interfaceClass = "org.apache.dubbo.demo.api.DemoService";
		reference.setApplication(application);
		reference.setRegistry(registry);
        reference.setInterface(interfaceClass);
        reference.setGeneric(true);

        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
        GenericService genericService = cache.get(reference); 

        String[] invokeParamTyeps = new String[1];
        Object[] invokeParams = new Object[1];
        invokeParamTyeps[0] = "java.lang.String";
        invokeParams[0] = "Generic Call From VMC";

        Object genericResult= genericService.$invoke("sayHello", invokeParamTyeps, invokeParams);
        System.err.println(genericResult);
	}
}
