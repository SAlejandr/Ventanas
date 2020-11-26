package com.example.demo.dto;

import com.example.demo.model.pojos.NaturalezaJuridica;
import com.example.demo.model.pojos.Tercero;
import com.example.demo.model.pojos.Tipo_Identificacion;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TerceroDTO {

	@Include
	private String id;
	private NaturalezaJuridica naturaleza;
	private Tipo_Identificacion tipoIdentificacion;
	private String nombre;
	private String direccion;
	private String email;
	private String telefono1;
	private String telefono2;
	private String ciudad;
	
	public TerceroDTO(Tercero tercero) {
		
		id = tercero.getId();
		naturaleza = tercero.getNaturaleza();
		tipoIdentificacion = tercero.getIdentificacion();
		nombre = tercero.getNombre();
		direccion = tercero.getDireccion();
		email = tercero.getEmail();
		telefono1 = tercero.getTelefono1();
		telefono2 = tercero.getTelefono2();
		ciudad = tercero.getCiudad().getId().getDepartamento().getNombre()+"/"+ tercero.getCiudad().getNombre();
		
	}
}
