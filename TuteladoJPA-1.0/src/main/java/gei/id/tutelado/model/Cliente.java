package gei.id.tutelado.model;

import javax.persistence.*;

@TableGenerator(name="generadorIdsCliente", table="tabla_ids",
pkColumnName="nombre_id", pkColumnValue="idCliente",
valueColumnName="ultimo_valor_id",
initialValue=0, allocationSize=1)

@Entity
@Inheritance (strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class Cliente {

	@Id
	@GeneratedValue(generator="generadorIdsCliente")
	private Long id;
	
	@Column(unique = true, nullable = false)
    private String codCliente;
	
	@Column(unique = true, nullable = false)
    private String email;
	
	@Column(unique = true, nullable = false)
    private String tlf;
	
	@Column(unique = true, nullable = false)
    private String numTarjeta;
	
	//Reservas [] : Reserva
	
    public Long getId() {
		return id;
	}
    
	public String getCodCliente() {
		return codCliente;
	}
	public String getEmail() {
		return email;
	}
	
	public String getTlf() {
		return tlf;
	}
	
	public String getNumTarjeta() {
		return numTarjeta;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public void setCodCliente(String codCliente) {
		this.codCliente = codCliente;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setTlf(String tlf) {
		this.tlf = tlf;
	}

	public void setNumTarjeta(String numTarjeta) {
		this.numTarjeta = numTarjeta;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codCliente == null) ? 0 : codCliente.hashCode());
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
		Cliente other = (Cliente) obj;
		if (codCliente == null) {
			if (other.codCliente != null)
				return false;
		} else if (!codCliente.equals(other.codCliente))
			return false;
		return true;
	}
	
}
