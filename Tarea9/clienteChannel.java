import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

public class clienteChannel {
    public final static int PUERTO = 7; //se especifica el puerto que va a usar el servidor
    private final static int LIMITE = 100;  //limite de datos que va a mandar el cliente, si pasa de mil puede matar el proceso del servidor servidor
    
    public static void main(String[] args) {
        boolean bandera = false;    //declaracion de la bandera que detendra el proceso cuando llegue al limite de datos
        SocketAddress remoto = new InetSocketAddress("127.0.0.1", PUERTO);  
        //inicio del socket por el cual se mandaran todos los datos, especificando la direccion localhost y el puerto que va a usar el servidor

        try {
            DatagramChannel canal = DatagramChannel.open(); //inicio del canal por el cual se va a comunicar con el servidor
            canal.configureBlocking(false); //se especifica que el canal no va a ser de tipo bloqueante
            canal.connect(remoto);  //se hace la conexion del socket con el canal, por ende se hace la conexion con el servidor

            Selector selector = Selector.open();    //se inicia el selector para poder hacer la ejecucion del canal
            canal.register(selector, SelectionKey.OP_WRITE);    //se epecifica registra el canal a usar como de escritura

            ByteBuffer buffer = ByteBuffer.allocate(4); //craceion del buffer que va a hacer el envio de datos al servidor 
            int n = 0;  //contador para el envio de datos

            while (true) {
                selector.select(5000);  //se pone en modo espera de 5 segundos al selector en caso de tener mas peticiones
                Set sk = selector.selectedKeys();   //se seleccionan los canales que van a mandar datos al servidor
                
                if (sk.isEmpty() && n == LIMITE || bandera) {
                    //comparacion que indica si ya no se deben de mandar mas datos, o la bandera indica que el sistema se puede ejecutar
                    canal.close();  //se cierra el canal y por ende la conexion
                    break;  //se cierra el ciclo while
                }else{
                    Iterator it = sk.iterator();    //se agragan las cenexiones de canales a un arreglo de tipo iterator
                    
                    while (it.hasNext()) {
                        //siempre que se tenga un canal en el arrglo del iterator se continua con la conexion
                        SelectionKey key = (SelectionKey) it.next();    //se hace la seleccion del canal para hacer el encion al servidor correspondiente
                        it.remove();    //se quita el canal que esta siendo usando

                        if (key.isWritable()) {
                            //se pregunta si los datos son posibles de escribir
                            buffer.clear(); // se limpia el buffer antes de almacecnar datos en el
                            buffer.putInt(n);   // se empiezan a almacenar los datos del contador al buffer
                            buffer.flip(); // se regresa a la primera posicion del buffer

                            canal.write(buffer); // se manda el dato del buffer al servidor

                            System.out.println("Escribiendo los datos: " + n);  //  impresion de lo que se esta mandando
                            n++;    //aumento del contador

                            if (n == LIMITE) {
                                //se pregunta si ahora el contador llego al limite, si es asi se limpia todo, se termina de escribir el ultimo dato del buffer y se recibe la respuesta del servidor
                                buffer.clear();
                                buffer.putInt(100);
                                buffer.flip();
                                canal.write(buffer);
                                bandera = true; //se pone la bandera en true para terminar el ciclo iterativo
                                key.interestOps(SelectionKey.OP_READ);
                                break;
                                //se termina el ciclo iterativo
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
}
