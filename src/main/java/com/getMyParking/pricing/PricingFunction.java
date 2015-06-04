package com.getMyParking.pricing;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by rahulgupta.s on 05/06/15.
 */
public class PricingFunction {

    //pricingSlots will be sorted by day. Day is Integer with 1 as Monday and 7 as Sunday
    int calculateCost(List<PricingSlot> pricingSlots, DateTime checkInTime, DateTime checkoutTime) {

        int cost = 0;
        int index;
        DateTime currentTime = checkInTime;

        int day = checkInTime.dayOfWeek().get();
        PricingSlot currentPricingSlot = pricingSlots.get(0);
        int minutesOfDay = checkInTime.getMinuteOfDay();

        for (index = 0; index < pricingSlots.size();index++) {
            PricingSlot pricingSlot = pricingSlots.get(index);
            if (pricingSlot.getDay() == day && pricingSlot.getStartMinutesOfDay() <= minutesOfDay
                    && pricingSlot.getEndMinutesOfDay() > minutesOfDay) {
                currentPricingSlot = pricingSlot;
                break;
            }
        }

        Collections.sort(currentPricingSlot.getPriceGrids(),priceGridComparator);
        PriceGrid currentPriceGrid = currentPricingSlot.getPriceGrids().get(0);
        int priceGridIndex = 0;

        while (currentTime.isBefore(checkoutTime)) {

            cost += currentPriceGrid.getCost();
            DateTime newCurrentTime = currentTime.plusMinutes(currentPriceGrid.getDuration());

            minutesOfDay = currentTime.getMinuteOfDay();

            if (newCurrentTime.getDayOfWeek() == currentTime.getDayOfWeek() &&
                    currentPricingSlot.getStartMinutesOfDay() <= minutesOfDay &&
                    currentPricingSlot.getEndMinutesOfDay() > minutesOfDay) {

                List<PriceGrid> currentPriceGrids = currentPricingSlot.getPriceGrids();
                priceGridIndex = (priceGridIndex == (currentPriceGrids.size() - 1))? priceGridIndex: priceGridIndex + 1;
                currentPriceGrid = currentPriceGrids.get(priceGridIndex);

            } else {
                PricingSlot nextPricingSlot = pricingSlots.get( (index + 1) % pricingSlots.size());
                if (nextPricingSlot.getStartMinutesOfDay() <= minutesOfDay
                        && nextPricingSlot.getEndMinutesOfDay() > minutesOfDay) {
                    currentPricingSlot = nextPricingSlot;
                    index = (index + 1) % pricingSlots.size();
                    Collections.sort(currentPricingSlot.getPriceGrids(),priceGridComparator);
                    currentPriceGrid = currentPricingSlot.getPriceGrids().get(0);
                    priceGridIndex = 0;

                }
            }
            currentTime = newCurrentTime;
        }

        return cost;
    }

    Comparator<PriceGrid> priceGridComparator = new Comparator<PriceGrid>() {
        @Override
        public int compare(PriceGrid o1, PriceGrid o2) {
            return o1.getSequenceNumber() - o2.getSequenceNumber();
        }
    };

}
