# protocol
handler the uart protocol data in different issues

解析文件
1. ExampleProtocol 中有个函数 checkAndGetFullData
	项目中 数据格式是这样的  5a a5 len cmd data0 data1 ... dataN checkSum
	checkSum  = (len + cmd + data0 +...+dataN - 1 )&0xFF
	
	通信通过串口传输。。。导致有些数据不能完全传递过来，需要第二次发送过来
	因此这里要做一个数据的拼接整合
	
	
