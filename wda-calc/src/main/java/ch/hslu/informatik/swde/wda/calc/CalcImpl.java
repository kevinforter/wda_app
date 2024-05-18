package ch.hslu.informatik.swde.wda.calc;

import ch.hslu.informatik.swde.wda.domain.Weather;

import java.time.LocalDateTime;
import java.util.TreeMap;

import static java.lang.Math.round;

public class CalcImpl implements Calc {

    @Override
    public double getMeanForTemperature(TreeMap<LocalDateTime, Weather> weatherMap) {

        double temperatureSum = 0;

        for (Weather w : weatherMap.values()) {
            temperatureSum += w.getCurrTempCelsius();
        }

        return (double) round((temperatureSum / weatherMap.size()) * 100) / 100;
    }

    @Override
    public double getMeanForPressure(TreeMap<LocalDateTime, Weather> weatherMap) {

        double pressureSum = 0;

        for (Weather w : weatherMap.values()) {
            pressureSum += w.getPressure();
        }

        return (double) round((pressureSum / weatherMap.size()) * 100) / 100;
    }

    @Override
    public double getMeanForHumidity(TreeMap<LocalDateTime, Weather> weatherMap) {

        double humiditySum = 0;

        for (Weather w : weatherMap.values()) {
            humiditySum += w.getHumidity();
        }

        return (double) round((humiditySum / weatherMap.size()) * 100) / 100;
    }

    @Override
    public double getMaxForTemperature(TreeMap<LocalDateTime, Weather> weatherMap) {

        double max = 0;

        for (Weather w : weatherMap.values()) {

            if (max < w.getCurrTempCelsius()) {
                max = w.getCurrTempCelsius();
            }
        }

        return max;
    }

    @Override
    public double getMinForTemperature(TreeMap<LocalDateTime, Weather> weatherMap) {

        double min = weatherMap.firstEntry().getValue().getCurrTempCelsius();

        for (Weather w : weatherMap.values()) {
            if (min > w.getCurrTempCelsius()) {
                min = w.getCurrTempCelsius();
            }
        }

        return min;
    }

    @Override
    public double getMaxForPressure(TreeMap<LocalDateTime, Weather> weatherMap) {

        double max = 0;

        for (Weather w : weatherMap.values()) {

            if (max < w.getPressure()) {
                max = w.getPressure();
            }
        }

        return max;
    }

    @Override
    public double getMinForPressure(TreeMap<LocalDateTime, Weather> weatherMap) {

        double min = weatherMap.firstEntry().getValue().getPressure();

        for (Weather w : weatherMap.values()) {
            if (min > w.getPressure()) {
                min = w.getPressure();
            }
        }

        return min;
    }

    @Override
    public double getMaxForHumidity(TreeMap<LocalDateTime, Weather> weatherMap) {

        double max = 0;

        for (Weather w : weatherMap.values()) {

            if (max < w.getHumidity()) {
                max = w.getHumidity();
            }
        }

        return max;
    }

    @Override
    public double getMinForHumidity(TreeMap<LocalDateTime, Weather> weatherMap) {

        double min = weatherMap.firstEntry().getValue().getHumidity();

        for (Weather w : weatherMap.values()) {
            if (min > w.getHumidity()) {
                min = w.getHumidity();
            }
        }

        return min;
    }
}
