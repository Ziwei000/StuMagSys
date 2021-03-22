import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;

import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;


public class Welcome extends JFrame {

	private String role;

	static FontUIResource font = new FontUIResource("宋体", Font.BOLD, 25);

	public static Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

	JTextField username = null;
	JTextField password = null;
	JTextField vertifycode = null;
	JButton login = null;
	JButton back = null;

	private JRadioButton student;
	private JRadioButton teacher;
	private JRadioButton admin;
	private JRadioButton acAdmin;

	private Imagecode imagecode;

	Image offScreenImage = null;
	private MyPanel p;

	Welcome(String s) {
		super(s);
		imagecode = new Imagecode();
		p = new MyPanel(prepareImage());
		p.setPreferredSize(new Dimension(70,40));
	}

	public void launchFrame() {

		// 按钮的定义及添加监听器
		login = new JButton("登录");
		back = new JButton("退出");

		login.addActionListener(new Login());
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		username = new JTextField();
		password = new JPasswordField();
		vertifycode = new JTextField();

		username.setPreferredSize(new Dimension(200, 40));
		password.setPreferredSize(new Dimension(200, 40));
		vertifycode.setPreferredSize(new Dimension(120, 40));

		// 利用布局管理器来控制显示页面的布局
		Box box = Box.createVerticalBox();

		Box box1 = Box.createHorizontalBox();
		box1.add(new JLabel("  		"));
		JLabel label = new JLabel("用户名：");
		box1.add(label);
		box1.add(username);
		box1.add(new JLabel("  		"));

		Box box2 = Box.createHorizontalBox();
		box2.add(new JLabel("  		"));
		label = new JLabel("  密码：");
		box2.add(label);
		box2.add(password);
		box2.add(new JLabel(" 		 "));

		JLabel b = new JLabel("换一张");
		b.setFont(new Font("宋体", Font.BOLD, 10));
		b.setPreferredSize(new Dimension(40, 40));
		b.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				closeWindow();
				new Welcome("欢迎界面").launchFrame();
			}

		});
		
		Box box3 = Box.createHorizontalBox();
		box3.add(new JLabel(" 		 "));
		label = new JLabel("验证码：");
		box3.add(label);
		box3.add(vertifycode);
		box3.add(p);
		box3.add(b);

		student = new JRadioButton("学生");
		teacher = new JRadioButton("教师");
		admin = new JRadioButton("管理员");
		acAdmin = new JRadioButton("权限管理员");
		ButtonGroup bg = new ButtonGroup();
		bg.add(student);
		bg.add(teacher);
		bg.add(admin);
		bg.add(acAdmin);
		student.setSelected(true);// 默认选学生
		Box box4 = Box.createHorizontalBox();
		box4.add(student);
		box4.add(teacher);
		box4.add(admin);
		box4.add(acAdmin);
		
		Box box5 = Box.createHorizontalBox();
		box5.add(new JLabel("   		 "));
		box5.add(login);
		box5.add(new JLabel("    		   "));
		box5.add(back);
		box5.add(new JLabel("  		  "));

		box.add(new JLabel(" "));
		box.add(new JLabel(" "));
		box.add(box1);
		box.add(new JLabel(" "));
		box.add(box2);
		box.add(new JLabel(" "));
		box.add(box3);
		box.add(new JLabel(" "));
		box.add(box4);
		box.add(new JLabel(" "));
		box.add(box5);
		box.add(new JLabel(" "));
		box.setAlignmentY(LEFT_ALIGNMENT);

		this.add(box);
		this.pack();

		// 设置大小和位置，默认关闭选择，可见，不可更改大小

		this.setLocation((screen.width - this.getWidth()) / 2, (screen.height - this.getHeight()) / 2);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
	}
	//添加验证码图片到界面
	private Image prepareImage() {
		imagecode.vertifycode = new String("");
		BufferedImage img = imagecode.getImage();
		try {
			imagecode.saveImage(img, new FileOutputStream("a.jpg"));//从文件中读取验证码
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}

	public static void main(String[] args) {//主函数
		Welcome.setUIFont(font);
		new Welcome("欢迎界面").launchFrame();
	}

	public static void setUIFont(javax.swing.plaf.FontUIResource f) {//设置窗口的字体
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof javax.swing.plaf.FontUIResource) {
				UIManager.put(key, f);
			}
		}
	}

	public void closeWindow() {
		this.setVisible(false);
	}
 //对登录按钮进行监听
	private class Login implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			String vf = vertifycode.getText();//验证码输入框
			if (!Imagecode.Vertify(vf)) {//若验证码有误
				JOptionPane.showMessageDialog(null, "验证码不正确！", "错误", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (username.getText().equals("") || password.getText().equals("")) {//密码或用户名为空
				JOptionPane.showMessageDialog(null, "用户名密码不能为空！", "提示", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			if (student.isSelected()) {//若选择角色为学生
				Net net = new Net();
				if (ConnectToServer.findUser(net, username.getText(), password.getText(), "Student")) {
					role = "Student";
					StudentWindow sw = new StudentWindow(net);
					sw.init(username.getText(), role);
					sw.start();
				} else {
					System.out.println("没找到");
					JOptionPane.showMessageDialog(null, "该用户不存在！", "提示", JOptionPane.INFORMATION_MESSAGE);
				}
			} else if (teacher.isSelected()) {//选择教师角色
				Net net = new Net();
				if (ConnectToServer.findUser(net, username.getText(), password.getText(), "Teacher")) {
					role = "Teacher";
					TeacherWindow tw = new TeacherWindow(net);
					tw.init(username.getText(), role);
					tw.start();
				} else {
					System.out.println("没找到");
					JOptionPane.showMessageDialog(null, "该用户不存在！", "提示", JOptionPane.INFORMATION_MESSAGE);
				}
			} else if (admin.isSelected()) {//选择管理员角色
				Net net = new Net();
				if (ConnectToServer.findUser(net, username.getText(), password.getText(), "Administrator")) {
					role = "Administrator";
					AdministratorWindow aw = new AdministratorWindow(net);
					aw.init(username.getText(), role);
					aw.start();
				} else {
					System.out.println("没找到");
					JOptionPane.showMessageDialog(null, "该用户不存在！", "提示", JOptionPane.INFORMATION_MESSAGE);
				}
			} else if (acAdmin.isSelected()) {//选择权限管理员角色
				Net net = new Net();
				if (ConnectToServer.findUser(net, username.getText(), password.getText(), "AccessAdmin")) {
					role = "AccessAdmin";
					AccessAdminWindow aaw = new AccessAdminWindow(net);
					aaw.init(username.getText(), role);
					aaw.start();
				} else {
					System.out.println("没找到");
					JOptionPane.showMessageDialog(null, "该用户不存在！", "提示", JOptionPane.INFORMATION_MESSAGE);
				}
			}

			//closeWindow();
		}}
    //添加验证码
	private class MyPanel extends JPanel {

//		Toolkit tk = Toolkit.getDefaultToolkit();
//		Image img = tk.createImage("F:/java study/StuMagClient/a.jpg");
		Image img = null;

		public MyPanel(Image img) {
			super();
			this.img = img;
			this.setBounds(0, 0, img.getWidth(null), img.getHeight(null));
		}

		public void paint(Graphics g) {
			g.drawImage(img, 0, 0, img.getWidth(null), img.getHeight(null), null);
		}
       //添加加验证码图片到窗口
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(img, 0, 0, img.getWidth(null), img.getHeight(null), null);
		}

	}
	}
