package com.bristol.snorediaby.logic.modules;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.bristol.snorediaby.R;
import com.bristol.snorediaby.repo.domain.SnoreStorage;
import com.bristol.snorediaby.logic.snores.CustomMp3Recorder;
import com.czt.mp3recorder.MP3Recorder;
import com.shuyu.waveview.FileUtils;
import com.vondear.rxtools.RxVibrateTool;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SnoreRecorder {

    private final String TAG = this.getClass().getSimpleName();

    private final Handler mUIHandler;
    private final List<Integer> countStatus;
    private final List<Integer> countSnoring;
    private final List<Integer> integerList;
    private final ArrayList<SnoreStorage> snoreList;

    private Disposable subscribe;
    private Disposable subscribe1;
    private Disposable subscribe2;
    private Disposable subscribe3;
    private CustomMp3Recorder mRecorder;

    private final Context context;
    private int countOnce = 0;
    private int countMore = 0;
    private int count = 0;
    private final double setValue;
    private boolean isStartVibrate = false;
    private boolean isPause = false;
    private boolean stillSnore = false;
    private final boolean useVibra;
    private int normalBreath = 0;
    private int valueDB;
    private int breathAlert = 0;

    public SnoreRecorder(Context context, double setValue, boolean useVibra) {
        this.context = context;
        this.setValue = setValue;
        this.useVibra = useVibra;

        mUIHandler = new Handler();
        countStatus = new ArrayList<>();
        countSnoring = new ArrayList<>();
        integerList = new ArrayList<>();
        snoreList = new ArrayList<>();
    }

    //@SuppressLint("HandlerLeak")
    public void resolveRecord() {
        String filePath = FileUtils.getAppPath();
        File file = new File(filePath);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Toast.makeText(context, R.string.file_fail, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        filePath = FileUtils.getAppPath() + UUID.randomUUID().toString() + ".mp3";
        mRecorder = new CustomMp3Recorder(new File(filePath));
        mRecorder.setErrorHandler(new Handler(msg -> {
            if (Objects.equals(MP3Recorder.ERROR_TYPE, msg.what)) {
                Toast.makeText(context, R.string.no_microphone, Toast.LENGTH_SHORT).show();
            }
            return false;
        }));
        integerList.clear();
        countSnoring.clear();

        // 打鼾狀態檢測，一秒輪循檢測，監測機制
        // 大於60分貝的話記錄，一秒內超過某分貝即為打鼾(一秒內記錄值大約為20次，超過三次，低於十五次即為打鼾，否則為其他狀態)
        subscribe = Observable.interval(1, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
            @SuppressLint("CheckResult")
            @Override
            public void accept(Long aLong) {
                if (integerList.size() > 0) {
                    countOnce = 0;
                    countMore = 0;
                    count = 0;
                    Observable.fromIterable(integerList).subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) {
                            if (integer > setValue) {
                                countOnce += integer;
                                ++count;
                            }
                            countMore += integer;
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {

                        }
                    });
                    Log.e(TAG, "一秒內數據: " + Arrays.toString(integerList.toArray()));

                    int iOnce = countOnce / 3;
                    int iMore = countMore / integerList.size();

                    if (iOnce > setValue) countStatus.add(iOnce);
                    Log.e(TAG, "Count: " + count + "--");

                    if (isStartVibrate && iMore > setValue && count <= (16 >= integerList.size() ? integerList.size() : 16) && !isPause) {
                        countSnoring.add(iMore); //偵測到打呼
                        stillSnore = true; //還沒呼吸中止
                        if (useVibra) RxVibrateTool.vibrateOnce(context, 300);
                    }
                    integerList.clear();
                }
            }
        });
        countStatus.clear();

        //檢測是否處於打鼾狀態 每10秒啟動一次
        subscribe1 = Observable.interval(10, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) {
                if (5 <= countStatus.size()) {
                    //countSnoring.add(countStatus.size()); //偵測到打呼
                    if (!isStartVibrate && !isPause) {
                        if (useVibra) RxVibrateTool.vibrateOnce(context, 1000);
                    }
                    countStatus.clear();
                    isStartVibrate = true;
                    stillSnore = true;
                } else {
                    isStartVibrate = false;
                    stillSnore = false;
                }
            }
        });

        //開啟超時檢測打鼾，每1分鐘啟動一次，一定時間內開啟，否則關閉打鼾狀態
        subscribe2 = Observable.timer(1, TimeUnit.MINUTES)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidScheduler.mainThread())
            .subscribe(new Consumer<Long>() {
                @Override
                public void accept(Long aLong) {
                    isStartVibrate = false;
                    stillSnore = false;
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) {
                }
            });

        //偵測OSA，偵測到打呼關閉後，偵測10-60秒間是否又突然偵測到打呼，突然打呼則為OSA
        subscribe3 = Observable.interval(1, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) {
                if (!stillSnore) { //不打呼了
                    normalBreath++;
                } else { //在規定時間內突然打呼
                    if (normalBreath > 15 && normalBreath < 60 && isStartVibrate && valueDB > 30) {
                        breathAlert++; //偵測到呼吸中止
                    }
                    normalBreath = 0;
                }
            }
        });

        try {
            mRecorder.start(new CustomMp3Recorder.VolumeListener() {
                @Override
                public void onVolumeListener(final Double volume) {
                    mUIHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            valueDB = volume.intValue();
                            if (!isPause) {
                                integerList.add(valueDB);
                            }
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.permission_not, Toast.LENGTH_SHORT).show();
        }
    }

    public void stopSnoreRecorder() {
        if (!subscribe.isDisposed()) subscribe.dispose();
        if (!subscribe1.isDisposed()) subscribe1.dispose();
        if (!subscribe2.isDisposed()) subscribe2.dispose();
        if (!subscribe3.isDisposed()) subscribe3.dispose();

        stillSnore = false;
        isStartVibrate = false;
        isPause = true;

        if (mRecorder != null && mRecorder.isRecording()) {
            mRecorder.setPause(false);
            mRecorder.stop();
        }
    }

    public int getCountSnoring() {
        return countSnoring.size();
    }

    public int getBreathAlert() {
        return breathAlert;
    }

    public void inputList(SnoreStorage snoreStorage) {
        snoreList.add(snoreStorage);
    }

    public void clearCountSnoring() {
        countSnoring.clear();
    }

    public int getVolume() {
        return valueDB;
    }

    public int getNormalBreath() {
        return normalBreath;
    }

    public ArrayList<SnoreStorage> getSnoreList() {
        return snoreList;
    }

    public double snorePercent(double timeUseSec, int snore) {
        //計算百分比 每小時呼吸960次
        double hour = timeUseSec / 60 / 60;
        double breath = hour * 960;
        double vital = (double) snore / breath * 100;
        //double vital = (double) (snore * 100000) / (timeUseSec * 1000);
        if (vital > 100) vital = 100;

        return vital;
    }

}
