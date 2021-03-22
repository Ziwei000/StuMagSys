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
//管理员界面
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
		super("权限管理员界面");
		this.net = net;
	}

	public void init(String id, String r) {
		username = id;
		role = r;
	}
//开始
	public void start() {
		jmb = new JMenuBar();
		roleName = new JMenu("请选择角色名");
		tableName = new JMenu("请选择表名");
		check = new JButton("查看权限");
		change = new JButton("修改权限");
		tf = new JTextField();
		initRole();
		initTableName();
     //建立监听
		check.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(r == null) 	{
					JOptionPane.showMessageDialog(null, "请先选择角色名", "提示", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if(tn == null) 	{
					JOptionPane.showMessageDialog(null, "请先选择表名", "提示", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				String s = ConnectToServer.findAccess(net, tn, r);
				tf.setText(s);
			}	
		});
		//建立修改监听
		change.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(r == null) 	{
					JOptionPane.showMessageDialog(null, "请先选择角色名", "提示", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if(tn == null) 	{
					JOptionPane.showMessageDialog(null, "请先选择表名", "提示", JOptionPane.INFORMATION_MESSAGE);
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
    //初始化表名
	private void initTableName() {
		JMenuItem Register = new JMenuItem("Register表");//添加控件
		Register.addActionListener(new TableListener());
		JMenuItem ACL = new JMenuItem("ACL表");
		ACL.addActionListener(new TableListener());
		JMenuItem School = new JMenuItem("School表");
		School.addActionListener(new TableListener());
		JMenuItem Dept = new JMenuItem("Dept表");
		Dept.addActionListener(new TableListener());
		JMenuItem Major = new JMenuItem("Major表");
		Major.addActionListener(new TableListener());
		JMenuItem Course = new JMenuItem("Course表");
		Course.addActionListener(new TableListener());
		JMenuItem Student = new JMenuItem("Student表");
		Student.addActionListener(new TableListener());
		JMenuItem Teacher = new JMenuItem("Teacher表");
		Teacher.addActionListener(new TableListener());
		JMenuItem Student_Course = new JMenuItem("Student_Course表");
		Student_Course.addActionListener(new TableListener());
		JMenuItem Teacher_Course = new JMenuItem("Teacher_Course表");
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
     //初始化角色名
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
    //角色监听
	private class RoleListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			JMenuItem ji = (JMenuItem) e.getSource();
			r = ji.getText();
			roleName.setText("角色名"  + ": "+ r);
			System.out.println("role = " + r);
		}

	}
	//表监听
	private class TableListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			JMenuItem ji = (JMenuItem) e.getSource();
			tn = ji.getText();
			tableName.setText("表名" + ": " + tn);
			System.out.println("tableName = " + tn);
		}

	}
}
