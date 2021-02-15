package com.smart.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserUpdate {
	private Integer id;
	private String name;
	private String email;
	private Integer age;
	private String about;


}
