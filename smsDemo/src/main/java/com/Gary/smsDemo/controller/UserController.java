 package com.Gary.smsDemo.controller;

import java.io.IOException;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.Gary.smsDemo.domain.User;
import com.Gary.smsDemo.serviceimpl.UserServiceImpl;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;

@RestController
public class UserController {

	@Autowired
	private UserServiceImpl userServiceImpl;
	
	//登录首页，接收所有请求
	@RequestMapping("/")
	public ModelAndView index(HttpServletRequest request,Model model) {
		String message =(String) request.getSession().getAttribute("message");
		model.addAttribute("message",message);
		
		return new ModelAndView("/index.html","userModel",model);
	}
	
	//用户登录
	@RequestMapping("login.action")
	public ModelAndView login(User user,HttpServletRequest request) {
		User loginUser = userServiceImpl.findUserByUsernameAndPassword(user.getUsername(),user.getPassword());
		if(loginUser == null) {
			//用户不存在
			request.getSession().setAttribute("message","用户不存在或密码错误！！");
			return new ModelAndView("redirect:/");
		}else {
			if(loginUser.getState() == 0) {
				//用户未激活
				request.getSession().setAttribute("message","用户未激活！！");
				return new ModelAndView("redirect:/");
				
			}else {
				return new ModelAndView("/welcome.html");
			}
		}
		
	}
	
	//激活用户
	@RequestMapping("/activeUser.action")
	public ModelAndView activeUser(User user,HttpServletRequest request) {
		
		User activeUser = userServiceImpl.findUserByUsernameAndPasswordAndTelephone(user.getUsername(),user.getPassword(),user.getTelephone());
		if(activeUser.getCode().equals(user.getCode())) {
			//可以激活
			userServiceImpl.changeUserState(user.getTelephone(), user.getUsername(), user.getPassword());
			request.getSession().setAttribute("message", "激活成功！！");
		}else {
			//激活失败
			request.getSession().setAttribute("message", "激活失败，请填写完！！");
		}
		
		return new ModelAndView("redirect:/");
	}
	
	//用户注册
	@RequestMapping("/register.action")
	public ModelAndView register(User user,HttpServletRequest request) {
		user.setState(0);
		//用户填写的验证码
		String usersms = user.getCode();
		String sms = (String) request.getSession().getAttribute("sms");
		//系统的验证码
		user.setCode(sms);
		userServiceImpl.save(user);
		
		if(usersms.equals(sms)) {
			//改变用户的状态
			userServiceImpl.changeUserState(user.getTelephone(), user.getUsername(), user.getPassword());
			request.getSession().setAttribute("message", "注册成功！！");
		}else {
			//验证码错误
			request.getSession().setAttribute("message", "验证码错误,请重新激活！！");
		}
			
		return new ModelAndView("redirect:/");
	}
	
	//发送短信
	@RequestMapping("/sms.action")
	public ModelAndView sms(HttpServletRequest request,HttpServletResponse response,String telephone) throws IOException {
		
		//填应用配置 SDK appID
		int appid = 1111184301;
		//填应用配置 APK appkey
		String appkey ="000000000000faba756087b9504bff46";
		
		//填短信正文ID
		int templateId = 111243;
		String smsSign = "Garyd公众号";
		
		String phoneNumber = telephone;
		
		//验证码
		Random r = new Random();
		//字符串的拼接
		String sms = ""+r.nextInt(10)+r.nextInt(10)+r.nextInt(10)+r.nextInt(10);
		//把验证码放置到session域中
		request.getSession().setAttribute("sms", sms);
		String[] params = new String[1];
		params[0]=sms;
		
		SmsSingleSender ssender = new SmsSingleSender(appid,appkey);
		try {
			SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumber, templateId, params, smsSign, "", "");
			System.out.println(result);
		} catch (HTTPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		response.getWriter().write("{\"success\":"+true+"}");
		
		return null;
	}
	
}
