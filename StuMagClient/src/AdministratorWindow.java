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
//����Ա����
class AdministratorWindow extends JFrame {

	JButton register = null;
	private String username;
	private String role;
	public Net net;
//����Ա����
	public AdministratorWindow(Net net) {
		super("����Ա����");
		this.net = net;//����һ��net����������紫��
	}

	public void init(String id, String r) {//��ȡ������Ϣ
		username = id;
		role = r;
	}
   //��ʼ
	public void start() {
		register = new JButton("ע���³�Ա");//ע��
		this.add(register);//
		register.addActionListener(new Register());//��������
		this.add(register);//ִ�е�½
		this.pack();
		this.setLocation((Welcome.screen.width - this.getWidth()) / 2, (Welcome.screen.height - this.getHeight()) / 2);
		this.setVisible(true);
	}
     //ע�����
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
			super("ע��ҳ��");
			identity = s;
			p = new JPanel();
			ok = new JButton("ȷ��");
			back = new JButton("����");
			ok.addActionListener(new ActionListener() {//ȷ������
				public void actionPerformed(ActionEvent arg0) {
					registerResponse();//����ע��Ĺ��ܺ���
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
			p.add(new JLabel("������"));
			p.add(name);
			p.add(new JLabel("�Ա�"));
			male = new JRadioButton("��");
			female = new JRadioButton("Ů");
			bg = new ButtonGroup();
			bg.add(male);
			bg.add(female);
			male.setSelected(true);// Ĭ��ѡ��
			p.add(male);
			p.add(female);
			p.add(new JLabel("���룺"));
			p.add(password);
			p.add(new JLabel("���֤�ţ�"));
			p.add(id);
			p.add(new JLabel("�绰��"));
			p.add(tel);
			if (s.equals("Teacher")) {
				p.add(new JLabel("ְ�ƣ�"));
				p.add(pos);
			} else if (s.equals("Student")) {
				classname = new JTextField();
				p.add(new JLabel("���ڰ༶��"));
				p.add(classname);
				p.add(new JLabel("����ʲô�ɲ���"));
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
         //�ж�ע����Ϣʱ�����ע��Ҫ��
		protected void registerResponse() {
			String tmp = null;
			if (password.getPassword().length < 6) {
				tmp = "���볤�Ȳ���С��6";
				JOptionPane.showMessageDialog(null, tmp, "��ʾ", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (name.getText().equals("")) {
				tmp = "��������Ϊ��";
				JOptionPane.showMessageDialog(null, tmp, "��ʾ", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (id.getText().equals("")) {
				tmp = "���֤�Ų���Ϊ��";
				JOptionPane.showMessageDialog(null, tmp, "��ʾ", JOptionPane.ERROR_MESSAGE);
				return;
			}

			this.setVisible(false);//��������Ϊ���ɼ�
			if (male.isSelected())
				sex = new String("��");
			else
				sex = new String("Ů");
			int user = 0;
			if (identity.equals("Teacher")) {
				user = insertTeacher(name.getText(), sex, new String(password.getPassword()), id.getText(),
						tel.getText(), pos.getText(), "1");
			} else {
				user = insertStudent(name.getText(), sex, new String(password.getPassword()), id.getText(),
						tel.getText(), pos.getText(), classname.getText());
			}
			tmp = "���ѳɹ�ע�ᣡ" + "���ס����ID�ţ����û������� " + user;
			JOptionPane.showMessageDialog(null, tmp, "��ʾ", JOptionPane.INFORMATION_MESSAGE);
		}
     //��������������ѧ��
		private int insertStudent(String name, String sex, String password, String id, String tel, String pos,
				String classname) {
			int i = ConnectToServer.insertStudent(net, name, sex, password, id, tel, pos, classname);
			return i;
		}
		//���������������ʦ
		private int insertTeacher(String name, String sex, String password, String id, String tel, String pos,
				String dept) {
			int i = ConnectToServer.insertTeacher(net, name, sex, password, id, tel, pos, dept);
			return i;
		}
	}

	private class Register extends JFrame implements ActionListener {

		Register() {
			super("ѡ��ҳ��");
			this.setLayout(new FlowLayout());
			this.setLocation((Welcome.screen.width - this.getWidth()) / 2,
					(Welcome.screen.height - this.getHeight()) / 2);
			JButton teacher = new JButton("��ʦ");
			JButton student = new JButton("ѧ��");
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
