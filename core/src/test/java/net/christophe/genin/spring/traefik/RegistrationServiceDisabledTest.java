package net.christophe.genin.spring.traefik;

import org.junit.Before;
import org.junit.Ignore;
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
@ActiveProfiles("disabled")
@SpringBootTest(classes = Application.class)
public class RegistrationServiceDisabledTest {

    public static final String TARGET = "target/test-traefik";
    @Autowired
    RegistrationService registrationService;

    @Before
    public void initialize() {
        new File(TARGET).mkdirs();
    }

    @Test
    public void testFile() {

        assertThat(registrationService.getConfFile()).isNull();
    }

}
