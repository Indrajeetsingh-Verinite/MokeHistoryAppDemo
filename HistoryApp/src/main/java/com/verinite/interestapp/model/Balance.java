package com.verinite.interestapp.model;

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
public class Balance {

	private Float amount;
    private String category;
    private Float interest;
    private Float applicableRate;
}
