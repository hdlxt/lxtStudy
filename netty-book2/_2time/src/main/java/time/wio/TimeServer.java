package time.wio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("The time server is start in port:"+port);

            Socket socket = null;
            TimeServerHandlerExecutePool executePool = new TimeServerHandlerExecutePool(
                    50,1000
            );//创建IO任务线程池
            while (true){
                socket = server.accept();
                executePool.execute(new TimeServerHandler(socket));
            }
        }finally {
            if(server != null){
                System.out.println("The time server close");
                server.close();
                server = null;
            }
        }
    }
}
