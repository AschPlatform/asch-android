package asch.so.widget.edittext;

import android.content.Context;
import android.media.Image;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import asch.so.widget.R;


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
        clearIv = (ImageView)mainLayout.findViewById(R.id.clear);
        clearIv.setOnClickListener(this);
        editText = (EditText)mainLayout.findViewById(R.id.passwd_et);

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
            }
            else {
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
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
