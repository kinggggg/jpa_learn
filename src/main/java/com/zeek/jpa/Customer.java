package com.zeek.jpa;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@NamedQuery(name="testNamedQuery", query="FROM Customer c where c.id = ?")
@Cacheable(value=true)
@Table(name="JPA_CUSTOMER")
@Entity
public class Customer {

	private Integer id;
	private String email;
	private Integer age;
	private String lastName;
	private Date createTime;
	private Date birth;
	
	private Set<Order> orders = new HashSet<Order>();

	public Customer() {
		super();
	}

	public Customer(Integer id, Integer age) {
		super();
		this.id = id;
		this.age = age;
	}



	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Column(name="LAST_NAME")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Temporal(value=TemporalType.TIMESTAMP)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(value=TemporalType.DATE)
	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	/**
	 * 工具方法不需要映射数据库的某个字段
	 * @return
	 */
	@Transient
	public String getInfo(){
		return "lastName : " + lastName + ", eamil : " +  email ;
	}

	@OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.REMOVE}, mappedBy="customer")//mappedBy的值为多的一端定义的1的一端的属性名称
	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", email=" + email + ", age=" + age
				+ ", lastName=" + lastName + ", createTime=" + createTime
				+ ", birth=" + birth + "]";
	}
	
	

}
