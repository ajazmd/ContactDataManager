package com.smart.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CaptchaResponse {
	private boolean success;
	private String challenge_ts;
	private String hostname;
}
