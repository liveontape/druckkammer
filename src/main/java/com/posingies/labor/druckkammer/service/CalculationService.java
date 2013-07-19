package com.posingies.labor.druckkammer.service;

import java.util.List;

import com.posingies.labor.druckkammer.service.CalculationServiceImpl.Result;

public interface CalculationService {

	Integer[] calculatePrimeNumbers(double parameter, boolean async);

	List<Result> getResults();

	void clearResults();

}
