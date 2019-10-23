package time.aio;

/**
 * Created by Lxt-PC on 2017/12/31.
 */
public class TimeServer {
    public static void main(String[] args) {
        int port = 8080;
        if(args != null && args.length > 0){
            try{
                port = Integer.valueOf(args[0]);
            }catch(NumberFormatException e){
                //采用默认值
            }
        }
        AsyncTimerServerHandler timerServerHandler = new AsyncTimerServerHandler(port);
        new Thread(timerServerHandler,"AIO-AsyncTimerServerHandler-00").start();
    }
}

