package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

    @Test
    public void contextLoads() {
        // Test to ensure the application context loads successfully
        try {
            SpringApplication.run(VulnadoApplication.class);
        } catch (Exception e) {
            throw new AssertionError("Application context failed to load", e);
        }
    }

    @Test
    public void main_ShouldInitializeApplication() {
        // Mock Postgres setup to ensure it is called during application startup
        Postgres mockPostgres = Mockito.mock(Postgres.class);
        Mockito.doNothing().when(mockPostgres).setup();

        try {
            VulnadoApplication.main(new String[]{});
        } catch (Exception e) {
            throw new AssertionError("Application failed to initialize", e);
        }

        // Verify that Postgres.setup() was called
        Mockito.verify(mockPostgres, Mockito.times(1)).setup();
    }
}

