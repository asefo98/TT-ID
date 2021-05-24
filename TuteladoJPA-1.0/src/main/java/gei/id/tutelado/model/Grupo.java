package gei.id.tutelado.model;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@NamedQueries ({
	@NamedQuery (name="Grupo.recuperaTodos",
				 query="SELECT g FROM Grupo g"),
	@NamedQuery (name="Grupo.recuperaPorCodigo",
	query="SELECT g FROM Grupo g WHERE g.codCliente=:codCliente")
})

@Entity
@Table(name="Grupo")
public class Grupo extends Cliente {

	@Column(unique = false, nullable = false)
	private Integer num_integrantes;

	@ManyToMany
	@JoinTable(name="CompGrup")
	private Set<Persona> Integrantes = new HashSet<Persona>();
	// Integrantes []

	public Integer getNum_integrantes(){
		return num_integrantes;
	}
	public Set<Persona> getIntegrantes(){
		return Integrantes;
	}
	public void setNum_integrantes(Integer num_integrantes){
		this.num_integrantes = num_integrantes;
	}
	public void setIntegrantes(Set<Persona> Integrantes){
		this.Integrantes = Integrantes;
	}
}

