package com.example.demo.dto;

import java.math.BigDecimal;

import com.example.demo.model.pojos.SaldoCentroDeCostos;
import com.example.demo.model.pojos.SaldoCuenta;

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
public class SaldoCCostosDTO {

	@Include
	private String cod_cuenta;
	@Include
	private Integer anno;
	@Include
	private Integer cCostos;
	@Include
	private String mes;
	
	private BigDecimal suma_debito;
	private BigDecimal suma_credito;
	private BigDecimal saldo_debito;
	private BigDecimal saldo_credito;
	
	public SaldoCCostosDTO (SaldoCentroDeCostos s) {
		
		this(s.getElID().getCuenta().getCodCuenta(), s.getElID().getMes().getIdFiscal().getAnno().getElAnno(), s.getElID().getCentro().getCodCentro(), s.getElID().getMes().getNombre(), s.getSumDebito(), s.getSumCredito(), s.getTotalDebito(), s.getTotalCredito());
	}
}
