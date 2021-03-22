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
			// 服务端证书库和信任证书库
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");

			// 密钥库和信任库
			KeyStore ks = KeyStore.getInstance("JKS");
			KeyStore tks = KeyStore.getInstance("JKS");

			ks.load(new FileInputStream("src/kserver.keystore"), SERVER_KEY_STORE_PASSWORD.toCharArray());
			tks.load(new FileInputStream("src/tserver.keystore"), SERVER_TRUST_KEY_STORE_PASSWORD.toCharArray());

			kmf.init(ks, SERVER_KEY_STORE_PASSWORD.toCharArray());
			tmf.init(tks);
			// 初始化SSL上下文
			ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

			// 初始化SSLSocket
			serverSocket = (SSLServerSocket) ctx.getServerSocketFactory().createServerSocket(DEFAULT_PORT);
			// 设置这个SSLServerSocket需要授权的客户端访问
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

//		JDBC.insertSchool("1", "计算机科学与通信工程学院", "王良民");
//		JDBC.insertDept("1", "物联网空间安全系", "1", "赵跃华");
//		JDBC.insertMajor("1", "信息安全", "1");
//		JDBC.insertClass("信息安全1501班", "2015", "1");
//		JDBC.insertClass("信息安全1502班", "2015", "1");
//		JDBC.insertCourse("1", "数据库", "必修", 4);
//		JDBC.insertCourse("2", "数学", "必修", 2);
//		JDBC.insertCourse("3", "信息系统", "必修", 4);
//		JDBC.insertCourse("4", "操作系统", "必修", 3);
//		JDBC.insertCourse("5", "数据结构", "必修", 4);
//		JDBC.insertCourse("6", "数据处理", "选修", 2);
//		JDBC.insertCourse("7", "PASCAL语言", "任选", 4);

		// 等待客户端连接
		while (true) {
			Socket socket = serverSocket.accept();//接收服务器端的sslsocket请求连接的数据包
			System.out.println("一个客户连上服务器了");
			new Thread(new ClientThread(socket)).start();
		}
	}

	private class ClientThread implements Runnable {

		Socket clientSocket = null;
		String role = null;
		Net net = null;

		ClientThread(Socket s) {
			clientSocket = s;//建立socket
			net = new Net(s);
			AccessControl.init();//访问控制
		}

		public void run() {
			if (!findUser())
				return;
			while (true) {
				String str = net.recvString();//接收客户端的访问请求
				function(str);//对访问请求进行相应的访问控制
			}
		}
       //访问控制，对所有数据库的操作查看权限
		private void function(String str) {
			if (str.equals("insertCourseArrange")) {//插入教师授课信息
				if (!AccessControl.isPermited("Teacher_Course表", role, "写")) {//读访问权限表，判断是否有操作权限
					System.out.println(AccessControl.isPermited("Teacher_Course表", role, "写"));
					net.sendString("无权限");
					return;
				} else {
					net.sendString("有权限");
					ConnectToClient.insertCourseArrange(net);//建立网络传输，回应客户端发起的请求
				}
				System.out.println("任务结束");
			}
			if (str.equals("chooseCourse")) {//选择课程
				if (!AccessControl.isPermited("Student_Course表", role, "写")) {//读访问权限表，判断是否有操作权限
					System.out.println(AccessControl.isPermited("Student_Course表", role, "写"));
					net.sendString("无权限");
					return;
				} else {
					net.sendString("有权限");
					ConnectToClient.chooseCourse(net);//建立网络传输，回应客户端发起的请求
				}
				System.out.println("任务结束");
			}
			if (str.equals("setGrade")) {//录入学生成绩
				if (!AccessControl.isPermited("Student_Course表", role, "写")) {
					System.out.println(AccessControl.isPermited("Student_Course表", role, "写"));
					net.sendString("无权限");
					return;
				} else {
					net.sendString("有权限");
					ConnectToClient.setGrade(net);//建立网络传输，回应客户端发起的请求
				}
				System.out.println("任务结束");
			}
			if (str.equals("findSChoosedCourse")) {//查找已选课程
				if (!AccessControl.isPermited("Student_Course表", role, "读")) {
					System.out.println(AccessControl.isPermited("Student_Course表", role, "读"));
					net.sendString("无权限");
					return;
				} else {
					net.sendString("有权限");
					ConnectToClient.findSChoosedCourse(net);//建立网络传输，回应客户端发起的请求
				}
				System.out.println("任务结束");
			}
			if (str.equals("deleteChoosedCourse")) {//删除已选课程
				if (!AccessControl.isPermited("Student_Course表", role, "写")) {
					System.out.println(AccessControl.isPermited("Student_Course表", role, "写"));
					net.sendString("无权限");
					return;
				} else {
					net.sendString("有权限");
					ConnectToClient.deleteChoosedCourse(net);//建立网络传输，回应客户端发起的请求
				}
				System.out.println("任务结束");
			}
			if (str.equals("cnameFindCno")) {//根据课程名查找课程号
				if (!AccessControl.isPermited("Course表", role, "读")) {
					System.out.println(AccessControl.isPermited("Course表", role, "读"));
					net.sendString("无权限");
					return;
				} else {
					net.sendString("有权限");
					ConnectToClient.cnameFindCno(net);//建立网络传输，回应客户端发起的请求
				}
				System.out.println("任务结束");
			}
			if (str.equals("findAccess")) {//查找权限
				if (!AccessControl.isPermited("ACL表", role, "读")) {
					System.out.println(AccessControl.isPermited("ACL表", role, "读"));
					net.sendString("无权限");
					return;
				} else {
					net.sendString("有权限");
					AccessControl.findAccess(net);//建立网络传输，回应客户端发起的请求
				}
				System.out.println("任务结束");
			}
			if (str.equals("changeAccess")) {//修改权限
				if (!AccessControl.isPermited("ACL表", role, "读、写")) {
					System.out.println(AccessControl.isPermited("ACL表", role, "读、写"));
					net.sendString("无权限");
					return;
				} else {
					net.sendString("有权限");
					AccessControl.changeAccess(net);//建立网络传输，回应客户端发起的请求
				}
				System.out.println("任务结束");
			}
			if (str.equals("showGrade")) {//查看成绩
				if (!AccessControl.isPermited("Student_Course表", role, "读")) {
					System.out.println(AccessControl.isPermited("Student_Course表", role, "读"));
					net.sendString("无权限");
					return;
				} else {
					net.sendString("有权限");
					ConnectToClient.showGrade(net);//建立网络传输，回应客户端发起的请求
				}
				System.out.println("任务结束");
			}
			if (str.equals("findSUsingCourse")) {//查看选择该课程的学生
				if (!(AccessControl.isPermited("Student_Course表", role, "读")
						&& AccessControl.isPermited("Student表", role, "读"))) {
					net.sendString("无权限");
					return;
				} else {
					net.sendString("有权限");
					ConnectToClient.findSUsingCourse(net);//建立网络传输，回应客户端发起的请求
				}
				System.out.println("任务结束");
			}
			if (str.equals("findSpecCourse")) {//查看所授课程
				if (!(AccessControl.isPermited("Teacher_Course表", role, "读")
						&& AccessControl.isPermited("Course表", role, "读"))) {
					net.sendString("无权限");
					return;
				} else {
					net.sendString("有权限");
					ConnectToClient.findSpecCourse(net);//建立网络传输，回应客户端发起的请求
				}
				System.out.println("任务结束");
			}
			if (str.equals("findLessons")) {//查找某门课的课程信息
				if (!AccessControl.isPermited("Course表", role, "读")) {
					net.sendString("无权限");
					return;
				} else {
					net.sendString("有权限");
					ConnectToClient.findLessons(net);//建立网络传输，回应客户端发起的请求
				}
				System.out.println("任务结束");
			}
			if (str.equals("findTeacher")) {//查找教师信息
				if (!AccessControl.isPermited("Teacher表", role, "读")) {
					net.sendString("无权限");
					return;
				} else {
					net.sendString("有权限");
					ConnectToClient.findTeacher(net);//建立网络传输，回应客户端发起的请求
				}
				System.out.println("任务结束");
			}
			if (str.equals("findStudent")) {//查找学生信息
				if (!AccessControl.isPermited("Student表", role, "读")) {
					net.sendString("无权限");
					return;
				} else {
					net.sendString("有权限");
					ConnectToClient.findStudent(net);//建立网络传输，回应客户端发起的请求
				}
				System.out.println("任务结束");
			}
			if (str.equals("insertTeacher")) {//插入教师
				if (!AccessControl.isPermited("Teacher表", role, "写")
						|| !AccessControl.isPermited("Register表", role, "写")) {
					net.sendString("无权限");
					return;
				} else {
					net.sendString("有权限");
					ConnectToClient.insertTeacher(net);//建立网络传输，回应客户端发起的请求
				}
				System.out.println("任务结束");
			}
			if (str.equals("insertStudent")) {//插入学生
				if (!AccessControl.isPermited("Student表", role, "写")
						|| !AccessControl.isPermited("Register表", role, "写")) {
					net.sendString("无权限");
					return;
				} else {
					net.sendString("有权限");
					ConnectToClient.insertStudent(net);//建立网络传输，回应客户端发起的请求
				}
				System.out.println("任务结束");
			}
			if (str.equals("setCode")) {//修改密码
				if (!AccessControl.isPermited("Register表", role, "写")) {
					net.sendString("无权限");
					return;
				} else {
					net.sendString("有权限");
					ConnectToClient.setCode(net);//建立网络传输，回应客户端发起的请求
				}
				System.out.println("任务结束");
			}
			if (str.equals("PasswordIsCorrect")) {//判断旧密码是否正确
				if (!AccessControl.isPermited("Register表", role, "读")) {
					net.sendString("无权限");
					return;
				} else {
					net.sendString("有权限");
					ConnectToClient.PasswordIsCorrect(net, role);//建立网络传输，回应客户端发起的请求
				}
				System.out.println("任务结束");
			}
		}

		public boolean findUser() {//查找用户
			role = net.recvString();//接收角色
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
