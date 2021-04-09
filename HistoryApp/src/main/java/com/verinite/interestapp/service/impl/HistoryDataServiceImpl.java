package com.verinite.interestapp.service.impl;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.verinite.interestapp.authentication.ConvertKeys;
import com.verinite.interestapp.authentication.JWTHelper;
import com.verinite.interestapp.dto.request.RequestHistoryData;
import com.verinite.interestapp.dto.response.ResponseHistoryData;
import com.verinite.interestapp.exception.ErrorCodes;
import com.verinite.interestapp.exception.InterestAppException;
import com.verinite.interestapp.model.HistoryData;
import com.verinite.interestapp.repository.HistoryDataRepository;
import com.verinite.interestapp.service.HistoryDataService;
import com.verinite.interestapp.utils.DateUtility;
import com.verinite.interestapp.utils.JsonSchemaValidator;
import com.verinite.interestapp.utils.ServiceUtility;

import lombok.SneakyThrows;

@Service
public class HistoryDataServiceImpl implements HistoryDataService {

	private ModelMapper modelMapper = new ModelMapper();
	private ObjectMapper mapper = new ObjectMapper();
	private Gson gson = new Gson();

	private final DateUtility dateUtility;
	private final ServiceUtility serviceUtility;

	private final HistoryDataRepository historyDataRepository;
	private final JWTHelper jwtHelper;
	private final  ConvertKeys convertKeys;
	
	@Autowired
	public HistoryDataServiceImpl(HistoryDataRepository historyDataRepository, DateUtility dateUtility,
			ServiceUtility serviceUtility,JWTHelper jwtHelper,ConvertKeys convertKeys) {
		this.historyDataRepository = historyDataRepository;
		this.dateUtility = dateUtility;
		this.serviceUtility = serviceUtility;
		this.jwtHelper = jwtHelper;
		this.convertKeys = convertKeys;
	}

	@Value("${authtoken.publickey}")
	private String publicKey;

	@Override
	public ResponseHistoryData createHistory(String requestData) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
		String jsonString = jwtHelper.verifyJWTClaimsAndReturnChallange(requestData, convertKeys.stringToPublicKey(publicKey));
		JsonSchemaValidator jsonSchemaValidator = new JsonSchemaValidator();
		jsonSchemaValidator.validate(jsonString);

		RequestHistoryData requestHistoryData = new Gson().fromJson(jsonString, RequestHistoryData.class); 

		if (dateUtility.dateCompare(requestHistoryData.getValidfrom(), requestHistoryData.getValidtill())) {
			throw new InterestAppException(ErrorCodes.ENROLLMENT_FAILED_DUE_TO_PRECONDITION,
					ErrorCodes.DATE_CONDITION_FAILED);
		}
		HistoryData historyData = modelMapper.map(requestHistoryData, HistoryData.class);

		historyData.setCreationDate(dateUtility.getLocalTimestamp());
		historyData.setModificationDate(dateUtility.getLocalTimestamp());
		historyData.setValidfrom(Timestamp.valueOf(requestHistoryData.getValidfrom()));
		historyData.setValidtill(Timestamp.valueOf(requestHistoryData.getValidtill()));
		return serviceUtility.mapObject(historyDataRepository.save(historyData), ResponseHistoryData.class);
	}

	@Override
	public Page<ResponseHistoryData> getHistory(Integer pageNo, Integer pageSize, String direction) {

		Pageable pageable = PageRequest.of(pageNo, pageSize, serviceUtility.validateDirection(direction), "id");

		Page<HistoryData> pageData = historyDataRepository.findAll(pageable);

		// list.forEach(product -> response.add(modelMapper.map(product,
		// ProductResponse.class)))
		// Or

		List pageContentList = pageData.getContent();

		if (pageContentList.isEmpty()) {
			throw new InterestAppException(ErrorCodes.ENROLLMENT_FAILED_DUE_TO_PRECONDITION,
					ErrorCodes.PAGE_LIMIT_EXCEED);
		}
		return new PageImpl<>(serviceUtility.mapList(pageContentList, ResponseHistoryData.class),
				pageData.getPageable(), pageData.getTotalElements());
	}

	@Override
	public ResponseHistoryData getHistoryById(Integer id) {

		return serviceUtility.mapObject(serviceUtility.checkIdExistOrNot(historyDataRepository, id),
				ResponseHistoryData.class);
	}

	public void deleteById(Integer id) {
		serviceUtility.checkIdExistOrNot(historyDataRepository, id);
		historyDataRepository.deleteById(id);
	}

	@SneakyThrows
	@Override
	public ResponseHistoryData patch(String requestData, Integer id)throws InvalidKeySpecException, NoSuchAlgorithmException {

		HistoryData oldHistoryData = serviceUtility.checkIdExistOrNot(historyDataRepository, id);

		String jsonString = jwtHelper.verifyJWTClaimsAndReturnChallange(requestData, convertKeys.stringToPublicKey(publicKey));
		JsonSchemaValidator jsonSchemaValidator = new JsonSchemaValidator();
		System.err.println(jsonString);
		//jsonSchemaValidator.validate(jsonString);

		RequestHistoryData requestHistoryData = new Gson().fromJson(jsonString, RequestHistoryData.class);

		HistoryData newHistoryData = mapper.readerForUpdating(oldHistoryData)
				.readValue(gson.toJson(requestHistoryData));

		if (dateUtility.dateCompare(newHistoryData.getValidfrom().toString(), newHistoryData.getValidtill().toString())) {
			throw new InterestAppException(ErrorCodes.ENROLLMENT_FAILED_DUE_TO_PRECONDITION,
					ErrorCodes.DATE_CONDITION_FAILED);
		}

		newHistoryData.setModificationDate(dateUtility.getLocalTimestamp());

		return serviceUtility.mapObject(historyDataRepository.save(newHistoryData), ResponseHistoryData.class);
	}

}
