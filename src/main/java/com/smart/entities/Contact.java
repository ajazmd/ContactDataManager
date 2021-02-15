package com.smart.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name="cntct")
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer cId;
	private String name;
	private String nickName;
	private String email;
	private String work;
	private String phone;
	@Column(nullable = true)
	private String image;
	@Column(length=500)
	private String desc;
	@ManyToOne
	@JoinColumn(name="user_fk")
	@JsonBackReference
	private User user;

	public boolean equals(Object obj) {
		return this.cId==((Contact)obj).getCId();

	}



	@Override
	public String toString() {
		return "Contact [cId=" + cId + ", name=" + name + ", nickName=" + nickName + ", email=" + email + ", work="
				+ work + ", phone=" + phone + ", image=" + image + ", desc=" + desc + "]";
	}



}
