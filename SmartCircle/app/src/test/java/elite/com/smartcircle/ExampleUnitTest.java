package elite.com.smartcircle;

import com.alibaba.fastjson.JSONObject;

import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testSaveJson() {
        String info = "{\"1262307385213\":\"体重 : 18.28\\n体脂 : 28.8%\\n水分 : 33.7%\\n肌肉 : 21.6%\\nBMI : 18.3\\n骨量 : 0%\\n高压 : 0 mmHg\\n低压 : 0 mmHg\\n心率 : 0 次/分钟\\n蛋白质 : 56\\nBMR : 6534 Kcal\\n内脏脂肪 : 1.29\\n体温 : 0\"}";
        String jsonText = "{\"0\":\"aaa\",\"1\":\"aaa\",\"2\":\"aaa\",\"3\":\"aaa\",\"4\":\"aaa\",\"5\":\"aaa\",\"6\":\"aaa\",\"7\":\"aaa\",\"8\":\"aaa\",\"9\":\"aaa\"}";
        Map<String, String> mapa = (Map<String, String>) JSONObject.parse(info);
        mapa.forEach((x,a) ->  System.out.println(a));
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>(16);
        Stream.iterate(0, x -> x+1).limit(10).forEach(x -> {
            map.put(x+"", "aaa");
        });
        JSONObject json = new JSONObject();
        json.putAll(map);
        System.out.println(json.toString());
    }
}