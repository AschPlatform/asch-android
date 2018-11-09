package asch.io.widget.edittext;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import asch.io.widget.R;


public class PassWordEditText extends RelativeLayout implements View.OnClickListener {

    RelativeLayout mainLayout;
    ImageView eyeIv;
    ImageView clearIv;
    EditText editText;



    public PassWordEditText(Context context,int id,View rootView) {
        super(context);
        mainLayout = (RelativeLayout) rootView.findViewById(id);
        eyeIv = (ImageView)mainLayout.findViewById(R.id.eye);
        eyeIv.setOnClickListener(this);
        eyeIv.setVisibility(View.INVISIBLE);
        clearIv = (ImageView)mainLayout.findViewById(R.id.clear);
        clearIv.setVisibility(View.INVISIBLE);
        clearIv.setOnClickListener(this);
        editText = (EditText)mainLayout.findViewById(R.id.passwd_et);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length()>0){
                    clearIv.setVisibility(View.VISIBLE);
                    eyeIv.setVisibility(View.VISIBLE);
                }else {
                    clearIv.setVisibility(View.INVISIBLE);
                    eyeIv.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    public String getText(){
        return editText.getText().toString();
    }

    public void setHint(String hint){
        editText.setHint(hint);
    }




    @Override
    public void onClick(View v) {
        if (v==eyeIv){
            if (editText.getInputType()== InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                eyeIv.setImageResource(R.drawable.icon_nodisplay);
            }
            else {
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                eyeIv.setImageResource(R.drawable.icon_xians);
            }
            editText.setSelection(editText.getText().toString().length());
        }

        if (v==clearIv){
            editText.setText("");
        }

    }



    private void setPasswordVisible(){
        editText.setTransformationMethod(null);
    }

    private void setPasswordInvisible(){
        editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }


}
