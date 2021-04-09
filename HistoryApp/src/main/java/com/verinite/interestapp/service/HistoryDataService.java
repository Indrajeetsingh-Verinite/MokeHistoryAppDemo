package com.verinite.interestapp.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.springframework.data.domain.Page;

import com.verinite.interestapp.dto.response.ResponseHistoryData;

public interface HistoryDataService {

	ResponseHistoryData createHistory(String requestHistoryData) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException;

	ResponseHistoryData getHistoryById(Integer id);

	ResponseHistoryData patch(String requestHistoryData,Integer id)throws InvalidKeySpecException, NoSuchAlgorithmException;

	void deleteById(Integer id);

	Page<ResponseHistoryData> getHistory(Integer pageNo, Integer pageSize, String direction);
	
}
