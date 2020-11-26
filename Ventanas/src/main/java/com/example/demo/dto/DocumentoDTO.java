package com.example.demo.dto;

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
	
	private String tipoDoc;

	private LocalDate fecha;

	private float valorTotal;

	private String concepto;
	
	private String responsable;
	

	public DocumentoDTO(Documento documento) {
		
		numDocumento = documento.getIdDocumento().getNumDocumento();
		tipoDocumento = documento.getIdDocumento().getTipoDocumento().getTipoDoc();
		tipoDoc = documento.getIdDocumento().getTipoDocumento().getNombreDocumento();
		fecha = documento.getFecha();
		responsable = documento.getResponsable().getNombre();
		valorTotal = documento.getValorTotal();
		concepto = documento.getConcepto();
		
	}
	
	
	
}
