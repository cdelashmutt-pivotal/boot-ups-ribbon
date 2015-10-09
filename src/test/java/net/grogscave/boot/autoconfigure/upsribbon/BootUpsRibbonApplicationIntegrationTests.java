package net.grogscave.boot.autoconfigure.upsribbon;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=BootUpsRibbonApplicationIntegrationTests.TestConfig.class)
@TestPropertySource(properties={
	"vcap.services.testclient.credentials.listOfServers=localhost:8085",
})
public class BootUpsRibbonApplicationIntegrationTests {

	@Autowired
	private ApplicationContext context;
	
	@Test
	public void testLookupServiceFromLocalConfig()
	{
		LoadBalancerClient lbClient = context.getBean(LoadBalancerClient.class);
		assertThat(lbClient, notNullValue());
		
		ServiceInstance si = lbClient.choose("testclient");
		assertThat(si, notNullValue());
		assertThat(si.getHost(), equalTo("localhost"));
		assertThat(si.getPort(), equalTo(8085));
	}

	@Test
	public void testLookupNonExistentService()
	{
		LoadBalancerClient lbClient = context.getBean(LoadBalancerClient.class);
		assertThat(lbClient, notNullValue());
		
		ServiceInstance si = lbClient.choose("foobarclient");
		assertThat(si, nullValue());
	}

	@Configuration
	@EnableAutoConfiguration
	static class TestConfig {
	}
}
