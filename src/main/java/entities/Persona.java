package entities;
// Generated 23/03/2016 01:44:51 AM by Hibernate Tools 4.3.1.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * Persona generated by hbm2java
 */
@Entity
@Table(name = "persona", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = "correo"))
public class Persona implements java.io.Serializable {

	private String cedula;
	private long personaId;
	private String nombre;
	private String apellido;
	private Date fechanacimiento;
	private String foto;
	private Boolean estado;
	private String correo;
	
	private Set<Jugador> jugadors = new HashSet<Jugador>(0);
	private Set<Usuario> usuarios = new HashSet<Usuario>(0);

	public Persona() {
	}

	

	public Persona(String cedula, long personaId, String nombre, String apellido, Date fechanacimiento, String foto,
			Boolean estado, String correo) {
		super();
		this.cedula = cedula;
		this.personaId = personaId;
		this.nombre = nombre;
		this.apellido = apellido;
		this.fechanacimiento = fechanacimiento;
		this.foto = foto;
		this.estado = estado;
		this.correo = correo;
	}

	@Id

	@Column(name = "cedula", unique = true, nullable = false)
	public String getCedula() {
		return this.cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	@Column(name = "persona_id", nullable = false)
	public long getPersonaId() {
		return this.personaId;
	}

	public void setPersonaId(long personaId) {
		this.personaId = personaId;
	}

	@Column(name = "nombre", nullable = false)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "apellido", nullable = false)
	public String getApellido() {
		return this.apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fechanacimiento", nullable = false, length = 29)
	public Date getFechanacimiento() {
		return this.fechanacimiento;
	}

	public void setFechanacimiento(Date fechanacimiento) {
		this.fechanacimiento = fechanacimiento;
	}

	@Column(name = "foto")
	public String getFoto() {
		return this.foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	@Column(name = "estado")
	public Boolean getEstado() {
		return this.estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	@Column(name = "correo", unique = true, nullable = false)
	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "persona")
	public Set<Jugador> getJugadors() {
		return this.jugadors;
	}

	public void setJugadors(Set<Jugador> jugadors) {
		this.jugadors = jugadors;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "persona")
	public Set<Usuario> getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Set<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

}