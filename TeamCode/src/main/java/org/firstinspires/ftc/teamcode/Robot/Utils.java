package org.firstinspires.ftc.teamcode.Robot;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class Utils {
	/**
	 * Checks if lhs is in the interval (rhs - interval, rhs + interval).
	 */
	public static boolean inVicinity(double lhs, double rhs, double interval) {
		return Math.abs(lhs - rhs) < interval;
	}

	/**
	 * Clip the value if necessary, so it fits in the [min, max] interval.
	 */
	public static double clamp(double val, double min, double max) {
		return Math.max(min, Math.min(val, max));
	}

	public static double interpolate(double a, double b, double p, double exp) {
		double factor = Math.pow(1 - p, exp);
		return a * factor + b * (1 - factor);
	}

	/**
	 * Check if the future has completed.
	 */
	public static boolean isDone(Future<?> f) {
		return f == null || f.isDone();
	}

	/**
	 * Check a condition until it is true at the given interval.
	 *
	 * @param scheduler The executor service to use for spawning the poll thread.
	 * @param fn The condition.
	 * @param onEnd An action to run when the returned future is canceled or the condition is true and polling stops.
	 * @param time The interval to poll the condition at.
	 * @param unit The time unit of the interval.
	 * @return A future that completes when the condition is true. Canceling this future will run the provided end action,
	 * if necessary.
	 */
	public static ScheduledFuture<?> poll(ScheduledExecutorService scheduler, Supplier<Boolean> fn, Runnable onEnd, long time, TimeUnit unit) {
		AtomicBoolean endCalled = new AtomicBoolean();

		ScheduledFuture<?> f = scheduler.scheduleAtFixedRate(() -> {
			if (!fn.get()) {
				return;
			}
			if (onEnd != null && endCalled.compareAndSet(false, true)) {
				onEnd.run();
			}
			throw new RuntimeException();
		}, 0, time, unit);

		return new ScheduledFuture<Object>() {
			@Override
			public long getDelay(TimeUnit unit) {
				return f.getDelay(unit);
			}

			@Override
			public int compareTo(Delayed o) {
				return f.compareTo(o);
			}

			@Override
			public boolean cancel(boolean mayInterruptIfRunning) {
				if (!f.cancel(mayInterruptIfRunning)) {
					return false;
				}

				if (endCalled.compareAndSet(false, true)) {
					onEnd.run();
				}

				return true;
			}

			@Override
			public boolean isCancelled() {
				return f.isCancelled();
			}

			@Override
			public boolean isDone() {
				return f.isDone();
			}

			@Override
			public Object get() throws ExecutionException, InterruptedException {
				return f.get();
			}

			@Override
			public Object get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
				return f.get(timeout, unit);
			}
		};
	}
}