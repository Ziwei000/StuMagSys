import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
//����Ա����
public class AccessAdminWindow extends JFrame {

	private String role;
	private String username = null;
	
	JButton check = null;
	JButton change = null;
	JMenuBar jmb = null;
	private JMenu roleName;
	private JMenu tableName;
	private JTextField  tf = null;

	String tn = null;
	String r = null;

	private Net net;
	
	public AccessAdminWindow(Net net) {
		super("Ȩ�޹���Ա����");
		this.net = net;
	}

	public void init(String id, String r) {
		username = id;
		role = r;
	}
//��ʼ
	public void start() {
		jmb = new JMenuBar();
		roleName = new JMenu("��ѡ���ɫ��");
		tableName = new JMenu("��ѡ�����");
		check = new JButton("�鿴Ȩ��");
		change = new JButton("�޸�Ȩ��");
		tf = new JTextField();
		initRole();
		initTableName();
     //��������
		check.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(r == null) 	{
					JOptionPane.showMessageDialog(null, "����ѡ���ɫ��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if(tn == null) 	{
					JOptionPane.showMessageDialog(null, "����ѡ�����", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				String s = ConnectToServer.findAccess(net, tn, r);
				tf.setText(s);
			}	
		});
		//�����޸ļ���
		change.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(r == null) 	{
					JOptionPane.showMessageDialog(null, "����ѡ���ɫ��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if(tn == null) 	{
					JOptionPane.showMessageDialog(null, "����ѡ�����", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				String s = tf.getText();
				if(s != null) ConnectToServer.changeAccess(net, tn, r, s);
				System.out.println(s);
			}	
		});
		
		Box box = Box.createVerticalBox();
		roleName.setPreferredSize(new Dimension(350, 50));
		tableName.setPreferredSize(new Dimension(350, 50));
		jmb.add(roleName);
		jmb.add(tableName);
		box.add(jmb);
		
		box.add(tf);
		
		Box box2 = Box.createHorizontalBox();
		box2.add(check);
		box2.add(change);
		box.add(box2);
		
		this.add(box);
		this.pack();
		this.setLocation((Welcome.screen.width - this.getWidth()) / 2, (Welcome.screen.height - this.getHeight()) / 2);
		this.setVisible(true);
	}
    //��ʼ������
	private void initTableName() {
		JMenuItem Register = new JMenuItem("Register��");//��ӿؼ�
		Register.addActionListener(new TableListener());
		JMenuItem ACL = new JMenuItem("ACL��");
		ACL.addActionListener(new TableListener());
		JMenuItem School = new JMenuItem("School��");
		School.addActionListener(new TableListener());
		JMenuItem Dept = new JMenuItem("Dept��");
		Dept.addActionListener(new TableListener());
		JMenuItem Major = new JMenuItem("Major��");
		Major.addActionListener(new TableListener());
		JMenuItem Course = new JMenuItem("Course��");
		Course.addActionListener(new TableListener());
		JMenuItem Student = new JMenuItem("Student��");
		Student.addActionListener(new TableListener());
		JMenuItem Teacher = new JMenuItem("Teacher��");
		Teacher.addActionListener(new TableListener());
		JMenuItem Student_Course = new JMenuItem("Student_Course��");
		Student_Course.addActionListener(new TableListener());
		JMenuItem Teacher_Course = new JMenuItem("Teacher_Course��");
		Teacher_Course.addActionListener(new TableListener());
		tableName.add(Register);
		tableName.add(ACL);
		tableName.add(School);
		tableName.add(Dept);
		tableName.add(Major);
		tableName.add(Course);
		tableName.add(Student);
		tableName.add(Teacher);
		tableName.add(Student_Course);
		tableName.add(Teacher_Course);
	}
     //��ʼ����ɫ��
	private void initRole() {
		JMenuItem AccessAdmin = new JMenuItem("AccessAdmin");
		AccessAdmin.addActionListener(new RoleListener());
		JMenuItem Administrator = new JMenuItem("Administrator");
		Administrator.addActionListener(new RoleListener());
		JMenuItem Student = new JMenuItem("Student");
		Student.addActionListener(new RoleListener());
		JMenuItem Teacher = new JMenuItem("Teacher");
		Teacher.addActionListener(new RoleListener());
		
		roleName.add(AccessAdmin);
		roleName.add(Administrator);
		roleName.add(Student);
		roleName.add(Teacher);
	}
    //��ɫ����
	private class RoleListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			JMenuItem ji = (JMenuItem) e.getSource();
			r = ji.getText();
			roleName.setText("��ɫ��"  + ": "+ r);
			System.out.println("role = " + r);
		}

	}
	//�����
	private class TableListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			JMenuItem ji = (JMenuItem) e.getSource();
			tn = ji.getText();
			tableName.setText("����" + ": " + tn);
			System.out.println("tableName = " + tn);
		}

	}
}
