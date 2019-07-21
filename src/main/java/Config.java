import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.util.List;

public class Config {
    public String controlHostname;
    public int controlPort;
    public String prometheusHostname;
    public float messagesPerSecond;
    public int experimentDuration;
    public int requestsPerMessage;
    public List<Metric> metrics;

    static Config getInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        return mapper.readValue(new File("config/global.yaml"), Config.class);
    }
}
