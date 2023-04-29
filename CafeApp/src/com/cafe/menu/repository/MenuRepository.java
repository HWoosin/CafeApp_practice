package com.cafe.menu.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cafe.common.DBConnect;
import com.cafe.menu.domain.Menu;
import com.cafe.payment.Payment;
import com.cafe.user.domain.User;


public class MenuRepository {

	private DBConnect connection = DBConnect.getInstance();
	private DBConnect connection2 = DBConnect.getInstance();
	private DBConnect connection3 = DBConnect.getInstance();

	//메뉴전체 불러오기
	public void addMenu () {

		List<Menu> menuList = new ArrayList<>();

		String menuSql = "SELECT * FROM cafeMenus";

		try (Connection conn = connection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(menuSql)){
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Menu menu = new Menu(
						rs.getString("menu_name"),
						rs.getInt("price"),
						rs.getString("menu_type")
						);
				menuList.add(menu);	

			}
		} catch (Exception e) {
			e.printStackTrace();
		}	

		int count = menuList.size();
		if(count > 0) {
			System.out.print("\n=============== 전체 메뉴 목록 ===============\n");
			for(Menu m : menuList) {
				System.out.println(m);
			}
			System.out.println("===============================================");
		}
	}

	//메뉴 주문하기 - 주문하자마자 주문내역 테이블에 추가
	public void menuHistory(Menu menu) {
		String selectsql ="Select menu_name, price from cafeMenus where menu_name = ?";
		String insertsql ="Insert into orderMenus (order_num, o_menu_name, order_price)"
				+ "values(orderMenus_seq.NEXTVAL, ?, ?) ";
		try (Connection conn = connection.getConnection();
				Connection conn2 = connection2.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(selectsql);
				PreparedStatement pstmt2 = conn2.prepareStatement(insertsql)
				){	
			pstmt.setString(1, menu.getMenuName());
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				pstmt2.setString(1,rs.getString(1));
				pstmt2.setInt(2,rs.getInt(2));
				if(pstmt2.executeUpdate()==1) {
					System.out.println("\n♥♥♥♥♥"+rs.getString(1)+"선택완료!♥♥♥♥♥\n");
					menu.setPrice(rs.getInt(2));
				}
				else {
					System.out.println("주문에 실패했습니다.");
				}

			}
			else {
				System.out.println("메뉴에 없는 목록입니다.");
			}
		} catch (Exception e) {

		}
	}
	//결제 방법 선택하기, 포인트 결제는 포인트에서 차감.
	public void paymentMenu(Payment payment, User user, Menu menu) {
		String selectsql = "Select howtopay from howpayment where howtopay =?";
		String updatesql ="Update orderMenus set payment = ? where o_menu_name = ?";	
		try(Connection conn = connection.getConnection();
				Connection conn2 = connection2.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(selectsql);
				PreparedStatement pstmt2 = conn2.prepareStatement(updatesql)){
			
			pstmt.setString(1, payment.getHowToPay());
			ResultSet rs = pstmt.executeQuery();

			if(rs.next()) {//검사
				if(rs.getString(1).equals("카드결제")) {
					pstmt2.setString(2,menu.getMenuName());
					pstmt2.setString(1,rs.getString(1));
					System.out.println("카드결제 완료!");
					
				}
				else if (rs.getString(1).equals("포인트결제")) {
					pstmt2.setString(2,menu.getMenuName());
					pstmt2.setString(1,rs.getString(1));
					payPoint(user,menu);
					
				}
				pstmt2.executeUpdate();
			}
			else {
				System.out.println("결제에 실패하였습니다.2");
			}
			
		} catch (Exception e) {
			
		}
	}
	//포인트 결제.
	public void payPoint(User user, Menu menu) {
		String selectPSql = "Select user_point from cafeUser Where user_id = ?";
		String selectMSql = "Select price from cafeMenus where menu_name = ?";
		
		String updatePSql = "Update cafeUser set user_point = ? where user_id =?";
		int point = 0;
		int price = 0;
		int result = 0;
		try (Connection conn = connection.getConnection();
				Connection conn2 = connection2.getConnection();
				Connection conn3 = connection3.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(selectPSql);
				PreparedStatement pstmt2 = conn2.prepareStatement(selectMSql);
				PreparedStatement pstmt3 = conn3.prepareStatement(updatePSql);) {
			
			pstmt.setString(1, user.getUserID());
			ResultSet rs1 = pstmt.executeQuery();//사용자 포인트
			rs1.next();
			
			pstmt2.setString(1, menu.getMenuName());
			ResultSet rs2 = pstmt2.executeQuery();//메뉴 가격
			rs2.next();
			
			point = rs1.getInt(1);//값들을 변수에저장해서 계산
			price = rs2.getInt(1);
			
			if(point>=price) {//포인트 계산로직
				System.out.println(user.getUserID()+"님이 "+price+"원을 포인트로 계산합니다.");
				result = point - price;
				pstmt3.setInt(1,result);//포인트 계산 후 사용자의 잔여 포인트를 업데이트 해줌
				pstmt3.setString(2,user.getUserID());
				System.out.println("결제완료! 남은 포인트"+result);
				pstmt3.executeUpdate();
			}
			else {
				System.out.println("결제실패! 포인트가 부족합니다. 잔액:"+point);
			}
			
			
		} catch (Exception e) {
			System.out.println("결제실패! DB오류!");

		}
	}

}
