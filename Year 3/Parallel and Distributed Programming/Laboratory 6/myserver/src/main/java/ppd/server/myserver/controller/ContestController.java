package ppd.server.myserver.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import ppd.server.myserver.entity.Participant;
import ppd.server.myserver.service.ContestService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/contest")
public class ContestController {
    Log logger = LogFactory.getLog(ContestController.class);
    private final ContestService contestService;

    public ContestController(ContestService contestService) {
        this.contestService = contestService;
    }

    @Async
    @PostMapping("/participants")
    public CompletableFuture<ResponseEntity<?>> insertParticipants(@RequestBody List<Participant> participantList) {
        contestService.insertParticipants(participantList);
        return CompletableFuture.completedFuture(new ResponseEntity<>("OK", HttpStatus.OK));
    }

    @Async
    @PostMapping("/totalProducerTasks")
    public CompletableFuture<ResponseEntity<?>> setTotalProducerTasks(@RequestBody Integer totalProducerTasks) {
        contestService.setTotalProducerTasks(totalProducerTasks);
        logger.info("Total producer tasks: " + totalProducerTasks);
        return CompletableFuture.completedFuture(new ResponseEntity<>("OK", HttpStatus.OK));
    }
    @Async
    @GetMapping("/calculateRanking")
    public CompletableFuture<ResponseEntity<?>> calculateRanking() {
        logger.info("Calculate ranking");
        return CompletableFuture.completedFuture(new ResponseEntity<>(contestService.calculateRanking(), HttpStatus.OK));
    }

    @Async
    @GetMapping("/finalRankingFiles")
    public CompletableFuture<ResponseEntity<byte[]>> getFinalRankingFiles() {
        try {
            logger.info("Get final ranking files");
            byte[] participantsRankingContent = contestService.loadFinalParticipantsRankingFile();
            byte[] countriesRankingContent = contestService.loadFinalCountriesRankingFile();

            ByteArrayOutputStream zipStream = new ByteArrayOutputStream();
            try (ZipOutputStream zipOut = new ZipOutputStream(zipStream)) {
                addToZip("finalParticipantsRanking.txt", participantsRankingContent, zipOut);
                addToZip("finalCountriesRanking.txt", countriesRankingContent, zipOut);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "finalRankingFiles.zip");
            return CompletableFuture.completedFuture(new ResponseEntity<>(zipStream.toByteArray(), headers, HttpStatus.OK));
        } catch (IOException e) {
            return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    private void addToZip(String fileName, byte[] content, ZipOutputStream zipOut) throws IOException {
        ZipEntry entry = new ZipEntry(fileName);
        zipOut.putNextEntry(entry);
        zipOut.write(content);
        zipOut.closeEntry();
    }
}
