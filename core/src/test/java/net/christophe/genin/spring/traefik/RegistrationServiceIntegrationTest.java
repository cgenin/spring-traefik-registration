package net.christophe.genin.spring.traefik;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class RegistrationServiceIntegrationTest {

    public static final String TARGET = "target/test-traefik";
    @Autowired
    RegistrationService registrationService;

    @Before
    public void initialize() {
        new File(TARGET).mkdirs();
    }

    @Test
    public void testFile() throws IOException {
        assertThat(registrationService.getConfFile().toString())
                .startsWith(TARGET)
                .endsWith("uno.toml");
        final String all = String.join("", Files.readAllLines(registrationService.getConfFile()));

        assertThat(all).isEqualTo("[http.routers]    [http.routers.Router-uno]        # By default, routers listen to every entry points        rule = \"PathPrefix(`/uno`)\"        priority = 2        service = \"uno\"[http.services]    [http.services.uno.loadBalancer]        [[http.services.uno.loadBalancer.servers]]            url = \"http://127.0.0.1:-1/uno\"");
    }

}
