package com.verinite.interestapp.model;

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
public class Attribute {

	private String ledgerId;
	private String statementId;
	private List<Categories> categories;
}
