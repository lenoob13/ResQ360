package model.loaders;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for loading and parsing a simplified YAML configuration file.
 * Supports nested sections and environment variable substitution.
 */
public class YamlFileLoader {
    private static final String UNKNOWN_ENV_VARIABLE = "UNKNOWN_ENV_VARIABLE";
    private final Map<String, String> configValues = new HashMap<>();

    /**
     * Loads and parses the YAML file located at the given resource path.
     *
     * @param resourcePath the path to the YAML file inside resources
     * @throws FileNotFoundException if the file cannot be found
     */
    public void loadFrom(String resourcePath) throws FileNotFoundException {
        configValues.clear();
        try (InputStream input = getClass().getResourceAsStream(resourcePath)) {
            if (input == null)
                throw new FileNotFoundException("Fichier de configuration introuvable : " + resourcePath);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
                String line;
                String currentPrefix = "";

                while ((line = reader.readLine()) != null) {
                    line = line.stripTrailing();
                    if (line.isEmpty() || line.startsWith("#")) continue;

                    int indentLevel = getIndentLevel(line);
                    line = line.strip();

                    if (line.endsWith(":")) {
                        // Nouvelle section
                        String section = line.substring(0, line.length() - 1).strip();
                        currentPrefix = buildPrefix(currentPrefix, section, indentLevel);
                    } else {
                        String[] parts = line.split(":", 2);
                        if (parts.length != 2) continue;

                        String key = parts[0].strip();
                        String rawValue = parts[1].strip();
                        String fullKey = currentPrefix.isEmpty() ? key : currentPrefix + "." + key;

                        configValues.put(fullKey, resolveEnvVariable(rawValue));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du chargement du fichier de configuration YAML : " + resourcePath, e);
        }
    }

    private int getIndentLevel(String line) {
        int count = 0;
        for (char c : line.toCharArray()) {
            if (c == ' ') count++;
            else break;
        }
        return count / 2;
    }

    
    private String buildPrefix(String currentPrefix, String section, int indentLevel) {
        String[] parts = currentPrefix.split("\\.");
        StringBuilder newPrefix = new StringBuilder();

        for (int i = 0; i < indentLevel; i++) {
            if (i < parts.length && !parts[i].isEmpty()) {
                newPrefix.append(parts[i]).append(".");
            }
        }

        newPrefix.append(section);
        return newPrefix.toString();
    }

    private String resolveEnvVariable(String value) {
        if (value.startsWith("${") && value.endsWith("}")) {
            String envVar = value.substring(2, value.length() - 1);
            String envValue = System.getenv(envVar);
            return envValue != null ? envValue : UNKNOWN_ENV_VARIABLE;
        }
        return value;
    }

    /**
     * Returns the string value associated with a given key.
     *
     * @param key the key to search
     * @return the value as a String, or null if not found
     */
    public String get(String key) {
        return configValues.get(key);
    }

    /**
     * Returns the value as an integer for the given key.
     *
     * @param key the key to search
     * @return the integer value
     * @throws IllegalArgumentException if the value is not a valid integer
     */
    public int getInt(String key) {
        try {
            return Integer.parseInt(configValues.get(key));
        } catch (Exception e) {
            throw new IllegalArgumentException("Valeur non entière pour la clé : " + key, e);
        }
    }

    /**
     * Returns the value as a boolean for the given key.
     *
     * @param key the key to search
     * @return true if the value is "true" (case-insensitive), false otherwise
     */
    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(configValues.getOrDefault(key, "false"));
    }

    /**
     * Returns an unmodifiable map of all key-value pairs loaded from the YAML.
     *
     * @return an immutable map of config values
     */
    public Map<String, String> getAll() {
        return Collections.unmodifiableMap(configValues);
    }

    public boolean containsKey(String key) {
        return configValues.containsKey(key);
    }
}
