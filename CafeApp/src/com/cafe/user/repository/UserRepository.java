package com.cafe.user.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.cafe.common.DBConnect;
import com.cafe.menu.domain.Menu;
import com.cafe.menu.domain.MenuList;
import com.cafe.order.domain.Order;
import com.cafe.user.domain.User;

public class UserRepository {
	
	private DBConnect connection = DBConnect.getInstance();
	
	//회원가입
	public void addUser(User user) {
		System.out.println("확인하기::::" +user);
		String insertSql = "INSERT INTO cafeUser (user_ID, user_PW, user_name, user_phone)"
				+ "VALUES(?,?,?,?)";
		try (Connection conn = connection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(insertSql)){	
			pstmt.setString(1, user.getUserID());
			pstmt.setString(2, user.getUserPW());
			pstmt.setString(3, user.getUserName());
			pstmt.setString(4, user.getUserPhone());
			
			if(pstmt.executeUpdate()==1) {
				System.out.println(user.getUserName()+"님 가입이 완료되었습니다!.");
			}
			else {
				System.out.println("회원가입에 실패했습니다.");
			}
		} catch (Exception e) {
		}
	}
	//로그인
	public int loginUser(User user) {
		String selectsql ="Select user_PW, user_name, user_point from cafeUser where user_id = ?";
		try (Connection conn = connection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(selectsql);
				){	
			pstmt.setString(1, user.getUserID());
			ResultSet rs = pstmt.executeQuery();

			if(rs.next()) {
				if(rs.getString(1).contentEquals(user.getUserPW()) )
				System.out.println("\n♥♥♥♥♥ "+rs.getString(2)+"님 환영합니다! ♥♥♥♥♥");
				user.setUserPoint(rs.getInt(3));//유저의 포인트를 끌고 와야 나중에 계산할때 쓸수있다.
				return 1;
			}
			else {
				System.out.println("로그인에 실패했습니다.");
				return 0;
			}
		} catch (Exception e) {
			return -2;//DB오류
		}
	}
	//휴대폰 번호로 회원확인 후 포인트 보여주기 void
	public void phoneNumber(User user) {
		String selectsql ="Select user_point from cafeUser where user_id = ?";
		try (Connection conn = connection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(selectsql);
				){	
			pstmt.setString(1, user.getUserID());
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				System.out.println("\n☆☆☆☆☆ "+rs.getInt(1)+" 점 입니다. ☆☆☆☆☆");
			}
			else {
				System.out.println("조회에 실패했습니다.");
			}
		} catch (Exception e) {
		}
	}
	
	public void SelectNumber(MenuList MenuList) {
		String selectsql ="Select o_menu_name, order_price from orderMenus where who_order = ?";
		try (Connection conn = connection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(selectsql);
				){	
			pstmt.setString(1, MenuList.getWho_order());
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				System.out.println("고객님의 주문 내역 입니다.");
				System.out.println("\n☆☆☆☆☆ 메뉴명 : "+rs.getString(1)+" ☆☆☆☆☆");
				System.out.println("☆☆☆☆☆ 가격 : " +rs.getInt(2)+" 원 ☆☆☆☆☆");
			}
			else {
				System.out.println("주문조회에 실패하셨습니다.");
			}
		} catch (Exception e) {
		}

	};
	
	
	
	
	
	
	
	
	
	
	
}
