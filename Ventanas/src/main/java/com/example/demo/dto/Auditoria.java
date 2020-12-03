package com.example.demo.dto;

import com.example.demo.model.pojos.Mes;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class Auditoria {

	private Mes mesInicial;
	private Mes mesFinal;
	
}
