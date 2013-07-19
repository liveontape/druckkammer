package com.posingies.labor.request;

import java.util.ArrayList;
import java.util.List;

public class Testresult {

	private long start;

	private long end;

	private String response;

	private String description;

	private final List<Testresult> testresults = new ArrayList<Testresult>();

	public long getInterval() {
		return end - start;
	}

	public long getStart() {
		return start;
	}

	public void setStart(final long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(final long end) {
		this.end = end;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(final String response) {
		this.response = response;
	}

	public List<Testresult> getTestresults() {
		return testresults;
	}

	public boolean addTestresult(final Testresult testresult) {
		return this.testresults.add(testresult);
	}

	public boolean removeTestresult(final Testresult testresult) {
		return this.testresults.remove(testresult);
	}

	public void setTestresults(final List<Testresult> testresults) {
		this.testresults.clear();
		this.testresults.addAll(testresults);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Testresult [start=");
		builder.append(start);
		builder.append(", end=");
		builder.append(end);
		builder.append(", response=");
		builder.append(response);
		builder.append(", description=");
		builder.append(description);
		builder.append(", testresults=");
		builder.append(testresults);
		builder.append("]");
		return builder.toString();
	}

}
