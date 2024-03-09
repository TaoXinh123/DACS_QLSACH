package qlsach;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;



import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import javax.swing.JSeparator;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.ImageIcon;

public class Login extends JFrame {
	protected static final AbstractButton checkboxremem = null;
	//private PreparedStatement pre = null;
	
	private JPanel dn;
	private JTextField txtten;
	private JPasswordField txtmk;

	static Socket socket;
	static ObjectOutputStream outputStream;
	static ObjectInputStream inputStream;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	
	/**
	 * Create the frame.
	 */
	public Login() {
		String ip = "localhost";
		try {
			socket = new Socket(ip, 12345);
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			System.err.println("Client exception: " + e.toString());
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("ĐĂNG NHẬP");
		setBounds(100, 100, 650, 450);
		dn = new JPanel();
		dn.setBackground(new Color(255, 255, 255));
		dn.setBorder(new EmptyBorder(5, 5, 5, 5));
		setLocationRelativeTo(null);

		setContentPane(dn);
		dn.setLayout(null);
		
		JPanel loginpn = new JPanel();
		loginpn.setBounds(87, 114, 464, 246);
		dn.add(loginpn);
		loginpn.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("User name:");
		lblNewLabel.setBounds(42, 50, 88, 30);
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
		loginpn.add(lblNewLabel);
		
		txtten = new JTextField();
		txtten.setBounds(156, 50, 209, 30);
		txtten.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		loginpn.add(txtten);
		txtten.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("PassWord:");
		lblNewLabel_1.setBounds(42, 100, 88, 20);
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 16));
		loginpn.add(lblNewLabel_1);
		
		txtmk = new JPasswordField();
		txtmk.setBounds(156, 96, 209, 30);
		
		txtmk.addKeyListener(new KeyAdapter() {
		    @Override
		    public void keyPressed(KeyEvent e) {
		        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		        	String username = txtten.getText();
					String password = txtmk.getText();
					
					//tạo để hiển thị một chuỗi thông báo
					
					StringBuilder sb= new StringBuilder();
					
					if(username.equals(""))  {
						sb.append("Username is empty \n");
						
					} 
					if (password.equals("")){
						sb.append("Password is empty \n");
					}
					if(sb.length()>0) {
						JOptionPane.showMessageDialog(null, sb.toString());
						return ;
					}
					
					if(username.equals("Admin") && password.equals("123")) {
						String ten = txtten.getText();
						String mk = txtmk.getText();
						try {
							outputStream.writeObject("login:"+ socket.getLocalPort());
							outputStream.writeObject(ten);
							outputStream.writeObject(mk);
							try {
								String tb= (String) inputStream.readObject();
								if(tb.equals("ok")) {
									ClientGD clientST1 = new ClientGD();
									clientST1.setVisible(true);
									

								}
							} catch (ClassNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
				
					}else {
						JOptionPane.showMessageDialog(null, "Invalid username or password ", "Failure",
								JOptionPane.ERROR_MESSAGE);
					}
						
		        }
		    }
		});
		loginpn.add(txtmk);
		
		
		
		final JCheckBox checbox = new JCheckBox("Show password");
		checbox.setBounds(151, 134, 127, 20);
		checbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(checbox.isSelected()) {
					txtmk.setEchoChar((char)0);
				} else {
					txtmk.setEchoChar('*');
				}
			}
		});
		checbox.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		loginpn.add(checbox);
		
		JButton loginbt = new JButton("Login");
		loginbt.setBounds(156, 181, 89, 30);
		loginbt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = txtten.getText();
				String password = txtmk.getText();
				
				//tạo để hiển thị một chuỗi thông báo
				
				StringBuilder sb= new StringBuilder();
				
				if(username.equals(""))  {
					sb.append("Username is empty \n");
					
				} 
				if (password.equals("")){
					sb.append("Password is empty \n");
				}
				if(sb.length()>0) {
					JOptionPane.showMessageDialog(null, sb.toString());
					return ;
				}
				
				if(username.equals("Admin") && password.equals("123")) {
					String ten = txtten.getText();
					String mk = txtmk.getText();
					try {
						outputStream.writeObject("login:"+ socket.getLocalPort());
						outputStream.writeObject(ten);
						outputStream.writeObject(mk);
						try {
							String tb= (String) inputStream.readObject();
							if(tb.equals("ok")) {
								ClientGD clientST1 = new ClientGD();
								clientST1.setVisible(true);
								

							}
						} catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
			
				}else {
					JOptionPane.showMessageDialog(null, "Invalid username or password ", "Failure",
							JOptionPane.ERROR_MESSAGE);
				}
					
			}
		});
		
		
//		            String password = new String(txtmk.getPassword());
//		            if (password.equals("Admin")) {
//		            	try {
//							String tb= (String) inputStream.readObject();
//							if(tb.equals("ok")) {
//								ClientGD clientST1 = new ClientGD();
//								clientST1.setVisible(true);
//								
//
//							}
//						} catch (ClassNotFoundException | IOException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
//		            } else {
//		               System.out.println("Sai that bai");
//		            }
//		        }
		
		loginbt.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		loginpn.add(loginbt);
		
		JButton resetbt = new JButton("Reset");
		resetbt.setBounds(264, 181, 89, 30);
		resetbt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			//get dung de lay du lieu, set thay doi gia tri
				txtten.setText("");
				txtmk.setText("");
				
			}
		});
		resetbt.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		loginpn.add(resetbt);
		
		JLabel dnlb = new JLabel("Login");
		dnlb.setBounds(205, 10, 73, 30);
		loginpn.add(dnlb);
		dnlb.setBackground(new Color(255, 0, 0));
		dnlb.setFont(new Font("Times New Roman", Font.BOLD, 26));
		
		JButton exitbt = new JButton("Exit");
		exitbt.setBounds(10, 372, 89, 31);
		exitbt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		exitbt.setBackground(new Color(255, 0, 0));
		exitbt.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		dn.add(exitbt);
		
		JPanel logopn = new JPanel();
		logopn.setBounds(0, 0, 636, 104);
		logopn.setBackground(new Color(128, 255, 255));
		dn.add(logopn);
		logopn.setLayout(null);
		
		JLabel vkulb = new JLabel("New label");
		vkulb.setBounds(10, 10, 93, 80);
		vkulb.setIcon(new ImageIcon("C:\\Users\\USER\\Downloads\\vku (2).png"));
		logopn.add(vkulb);
		
		JLabel namelb = new JLabel("Trường Đại học Công nghệ Thông tin và Truyền thông Việt - Hàn");
		namelb.setBounds(101, 10, 515, 24);
		namelb.setFont(new Font("Times New Roman", Font.BOLD, 18));
		logopn.add(namelb);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(130, 44, 454, 2);
		logopn.add(separator_2);
		
		JLabel dclb = new JLabel("Đại chỉ: 470 Trần Đại Nghĩa, P.Hòa Quý, Q.NGũ Hành Sơn, TP.Đà Nẵng ");
		dclb.setBounds(165, 52, 422, 19);
		dclb.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		logopn.add(dclb);
		
		JLabel wslb = new JLabel("Website : http://vku.udn.vn/");
		wslb.setBounds(165, 68, 227, 23);
		wslb.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		logopn.add(wslb);
	}
	
}
