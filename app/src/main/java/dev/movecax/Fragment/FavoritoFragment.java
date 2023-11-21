package dev.movecax.Fragment;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.fragment.app.Fragment;

import dev.movecax.R;

public class FavoritoFragment extends Fragment {

    private ViewFlipper viewFlipper;
    private ImageView[] dots;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private int currentIndex = 0;

    private int[] imageResIds = {R.drawable.anun1, R.drawable.anun2, R.drawable.anun3};
    private String[] titles = {"Título 1", "Título 2", "Título 3"};
    private String[] descriptions = {"Descripción 1", "Descripción 2", "Descripción 3"};

    private GestureDetector gestureDetector;

    public FavoritoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        viewFlipper = view.findViewById(R.id.viewFlipper);

        // Agrega las tres imágenes al ViewFlipper
        for (int i = 0; i < imageResIds.length; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(imageResIds[i]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            viewFlipper.addView(imageView);
        }

        titleTextView = view.findViewById(R.id.titleTextView);
        descriptionTextView = view.findViewById(R.id.descriptionTextView);

        // Inicializa los puntos
        initializeDots(view);

        // Configura el detector de gestos táctiles
        gestureDetector = new GestureDetector(getContext(), new GestureListener());

        // Agrega el listener táctil al ViewFlipper
        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true; // Debe devolver true para indicar que el evento ha sido manejado
            }
        });

        return view;
    }

    private void initializeDots(View view) {
        ViewGroup dotsContainer = view.findViewById(R.id.dotsContainer);

        dots = new ImageView[imageResIds.length];

        for (int i = 0; i < imageResIds.length; i++) {
            dots[i] = new ImageView(getContext());
            dots[i].setImageResource(R.drawable.dot_selector);
            dots[i].setPadding(8, 0, 8, 0); // Ajusta según sea necesario
            dotsContainer.addView(dots[i]);
        }

        dots[currentIndex].setSelected(true);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();

                if (Math.abs(diffX) > Math.abs(diffY) &&
                        Math.abs(diffX) > SWIPE_THRESHOLD &&
                        Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        viewFlipper.showPrevious();
                    } else {
                        viewFlipper.showNext();
                    }
                    return true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return false;
        }
    }

    private void updateDots(int position) {
        for (int i = 0; i < dots.length; i++) {
            dots[i].setSelected(i == position);
        }
    }
}
