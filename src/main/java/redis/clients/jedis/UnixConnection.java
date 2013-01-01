package redis.clients.jedis;

import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.util.RedisInputStream;
import redis.clients.util.RedisOutputStream;
import org.newsclub.net.unix.AFUNIXSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;

import java.io.File;
import java.io.IOException;



/**
 * Created with IntelliJ IDEA.
 * User: Bernard McCormack
 * Date: 1/1/13
 * Time: 2:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class UnixConnection  extends Connection{

    public void connect() {
        if (!isConnected()) {
            try {

                File socketFile = new File("test");
                 socket = AFUNIXSocket.newInstance();


                //->@wjw_add
                socket.setReuseAddress(true);
                socket.setKeepAlive(true);  //Will monitor the TCP connection is valid
                socket.setTcpNoDelay(true);  //Socket buffer Whetherclosed, to ensure timely delivery of data
                socket.setSoLinger(true,0);  //Control calls close () method, the underlying socket is closed immediately
                //<-@wjw_add

                socket.connect(new AFUNIXSocketAddress(socketFile));
                socket.setSoTimeout(timeout);
                outputStream = new RedisOutputStream(socket.getOutputStream());
                inputStream = new RedisInputStream(socket.getInputStream());
            } catch (IOException ex) {
                throw new JedisConnectionException(ex);
            }
        }
    }

}
