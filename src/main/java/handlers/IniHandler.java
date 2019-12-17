package handlers;

import org.ini4j.Ini;
import org.ini4j.Wini;

import java.io.*;

/**
 * A class which handles functions related to reading/writing from/to .ini files.
 * @author Hidayat Rzayev
 */
public class IniHandler {

    // path to the .ini file
    private String iniFile;

    public IniHandler(String iniFile) {
        this.iniFile = iniFile;
    }

    /**
     * Creates/updates the given section in this .ini file, depending on whether the section already exists or not,
     * and adds/updates the specified key/value pair in it.
     * If the given .ini file does not yet exist, it will first be created.
     *
     * @param sectionName - section to be created/updated
     * @param key - key to be added/updated in the section
     * @param value - value to be added/updated in the section
     *
     * @throws IOException if the file could not be created or saved
     */
    public void updateSection(String sectionName, String key, Object value) throws IOException {
        File file = new File(this.iniFile);
        file.createNewFile();

        Ini ini = new Ini(file);
        ini.put(sectionName, key, value);
        ini.store();
    }

    /**
     * Gets the value of the specified key from the specified section.
     *
     * @param sectionName - section to be fetched
     * @param key - key to be fetched
     * @param expectedType - expected type of the retrieved value
     * @param <T> - type of the value which is returned
     * @return the retrieved value
     *
     * @throws IOException if the file could not be loaded
     */
    public <T> T readSection(String sectionName, String key, Class<T> expectedType) throws IOException {
        Wini ini = new Wini(new File(this.iniFile));
        return ini.get(sectionName, key, expectedType);
    }
}
