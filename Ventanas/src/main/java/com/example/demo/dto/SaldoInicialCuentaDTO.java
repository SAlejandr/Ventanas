package com.example.demo.dto;

import com.example.demo.model.pojos.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)


public class SaldoInicialCuentaDTO {
	@Include
	private String cod_cuenta;
	@Include
	private Integer anno;
	private BigDecimal debito;
	private BigDecimal credito;

	public SaldoInicialCuenta convertir() {

		SaldoInicialCuenta s = SaldoInicialCuenta.builder().
				id(IdSaldoInicial.builder().
						anno(Anno.builder().
								elAnno(anno).
								build()).
						cuenta(Cuenta.builder().
							codCuenta(cod_cuenta).
							build()).
						build()).
				credito(credito).
				debito(debito).
				build();

		return s;

	}

	public SaldoInicialCuentaDTO(String cod_cuenta2, Number number1, Number number2, BigDecimal debito2, BigDecimal credito2){

			this.anno = Integer.parseInt(number1.toString());
			this.cod_cuenta = cod_cuenta2;
			this.debito = debito2;
			this.credito = credito2;



		// TODO Auto-generated constructor stub
	}

	public SaldoInicialCuentaDTO(SaldoInicialCuenta s) {


		this(s.getId().getCuenta().getCodCuenta(), s.getId().getAnno().getElAnno(), s.getDebito(), s.getCredito());
	}

}
