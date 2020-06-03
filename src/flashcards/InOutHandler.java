package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class InOutHandler {
    private final Scanner scanner = new Scanner(System.in);
    private ArrayList<String> logger = new ArrayList<>();

    public String readLine() {
        String command = scanner.nextLine();
        logger.add("> " + command);
        return command;
    }

    public void log(String line) {
        System.out.println(line);
        logger.add(line);
    }

    public void close() {
        scanner.close();
    }

    public int readInt() {
        return Integer.parseInt(readLine());
    }

    public void readDataFile(File file, CardsDeck cards, Stats stats) {
        try (Scanner fileScan = new Scanner(file)) {
            int count = 0;
            while (fileScan.hasNext()) {
                String[] card = fileScan.nextLine().split(":");
                cards.addCardToDeck(card[0], card[1]);
                stats.put(card[0], Integer.parseInt(card[2]));
                count++;
            }
            log( "Import action: " + count + " cards have been loaded.");
        } catch (FileNotFoundException exception) {
            log("File not found.");
        }
    }


    public void writeLog(File file) {
        try (PrintWriter fileWriter = new PrintWriter(file)) {
            for (var log : logger) {
                fileWriter.println(log);
            }
            log("The log has been saved.");
        } catch (Exception exception) {
            log("Write file error" + exception.getMessage());
        }
    }

    public void writeCardsData(File file, CardsDeck cards, Stats stats) {
        int count = 0;
        try (PrintWriter fileWriter = new PrintWriter(file)) {
            for (String card : cards.getArray()) {
                fileWriter.println(
                        card +
                                ":" +
                                cards.getDefinition(card) +
                                ":" +
                                stats.getValueOrZero(card)
                );
                count++;
            }
            log("Export action: " + count + " cards have been saved.");
        } catch (Exception exception) {
            log("Write file error" + exception.getMessage());
        }
    }
}
