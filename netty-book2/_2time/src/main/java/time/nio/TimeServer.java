package time.nio;

import java.io.IOException;

/**
 * Created by Lxt-PC on 2017/12/17.
 */
public class TimeServer {


    public static void main(String[] args) throws IOException {
        //默认端口号
        int port = 8080;
        if(args != null && args.length > 0){
            try{
                //自定义端口号
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){
                //采用默认值
            }
        }
       MultiplexerTimeServer multiplexerTimeServer = new MultiplexerTimeServer(port);
        new Thread(multiplexerTimeServer,"NIO MultiplexerTimeServer-001").start();
    }
}
