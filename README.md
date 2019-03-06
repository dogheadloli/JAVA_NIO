# JAVA_NIO
## NIO学习
### 1.介绍
JDK 1.4引入的一个新的IO API，支持面向缓冲区、基于通道的IO操作，可以以更加高效的方式进行文件的读写操作。

NIO系统的核心在于：通道（Channel）和缓冲区（Buffer）。通道标识打开到IO设备（文件、套接字）的链接。
若需要使用NIO系统，需要获取用于连接IO设备的通道以及用于容纳数据的缓冲区，然后操作缓冲区，对数据进行处理。
通道负责传输，缓冲区负责存储

### 2.缓冲区
负责数据的存取，就是数组，用于存储不同数据类型的数据。

根据数据类型不同（boolean除外），提供了相应类型的缓冲区

ByteBuffer

CharBuffer

ShortBuffer

IntBuffer

LongBuffer

FloatBuffer

DoubleBuffer

通过allocate()获取缓冲区

#### 属性：
capacity：容量，表示缓冲区中最大存储数据容量，一旦声名不能改变

limit：界限，表示缓冲区可以操作数据的大小。（limit后数据不能读\写）

position：位置，表示缓冲区正在操作数据的位置

mark：标记，记录当前position的位置

#### 方法：
put():存入数据到缓冲区

get():获取缓冲区数据

flip():切换到读数据的模式

rewide():重读数据

clear():清空缓冲区，缓冲区数据依然存在

reset():使position回到mark位置

hasRemaining():获取缓冲区中可操作的数量

### 3.直接缓冲区与非直接缓冲区
非直接缓冲区：通过allocate()方法分配缓冲区，将缓冲区建立在JVM的内存中

直接缓冲区：通过allocateDirect()方法分配缓冲区，将缓冲区建立在物理内存中

### 4.通道
用于源节点与目标节点的链接，在NIO中负责缓冲区数据的传输，Channel本身不储存数据

通道的主要实现类

java.nio.channels.Channel 接口：

    |--FileChannel    // 本地文件 
    
    |--SocketChannel    // Tcp
    
    |--ServerSocketChannel    // Tcp
    
    |--DatagramChannel    // Udp
    
获取通道：

（1）支持通道的类提供getChannel()方法

本地IO：使用流获取通道

FileInputStream/FileOutputStream

RandomAccessFile

网络IO：

Socket

ServerSocket

DatagramSocket

（2）JDK1.7中的NIO2，对各个Channel类提供了open()方法

（3）JDK1.7中的NIO2，Files工具类的newByteChannel()方法

### 5. 分散与聚集
分散读取（Scattering Reads）：将通道中的数据分散到多个缓冲区中

聚集写入（Gathering Writes）：将多个缓冲区中的数据聚集到通道中

### 6.字符集Charset
编码：字符串转换为字节数组

解码：字节数组转换为字符串

### 7.网络通信

使用 NIO 完成网络通信的三个核心：
#### 1. 通道（Channel）：
负责连接
java.nio.channels.Channel 接口：

       |--SelectableChannel
       
           |--SocketChannel    // Tcp
           
           |--ServerSocketChannel    // Tcp
           
           |--DatagramChannel    //UDP
           
           |--Pipe.SinkChannel
           
           |--Pipe.SourceChannel
           
 #### 2. 缓冲区（Buffer）：
 负责数据的存取

 #### 3. 选择器（Selector）：
 是 SelectableChannel 的多路复用器。用于监控 SelectableChannel 的 IO 状况

阻塞式：

非阻塞式：

UDP：

### 管道：
