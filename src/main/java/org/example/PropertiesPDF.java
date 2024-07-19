package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesPDF {
    private String sourceFolder = "";
    private String destinationFolder = "";
    private String stampText = "";
    private float xOffset = 0.0f;
    private float yOffset = 0.0f;
    private float stampWidth = 0.0f;
    private float stampHeight = 0.0f;
    private String imagePath = "";

    public PropertiesPDF(String configFilePath){

        Properties properties = new Properties();
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(configFilePath), "UTF-8")) {
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        sourceFolder = properties.getProperty("source.folder");
        destinationFolder = properties.getProperty("destination.folder");
        stampText = properties.getProperty("stamp.text");
        xOffset = Float.parseFloat(properties.getProperty("stamp.x.offset"));
        yOffset = Float.parseFloat(properties.getProperty("stamp.y.offset"));
        stampWidth = Float.parseFloat(properties.getProperty("stamp.width"));
        stampHeight = Float.parseFloat(properties.getProperty("stamp.height"));
        imagePath = properties.getProperty("stamp.image.path");
    }

    public String getSourceFolder(){
        return this.sourceFolder;
    }


    public String getDestinationFolder(){
        return this.destinationFolder;
    }

    public String getStampText(){
        return this.stampText;
    }

    public float getXOffset(){
        return this.xOffset;
    }

    public float getYOffset(){
        return this.yOffset;
    }

    public float getStampWidth(){
        return this.stampWidth;
    }

    public float getStampHeight(){
        return this.stampHeight;
    }

    public String getImagePath(){
        return this.imagePath;
    }
}
