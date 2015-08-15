package com.getMyParking.pricing;

import com.getMyParking.entity.PriceGridEntity;
import com.getMyParking.entity.PricingSlotEntity;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by rahulgupta.s on 05/06/15.
 */
public class PricingFunction {
/*
    //pricingSlots will be sorted by day. Day is Integer with 1 as Monday and 7 as Sunday
    public static int calculateInitialCost(List<PricingSlotEntity> pricingSlots, DateTime checkInTime) {

        int day = checkInTime.dayOfWeek().get();
        PricingSlotEntity currentPricingSlot = pricingSlots.get(0);
        int minutesOfDay = checkInTime.getMinuteOfDay();

        for (PricingSlotEntity pricingSlot : pricingSlots) {
            if (pricingSlot.getDay() == day && pricingSlot.getStartMinutesOfDay() <= minutesOfDay
                    && pricingSlot.getEndMinutesOfDay() > minutesOfDay) {
                currentPricingSlot = pricingSlot;
                break;
            }
        }
        List<PriceGridEntity> priceGridList = Lists.newArrayList(currentPricingSlot.getPriceGridsById());
        Collections.sort(priceGridList,priceGridComparator);
        if (currentPricingSlot.getCollectionModel().equalsIgnoreCase("PREPAID")) return 0;
        else return priceGridList.get(0).getCost();

    }

    public static int calculateCost(List<PricingSlotEntity> pricingSlots, DateTime checkInTime, DateTime checkoutTime) {

        int cost = 0;
        int index;
        DateTime currentTime = checkInTime;

        int day = checkInTime.dayOfWeek().get();
        PricingSlotEntity currentPricingSlot = pricingSlots.get(0);
        int minutesOfDay = checkInTime.getMinuteOfDay();

        for (index = 0; index < pricingSlots.size();index++) {
            PricingSlotEntity pricingSlot = pricingSlots.get(index);
            if (pricingSlot.getDay() == day && pricingSlot.getStartMinutesOfDay() <= minutesOfDay
                    && pricingSlot.getEndMinutesOfDay() > minutesOfDay) {
                currentPricingSlot = pricingSlot;
                break;
            }
        }

        List<PriceGridEntity> priceGridList = Lists.newArrayList(currentPricingSlot.getPriceGridsById());
        Collections.sort(priceGridList,priceGridComparator);
        PriceGridEntity currentPriceGrid = priceGridList.get(0);
        int priceGridIndex = 0;

        while (currentTime.isBefore(checkoutTime)) {

            cost += currentPriceGrid.getCost();
            DateTime newCurrentTime = currentTime.plusMinutes(currentPriceGrid.getDuration());

            minutesOfDay = currentTime.getMinuteOfDay();

            if (newCurrentTime.getDayOfWeek() == currentTime.getDayOfWeek() &&
                    currentPricingSlot.getStartMinutesOfDay() <= minutesOfDay &&
                    currentPricingSlot.getEndMinutesOfDay() > minutesOfDay) {

                List<PriceGridEntity> currentPriceGrids = Lists.newArrayList(currentPricingSlot.getPriceGridsById());
                priceGridIndex = (priceGridIndex == (currentPriceGrids.size() - 1))? priceGridIndex: priceGridIndex + 1;
                currentPriceGrid = currentPriceGrids.get(priceGridIndex);

            } else {
                PricingSlotEntity nextPricingSlot = pricingSlots.get( (index + 1) % pricingSlots.size());
                if (nextPricingSlot.getStartMinutesOfDay() <= minutesOfDay
                        && nextPricingSlot.getEndMinutesOfDay() > minutesOfDay) {
                    currentPricingSlot = nextPricingSlot;
                    index = (index + 1) % pricingSlots.size();
                    List<PriceGridEntity> currentPriceGrids = Lists.newArrayList(currentPricingSlot.getPriceGridsById());
                    Collections.sort(currentPriceGrids,priceGridComparator);
                    currentPriceGrid = currentPriceGrids.get(0);
                    priceGridIndex = 0;

                }
            }
            currentTime = newCurrentTime;
        }

        return cost;
    }

    private static Comparator<PriceGridEntity> priceGridComparator = new Comparator<PriceGridEntity>() {
        @Override
        public int compare(PriceGridEntity o1, PriceGridEntity o2) {
            return o1.getSequenceNumber() - o2.getSequenceNumber();
        }
    };
*/
}
