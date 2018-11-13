import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLOutput;
import java.util.Map;
import java.util.Set;

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

	//分散和聚集
	@Test
	public void test4() throws IOException {

		// 1.获取通道
		RandomAccessFile randomAccessFile1 = new RandomAccessFile("1.txt", "rw");
		FileChannel fileChannel1 = randomAccessFile1.getChannel();

		// 2.分配缓冲区
		ByteBuffer buf1 = ByteBuffer.allocate(100);
		ByteBuffer buf2 = ByteBuffer.allocate(1024);

		// 3.分散读取
		ByteBuffer[] bufs = {buf1, buf2};
		fileChannel1.read(bufs);

		for (ByteBuffer byteBuffer : bufs) {
			byteBuffer.flip();
		}

		System.out.println(new String(bufs[0].array(), 0, bufs[0].limit()));
		System.out.println(new String(bufs[1].array(), 0, bufs[1].limit()));

		// 4.聚集写入
		RandomAccessFile randomAccessFile2 = new RandomAccessFile("2.txt", "rw");
		FileChannel fileChannel2 = randomAccessFile2.getChannel();

		fileChannel2.write(bufs);
	}

	@Test
	public void test5() {
		Map<String, Charset> map = Charset.availableCharsets();

		Set<Map.Entry<String, Charset>> set = map.entrySet();

		for (Map.Entry<String, Charset> entry : set) {
			System.out.println(entry.getKey() + "=" + entry.getValue());
		}
	}

	@Test
	public void test6() throws CharacterCodingException {
		Charset cs1 = Charset.forName("GBK");

		// 获取编码器
		CharsetEncoder ce = cs1.newEncoder();
		// 获取解码器
		CharsetDecoder cd = cs1.newDecoder();

		CharBuffer cBuf = CharBuffer.allocate(1024);

		cBuf.put("风尘中夜朦胧今相逢论英雄");
		cBuf.flip();

		// 编码
		ByteBuffer bBuf = ce.encode(cBuf);

		for (int i = 0; i < 24; i++) {
			System.out.println(bBuf.get());
		}

		// 解码
		bBuf.flip();
		CharBuffer cBuf2 = cd.decode(bBuf);
		System.out.println(cBuf2.toString());
	}
}
