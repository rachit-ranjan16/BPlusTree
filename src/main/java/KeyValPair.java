import java.util.ArrayList;
import java.util.Random;

public class KeyValPair {
    double key;
    ArrayList<Double> value;

    public KeyValPair() {
        key = Double.POSITIVE_INFINITY;
        value = new ArrayList<>();
    }

    public KeyValPair(double key, double value) {
        this.key = key;
        this.value = new ArrayList<>();
        this.value.add(value);
    }

    public KeyValPair(double key) {
        this.key = key;
        this.value = new ArrayList<>();
        this.value.add(new Random().nextDouble());
    }

    public double getKey() {
        return key;
    }

    public void setKey(double key) {
        this.key = key;
    }

    public ArrayList<Double> getValue() {
        return value;
    }

    public void addToValues(double val) {
        /**
         * Adds val to KeyValPair's ArrayList of Values
         * @param val
         */
        value.add(val);
    }

    public String toString() {
        String val = "";
        for(Double x: value)
            val += "Value" + x + ",";
        return new String("(" + key + "," + val + ")");
    }
}
