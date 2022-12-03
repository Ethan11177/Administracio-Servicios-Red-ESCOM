import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import javax.security.auth.callback.TextInputCallback;

import java.awt.Desktop; //Desktop.getDesktop().open(objetofile); para abir las imagenes

/**
 * Cliente
 */
public class Cliente {

    public static Carrito[] ListarProductos(Productos[] objPro) throws IOException, InterruptedException {

        String seguir;
        Scanner teclado = new Scanner(System.in);
        int idArr = 0, cantArr = 0, preArr = 0;
        Carrito[] tempCar = new Carrito[100];
        int j = 0, ban2 = 0;

        for (int i = 0; i < tempCar.length; i++) {
            tempCar[i] = new Carrito(0, 0, 0);
        }

        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

        for (int i = 0; i < objPro.length; i++) {

            if (objPro[i].getExistencia() > 0) {

                File f = new File("archivosCliente\\" + objPro[i].getNombre() + ".jpg");

                System.out.println("No. Productos: " + objPro[i].getId());
                System.out.println("Nombre Producto: " + objPro[i].getNombre());
                System.out.println("Descripcion Producto: " + objPro[i].getDescripcion());
                System.out.println("Precio Producto: " + objPro[i].getPrecio());
                System.out.println("Existencias: " + objPro[i].getExistencia());
                Desktop.getDesktop().open(f);
                System.out.println();

            }

        }

        for (;;) {
            int ban = 1;

            System.out.println("Que producto es el que quiere (Escribir la ID): ");
            idArr = teclado.nextInt();

            for (int i = 0; i < tempCar.length; i++) {
                if (tempCar[i].getId_compra() == idArr && tempCar[i].getCantidad() != 0) {
                    System.out.println("Ya esta este producto, cuantas unidades desea ahora: ");
                    cantArr = teclado.nextInt();
                    if (cantArr <= objPro[idArr].getExistencia() && cantArr > 0) {
                        tempCar[i].setCantidad(cantArr);
                        tempCar[i].setPrecio_t(cantArr * objPro[idArr].getPrecio());
                    } else {
                        System.out
                                .println("Cantidad maxima de compra alcanzada producto: " + objPro[idArr].getNombre());
                    }
                    ban2 = 1;
                    break;
                }
            }

            if (idArr <= (objPro.length) && idArr > -1 && ban2 == 0) {
                System.out.println("Cuantas unidades quiere del producto: " + objPro[idArr].getNombre());
                cantArr = teclado.nextInt();

                if (cantArr < objPro[idArr].getExistencia() && objPro[idArr].getExistencia() != 0) {

                    preArr = cantArr * objPro[idArr].getPrecio();
                    System.out.println("Precio 1/u: " + objPro[idArr].getPrecio() + " Precio total: " + preArr);
                    tempCar[j] = new Carrito(idArr, cantArr, preArr);
                    j++;
                } else {
                    System.out.println("Cantidad maxima de compra alcanzada producto: " + objPro[idArr].getNombre());
                }
            } else {
                if (ban2 == 0) {
                    System.out.println("Error id incorecto");
                }
            }

            System.out.println("Quiere agregar mas prductos al carrito:\n1. si\n2. no");
            ban = teclado.nextInt();

            if (ban == 2) {

                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

                Carrito[] objCar = new Carrito[j];

                for (int i = 0; i < j; i++) {
                    objCar[i] = tempCar[i];
                    System.out.println("Id objeto: " + objCar[i].getId_compra() + "\nNombre producto:  "
                            + objPro[objCar[i].getId_compra()].getNombre() + "\nPrecio: " + objCar[i].getPrecio_t()
                            + "\nCantidad: " + objCar[i].getCantidad() + "\n");

                }

                System.out.println("Press Enter key to continue...");
                seguir = teclado.next();
                return objCar;
            }
        }
    }

    public static Carrito[] ModificarCarrito(Productos[] objPro, Carrito[] objCar)
            throws NumberFormatException, IOException, InterruptedException {

        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int idP = 0, cantP = 0;
        String conti;

        for (int i = 0; i < objCar.length; i++) {
            System.out.println("Id objeto: " + objCar[i].getId_compra() + "\nNombre producto:  "
                    + objPro[objCar[i].getId_compra()].getNombre() + "\nPrecio: " + objCar[i].getPrecio_t()
                    + "\nCantidad: " + objCar[i].getCantidad() + "\n");
        }

        System.out.println("ID del producto que desea modificar: ");
        idP = Integer.parseInt(br.readLine());

        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

        for (int i = 0; i < objCar.length; i++) {
            if (idP == objCar[i].getId_compra()) {
                System.out.println("Id objeto: " + objCar[i].getId_compra() + "\nNombre producto:  "
                        + objPro[objCar[i].getId_compra()].getNombre() + "\nPrecio: " + objCar[i].getPrecio_t()
                        + "\nCantidad: " + objCar[i].getCantidad() + "\n");

                System.out.println("Cual es la nueva cantidad que desea");
                cantP = Integer.parseInt(br.readLine());

                if (objPro[objCar[i].getId_compra()].getExistencia() >= cantP && cantP != 0) {
                    objCar[i].setCantidad(cantP);
                    objCar[i].setPrecio_t(cantP * objPro[objCar[i].getId_compra()].getPrecio());
                } else if (cantP == 0) {

                } else {
                    System.out.println("La cantidad no es valida");
                }
                break;
            }
        }

        for (int i = 0; i < objCar.length; i++) {
            System.out.println("Id objeto: " + objCar[i].getId_compra() + "\nNombre producto:  "
                    + objPro[objCar[i].getId_compra()].getNombre() + "\nPrecio: " + objCar[i].getPrecio_t()
                    + "\nCantidad: " + objCar[i].getCantidad() + "\n");
        }

        System.out.println("Presione una tecla para continuar...");
        conti = br.readLine();

        return objCar;
    }

    public static Carrito[] EliminarCarrito(Productos[] objPro, Carrito[] objCar)
            throws InterruptedException, IOException {

        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int idP = 0, ban = 0, j = 0;
        String conti;

        for (int i = 0; i < objCar.length; i++) {
            System.out.println("Id objeto: " + objCar[i].getId_compra() + "\nNombre producto:  "
                    + objPro[objCar[i].getId_compra()].getNombre() + "\nPrecio: " + objCar[i].getPrecio_t()
                    + "\nCantidad: " + objCar[i].getCantidad() + "\n");
        }

        System.out.println("Que elemento desea eliminar (escriba el ID): ");
        idP = Integer.parseInt(br.readLine());

        for (int i = 0; i < objCar.length; i++) {
            if (idP == objCar[i].getId_compra()) {
                System.out.println("\nId objeto: " + objCar[i].getId_compra() + "\nNombre producto:  "
                        + objPro[objCar[i].getId_compra()].getNombre() + "\nPrecio: " + objCar[i].getPrecio_t()
                        + "\nCantidad: " + objCar[i].getCantidad() + "\n");

                System.out.println("Seguro que quiere eliminar el elemento del carrito\n1. Si\n2. No");
                ban = Integer.parseInt(br.readLine());

                if (ban == 1) {
                    objCar[i] = new Carrito(0, 0, 0);
                }

                break;

            }
        }

        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

        for (int i = 0; i < objCar.length; i++) {

            if (objCar[i].getCantidad() > 0) {
                j++;
            }

        }

        Carrito[] tmpCar = new Carrito[j];
        j = 0;

        for (int i = 0; i < objCar.length; i++) {

            if (objCar[i].getCantidad() > 0) {
                System.out.println("\nId objeto: " + objCar[i].getId_compra() + "\nNombre producto:  "
                        + objPro[objCar[i].getId_compra()].getNombre() + "\nPrecio: " + objCar[i].getPrecio_t()
                        + "\nCantidad: " + objCar[i].getCantidad() + "\n");

                tmpCar[j] = new Carrito(objCar[i].getId_compra(), objCar[i].getCantidad(), objCar[i].getPrecio_t());
                j++;
            }

        }

        System.out.println("Presione una tecla para continuar...");
        conti = br.readLine();

        return tmpCar;
    }

    public static void MenuCliente(Socket cliente, Productos[] objPro) throws Exception {

        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            int opcion = 0, ban = 0;
            ;
            String conti;
            Carrito[] objCar = new Carrito[100];

            while (opcion != 5) {

                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

                System.out.println("////////////////////Menu Cliente//////////////////////////");
                System.out.println("1. Agregar productos al carrito");
                System.out.println("2. Modificar pedido");
                System.out.println("3. Eliminar producto"); // se tiene que generar un metodo que elimine el objeto en
                                                            // base a la id que se quiere, une ves eso generamos un
                                                            // objeto temporal y regresamos este al objeto original para
                                                            // la modificacion
                System.out.println("4. Hacer pedido");
                System.out.println("5. Salir");
                opcion = Integer.parseInt(br.readLine());

                switch (opcion) {
                    case 1:
                        objCar = ListarProductos(objPro);
                        ban = 1;
                        break;

                    case 2:
                        if (ban == 1) {
                            objCar = ModificarCarrito(objPro, objCar);

                        } else {
                            System.out.println("Aun no tiene un carrito de compras...");
                            conti = br.readLine();
                        }
                        break;

                    case 3:
                        if (ban == 1) {
                            objCar = EliminarCarrito(objPro, objCar);

                        } else {
                            System.out.println("Aun no tiene un carrito de compras...");
                            conti = br.readLine();
                        }
                        break;

                    case 4:

                        if (ban == 1) {

                            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

                            for (int i = 0; i < objCar.length; i++) {
                                System.out.println("Id objeto: " + objCar[i].getId_compra() + "\nNombre producto:  "
                                        + objPro[objCar[i].getId_compra()].getNombre() + "\nPrecio: "
                                        + objCar[i].getPrecio_t()
                                        + "\nCantidad: " + objCar[i].getCantidad() + "\n");
                            }

                            System.out.println("Quiere continuar con la compra\n1. Si\n2. No");
                            opcion = Integer.parseInt(br.readLine());

                            int suma = 0;
                                    
                            if (opcion == 1) {

                                for (int i = 0; i < objCar.length; i++) {
                                    int res = objPro[objCar[i].getId_compra()].getExistencia()
                                            - objCar[i].getCantidad();
                                    objPro[objCar[i].getId_compra()].setExistencia(res);
                                    suma += objCar[i].getPrecio_t();

                                }

                                System.out.println("Precio final: " + suma);

                                SerializacionObjeto(objPro);

                                EnviarArchivo(cliente, objPro);

                                System.out.println("Gracias por su compra");
                                conti = br.readLine();
                                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                            }

                        } else {
                            System.out.println("Aun no tiene un carrito de compras...");
                            conti = br.readLine();
                        }

                        break;

                    case 5:
                        System.out.println("Gracias");
                        break;

                    default:
                        break;
                }
            }
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    public static void EnviarArchivo(Socket cliente, Productos[] objPro) throws Exception {
        File arcSe;

        arcSe = new File("archivosCliente\\objetoC.txt");

        String archivo = arcSe.getAbsolutePath();
        String nombre = arcSe.getName();
        long tam = arcSe.length();

        DataOutputStream salida = new DataOutputStream(cliente.getOutputStream());
        DataInputStream entrada = new DataInputStream(new FileInputStream(archivo));

        salida.writeUTF(nombre);
        salida.flush();

        salida.writeLong(tam);
        salida.flush();

        byte[] b = new byte[1024];
        long enviados = 0;
        int porcentaje = 0, n = 0;

        while (enviados < tam) {

            n = entrada.read(b);
            salida.write(b, 0, n);
            salida.flush();

            enviados += n;
            porcentaje = (int) (enviados * 100 / tam);
            // System.out.println("enviado: " + porcentaje + "%\r");

        }
        // System.out.println("\nArchivo enviado " + nombre + "\n");

        salida.flush();

        Thread.sleep(500);
    }

    public static void RecibirArchivo(Socket cliente) throws Exception {

        try {

            DataInputStream entrada1 = new DataInputStream(cliente.getInputStream());

            int maxAr = entrada1.readInt();

            for (int i = 0; i <= maxAr; i++) {
                DataInputStream entrada = new DataInputStream(cliente.getInputStream());

                byte[] b = new byte[1024];
                String nombre = entrada.readUTF();

                // System.out.println("Recibimos el archivo: " + nombre);

                long tam = entrada.readLong();

                DataOutputStream salida = new DataOutputStream(
                        new FileOutputStream("archivosCliente\\" + nombre));

                long recibidos = 0;
                int n = 0, porcentaje;

                while (recibidos < tam) {

                    n = entrada.read(b);
                    salida.write(b, 0, n);
                    salida.flush();

                    recibidos += n;
                    porcentaje = (int) (recibidos * 100 / tam);
                    // System.out.println("Progreso: " + porcentaje + "%");
                }
                // System.out.println("\nArchivo recibido " + nombre + "\n");
                salida.flush();
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static Productos[] DeserializacionProductos()
            throws FileNotFoundException, IOException, ClassNotFoundException {

        ObjectInputStream entradaObj = new ObjectInputStream(
                new FileInputStream("archivosCliente\\objetoS.txt"));

        Productos[] prueRec = (Productos[]) entradaObj.readObject();

        entradaObj.close();

        return prueRec;
    }

    public static void SerializacionObjeto(Productos[] objPro) throws Exception {

        try {

            ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream("archivosCliente\\objetoC.txt"));
            salida.writeObject(objPro);
            salida.close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {

        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Escriba la direccion del servidor");
        String host = br.readLine();

        System.out.printf("\nEscribe el puerto:");
        int pto = Integer.parseInt(br.readLine());

        try {

            Socket cliente = new Socket(host, pto);

            RecibirArchivo(cliente);

            Productos[] objPro = (Productos[]) DeserializacionProductos();

            MenuCliente(cliente, objPro);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}