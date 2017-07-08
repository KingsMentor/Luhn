package xyz.belvi.luhn.customTextInputLayout.inputLayouts;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.design.widget.CheckableImageButton;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.util.AttributeSet;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import xyz.belvi.luhn.customTextInputLayout.transformations.CreditCardTransformation;

public class CardTextInputLayout extends TextInputLayout {
    private boolean hasValidInput;
    private Object collapsingTextHelper;
    private Rect bounds;
    private Method recalculateMethod;
    private CheckableImageButton mPasswordToggleView;
    private boolean hasUpdated;

    public CardTextInputLayout(Context context) {
        this(context, null);
    }

    public CardTextInputLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        adjustBounds();
        if (!hasUpdated) {
            try {
                getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
                Typeface face = Typeface.createFromAsset(getEditText().getContext().getAssets(), CalligraphyConfig.get().getFontPath());
                setTypeface(face);
                getEditText().setTypeface(face);
                updatePasswordToggleView();


                Field mPasswordToggleViewField = TextInputLayout.class.getDeclaredField("mPasswordToggleView");
                mPasswordToggleViewField.setAccessible(true);
                Object o = mPasswordToggleViewField.get(this);
                mPasswordToggleView = ((CheckableImageButton) o);

                passwordVisibilityToggleRequested();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            hasUpdated = true;
        }

    }

    private void init() {
        try {
            Field cthField = TextInputLayout.class.getDeclaredField("mCollapsingTextHelper");
            cthField.setAccessible(true);
            collapsingTextHelper = cthField.get(this);


            Field boundsField = collapsingTextHelper.getClass().getDeclaredField("mCollapsedBounds");
            boundsField.setAccessible(true);
            bounds = (Rect) boundsField.get(collapsingTextHelper);

            recalculateMethod = collapsingTextHelper.getClass().getDeclaredMethod("recalculate");

        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException e) {
            collapsingTextHelper = null;
            bounds = null;
            recalculateMethod = null;
            e.printStackTrace();
        }
    }

    private void adjustBounds() {
        if (collapsingTextHelper == null) {
            return;
        }

        try {
            bounds.left = getEditText().getLeft() + getEditText().getPaddingLeft();
            recalculateMethod.invoke(collapsingTextHelper);
        } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void toggleEnabled(String fieldName, boolean value) throws NoSuchFieldException, IllegalAccessException {
        Field cthField = null;
        cthField = TextInputLayout.class.getDeclaredField(fieldName);
        cthField.setAccessible(true);
        cthField.setBoolean(this, value);
    }

    private void updatePasswordToggleView() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method updatePasswordToggleView = TextInputLayout.class.getDeclaredMethod("updatePasswordToggleView");
        updatePasswordToggleView.setAccessible(true);
        updatePasswordToggleView.invoke(this);
    }

    public void passwordVisibilityToggleRequested() throws NoSuchFieldException, IllegalAccessException {
        // Store the current cursor position
        int selection = getEditText().getSelectionEnd();

        if (!getEditText().getText().toString().isEmpty()) {
            getEditText().setTransformationMethod(CreditCardTransformation.getInstance());
            toggleEnabled("mPasswordToggledVisible", false);
            mPasswordToggleView.setChecked(false);
        } else {
            getEditText().setTransformationMethod(null);
            toggleEnabled("mPasswordToggledVisible", true);
            mPasswordToggleView.setChecked(true);
        }
        // And restore the cursor position
        getEditText().setSelection(selection);

    }

    public boolean hasValidInput() {
        return this.hasValidInput;
    }

    public CardTextInputLayout setHasValidInput(boolean hasValidInput) {
        this.hasValidInput = hasValidInput;
        return this;
    }

    public CheckableImageButton getPasswordToggleView() {
        return mPasswordToggleView;
    }
}