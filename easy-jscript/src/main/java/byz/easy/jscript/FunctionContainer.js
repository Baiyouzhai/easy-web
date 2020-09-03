// 必须先引入JavaImport.js
const FunctionContainer = function () {
	var mapping = new HashMap();
	return {
		put: function (path, argNames, functionBody) {
			let fn = new Function(getArgNames(argNames), functionBody);
			return mapping.put(path, fn);
		},
		get: function (path) {
			return mapping.get(path);
		},
		remove: function (path) {
			return mapping.remove(path);
		}
	}
}()