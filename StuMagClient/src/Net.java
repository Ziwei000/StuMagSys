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

	private static final String DEFAULT_HOST = "127.0.0.1";//Ĭ��ip����
	private static final int DEFAULT_PORT = 8887;//Ĭ�϶˿ں�
	private static final String CLIENT_KEY_STORE_PASSWORD = "123456";//�ͻ��˵�˽Կ
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
			//	�ͻ���֤��������֤���
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
			// ��Կ������ο�
			KeyStore ks = KeyStore.getInstance("JKS");
			KeyStore tks = KeyStore.getInstance("JKS");

			ks.load(new FileInputStream("src/kclient.keystore"), CLIENT_KEY_STORE_PASSWORD.toCharArray());
			tks.load(new FileInputStream("src/tclient.keystore"), CLIENT_TRUST_KEY_STORE_PASSWORD.toCharArray());

			kmf.init(ks, CLIENT_KEY_STORE_PASSWORD.toCharArray());
			tmf.init(tks);
			// ��ʼ��SSL������
			ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
			// ��ʼ��SSLSocket
			sslSocket = (SSLSocket) ctx.getSocketFactory().createSocket(DEFAULT_HOST, DEFAULT_PORT);

//            // ��ȡ�������˴�������Ϣ��ObjectInputStream  
//            ois = new ObjectInputStream(sslSocket.getInputStream());  
//            // ��������˷�����Ϣ��ObjectOutputStream  
//            oos = new ObjectOutputStream(sslSocket.getOutputStream()); 

			// ��ȡ�������˴�������Ϣ��DataInputStream
			dis = new DataInputStream(sslSocket.getInputStream());
			// ��������˷�����Ϣ��DataOutputStream
			dos = new DataOutputStream(sslSocket.getOutputStream());

		} catch (Exception e) {
			System.out.println(e);
		}
	}
    //����String�����ֽ���
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
	//����int�����ֽ���
	public void sendInt(int i) {
		try {
			dos.writeInt(i);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//����Boolean�����ֽ���
	public void sendBoolean(boolean b) {
		try {
			dos.writeBoolean(b);
		} catch(Exception e)  {
			e.printStackTrace();
		}
	}
	//����String�����ֽ���
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
	//����int�����ֽ���
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
	//����boolean�����ֽ���
	public boolean recvBool() {
		try {
			boolean b = dis.readBoolean();
			return b;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
  //�ر�������������
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
		System.out.println("�������");
	}

}
