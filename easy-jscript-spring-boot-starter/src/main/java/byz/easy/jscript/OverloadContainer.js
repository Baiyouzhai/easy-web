// 重写
const OverloadContainer = function () {
	var methods = new Array();
	var getArgNames = function (argNames, join) {
		return Array.isArray(argNames) ? argNames.join(join) : argNames;
	}
	return {
		getArgNames : getArgNames,
		put : function (argNames, functionBody) {
			let _argNames = getArgNames(argNames, ',');
			let old = methods[_argNames];
			methods[_argNames] = new Function(_argNames, functionBody);
			return old;
		},
		get : function (argNames) {
			let _argNames = getArgNames(argNames, ',');
			return methods[_argNames];
		},
		remove : function (argNames) {
			let _argNames = getArgNames(argNames, ',');
			let old = methods[_argNames];
			delete methods[_argNames];
			return old;
		}
	}
}