package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.demo.model.pojos.Movimiento;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MovimientoDTO {
	
	@Include
	private int item;
	private LocalDate fecha;
	private String concepto;
	private String ccostos;
	private String tercero;
	private String cuenta;
	private BigDecimal base;
	private BigDecimal credito;
	private BigDecimal debito;
	private String estado;
	
	
	public MovimientoDTO(Movimiento movimiento) {
		// TODO Auto-generated constructor stub
		
		item = movimiento.getId().getItem();
		fecha = movimiento.getFecha();
		concepto = movimiento.getConcepto();
		ccostos = (movimiento.getCCostos() != null)?movimiento.getCCostos().getCodCentro()+"-"+movimiento.getCCostos().getNombre():0+"-";
		tercero = (movimiento.getTercero() != null)?movimiento.getTercero().getId()+"-"+movimiento.getTercero().getNombre():".-";
		cuenta = movimiento.getCuenta().getCodCuenta()+"-"+movimiento.getCuenta().getNombre();
		base = movimiento.getBase();
		credito = movimiento.getCredito();
		debito = movimiento.getDebito();
		estado = movimiento.getEstado().getDescripcion();
		
	}
	
}
