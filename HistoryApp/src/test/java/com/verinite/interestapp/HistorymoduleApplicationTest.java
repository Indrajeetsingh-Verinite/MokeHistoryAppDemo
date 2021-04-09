package com.verinite.interestapp;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.verinite.interestapp.controller.HistoryDataController;
import com.verinite.interestapp.repository.HistoryDataRepository;
import com.verinite.interestapp.utils.TestConfig;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class HistorymoduleApplicationTest extends TestConfig {

	@Autowired private HistoryDataRepository historyDataRepository;
	@Autowired protected HistoryDataController historyDataController;
	
	@Test
	public void contextLoads() {

		String s = "Hello";
		assertEquals( "Hello",s);
	}

	@Override
	public void setup() {

		historyDataRepository.deleteAllInBatch();
	}

}
