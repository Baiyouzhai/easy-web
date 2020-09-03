// 必须先引入JavaImport.js
// 必须先引入FunctionContainer.js
var Runner = function (package, name, version, argNames, args) {
	return FunctionContainer.get(package, name, version, argNames).apply(null, args);
}