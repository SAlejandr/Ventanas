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


public class SaldoInicialCentroDTO {
	@Include
	private String cod_cuenta;
	@Include
	private Integer anno;
	@Include
	private int centro;
	private BigDecimal debito;
	private BigDecimal credito;

	public SaldoInicialCentroDeCostos convertir() {

		SaldoInicialCentroDeCostos s = SaldoInicialCentroDeCostos.builder().
				elId(IdSaldoInCCostos.builder().
						idSaldoIn(IdSaldoInicial.builder().
								cuenta(Cuenta.builder().
									codCuenta(cod_cuenta).
									build()).
								anno(Anno.builder().
									elAnno(anno).
									build()).
						build()).
						centro(CentroDeCosto.builder().
								codCentro(centro).
								build()).
						build()).
				credito(credito).
				debito(debito).
				build();

		return s;

	}

	/*public SaldoInicialCentroDTO(String cod_cuenta2, Number number1, Number number2, BigDecimal debito2, BigDecimal credito2){

			this.anno = Integer.parseInt(number1.toString());
			this.cod_cuenta = cod_cuenta2;
			this.centro = Integer.parseInt(number2.toString());
			this.debito = debito2;
			this.credito = credito2;



		// TODO Auto-generated constructor stub
	}*/

	public SaldoInicialCentroDTO(SaldoInicialCentroDeCostos s) {


		this(s.getElId().getIdSaldoIn().getCuenta().getCodCuenta(), s.getElId().getIdSaldoIn().getAnno().getElAnno(), s.getElId().getCentro().getCodCentro(), s.getDebito(), s.getCredito());
	}

}
