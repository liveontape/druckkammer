package com.posingies.labor.request;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class SenderTest extends TestCase {

	private static final Logger LOG = LoggerFactory.getLogger(SenderTest.class);
	private static final String REST_ROOT_URL = "http://localhost:8080/druckkammer";
	private static final int TEST_RUNS = 20;
	private static final RestTemplate rest = initRestTemplate();

	private static RestTemplate initRestTemplate() {
		return new RestTemplate();
	}

	private static class TestRunnerThread extends Thread {

		private final String url;
		private final Testresult testresult;
		private final double parameter;
		private final boolean async;

		private TestRunnerThread(final Testresult _testresult,
				final String _url, final double _parameter, final boolean _async) {
			testresult = _testresult;
			url = _url;
			parameter = _parameter;
			async = _async;
		}

		@Override
		public void run() {
			try {
				final Testresult result = readTestresult(url, parameter, async);
				testresult.addTestresult(result);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void testSynchronousRestAccess() throws IOException {
		final Testresult testresult = new Testresult();
		testresult.setStart(System.nanoTime());
		Thread testRunnerThread;
		for (int index = 0; index < TEST_RUNS; index++) {
			testRunnerThread = new TestRunnerThread(testresult, REST_ROOT_URL
					+ "/test?mode=sync&parameter=", index, false);
			testRunnerThread.run();
		}
		testresult.setEnd(System.nanoTime());
		analyse(testresult, "testSynchronousRestAccess");
	}

	public void testAsynchronousRestAccess() throws IOException {
		final Testresult testresult = new Testresult();
		testresult.setStart(System.nanoTime());
		Thread testRunnerThread;
		for (int index = 0; index < TEST_RUNS; index++) {
			testRunnerThread = new TestRunnerThread(testresult, REST_ROOT_URL
					+ "/test?mode=async&parameter=", index, true);
			testRunnerThread.run();
		}
		int readResultsize = readResultsize();
		while (readResultsize < TEST_RUNS) {
			readResultsize = readResultsize();
		}
		LOG.trace(" Async processing finished (readResultsize="
				+ readResultsize + ")");
		testresult.setEnd(System.nanoTime());
		analyse(testresult, "testAsynchronousRestAccess");
	}

	private int readResultsize() throws MalformedURLException, IOException {
		return rest.getForObject(REST_ROOT_URL + "/test/result/size",
				Integer.class);
	}

	private void analyse(final Testresult testresult, final String title) {
		double lowest = Double.MAX_VALUE;
		double highest = 0;
		double avg = 0;
		double sum = 0;
		for (final Testresult _testresult : testresult.getTestresults()) {
			sum += _testresult.getInterval();
			if (_testresult.getInterval() < lowest) {
				lowest = _testresult.getInterval();
			}
			if (_testresult.getInterval() > highest) {
				highest = _testresult.getInterval();
			}
		}
		avg = sum / testresult.getTestresults().size();
		LOG.trace("**** Results of '" + title + "' ****");
		LOG.trace("Total: " + secondsString(testresult.getInterval()));
		LOG.trace("Lowest: " + secondsString(lowest));
		LOG.trace("Highest: " + secondsString(highest));
		LOG.trace("Average: " + secondsString(avg));
	}

	private String secondsString(final double nanoseconds) {
		return (nanoseconds / 1000000000) + " s";
	}

	private static Testresult readTestresult(final String url,
			final double parameter, final boolean async) throws IOException {
		final Testresult result = new Testresult();
		result.setStart(System.nanoTime());
		result.setResponse(Arrays.toString(rest.getForObject(url + parameter,
				Integer[].class)));
		result.setEnd(System.nanoTime());
		return result;
	}

	@Override
	protected void tearDown() throws Exception {
		rest.delete(REST_ROOT_URL + "/test/result");
	}

}
