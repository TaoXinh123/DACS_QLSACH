package qlsach;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

public class ClientGD extends JFrame implements Runnable {
	
	DefaultTableModel model = null;
	JButton btAdd, btRefesh, btXoa;

	static Socket socket;
	static ObjectOutputStream outputStream;
	static ObjectInputStream inputStream;
	ArrayList<Sach> list = new ArrayList<Sach>();
	ArrayList<Client> listCl = new ArrayList<Client>();
	private JTable table;
	private JTextField txtid;
	private JTextField txtTen;
	private JTextField txttg;
	private JTextField txtnxb;
	private JTextField txttk;

	public ClientGD() {
		String ip = "localhost";
		try {
			socket = new Socket(ip, 12345);
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			System.err.println("Client exception: " + e.toString());
		}

		setTitle("Book Management");
		setSize(633, 377);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(243, 50, 366, 155);
		getContentPane().add(scrollPane);

		String[] str = { "ID", "Ten", "Tác Giả", "Năm xuất bản" };
		model = new DefaultTableModel(str, 0);
		table = new JTable(model);

		table.addKeyListener(new KeyAdapter() {

			public void keyReleased(KeyEvent e) {
				int row = table.getSelectedRow();
				if (row >= 0) {

					txtid.setText(table.getValueAt(row, 0).toString());
					txtTen.setText(table.getValueAt(row, 1).toString());
					txttg.setText(table.getValueAt(row, 2).toString());
					txtnxb.setText(table.getValueAt(row, 3).toString());

				}
			}
		});

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				if (row >= 0) {

					txtid.setText(table.getValueAt(row, 0).toString());
					txtTen.setText(table.getValueAt(row, 1).toString());
					txttg.setText(table.getValueAt(row, 2).toString());
					txtnxb.setText(table.getValueAt(row, 3).toString());

				}
			}
		});

		scrollPane.setViewportView(table);

		JLabel lblNewLabel = new JLabel("ID:");
		lblNewLabel.setBounds(10, 50, 56, 33);
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		getContentPane().add(lblNewLabel);JLabel lblTen = new JLabel("Ten:");
lblTen.setBounds(10, 89, 56, 33);
		lblTen.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		getContentPane().add(lblTen);

		JLabel lbtg = new JLabel("Tác giả:");
		lbtg.setBounds(10, 132, 56, 33);
		lbtg.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		getContentPane().add(lbtg);

		JLabel lbnxb = new JLabel("Năm XB:");
		lbnxb.setBounds(10, 175, 69, 33);
		lbnxb.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		getContentPane().add(lbnxb);

		txtid = new JTextField();
		txtid.setBounds(76, 52, 135, 27);
		txtid.setFont(new Font("Tahoma", Font.PLAIN, 14));
		getContentPane().add(txtid);
		txtid.setColumns(10);

		txtTen = new JTextField();
		txtTen.setBounds(77, 90, 135, 27);
		txtTen.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtTen.setColumns(10);
		getContentPane().add(txtTen);

		txttg = new JTextField();
		txttg.setBounds(76, 134, 135, 27);
		txttg.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txttg.setColumns(10);
		getContentPane().add(txttg);

		txtnxb = new JTextField();
		txtnxb.setBounds(77, 177, 135, 27);
		txtnxb.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtnxb.setColumns(10);
		getContentPane().add(txtnxb);

		btRefesh = new JButton("Refesh");
		btRefesh.setBounds(10, 286, 112, 33);
		btRefesh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowCount = model.getRowCount();
				// Remove rows one by one from the end of the table
				for (int i = rowCount - 1; i >= 0; i--) {
					model.removeRow(i);
				}
				refreshSachList();
			}
		});
		btRefesh.setFont(new Font("Times New Roman", Font.BOLD, 16));
		getContentPane().add(btRefesh);

		btAdd = new JButton("Add");
		btAdd.setBounds(10, 243, 112, 33);
		btAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addSach();

			}
		});
		btAdd.setFont(new Font("Times New Roman", Font.BOLD, 16));
		getContentPane().add(btAdd);

		JButton btXoa = new JButton("Delete");
		btXoa.setBounds(132, 243, 112, 33);
		btXoa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtid.getText().equals("") || txtTen.getText().equals("") || txttg.getText().equals("")
						|| txtnxb.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Vui lòng chọn sách cần xóa!");
					return;
				} else {
				int indext = table.getSelectedRow();
				try {
					outputStream.writeObject("remove:" + socket.getLocalPort());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Object ma = table.getValueAt(indext, 0);
				try {
					for (Sach s : list) {
						if (ma.equals(s.getId())) {
							outputStream.writeObject(s.getId());

							model.removeRow(indext);
							list.remove(s);
							JOptionPane.showMessageDialog(null, "xoa thanh cong");
						}
					}
				} catch (Exception e2) {// JOptionPane.showMessageDialog(null, "Khong thanh cong " );
				}

			}
			}
		});
		btXoa.setFont(new Font("Times New Roman", Font.BOLD, 16));
		getContentPane().add(btXoa);

		JButton btnEdit = new JButton("Edit");
		btnEdit.setBounds(254, 243, 112, 33);
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int indext = table.getSelectedRow();

				int ma = Integer.parseInt(txtid.getText());
				try {
					for (int i = 0; i < list.size(); i++) {
						
						if (ma == list.get(i).getId()) {
							list.get(i).setId(Integer.parseInt(txtid.getText()));
							list.get(i).setTen(txtTen.getText());
							list.get(i).setTacGia(txttg.getText());
							list.get(i).setNamXB(Integer.parseInt(txtnxb.getText()));

							model.removeRow(indext);
							Object[] o = { list.get(i).getId(), list.get(i).getTen(), list.get(i).getTacGia(),
									list.get(i).getNamXB() };
							model.addRow(o);

							 JOptionPane.showMessageDialog(null, "Sua thanh cong");
						}
					}
					try {
						outputStream.writeObject("edit:" + socket.getLocalPort());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					outputStream.writeObject(list);
					int rowCount = model.getRowCount();
					// Remove rows one by one from the end of the table
					for (int i = rowCount - 1; i >= 0; i--) {
						model.removeRow(i);
					}
					refreshSachList();
				} catch (Exception e2) {

					// JOptionPane.showMessageDialog(null, "Khong thanh cong " );
				}

			}
		});
		btnEdit.setFont(new Font("Times New Roman", Font.BOLD, 16));
		getContentPane().add(btnEdit);
		
		JButton btnNewButton = new JButton("Statistical");
		btnNewButton.setBounds(254, 286, 112, 33);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//gui thong diep toi server
			//	int indext = table.getSelectedRow();
				try {
					outputStream.writeObject("thong ke:" + socket.getLocalPort());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				try {
					//int nam=Integer.parseInt(txtnxb.getText());
					String ten = txtTen.getTen();
					outputStream.writeObject(nam);							
					int tong = (int) inputStream.readObject();
					String tk=" "+tong;
					txttk.setText(tk);
				} 
				catch (Exception e2) {// JOptionPane.showMessageDialog(null, "Khong thanh cong " );
				}
				
			}
		});
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
		getContentPane().add(btnNewButton);
		
		txttk = new JTextField();
		txttk.setBounds(376, 286, 117, 33);
		getContentPane().add(txttk);
		txttk.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("Search: ");
		btnNewButton_1.setBounds(131, 286, 113, 33);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					
				// Send request to server
				int id = Integer.parseInt(txtid.getText());
				outputStream.writeObject("search:" + socket.getLocalPort());
				
				outputStream.writeObject(id);
				outputStream.flush();
				// Receive student list from server
				
				Sach sach = (Sach) inputStream.readObject();
				ArrayList<Sach> list = new ArrayList<>();
	            list.add(sach);
	            DefaultTableModel model = (DefaultTableModel) table.getModel();
	            model.setRowCount(0); // Xóa tất cả các dòng trong bảng
	            
	            for (Sach s : list) {
					Object[] o = { s.getId(), s.getTen(), s.getTacGia(), s.getNamXB() };
					
					model.addRow(o);
				
				}
	            
				System.out.println("Ds là:" + sach.getId()+ sach.getTen()+ sach.getTacGia()+ sach.getNamXB());
				

			} catch (IOException | ClassNotFoundException e1) {
				e1.printStackTrace();
			}
			}		
	});
				
		btnNewButton_1.setFont(new Font("Times New Roman", Font.BOLD, 14));
		getContentPane().add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Clear");
		btnNewButton_2.setBounds(376, 243, 117, 33);
		btnNewButton_2.setFont(new Font("Times New Roman", Font.BOLD, 16));
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtid.setText("");
				txtTen.setText("");
				txttg.setText("");
				txtnxb.setText("");
				txttk.setText("");
				
			}
		});
		
		getContentPane().add(btnNewButton_2);
		
		JLabel lblNewLabel_1 = new JLabel("BOOK MANAGEMENT");
		lblNewLabel_1.setForeground(new Color(255, 0, 0));
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblNewLabel_1.setBounds(195, 10, 260, 30);
		getContentPane().add(lblNewLabel_1);

		refreshSachList();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {

					outputStream.writeObject("cancel:" + socket.getLocalPort());

					// socket.close();
					System.exit(0);

				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		});
	}

	private void refreshSachList() {
		try {

			int portcl = socket.getLocalPort();
			System.out.println(portcl);

			// Send request to server
			outputStream.writeObject("list:" + socket.getLocalPort());
			outputStream.flush();
     
			// Receive student list from server
			list = (ArrayList<Sach>) inputStream.readObject();
			for (Sach s : list) {
				Object[] o = { s.getId(), s.getTen(), s.getTacGia(), s.getNamXB() };
				model.addRow(o);
			
			}

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void addSach() {
		
		if (txtid.getText().equals("") || txtTen.getText().equals("") || txttg.getText().equals("")
				|| txtnxb.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Vui lòng nhập đủ thông tin!");
			return;
		} else {

			try {
				Integer.parseInt(txtid.getText());
				if (Integer.parseInt(txtid.getText()) <= 0) {JOptionPane.showMessageDialog(null, "ID phải là số nguyên dương!");
				return;
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "ID phải là số nguyên dương!");
			return;
			// TODO: handle exception
		}

		try {
			Integer.parseInt(txtnxb.getText());
			if (Integer.parseInt(txtnxb.getText()) <= 0 ) {
				JOptionPane.showMessageDialog(null, "Năm xuất bản phải là số dương!");
				return;
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Năm xuất bản phải là số dương!");
			return;
			
		}

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getId() == Integer.parseInt(txtid.getText())) {
				JOptionPane.showMessageDialog(null, "ID đã tồn tại!");
				return;
			}
		}

	int id = Integer.parseInt(txtid.getText());
	String name = txtTen.getText();
	String tg = txttg.getText();
	int xb = Integer.parseInt(txtnxb.getText());

	Sach s = new Sach(id, name, tg, xb);
	sendAddRequest(s);

	int rowCount = model.getRowCount();
	// Remove rows one by one from the end of the table
	for (int i = rowCount - 1; i >= 0; i--) {
		model.removeRow(i);
	}
   
	refreshSachList();
   
	}
}

private void sendAddRequest(Sach sach) {
	try {

		// Send request to server
		outputStream.writeObject("add:" + socket.getLocalPort());
		outputStream.writeObject(sach);
		outputStream.flush();

		// JOptionPane.showMessageDialog(btAdd, "");
	} catch (IOException e) {
		e.printStackTrace();
	}
}

private void closeConnection() throws IOException {
	try {
		if (outputStream != null)
			outputStream.close();
		if (inputStream != null)
			inputStream.close();
		if (socket != null)
			socket.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
}

public static void main(String[] args) {
	SwingUtilities.invokeLater(new Runnable() {
		@Override
		public void run() {
			try {
				ClientGD clientST1 = new ClientGD();
				clientST1.setVisible(true);

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	});
}

@Override
public void run() {

}
}