package com.verinite.interestapp.utils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.verinite.interestapp.exception.ErrorCodes;
import com.verinite.interestapp.exception.InterestAppException;
import com.verinite.interestapp.model.HistoryData;
import com.verinite.interestapp.repository.HistoryDataRepository;

@Component
public class ServiceUtility {
	
	
	public Direction validateDirection(String direction) {
		Direction directionBy = null;
		if(direction.equalsIgnoreCase("asc")) {
			directionBy=Direction.ASC;
		}
		else if(direction.equalsIgnoreCase("desc")) {
			directionBy=Direction.DESC;
		}
		else {
			throw new InterestAppException(ErrorCodes.CONFLICT,
							ErrorCodes.PAGINATION_DIRECTION_NOT_VALID);
		}
		return directionBy;
	}

	public HistoryData checkIdExistOrNot(HistoryDataRepository historyDataRepository ,Integer id) {

		Optional<HistoryData> historyData = historyDataRepository.findById(id);
		if (! historyData.isPresent()) {
			throw new InterestAppException(
					ErrorCodes.NOT_FOUND, ErrorCodes.ID_NOT_PRESENT);
		}
		return historyData.get();
	}

	public <T> T mapObject(Object object , Class<T> clazz) {
		
		ModelMapper mapper = new ModelMapper();
		return  mapper.map(object, clazz);
	
	}
	
	public <T> List<T> mapList(List<T> list , Class<T> clazz){
		ModelMapper mapper = new ModelMapper();
		return 
				list
				.stream()
				.map(object -> mapper.map(object,clazz))
				.collect(Collectors.toList());

	}

}
