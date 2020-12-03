package com.example.demo.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class AuditoriaDTO {

	private Integer mesInicial= LocalDate.now().getMonthValue();
	private Integer mesFinal= LocalDate.now().getMonthValue();
	private Integer anioAuditoria= LocalDate.now().getYear();
		
}
