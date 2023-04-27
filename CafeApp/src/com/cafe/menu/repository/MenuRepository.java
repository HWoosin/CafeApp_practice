package com.cafe.menu.repository;

import java.util.ArrayList;
import java.util.List;

import com.cafe.common.DBConnect;
import com.cafe.menu.domain.Menu;

public class MenuRepository {

	private DBConnect connection = DBConnect.getInstance();

	public void showMenu () {

		String menuSql = "SELECT * FROM menus";

		List<Menu> menuList = new ArrayList<>();
		int count = menuList.size();

		if(count > 0) {
			System.out.print("\n=============== 전체 메뉴 목록 ===============\n");
			for(Menu m : menuList) {
				System.out.println(m);
			}
			System.out.println("===============================================");
		}



	}

}
