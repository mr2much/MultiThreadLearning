package com.lockward.anubis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Main {

    private static String[] inputFiles = {"numbers.txt", "numbers1.txt", "numbers2.txt", "numbers3.txt",
        "numbers4.txt", "numbers5.txt", "numbers6.txt"};

    private static String[] outputFiles = {"numbers.out.txt", "numbers1.out.txt", "nummbers2.out.txt",
            "numbers3.out.txt", "numbers4.out.txt", "numbers5.out.txt", "numbers6.out.txt"};

    public static void main(String[] args) {
//        try {
//            generateValuesAndWriteToFiles();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        System.out.println("Reading and adding numbers");

        System.out.println();
        System.out.println("Without Multi-Threading");
        long startTime = System.nanoTime();

        for (int i = 0; i < inputFiles.length; i++) {
            Adder adder = new Adder(inputFiles[i], outputFiles[i]);
            try {
                adder.doAdd();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        long total = 0;
//        try (BufferedReader reader = Files.newBufferedReader(Paths.get("numbers.txt"))) {
//            String line;
//
//            while(null != (line = reader.readLine())) {
//                total += Long.parseLong(line);
//            }
//        } catch (IOException e) {
//            System.out.println(e.getClass().getSimpleName() + " - " + e.getMessage());
//        }
//
//        System.out.println("Total sum is: " + total);

        long stopTime = System.nanoTime();
        long elapsedTime = stopTime - startTime;

        System.out.println("Elapsed time: " + TimeUnit.NANOSECONDS.toSeconds(elapsedTime) + " seconds.");

        System.out.println();
        System.out.println("With Multi-Threading...");
        System.out.println("Using ExecutorService and Runnable");
        startTime = System.nanoTime();

        ExecutorService es = Executors.newFixedThreadPool(3);

        for (int i = 0; i < inputFiles.length; i++) {
            AdderThread adder = new AdderThread(inputFiles[i], outputFiles[i]);
            es.submit(adder);
        }

        try {
            es.shutdown();
            es.awaitTermination(60, TimeUnit.SECONDS);

            if (es.isTerminated()) {
                System.out.println("ExecutorService shutdown correctly.");
            }
        } catch (InterruptedException e) {
            System.out.println(e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        stopTime = System.nanoTime();
        elapsedTime = stopTime - startTime;
        System.out.println("Elapsed time: " + TimeUnit.NANOSECONDS.toSeconds(elapsedTime) + " seconds.");

        System.out.println("Using Future and Callable");

        startTime = System.nanoTime();

        es = Executors.newFixedThreadPool(3);
        Future<Long>[] results = new Future[inputFiles.length];

        for (int i = 0; i < inputFiles.length; i++) {
            AdderCallable adderCallable = new AdderCallable(inputFiles[i]);
            results[i] = es.submit(adderCallable);
        }

        for (Future<Long> result : results) {
            try {
                NumberFormat format = new DecimalFormat();
                long value = result.get();
                System.out.println("Total: " + format.format(value));
            } catch (ExecutionException e) {
                Throwable adderEx = e.getCause();

                System.out.println(adderEx.getClass().getSimpleName() + " - " + adderEx.getMessage());
            } catch (Exception e) {
                Throwable ex = e.getCause();
                System.out.println(ex.getClass().getSimpleName() + " - " + ex.getMessage());
            }

        }

        /*
            if you don't shutdown the executorservice the program won't stop
           running, even if it has executed all lines.
        */
        try {
            es.shutdown();
            es.awaitTermination(60, TimeUnit.SECONDS);

            if (es.isTerminated()) {
                System.out.println("ExecutorService shutdown correctly.");
            }
        } catch (InterruptedException e) {
            System.out.println("Error shutting down ExecutorService: " + e.getMessage());
        }
        stopTime = System.nanoTime();

        elapsedTime = stopTime - startTime;

        System.out.println("Elapsed time: " + TimeUnit.NANOSECONDS.toSeconds(elapsedTime));
    }

    public static void generateValuesAndWriteToFiles() throws IOException {
        Random rand = new Random();

        for (int i = 0; i < inputFiles.length; i++) {
            try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(inputFiles[i]), StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE)) {
                int numbers = rand.nextInt(10000000) + 1;
                System.out.println("Generating " + numbers + " random numbers.");
                for(int j = 0; j < numbers; j++) {
                    int n = rand.nextInt(numbers);
                    bw.write(Integer.toString(n));
                    bw.newLine();
                }
            }
        }
    }
}
