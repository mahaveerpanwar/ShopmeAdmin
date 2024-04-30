package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.repository.RoleRepository;
import com.shopme.common.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {

	@Autowired
	private RoleRepository roleRepository;

	@Test
	public void TestCreateFirstRole() {

		Role roleAdmin = new Role("Admin", "manage Everything");
		Role savenewrole = roleRepository.save(roleAdmin);

		assertThat(savenewrole.getId()).isGreaterThan(0);

	}

	@Test
	public void TestCreateRestRoles() {
		Role roleSalesPerson = new Role("Sales_Person",
				"manage Product price, customers, shipping, orders & sales report");
		Role roleEditor = new Role("Editor", "manage categories, brands, product, articles & menus");
		Role roleShipper = new Role("Shipper", "View products,view orders &  update order status");
		Role roleAssistant = new Role("Assistant", "manage questions and review");
		
		roleRepository.saveAll(List.of(roleSalesPerson, roleEditor, roleShipper, roleAssistant));

	}

}
