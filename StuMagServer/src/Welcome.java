import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;

import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.TrustManagerFactory;
import javax.swing.Box;
import javax.swing.JFrame;

public class Welcome extends JFrame {

	private static final int DEFAULT_PORT = 8887;
	private static final String SERVER_KEY_STORE_PASSWORD = "123456";
	private static final String SERVER_TRUST_KEY_STORE_PASSWORD = "123456";

	private SSLServerSocket serverSocket;

	public static void main(String[] args) throws Exception {
		Welcome w = new Welcome();
		w.init();
		w.start();
	}

	public void init() {
		try {
			SSLContext ctx = SSLContext.getInstance("SSL");
			// �����֤��������֤���
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");

			// ��Կ������ο�
			KeyStore ks = KeyStore.getInstance("JKS");
			KeyStore tks = KeyStore.getInstance("JKS");

			ks.load(new FileInputStream("src/kserver.keystore"), SERVER_KEY_STORE_PASSWORD.toCharArray());
			tks.load(new FileInputStream("src/tserver.keystore"), SERVER_TRUST_KEY_STORE_PASSWORD.toCharArray());

			kmf.init(ks, SERVER_KEY_STORE_PASSWORD.toCharArray());
			tmf.init(tks);
			// ��ʼ��SSL������
			ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

			// ��ʼ��SSLSocket
			serverSocket = (SSLServerSocket) ctx.getServerSocketFactory().createServerSocket(DEFAULT_PORT);
			// �������SSLServerSocket��Ҫ��Ȩ�Ŀͻ��˷���
			serverSocket.setNeedClientAuth(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void start() throws Exception {
		if (serverSocket == null) {
			System.out.println("ERROR");
			return;
		}

		JDBC.connect();

//		JDBC.insertSchool("1", "�������ѧ��ͨ�Ź���ѧԺ", "������");
//		JDBC.insertDept("1", "�������ռ䰲ȫϵ", "1", "��Ծ��");
//		JDBC.insertMajor("1", "��Ϣ��ȫ", "1");
//		JDBC.insertClass("��Ϣ��ȫ1501��", "2015", "1");
//		JDBC.insertClass("��Ϣ��ȫ1502��", "2015", "1");
//		JDBC.insertCourse("1", "���ݿ�", "����", 4);
//		JDBC.insertCourse("2", "��ѧ", "����", 2);
//		JDBC.insertCourse("3", "��Ϣϵͳ", "����", 4);
//		JDBC.insertCourse("4", "����ϵͳ", "����", 3);
//		JDBC.insertCourse("5", "���ݽṹ", "����", 4);
//		JDBC.insertCourse("6", "���ݴ���", "ѡ��", 2);
//		JDBC.insertCourse("7", "PASCAL����", "��ѡ", 4);

		// �ȴ��ͻ�������
		while (true) {
			Socket socket = serverSocket.accept();//���շ������˵�sslsocket�������ӵ����ݰ�
			System.out.println("һ���ͻ����Ϸ�������");
			new Thread(new ClientThread(socket)).start();
		}
	}

	private class ClientThread implements Runnable {

		Socket clientSocket = null;
		String role = null;
		Net net = null;

		ClientThread(Socket s) {
			clientSocket = s;//����socket
			net = new Net(s);
			AccessControl.init();//���ʿ���
		}

		public void run() {
			if (!findUser())
				return;
			while (true) {
				String str = net.recvString();//���տͻ��˵ķ�������
				function(str);//�Է������������Ӧ�ķ��ʿ���
			}
		}
       //���ʿ��ƣ����������ݿ�Ĳ����鿴Ȩ��
		private void function(String str) {
			if (str.equals("insertCourseArrange")) {//�����ʦ�ڿ���Ϣ
				if (!AccessControl.isPermited("Teacher_Course��", role, "д")) {//������Ȩ�ޱ��ж��Ƿ��в���Ȩ��
					System.out.println(AccessControl.isPermited("Teacher_Course��", role, "д"));
					net.sendString("��Ȩ��");
					return;
				} else {
					net.sendString("��Ȩ��");
					ConnectToClient.insertCourseArrange(net);//�������紫�䣬��Ӧ�ͻ��˷��������
				}
				System.out.println("�������");
			}
			if (str.equals("chooseCourse")) {//ѡ��γ�
				if (!AccessControl.isPermited("Student_Course��", role, "д")) {//������Ȩ�ޱ��ж��Ƿ��в���Ȩ��
					System.out.println(AccessControl.isPermited("Student_Course��", role, "д"));
					net.sendString("��Ȩ��");
					return;
				} else {
					net.sendString("��Ȩ��");
					ConnectToClient.chooseCourse(net);//�������紫�䣬��Ӧ�ͻ��˷��������
				}
				System.out.println("�������");
			}
			if (str.equals("setGrade")) {//¼��ѧ���ɼ�
				if (!AccessControl.isPermited("Student_Course��", role, "д")) {
					System.out.println(AccessControl.isPermited("Student_Course��", role, "д"));
					net.sendString("��Ȩ��");
					return;
				} else {
					net.sendString("��Ȩ��");
					ConnectToClient.setGrade(net);//�������紫�䣬��Ӧ�ͻ��˷��������
				}
				System.out.println("�������");
			}
			if (str.equals("findSChoosedCourse")) {//������ѡ�γ�
				if (!AccessControl.isPermited("Student_Course��", role, "��")) {
					System.out.println(AccessControl.isPermited("Student_Course��", role, "��"));
					net.sendString("��Ȩ��");
					return;
				} else {
					net.sendString("��Ȩ��");
					ConnectToClient.findSChoosedCourse(net);//�������紫�䣬��Ӧ�ͻ��˷��������
				}
				System.out.println("�������");
			}
			if (str.equals("deleteChoosedCourse")) {//ɾ����ѡ�γ�
				if (!AccessControl.isPermited("Student_Course��", role, "д")) {
					System.out.println(AccessControl.isPermited("Student_Course��", role, "д"));
					net.sendString("��Ȩ��");
					return;
				} else {
					net.sendString("��Ȩ��");
					ConnectToClient.deleteChoosedCourse(net);//�������紫�䣬��Ӧ�ͻ��˷��������
				}
				System.out.println("�������");
			}
			if (str.equals("cnameFindCno")) {//���ݿγ������ҿγ̺�
				if (!AccessControl.isPermited("Course��", role, "��")) {
					System.out.println(AccessControl.isPermited("Course��", role, "��"));
					net.sendString("��Ȩ��");
					return;
				} else {
					net.sendString("��Ȩ��");
					ConnectToClient.cnameFindCno(net);//�������紫�䣬��Ӧ�ͻ��˷��������
				}
				System.out.println("�������");
			}
			if (str.equals("findAccess")) {//����Ȩ��
				if (!AccessControl.isPermited("ACL��", role, "��")) {
					System.out.println(AccessControl.isPermited("ACL��", role, "��"));
					net.sendString("��Ȩ��");
					return;
				} else {
					net.sendString("��Ȩ��");
					AccessControl.findAccess(net);//�������紫�䣬��Ӧ�ͻ��˷��������
				}
				System.out.println("�������");
			}
			if (str.equals("changeAccess")) {//�޸�Ȩ��
				if (!AccessControl.isPermited("ACL��", role, "����д")) {
					System.out.println(AccessControl.isPermited("ACL��", role, "����д"));
					net.sendString("��Ȩ��");
					return;
				} else {
					net.sendString("��Ȩ��");
					AccessControl.changeAccess(net);//�������紫�䣬��Ӧ�ͻ��˷��������
				}
				System.out.println("�������");
			}
			if (str.equals("showGrade")) {//�鿴�ɼ�
				if (!AccessControl.isPermited("Student_Course��", role, "��")) {
					System.out.println(AccessControl.isPermited("Student_Course��", role, "��"));
					net.sendString("��Ȩ��");
					return;
				} else {
					net.sendString("��Ȩ��");
					ConnectToClient.showGrade(net);//�������紫�䣬��Ӧ�ͻ��˷��������
				}
				System.out.println("�������");
			}
			if (str.equals("findSUsingCourse")) {//�鿴ѡ��ÿγ̵�ѧ��
				if (!(AccessControl.isPermited("Student_Course��", role, "��")
						&& AccessControl.isPermited("Student��", role, "��"))) {
					net.sendString("��Ȩ��");
					return;
				} else {
					net.sendString("��Ȩ��");
					ConnectToClient.findSUsingCourse(net);//�������紫�䣬��Ӧ�ͻ��˷��������
				}
				System.out.println("�������");
			}
			if (str.equals("findSpecCourse")) {//�鿴���ڿγ�
				if (!(AccessControl.isPermited("Teacher_Course��", role, "��")
						&& AccessControl.isPermited("Course��", role, "��"))) {
					net.sendString("��Ȩ��");
					return;
				} else {
					net.sendString("��Ȩ��");
					ConnectToClient.findSpecCourse(net);//�������紫�䣬��Ӧ�ͻ��˷��������
				}
				System.out.println("�������");
			}
			if (str.equals("findLessons")) {//����ĳ�ſεĿγ���Ϣ
				if (!AccessControl.isPermited("Course��", role, "��")) {
					net.sendString("��Ȩ��");
					return;
				} else {
					net.sendString("��Ȩ��");
					ConnectToClient.findLessons(net);//�������紫�䣬��Ӧ�ͻ��˷��������
				}
				System.out.println("�������");
			}
			if (str.equals("findTeacher")) {//���ҽ�ʦ��Ϣ
				if (!AccessControl.isPermited("Teacher��", role, "��")) {
					net.sendString("��Ȩ��");
					return;
				} else {
					net.sendString("��Ȩ��");
					ConnectToClient.findTeacher(net);//�������紫�䣬��Ӧ�ͻ��˷��������
				}
				System.out.println("�������");
			}
			if (str.equals("findStudent")) {//����ѧ����Ϣ
				if (!AccessControl.isPermited("Student��", role, "��")) {
					net.sendString("��Ȩ��");
					return;
				} else {
					net.sendString("��Ȩ��");
					ConnectToClient.findStudent(net);//�������紫�䣬��Ӧ�ͻ��˷��������
				}
				System.out.println("�������");
			}
			if (str.equals("insertTeacher")) {//�����ʦ
				if (!AccessControl.isPermited("Teacher��", role, "д")
						|| !AccessControl.isPermited("Register��", role, "д")) {
					net.sendString("��Ȩ��");
					return;
				} else {
					net.sendString("��Ȩ��");
					ConnectToClient.insertTeacher(net);//�������紫�䣬��Ӧ�ͻ��˷��������
				}
				System.out.println("�������");
			}
			if (str.equals("insertStudent")) {//����ѧ��
				if (!AccessControl.isPermited("Student��", role, "д")
						|| !AccessControl.isPermited("Register��", role, "д")) {
					net.sendString("��Ȩ��");
					return;
				} else {
					net.sendString("��Ȩ��");
					ConnectToClient.insertStudent(net);//�������紫�䣬��Ӧ�ͻ��˷��������
				}
				System.out.println("�������");
			}
			if (str.equals("setCode")) {//�޸�����
				if (!AccessControl.isPermited("Register��", role, "д")) {
					net.sendString("��Ȩ��");
					return;
				} else {
					net.sendString("��Ȩ��");
					ConnectToClient.setCode(net);//�������紫�䣬��Ӧ�ͻ��˷��������
				}
				System.out.println("�������");
			}
			if (str.equals("PasswordIsCorrect")) {//�жϾ������Ƿ���ȷ
				if (!AccessControl.isPermited("Register��", role, "��")) {
					net.sendString("��Ȩ��");
					return;
				} else {
					net.sendString("��Ȩ��");
					ConnectToClient.PasswordIsCorrect(net, role);//�������紫�䣬��Ӧ�ͻ��˷��������
				}
				System.out.println("�������");
			}
		}

		public boolean findUser() {//�����û�
			role = net.recvString();//���ս�ɫ
			if (!ConnectToClient.findUser(net, role)) {
				role = null;
				return false;
			}
			return true;
		}

		public void close() {
			if (clientSocket != null) {
				try {
					clientSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
