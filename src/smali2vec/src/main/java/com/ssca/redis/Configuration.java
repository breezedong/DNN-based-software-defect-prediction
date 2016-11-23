package com.ssca.redis;
 
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
public class Configuration extends Properties {
 
	private static Logger logger = LogManager.getLogger(Configuration.class);
    private static final long serialVersionUID = 50440463580273222L;
 
    private static Configuration instance = null;
 
    public static synchronized Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }
 
    public String getProperty(String key, String defaultValue) {
        String val = getProperty(key);
        return (val == null || val.isEmpty()) ? defaultValue : val;
    }
 
    public String getString(String name, String defaultValue) {
        return this.getProperty(name, defaultValue);
    }
 
    public int getInt(String name, int defaultValue) {
        String val = this.getProperty(name);
        return (val == null || val.isEmpty()) ? defaultValue : Integer.parseInt(val);
    }
 
    public long getLong(String name, long defaultValue) {
        String val = this.getProperty(name);
        return (val == null || val.isEmpty()) ? defaultValue : Integer.parseInt(val);
    }
 
    public float getFloat(String name, float defaultValue) {
        String val = this.getProperty(name);
        return (val == null || val.isEmpty()) ? defaultValue : Float.parseFloat(val);
    }
 
    public double getDouble(String name, double defaultValue) {
        String val = this.getProperty(name);
        return (val == null || val.isEmpty()) ? defaultValue : Double.parseDouble(val);
    }
 
    public byte getByte(String name, byte defaultValue) {
        String val = this.getProperty(name);
        return (val == null || val.isEmpty()) ? defaultValue : Byte.parseByte(val);
    }
 
    public Configuration() {
        InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("config.properties");
        try {
            this.load(in);
            in.close();
        } catch (IOException e) {
        }
    }
}