package com.example.utils;

import com.example.activity.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

/*
 * �Զ���EditText�ؼ�
 * 
 * view.OnFocusChangeListener���ڼ�������ĸı䣨�õ��������ʧȥ���㣩
 * 
 * TextWatcher����ʱ����Ҫ��EditText�������ƣ������������ƣ���ʱ��ͨ��TextWatcher�����м���
 */
public class SearchClearEditText extends EditText implements
		OnFocusChangeListener, TextWatcher {

	// ɾ����ť������
	private Drawable mClearDrawable;
	// �ؼ��Ƿ��н���
	private boolean hasFocus;

	public SearchClearEditText(Context context) {
		this(context, null);
	}

	public SearchClearEditText(Context context, AttributeSet attrs) {
		// ���ﹹ�췽������Ҫ����������ܶ����Բ�����XML�ļ��ж���
		this(context, attrs, android.R.attr.editTextStyle);
	}

	// ��д�������������Ĺ��췽���ᱨ��
	public SearchClearEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		// ���EditText��DrawableRight������û�����þ�ʹ��Ĭ�ϵ�ͼƬ
		mClearDrawable = getCompoundDrawables()[2]; // [2]=right
		if (mClearDrawable == null) {
			mClearDrawable = getResources()
					.getDrawable(R.drawable.search_clear);
		}
		mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(),
				mClearDrawable.getIntrinsicHeight()); // ����ͼ�ı߽磬�зŴ���С������
		// Ĭ����������ͼ��
		setClearIconVisible(false);
		// ���ý���ı�ļ���
		setOnFocusChangeListener(this);
		// ����������������ݷ����ı�ļ���
		addTextChangedListener(this);

	}

	/*
	 * ��Ϊ���ǲ���ֱ�Ӹ�EditText���õ���¼������������ü�ס���ǰ��µ�λ����ģ�����¼� �����ǰ��µ�λ�� �� EditText�Ŀ�� -
	 * ͼ�굽�ؼ��ұߵļ�� - ͼ��Ŀ�� �� EditText�Ŀ�� - ͼ�굽�ؼ��ұߵļ��֮�����Ǿ�������ͼ�꣬��ֱ�����û�п���
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) { // ACTION_UP�ǰ���̧��
			if (getCompoundDrawables()[2] != null) {

				// ����ָ̧���λ����clean��ͼ������� ���ǽ�����Ϊ����������� ��
				// getWidth():�õ��ؼ��Ŀ��
				// event.getX():̧��ʱ������(������������ڿؼ�������Ե�)
				// getTotalPaddingRight():clean��ͼ�����Ե���ؼ��ұ�Ե�ľ���
				// getPaddingRight():clean��ͼ���ұ�Ե���ؼ��ұ�Ե�ľ���
				// getWidth() - getTotalPaddingRight()��ʾ: �ؼ���ߵ�clean��ͼ�����Ե������
				// getWidth() - getPaddingRight()��ʾ: �ؼ���ߵ�clean��ͼ���ұ�Ե������
				// ����������֮�������պ���clean��ͼ�������
				boolean touchable = (event.getX() > (getWidth() - getTotalPaddingRight()))
						&& (event.getX() < (getWidth() - getPaddingRight()));
				if (touchable) {
					setText("");
				}
			}
		}
		return super.onTouchEvent(event);
	}

	/*
	 * �������ͼ�����ʾ������,����setCompoundDrawablesΪEditText������ȥ
	 */
	private void setClearIconVisible(boolean visible) {
		Drawable right = visible ? mClearDrawable : null;
		// setCompoundDrawables(Drawable left, Drawable top, Drawable right,
		// Drawable bottom)�������������ҵ�ͼ��
		setCompoundDrawables(getCompoundDrawables()[0],
				getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
	}

	/*
	 * ��SearchClearEditText���㷢���仯��ʱ���ж������ַ��������������ͼ�����ʾ������
	 */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		this.hasFocus = hasFocus;
		/*
		 * �����ʱ�򣬽���ͻ�ı䣬hasFocus = true��Ȼ�����ʾ���ͼ��
		 */
		if (hasFocus) {
			setClearIconVisible(getText().length() > 0);
		} else {
			setClearIconVisible(false);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	/*
	 * �����������ݷ����ı��ʱ��ص��ķ���
	 */
	@Override
	public void onTextChanged(CharSequence text, int start, int lengthBefore,
			int lengthAfter) {
		// super.onTextChanged(text, start, lengthBefore, lengthAfter);
		if (hasFocus) {
			setClearIconVisible(text.length() > 0);
		}
	}

	/*
	 * ���ûζ�����
	 */
	public void setShakeAnimation() {
		this.setAnimation(shakeAnimation(5));
	}

	/*
	 * �ζ����� @param counts��һ��ζ�������
	 */
	private Animation shakeAnimation(int counts) {
		Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
		translateAnimation.setInterpolator(new CycleInterpolator(counts));
		translateAnimation.setDuration(1000);
		return translateAnimation;
	}

}
