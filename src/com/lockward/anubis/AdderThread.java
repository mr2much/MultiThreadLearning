package com.lockward.anubis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;

public class AdderThread implements Runnable {

    String inputFile, outputFile;

    public AdderThread(String inputFile, String outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    public void doAdd() throws IOException {
        NumberFormat format = new DecimalFormat();
        int total = 0;
        String line;

        System.out.println("Reading file: " + inputFile);
        try (BufferedReader br = Files.newBufferedReader(Paths.get(inputFile), StandardCharsets.UTF_8)) {
            while (null != (line = br.readLine())) {
                total += Integer.parseInt(line);
            }
        }

        System.out.println("Writing to: " + outputFile);
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(outputFile), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE)) {
            bw.write("Total: " + format.format(total));
            bw.newLine();
        }
    }

    @Override
    public void run() {
        try {
            doAdd();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
