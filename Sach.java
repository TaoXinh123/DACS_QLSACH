package qlsach;

import java.io.Serializable;

public class Sach implements Serializable{
	private int id;
	private String ten;
	private String tacGia;
	private int namXB;
	//private String NXB;
	
	
	public Sach() {
		super();
	
	}
	public Sach(int id, String ten, String tacGia, int namXB) {
		super();
		this.id = id;
		this.ten = ten;
		this.tacGia = tacGia;
		this.namXB = namXB;
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTen() {
		return ten;
	}
	public void setTen(String ten) {
		this.ten = ten;
	}
	public String getTacGia() {
		return tacGia;
	}
	public void setTacGia(String tacGia) {
		this.tacGia = tacGia;
	}
	public int getNamXB() {
		return namXB;
	}
	public void setNamXB(int namXB) {
		this.namXB = namXB;
	}
	
	
}
