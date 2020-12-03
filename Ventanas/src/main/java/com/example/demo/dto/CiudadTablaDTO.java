 package com.example.demo.dto;

import com.example.demo.model.pojos.Ciudad;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CiudadTablaDTO {

	@Include
	private String id;

	private String nombre;
	private long poblacion;
	
	@Include
	private String departamento;
	
	public CiudadTablaDTO(Ciudad c) {
		// TODO Auto-generated constructor stub
		
		id = c.getId().getCodCiudad();
		nombre = c.getNombre();
		poblacion = c.getPoblacion();
		departamento = c.getId().getDepartamento().getNombre();
	}
}
