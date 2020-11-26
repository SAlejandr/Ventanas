package com.example.demo.dto;

import com.example.demo.model.pojos.Cuenta;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CuentaDTO {

	private String codCuenta;
	private String nombre;
	private String codCuentaS;
	private String codConfig;
	private boolean tercero;
	private boolean centroCostos;
	private boolean movimientos;

	public CuentaDTO(Cuenta c) {

		codCuenta = c.getCodCuenta();
		nombre = c.getNombre();
		codConfig = c.getClaseCuenta().getCodConfig();

		tercero = c.isTerceros();
		centroCostos = c.isCcostos();
		movimientos = c.isMovimientos();
		codCuentaS = c.getCuentaSuperior();

	}

}
