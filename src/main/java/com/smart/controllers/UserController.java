package com.smart.controllers;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.BillDto;
import com.smart.entities.Contact;
import com.smart.entities.CreditCard;
import com.smart.entities.User;
import com.smart.helper.MessageErrorType;
import com.smart.propertyeditor.MyCreditCardEditor;
import com.smart.propertyeditor.MyCurrencyEditor;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;

	//Handler for current logged in user information
	@ModelAttribute
	public void handlingCommonData(Model model,Principal principal) {
		String username=principal.getName();
		User user = userRepository.findByEmail(username).get();
		model.addAttribute("user", user);
	}

	//Show dash-board of User
	@GetMapping("/index")
	public String showDashboard(Model model) {
		model.addAttribute("title","user/dashboard" );
		return "normaluser/userdashboard";
	}

	//Show add contact form
	@GetMapping("/addcontact")
	public String showAddContactForm(Model model) {
		model.addAttribute("title","user/add-contact" );
		model.addAttribute("contact", new Contact());
		return "normaluser/add-contact";

	}

	//Submit add contact form
	@PostMapping("/processcontact")
	public String handlingSubmitContact(@ModelAttribute("contact") Contact contact, 
			@RequestParam("imag") MultipartFile file,
			Model model,
			HttpSession session) throws IOException,IllegalStateException {
		model.addAttribute("title","user/process-contact" );
		User user = (User) model.getAttribute("user");
		try {
			if(file.isEmpty()) {
				System.out.println("File not chosen");
			}
			else {
				contact.setImage(file.getOriginalFilename());
				File saveFile = new ClassPathResource("static/image").getFile();
				Files.copy(file.getInputStream(), 
						Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename()), 
						StandardCopyOption.REPLACE_EXISTING);
				System.out.println("our image is uploaded");
			}
			contact.setUser(user);
			user.getContacts().add(contact);
			userRepository.save(user);
			System.out.println(user);
			System.out.println(contact);
			session.setAttribute("message", new MessageErrorType("Your contact is successfully added!! Add more","success"));
		}
		catch(Exception e){
			e.printStackTrace();
			session.setAttribute("message", new MessageErrorType("Something went wrong","danger"));

		}
		return "normaluser/add-contact";

	}

	//Show all the contacts of a particular user using pagination
	@GetMapping("/viewcontacts/{page}")
	public String handleshowContacts(Model model,@PathVariable("page") Integer page) {
		model.addAttribute("title","user/view-contact" );
		User user = (User) model.getAttribute("user");
		Sort sort=Sort.by("name").ascending();
		Pageable pageable=PageRequest.of(page, 4 , sort);
		Page<Contact> contactsall = contactRepository.findContactsByUserId(user.getId(), pageable);
		model.addAttribute("contactsall",contactsall);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contactsall.getTotalPages());
		model.addAttribute("totalElements",contactsall.getTotalElements());
		return "normaluser/view-contact";
	}

	//Show the contact details by ID
	@GetMapping("/{cId}/contact")
	public String handleContact(@PathVariable("cId") Integer cId,Model model) {
		//model.addAttribute("title","user/add-contact" );
		Contact contact = contactRepository.findById(cId).get();
		User user = (User) model.getAttribute("user");
		if(user.getId()==contact.getUser().getId())
			model.addAttribute("contact", contact);
		return "normaluser/contact-details";

	}

	//Delete contact by ID
	@PostMapping("/delete/{cId}")
	@Transactional
	public String handleDelete(@PathVariable("cId") Integer cId,HttpSession session,
			Model model) {
		Contact contact = contactRepository.findById(cId).get();
		User user = (User) model.getAttribute("user");
		user.getContacts().remove(contact);
		userRepository.save(user);
		session.setAttribute("message", new MessageErrorType("Contact deleted successfully","success"));
		return "redirect:/user/viewcontacts/0";


	}

	//Show update form of the contact squaring up with ID
	@PostMapping("/update/{cId}")
	public String showUpdate(@PathVariable("cId") Integer cId,Model model) {
		model.addAttribute("title", "update-contact");
		Contact contact = contactRepository.findById(cId).get();
		model.addAttribute("contact", contact);
		return "normaluser/update-form";
	}

	//Update contact following ID
	@PostMapping("/processupdate")
	public String handleUpdate(@ModelAttribute Contact contact,
			@RequestParam("imag") MultipartFile file,
			Model model,
			HttpSession session) {
		try {
			Contact oldcontact = contactRepository.findById(contact.getCId()).get();
			if(!file.isEmpty()) {
				//delete old profile
				File deleteFile = new ClassPathResource("static/image").getFile();
				File file1=new File(deleteFile, oldcontact.getImage());
				file1.delete();
				//save new profile
				File saveFile = new ClassPathResource("static/image").getFile();
				Files.copy(file.getInputStream(), 
						Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename()), 
						StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(file.getOriginalFilename());
			}
			else {
				contact.setImage(oldcontact.getImage());
			}
			User user = (User) model.getAttribute("user");
			contact.setUser(user);
			contactRepository.save(contact);
			session.setAttribute("message", new MessageErrorType("Your contact is successfully updated","success"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/user/"+contact.getCId()+"/contact";

	}

	//Show profile of current user
	@GetMapping("/profile")
	public String showProfile(Model model) {
		model.addAttribute("title", "user/profile");
		return "normaluser/profile";

	}

	//Show setting form page
	@GetMapping("/setting")
	public String showSettingPage(Model model) {
		model.addAttribute("title", "user/setting");
		return "normaluser/setting";
	}

	//Change password by asking old password
	@PostMapping("/change-password")
	public String handleChangePassword(@RequestParam String oldpassword,
			@RequestParam String newpassword,
			Model model,HttpSession session) {
		User currUser = (User) model.getAttribute("user");
		if (bCryptPasswordEncoder.matches(oldpassword, currUser.getPassword())) {
			currUser.setPassword(bCryptPasswordEncoder.encode(newpassword));
			userRepository.save(currUser);
			session.setAttribute("message", new MessageErrorType("The password has been changed","success"));
		}
		else {
			session.setAttribute("message", new MessageErrorType("Please enter correct old password","danger"));
			return"redirect:/user/setting";
		}


		return"redirect:/user/index";

	}

	//Show bill page
	@GetMapping("/support")
	public String showBillPage(Model model){
		model.addAttribute("billDto", new BillDto());
		return "normaluser/bill-page";

	}

	//Submit Payment
	@PostMapping("/submit-payment")
	public String submitPayment(@ModelAttribute("billDto") BillDto billDto, Model model) {
		model.addAttribute("billDto", billDto);
		System.out.println(billDto);
		return "normaluser/submit-payment";

	}

	//Implement predefined and custom initBinder
	@InitBinder
	public void supportHandler(WebDataBinder binder) {

		//Custom property editor for credit-card field
		binder.registerCustomEditor(CreditCard.class, "creditCard", 
				new MyCreditCardEditor());

		//Predefined editor for amount field
		NumberFormat nf=new DecimalFormat("##,###.00");
		binder.registerCustomEditor(BigDecimal.class, "amount",
				new CustomNumberEditor(BigDecimal.class, nf, true));

		//Predefined editor for date field
		SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
		binder.registerCustomEditor(Date.class, "date",
				new CustomDateEditor(sdf, true));

		//Custom editor for currency field
		binder.registerCustomEditor(Currency.class, "currency", new MyCurrencyEditor());

	}


}
