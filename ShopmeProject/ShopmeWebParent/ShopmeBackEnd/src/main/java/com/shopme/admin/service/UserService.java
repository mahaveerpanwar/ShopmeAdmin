package com.shopme.admin.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.admin.exception.UserNotFoundException;
import com.shopme.admin.repository.RoleRepository;
import com.shopme.admin.repository.UserRepository;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {

	public static final int User_Per_Page = 5;

	@Autowired
	private UserRepository userrepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public User getByEmail(String email) {

		return userrepository.getUserByEmail(email);

	}

	public List<User> listallusers() {

		return (List<User>) userrepository.findAll(Sort.by("firstname").ascending());

	}

	public Page<User> listofPages(int pageNumber, String sortFeild, String sortDir, String keyword) {

		Sort sort = Sort.by(sortFeild);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNumber - 1, User_Per_Page, sort);

		if (keyword != null) {

			return userrepository.findAll(keyword, pageable);
		}
		return userrepository.findAll(pageable);

	}

	public List<Role> listofRoles() {
		return (List<Role>) roleRepository.findAll();
	}

	public User save(User user) {

		boolean isUpdatinguser = (user.getId() != null);

		if (isUpdatinguser) {
			User isexistingUser = userrepository.findById(user.getId()).get();

			if (user.getPassword().isEmpty()) {

				user.setPassword(isexistingUser.getPassword());

			} else {
				encodePassword(user);
			}

		} else {

			encodePassword(user);
		}

		return userrepository.save(user);

	}

	// Updating the details of new user

	public User updateAccount(User userInform) {
		User userUdpdateInfo = userrepository.findById(userInform.getId()).get();

		if (!userInform.getPassword().isEmpty()) {
			userUdpdateInfo.setPassword(userInform.getPassword());
			encodePassword(userUdpdateInfo);
		}
		
		if(userInform.getPhotos()!=null) {
			userUdpdateInfo.setPhotos(userInform.getPhotos());
		}
		
		userUdpdateInfo.setFirstname(userInform.getFirstname());
		userUdpdateInfo.setLastname(userInform.getLastname());

		return userrepository.save(userUdpdateInfo);
	}

	private void encodePassword(User user) {

		String encondedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encondedPassword);

	}

	@SuppressWarnings({ "unused", "null" })
	public boolean isEmailUnique(Integer id, String email) {

		User userByEmail = userrepository.getUserByEmail(email);

		if (userByEmail == null)
			return true;

		boolean isCreatingNew = (id == null);

		if (isCreatingNew) {
			if (userByEmail != null)
				return false;

		} else {
			if (userByEmail.getId() != id) {

				return false;
			}
		}

		return true;

	}

	public User get(Integer id) throws UserNotFoundException {

		try {
			return userrepository.findById(id).get();

		} catch (NoSuchElementException e) {
			throw new UserNotFoundException("User not found with this ID: " + id);

		}
	}
	
	

	public void delete(Integer id) throws UserNotFoundException {

		Long countById = userrepository.countById(id);
		if (countById == null || countById == 0) {
			throw new UserNotFoundException("User not found with this ID: " + id);

		}

		userrepository.deleteById(id);
	}

	public void updateUserEnabledStatus(Integer id, boolean enabled) {
		userrepository.enabledStatus(id, enabled);

	}

}
