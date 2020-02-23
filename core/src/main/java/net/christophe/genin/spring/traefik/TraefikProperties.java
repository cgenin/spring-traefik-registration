package net.christophe.genin.spring.traefik;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "traefik")
public class TraefikProperties {

    private Boolean disabled = false;

    @NotBlank
    private String configurationDirectory;

    private Integer priority = 2;

    private String hostname = "127.0.0.1";

    private boolean https = false;

    private String forceContext;

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getConfigurationDirectory() {
        return configurationDirectory;
    }

    public void setConfigurationDirectory(String configurationDirectory) {
        this.configurationDirectory = configurationDirectory;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public boolean isHttps() {
        return https;
    }

    public void setHttps(boolean https) {
        this.https = https;
    }

    public String getForceContext() {
        return forceContext;
    }

    public void setForceContext(String forceContext) {
        this.forceContext = forceContext;
    }

}
