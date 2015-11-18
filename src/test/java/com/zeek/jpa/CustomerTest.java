package com.zeek.jpa;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.Test;

public class CustomerTest {

	@Test
	public void customerTest(){
		String persistenceUnitName = "jpa-1" ;
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
		//得到entityManagerFactory的第二种方式，通过这里设置的属性第一优先使用
//		Map<String,String> map = new HashMap<String,String>();
//		map.put("hibernate.show_sql", "false");
//		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName, map);
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		Customer c = new Customer();
		c.setAge(23);
		c.setEmail("test@test.com");
		c.setLastName("zeek");
		c.setCreateTime(new Date());
		c.setBirth(new Date());
		entityManager.persist(c);
		transaction.commit();
		entityManager.close();
		entityManagerFactory.close();
	}
}
