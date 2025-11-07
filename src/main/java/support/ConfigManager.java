package support;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigManager {
    private static final Properties PROPS = new Properties();
    private static final String ENV = System.getProperty("env", "local");

    static {
        String path = "environments/" + ENV + ".properties";
        try (InputStream is = ConfigManager.class.getClassLoader().getResourceAsStream(path)) {
            if (is == null) throw new IllegalStateException("Missing config: " + path);
            PROPS.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + path, e);
        }
    }

    private ConfigManager() {}

    public static String baseUrl()       { return get("base.url"); }
    public static String username()      { return get("auth.username"); }
    public static String password()      { return get("auth.password"); }
    public static int timeoutMs()        { return Integer.parseInt(PROPS.getProperty("timeout.ms", "15000")); }

    private static String get(String key) {
        String v = PROPS.getProperty(key);
        if (v == null) throw new IllegalStateException("Missing property: " + key);
        return v;
    }
}
