package net.krlite.splasher;

import com.google.common.collect.Lists;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

import static net.krlite.splasher.SplasherMod.LOGGER;

public class CustomSplashTextLoader {
    private static final List<String> result = Lists.newArrayList();

    private static void loadFile(File file) throws FileNotFoundException {
        if ( !result.isEmpty() ) {
            result.clear();
        }

        Scanner scanner = new Scanner(file);

        while ( scanner.hasNextLine() ) {
            result.add(scanner.nextLine());
        }

        if ( result.isEmpty() ) {
            LOGGER.warn("Loaded empty custom splash.");
        }
    }

    public static List<String> ReadCustomSplashText(Path path, String fileName) {
        File file = path.resolve(fileName).toFile();

        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            loadFile(file);
        } catch (Exception e) {
            LOGGER.trace(String.valueOf(e));
        }
        //LOGGER.warn("Result: " + result.toString());

        return result;
    }
}
