import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import javax.swing.JFileChooser;

/**
 * Cliente
 */
public class Cliente {

    public static void MandarArchivo() {

        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

                File f = new File("./prueba/prueba.obj");

                Socket cliente = new Socket("localhost", 50000);
                String archivo = f.getAbsolutePath();
                String nombre = f.getName();
                long tam = f.length();

                DataOutputStream salida = new DataOutputStream(cliente.getOutputStream());
                DataInputStream entrada = new DataInputStream(new FileInputStream(archivo));

                salida.writeUTF(nombre);
                salida.flush();

                salida.writeLong(tam);
                salida.flush();

                byte[] b = new byte[1024];
                long enviados = 0;
                int porcentaje, n;

                while (enviados < tam) {

                    n = entrada.read(b);
                    salida.write(b, 0, n);
                    salida.flush();

                    enviados += n;
                    porcentaje = (int) (enviados * 100 / tam);
                    System.out.println("enviado: " + porcentaje + "%\r");

                }
                System.out.println("\n\nArchivo enviado");
                salida.close();
                entrada.close();
                cliente.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {

        //aqui se crea el objeto para poder hacer que tenga un valor con atributos llenos de datos.
        PruebaEjercicio prue = new PruebaEjercicio("Ethan", "Mexico", 21, 117, 1.81, false, true);

        System.out.println(prue.getNombre() + " " + prue.getCiudad() + " " + prue.getEdad() + " " + prue.getNum() + " "
                + prue.getTam() + " " + prue.isBan() + " " + prue.isSig());

        //inicio de la serializacion del objeto donde se tiene en cuenta que puede fallar, por eso el el try.
        try {

            ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(
                    "C:/Users/eevae/OneDrive/Documentos/GitHub/Administracio_Servicios_Red/Practica3/prueba/prueba.obj"));
            salida.writeObject(prue);
            salida.close();

            ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(
                    "C:/Users/eevae/OneDrive/Documentos/GitHub/Administracio_Servicios_Red/Practica3/prueba/prueba.obj"));

            PruebaEjercicio prueRec = (PruebaEjercicio) entrada.readObject();
            System.out.println(prueRec.getNombre() + " " + prueRec.getCiudad() + " " + prueRec.getEdad() + " "
                    + prueRec.getNum() + " " + prueRec.getTam() + " " + prueRec.isBan() + " " + prueRec.isSig());

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        MandarArchivo();

    }
}