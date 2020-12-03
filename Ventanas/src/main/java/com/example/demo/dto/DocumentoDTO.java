package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.demo.model.pojos.Documento;
import com.example.demo.model.pojos.TipoDocumento;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DocumentoDTO {

	@Include
	private long numDocumento;
	
	@Include
	private String tipoDocumento;

	private LocalDate fecha;

	private BigDecimal valorTotal;

	private String concepto;
	
	private String responsable;
	

	public DocumentoDTO(Documento documento) {
		
		tipoDocumento = documento.getIdDocumento().getTipoDocumento().getTipoDoc();
		numDocumento = documento.getIdDocumento().getNumDocumento();
		fecha = documento.getFecha();
		concepto = documento.getConcepto();
		responsable = documento.getResponsable().getNombre();
		valorTotal = documento.getValorTotal();
		
	}
	
	
	
}
