package gei.id.tutelado.model;

import java.time.LocalDate;

import javax.persistence.*;

@TableGenerator(name="generadorIdsReserva", table="tabla_ids",
pkColumnName="nombre_id", pkColumnValue="idReserva",
valueColumnName="ultimo_valor_id",
initialValue=0, allocationSize=1)

// El Id funcionará como atributo subrogado, en este caso se genera automaticamente
// por Hivernate
// Las querys las hacemos por la clave natural, en este caso el codigo de la reserva

@NamedQueries ({
	@NamedQuery (name="Reserva.recuperaPorCodigo",
				 query="SELECT r FROM Reserva r WHERE r.codReserva=:codReserva"),
	@NamedQuery (name="Reserva.recuperaTodos",
				query="SELECT r FROM Reserva r ORDER BY r.codReserva"),
	@NamedQuery (name="Reserva.filtroFecha",
				query="Select r FROM Reserva r WHERE r.fechaIni=:fechaIni ORDER BY r.fechaIni"),
	@NamedQuery (name="Reserva.filtroNombre",
				query="Select r FROM Reserva r WHERE r.nomAlbergue=:nomAlbergue ORDER BY r.nomAlbergue")
})

@Entity
public class Reserva {
	@Id
	@GeneratedValue (generator="generadorIdsReserva")
	private Long id;
	
	@Column(nullable = false, unique = true)
    private String codReserva;
	
	@Column(nullable = false, unique = false)
    private String nomAlbergue;
	
	@Column(nullable = false, unique=false)
    private LocalDate fechaIni;
	
	@Column(nullable = false, unique=false)
    private LocalDate fechaFin;
	
	@ManyToOne (cascade={}, fetch=FetchType.EAGER)
	@JoinColumn(name="cliente",nullable=false)
	private Cliente cliente;
	// Getters & Setters
	public Long getId() {
		return id;
	}
	
	public String getCodReserva() {
		return codReserva;
	}
	
	public String getNomAlbergue() {
		return nomAlbergue;
	}
		
	public LocalDate getFechaIni() {
		return fechaIni;
	}
	
	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public Cliente getCliente() {
		return cliente;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setCodReserva(String codReserva) {
		this.codReserva = codReserva;
	}
	
	public void setNomAlbergue(String nomAlbergue) {
		this.nomAlbergue = nomAlbergue;
	}
	
	public void setFechaIni(LocalDate fechaIni) {
		this.fechaIni = fechaIni;
	}
	
	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codReserva == null) ? 0 : codReserva.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reserva other = (Reserva) obj;
		if (codReserva == null) {
			if (other.codReserva != null)
				return false;
		} else if (!codReserva.equals(other.codReserva))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Reserva [id=" + id + ", codReserva=" + codReserva + ", nomAlbergue=" + nomAlbergue + ", fechaIni="
				+ fechaIni + ", fechaFin=" + fechaFin + "]";
	}
	
}