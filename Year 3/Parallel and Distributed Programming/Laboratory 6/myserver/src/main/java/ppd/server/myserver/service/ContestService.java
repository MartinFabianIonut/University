package ppd.server.myserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ppd.server.myserver.datastructure.MyBlackList;
import ppd.server.myserver.datastructure.MyBlockingLinkedList;
import ppd.server.myserver.datastructure.MyBlockingQueue;
import ppd.server.myserver.entity.Participant;
import ppd.server.myserver.util.Constants;
import ppd.server.myserver.util.Consumer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ContestService {

    private final Logger logger = LoggerFactory.getLogger(ContestService.class);

    private final MyBlockingLinkedList resultList = new MyBlockingLinkedList();

    private final MyBlackList blackList = new MyBlackList();

    private final MyBlockingQueue myBlockingQueue;

    private final Map<String, ReentrantLock> locks = new HashMap<>();

    private final ExecutorService executorService = Executors.newFixedThreadPool(Constants.P_R);

    private final AtomicInteger totalProducerTasks = new AtomicInteger();

    private final Map<String, Double> countryToRanking = new HashMap<>();

    private long lastRankingUpdateTime = System.currentTimeMillis();

    private final long rankingUpdateInterval = TimeUnit.MILLISECONDS.toMillis(Constants.DELTA_T);


    public ContestService() {
        this.myBlockingQueue = new MyBlockingQueue(totalProducerTasks);
        for(int i = 0; i < 1000; ++i) {
            locks.put(String.valueOf(i), new ReentrantLock());
        }
    }

    public void setTotalProducerTasks(int totalProducerTasks) {
        this.totalProducerTasks.set(totalProducerTasks);
        startConsuming();
    }

    private void startConsuming() {
        List<Thread> consumerThreads = new ArrayList<>();

        for (int i = 0; i < Constants.P_W; ++i) {
            consumerThreads.add(new Consumer(totalProducerTasks, myBlockingQueue, blackList, resultList, locks));
        }
        consumerThreads.forEach(Thread::start);
        consumerThreads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        writeFinalRankingFiles();
    }

    public void insertParticipants(List<Participant> participantList) {
        logger.info("Received participants from " + participantList.get(0).getId() + " to " +
            participantList.get(participantList.size() - 1).getId());
        executorService.execute(() -> {
            for(Participant participant : participantList) {
                try {
                    myBlockingQueue.put(participant);
                } catch(InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            if(totalProducerTasks.decrementAndGet() == 0) {
                myBlockingQueue.finish();
            }
        });
    }

    public Map<String, Double> calculateRanking() {
        long currentTime = System.currentTimeMillis();
        logger.info("Current time: " + currentTime + " thread: " + Thread.currentThread().getName());
        logger.info("Country to ranking size: " + countryToRanking.size() + " thread: " + Thread.currentThread().getName());
        if (currentTime - lastRankingUpdateTime < rankingUpdateInterval && !countryToRanking.isEmpty()) {
            return countryToRanking;
        }

        Map<String, Double> countryToScore = new HashMap<>();
        for (Participant participant : resultList.toList()) {
            countryToScore.putIfAbsent(participant.getCountry(), 0.0);
            countryToScore.put(participant.getCountry(), (countryToScore.get(participant.getCountry()) + participant.getScore()));
        }

        List<Map.Entry<String, Double>> sortedCountries = new ArrayList<>(countryToScore.entrySet());
        sortedCountries.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        countryToRanking.clear();

        logger.info("Ranking:\n");
        for (int i = 0; i < sortedCountries.size(); ++i) {
            countryToRanking.put(computeCountry(sortedCountries.get(i).getKey(), i), sortedCountries.get(i).getValue());
            logger.info(sortedCountries.get(i).getKey() + " " + sortedCountries.get(i).getValue());
        }

        lastRankingUpdateTime = currentTime;

        return countryToRanking;
    }

    private String computeCountry(String countryName, int rank) {
        return switch (rank) {
            case 0 -> "First place for " + countryName + "!";
            case 1 -> "Second place for " + countryName + "!";
            case 2 -> "Third place for " + countryName + "!";
            case 3 -> "Fourth place for " + countryName + "!";
            case 4 -> "Fifth place for " + countryName + "!";
            default -> "";
        };
    }

    private void writeFinalRankingFiles() {
        resultList.sort();
        validateResult();

        byte[] participantsRankingContent = getParticipantsRankingContent();
        byte[] countriesRankingContent = getCountriesRankingContent();
        blackList.deleteAll();
        resultList.deleteAll();

        writeToFile("finalParticipantsRanking.txt", participantsRankingContent);
        writeToFile("finalCountriesRanking.txt", countriesRankingContent);

    }

    private void validateResult() {
        try {
            //Participant48 C1,78 this is how a line in valid file looks like
            Path filePath = Paths.get("src/main/resources/valid.txt");
            List<String> validLines = Files.readAllLines(filePath);
            List<Participant> validParticipants = new ArrayList<>();
            for (String validLine : validLines) {
                String[] tokens = validLine.split(",");
                String idAndCountry = tokens[0].substring(10);
                String id = idAndCountry.split(" ")[0];
                String country = getCountry(idAndCountry.split(" ")[1]);
                Double score = Double.parseDouble(tokens[1]);
                validParticipants.add(new Participant(id, score, country));
            }
            for(int i = 0; i < validParticipants.size(); ++i) {
                if(!Objects.equals(validParticipants.get(i).getScore(), resultList.toList().get(i).getScore())) {
                    logger.error("Invalid result at line " + (i + 1) + "!");
                    break;
                }
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
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

    private byte[] getParticipantsRankingContent() {

        StringBuilder contentBuilder = new StringBuilder();
        for (Participant participant : resultList.toList()) {
            contentBuilder.append(participant.toString()).append(System.lineSeparator());
        }
        return contentBuilder.toString().getBytes();
    }

    private byte[] getCountriesRankingContent() {
        StringBuilder contentBuilder = new StringBuilder();
        Map<String, Double> countriesRanking = calculateRanking();
        logger.info("Countries ranking size: " + countriesRanking.size());
        // get countries ordered by score descending
        List<Map.Entry<String, Double>> sortedCountries = new ArrayList<>(countriesRanking.entrySet());
        sortedCountries.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        for (Map.Entry<String, Double> sortedCountry : sortedCountries) {
            contentBuilder.append(sortedCountry.getKey()).append(" It has a total score of ")
                .append(sortedCountry.getValue()).append(System.lineSeparator());
        }
        return contentBuilder.toString().getBytes();
    }

    private void writeToFile(String fileName, byte[] content) {
        try {
            Path filePath = Paths.get(fileName);
            Files.write(filePath, content);
            logger.info("File {} created with ranking content.", fileName);
        } catch (IOException e) {
            logger.error("Error writing file {}: {}", fileName, e.getMessage());
        }
    }

    public byte[] loadFinalParticipantsRankingFile() {
        try {
            Path filePath = Paths.get("finalParticipantsRanking.txt");
            return Files.readAllBytes(filePath);
        } catch (Exception e) {
            logger.error("Error reading file {}: {}", "finalParticipantsRanking.txt", e.getMessage());
            return null;
        }
    }

    public byte[] loadFinalCountriesRankingFile() {
        try {
            Path filePath = Paths.get("finalCountriesRanking.txt");
            return Files.readAllBytes(filePath);
        } catch (Exception e) {
            logger.error("Error reading file {}: {}", "finalCountriesRanking.txt", e.getMessage());
            return null;
        }
    }
}
