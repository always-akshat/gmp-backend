package com.getMyParking.pricing;

import com.getMyParking.entity.PriceGridEntity;
import com.getMyParking.entity.PricingSlotEntity;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by rahulgupta.s on 05/06/15.
 */
public class PricingFunction {

    //pricingSlots will be sorted by day. Day is Integer with 1 as Monday and 7 as Sunday
    public static double calculateInitialCost(Map<Integer,List<PricingSlotEntity>> pricingSlotsMap, DateTime checkInTime) {

        int day = checkInTime.dayOfWeek().get();
        List<PricingSlotEntity> pricingSlots = pricingSlotsMap.get(day);

        if (pricingSlots == null) {
            pricingSlots = pricingSlotsMap.get(0);
        }

        PricingSlotEntity currentPricingSlot = pricingSlots.get(0);
        int minutesOfDay = checkInTime.getMinuteOfDay();

        for (PricingSlotEntity pricingSlot : pricingSlots) {
            if (pricingSlot.getDay() == day && pricingSlot.getStartMinutesOfDay() <= minutesOfDay
                    && pricingSlot.getEndMinutesOfDay() > minutesOfDay) {
                currentPricingSlot = pricingSlot;
                break;
            }
        }

        Collections.sort(Lists.newArrayList(currentPricingSlot.getPriceGrids()),priceGridComparator);
        return Lists.newArrayList(currentPricingSlot.getPriceGrids()).get(0).getCost();

    }

    public static double calculateCost(Map<Integer,List<PricingSlotEntity>> pricingSlotsMap, DateTime checkInTime, DateTime checkoutTime) {

        double cost = 0;
        int index;
        DateTime currentTime = checkInTime;

        int day = checkInTime.dayOfWeek().get();

        List<PricingSlotEntity> pricingSlots = pricingSlotsMap.get(day);

        if (pricingSlots == null) {
            pricingSlots = pricingSlotsMap.get(0);
        }

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

        Collections.sort(Lists.newArrayList(currentPricingSlot.getPriceGrids()), priceGridComparator);
        PriceGridEntity currentPriceGrid = Lists.newArrayList(currentPricingSlot.getPriceGrids()).get(0);
        int priceGridIndex = 0;

        while (currentTime.isBefore(checkoutTime)) {

            cost += currentPriceGrid.getCost();
            DateTime newCurrentTime = currentTime.plusMinutes(currentPriceGrid.getDuration());

            minutesOfDay = currentTime.getMinuteOfDay();

            if (newCurrentTime.getDayOfWeek() == currentTime.getDayOfWeek() &&
                    currentPricingSlot.getStartMinutesOfDay() <= minutesOfDay &&
                    currentPricingSlot.getEndMinutesOfDay() > minutesOfDay) {

                List<PriceGridEntity> currentPriceGrids = Lists.newArrayList(currentPricingSlot.getPriceGrids());
                priceGridIndex = (priceGridIndex == (currentPriceGrids.size() - 1))? priceGridIndex: priceGridIndex + 1;
                currentPriceGrid = currentPriceGrids.get(priceGridIndex);
                currentTime = newCurrentTime;

            } else if (newCurrentTime.getDayOfWeek() == currentTime.getDayOfWeek() &&
                    pricingSlots.size() > index) {
                int endMinutesOfCurrentSlot = currentPricingSlot.getEndMinutesOfDay();
                index++;
                currentPricingSlot = pricingSlots.get(index);
                Collections.sort(Lists.newArrayList(currentPricingSlot.getPriceGrids()), priceGridComparator);
                currentPriceGrid = Lists.newArrayList(currentPricingSlot.getPriceGrids()).get(0);
                priceGridIndex = 0;
                currentTime = currentTime.plusMinutes(endMinutesOfCurrentSlot - currentTime.getMinuteOfDay());
            } else {
                int endMinutesOfCurrentSlot = currentPricingSlot.getEndMinutesOfDay();
                pricingSlots = pricingSlotsMap.get((day % 7) + 1);
                if (pricingSlots == null) {
                    pricingSlots = pricingSlotsMap.get(0);
                }
                currentPricingSlot = pricingSlots.get(0);
                index = 0;
                Collections.sort(Lists.newArrayList(currentPricingSlot.getPriceGrids()), priceGridComparator);
                currentPriceGrid = Lists.newArrayList(currentPricingSlot.getPriceGrids()).get(0);
                priceGridIndex = 0;
                currentTime = currentTime.plusMinutes(endMinutesOfCurrentSlot - currentTime.getMinuteOfDay());
            }
        }

        return cost;
    }

    private static Comparator<PriceGridEntity> priceGridComparator = new Comparator<PriceGridEntity>() {
        @Override
        public int compare(PriceGridEntity o1, PriceGridEntity o2) {
            return o1.getSequenceNumber() - o2.getSequenceNumber();
        }
    };

}
