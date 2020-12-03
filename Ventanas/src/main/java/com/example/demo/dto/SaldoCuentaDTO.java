package com.example.demo.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.example.demo.model.pojos.SaldoCuenta.SaldoCuentaBuilder;
import com.example.demo.model.pojos.Anno;
import com.example.demo.model.pojos.Cuenta;
import com.example.demo.model.pojos.MesFiscalId;
import com.example.demo.model.pojos.IdSaldo;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)


public class SaldoCuentaDTO {
	@Include
	private String cod_cuenta;
	@Include
	private Integer anno;
	@Include
	private int mes;
	private BigDecimal suma_debito;
	private BigDecimal suma_credito;
	private BigDecimal saldo_debito;
	private BigDecimal saldo_credito;
	
	public SaldoCuenta convertir() {
		
		SaldoCuenta s = SaldoCuenta.builder().
				id(IdSaldo.builder().
						cuenta(Cuenta .builder().
								codCuenta(cod_cuenta).
								build()).
						mes(Mes.builder().
								idFiscal(new MesFiscalId(Anno.builder().
										elAnno(anno.intValue()).build(), mes)).
								build()).
						build()).
				sumCredito(saldo_credito).
				sumDebito(saldo_debito).
				totalCredito(suma_credito).
				totalDebito(suma_debito).
				build();
		
		return s;
		
	}

	public SaldoCuentaDTO(String cod_cuenta2, Number number1, Number number2, BigDecimal suma_debito2, BigDecimal suma_credito2,
			BigDecimal saldo_debito2, BigDecimal saldo_credito2) {
			this.mes = Integer.parseInt(number2.toString());
			this.anno = Integer.parseInt(number1.toString());
			this.cod_cuenta = cod_cuenta2;
			this.suma_debito = suma_debito2;
			this.suma_credito = suma_credito2;
			this.saldo_debito = saldo_debito2;
			this.saldo_credito = saldo_credito2;

			
		// TODO Auto-generated constructor stub
	}

	public SaldoCuentaDTO(SaldoCuenta s) {
		
		
		this(s.getId().getCuenta().getCodCuenta(), s.getId().getMes().getIdFiscal().getAnno().getElAnno(), s.getId().getMes().getIdFiscal().getMes(), s.getSumDebito(), s.getSumCredito(), s.getTotalDebito(), s.getTotalCredito());
	}

}
