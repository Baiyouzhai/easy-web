JscriptEngine 是JS脚本运行的基础, 一个引擎当中包含众多变量和函数(函数名也是一个变量)

JscriptEngineWapper 对 JscriptEngine 进行封装, 严格控制传入 JscriptEngine 的数据

JscriptEngineLoader 对 JscriptEngine 进行加载管理
	1.执行顺序控制, 比如: 定义"var temp = 1;", 而这时有脚本A -> "temp=2;", B -> "temp=3;", 此时脚本的运行顺序就非常重要, 需要有额外的一个属性说明执行顺序
		如果执行顺序调整, 则重新加载全部
	2.别名代码块, 可以给 JscriptInit 定义别名来调用。比如: "var temp = null;", 脚本A -> "temp = 14;" 取别名: "test", 如果想重置可通过"test"别名调用
	3.

JscriptEngineManage 对 JscriptEngine 进行整体管理
	
JscriptEngine
	JscriptEngine 对象构建, 
		注入variables
		运行init
		注册function
	
	注意: JS是弱引用类型变量值可以随意替换, 为了使JscriptEngine引擎使用简单透明且不出错, 定义了几个基本规则。
		1.常量不可修改
		2.函数需要使用注册方法后才能有函数名(变量名), 否则都是以匿名函数直接运行
		在原本的引擎当中不同的环境运行
		getTempEngine() 临时引擎, 临时引擎具有与原引擎一样的环境, 但是不可以修改原环境的任何内容, 
	 

Jscript 是最基础的JS脚本对象封装, 只含有JS脚本代码块, JscriptEngine运行也只运行这些代码块
JscriptInit 是对于JscriptEngine



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
	包含拥有持久引擎的运行环境(变量, 函数), 但是不能对其进行修改, 引擎中做的任何操作最后会还原
