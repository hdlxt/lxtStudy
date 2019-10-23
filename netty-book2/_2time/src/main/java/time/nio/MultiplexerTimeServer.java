package time.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Lxt-PC on 2017/12/18.
 */
public class MultiplexerTimeServer implements  Runnable {

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private volatile  boolean stop;

    /**
     * 初始化多路复用器
     * @param port
     */
    public MultiplexerTimeServer(int port){
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            //配置为非阻塞队列
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port),1024);
            //注册OP_ACCEPT类型
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("the time server is start in port:"+port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void stop(){
        this.stop = true;
    }
    @Override
    public void run() {
        while(!stop) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectedKeys.iterator();
                SelectionKey key = null;
                while(it.hasNext()){
                    key = it.next();
                    it.remove();;
                    try{
                        handleInput(key);
                    }catch (Exception e){
                        if(key != null){
                            key.cancel();
                            if(key.channel() != null){
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //多路复用器关闭之后，所有注册在上面的Channle 和 Pipe 等资源都会被自动注册和关闭，所以不需要重复释放资源
        if(selector !=null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if(key.isValid()){
            //处理新接入的请求
            if(key.isAcceptable()){
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector,SelectionKey.OP_READ);
                    if(key.isReadable()){
                        //Read the data
                        SocketChannel sc = (SocketChannel) key.channel();
                        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                        int readBytes = sc.read(readBuffer);
                        if(readBytes > 0){
                            readBuffer.flip();
                            byte[] bytes = new byte[readBuffer.remaining()];
                            readBuffer.get(bytes);
                            String body = new String(bytes,"UTF-8");
                            System.out.println("the time server receive order :"+body);
                            String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ?
                                    new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
                            doWrite(sc,currentTime);
                        }else if (readBytes < 0){
                            //对端链路关闭
                            key.cancel();
                            sc.close();
                        }else{
                            //忽略
                        }
                    }
            }
        }

    }
    private void doWrite(SocketChannel channel,String response) throws IOException {
        if(response != null && response.trim().length() > 0){
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer);
        }

    }
}
