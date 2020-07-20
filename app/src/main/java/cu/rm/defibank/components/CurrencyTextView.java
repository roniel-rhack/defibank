package cu.rm.defibank.components;

import android.content.Context;
import android.util.AttributeSet;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class CurrencyTextView extends androidx.appcompat.widget.AppCompatTextView {

    String rawText;

    public CurrencyTextView(Context context) {
        super(context);
    }

    public CurrencyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CurrencyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        rawText = text.toString();
        String prezzo = text.toString();
        try {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator('.');
            DecimalFormat decimalFormat = new DecimalFormat("$ ###,###,###,###.##", symbols);
            prezzo = decimalFormat.format(Float.parseFloat(text.toString()));
        }catch (Exception e){}

        super.setText(prezzo, type);
    }
    @Override
    public CharSequence getText() {

        return rawText;
    }
}
