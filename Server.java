package qlsach;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class Server {
	static DBConnection con= new DBConnection();
	static PreparedStatement pre= null;
	static ResultSet rs=null;
	
	private static final int PORT = 12345;
	private static ArrayList<Sach> sachList = new ArrayList<>();
	static ArrayList<Client> listCl = new ArrayList<Client>();
	static File file = null;
	static ObjectInputStream ois = null;
	static ObjectOutputStream oos = null;
	static FileOutputStream fos = null;
	static FileInputStream fis = null;

	public static void main(String[] args) {
		try {
			loadFromFile(); // Load student data from file

			ServerSocket serverSocket = new ServerSocket(PORT);
			System.out.println("Server ready ... ");

			while (true) {
				//tao socket chap nhan cac ket noi tu client
				Socket socket = serverSocket.accept();
				
				// Khi nhận được kết nối từ client
				String clientAddress = socket.getInetAddress().getHostAddress();
				int clientPort = socket.getPort();
				Client newclient = new Client(clientAddress, clientPort, socket);

				listCl.add(newclient);

				// Để truy cập thông tin của một client cụ thể
//				int port = clientMap.get(clientAddress);
//				System.out.println("Client connected: " + port);
				for (Client client : listCl) {
					System.out.println(client.getPort() + " " + client.getIp());

				}
				// Luồng
				Thread thread = new Thread(new Luong(socket));
				thread.start();

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static synchronized void addSach(Sach sach, ObjectOutputStream oos) throws IOException {
		sachList.add(sach);
		saveToFile();
		// lưu student vào file abc.bin
		System.out.println("Added new book: " + sach);
	}
	private static synchronized void editSach(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		sachList.clear();
		sachList = (ArrayList<Sach>) ois.readObject();
//		studentList.add(student);
		saveToFile();
		// lưu student vào file abc.bin
		System.out.println("edit book: ");
	}

	private static synchronized void removeSach(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		int s1 = (int) ois.readObject();
		ArrayList<Sach> toRemove = new ArrayList<>();
		for (Sach sach : sachList) {
			if (s1 == sach.getId()) {
				// Mark student for removal
				toRemove.add(sach);
				System.out.println("Da danh dau sach can xoa: " + sach);
			}
		}
		// Remove students from list
		sachList.removeAll(toRemove);
		saveToFile();

	}
	
	private static synchronized void thongke(ObjectInputStream ois,ObjectOutputStream oos) throws IOException, ClassNotFoundException {
		int nam = (int) ois.readObject();
	   int d=0;
		for (Sach sach : sachList) {
			if (nam== sach.getNamXB()) {
				// Mark student for removal
				d++;
			}
		}
		// Remove students from list
		
		oos.writeObject(d);
		oos.flush();
	}
	
//login dang nhap vao 
	private static synchronized void login(ObjectOutputStream oos, ObjectInputStream ois)throws IOException, ClassNotFoundException, SQLException {
		try {
			String ten = (String) ois.readObject();
			String mk = (String ) ois.readObject();
			
		//	con = getConnection();
			rs= con.Login(ten);
			
			if(rs.next()) {
				oos.writeObject("ok");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//bt tim kiem
	private static synchronized void searchSach(ObjectInputStream ois, ObjectOutputStream oos) throws IOException, ClassNotFoundException{
		try {
			int id = (int) ois.readObject();
			for (Sach sach : sachList) {
				if (id== sach.getId()) {
					oos.writeObject(sach);
					oos.flush();
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	
	private static synchronized void sendRemove(ObjectOutputStream oos) {
		try {

			oos.writeObject(sachList);
			oos.flush();
			System.out.println("Đã gửi ds tới Client");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static synchronized void sendSachList(ObjectOutputStream oos) {
		try {

			oos.writeObject(sachList);
			oos.flush();
			System.out.println("Đã gửi ds tới Client");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static synchronized void loadFromFile() {
		file = new File("s.bin");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		try {
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			sachList = (ArrayList<Sach>) ois.readObject();
			ois.close();
			fis.close();
			System.out.println("Loaded data from file");
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static synchronized void saveToFile() {
		try {
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(sachList);
			oos.close();
			fos.close();
			System.out.println("Đã lưu vào file");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static synchronized void removeClient(Client client) {
		if (listCl.contains(client)) {
			listCl.remove(client);
			System.out.println("Client disconnected: " + client.getPort() + " " + client.getIp());
		}
	}

	public static Sach findSachByID(int maSach) {
	    for (Sach sach : sachList) {
	        if (sach.getId() == maSach) {
	            return sach;
	        }
	    }
	    return null;
	}
	
	static class Luong implements Runnable {
		private Socket socket;

		public Luong(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

				// Read client request and process it

				while (!socket.isClosed()) {
					try {
						String request = ("" + inputStream.readObject());
//						System.out.println(request);
						String[] s = request.split(":", 2);

						switch (s[0]) {
						case "add":
							Sach sach = (Sach) inputStream.readObject();
							addSach(sach, outputStream);
							break;
						case "list":
							loadFromFile();
							sendSachList(outputStream);
							break;
						case "remove":
							removeSach(inputStream);
							break;

						case "cancel":
							for (int i = 0; i < listCl.size(); i++) {
								int port = listCl.get(i).getPort();
								int out = Integer.parseInt(s[1]);
								if (out == port) {
									listCl.remove(listCl.get(i));

									System.out.println("Client disconnected: " + port);
								}

							}
							break;
						case "edit":
							editSach(inputStream);
							break;
						case "thong ke":
							thongke(inputStream, outputStream);
							break;
						case "login":
						       login(outputStream, inputStream);
						   
							break;
						case "search":
							searchSach(inputStream, outputStream);
							break;
						default:
							break;
						}
						System.out.println(s[0]);
					} catch (SocketException e) {

					} catch (IOException | ClassNotFoundException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("da thoat");
			}
		}
	}
}
