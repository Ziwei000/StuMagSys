import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class StudentWindow extends JFrame {
	
	String username = null;
	String role = null;
	
	JButton information = new JButton("我的信息");
	JButton course = new JButton("江大选课");
	JButton changeCode = new JButton("修改密码");
	JButton back = new JButton("退出登录");
	JTextArea show = new JTextArea(15, 25);
	public Net net;

	
	public StudentWindow(Net net) {
		super("学生界面");
		this.net = net;
		
//		ConnectToServer.insertCourseArrange(net, "200000001", "1", "信息安全1501班");
//		ConnectToServer.insertCourseArrange(net, "200000001", "2", "信息安全1501班");
//		ConnectToServer.insertCourseArrange(net, "200000001", "3", "信息安全1501班");
	}
	
	public void init(String id, String r) {
		username = id;
		role = r;
	}
	
	public void start()  {
		
		JPanel content = (JPanel)this.getContentPane();
		information.addActionListener(new InfoListener());
		course.addActionListener(new CourseListener());
		changeCode.addActionListener(new ChangeListener());
		back.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
				close();
			}		
		});
		Box box = Box.createHorizontalBox();
		box.add(information);
		box.add(course);
		box.add(changeCode);
		box.add(back);
		String initialInformation = "新手上路，请多指教";
		show.setText(initialInformation);
		
		Box box2 = Box.createVerticalBox();
		box2.add(new JLabel("信息公告"));
		box2.add(show);
		
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.add(box);
		content.add(box2);
	
		this.pack();
		this.setLocation((Welcome.screen.width - this.getWidth())/2, (Welcome.screen.height - this.getHeight())/2);
		this.setVisible(true);
	}
	
	private void close() {
		this.setVisible(false);
	}	
	//查看个人信息窗口
	private class InfoListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			new InfoWindow();
		}
		
	}
	//查看选课情况窗口
	private class CourseListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			CourseSystem cs = new CourseSystem(net, username);
			cs.showSCourseWindow();
		}
		
	}
	//更改密码窗口
	private class ChangeListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			new	changeWindow();
		}
		
	}
	//查看个人信息窗口
	private class InfoWindow extends JFrame {
		
		InfoWindow() {
			super("我的信息");
			StringBuffer name = new StringBuffer();
			StringBuffer sex = new StringBuffer();
			StringBuffer id = new StringBuffer();
			StringBuffer school = new StringBuffer();
			StringBuffer dept = new StringBuffer();
			StringBuffer major = new StringBuffer();
			StringBuffer classname = new StringBuffer();
			StringBuffer cyear = new StringBuffer();
			StringBuffer tel = new StringBuffer();
			StringBuffer pos = new StringBuffer();
	
			ConnectToServer.findStudent(net, username, name, sex, id, school, dept, major, classname, cyear, pos, tel);
			
			Box box = Box.createVerticalBox();
			box.add(new Label("学号:" + username));
			box.add(new Label("姓名:" + name));
			box.add(new Label("性别:" + sex));
			box.add(new Label("身份证号:"+id));
			box.add(new Label("所在学院:"+school));
			box.add(new Label("所在系:"+dept));
			box.add(new Label("所在专业:"+major));
			box.add(new Label("所在班级:"+classname));
			box.add(new Label("入学年份:"+cyear));
			box.add(new Label("担任干部:"+pos));
			box.add(new Label("电话:"+tel));
		
			this.add(box);
			this.pack();
			this.setLocation((Welcome.screen.width - this.getWidth())/2, (Welcome.screen.height - this.getHeight())/2);
			this.setVisible(true);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}

	}
	//更改密码窗口
	private class changeWindow extends JFrame {
		
		JPasswordField oldCode = new JPasswordField();
		JPasswordField newCode = new JPasswordField();
		JPasswordField confirm = new JPasswordField();
		JButton ok = new JButton("确定");
		JButton back = new JButton("返回");
		
		changeWindow() {
				super("修改密码");
				
				Box box = Box.createVerticalBox();
				box.add(new Label("旧密码:"));
				box.add(oldCode);
				box.add(new Label("新密码:"));
				box.add(newCode);
				box.add(new Label("确认新密码:"));
				box.add(confirm);
				
				back.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						close();
					}		
				});
				ok.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						changeCodeResponse();//执行修改密码的操作
					}
			
				});
				Box box2 = Box.createHorizontalBox();
				box2.add(ok);
				box2.add(back);
				box.add(box2);
			
				this.add(box);
				this.pack();
				this.setLocation((Welcome.screen.width - this.getWidth())/2, (Welcome.screen.height - this.getHeight())/2);
				this.setVisible(true);
				this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		
		private void close() {
			this.setVisible(false);
		}
		//更改密码
		private void changeCodeResponse() {
			String tmp = null;
			if(PasswordIsCorrect()) {//判断旧密码是否输入正确
				if(new String(confirm.getPassword()).equals(new String(newCode.getPassword()))) {//判断新密码和确认密码是否相同
					if(newCode.getPassword().length < 6) {//判断密码长度
						tmp = "密码长度不能小于6！";
						JOptionPane.showMessageDialog(null, tmp, "错误", JOptionPane.ERROR_MESSAGE);	
						return;
					}
					//向服务器请求更改密码
					setCode(username, new String(newCode.getPassword()));
					close();
				} else {
					tmp = "两次输入不一致！";
					JOptionPane.showMessageDialog(null, tmp, "错误", JOptionPane.ERROR_MESSAGE);			
				}
			} else {
				tmp = "旧密码输入不正确！";
				JOptionPane.showMessageDialog(null, tmp, "错误", JOptionPane.ERROR_MESSAGE);	
			}
		}
		//向服务器请求更改密码
		private void setCode(String username, String string) {
			ConnectToServer.setCode(net, username, string);
		}
		//判断旧密码是否输入正确
		private boolean PasswordIsCorrect() {
			return ConnectToServer.PasswordIsCorrect(net, username, new String(oldCode.getPassword()));
		}	

	}
}
