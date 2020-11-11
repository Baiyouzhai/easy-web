package byz.easy.common;

import java.util.function.Function;

public class ClassFunctionBuilder<T> implements ClassBuilderFactory<T> {

	protected Function<Object, T> callBack;

	public ClassFunctionBuilder(Function<Object, T> callBack) {
		this.callBack = callBack;
	}

	@Override
	public T build(Object... obj) {
		return callBack.apply(obj);
	}


}
