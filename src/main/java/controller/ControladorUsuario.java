package controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.util.CollectionUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import business.service.CRUDService;
import entities.Jugador;
import entities.Persona;
import entities.Rol;
import entities.Usuario;


public class ControladorUsuario extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	
	@WireVariable
	protected CRUDService service = (CRUDService) SpringUtil.getBean("CRUDService");

	private List<Persona> personas;
	private List<Jugador> jugadores;
	private List<Usuario> usuarios;
	private List<Rol> roles;
	private Usuario unusuario;
	

	
	//wire components registrar
	@Wire
	Textbox cedula;
	@Wire
	Textbox nombre;
	@Wire
	Textbox apellido;
	@Wire
	Textbox correo;
	@Wire
	Datebox fechanacimiento;
	@Wire
	Textbox clave;
	@Wire
	Textbox confcontrasena;
	@Wire
	Button urlfoto;
	//wire components login
		@Wire
		Textbox usuario;
		@Wire
		Textbox password;
	//Mensajes
	@Wire
	Label message;
		
		public ControladorUsuario(){
			super();
			try{
			//Buscamos y llenamos nuestras listas con la data en nuestras tablas en la base de datos.
			personas = service.getAll(Persona.class);
			usuarios = service.getAll(Usuario.class);
			jugadores = service.getAll(Jugador.class);

			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		@Listen("onClick=#login; onOK=#loginWin")
		public void doLogin(){
			//Login
			String txtUser = usuario.getValue().toString();
			String txtPass = password.getValue().toString();
			
			Session miSession = Sessions.getCurrent();
			
			//Paso 1 Buscar usuario con ese correo: Hay dos formas, 1. con una consulta SQL, 2. En los servicios.
			//Paso 2 si existe validad que la contraseña sea la correcta.
			//Paso 3 setear variable de sesion
			//Paso 4 si es valido redireccionar
			
			Boolean s=false;
					
			for (int i=0;i<usuarios.size();i++){
			
				//Paso 1 Buscar usuario con ese correo
				if(txtUser.equals(usuarios.get(i).getCorreo())) {

					//Paso 2 si existe validad que la contraseña sea la correcta.
					if (txtPass.equals(usuarios.get(i).getContrasena()) && usuarios.get(i).getIdrol() == 1 ){ //si es jugador
						//Paso 3 setear variable de sesion
						miSession.setAttribute("usuario", usuarios.get(i));	
						miSession.setAttribute("idUser", usuarios.get(i).getUsuarioId());	
						System.out.println(usuarios.get(i).getUsuarioId());
						//Paso 4 si es valido redireccionar
						//if it is admin user redirect to dashboard or admin panel
						Messagebox.show("You have Sign in - logged as admin / Has iniciado sesion correctamente como administrador");
						Executions.sendRedirect("/index.zul");					
						s=true;
					}
					else //else if it is a player user redirect to dashboard for player
					if (txtPass.equals(usuarios.get(i).getContrasena()) && usuarios.get(i).getIdrol() == 2){ //si es admin
	
						miSession.setAttribute("usuario", usuarios.get(i));				
						miSession.setAttribute("idUser", usuarios.get(i).getUsuarioId());	
						System.out.println(usuarios.get(i).getUsuarioId());
						Messagebox.show("You have Sign in - logged as player / Has iniciado sesion correctamente como jugador");
						Executions.sendRedirect("/index.zul");		
						s=true;
					}
						
				}
								
			}	
			if (s==false){
				 Messagebox.show("Clave o Usuario Incorrecto, por favor verifique.", "Error", Messagebox.OK, Messagebox.ERROR);
			}			
		}
		
		@Listen("onClick=#saved; onOK=#registrarWin")
		public void saved(){
			//Registrar; Register
			String txtCedula = cedula.getValue().toString();
			String txtNombre= nombre.getValue().toString();
			String txtApellido = apellido.getValue().toString();
			String txtCorreo = correo.getValue().toString();
			String txtClave =clave.getValue().toString();
			String txtConfcontrasena = confcontrasena.getValue().toString();
			
			@SuppressWarnings("deprecation")
			String txtUrlfoto = urlfoto.getUpload();
			
			//Tomando la fecha de nacimiento y dandole formato correcto
			SimpleDateFormat smFechanacimiento = new SimpleDateFormat("dd/MM/yyyy");
			Date txtFechanacimiento = new Date();
			try {
				txtFechanacimiento = smFechanacimiento.parse(fechanacimiento.getText().toString());
			} catch (WrongValueException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//Messagebox.show("URL foto, fecha nacimiento"+txtUrlfoto+" "+txtFechanacimiento.toGMTString().toString());
			
			//Paso 1 validad campos no vacios
			//Paso 2 Valida contraseñas iguales
			if(txtClave.equals(txtConfcontrasena) ){
				//Paso 3 si todo el formulario esta completo guardar
				Persona personac = new Persona(txtCedula, (long)personas.size()+1, txtNombre, txtApellido,txtFechanacimiento, txtUrlfoto, true, txtCorreo);
				service.Save(personac);
				Usuario usuarioc = new Usuario((long)usuarios.size()+1, txtClave, new Date(), true, 2, txtCorreo);
				service.Save(usuarioc);
				Jugador jugadorc = new Jugador((long)jugadores.size()+1, (float)0, new Date(), txtCedula);
				service.Save(jugadorc);	
				Messagebox.show("You have successful sign up / Te has registrado correctamente");
			}else{
				 Messagebox.show("Contreñas no son iguales, por favor verifique.", "Error", Messagebox.OK, Messagebox.ERROR);
			}
			
			
		}
}
