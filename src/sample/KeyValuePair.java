package sample;

public class KeyValuePair {
    private final Long key;
    private final String value;
    public KeyValuePair(Long key, String value) {
        this.key = key;
        this.value = value;
    }

    public Long getKey() {return key;}

    public String toString() {return value;}
}