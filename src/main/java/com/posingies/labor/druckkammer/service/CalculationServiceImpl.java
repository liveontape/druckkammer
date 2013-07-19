package com.posingies.labor.druckkammer.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.R;
import reactor.core.Environment;
import reactor.core.Reactor;
import reactor.event.Event;
import reactor.event.dispatch.Dispatcher;
import reactor.event.dispatch.ThreadPoolExecutorDispatcher;
import reactor.event.selector.RegexSelector;
import reactor.function.Consumer;

public class CalculationServiceImpl implements CalculationService {

	private static final Logger LOG = LoggerFactory
			.getLogger(CalculationServiceImpl.class);

	private boolean ioBlockingSimulationEnabled;

	private final List<Result> reactorResults = new ArrayList<Result>();

	private final Environment reactorEnvironment = createReactorEnvironment();

	private final Reactor reactor = createReactor();

	private Environment createReactorEnvironment() {
		final Environment _env = new Environment();
		final Dispatcher dispatcher = new ThreadPoolExecutorDispatcher(100, 100);
		_env.addDispatcher(Environment.THREAD_POOL, dispatcher);
		return _env;
	}

	public static class Result {

		private double parameter;
		private Integer[] result;

		public Result(final double parameter, final Integer[] result) {
			super();
			this.parameter = parameter;
			this.result = result;
		}

		public double getParameter() {
			return parameter;
		}

		public void setParameter(final double parameter) {
			this.parameter = parameter;
		}

		public Integer[] getResult() {
			return result;
		}

		public void setResult(final Integer[] result) {
			this.result = result;
		}

	}

	private Reactor createReactor() {
		final Reactor _reactor = R.reactor().env(reactorEnvironment)
				.dispatcher(Environment.EVENT_LOOP).get();
		_reactor.on(new RegexSelector("calculatePrimeNumbers"),
				new Consumer<Event<Double>>() {

					@Override
					public void accept(final Event<Double> ev) {
						try {
							LOG.trace("workerReactor received event "
									+ ev.getData());
							final double parameter = ev.getData();
							final Integer[] result = calculatePrimeNumbers(
									parameter, true);
							addResult(new Result(parameter, result));
						} catch (final Exception e) {
							e.printStackTrace();
						}
					}

				});
		return _reactor;
	}

	@Override
	public Integer[] calculatePrimeNumbers(final double parameter,
			final boolean async) {
		if (ioBlockingSimulationEnabled) {
			if (System.currentTimeMillis() % 3 == 0) {
				try {
					LOG.trace("Simulating IO blocking (thread sleeps for 2 s)");
					Thread.sleep(2000);
					LOG.trace("Thread woke up!");
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		final List<Integer> buffer = new ArrayList<Integer>();
		int x, y;
		for (x = 2; x < parameter; x++) {
			if (x % 2 != 0 || x == 2) {
				for (y = 2; y <= x / 2; y++) {
					if (x % y == 0) {
						break;
					}
				}
				if (y > x / 2) {
					buffer.add(x);
				}
			}
		}
		LOG.trace("Calculated " + buffer.size()
				+ " prim number(s) for parameter " + parameter);
		return buffer.toArray(new Integer[buffer.size()]);
	}

	public void setIoBlockingSimulationEnabled(
			final boolean ioBlockingSimulationEnabled) {
		this.ioBlockingSimulationEnabled = ioBlockingSimulationEnabled;
	}

	@Override
	public List<Result> getResults() {
		return reactorResults;
	}

	private boolean addResult(final Result result) {
		return reactorResults.add(result);
	}

	@Override
	public void clearResults() {
		this.reactorResults.clear();
	}

}
