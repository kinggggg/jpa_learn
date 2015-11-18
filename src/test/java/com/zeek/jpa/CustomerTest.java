package com.zeek.jpa;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CustomerTest {
	
	private static EntityManagerFactory entityManagerFactory ;
	private static EntityManager entityManager;
	private static EntityTransaction entityTransaction;
	
	@BeforeClass
	public static void init(){
		String persistenceUnitName = "jpa-1" ;
		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
		entityManager = entityManagerFactory.createEntityManager();
		entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
	}
	
	@AfterClass
	public static void destroy(){
		entityTransaction.commit();
		entityManager.close();
		entityManagerFactory.close();
	}

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
	
	/**
	 * JPA 中find方法相当于hibernate中的get方法
	 */
	@Test
	public void testFind(){
		Customer customer = entityManager.find(Customer.class, 100);
		System.out.println("========================================================");
		System.out.println(customer);
	}
	
	/**
	 * JPA中的getReference方法相当于hibernate中的load方法
	 */
	@Test
	public void testGetReference(){
		Customer customer = entityManager.getReference(Customer.class, 100);
		System.out.println(customer.getClass().getName());//getReference得到的是一个代理对象
		System.out.println("========================================================");
		System.out.println(customer);
	}
	
	/**
	 * JPA中的persist方法类似于hibernate中的save方法: 使对象由临时状态变为持久化状态
	 * 但是与hibernate中的save有些许的不同:若对象有id，则不能执行insert操作，否则会抛出异常
	 * 
	 */
	@Test
	public void testPersist(){
		Customer customer = new Customer();
		customer.setAge(22);
		customer.setBirth(new Date());
		customer.setCreateTime(new Date());
		customer.setEmail("test@test.com");
		customer.setLastName("zhangsan");
//		customer.setId(200);
		entityManager.persist(customer);
		System.out.println(customer);
	}
	
	/**
	 * JPA中的remove方法相当于hibernate中的delete方法：是对象对应的记录从数据库中删除
	 * 但是，该方法只能移除 持久化 对象，而hibernate中的remove方法实际上还可以移除游离对象
	 */
	@Test
	public void testRemove(){
		//
//		Customer customer = new Customer();
//		customer.setId(200);
//		entityManager.remove(customer);
		Customer customer = entityManager.find(Customer.class, 200);
		entityManager.remove(customer);
		System.out.println(customer);
	}
}
