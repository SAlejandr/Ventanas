package com.example.demo.dto;

import java.math.BigDecimal;

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
public class SaldoCuentaDTO {

	@Include
	private String cod_cuenta;
	@Include
	private Integer anno;
	@Include
	private String mes;
	private BigDecimal suma_debito;
	private BigDecimal suma_credito;
	private BigDecimal saldo_debito;
	private BigDecimal saldo_credito;
	
	public SaldoCuentaDTO (SaldoCuenta s) {
		
		this(s.getId().getCuenta().getCodCuenta(), s.getId().getMes().getIdFiscal().getAnno().getElAnno(), s.getId().getMes().getIdFiscal().getMes(), s.getSumDebito(), s.getSumCredito(), s.getTotalDebito(), s.getTotalCredito());
	}
	
}
