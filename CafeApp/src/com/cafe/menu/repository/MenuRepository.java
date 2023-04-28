package com.cafe.menu.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cafe.common.DBConnect;
import com.cafe.menu.domain.Menu;
import com.cafe.user.domain.User;

public class MenuRepository {

	private DBConnect connection = DBConnect.getInstance();
	private DBConnect connection2 = DBConnect.getInstance();

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
				if(pstmt2.executeUpdate()==1)
					System.out.println("\n♥♥♥♥♥"+rs.getString(1)+"주문완료!");
				else {
					System.out.println("주문에 실패했습니다.");
				}

			}
			else {
				System.out.println("주문에 실패했습니다.");
			}
		} catch (Exception e) {

		}
	}

}
