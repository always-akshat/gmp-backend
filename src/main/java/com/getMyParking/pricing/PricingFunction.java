package com.getMyParking.pricing;

import com.getMyParking.entity.PriceGridEntity;
import com.getMyParking.entity.PricingSlotEntity;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;

import java.util.*;

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

    public static double calculateTotalCost(Map<Integer,List<PricingSlotEntity>> pricingSlotsMap, DateTime checkInTime, DateTime checkoutTime) {
        double cost = 0;

        Map<Integer,List<PricingSlotEntity>> normalSlotMap = new HashMap<>();
        Map<Integer,List<PricingSlotEntity>> specialSlotMap = new HashMap<>();;
        for (Map.Entry<Integer,List<PricingSlotEntity>> entry : pricingSlotsMap.entrySet()) {
            List<PricingSlotEntity> normalSlots = Lists.newArrayList();
            List<PricingSlotEntity> specialSlots = Lists.newArrayList();
            for (PricingSlotEntity pricingSlot : entry.getValue()) {
                if (pricingSlot.getType().equalsIgnoreCase("NORMAL"))
                    normalSlots.add(pricingSlot);
                else
                    specialSlots.add(pricingSlot);
            }
            normalSlotMap.put(entry.getKey(),normalSlots);
            specialSlotMap.put(entry.getKey(),specialSlots);
        }
        cost = calculateCost(normalSlotMap,checkInTime,checkoutTime);
        if (specialSlotMap.size() > 0) cost += calculateCost(specialSlotMap,checkInTime,checkoutTime);
        return cost;
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
        Collections.sort(pricingSlots,priceSlotComparator);

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

        if (!currentPricingSlot.getType().equalsIgnoreCase("NORMAL")) {
            if (currentPricingSlot.getStartMinutesOfDay() > minutesOfDay || currentPricingSlot.getEndMinutesOfDay() < minutesOfDay) {
                for (index = 0; index < pricingSlots.size();index++) {
                    PricingSlotEntity pricingSlot = pricingSlots.get(index);
                    if (pricingSlot.getStartMinutesOfDay() > minutesOfDay) {
                        currentPricingSlot = pricingSlot;
                        break;
                    }
                }
                currentTime = currentTime.plusMinutes(currentPricingSlot.getStartMinutesOfDay() - minutesOfDay);
            }
        }

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
                if (!currentPricingSlot.getType().equalsIgnoreCase("NORMAL")) {
                    currentTime = currentTime.plusMinutes(currentPricingSlot.getStartMinutesOfDay() - currentTime.getMinuteOfDay());
                }
            } else {
                int endMinutesOfCurrentSlot = currentPricingSlot.getEndMinutesOfDay();
                pricingSlots = pricingSlotsMap.get((day % 7) + 1);
                if (pricingSlots == null) {
                    pricingSlots = pricingSlotsMap.get(0);
                }
                Collections.sort(pricingSlots,priceSlotComparator);
                currentPricingSlot = pricingSlots.get(0);
                index = 0;
                Collections.sort(Lists.newArrayList(currentPricingSlot.getPriceGrids()), priceGridComparator);
                currentPriceGrid = Lists.newArrayList(currentPricingSlot.getPriceGrids()).get(0);
                priceGridIndex = 0;
                currentTime = currentTime.plusMinutes(endMinutesOfCurrentSlot - currentTime.getMinuteOfDay());
                if (!currentPricingSlot.getType().equalsIgnoreCase("NORMAL")) {
                    currentTime = currentTime.plusMinutes(currentPricingSlot.getStartMinutesOfDay() - currentTime.getMinuteOfDay());
                }
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

    private static Comparator<PricingSlotEntity> priceSlotComparator = new Comparator<PricingSlotEntity>() {
        @Override
        public int compare(PricingSlotEntity o1, PricingSlotEntity o2) {
            return o1.getStartMinutesOfDay() - o2.getStartMinutesOfDay();
        }
    };

}
