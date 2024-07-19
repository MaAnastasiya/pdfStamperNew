package org.example;

public class Main {
    public static void main(String[] args) {
        if (args.length<1){
            System.err.println("You must specify the path to the configuration file");
            System.exit(1);
        }

        String configFilePath = args[0];
        PropertiesPDF propertiesPDF = new PropertiesPDF(configFilePath);
        PdfStamp pdfStamp = new PdfStamp();
        pdfStamp.showFiles(propertiesPDF);
    }
}