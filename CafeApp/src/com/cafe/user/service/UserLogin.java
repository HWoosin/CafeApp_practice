package com.cafe.user.service;

import static com.cafe.common.AppInput.inputString;

import com.cafe.common.AppStart;
import com.cafe.user.domain.User;
import com.cafe.user.repository.UserRepository;
import com.cafe.view.AppUI;

public class UserLogin implements AppStart{
	public final UserRepository userRepository = new UserRepository();
	User user = new User();
	@Override
	public void start() {
		login();
		if(userRepository.loginUser(user)==1) {
			AppUI.UserMenu();
			inputString();
			return;
		}
		else {
			return;
		}
//		System.out.println(userRepository.loginUser(user)); 
		
	}
	
	public void login() {
		System.out.println("\n====== 로그인을 진행합니다. ======");
		System.out.print("♥아이디: ");
		String ID = inputString();
		System.out.print("♥비밀번호: ");
		String PW = inputString();
		
		
		user.setUserID(ID);
		user.setUserPW(PW);
		
//		userRepository.loginUser(user);
		
	}

}
