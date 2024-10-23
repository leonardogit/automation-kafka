package constants;

import utils.DefaultProperties;

public class Constants extends DefaultProperties {
    public String topicname = loadProperties().getProperty("topicname");
    public String message = loadProperties().getProperty("message");
}
