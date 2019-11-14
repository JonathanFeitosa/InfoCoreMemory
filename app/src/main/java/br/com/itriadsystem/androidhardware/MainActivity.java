package br.com.itriadsystem.androidhardware;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ArrayList<CPUCoreInfo> cpuCoreList;
    private ScheduledExecutorService scheduledExecutorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadCPUCore();

        if (!cpuCoreList.isEmpty()) {
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduledExecutorService.scheduleAtFixedRate(() -> {
                loadCPUCore();

                int g = 0;
                for(CPUCoreInfo teste : cpuCoreList){
                    g+=1;
                    Log.i("Resultado", teste.getCoreName() + " - Valor: " + teste.getCoreValue());
                }

            }, 1, 2, TimeUnit.SECONDS);
        }
    }

    private void loadCPUCore() {
        cpuCoreList = new ArrayList<>();
        for (int corecount = 0; corecount < Runtime.getRuntime().availableProcessors(); corecount++) {
            try {
                double currentFreq;
                RandomAccessFile readerCurFreq;
                readerCurFreq = new RandomAccessFile("/sys/devices/system/cpu/cpu" + corecount + "/cpufreq/scaling_cur_freq", "r");
                String curfreg = readerCurFreq.readLine();
                currentFreq = Double.parseDouble(curfreg) / 1000;
                readerCurFreq.close();
                cpuCoreList.add(new CPUCoreInfo(getString(R.string.core) + " " + corecount, (int) currentFreq + " Mhz"));

            } catch (Exception ex) {
                cpuCoreList.add(new CPUCoreInfo(getString(R.string.core) + " " + corecount, getString(R.string.idle)));
            }
        }
    }
}
