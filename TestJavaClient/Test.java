package TestJavaClient;

import java.net.Socket;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class Test {
    public static void main(String[] args) {
/*        String _ip = string.Empty;
         int _port = 0;
          Socket _socket = null;
         byte[] buffer = new byte[1024 * 1024 * 2];
        //1.0 实例化套接字(IP4寻址地址,流式传输,TCP协议)
        _socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
        //2.0 创建IP对象
        IPAddress address = IPAddress.Parse(_ip);
        //3.0 创建网络端口包括ip和端口
        IPEndPoint endPoint = new IPEndPoint(address, _port);
        //4.0 建立连接
        _socket.Connect(endPoint);
         Console.WriteLine("连接服务器成功")*/;

        TimeZone tz=TimeZone.getTimeZone("America/New_York");
        Calendar gc2=Calendar.getInstance(tz, Locale.US);

        //GregorianCalendar gc2 = new GregorianCalendar();
        //gc2.setTimeZone(TimeZone.getTimeZone("GMT"));//GMT America/New_York
        String dateTime2 = "" +
                gc2.get(Calendar.YEAR) +
                pad(gc2.get(Calendar.MONTH) + 1) +
                pad(gc2.get(Calendar.DAY_OF_MONTH)) + " " +
                pad(gc2.get(Calendar.HOUR_OF_DAY)) + ":" +
                pad(gc2.get(Calendar.MINUTE)) + ":" +
                pad(gc2.get(Calendar.SECOND)) + " " +
                gc2.getTimeZone().getDisplayName( false, TimeZone.SHORT);

        System.out.println(dateTime2);



    }

    private static String pad(int val) {
        return val < 10 ? "0" + val : "" + val;
    }
}
