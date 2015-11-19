package com.zeek.jpa;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * JPA中的实体的状态和hibernate中实体的状态是相同的（可以查阅下hibernate笔记）
 * 新建状态:   新创建的对象，尚未拥有持久性主键。
	持久化状态：已经拥有持久性主键并和持久化建立了上下文环境
	游离状态：拥有持久化主键，但是没有与持久化建立上下文环境
	删除状态:  拥有持久化主键，已经和持久化建立上下文环境，但是从数据库中删除。

 * @author weibo_li
 *
 */
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
	
	/**
	 * 整体来看JPA的方法类似于hibernate中的saveOrUpdate方法
	 */
	/**
	 * 若传入的是临时对象，JPA会创建一个新的对象，把临时对象的属性复制到新的对象中，然后对新的对象执行持久化操作。所以新的对象中有id
	 * 但是以前的临时对象没有id
	 */
	@Test
	public void testMerge1(){
		Customer customer = new Customer();
		customer.setAge(12);
		customer.setBirth(new Date());
		customer.setCreateTime(new Date());
		customer.setEmail("test@test.com");
		customer.setLastName("zhangsan");
		
		Customer customer2 = entityManager.merge(customer);
		
		System.out.println("Customer: " + customer);
		System.out.println("Customer2: " + customer2);
	}
	
	//若传入的是一个游离对象, 即传入的对象有 OID. 
	//1. 若在 EntityManager 缓存中没有该对象
	//2. 若在数据库中也没有对应的记录
	//3. JPA 会创建一个新的对象, 然后把当前游离对象的属性复制到新创建的对象中
	//4. 对新创建的对象执行 insert 操作. 
	@Test
	public void testMerge2(){
		Customer customer = new Customer();
		customer.setAge(18);
		customer.setBirth(new Date());
		customer.setCreateTime(new Date());
		customer.setEmail("dd@163.com");
		customer.setLastName("DD");
		
		customer.setId(100);
		
		Customer customer2 = entityManager.merge(customer);
		
		System.out.println("customer#id:" + customer.getId());
		System.out.println("customer2#id:" + customer2.getId());
	}
	
	//若传入的是一个游离对象, 即传入的对象有 OID. 
	//1. 若在 EntityManager 缓存中没有该对象
	//2. 若在数据库中也有对应的记录
	//3. JPA 会查询对应的记录, 然后返回该记录对一个的对象, 再然后会把游离对象的属性复制到查询到的对象中.
	//4. 对查询到的对象执行 update 操作. 
	@Test
	public void testMerge3(){
		Customer customer = new Customer();
		customer.setAge(18);
		customer.setBirth(new Date());
		customer.setCreateTime(new Date());
		customer.setEmail("ee@163.com");
		customer.setLastName("EE");
		
		customer.setId(2);
		
		Customer customer2 = entityManager.merge(customer);
		
		System.out.println(customer == customer2); //false
	}
	
	//若传入的是一个游离对象, 即传入的对象有 OID. 
	//1. 若在 EntityManager 缓存中有对应的对象(貌似和hibernate中的老师的笔记一样：位于一级缓存中的对象一定处于持久化状态（在数据库中有相依的记录）)
	//2. JPA 会把游离对象的属性复制到查询到EntityManager 缓存中的对象中.
	//3. EntityManager 缓存中的对象执行 UPDATE. 
	@Test
	public void testMerge4(){
		Customer customer = new Customer();
		customer.setAge(18);
		customer.setBirth(new Date());
		customer.setCreateTime(new Date());
		customer.setEmail("dd@163.com");
		customer.setLastName("DD");
		
		customer.setId(6);
		Customer customer2 = entityManager.find(Customer.class, 6);
		
		entityManager.merge(customer);
		
		System.out.println(customer == customer2); //false
	}
	
	/**
	 * 同hibernate中的session的flush方法，默认在提交事务的时候，刷新缓存
	 */
	@Test
	public void testFlush(){
		Customer customer = entityManager.find(Customer.class, 1);
		customer.setLastName("zhangsan");
	}
	
	/**
	 * 同hibernate种的session的refresh方法。
	 * 这里所演示的refresh方法，只是强迫JPA重新向数据库发送查询语句，并且这里演示的是数据库字段信息没有修改的情况。
	 * 此时出现一个问题：当执行到entityManager.refresh(customer);代码之前，假如说此时id我1的记录被其他的线程修改了，此时如何将被修改的信息refresh到customer中呢？
	 */
	@Test
	public void testRefresh(){
		Customer customer = entityManager.find(Customer.class, 1);
		
		entityManager.refresh(customer);
	}
	
	/**
	 * 保存多对一时, 建议先保存 1 的一端, 后保存 n 的一端, 这样不会多出额外的 UPDATE 语句.
	 */
	@Test
	public void testManyToOnePersist(){
		Customer customer = new Customer();
		customer.setAge(18);
		customer.setBirth(new Date());
		customer.setCreateTime(new Date());
		customer.setEmail("gg@163.com");
		customer.setLastName("GG");
		
		Order order1 = new Order();
		order1.setOrderName("G-GG-1");
		
		Order order2 = new Order();
		order2.setOrderName("G-GG-2");
		
		//设置关联关系
		order1.setCustomer(customer);
		order2.setCustomer(customer);
		
		//执行保存操作
		entityManager.persist(order1);
		entityManager.persist(order2);
		
		entityManager.persist(customer);
	}
}
