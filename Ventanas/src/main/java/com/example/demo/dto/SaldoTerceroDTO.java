package com.example.demo.dto;

import com.example.demo.model.pojos.*;
import lombok.*;
import lombok.EqualsAndHashCode.Include;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)


public class SaldoTerceroDTO {
	@Include
	private String cod_cuenta;
	@Include
	private Integer anno;
	@Include
	private int mes;
	@Include
	private String tercero;
	private BigDecimal suma_debito;
	private BigDecimal suma_credito;
	private BigDecimal total_debito;
	private BigDecimal total_credito;

	public SaldoTercero convertir() {

		SaldoTercero s = SaldoTercero.builder().
				id(IdSaldoTercero.builder().
						cuenta(Cuenta .builder().
								codCuenta(cod_cuenta).
								build()).
						mes(Mes.builder().
								idFiscal(new MesFiscalId(Anno.builder().
										elAnno(anno.intValue()).build(), mes)).
								build()).
						tercero(Tercero.builder().
								id(tercero).
								build()).
						build()).
				sumCredito(suma_credito).
				sumDebito(suma_debito).
				totalCredito(total_credito).
				totalDebito(total_debito).
				build();

		return s;

	}

	public SaldoTerceroDTO(String cod_cuenta2, Number number1, Number number2, BigDecimal suma_debito2, BigDecimal suma_credito2,
                           BigDecimal total_debito2, BigDecimal total_credito2, String tercero) {
			this.mes = Integer.parseInt(number2.toString());
			this.anno = Integer.parseInt(number1.toString());
			this.cod_cuenta = cod_cuenta2;
			this.tercero = tercero;
			this.suma_debito = suma_debito2;
			this.suma_credito = suma_credito2;
			this.total_debito = total_debito2;
			this.total_credito = total_credito2;


		// TODO Auto-generated constructor stub
	}

	public SaldoTerceroDTO(SaldoTercero s) {
		
		
		this(s.getId().getCuenta().getCodCuenta(), s.getId().getMes().getIdFiscal().getAnno().getElAnno(), s.getId().getMes().getIdFiscal().getMes(), s.getSumDebito(), s.getSumCredito(), s.getTotalDebito(), s.getTotalCredito(), s.getId().getTercero().getId());
	}

}
