package dds.domain.entities.persona;

import dds.db.EntityManagerHelper;
import dds.db.RepositorioMascotas;
import dds.db.repositorioException.LogicRepoException;
import dds.domain.entities.mascota.Mascota;
import dds.domain.entities.persona.personaException.TransactionException;
import dds.domain.entities.persona.roles.Duenio;
import dds.domain.entities.persona.roles.RolPersona;
import dds.domain.entities.persona.transaccion.Transaccion;
import dds.servicios.avisos.FormaNotificacion;
import dds.servicios.avisos.Notificador;
import dds.servicios.helpers.DateHelper;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "persona")
public class Persona {
    public Persona() {
    }

    @Id
    @Column (name = "idPersona")
    private String idPersona;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Mascota> mascotas = new ArrayList<>();

    @Column (columnDefinition = "DATE")
    private Date fechaNac;

    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDoc;

    @Column
    private Integer nroDoc;

    @Column
    private String direccion;

    @ManyToMany(cascade = {CascadeType.ALL})
    private List<RolPersona> listaRoles = new ArrayList<>();

    @OneToOne (cascade = {CascadeType.ALL})
    @JoinColumn(name = "notificador_id")
    private Notificador notificador;

    public Persona(String nombre, String apellido, List<Mascota> mascotas, List<RolPersona> listaRoles, Notificador notificador) {
        this.idPersona = UUID.randomUUID().toString().replace("-", "");
        this.mascotas = new ArrayList<>();
        this.mascotas.addAll(mascotas);
        this.listaRoles = new ArrayList<>();
        this.listaRoles.addAll(listaRoles);
        this.notificador = notificador;
        notificador.getContactos().get(0).setNombre(nombre);
        notificador.getContactos().get(0).setApellido(apellido);
    }
    //Alta de persona que encontro a su mascota
    public Persona(String nombre, String apellido,TipoDocumento tipoDoc,Integer nroDoc,LocalDate fechaNac,String direccion,String telefono, String email,List<FormaNotificacion> formasDeNoti) {
        this.idPersona = UUID.randomUUID().toString().replace("-", "");
        this.tipoDoc = tipoDoc;
        this.nroDoc = nroDoc;
        listaRoles = new ArrayList<>();
        this.listaRoles.add(Duenio.getDuenio());
        this.fechaNac = DateHelper.getHelper().LocalDateToDate(fechaNac);
        this.direccion = direccion;
        this.mascotas = new ArrayList<>();
        this.notificador = new Notificador();
        notificador.agendarContacto(nombre,apellido,telefono,email,formasDeNoti);

    }



    public String getNombre() {
        return notificador.getContactos().get(0).getNombre();
    }
    public String getApellido() {
        return notificador.getContactos().get(0).getApellido();
    }

    public List<Mascota> getMascotas() {
        return RepositorioMascotas.getRepositorio().getMascotasXPersonaId(this.idPersona);
    }

    public void agregarMascota(Mascota mascota){
        this.mascotas.add(mascota);
    }
    public Notificador getNotificador() {
        return notificador;
    }

    public void setIdPersona(String idPersona) {
        this.idPersona = idPersona;
    }

    public Date getFechaNac() {
        return fechaNac;
    }

    public TipoDocumento getTipoDoc() {
        return tipoDoc;
    }

    public Integer getNroDoc() {
        return nroDoc;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getIdPersona() {
        return idPersona;
    }

    public Mascota getMascota(String idMascota){
        if(RepositorioMascotas.getRepositorio().esIDValido(idMascota)){
            String jql = "Select m from Persona p, Mascota m where m.idMascota = :idMascota and p.idPersona = :idPersona";
            Mascota mascota = (Mascota) EntityManagerHelper.getEntityManager().createQuery(jql).
                    setParameter("idMascota",idMascota).setParameter("idPersona",this.idPersona).getResultList().get(0);
            return  mascota;

        }else {
            throw new LogicRepoException("El dueño no posee este id_mascota");
        }
    }

    public List<RolPersona> getListaRoles() {
        return listaRoles;
    }
    public void ejecutarTransaccion(Transaccion transaccion)  {
        int i = 1;
        for(RolPersona rol: listaRoles){
            try{
                rol.ejecutarTransaccion(transaccion);
                return;
            }catch(Exception e){
                if (i>=listaRoles.size()){
                    throw new TransactionException("No posee los permisos para ejecutar la transaccion");
                }
            }
            i = i+1;
        }
    }

    public String getEmail() {
        return this.notificador.getContactos().get(0).getEmail();
    }
    public String getTelefono() {
        return this.notificador.getContactos().get(0).getTelefono();
    }
    public void agregarRol(RolPersona rol){
        this.listaRoles.add(rol);
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().merge(this);
        EntityManagerHelper.commit();
    }

    public void setMascotas(List<Mascota> mascotas) {
        this.mascotas = mascotas;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }

    public void setTipoDoc(TipoDocumento tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public void setNroDoc(Integer nroDoc) {
        this.nroDoc = nroDoc;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setListaRoles(List<RolPersona> listaRoles) {
        this.listaRoles = listaRoles;
    }

    public void setNotificador(Notificador notificador) {
        this.notificador = notificador;
    }
}
