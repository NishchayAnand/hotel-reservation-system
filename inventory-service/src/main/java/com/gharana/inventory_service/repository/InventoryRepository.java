package com.gharana.inventory_service.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

import com.gharana.inventory_service.model.entity.InventoryRecord;


public interface InventoryRepository extends JpaRepository<InventoryRecord, Long> {

    @Query("SELECT ir.hotelId, ir.roomTypeId, MIN(ir.totalCount - ir.reservedCount) " + 
        "FROM InventoryRecord ir " +
        "WHERE ir.hotelId IN :hotelIds " +
           "  AND ir.reservationDate >= :checkInDate " +
           "  AND ir.reservationDate < :checkOutDate " +
           "  AND ir.reservedCount < ir.totalCount " +
        "GROUP BY ir.hotelId, ir.roomTypeId " + 
        "HAVING COUNT(ir.reservationDate) = :nights"
    )
    public List<Object[]> findAvailableRoomTypesForHotels(Set<Long> hotelIds, 
        LocalDate checkInDate, 
        LocalDate checkOutDate, 
        long nights);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from InventoryRecord i " +
           "where i.hotelId = :hotelId and i.roomTypeId = :roomTypeId " +
           "and i.reservationDate between :start and :end " +
           "order by i.reservationDate")
    List<InventoryRecord> findByHotelIdAndRoomTypeIdAndReservationDateBetween(
        @Param("hotelId") Long hotelId,
        @Param("roomTypeId") Long roomTypeId,
        @Param("start") LocalDate start,
        @Param("end") LocalDate end);

}
