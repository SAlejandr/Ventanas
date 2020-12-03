package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.demo.model.pojos.Movimiento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MovimientoDTO {
	
	@Include
	private int item;
	@Include
	private long numDoc;
	@Include
	private String tipoDoc;
	private LocalDate fecha;
	private String concepto;
	private int ccostos;
	private String tercero;
	private String cuenta;
	private BigDecimal base;
	private BigDecimal credito;
	private BigDecimal debito;
	private String estado;
	
	
	public MovimientoDTO(Movimiento movimiento) {
		// TODO Auto-generated constructor stub
		numDoc = movimiento.getId().getDocumento().getIdDocumento().getNumDocumento();
		tipoDoc = movimiento.getId().getDocumento().getIdDocumento().getTipoDocumento().getTipoDoc();
		item = movimiento.getId().getItem();
		fecha = movimiento.getFecha();
		concepto = movimiento.getConcepto();
		ccostos = (movimiento.getCCostos() != null)?movimiento.getCCostos().getCodCentro():0;
		tercero = (movimiento.getTercero() != null)?movimiento.getTercero().getId():"";
		cuenta = movimiento.getCuenta().getCodCuenta();
		base = movimiento.getBase();
		credito = movimiento.getCredito();
		debito = movimiento.getDebito();
		estado = movimiento.getEstado().getDescripcion();
		
	}
	
	

	
	
}
