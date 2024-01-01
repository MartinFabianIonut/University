package ppd.client.myclient.util;

import lombok.Getter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ppd.client.myclient.entity.Participant;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

public class Client {

    private static final int BATCH_SIZE = 20;

    private static final double DELAY = 0.3; // Δx in seconds

    private final String serverUrl;

    @Getter
    private final ExecutorService executorService;

    private final List<Participant> participants;

    public Client(String serverUrl, List<Participant> participants) {
        this.serverUrl = serverUrl;
        this.executorService = Executors.newFixedThreadPool(5);
        this.participants = participants;
    }

    public void startSending() {
        for(int i = 0; i < Constants.NUMBER_OF_COUNTRIES; ++i) {
            int finalI = i;
            executorService.submit(() -> {
                int start = finalI * 800;
                int end = start + 800;
                for(int j = start; j < end; j += BATCH_SIZE) {
                    sendBatch(participants.subList(j, Math.min(j + BATCH_SIZE, end)));
                    System.out.println("Sent batch " + j + " to " + Math.min(j + BATCH_SIZE, end));
                    try {
                        Thread.sleep((long) (DELAY * 1000));
                    } catch(InterruptedException e) {
                        System.out.println("Interrupted while sleeping!");
                    }
                }
                getRanking();
                getFinalRankingFiles();
            });
        }
        executorService.shutdown();
    }

    private void sendBatch(List<Participant> batch) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Participant>> request = new HttpEntity<>(batch, headers);
        String response = restTemplate.postForObject(serverUrl + "/participants", request, String.class);
        System.out.println("Response from server: " + response);
    }

    public void sendBatchSize(int size) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Integer> request = new HttpEntity<>(size / 20, headers);
        CompletableFuture<String> response = CompletableFuture.supplyAsync(() -> {
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.postForObject(serverUrl + "/totalProducerTasks", request, String.class);
        });
        try {
            System.out.println("Response from server: " + response.get(1, TimeUnit.SECONDS));
        } catch(ExecutionException | TimeoutException | InterruptedException e) {
            System.out.println("Late response from server!");
        }
    }

    public void getRanking() {
        RestTemplate restTemplate = new RestTemplate();
        CompletableFuture<Map<String, Double>> ranking = CompletableFuture.supplyAsync(() -> {
            ResponseEntity<Map<String, Double>> responseEntity = restTemplate.exchange(
                    serverUrl + "/calculateRanking",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    });
            return responseEntity.getBody();
        });

        try {
            ranking.get()
                    .forEach((key, value) -> System.out.println(key + " " + value +
                            ", received from thread with id = " + Thread.currentThread().getId()));
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void getFinalRankingFiles() {
        RestTemplate restTemplate = new RestTemplate();
        byte[] zipFile = restTemplate.getForObject(serverUrl + "/finalRankingFiles", byte[].class);
        saveFileLocally("finalRankingFiles_" + Thread.currentThread().getId() + ".zip", Objects.requireNonNull(zipFile));
    }

    private void saveFileLocally(String fileName, byte[] fileContent) {
        try {
            Path filePath = Paths.get(fileName);
            Files.write(filePath, fileContent);
            System.out.println("File " + fileName + " received and saved locally.");
        } catch (IOException e) {
            System.out.println("Error saving file " + fileName + ": " + e.getMessage());
        }
    }

}