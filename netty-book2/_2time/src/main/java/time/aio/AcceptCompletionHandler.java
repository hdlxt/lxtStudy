package time.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by Lxt-PC on 2017/12/31.
 */
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel,AsyncTimerServerHandler>{
    @Override
    public void completed(AsynchronousSocketChannel result, AsyncTimerServerHandler attachment) {
        attachment.asynchronousServerSocketChannel.accept(attachment, this);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        result.read(byteBuffer,byteBuffer,new ReadCompletionHandler(result));

    }

    @Override
    public void failed(Throwable exc, AsyncTimerServerHandler attachment) {
        attachment.latch.countDown();
    }
}
