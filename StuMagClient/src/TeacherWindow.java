import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class TeacherWindow extends JFrame {	
	
	String username = null;
	String role = null;
	
	JButton information = new JButton("我的信息");
	JButton course = new JButton("江大选课");
	JButton changeCode = new JButton("修改密码");
	JButton back = new JButton("退出登录");
	JTextArea show = new JTextArea(15, 25);
	public Net net;

	
	public TeacherWindow(Net net) {
		super("教师界面");
		this.net = net;
	}
	
	public void init(String id, String r) {
		username = id;
		role = r;
	}
	//教师界面
	public void start()  {
		
		
		JPanel content = (JPanel)this.getContentPane();
		information.addActionListener(new InfoListener());
		course.addActionListener(new CourseListener());
		changeCode.addActionListener(new ChangeListener());
		back.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {//退出登录
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
	
	private class InfoListener implements ActionListener {//教师信息

		public void actionPerformed(ActionEvent arg0) {
			new InfoWindow();
		}
		
	}
	
	private class CourseListener implements ActionListener {//所授课程

		public void actionPerformed(ActionEvent arg0) {
			
			CourseSystem cs = new CourseSystem(net, username);
			cs.showTCourseWindow();//调用coursesystem.class中的教师课程界面
		}
		
	}
	
	private class ChangeListener implements ActionListener {//更改密码

		public void actionPerformed(ActionEvent arg0) {
			new	changeWindow();
		}
		
	}
	//查看个人信息
	private class InfoWindow extends JFrame {
		
		InfoWindow() {
			super("我的信息");
			StringBuffer name = new StringBuffer();
			StringBuffer sex = new StringBuffer();
			StringBuffer id = new StringBuffer();
			StringBuffer school = new StringBuffer();
			StringBuffer dept = new StringBuffer();
			StringBuffer tel = new StringBuffer();
			StringBuffer pos = new StringBuffer();
	       //向服务器请求查看教师信息
			ConnectToServer.findTeacher(net, username, name, sex, id, school, dept, tel, pos);
			
			Box box = Box.createVerticalBox();
			box.add(new Label("教师号:" + username));
			box.add(new Label("姓名:" + name));
			box.add(new Label("性别:" + sex));
			box.add(new Label("身份证号:"+id));
			box.add(new Label("所在学院:"+school));
			box.add(new Label("所在系:"+dept));
			box.add(new Label("职称:"+tel));
			box.add(new Label("电话:"+pos));
		
			this.add(box);
			this.pack();
			this.setLocation((Welcome.screen.width - this.getWidth())/2, (Welcome.screen.height - this.getHeight())/2);
			this.setVisible(true);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}

	}
	//修改密码
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
				
				back.addActionListener(new ActionListener() {//退出
					public void actionPerformed(ActionEvent arg0) {
						close();
					}		
				});
				ok.addActionListener(new ActionListener() {//确定
					public void actionPerformed(ActionEvent arg0) {
						changeCodeResponse();//执行更改密码操作
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
        //修改密码提示信息
		private void changeCodeResponse() {
			String tmp = null;
			if(PasswordIsCorrect()) {
				if(new String(confirm.getPassword()).equals(new String(newCode.getPassword()))) {
					if(newCode.getPassword().length < 6) {
						tmp = "密码长度不能小于6！";
						JOptionPane.showMessageDialog(null, tmp, "错误", JOptionPane.ERROR_MESSAGE);	
						return;
					}
					setCode(username, new String(newCode.getPassword()));
					close();
				//	tmp = "修改密码成功！";			
					//JOptionPane.showMessageDialog(null, tmp, "提示", JOptionPane.INFORMATION_MESSAGE);	
				} else {
					tmp = "两次输入不一致！";
					JOptionPane.showMessageDialog(null, tmp, "错误", JOptionPane.ERROR_MESSAGE);			
				}
			} else {
				tmp = "旧密码输入不正确！";
				JOptionPane.showMessageDialog(null, tmp, "错误", JOptionPane.ERROR_MESSAGE);	
			}
		}
        //请求修改密码
		private void setCode(String username, String string) {
			ConnectToServer.setCode(net, username, string);
		}
        //判断旧密码正误
		private boolean PasswordIsCorrect() {
			return ConnectToServer.PasswordIsCorrect(net, username, new String(oldCode.getPassword()));
		}	
	}
	
	
	
}
