package com.smart.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.smart.validator.Age;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name="usr")
public class User {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Integer id;
	@NotBlank(message = "*Your name should not have empty")
	@Size(min = 3,max = 15, message = "*Your name should have 3-15 characters")
	private String name;
	@Column(unique = true)
	@Email(regexp = ".+@.+\\..+", message = "*Please provide a valid email ")
	private String email;
	@NotEmpty(message="*Password might not be empty")
	private String password;
	private String imageUrl;
	private Boolean enabled;
	private String gender;
	@Age
	private Integer age;
	private String role;
	@AssertTrue(message = "*To use services you must agree our terms and conditions")
	private boolean agreement;
	@Size(min=0 ,max=200, message = "*Your description exceeded the range")
	private String about;
	@OneToMany(mappedBy = "user" , cascade = CascadeType.ALL,fetch = FetchType.EAGER
			,orphanRemoval = true) 
	@JsonManagedReference
	private List<Contact> contacts=new ArrayList<>();


}
