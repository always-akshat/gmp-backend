package com.getMyParking.pricing;

import com.getMyParking.entity.PriceGridEntity;
import com.getMyParking.entity.PricingSlotEntity;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

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
            if (normalSlots.size() > 0) normalSlotMap.put(entry.getKey(),normalSlots);
            if (specialSlots.size() > 0) specialSlotMap.put(entry.getKey(),specialSlots);
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

        if (pricingSlotsMap.size() == 1 && pricingSlotsMap.get(0).get(0).getEndMinutesOfDay() == 0) {
            return flatDaysCost(pricingSlotsMap.get(0).get(0), checkInTime, checkoutTime);
        }

        List<PricingSlotEntity> pricingSlots = pricingSlotsMap.get(day);

        if (pricingSlots == null) {
            pricingSlots = pricingSlotsMap.get(0);
        }
        Collections.sort(pricingSlots,priceSlotComparator);

        PricingSlotEntity currentPricingSlot = pricingSlots.get(0);
        index = 1;
        int minutesOfDay = checkInTime.getMinuteOfDay();

        for (int i = 0; i < pricingSlots.size();i++) {
            PricingSlotEntity pricingSlot = pricingSlots.get(i);
            if (pricingSlot.getStartMinutesOfDay() <= minutesOfDay
                    && pricingSlot.getEndMinutesOfDay() > minutesOfDay) {
                currentPricingSlot = pricingSlot;
                index = i + 1;
                break;
            }
        }

        List<PriceGridEntity> currentPriceGrids = Lists.newArrayList(currentPricingSlot.getPriceGrids());
        Collections.sort(currentPriceGrids, priceGridComparator);
        PriceGridEntity currentPriceGrid = currentPriceGrids.get(0);
        int priceGridIndex = 0;

        if (!currentPricingSlot.getType().equalsIgnoreCase("NORMAL")) {
            if (currentPricingSlot.getStartMinutesOfDay() > minutesOfDay || currentPricingSlot.getEndMinutesOfDay() < minutesOfDay) {
                for (int i = 0; i < pricingSlots.size();i++) {
                    PricingSlotEntity pricingSlot = pricingSlots.get(i);
                    if (pricingSlot.getStartMinutesOfDay() > minutesOfDay) {
                        currentPricingSlot = pricingSlot;
                        index = i + 1;
                        break;
                    }
                }
                if (currentPricingSlot.getStartMinutesOfDay() > minutesOfDay)
                    currentTime = currentTime.plusMinutes(currentPricingSlot.getStartMinutesOfDay() - minutesOfDay);
                else
                    currentTime = currentTime.plusMinutes(DateTimeConstants.MINUTES_PER_DAY - minutesOfDay);
            }
        }

        while (currentTime.isBefore(checkoutTime)) {

            cost += currentPriceGrid.getCost();
            DateTime newCurrentTime = currentTime.plusMinutes(currentPriceGrid.getDuration());

            minutesOfDay = newCurrentTime.getMinuteOfDay();

            if (newCurrentTime.getDayOfWeek() == currentTime.getDayOfWeek() &&
                    currentPricingSlot.getStartMinutesOfDay() <= minutesOfDay &&
                    currentPricingSlot.getEndMinutesOfDay() > minutesOfDay) {

                priceGridIndex = (priceGridIndex == (currentPriceGrids.size() - 1))? priceGridIndex: priceGridIndex + 1;
                currentPriceGrid = currentPriceGrids.get(priceGridIndex);
                currentTime = newCurrentTime;

            } else if (newCurrentTime.getDayOfWeek() == currentTime.getDayOfWeek() &&
                    pricingSlots.size() > index) {
                int endMinutesOfCurrentSlot = currentPricingSlot.getEndMinutesOfDay();
                index++;
                currentPricingSlot = pricingSlots.get(index);
                currentPriceGrids = Lists.newArrayList(currentPricingSlot.getPriceGrids());
                Collections.sort(currentPriceGrids, priceGridComparator);
                currentPriceGrid = currentPriceGrids.get(0);
                priceGridIndex = 0;
                currentTime = currentTime.plusMinutes(endMinutesOfCurrentSlot - currentTime.getMinuteOfDay());
                if (!currentPricingSlot.getType().equalsIgnoreCase("NORMAL")) {
                    if (currentPricingSlot.getStartMinutesOfDay() > currentTime.getMinuteOfDay())
                        currentTime = currentTime.plusMinutes(currentPricingSlot.getStartMinutesOfDay() - currentTime.getMinuteOfDay());
                    else
                        currentTime = currentTime.plusMinutes(DateTimeConstants.MINUTES_PER_DAY - currentTime.getMinuteOfDay());
                }
            } else {
                int endMinutesOfCurrentSlot = currentPricingSlot.getEndMinutesOfDay();
                pricingSlots = pricingSlotsMap.get((day % 7) + 1);
                if (pricingSlots == null) {
                    pricingSlots = pricingSlotsMap.get(0);
                }
                Collections.sort(pricingSlots,priceSlotComparator);
                currentPricingSlot = pricingSlots.get(0);
                index = 1;
                currentPriceGrids = Lists.newArrayList(currentPricingSlot.getPriceGrids());
                Collections.sort(currentPriceGrids, priceGridComparator);
                currentPriceGrid = currentPriceGrids.get(0);
                priceGridIndex = 0;
                currentTime = currentTime.plusMinutes(endMinutesOfCurrentSlot - currentTime.getMinuteOfDay());
                if (!currentPricingSlot.getType().equalsIgnoreCase("NORMAL")) {
                    if (currentPricingSlot.getStartMinutesOfDay() > currentTime.getMinuteOfDay())
                        currentTime = currentTime.plusMinutes(currentPricingSlot.getStartMinutesOfDay() - currentTime.getMinuteOfDay());
                    else
                        currentTime = currentTime.plusMinutes(DateTimeConstants.MINUTES_PER_DAY - currentTime.getMinuteOfDay());
                }
            }
        }

        return cost;
    }

    private static Double flatDaysCost(PricingSlotEntity pricingSlot, DateTime checkInTime, DateTime checkoutTime) {
        double cost = 0;
        DateTime currentTime = checkInTime;
        List<PriceGridEntity> currentPriceGrids = Lists.newArrayList(pricingSlot.getPriceGrids());
        Collections.sort(currentPriceGrids, priceGridComparator);
        PriceGridEntity currentPriceGrid = currentPriceGrids.get(0);
        int priceGridIndex = 0;

        while (currentTime.isBefore(checkoutTime)) {
            cost += currentPriceGrid.getCost();
            DateTime newCurrentTime = currentTime.plusMinutes(currentPriceGrid.getDuration());
            if ((newCurrentTime.getMillis() - checkInTime.getMillis()) % DateTimeConstants.MILLIS_PER_DAY == 0) {
                priceGridIndex = 0;
                currentPriceGrid = currentPriceGrids.get(priceGridIndex);
                currentTime = newCurrentTime;
            } else {
                priceGridIndex = (priceGridIndex == (pricingSlot.getPriceGrids().size() - 1))? priceGridIndex: priceGridIndex + 1;
                currentPriceGrid = currentPriceGrids.get(priceGridIndex);
                currentTime = newCurrentTime;
            }
        }

        return cost;
    }

    private static Comparator<PriceGridEntity> priceGridComparator =
            (o1, o2) -> o1.getSequenceNumber() - o2.getSequenceNumber();

    private static Comparator<PricingSlotEntity> priceSlotComparator =
            (o1, o2) -> o1.getStartMinutesOfDay() - o2.getStartMinutesOfDay();

}
