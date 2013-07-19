package com.posingies.labor.druckkammer.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.posingies.labor.druckkammer.service.CalculationService;

@RequestMapping("/test")
@Controller
public class CalculationController {

	private final Logger LOG = LoggerFactory
			.getLogger(CalculationController.class);

	@Resource
	private CalculationService calculationService;

	@RequestMapping(value = "", method = RequestMethod.TRACE)
	public void trace() {
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	public Integer[] getTestresult(
			@RequestParam(required = true) final String mode,
			@RequestParam(required = true) final double parameter) {
		return calculationService.calculatePrimeNumbers(parameter,
				"async".equals(mode));
	}

	@RequestMapping(value = "/result/size", method = RequestMethod.GET)
	@ResponseBody
	public int getResultsSize() {
		return calculationService.getResults().size();
	}

	@RequestMapping(value = "/result", method = RequestMethod.DELETE)
	@ResponseBody
	public void clearResults() {
		calculationService.getResults().clear();
	}

}