package com.gharana.pricing_service.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.Query;

import org.springframework.stereotype.Repository;

import com.gharana.pricing_service.dto.AvailableRoomTypeDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class PricingRepositoryImpl implements PricingRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @SuppressWarnings("unchecked")
    public List<Object[]> getAvgRateByHotelAndRoomType(List<AvailableRoomTypeDTO> availableRoomTypes,
            LocalDate checkInDate, LocalDate checkOutDate) {

        // If availableRoomTypes has 3 items, values becomes "(?,?),(?,?),(?,?)".
        String values = availableRoomTypes.stream()
            .map(roomType -> "(?,?)")
            .collect(Collectors.joining(","));

        String sql = """
                WITH combos(hotel_id, room_type_id) AS (VALUES %s)
                SELECT c.hotel_id, c.room_type_id, ROUND(AVG(r.rate)) as avg_rate_per_night
                FROM combos c
                LEFT JOIN room_type_rate r
                     ON c.hotel_id = r.hotel_id
                    AND c.room_type_id = r.room_type_id
                    AND r.reservation_date >= ?
                    AND r.reservation_date < ?
                GROUP BY c.hotel_id, c.room_type_id
                ORDER BY c.hotel_id, c.room_type_id 
                """.formatted(values);

        Query query = em.createNativeQuery(sql);

        int idx = 1;
        for(AvailableRoomTypeDTO availableRoomType: availableRoomTypes) {
            query.setParameter(idx++, availableRoomType.getHotelId());
            query.setParameter(idx++, availableRoomType.getRoomTypeId());
        }

        query.setParameter(idx++, Date.valueOf(checkInDate));
        query.setParameter(idx++, Date.valueOf(checkOutDate));
        
        // For a native query without mapping you get List<Object[]>
        return query.getResultList();

    }

}
