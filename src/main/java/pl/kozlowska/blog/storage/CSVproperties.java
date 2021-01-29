package pl.kozlowska.blog.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("csvproperties")
public class CSVproperties {

    /**
     * Folder location for storing files
     */
    private String location = "src\\main\\resources\\downloaded";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
