package com.example.demo.dto;

import java.time.LocalDate;

import com.example.demo.model.pojos.EstadosDeMes;
import com.example.demo.model.pojos.Mes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MesDTO {
	
	@Include
	private String nombreMes;
	private EstadosDeMes estado;
	private LocalDate fechaInicio, fechaFin;

	public MesDTO(Mes mes) {
		
		nombreMes = mes.getNombre();
		fechaInicio = mes.getInicio();
		fechaFin = mes.getFin();
		estado = mes.getEstado(); 
	}
}
