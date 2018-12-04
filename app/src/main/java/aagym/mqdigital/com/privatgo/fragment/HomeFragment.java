package aagym.mqdigital.com.privatgo.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import aagym.mqdigital.com.privatgo.FavoriteTeacherActivity;
import aagym.mqdigital.com.privatgo.MapsActivity;
import aagym.mqdigital.com.privatgo.R;
import aagym.mqdigital.com.privatgo.adapter.BannerAdapter;
import aagym.mqdigital.com.privatgo.entity.Banner;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomeFragment extends Fragment {

    @BindView(R.id.favorite_teacher)
    TextView favoriteTeacher;
    Unbinder unbinder;
    @BindView(R.id.carousel)
    DiscreteScrollView carousel;
    Calendar calendar;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.
    @BindView(R.id.date)
    AutoCompleteTextView dateLabel;
    @BindView(R.id.teacher)
    AutoCompleteTextView teacher;
    @BindView(R.id.category)
    AutoCompleteTextView category;
    @BindView(R.id.course)
    AutoCompleteTextView course;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<Banner> bannerList = new ArrayList<>();
        bannerList.add(new Banner(R.drawable.banner));
        bannerList.add(new Banner(R.drawable.banner));
        bannerList.add(new Banner(R.drawable.banner));
        BannerAdapter bannerAdapter = new BannerAdapter(bannerList);
        InfiniteScrollAdapter wrapper = InfiniteScrollAdapter.wrap(bannerAdapter);
        carousel.setAdapter(bannerAdapter);
        carousel.setSlideOnFling(true);
        carousel.setSlideOnFlingThreshold(2100);
        carousel.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.05f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.BOTTOM) // CENTER is a default one
                .build());
        calendar = Calendar.getInstance();

//        /*After setting the adapter use the timer */
//        final Handler handler = new Handler();
//        // run banner animation
//        final Runnable Update = new Runnable() {
//            boolean forward = true;
//            int currentPage = carousel.getCurrentItem(); //returns adapter position of the currently selected item or -1 if adapter is empty.
//            public void run() {
//                if (currentPage == 2) {
//                    forward = false;
//                }else if(currentPage ==0){
//                    forward = true;
//                }
//                Log.i("currentPage ", ""+currentPage);
//                if(forward){
//                    carousel.smoothScrollToPosition(currentPage++);
//                }
//                else{
//                    carousel.smoothScrollToPosition(currentPage--);
//                }
//            }
//        };
//
//        timer = new Timer(); // This will create a new Thread
//        timer.schedule(new TimerTask() { // task to be scheduled
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, DELAY_MS, PERIOD_MS);
    }

    private void showDatePicker() {
        final Dialog dialog = new Dialog(getActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.date_picker);
        dialog.setTitle("");
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER_VERTICAL;
        window.setDimAmount(0.5f);
        window.setAttributes(wlp);

        MaterialCalendarView datePicker = (MaterialCalendarView) dialog.findViewById(R.id.date_picker);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int selectedDate = calendar.get(Calendar.DAY_OF_MONTH);
        int selectedMonth = calendar.get(Calendar.MONTH);
        int selectedYear = calendar.get(Calendar.YEAR);
        datePicker.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {

                String myFormat = "dd-MM-yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                String dateStr = sdf.format(calendarDay.getDate());
                dateLabel.setText(dateStr);
                new Timer().schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                // your code here
                                dialog.dismiss();
                            }
                        },
                        100
                );
            }
        });
        dialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.teacher, R.id.favorite_teacher, R.id.date, R.id.category, R.id.course})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.category:
                showCategory();
                break;
            case R.id.course:
                showCourse();
                break;
            case R.id.teacher:
                intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
                break;
            case R.id.date:
                showDatePicker();
                break;
            case R.id.favorite_teacher:
                startActivity(new Intent(getContext(), FavoriteTeacherActivity.class));
                break;
        }
    }
    private void showCategory() {
        final ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getResources()
                .getStringArray(R.array.category));
        category.setAdapter(arrayAdapter);
        category.setCursorVisible(false);
        category.showDropDown();
        category.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                category.showDropDown();
                String selection = (String) parent.getItemAtPosition(position);
                Toast.makeText(getContext(), selection,
                        Toast.LENGTH_SHORT);
            }
        });
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category.showDropDown();
            }
        });
    }

    private void showCourse() {
        final ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getResources()
                .getStringArray(R.array.course));
        course.setAdapter(arrayAdapter);
        course.setCursorVisible(false);
        course.showDropDown();
        course.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                course.showDropDown();
                String selection = (String) parent.getItemAtPosition(position);
                Toast.makeText(getContext(), selection,
                        Toast.LENGTH_SHORT);
            }
        });
        course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                course.showDropDown();
            }
        });
    }
}
