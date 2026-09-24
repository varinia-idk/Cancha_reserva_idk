package com.example.reservacanchasapp.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

import com.example.reservacanchasapp.data.entity.Pago;

@Dao
public interface PagoDao {

    @Insert
    long insertarPago(Pago pago);

    @Update
    void actualizarPago(Pago pago);

    @Query("SELECT * FROM pagos WHERE idReservaDetalle = :idReservaDetalle")
    LiveData<List<Pago>> obtenerPorReservaDetalle(int idReservaDetalle);

    @Query("SELECT * FROM pagos WHERE idPago = :idPago")
    Pago obtenerPorId(int idPago);

    @Query("UPDATE pagos SET estado = :nuevoEstado WHERE idPago = :idPago")
    void cambiarEstado(int idPago, String nuevoEstado);

    @Query("SELECT COUNT(*) FROM pagos WHERE idReservaDetalle = :idDetalle AND estado = 'Pagado'")
    int contarPagosPagados(int idDetalle);
}
