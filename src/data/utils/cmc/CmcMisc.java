package data.utils.cmc;

public class CmcMisc {
    public static String getDigitValue(float value) {
        return getDigitValue(value, 1);
    }

    public static String getDigitValue(float value, int digit) {
        if (Math.abs((float) Math.round(value) - value) < 0.0001f) {
            return String.format("%d", (int) Math.round(value));
        } else {
            return String.format("%." + digit + "f", value);
        }
    }
}
