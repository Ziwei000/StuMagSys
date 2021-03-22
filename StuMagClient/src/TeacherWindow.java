import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class TeacherWindow extends JFrame {	
	
	String username = null;
	String role = null;
	
	JButton information = new JButton("�ҵ���Ϣ");
	JButton course = new JButton("����ѡ��");
	JButton changeCode = new JButton("�޸�����");
	JButton back = new JButton("�˳���¼");
	JTextArea show = new JTextArea(15, 25);
	public Net net;

	
	public TeacherWindow(Net net) {
		super("��ʦ����");
		this.net = net;
	}
	
	public void init(String id, String r) {
		username = id;
		role = r;
	}
	//��ʦ����
	public void start()  {
		
		
		JPanel content = (JPanel)this.getContentPane();
		information.addActionListener(new InfoListener());
		course.addActionListener(new CourseListener());
		changeCode.addActionListener(new ChangeListener());
		back.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {//�˳���¼
				close();
			}		
		});
		Box box = Box.createHorizontalBox();
		box.add(information);
		box.add(course);
		box.add(changeCode);
		box.add(back);
		
		String initialInformation = "������·�����ָ��";
		show.setText(initialInformation);
		
		Box box2 = Box.createVerticalBox();
		box2.add(new JLabel("��Ϣ����"));
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
	
	private class InfoListener implements ActionListener {//��ʦ��Ϣ

		public void actionPerformed(ActionEvent arg0) {
			new InfoWindow();
		}
		
	}
	
	private class CourseListener implements ActionListener {//���ڿγ�

		public void actionPerformed(ActionEvent arg0) {
			
			CourseSystem cs = new CourseSystem(net, username);
			cs.showTCourseWindow();//����coursesystem.class�еĽ�ʦ�γ̽���
		}
		
	}
	
	private class ChangeListener implements ActionListener {//��������

		public void actionPerformed(ActionEvent arg0) {
			new	changeWindow();
		}
		
	}
	//�鿴������Ϣ
	private class InfoWindow extends JFrame {
		
		InfoWindow() {
			super("�ҵ���Ϣ");
			StringBuffer name = new StringBuffer();
			StringBuffer sex = new StringBuffer();
			StringBuffer id = new StringBuffer();
			StringBuffer school = new StringBuffer();
			StringBuffer dept = new StringBuffer();
			StringBuffer tel = new StringBuffer();
			StringBuffer pos = new StringBuffer();
	       //�����������鿴��ʦ��Ϣ
			ConnectToServer.findTeacher(net, username, name, sex, id, school, dept, tel, pos);
			
			Box box = Box.createVerticalBox();
			box.add(new Label("��ʦ��:" + username));
			box.add(new Label("����:" + name));
			box.add(new Label("�Ա�:" + sex));
			box.add(new Label("���֤��:"+id));
			box.add(new Label("����ѧԺ:"+school));
			box.add(new Label("����ϵ:"+dept));
			box.add(new Label("ְ��:"+tel));
			box.add(new Label("�绰:"+pos));
		
			this.add(box);
			this.pack();
			this.setLocation((Welcome.screen.width - this.getWidth())/2, (Welcome.screen.height - this.getHeight())/2);
			this.setVisible(true);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}

	}
	//�޸�����
	private class changeWindow extends JFrame {
		
		JPasswordField oldCode = new JPasswordField();
		JPasswordField newCode = new JPasswordField();
		JPasswordField confirm = new JPasswordField();
		JButton ok = new JButton("ȷ��");
		JButton back = new JButton("����");
		
		changeWindow() {
				super("�޸�����");
				
				Box box = Box.createVerticalBox();
				box.add(new Label("������:"));
				box.add(oldCode);
				box.add(new Label("������:"));
				box.add(newCode);
				box.add(new Label("ȷ��������:"));
				box.add(confirm);
				
				back.addActionListener(new ActionListener() {//�˳�
					public void actionPerformed(ActionEvent arg0) {
						close();
					}		
				});
				ok.addActionListener(new ActionListener() {//ȷ��
					public void actionPerformed(ActionEvent arg0) {
						changeCodeResponse();//ִ�и����������
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
        //�޸�������ʾ��Ϣ
		private void changeCodeResponse() {
			String tmp = null;
			if(PasswordIsCorrect()) {
				if(new String(confirm.getPassword()).equals(new String(newCode.getPassword()))) {
					if(newCode.getPassword().length < 6) {
						tmp = "���볤�Ȳ���С��6��";
						JOptionPane.showMessageDialog(null, tmp, "����", JOptionPane.ERROR_MESSAGE);	
						return;
					}
					setCode(username, new String(newCode.getPassword()));
					close();
				//	tmp = "�޸�����ɹ���";			
					//JOptionPane.showMessageDialog(null, tmp, "��ʾ", JOptionPane.INFORMATION_MESSAGE);	
				} else {
					tmp = "�������벻һ�£�";
					JOptionPane.showMessageDialog(null, tmp, "����", JOptionPane.ERROR_MESSAGE);			
				}
			} else {
				tmp = "���������벻��ȷ��";
				JOptionPane.showMessageDialog(null, tmp, "����", JOptionPane.ERROR_MESSAGE);	
			}
		}
        //�����޸�����
		private void setCode(String username, String string) {
			ConnectToServer.setCode(net, username, string);
		}
        //�жϾ���������
		private boolean PasswordIsCorrect() {
			return ConnectToServer.PasswordIsCorrect(net, username, new String(oldCode.getPassword()));
		}	
	}
	
	
	
}
