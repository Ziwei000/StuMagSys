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

	static FontUIResource font = new FontUIResource("����", Font.BOLD, 25);

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

		// ��ť�Ķ��弰��Ӽ�����
		login = new JButton("��¼");
		back = new JButton("�˳�");

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

		// ���ò��ֹ�������������ʾҳ��Ĳ���
		Box box = Box.createVerticalBox();

		Box box1 = Box.createHorizontalBox();
		box1.add(new JLabel("  		"));
		JLabel label = new JLabel("�û�����");
		box1.add(label);
		box1.add(username);
		box1.add(new JLabel("  		"));

		Box box2 = Box.createHorizontalBox();
		box2.add(new JLabel("  		"));
		label = new JLabel("  ���룺");
		box2.add(label);
		box2.add(password);
		box2.add(new JLabel(" 		 "));

		JLabel b = new JLabel("��һ��");
		b.setFont(new Font("����", Font.BOLD, 10));
		b.setPreferredSize(new Dimension(40, 40));
		b.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				closeWindow();
				new Welcome("��ӭ����").launchFrame();
			}

		});
		
		Box box3 = Box.createHorizontalBox();
		box3.add(new JLabel(" 		 "));
		label = new JLabel("��֤�룺");
		box3.add(label);
		box3.add(vertifycode);
		box3.add(p);
		box3.add(b);

		student = new JRadioButton("ѧ��");
		teacher = new JRadioButton("��ʦ");
		admin = new JRadioButton("����Ա");
		acAdmin = new JRadioButton("Ȩ�޹���Ա");
		ButtonGroup bg = new ButtonGroup();
		bg.add(student);
		bg.add(teacher);
		bg.add(admin);
		bg.add(acAdmin);
		student.setSelected(true);// Ĭ��ѡѧ��
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

		// ���ô�С��λ�ã�Ĭ�Ϲر�ѡ�񣬿ɼ������ɸ��Ĵ�С

		this.setLocation((screen.width - this.getWidth()) / 2, (screen.height - this.getHeight()) / 2);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
	}
	//�����֤��ͼƬ������
	private Image prepareImage() {
		imagecode.vertifycode = new String("");
		BufferedImage img = imagecode.getImage();
		try {
			imagecode.saveImage(img, new FileOutputStream("a.jpg"));//���ļ��ж�ȡ��֤��
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}

	public static void main(String[] args) {//������
		Welcome.setUIFont(font);
		new Welcome("��ӭ����").launchFrame();
	}

	public static void setUIFont(javax.swing.plaf.FontUIResource f) {//���ô��ڵ�����
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
 //�Ե�¼��ť���м���
	private class Login implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			String vf = vertifycode.getText();//��֤�������
			if (!Imagecode.Vertify(vf)) {//����֤������
				JOptionPane.showMessageDialog(null, "��֤�벻��ȷ��", "����", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (username.getText().equals("") || password.getText().equals("")) {//������û���Ϊ��
				JOptionPane.showMessageDialog(null, "�û������벻��Ϊ�գ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			if (student.isSelected()) {//��ѡ���ɫΪѧ��
				Net net = new Net();
				if (ConnectToServer.findUser(net, username.getText(), password.getText(), "Student")) {
					role = "Student";
					StudentWindow sw = new StudentWindow(net);
					sw.init(username.getText(), role);
					sw.start();
				} else {
					System.out.println("û�ҵ�");
					JOptionPane.showMessageDialog(null, "���û������ڣ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
				}
			} else if (teacher.isSelected()) {//ѡ���ʦ��ɫ
				Net net = new Net();
				if (ConnectToServer.findUser(net, username.getText(), password.getText(), "Teacher")) {
					role = "Teacher";
					TeacherWindow tw = new TeacherWindow(net);
					tw.init(username.getText(), role);
					tw.start();
				} else {
					System.out.println("û�ҵ�");
					JOptionPane.showMessageDialog(null, "���û������ڣ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
				}
			} else if (admin.isSelected()) {//ѡ�����Ա��ɫ
				Net net = new Net();
				if (ConnectToServer.findUser(net, username.getText(), password.getText(), "Administrator")) {
					role = "Administrator";
					AdministratorWindow aw = new AdministratorWindow(net);
					aw.init(username.getText(), role);
					aw.start();
				} else {
					System.out.println("û�ҵ�");
					JOptionPane.showMessageDialog(null, "���û������ڣ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
				}
			} else if (acAdmin.isSelected()) {//ѡ��Ȩ�޹���Ա��ɫ
				Net net = new Net();
				if (ConnectToServer.findUser(net, username.getText(), password.getText(), "AccessAdmin")) {
					role = "AccessAdmin";
					AccessAdminWindow aaw = new AccessAdminWindow(net);
					aaw.init(username.getText(), role);
					aaw.start();
				} else {
					System.out.println("û�ҵ�");
					JOptionPane.showMessageDialog(null, "���û������ڣ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
				}
			}

			//closeWindow();
		}}
    //�����֤��
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
       //��Ӽ���֤��ͼƬ������
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(img, 0, 0, img.getWidth(null), img.getHeight(null), null);
		}

	}
	}
