import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyStore;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

public class Net {

	private static final String DEFAULT_HOST = "127.0.0.1";//默认ip本机
	private static final int DEFAULT_PORT = 8887;//默认端口号
	private static final String CLIENT_KEY_STORE_PASSWORD = "123456";//客户端的私钥
	private static final String CLIENT_TRUST_KEY_STORE_PASSWORD = "123456";//
	private SSLSocket sslSocket;

	public static DataInputStream dis;
	public static DataOutputStream dos;
//	public static ObjectInputStream ois;
//	public static ObjectOutputStream oos; 
	private static String str;
	public Net() {
		this.init();
	}
	public void init() {
		try {
			SSLContext ctx = SSLContext.getInstance("SSL");
			//	客户端证书库和信任证书库
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
			// 密钥库和信任库
			KeyStore ks = KeyStore.getInstance("JKS");
			KeyStore tks = KeyStore.getInstance("JKS");

			ks.load(new FileInputStream("src/kclient.keystore"), CLIENT_KEY_STORE_PASSWORD.toCharArray());
			tks.load(new FileInputStream("src/tclient.keystore"), CLIENT_TRUST_KEY_STORE_PASSWORD.toCharArray());

			kmf.init(ks, CLIENT_KEY_STORE_PASSWORD.toCharArray());
			tmf.init(tks);
			// 初始化SSL上下文
			ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
			// 初始化SSLSocket
			sslSocket = (SSLSocket) ctx.getSocketFactory().createSocket(DEFAULT_HOST, DEFAULT_PORT);

//            // 读取服务器端传过来信息的ObjectInputStream  
//            ois = new ObjectInputStream(sslSocket.getInputStream());  
//            // 向服务器端发送信息的ObjectOutputStream  
//            oos = new ObjectOutputStream(sslSocket.getOutputStream()); 

			// 读取服务器端传过来信息的DataInputStream
			dis = new DataInputStream(sslSocket.getInputStream());
			// 向服务器端发送信息的DataOutputStream
			dos = new DataOutputStream(sslSocket.getOutputStream());

		} catch (Exception e) {
			System.out.println(e);
		}
	}
    //发送String类型字节流
	public void sendString(String s) {
		try {
			dos.writeUTF(s);
//			oos.writeObject(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	public void sendObject(Object o) {
//		try {
//			oos.writeObject(o);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	//发送int类型字节流
	public void sendInt(int i) {
		try {
			dos.writeInt(i);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//发送Boolean类型字节流
	public void sendBoolean(boolean b) {
		try {
			dos.writeBoolean(b);
		} catch(Exception e)  {
			e.printStackTrace();
		}
	}
	//接收String类型字节流
	public String recvString() {
		try {
			str = dis.readUTF();
//			String str = (String)ois.readObject();
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

//	public Object recvObject() {
//		try {
//			return ois.readObject();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	//接收int类型字节流
	public int recvInt() {
		try {
			int i = dis.readInt();
//			String str = (String)ois.readObject();
			return i;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	//接收boolean类型字节流
	public boolean recvBool() {
		try {
			boolean b = dis.readBoolean();
			return b;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
  //关闭所有网络连接
	public void close() {
		try {
			dis.close();
			dos.close();
//			ois.close();
//			oos.close();
			sslSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("任务结束");
	}

}
