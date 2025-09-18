package com.gharana.pricing_service.service;

import java.time.LocalDate;
import java.util.List;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gharana.pricing_service.dao.PricingRepository;
import com.gharana.pricing_service.dto.PricingRecord;

@Service
public class PricingServiceImpl implements PricingService {

    @Autowired
    private PricingRepository pricingRepository;

    public PricingServiceImpl(PricingRepository pricingRepository) {
        this.pricingRepository = pricingRepository;
    }

    @Override
    public double getAvgPricePerNight(String hotelId, String roomTypeId, LocalDate checkInDate, LocalDate checkOutDate) {
        List<PricingRecord> priceList = pricingRepository.getPriceByRoomTypeAndDateRange(hotelId, roomTypeId, checkInDate, checkOutDate);
        double totalPrice = priceList.stream().mapToDouble(PricingRecord::getPrice).sum();
        double avg = totalPrice / priceList.size();
        return BigDecimal.valueOf(avg).setScale(0, RoundingMode.HALF_UP).doubleValue();
    }
    

    // Using Bounded Parallelism (Executor with limited threads)
    // public List<PriceQuote> getAvgPricePerNight(List<AvailableRoomType> availableRoomTypes, LocalDate checkInDate, LocalDate checkOutDate) {

    //     List<PriceQuote> response = Collections.synchronizedList(new ArrayList<>()); // what is the use of synchronized list?
    //     List<String> errors = Collections.synchronizedList(new ArrayList<>());

    //     ExecutorService exec = Executors.newFixedThreadPool(Math.min(availableRoomTypes.size(), 8));
    //     List<CompletableFuture<Void>> futures = new ArrayList<>(); // what is completable future?
    //     for(AvailableRoomType roomType : availableRoomTypes) {
    //         futures.add(CompletableFuture.runAsync(() -> {
    //             try {

    //             // resolve calendar or dynamic rates for each night
    //             List<PricingRecord> records = pricingRepository.getPrice(roomType.getHotelId(), roomType.getRoomTypeId(), checkInDate, checkOutDate);

    //             double avgPrice = records.stream().mapToDouble(PricingRecord::getRate).sum() / records.size();
                    
    //             //BigDecimal avg = total.divide(BigDecimal.valueOf(records.size()), 2, RoundingMode.HALF_UP);
    //             PriceQuote q = new PriceQuote(roomType.getHotelId(), roomType.getRoomTypeId(), avgPrice);
    //             response.add(q);

    //             } catch (Exception ex) {
    //             errors.add("internal_error");
    //             }
            
    //         }, exec));
    //     }

    //     // wait with timeout for all tasks (guard overall budget)
    //     try {
    //         CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
    //                         .get(1200, TimeUnit.MILLISECONDS);
    //     } catch (Exception e) {
    //         // some tasks may still be running; mark missing as stale/error
    //     } finally {
    //         exec.shutdownNow();
    //     }

    //     return response;

    // }

}
