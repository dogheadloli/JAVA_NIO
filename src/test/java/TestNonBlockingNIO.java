import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 2 * @Author: 睿
 * 3 * @Date: 2018/11/11 0011 10:05
 * 4
 */
public class TestNonBlockingNIO {

	// 客户端
	@Test
	public void client() throws IOException, InterruptedException {
		// 1.获取通道
		SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",9898));

		// 2.切换为非阻塞模式
		sChannel.configureBlocking(false);

		// 3.创建缓冲区
		ByteBuffer buf = ByteBuffer.allocate(1024);

		// 4.发送数据
		buf.put("客户端发送啊啊啊".getBytes());
		buf.flip();
		sChannel.write(buf);
		buf.clear();

		Thread.sleep(10000);

		sChannel.close();
	}

	// 服务端
	@Test
	public void server() throws IOException {
		// 1.获取通道
		ServerSocketChannel ssChannel = ServerSocketChannel.open();

		// 2.切换为非阻塞
		ssChannel.configureBlocking(false);

		// 3.绑定连接
		ssChannel.bind(new InetSocketAddress(9898));

		// 4.获取选择器
		Selector selector = Selector.open();

		// 5.将通道注册到选择器，并指定监听事件
		ssChannel.register(selector, SelectionKey.OP_ACCEPT);

		// 6.轮询式获取选择器上已经“准备就绪”的事件
		while (selector.select() > 0) {
			// 7.获取当前选择器中所有就绪的事件
			Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

			while (iterator.hasNext()) {
				// 8.获取准备就绪的事件
				SelectionKey sk = iterator.next();

				// 9.判断具体是什么事件准备就绪
				if (sk.isAcceptable()) {
					// 10.若"接收就绪"则获取客户端连接
					SocketChannel schannel = ssChannel.accept();
					schannel.configureBlocking(false);

					// 11.注册到选择器
					schannel.register(selector, SelectionKey.OP_READ);
				} else if (sk.isReadable()) {
					// 12.获取"读就绪“的通道
					SocketChannel schannel = (SocketChannel) sk.channel();

					// 13.读数据
					ByteBuffer buf = ByteBuffer.allocate(1024);
					int len = 0;
					while ((len = schannel.read(buf)) > 0) {
						buf.flip();
						System.out.println(new String(buf.array(), 0, len));
						buf.clear();
					}
				}
				// 取消选择键
				iterator.remove();
			}
		}
	}
}
