package flashcards;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    static AppState state = AppState.MENU;

    static InOutHandler console = new InOutHandler();
    static CardsDeck cards = new CardsDeck();
    static Stats stats = new Stats();
    static File exportBeforeExit = null;

    public static void main(String[] args) {
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "-import":
                        console.readDataFile(new File(args[i + 1]), cards, stats);
                        break;
                    case "-export":
                        exportBeforeExit = new File(args[i + 1]);
                        break;
                }
            }
        }

        while (state != AppState.EXIT) {
            actionsHandler();
        }
    }

    public static void actionsHandler() {
        switch (state) {
            case MENU:
                handleMenu();
                break;
            case IMPORT:
                handleImport();
                break;
            case EXPORT:
                handleExport();
                break;
            case ADD:
                handleAddCard();
                break;
            case REMOVE:
                handleRemoveCard();
                break;
            case ASK:
                handleAsk();
                break;
            case LOG:
                exportLog();
                break;
            default:
                console.log("Action invalid!");
        }
    }

    public static void handleMenu() {
        console.log("\nInput the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
        switch (console.readLine()) {
            case "add":
                state = AppState.ADD;
                break;
            case "ask":
                state = AppState.ASK;
                break;
            case "remove":
                state = AppState.REMOVE;
                break;
            case "import":
                state = AppState.IMPORT;
                break;
            case "export":
                state = AppState.EXPORT;
                break;
            case "exit":
                state = AppState.EXIT;
                console.log("Bye bye!");
                if (exportBeforeExit != null) {
                    console.writeCardsData(exportBeforeExit, cards, stats);
                }
                console.close();
                break;
            case "log":
                state = AppState.LOG;
                break;
            case "reset stats":
                resetCardsStats();
                break;
            case "hardest card":
                printCardsStats();
                break;
            default:
                console.log("Action invalid!");
                break;
        }
    }

    public static void handleAddCard() {
        console.log("The card :");
        String card = console.readLine();
        if (cards.isNameExist(card)) {
            console.log("The card \"" + card + "\" already exists.");
            state = AppState.MENU;
            return;
        }

        console.log("The definition of the card :");
        String definition = console.readLine();
        if (cards.isDefinitionExist(definition)) {
            console.log("The definition \"" + definition + "\" already exists.");
        } else {
            cards.addCardToDeck(card, definition);
            console.log("The pair (\"" + card + "\":\"" + definition + "\") has been added.");
        }
        state = AppState.MENU;
    }

    public static void handleRemoveCard() {
        console.log("The card :");
        String name = console.readLine();
        if (cards.isNameExist(name)) {
            cards.removeCard(name);
            console.log("The card has been removed.");
            stats.remove(name);
        } else {
            console.log("Can't remove \"" + name + "\": there is no such card.");
        }
        state = AppState.MENU;
    }

    public static void handleAsk() {
        console.log("How many times to ask?");
        int times = console.readInt();
        String[] cardsList = cards.getArray();
        Random random = new Random();

        while (times-- > 0) {
            int index = random.nextInt(cardsList.length);
            String card = cardsList[index];
            String rightAnswer = cards.getDefinition(card);

            console.log("Print the definition of \""+ card +"\":");
            String answer = console.readLine();

            if (rightAnswer.equals(answer)) {
                console.log( "Correct answer.");
            } else {
                String rightKey = cards.findNameFromDefinition(answer);
                if (rightKey == null) {
                    console.log( "Wrong answer. The correct one is \""+ rightAnswer +"\".");
                } else {
                    console.log(
                            "Wrong answer. The correct one is \"" +
                                    rightAnswer +
                                    "\", you've just written the definition of \"" +
                                    rightKey +
                                    "\".");
                }
                addCardsStats(card);
            }
        }
        state = AppState.MENU;
    }

    public static void handleImport() {
        console.log("File name:");
        File file = new File(console.readLine());
        console.readDataFile(file, cards, stats);
        state = AppState.MENU;
    }

    public static void handleExport() {
        console.log("File name:");
        File file = new File(console.readLine());
        console.writeCardsData(file, cards, stats);
        state = AppState.MENU;
    }

    public static void exportLog() {
        console.log("File name:");
        File file = new File(console.readLine());
        console.writeLog(file);
        state = AppState.MENU;
    }

    public static void addCardsStats(String card) {
        stats.addOrAppend(card);
    }

    public static void printCardsStats() {
        ArrayList<String> errorsList = stats.getArray();
        if (errorsList.size() == 0) {
           console.log("There are no cards with errors.");
           return;
        }

        ArrayList<String> hardestCards = new ArrayList<>();
        int times = 0;

        for (String error : errorsList) {
            int currentTimes = stats.get(error);
            if (currentTimes > times) {
                times = currentTimes;
                hardestCards.clear();
                hardestCards.add(error);
            } else if (currentTimes == times) {
                hardestCards.add(error);
            }
        }

        StringBuilder statsLog = new StringBuilder();
        statsLog.append("The hardest card ")
                .append(hardestCards.size() == 1 ? "is" : "are");
        for (String card : hardestCards) {
            statsLog.append(" \""+ card +"\",");
        }
        statsLog.deleteCharAt(statsLog.length() - 1)
                .append(". You have ")
                .append(times)
                .append(" errors answering it.");
        console.log(statsLog.toString());
    }

    public static void resetCardsStats() {
        stats.clear();
        console.log("Card statistics has been reset");
    }
}