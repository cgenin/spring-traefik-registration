package net.christophe.genin.spring.traefik;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class RegistrationService {

    private static final Logger LOG = LoggerFactory.getLogger(RegistrationService.class);

    private final Environment environment;
    private final TraefikProperties properties;
    private AtomicBoolean registered = new AtomicBoolean(false);
    private Path confFile;

    @Autowired
    public RegistrationService(Environment environment, TraefikProperties properties) {
        this.environment = environment;
        this.properties = properties;
    }

    @PostConstruct
    public void onStart() throws IOException {
        final Boolean disabled = properties.getDisabled();
        final boolean registered = this.registered.get();
        if (!registered && Boolean.FALSE.equals(disabled)) {
            final String currentPort = this.environment.getProperty("server.port");
            final String context = context();

            String name = context.substring(1);
            String scheme = scheme();
            String url = scheme + properties.getHostname() + ":" + currentPort + context;

            String content = "[http.routers]\n" +
                    "    [http.routers.Router-" + name + "]\n" +
                    "        # By default, routers listen to every entry points\n" +
                    "        rule = \"PathPrefix(`" + context + "`)\"\n" +
                    "        priority = " + properties.getPriority() + "\n" +
                    "        service = \"" + name + "\"\n" +
                    "[http.services]\n" +
                    "    [http.services." + name + ".loadBalancer]\n" +
                    "        [[http.services." + name + ".loadBalancer.servers]]\n" +
                    "            url = \"" + url + "\"\n";


            final String configurationDirectory = properties.getConfigurationDirectory();
            final String p = (configurationDirectory.endsWith("/")) ? configurationDirectory : configurationDirectory + File.separator;
            final String fileName = p + name + ".toml";
            confFile = new File(fileName).toPath();
            LOG.info("Writing " + confFile + " for traefik reverse proxy.");
            Files.write(confFile, content.getBytes());
            this.registered.set(true);
            LOG.info("Registered.");


        } else {
            LOG.info("Traefik disabled : " + disabled + " or registered : " + registered);
        }
    }

    private String scheme() {
        return (properties.isHttps()) ? "https://" : "http://";
    }

    private String context() {
        return Optional.ofNullable(properties.getForceContext())
                .map(Optional::of)
                .orElseGet(() -> Optional.ofNullable(this.environment.getProperty("server.servlet.context-path")))
                .orElseGet(() -> {
                    LOG.warn("Property 'server.servlet.context-path' not found. Use '/' instead");
                    return "/";
                });
    }

    @PreDestroy
    public void onStop() {
        final boolean disabled = Boolean.FALSE.equals(properties.getDisabled());
        if (!disabled ||  registered.get()) {
            try {
                Files.delete(confFile);
            } catch (IOException e) {
                LOG.warn("Impossible de détruire le fichier de traefik", e);
            }
        }
    }

    public Path getConfFile() {
        return confFile;
    }
}
