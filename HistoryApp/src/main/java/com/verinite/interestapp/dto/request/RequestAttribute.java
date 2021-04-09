package com.verinite.interestapp.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class RequestAttribute {

	private String ledgerId;
	private String statementId;
	private List<RequestCategories> categories;
}
