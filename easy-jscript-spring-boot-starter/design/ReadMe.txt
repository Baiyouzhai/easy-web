JscriptEngine 是JS脚本运行的基础，一个引擎当中包含变量和函数，在原本的引擎当中不同的环境运行

Jscript 是最基础的JS脚本对象封装，只含有JS脚本代码块，运行也只运行这些代码块，



JscriptEngine ->
	JscriptFolder ->
		JscriptFolder ->
			JscriptFolder ->
				Jscript
				Jscript
			Jscript
			Jscript
		JscriptFolder ->
			Jscript
			Jscript
			Jscript
		JscriptFolder ->
			Jscript

持久引擎 - 运行稳定内容
临时引擎 - 运行测试内容
	包含拥有持久引擎的运行环境(变量，函数)，但是不能对其进行修改，引擎中做的任何操作最后会还原
