package data;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


public class DataGenerator {

    private DataGenerator() {
    }


    public static boolean getRandomBoolean() {
        return Math.random() >= 0.5;
    }

    public static int getRandomIntegerInRange(int min, int max) {
        return (int) (Math.round((max - min) * Math.random()) + min);
    }

    public static int getRandomIntegerInRange(int value) {
        return getRandomIntegerInRange(0, value);
    }

    public static double getRandomDoubleRange(double min, double max) {
        Random random = new Random();
        double randomValue = random.nextDouble();
        return min + (randomValue * (max - min));
    }

    public static double getRandomDoubleInRange(double min, double max, int... decimalPlaces) {
        assert decimalPlaces.length <= 1;
        if (decimalPlaces.length > 0) {
            double random = (max - min) * Math.random() + min;
            double roundingParameter = Math.pow(10, decimalPlaces[0]);
            return Math.round(roundingParameter * random) / roundingParameter;
        }
        else
            return getRandomDoubleInRange(min, max);
    }

    public static String getRandomString(int length,
                                         boolean useEngCharacters,
                                         boolean useNumbers,
                                         boolean useSpecialCharacters,
                                         boolean useDiacriticCharacters,
                                         boolean useCyrillicCharacters) {
        String pool = "";
        if (useEngCharacters)
            pool += "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz";
        if (useNumbers)
            pool += "0123456789";
        if (useSpecialCharacters)
            pool += "!@#$%^&*()_+-=[]{}|;':\"<>,.?/~`";
        if (useDiacriticCharacters)
            pool += "ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÑÒÓÔÕÖØŒÙÚÛÜÝÞßàáâãäåæçèéêëìíîïñòóôõöøœùúûüýþÿ";
        if (useCyrillicCharacters)
            pool += "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ" + "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";

        StringBuilder stringBuffer = new StringBuilder(length);
        for (int index = 0; index < length; index++) {
            int charIndex = (int) (pool.length() * Math.random());
            char character = pool.charAt(charIndex);
            stringBuffer.append(character);
        }
        return stringBuffer.toString();
    }

    public static String getDate(String dateLabel) {
        if (dateLabel.contains("future"))
            return getDateInFormat(dateLabel, 1);
        if (dateLabel.contains("past"))
            return getDateInFormat(dateLabel, -1);
        if (dateLabel.contains("current"))
            return getDateInFormat(dateLabel, 0);
        if (dateLabel.contains("random"))
            return getDateInFormat(dateLabel, 2);
        return "";
    }

    /**
     * The method is used for generating dates and dates/time of specific format.
     * We can generate different types of dates:
     * - future dates, when timeIndex =  1
     * - past dates,   when timeIndex = -1
     * - current date, when timeIndex =  0
     * - random date,  when timeIndex = other value
     * */
    public static String getDateInFormat(String dateLabel, Integer timeIndex) {
        if(dateLabel.contains("+")) {
            int days = Integer.parseInt(dateLabel.substring(dateLabel.indexOf("+") + 2));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDateTime.now().plusDays(days).format(formatter);
        }
        else if (dateLabel.contains("-")) {
            int days = Integer.parseInt(dateLabel.substring(dateLabel.indexOf("-") + 2));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDateTime.now().minusDays(days).format(formatter);
        }
        else {
            int timeParameter;
            if (timeIndex == 1 || timeIndex == -1 || timeIndex == 0)
                timeParameter = timeIndex;
            else {
                double random = Math.random();
                if (random <= 0.33)
                    timeParameter = -1;
                else if (random > 0.66)
                    timeParameter = 1;
                else
                    timeParameter = 0;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, timeParameter * (int) (Math.round(364 * Math.random()) + 1));

            DateFormat df;
            if (dateLabel.contains("format"))
                df = new SimpleDateFormat(dateLabel.substring(dateLabel.indexOf(": ") + 2));
            else
                df = new SimpleDateFormat("yyyy-MM-dd");

            return df.format(calendar.getTime());
        }
    }


    public static String getCurrentDateInFormat(String format) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return currentDate.format(formatter);
    }

    public static String getRandomString(String label) {
        boolean useEngCharacters = label.toLowerCase().contains("english");
        boolean useNumbers = label.toLowerCase().contains("numbers");
        boolean useSpecialChars = label.toLowerCase().contains("special");
        boolean useDiacriticChars = label.toLowerCase().contains("diacritic");
        boolean useCyrillicCharacters = label.toLowerCase().contains("cyrillic");
        boolean increaseSize = label.toLowerCase().contains("+");
        String labelCharRemove = label.replaceAll("[A-Za-z:+\\s]", "");
        int size = labelCharRemove.equals("") ? 5 : Integer.parseInt(labelCharRemove);
        if (increaseSize)
            size++;
        return getRandomString(size, useEngCharacters, useNumbers, useSpecialChars, useDiacriticChars, useCyrillicCharacters);
    }

    public static Object getValue(String valueLabel) {
        if (valueLabel.contains("null"))             return null;
        if (valueLabel.contains("empty"))            return "";
        if (valueLabel.contains("space"))            return " ";
        if (valueLabel.contains("boolean"))          return getRandomBoolean();
        if (valueLabel.contains("negative integer")) return Integer.parseInt(String.valueOf(new BigDecimal(getRandomIntegerInRange(1000)).negate()));
        if (valueLabel.contains("double"))           return getRandomDoubleRange(1, 100);
        if (valueLabel.contains("decimal"))          return getRandomDoubleInRange(0., 100., 2);
        if (valueLabel.contains("integer"))          return getRandomIntegerInRange(1000);
        if (valueLabel.contains("date"))             return getDate(valueLabel);
        if (valueLabel.contains("string"))           return getRandomString(valueLabel);
        if (valueLabel.contains("use: "))            return valueLabel.replace("use: ", "");
        throw new RuntimeException("Check please if you are using a correct label to get the data generated");
    }
}
