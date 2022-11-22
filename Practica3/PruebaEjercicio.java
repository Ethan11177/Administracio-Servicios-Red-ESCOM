import java.io.Serializable;

/**
 * PruebaEjercicio
 */
public class PruebaEjercicio implements Serializable {

    String nombre;
    transient String ciudad;
    int edad;
    double num;
    transient double tam;
    boolean ban;
    transient boolean sig;

    public PruebaEjercicio(String nombre, String ciudad, int edad, double num, double tam, boolean ban, boolean sig) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.edad = edad;
        this.num = num;
        this.tam = tam;
        this.ban = ban;
        this .sig = sig;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public int getEdad() {
        return edad;
    }

    public double getNum() {
        return num;
    }

    public double getTam() {
        return tam;
    }

    public boolean isBan() {
        return ban;
    }

    public boolean isSig() {
        return sig;
    }
}