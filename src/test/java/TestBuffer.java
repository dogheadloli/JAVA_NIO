import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * 2 * @Author: 睿
 * 3 * @Date: 2018/10/29 0029 21:10
 * 4
 */
public class TestBuffer {
	@Test
	public void test1() {
		String str="aaa";
		// 分配一个指定大小的缓冲区
		ByteBuffer buf=ByteBuffer.allocate(1024);

		System.out.println(buf.position());     // 0
		System.out.println(buf.limit());        // 1024
		System.out.println(buf.capacity());     // 1024

		buf.put(str.getBytes());

		System.out.println(buf.position());     // 3
		System.out.println(buf.limit());        // 1024
		System.out.println(buf.capacity());     // 1024

		buf.flip();

		System.out.println(buf.position());     // 0
		System.out.println(buf.limit());        // 3
		System.out.println(buf.capacity());     // 1024

		byte [] dst=new byte[buf.limit()];
		buf.get(dst);
		System.out.println(new String(dst,0,dst.length));

		System.out.println(buf.position());     // 3
		System.out.println(buf.limit());        // 3
		System.out.println(buf.capacity());     // 1024

		buf.rewind();

		System.out.println(buf.position());     // 0
		System.out.println(buf.limit());        // 3
		System.out.println(buf.capacity());     // 1024

		buf.clear();

		System.out.println(buf.position());     // 0
		System.out.println(buf.limit());        // 1024
		System.out.println(buf.capacity());     // 1024

	}

	@Test
	public void test2(){
		String str="abcde";

		ByteBuffer buffer=ByteBuffer.allocate(1024);

		buffer.put(str.getBytes());

		buffer.flip();

		byte[] dst=new byte[buffer.limit()];
		buffer.get(dst,0,2);
		System.out.println(new String(dst,0,2));
		System.out.println(buffer.position());

		buffer.mark();// 标记

		buffer.get(dst,2,2);
		System.out.println(new String(dst,2,2));
		System.out.println(buffer.position());

		buffer.reset();
		System.out.println(buffer.position());
	}

	@Test
	public void test3(){
		// 分配直接缓冲区
		ByteBuffer buffer=ByteBuffer.allocateDirect(1024);

		System.out.println(buffer.isDirect());
	}
}
