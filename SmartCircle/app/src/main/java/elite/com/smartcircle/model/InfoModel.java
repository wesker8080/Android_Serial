package elite.com.smartcircle.model;

/**
 * @author Wesker
 * @create 2019-01-24 15:33
 */
public class InfoModel {
    // 时间
    private String time;
    // 体重
    private String weight;
    // 体脂
    private String fat;
    // 心率
    private String rate;
    // 血压
    private String blood;
    // 温度
    private String temp;

    public String getTime() {
        return time;
    }

    public void setTime(String mTime) {
        time = mTime;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String mWeight) {
        weight = mWeight;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String mFat) {
        fat = mFat;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String mRate) {
        rate = mRate;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String mBlood) {
        blood = mBlood;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String mTemp) {
        temp = mTemp;
    }

    @Override
    public String toString() {
        return "InfoModel{" +
                "time='" + time + '\'' +
                ", weight='" + weight + '\'' +
                ", fat='" + fat + '\'' +
                ", rate='" + rate + '\'' +
                ", blood='" + blood + '\'' +
                ", temp='" + temp + '\'' +
                '}';
    }
}
