package com.verinite.interestapp.controller;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

import com.nimbusds.jose.JOSEException;
import com.verinite.interestapp.authentication.ConvertKeys;
import com.verinite.interestapp.authentication.GenerateToken;
import com.verinite.interestapp.dto.response.ResponseHistoryData;
import com.verinite.interestapp.service.impl.HistoryDataServiceImpl;

@RestController
@RequestMapping("/history")
public class HistoryDataController {

	private HistoryDataServiceImpl historyDataServiceImpl;
	@Value("${authtoken.privatekey}")
	private String privateKey ;

    ConvertKeys convertKeys = new ConvertKeys();

    @Autowired
    private WebClient.Builder webClientBuilder;

	@Autowired
	public HistoryDataController(HistoryDataServiceImpl historyDataServiceImpl) {
		super();
		this.historyDataServiceImpl = historyDataServiceImpl;
	}

	@PostMapping
	public ResponseEntity<ResponseHistoryData> historyCreation(@RequestBody String requestHistoryData)
			throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {

		return new ResponseEntity<>(historyDataServiceImpl.createHistory(requestHistoryData), HttpStatus.OK);

	}

	@GetMapping
	public ResponseEntity<Page<ResponseHistoryData>> getHistory(@RequestParam Integer start,
			@RequestParam Integer limit, @RequestParam String direction) {

		return new ResponseEntity<>(historyDataServiceImpl.getHistory(start, limit, direction), HttpStatus.OK);
	}

	@GetMapping("/{historyId}")
	public ResponseEntity<ResponseHistoryData> getHistoryById(@PathVariable Integer historyId) {

		return new ResponseEntity<>(historyDataServiceImpl.getHistoryById(historyId), HttpStatus.OK);
	}

	@DeleteMapping("/{historyId}")
	public ResponseEntity<Object> histroyDeletionById(@PathVariable Integer historyId) {
		historyDataServiceImpl.deleteById(historyId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PatchMapping("/{historyId}")
	public ResponseEntity<ResponseHistoryData> histroyUpdation(@RequestBody String requestHistoryData,
			@PathVariable Integer historyId) throws InvalidKeySpecException, NoSuchAlgorithmException {

		return new ResponseEntity<>(historyDataServiceImpl.patch(requestHistoryData, historyId), HttpStatus.OK);
	}

    @PostMapping("/generate")
    public String generate(@RequestBody String signedData) throws NoSuchAlgorithmException, InvalidKeySpecException, JOSEException{
        GenerateToken tokenObject = new GenerateToken();
		return tokenObject.getToken(signedData, 30000,convertKeys.stringToPrivateKey(privateKey)); 
    }

    @PostMapping("/saveHistory")
    public Object saveHistory(@RequestBody String signedData) throws NoSuchAlgorithmException, InvalidKeySpecException, JOSEException{
        GenerateToken tokenObject = new GenerateToken();
		 String token = tokenObject.getToken(signedData, 30000,convertKeys.stringToPrivateKey(privateKey)); 
         return webClientBuilder.build()
                                    .post()
                                    .uri("http://localhost:8086/history")
                                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                    .body(Mono.just(token), String.class)
                                    .retrieve()
                                    .bodyToMono(Object.class)
                                    .block();
    }

    @PatchMapping("/updateHistory/{historyId}")
    public Object updateHistory(@RequestBody String signedData,@RequestParam Integer historyId) throws NoSuchAlgorithmException, InvalidKeySpecException, JOSEException{
        GenerateToken tokenObject = new GenerateToken();
		String token = tokenObject.getToken(signedData, 30000,convertKeys.stringToPrivateKey(privateKey)); 

         return webClientBuilder.build()
                                    .patch()
                                    .uri("http://localhost:8086/history/"+historyId)
                                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                    .body(Mono.just(token), String.class)
                                    .retrieve()
                                    .bodyToMono(Object.class)
                                    .block();
    }
	


}
