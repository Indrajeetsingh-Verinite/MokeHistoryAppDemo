package com.verinite.interestapp.dto.request;


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
public class RequestBalance {

	private Float amount;
    private String category;
    private Float interest;
    private Float applicableRate;
}
