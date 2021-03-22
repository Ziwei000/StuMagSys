import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
//管理员界面
class AdministratorWindow extends JFrame {

	JButton register = null;
	private String username;
	private String role;
	public Net net;
//管理员窗口
	public AdministratorWindow(Net net) {
		super("管理员界面");
		this.net = net;//建立一个net对象进行网络传输
	}

	public void init(String id, String r) {//获取对象信息
		username = id;
		role = r;
	}
   //开始
	public void start() {
		register = new JButton("注册新成员");//注册
		this.add(register);//
		register.addActionListener(new Register());//建立监听
		this.add(register);//执行登陆
		this.pack();
		this.setLocation((Welcome.screen.width - this.getWidth()) / 2, (Welcome.screen.height - this.getHeight()) / 2);
		this.setVisible(true);
	}
     //注册界面
	private class RegisterFrame extends Frame {

		String identity = null;
		JButton ok = null;
		JButton back = null;
		private JPanel p = null;
		private JTextField name = null;
		private JPasswordField password = null;
		private JTextField id = null;
		private JTextField tel = null;
		private JTextField pos = null;
		ButtonGroup bg;
		JRadioButton male = null;
		JRadioButton female = null;
		String sex = null;
		private JTextField classname;

		RegisterFrame(String s) {
			super("注册页面");
			identity = s;
			p = new JPanel();
			ok = new JButton("确定");
			back = new JButton("返回");
			ok.addActionListener(new ActionListener() {//确定监听
				public void actionPerformed(ActionEvent arg0) {
					registerResponse();//调用注册的功能函数
				}
			});
			back.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					backResponse();
				}
			});
			name = new JTextField();
			password = new JPasswordField();
			id = new JTextField();
			tel = new JTextField();
			pos = new JTextField();
			BoxLayout boxLayout = new BoxLayout(p, BoxLayout.Y_AXIS);
			p.setLayout(boxLayout);
			p.add(new JLabel("姓名："));
			p.add(name);
			p.add(new JLabel("性别："));
			male = new JRadioButton("男");
			female = new JRadioButton("女");
			bg = new ButtonGroup();
			bg.add(male);
			bg.add(female);
			male.setSelected(true);// 默认选男
			p.add(male);
			p.add(female);
			p.add(new JLabel("密码："));
			p.add(password);
			p.add(new JLabel("身份证号："));
			p.add(id);
			p.add(new JLabel("电话："));
			p.add(tel);
			if (s.equals("Teacher")) {
				p.add(new JLabel("职称："));
				p.add(pos);
			} else if (s.equals("Student")) {
				classname = new JTextField();
				p.add(new JLabel("所在班级："));
				p.add(classname);
				p.add(new JLabel("担任什么干部："));
				p.add(pos);
			}
			Box box = Box.createVerticalBox();
			Box box2 = Box.createHorizontalBox();
			box2.add(ok);
			box2.add(back);
			box.add(p);
			box.add(box2);
			this.add(box);
			this.pack();
			this.setLocation((Welcome.screen.width - this.getWidth()) / 2,
					(Welcome.screen.height - this.getHeight()) / 2);
			this.setResizable(false);
			this.setVisible(true);

			this.addWindowListener(new WindowAdapter() {

				public void windowClosing(WindowEvent arg0) {
					close();
				}

			});
		}

		protected void close() {
			this.setVisible(false);
		}

		protected void backResponse() {
			this.setVisible(false);
		}
         //判断注册信息时候符合注册要求
		protected void registerResponse() {
			String tmp = null;
			if (password.getPassword().length < 6) {
				tmp = "密码长度不能小于6";
				JOptionPane.showMessageDialog(null, tmp, "提示", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (name.getText().equals("")) {
				tmp = "姓名不能为空";
				JOptionPane.showMessageDialog(null, tmp, "提示", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (id.getText().equals("")) {
				tmp = "身份证号不能为空";
				JOptionPane.showMessageDialog(null, tmp, "提示", JOptionPane.ERROR_MESSAGE);
				return;
			}

			this.setVisible(false);//窗口设置为不可见
			if (male.isSelected())
				sex = new String("男");
			else
				sex = new String("女");
			int user = 0;
			if (identity.equals("Teacher")) {
				user = insertTeacher(name.getText(), sex, new String(password.getPassword()), id.getText(),
						tel.getText(), pos.getText(), "1");
			} else {
				user = insertStudent(name.getText(), sex, new String(password.getPassword()), id.getText(),
						tel.getText(), pos.getText(), classname.getText());
			}
			tmp = "您已成功注册！" + "请记住您的ID号（即用户名）： " + user;
			JOptionPane.showMessageDialog(null, tmp, "提示", JOptionPane.INFORMATION_MESSAGE);
		}
     //向服务器请求插入学生
		private int insertStudent(String name, String sex, String password, String id, String tel, String pos,
				String classname) {
			int i = ConnectToServer.insertStudent(net, name, sex, password, id, tel, pos, classname);
			return i;
		}
		//向服务器请求插入教师
		private int insertTeacher(String name, String sex, String password, String id, String tel, String pos,
				String dept) {
			int i = ConnectToServer.insertTeacher(net, name, sex, password, id, tel, pos, dept);
			return i;
		}
	}

	private class Register extends JFrame implements ActionListener {

		Register() {
			super("选择页面");
			this.setLayout(new FlowLayout());
			this.setLocation((Welcome.screen.width - this.getWidth()) / 2,
					(Welcome.screen.height - this.getHeight()) / 2);
			JButton teacher = new JButton("教师");
			JButton student = new JButton("学生");
			this.add(teacher);
			this.add(student);
			this.pack();
			this.setLocation((Welcome.screen.width - this.getWidth()) / 2,
					(Welcome.screen.height - this.getHeight()) / 2);
			teacher.addActionListener(new TeacherRegister());
			student.addActionListener(new StudentRegister());
		}

		public void actionPerformed(ActionEvent arg0) {
			this.setVisible(true);
		}

		public void close() {
			this.setVisible(false);
		}

		private class TeacherRegister implements ActionListener {

			public void actionPerformed(ActionEvent arg0) {
				JDialog registerDialog = new JDialog(new RegisterFrame("Teacher"), true);
				registerDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				close();
			}

		}
       
		private class StudentRegister implements ActionListener {

			public void actionPerformed(ActionEvent arg0) {
				JDialog registerDialog = new JDialog(new RegisterFrame("Student"), true);
				registerDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				close();
			}

		}
	}

}
