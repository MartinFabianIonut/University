package ppd.client.myclient.util;


import ppd.client.myclient.entity.Participant;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class IOHandler {

    public static List<String> generateFileNames() {
        List<String> files = new LinkedList<>();
        String format = "RezultateC%d_P%d";
        for (int i = 1; i <= Constants.NUMBER_OF_COUNTRIES; ++i) {
            for (int j = 1; j <= 10; ++j) {
                files.add(String.format(format, i, j));
            }
        }
        return files;
    }

    public static void validate(String outputFilename, String validFilename) {
        int line = 1;
        try(BufferedReader br1 = new BufferedReader(new FileReader(outputFilename));
            BufferedReader br2 = new BufferedReader(new FileReader(validFilename))) {
            String line1;
            String line2;
            while((line1 = br1.readLine()) != null && (line2 = br2.readLine()) != null) {
                if(!line1.equals(line2)) {
                    int score1 = Integer.parseInt(line1.split(",")[1]);
                    int score2 = Integer.parseInt(line2.split(",")[1]);
                    if(score1 != score2) {
                      System.err.println("Output is not valid at line " + line + "!");
                    }
                }
                line++;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Participant> readParticipants(List<String> files) {
        List<Participant> participants = new LinkedList<>();
        for (String file : files) {
            String country = file.split("_")[0].substring(9);
            try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/input/" + file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] tokens = line.split(",");
                    participants.add(new Participant(tokens[0], Double.parseDouble(tokens[1]), getCountry(country)));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return participants;
    }

    private static String getCountry(String country) {
        return switch(country) {
            case "C1" -> "France";
            case "C2" -> "Germany";
            case "C3" -> "Italy";
            case "C4" -> "Spain";
            case "C5" -> "Romania";
            default -> throw new RuntimeException("Invalid country!");
        };
    }
}
