package com.example.demo.dto;

import java.time.LocalDateTime;

import com.example.demo.model.pojos.LogAcceso;
import com.example.demo.model.pojos.LogSuceso;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode (onlyExplicitlyIncluded = true)

public class LogSucesoDTO {

	@Include
	private long consecutivo;
	private int idUsuario;
	private String nombreUsuario;
	private int idTabla;
	private String nombreTabla;
	private LocalDateTime instante;
	
	public LogSucesoDTO(LogSuceso log) {
		
		this.setConsecutivo(log.getConsecutivo());
		this.setIdUsuario(log.getUsuario().getId());
		this.setNombreUsuario(log.getUsuario().getUsername()+"-"+log.getUsuario().getNombre());
		this.setIdTabla(log.getTabla().getTablaId());
		this.setNombreTabla(log.getTabla().getNombreTabla());
		this.setInstante(log.getInstante());
	}
}
