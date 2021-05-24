package gei.id.tutelado.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.LazyInitializationException;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Cliente;

public class ClienteDaoJPA implements ClienteDao{
    private EntityManagerFactory emf;
    private EntityManager em;

    @Override
    public void setup (Configuracion config) {
        this.emf = (EntityManagerFactory) config.get("EMF");
    }
    
    @Override
    public Cliente almacena(Cliente cliente){
        try{
            em = emf.createEntityManager();
            em.getTransaction().begin();

            em.persist(cliente);

            em.getTransaction().commit();
            em.close();
        }catch (Exception ex) {
            if (em!=null && em.isOpen()) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                em.close();
                throw(ex);
            }
        }
        return cliente;
    }

    @Override
    public Cliente modifica(Cliente cliente) {

        try {
            
            em = emf.createEntityManager();
            em.getTransaction().begin();

            cliente= em.merge (cliente);

            em.getTransaction().commit();
            em.close();		
            
        } catch (Exception ex ) {
            if (em!=null && em.isOpen()) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                em.close();
                throw(ex);
            }
        }
        return (cliente);
    }

    @Override
    public void elimina(Cliente cliente) {
        try {
            
            em = emf.createEntityManager();
            em.getTransaction().begin();

            Cliente personaTmp = em.find (Cliente.class, cliente.getId());
            em.remove (personaTmp);

            em.getTransaction().commit();
            em.close();
            
        } catch (Exception ex ) {
            if (em!=null && em.isOpen()) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                em.close();
                throw(ex);
            }
        }
    }
    
    @Override
    public Cliente recuperaCodigoCli(String cod) {

        List<Cliente> clientes=null;
        
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            clientes = em.createNamedQuery("Cliente.recuperaPorCodigo", Cliente.class)
            .setParameter("codCliente", cod).getResultList(); 

            em.getTransaction().commit();
            em.close();
        } catch (Exception ex ) {
            if (em!=null && em.isOpen()) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                em.close();
                throw(ex);
            }
        }
        return (clientes.size()==0?null:clientes.get(0));
    }
	@Override
	public Cliente restauraReservas(Cliente cliente) {
		// Devolve o obxecto user coa coleccion de entradas cargada (se non o estaba xa)

		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			try {
				cliente.getReservas().size();
			} catch (Exception ex2) {
				if (ex2 instanceof LazyInitializationException)

				{
					/* OPCION DE IMPLEMENTACION 1 (comentada): Cargar a propiedade "manualmente" cunha consulta, 
					 *  e actualizar tamen "manualmente" o valor da propiedade  */  
					//List<EntradaLog> entradas = (List<EntradaLog>) entityManager.createQuery("From EntradaLog l where l.usuario=:usuario order by dataHora").setParameter("usuario",user).getResultList();
					//user.setEntradasLog (entradas);

					/* OPCION DE IMPLEMENTACIÓN 2: Volver a ligar o obxecto usuario a un novo CP, 
					 * e acceder á propiedade nese momento, para que Hibernate a cargue.*/
					cliente = em.merge(cliente);
					cliente.getReservas().size();

				} else {
					throw ex2;
				}
			}
			em.getTransaction().commit();
			em.close();
		}
		catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}
		
		return (cliente);

	}
}

