public class Carrito {
    int id_compra;
    int cantidad;
    int precio_t;

    public Carrito(int id_compra, int cantidad, int precio_t) {
        this.id_compra = id_compra;
        this.cantidad = cantidad;
        this.precio_t = precio_t;
    }
    public int getId_compra() {
        return id_compra;
    }
    public void setId_compra(int id_compra) {
        this.id_compra = id_compra;
    }
    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public int getPrecio_t() {
        return precio_t;
    }
    public void setPrecio_t(int precio_t) {
        this.precio_t = precio_t;
    }
    
}
