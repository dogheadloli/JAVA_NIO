import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

/**
 * 2 * @Author: 睿
 * 3 * @Date: 2018/11/11 0011 19:19
 * 4
 */
public class TestPipe {
	@Test
	public void send() throws IOException {
		// 1.获取管道
		Pipe pipe = Pipe.open();

		// 2.将缓冲区数据写入管道
		ByteBuffer buf = ByteBuffer.allocate(1024);

		Pipe.SinkChannel sinkChannel = pipe.sink();
		buf.put("通过管道发送数据".getBytes());
		buf.flip();
		sinkChannel.write(buf);

		sinkChannel.close();

	}

	@Test
	public void receive() throws IOException {
		Pipe pipe = Pipe.open();

		ByteBuffer buf = ByteBuffer.allocate(1024);
		// 3.读取缓冲区中的数据
		Pipe.SourceChannel sourceChannel = pipe.source();
		buf.flip();
		int len = sourceChannel.read(buf);
		System.out.println(new String(buf.array(), 0, len));
		sourceChannel.close();
	}
}
