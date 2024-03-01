package com.tunehub.app.controllers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.tunehub.app.entities.User;
import com.tunehub.app.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PaymentController {
	@Autowired
	UserService user_service;
	
	@GetMapping("/map-buyPremium")
	public String mapBuyPremium(HttpSession session) {
		Object email=session.getAttribute("email");
		if(email==null) {
			return "loginForm";
		}
		else {
			User user=user_service.getUser((String)email);
			if(user.getRole().equals("user")) {
				return "buyPremium";
			}
			else {
				session.removeAttribute("email");
				return "loginForm";
			}
		}
	}
	
	@PostMapping("/createOrder")
	@ResponseBody
	public String createOrder(HttpSession session) {
		Order order=null;
		try {
			RazorpayClient razorpay = new RazorpayClient("rzp_test_V2gQgZssVZyyZq", "VLhiFim9HAEMh9WkXfGfu6Jb");

			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount",50000);
			orderRequest.put("currency","INR");
			orderRequest.put("receipt", "receipt#1");
			JSONObject notes = new JSONObject();
			notes.put("notes_key_1","Tea, Earl Grey, Hot");
			orderRequest.put("notes",notes);

			order = razorpay.orders.create(orderRequest);
			
		} catch (Exception e) {
			System.out.println("Exception while creating order.");
		}
		return order.toString();
	}
	
	@PostMapping("/verify")
	@ResponseBody
	public boolean verifyPayment(HttpSession session, @RequestParam  String orderId, @RequestParam String paymentId, @RequestParam String signature) {
	    try {
	        // Initialize Razorpay client with your API key and secret
	        RazorpayClient razorpayClient = new RazorpayClient("rzp_test_V2gQgZssVZyyZq", "VLhiFim9HAEMh9WkXfGfu6Jb");
	        // Create a signature verification data string
	        String verificationData = orderId + "|" + paymentId;

	        // Use Razorpay's utility function to verify the signature
	        boolean isValidSignature = Utils.verifySignature(verificationData, signature, "VLhiFim9HAEMh9WkXfGfu6Jb");
	        //Updating user premium status
	        if(isValidSignature) {
	        	Object email=session.getAttribute("email");
				if(email!=null) {
					User user=user_service.getUser((String)email);
					if(user.getRole().equals("user")) {
						user.setPremium(true);
						user_service.updateUser(user);
					}
				}
	        }
	        return isValidSignature;
	    } catch (RazorpayException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	@GetMapping("/payment-success")
	public String paymentSuccess(HttpSession session ,Model model) {
		Object email=session.getAttribute("email");
		if(email==null) {
			return "loginForm";
		}
		else {
			User user=user_service.getUser((String)email);
			if(user.getRole().equals("user")) {
				model.addAttribute("status", "PaymentSuccess");
				return "buyPremium";
			}
			else {
				session.removeAttribute("email");
				return "loginForm";
			}
		}
	}
	
	@GetMapping("/payment-failure")
	public String paymentFailure(HttpSession session, Model model) {
		Object email=session.getAttribute("email");
		if(email==null) {
			return "loginForm";
		}
		else {
			User user=user_service.getUser((String)email);
			if(user.getRole().equals("user")) {
				model.addAttribute("status", "PaymentFailed");
				return "buyPremium";
			}
			else {
				session.removeAttribute("email");
				return "loginForm";
			}
		}
	}
}
