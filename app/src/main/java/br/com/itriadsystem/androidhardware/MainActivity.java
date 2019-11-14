package br.com.itriadsystem.androidhardware;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerCPU;

    private ArrayList<CPUCoreInfo> cpuCoreList, cpuCoreList2;
    private CPUCoreAdapter cpuCoreAdapter;
    private ScheduledExecutorService scheduledExecutorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerCPU = (RecyclerView) findViewById(R.id.recyclerCPU);
        recyclerCPU.setItemAnimator(null);
        loadCPUCore();
        int columns;
        if (Runtime.getRuntime().availableProcessors() == 2) {
            columns = 2;
        } else {
            columns = 4;
        }
        GridLayoutManager layoutManager = new GridLayoutManager(this, columns);
        cpuCoreAdapter = new CPUCoreAdapter(this, cpuCoreList);
        recyclerCPU.setLayoutManager(layoutManager);
        recyclerCPU.setAdapter(cpuCoreAdapter);
        recyclerCPU.setNestedScrollingEnabled(false);

        if (!cpuCoreList.isEmpty()) {
            cpuCoreList2 = new ArrayList<>();
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduledExecutorService.scheduleAtFixedRate(() -> {
                loadCPUCore();
                cpuCoreList2 = cpuCoreList;
                recyclerCPU.post(() ->
                        cpuCoreAdapter.updateCPUCoreListItems(cpuCoreList2)
                );
            }, 1, 2, TimeUnit.SECONDS);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if (recyclerCPU != null) {
            LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this, R.anim.recycler_layout_animation);
            recyclerCPU.setLayoutAnimation(controller);
            recyclerCPU.scheduleLayoutAnimation();
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
