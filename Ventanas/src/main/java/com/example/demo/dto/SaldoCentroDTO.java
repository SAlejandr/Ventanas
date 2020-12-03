package com.example.demo.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.example.demo.model.pojos.SaldoCuenta.SaldoCuentaBuilder;
import com.example.demo.model.pojos.Anno;
import com.example.demo.model.pojos.CentroDeCosto;
import com.example.demo.model.pojos.Cuenta;
import com.example.demo.model.pojos.MesFiscalId;
import com.example.demo.model.pojos.SaldoCentroDeCostos;
import com.example.demo.model.pojos.IdSaldo;
import com.example.demo.model.pojos.IdSaldoCCostos;
import com.example.demo.model.pojos.Mes;
import com.example.demo.model.pojos.SaldoCuenta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)


public class SaldoCentroDTO {
	@Include
	private String cod_cuenta;
	@Include
	private Integer anno;
	@Include
	private int mes;
	@Include
	private int centro;
	private BigDecimal suma_debito;
	private BigDecimal suma_credito;
	private BigDecimal total_debito;
	private BigDecimal total_credito;
	
	public SaldoCentroDeCostos convertir() {
		
		SaldoCentroDeCostos s = SaldoCentroDeCostos.builder().
				elID(IdSaldoCCostos.builder().
						cuenta(Cuenta .builder().
								codCuenta(cod_cuenta).
								build()).
						mes(Mes.builder().
								idFiscal(new MesFiscalId(Anno.builder().
										elAnno(anno.intValue()).build(), mes)).
								build()).
						centro(CentroDeCosto.builder().
								codCentro(centro).
								build()).
						build()).
				sumCredito(suma_credito).
				sumDebito(suma_debito).
				totalCredito(total_credito).
				totalDebito(total_debito).
				build();
		
		return s;
		
	}

	public SaldoCentroDTO(String cod_cuenta2, Number number1, Number number2, BigDecimal suma_debito2, BigDecimal suma_credito2,
			BigDecimal total_debito2, BigDecimal total_credito2, Number centro) {
			this.mes = Integer.parseInt(number2.toString());
			this.anno = Integer.parseInt(number1.toString());
			this.cod_cuenta = cod_cuenta2;
			this.centro = Integer.parseInt(centro.toString());
			this.suma_debito = suma_debito2;
			this.suma_credito = suma_credito2;
			this.total_debito = total_debito2;
			this.total_credito = total_credito2;

			
		// TODO Auto-generated constructor stub
	}

	public SaldoCentroDTO(SaldoCentroDeCostos s) {
		
		
		this(s.getElID().getCuenta().getCodCuenta(), s.getElID().getMes().getIdFiscal().getAnno().getElAnno(), s.getElID().getMes().getIdFiscal().getMes(), s.getSumDebito(), s.getSumCredito(), s.getTotalDebito(), s.getTotalCredito(), s.getElID().getCentro().getCodCentro());
	}

}
