package elite.com.smartcircle.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import elite.com.smartcircle.R;
import elite.com.smartcircle.model.InfoModel;
import elite.com.smartcircle.util.CommonAdapter;
import elite.com.smartcircle.util.ViewHolder;

/**
 * @author Wesker
 * @create 2019-01-24 15:12
 */
public class InfoActivity extends Activity {

    private static final String TAG = "InfoActivity";

    private ListView listView;
    private CommonAdapter<InfoModel> adapter;
    private List<InfoModel> infoResultList = new ArrayList<>(64);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        createFatData();
        listView = findViewById(R.id.list_info);
        adapter = new CommonAdapter<InfoModel>(this, R.layout.item_info, LayoutInflater.from(this), infoResultList) {
            @Override
            public void convert(ViewHolder holder, InfoModel mInfoModel, int position) {
               TextView time =  holder.getView(R.id.tv_info_time);
               TextView blood =  holder.getView(R.id.tv_info_blood);
               TextView fat =  holder.getView(R.id.tv_info_fat);
               TextView rate =  holder.getView(R.id.tv_info_rate);
               TextView weight =  holder.getView(R.id.tv_info_weight);
               TextView temp =  holder.getView(R.id.tv_info_temp);
                time.setText(infoResultList.get(position).getTime());
                blood.setText(infoResultList.get(position).getBlood());
                fat.setText(infoResultList.get(position).getFat());
                rate.setText(infoResultList.get(position).getRate());
                weight.setText(infoResultList.get(position).getWeight());
                temp.setText(infoResultList.get(position).getTemp());
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Log.d(TAG, "click : " + position);
        });
    }

    private void createFatData() {
        Stream.iterate(0, x -> x+1)
                .limit(50)
                .forEach(x -> {
                    InfoModel mInfoModel = new InfoModel();
                    mInfoModel.setTime(LocalDateTime.now().toString());
                    mInfoModel.setBlood("100/120");
                    mInfoModel.setTemp("35.5度");
                    mInfoModel.setFat("20.2");
                    mInfoModel.setRate("81");
                    mInfoModel.setWeight("60kg");
                    infoResultList.add(mInfoModel);
                });
    }
}
