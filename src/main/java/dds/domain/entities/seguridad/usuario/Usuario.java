package dds.domain.entities.seguridad.usuario;

import dds.db.EntityManagerHelper;
import dds.domain.entities.asociacion.Asociacion;
import dds.domain.entities.persona.Persona;
import dds.domain.entities.seguridad.validador.ValidadorPassword;
import dds.servicios.helpers.DateHelper;
import dds.servicios.helpers.HashHelper;



import javax.persistence.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.security.*;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
@Table
@DiscriminatorColumn(name = "tipo_usuario")
public class Usuario {
    public Usuario() {
    }

    @Id
    private String userName;

    @Column
    private String password;
    @Column
    private Date lastPasswordDT;
    @Column
    private Integer intentosFallidos;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "usedPassword")
    private List<String> usedPasswords = new ArrayList<>();

    @Column
    private Boolean isBlocked;


    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "asociacion_id")
    private Asociacion asociacion;

    public Usuario (String userName, String password) throws NoSuchAlgorithmException{
        this.userName = userName;
        ValidadorPassword.getValidadorPassword().validarPassword(password,this);
        this.isBlocked = false;
        this.intentosFallidos= 0;
        this.password = HashHelper.getHashHelper().passwordAMD5(password);
        this.usedPasswords.add(this.password);
        setLastPasswordDT(LocalDateTime.now(ZoneOffset.UTC));
    }


    public Usuario (String userName, String password, Asociacion asociacion) throws NoSuchAlgorithmException{
        this.userName = userName;
        ValidadorPassword.getValidadorPassword().validarPassword(password,this);
        this.isBlocked = false;
        this.intentosFallidos= 0;
        this.password = HashHelper.getHashHelper().passwordAMD5(password);
        this.usedPasswords.add(this.password);
        this.asociacion = asociacion;
        setLastPasswordDT(LocalDateTime.now(ZoneOffset.UTC));
    }
    public Boolean soyAdmin(){
        return false;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> lastPasswords() {
        int cant = 5;
        if (usedPasswords.size() < 5) {
            cant = usedPasswords.size();
        }
        return this.usedPasswords.subList(usedPasswords.size() - cant, usedPasswords.size());
    }

    public void changePassword(String newPassword) throws NoSuchAlgorithmException {
        ValidadorPassword.getValidadorPassword().validarPassword(newPassword, this);
        setPassword(HashHelper.getHashHelper().passwordAMD5(newPassword));
        addUsedPassword(HashHelper.getHashHelper().passwordAMD5(newPassword));
        setLastPasswordDT(LocalDateTime.now(ZoneOffset.UTC));
    }

    public Boolean passwordVencida(){
        return lastPasswordDT.before(DateHelper.getHelper().LocalDateTimeToDate(LocalDateTime.now(ZoneOffset.UTC).minusDays(30)));
    }

    // Metodos de bloqueado
    public void bloquear() {
        isBlocked = true;
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.entityManager().merge(this);
        EntityManagerHelper.commit();
    }
    public void desbloquear() {
        isBlocked = false;
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.entityManager().merge(this);
        EntityManagerHelper.commit();
    }
    public boolean estaBloqueado() {
        return isBlocked;
    }


    public void addUsedPassword(String newPassword) {
        this.usedPasswords.add(newPassword);
    }

    public void setLastPasswordDT(LocalDateTime newLastPasswordDT){
        this.lastPasswordDT = DateHelper.getHelper().LocalDateTimeToDate(newLastPasswordDT);
    }

    //Va a servir al momento del logueo
    public void verificarIntentosFallidos() {
        if (intentosFallidos == 3) {
            this.bloquear();
            EntityManagerHelper.beginTransaction();
            EntityManagerHelper.entityManager().merge(this);
            EntityManagerHelper.commit();
        }
    }
    public void setIntentosFallidos(int intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }
    public void sumaIntentoFallido() {
        this.intentosFallidos += 1;
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.entityManager().merge(this);
        EntityManagerHelper.commit();
    }

    public Persona getPersona() {
        return null;
    }

    public Asociacion getAsociacion() {
        return asociacion;
    }

    public void setAsociacion(Asociacion asociacion) {
        this.asociacion = asociacion;

    }
}
