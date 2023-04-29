package com.cafe.user.service;

import static com.cafe.common.AppInput.inputInteger;
import static com.cafe.common.AppInput.inputString;

import com.cafe.common.AppStart;
import com.cafe.menu.domain.Menu;
import com.cafe.menu.repository.MenuRepository;
import com.cafe.order.domain.Order;
import com.cafe.payment.Payment;
import com.cafe.user.domain.User;
import com.cafe.user.repository.UserRepository;
import com.cafe.view.AppUI;

public class UserLogin implements AppStart{
	public final UserRepository userRepository = new UserRepository();
	public final MenuRepository menuRepository = new MenuRepository();
	User user = new User();
	Menu menu = new Menu();
	Payment payment = new Payment();
	Order order = new Order();
	
	String ID ="";
	@Override
	public void start() {
		login();
		while(true) {
//			login();
			if(userRepository.loginUser(user)==1) {
				AppUI.userMenu();
				int selection = inputInteger();
				switch (selection) {
				case 1://주문하기
					menuRepository.addMenu();
					AppUI.orderMenu();
					selection = inputInteger();
					switch (selection) {
					case 1: //메뉴선택
						chooseMenu();
						menuRepository.menuHistory(menu);
						payment();
//						System.out.println(payment.getHowToPay());
						menuRepository.paymentMenu(payment,user,menu,order);
						break;
					case 2: //뒤로가기
						break;
					default:
						System.out.println("잘못된 선택");
					}
					break;
					
				case 2://포인트조회
					findpoint();
					userRepository.phoneNumber(user);
					break;
				case 3://주문조회
					
					break;
				case 4://로그아웃
					
					return;

				default:
					System.out.println("잘못된 선택");
				}
				
			}
			else {
//				return;
			}
		}
//		System.out.println(userRepository.loginUser(user)); 
		
	}


	public void login() {
		System.out.println("\n====== 로그인을 진행합니다. ======");
		System.out.print("♥아이디: ");
		ID = inputString();
		System.out.print("♥비밀번호: ");
		String PW = inputString();
		
		
		user.setUserID(ID);
		user.setUserPW(PW);
		
//		userRepository.loginUser(user);
		
	}
	public void findpoint() {
		System.out.println("포인트조회");
		System.out.println("♥본인확인을 위한 전화번호를 입력해주세요! ");
		System.out.print(">>>");
		String phone = inputString();
		
		user.setUserPhone(phone);
		
	}
	
	public void chooseMenu() {
		System.out.println("주문하실 메뉴를 입력해주세요 ");
		System.out.print(">>>");
		String menuName = inputString();
		
		menu.setMenuName(menuName);
	}
	public void payment() {
		System.out.println("결제방법을 선택해주세요 ");
		System.out.println("[1.카드결제 | 2.포인트 결제]");
		System.out.print(">>>");
		int pay = inputInteger();
		
		if(pay == 1) {
			payment.setHowToPay("카드결제");
			user.setUserID(ID);
		}
		else if (pay == 2) {
			payment.setHowToPay("포인트결제");
			user.setUserID(ID);
		}
		else {
			System.out.println("잘못된선택");
		}
	}
	
}
