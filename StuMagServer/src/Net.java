import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class Net {

	private Socket mySocket;
	private DataInputStream dis;
	private DataOutputStream dos; 
//	private ObjectInputStream ois;
//	private ObjectOutputStream oos; 
	private String str;

	public Net(Socket s) {
		mySocket = s;
		init();
	}
	
	public void init() {
		try {
			dis=new DataInputStream(mySocket.getInputStream());//����������
			dos=new DataOutputStream(mySocket.getOutputStream());//���������
//			ois=new ObjectInputStream(mySocket.getInputStream());
//			oos=new ObjectOutputStream(mySocket.getOutputStream());
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
			mySocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
