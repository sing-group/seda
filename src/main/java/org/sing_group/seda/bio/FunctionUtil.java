package org.sing_group.seda.bio;

import java.util.function.Consumer;
import java.util.function.Function;

public class FunctionUtil {

	public static <T, R> Function<T, R> wrapWithExceptionToNull(Function<T, R> function) {
		return wrapWithExceptionToNull(function, e -> {});
	}

	public static <T, R> Function<T, R> wrapWithExceptionToNull(Function<T, R> function, Consumer<Throwable> exceptionManager) {
		return param -> {
			try {
				return function.apply(param);
			} catch (Throwable e) {
				exceptionManager.accept(e);
				return null;
			}
		};
	}

}
