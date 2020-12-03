package com.example.demo.dto;

import java.time.LocalDateTime;

import com.example.demo.model.pojos.LogAcceso;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode (onlyExplicitlyIncluded = true)

public class LogAccesoDTO {

	@Include
	private long consecutivo;
	private int idUsuario;
	private String nombreUsuario;
	private LocalDateTime instanteDeAcceso;
	private LocalDateTime instanteDeSalida;
	
	public LogAccesoDTO(LogAcceso log) {
		
		this.setConsecutivo(log.getConsecutivo());
		this.setIdUsuario(log.getUsuario().getId());
		this.setNombreUsuario(log.getUsuario().getUsername()+"-"+log.getUsuario().getNombre());
		this.setInstanteDeAcceso(log.getInstanteDeAcceso());
		this.setInstanteDeSalida(log.getInstanteDeSalida());
	}
}
