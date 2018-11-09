import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 2 * @Author: 睿
 * 3 * @Date: 2018/11/7 0007 18:56
 * 4
 */
public class TestChannel {

	// 利用通道完成文件复制
	@Test
	public void test1() throws IOException {

		FileInputStream fileInputStream = new FileInputStream("F:\\logs\\1.jpg");
		FileOutputStream fileOutputStream = new FileOutputStream("F:\\logs\\a.jpg");

		// 获取通道
		FileChannel inChannel = fileInputStream.getChannel();
		FileChannel outChannel = fileOutputStream.getChannel();

		// 分配指定大小缓冲区
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

		// 将通道数据存入缓冲区
		while (inChannel.read(byteBuffer) != -1) {
			// 切换为读取模式
			byteBuffer.flip();
			// 将缓冲区数据写入通道
			outChannel.write(byteBuffer);
			// 清空缓冲区
			byteBuffer.clear();
		}
		outChannel.close();
		inChannel.close();
		fileInputStream.close();
		fileOutputStream.close();
	}

	// 使用直接缓冲区
	@Test
	public void test2() throws IOException {
		// 获取通道
		FileChannel inChannel = FileChannel.open(Paths.get("F:\\logs\\2.jpg"), StandardOpenOption.READ);
		FileChannel outChannel = FileChannel.open(Paths.get("F:\\logs\\b.jpg"), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);

		// 内存映射文件
		MappedByteBuffer inMapBuf = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
		MappedByteBuffer outMapBuf = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());

		//直接对缓冲区进行数据的读写操作
		byte[] dst = new byte[inMapBuf.limit()];
		inMapBuf.get(dst);
		outMapBuf.put(dst);

		inChannel.close();
		outChannel.close();
	}

	// 通道之间传输使用直接缓冲区
	@Test
	public void test3() throws IOException {
		// 获取通道
		FileChannel inChannel = FileChannel.open(Paths.get("F:\\logs\\2.jpg"), StandardOpenOption.READ);
		FileChannel outChannel = FileChannel.open(Paths.get("F:\\logs\\b.jpg"), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);

		//inChannel.transferTo(0,inChannel.size(),outChannel);
		outChannel.transferFrom(inChannel, 0, inChannel.size());

		inChannel.close();
		outChannel.close();
	}


}
