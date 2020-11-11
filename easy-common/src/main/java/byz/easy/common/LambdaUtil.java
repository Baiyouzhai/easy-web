package byz.easy.common;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author
 * @since 2020年8月24日
 */
public class LambdaUtil {

	@FunctionalInterface
	public interface ConsumerWarrper<T, E extends Exception> {
		void accept(T t) throws E;
	}

	@FunctionalInterface
	public interface BiConsumerConsumerWarrper<K, V, E extends Exception> {
		void accept(K k, V v) throws E;
	}

	@FunctionalInterface
	public interface FunctionWarrper<T, R, E extends Exception> {
		R apply(T t) throws E;
	}

	public static <T, E extends Exception> Consumer<T> apply(ConsumerWarrper<T, E> consumer) throws E {
		return t -> {
			try {
				consumer.accept(t);
			} catch (Exception e) {
				throwAsUnchecked(e);
			}
		};
	}

	public static <K, V, E extends Exception> BiConsumer<K, V> apply(BiConsumerConsumerWarrper<K, V, E> consumer) throws E {
		return (k, v) -> {
			try {
				consumer.accept(k, v);
			} catch (Exception e) {
				throwAsUnchecked(e);
			}
		};
	}

	public static <T, R, E extends Exception> Function<T, R> apply(FunctionWarrper<T, R, E> function) throws E {
		return t -> {
			try {
				return function.apply(t);
			} catch (Exception e) {
				throwAsUnchecked(e);
				return null;
			}
		};
	}

	private static <E extends Exception> void throwAsUnchecked(Exception exception) throws E {
		throw (E) exception;
	}

}