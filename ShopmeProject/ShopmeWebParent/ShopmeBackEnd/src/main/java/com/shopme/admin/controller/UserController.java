package com.shopme.admin.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.exception.UserNotFoundException;
import com.shopme.admin.service.UserService;
import com.shopme.admin.user.export.UserCsvExporter;
import com.shopme.admin.user.export.UserExcelExporter;
import com.shopme.admin.user.export.UserPdfExporter;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import com.shopme.common.*;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/users")
	public String listFirstPage(Model model) {
//		List<User> listallusers = userService.listallusers();
//		model.addAttribute("listallusers", listallusers);
//
//		return "users";

		return listByPage(1, model, "id", "asc", null);
	}

	@GetMapping("/users/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortFeild, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {

//		System.out.println("sortFeild==> " + sortFeild);
//		System.out.println("sortDir==> " + sortDir);

		Page<User> page = userService.listofPages(pageNum, sortFeild, sortDir, keyword);
		List<User> listUsers = page.getContent();

		long startCount = (pageNum - 1) * UserService.User_Per_Page + 1;
		long endCount = startCount + UserService.User_Per_Page - 1;

		if (endCount > page.getTotalElements()) {

			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalpages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listallusers", listUsers);
		model.addAttribute("sortFeild", sortFeild);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);

		return "users/users";

	}

	@GetMapping("/users/new")
	public String newUserForm(Model model) {
		List<Role> listofRoles = userService.listofRoles();
		User newUser = new User();
		newUser.setEnabled(true);
		model.addAttribute("newUser", newUser);
		model.addAttribute("listofRoles", listofRoles);
		model.addAttribute("pageTitle", "Create New user");

		return "users/newUser";
	}

	/* Download data in CSV  | Excel | PDF Start */
	
	@GetMapping("/users/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		List<User> listallusers = userService.listallusers();
		UserCsvExporter userexporter = new UserCsvExporter();

		userexporter.export(listallusers, response);

	}
	
	@GetMapping("/users/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		List<User> listallusers = userService.listallusers();
		
		UserExcelExporter userExcelExporter = new UserExcelExporter();
		userExcelExporter.export(listallusers, response);

	}
	
	@GetMapping("/users/export/pdf")
	public void exportToPdf(HttpServletResponse response) throws IOException {
		List<User> listallusers = userService.listallusers();
		
		UserPdfExporter userpdfExporter = new UserPdfExporter();
		userpdfExporter.export(listallusers, response);

	}
	
	
	
	
	/* Download data in CSV  | Excel | PDF  Ends */

	@PostMapping("/user/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {

		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);

			User savedUser = userService.save(user);
			String uploadDir = "user-photos/" + savedUser.getId();
			FileUploadUtil.cleanDirect(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

		} else {
			if (user.getPhotos().isEmpty()) {
				user.setPhotos(null);
				userService.save(user);
			}

		}
		userService.save(user);

		redirectAttributes.addFlashAttribute("message", "The User has been saved successfully!");

		return "redirect:/users";

	}

	@GetMapping("/users/edit/{id}")
	public String eidtUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) {
		try {
			User editUser = userService.get(id);
			List<Role> listofRoles = userService.listofRoles();

			model.addAttribute("newUser", editUser);
			model.addAttribute("listofRoles", listofRoles);
			model.addAttribute("pageTitle", "Edit User (ID:" + id + ")");

			return "users/newUser";

		} catch (UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/users/users";
		}

	}

	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes,
			Model model) {
		try {
			userService.delete(id);
			redirectAttributes.addFlashAttribute("message", "The user " + id + "has been deleted Successfully !");

		} catch (UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}

		return "redirect:/users";
	}

	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnableStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes) {

		userService.updateUserEnabledStatus(id, enabled);
		String status = enabled ? "Enabled" : "Disabled";
		String message = "The User Id" + id + "has been :" + status;
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/users";

	}

}
