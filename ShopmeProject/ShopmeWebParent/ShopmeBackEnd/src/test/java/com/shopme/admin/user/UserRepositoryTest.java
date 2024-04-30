package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.hibernate.query.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.repository.UserRepository;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TestEntityManager testEntityManager;

	@Test
	public void testCreateuserWithOneRole() {
		Role roleAdmin = testEntityManager.find(Role.class, 5);
		User userNameWithRole = new User("assist9@.com", "assist1212", "Hin", "Man");
		userNameWithRole.addRole(roleAdmin);

		User saveUser = userRepository.save(userNameWithRole);

		assertThat(saveUser.getId()).isGreaterThan(0);

	}

	@Test
	public void testCreateuserWithTwoRole() {

		User userMahaveer = new User("Sam@gmail.com", "sam@12", "Sam", "Yen");

		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);

		userMahaveer.addRole(roleEditor);
		userMahaveer.addRole(roleAssistant);

		User savedUser = userRepository.save(userMahaveer);

		assertThat(savedUser.getId()).isGreaterThan(0);

	}

	@Test
	public void TestAllListUser() {

		Iterable<User> allUsersList = userRepository.findAll();
		allUsersList.forEach(user -> System.out.println(user));

	}

	@Test
	public void getuserById() {

		User userNamebyId = userRepository.findById(1).get();
		System.out.println(" userNamebyId ========>" + userNamebyId);

		assertThat(userNamebyId).isNotNull();

	}

	@Test
	public void updateUserDetails() {
		User userDetails = userRepository.findById(2).get();
		userDetails.setEnabled(true);
		userDetails.setFirstname("Iron");

		userRepository.save(userDetails);

	}

	@Test
	public void updateUserRoles() {
		User usermahaveer = userRepository.findById(3).get();
		Role removeroleEditor = new Role(3);
		Role roleRoleAssist = new Role(5);

		Role addRoleShipping = new Role(4);
		Role addRoleSales_person = new Role(2);

		usermahaveer.getRoles().remove(removeroleEditor);
		usermahaveer.getRoles().remove(roleRoleAssist);

		usermahaveer.addRole(addRoleShipping);
		usermahaveer.addRole(addRoleSales_person);

		userRepository.save(usermahaveer);

	}

	@Test
	public void deleteUser() {

		Integer userId = 2;
		userRepository.deleteById(userId);

	}

	@Test
	public void getUserByEmail() {

		String emai = "Mahaveer@gmail.com";
		User userByEmail = userRepository.getUserByEmail(emai);
		assertThat(userByEmail).isNotNull();

	}

	@Test
	public void testCountId() {

		Integer id = 100;
		Long countByid = userRepository.countById(id);
		assertThat(countByid).isNotNull().isGreaterThan(0);

	}

	@Test
	public void testDisableUser() {
		Integer id = 9;
		userRepository.enabledStatus(id, false);

	}

	@Test
	public void testEnableUser() {
		Integer id = 9;
		userRepository.enabledStatus(id, true);

	}

	@Test
	public void testListPage() {

		int pageNumber = 1;
		int pageSize = 4;

		org.springframework.data.domain.Pageable pageable = PageRequest.of(pageNumber, pageSize);
		org.springframework.data.domain.Page<User> page = userRepository.findAll(pageable);

		List<User> listUsers = page.getContent();
		listUsers.forEach(user -> System.out.println(user));

		assertThat(listUsers.size()).isEqualTo(pageSize);
	}

	@Test
	public void testSearchUser() {

		String keyword = "Bruce";

		int pageNumber = 0;
		int pagesize = 4;

		org.springframework.data.domain.Pageable pageable = PageRequest.of(pageNumber, pagesize);
		org.springframework.data.domain.Page<User> page = userRepository.findAll(keyword, pageable);
		
		
		List<User> listOfUsers=page.getContent();
		listOfUsers.forEach(user -> System.out.println(user));
		
		assertThat(listOfUsers.size()).isGreaterThan(0);

	}
}
