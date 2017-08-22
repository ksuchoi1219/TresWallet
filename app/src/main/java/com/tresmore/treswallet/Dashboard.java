package com.tresmore.treswallet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Dashboard extends AppCompatActivity {

    private static final int BLACK = 0xFFFFFFFF;
    private static final int WHITE = 0xFF000000;
    private final static int WIDTH=500;
    private TextView userCommission;
    private TextView userTPCredit;
    private TextView userCashbackCredit;
    private TextView userTrespass;
    private TextView userTotalCommission;
    private ImageView userQRcode;
    //private ConnectionClass connectionClass;
    private String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;


        userQRcode = (ImageView) findViewById(R.id.userQRcode);
        userCommission = (TextView) findViewById(R.id.userCommission);
        userTPCredit = (TextView) findViewById(R.id.userTPCredit);
        userCashbackCredit = (TextView) findViewById(R.id.userCashbackCredit);
        userTrespass = (TextView) findViewById(R.id.userTrespass);
        userTotalCommission = (TextView) findViewById(R.id.userTotalCommission);

        String STRuserCommission = userCommission.getText().toString();
        float FuserComission = Float.valueOf(STRuserCommission);
        String STRuserTPCredit = userTPCredit.getText().toString();
        float FuserTPCredit = Float.valueOf(STRuserTPCredit);
        String STRuserCashbackCredit = userCashbackCredit.getText().toString();
        float FuserCashbackCredit = Float.valueOf(STRuserCashbackCredit);
        String STRuserTrespass = userTrespass.getText().toString();
        float FuserTrespass = Float.valueOf(STRuserTrespass);

        float totalCommission = FuserCashbackCredit + FuserComission + FuserTPCredit + FuserTrespass;
        String STRtotalCommission = String.valueOf(totalCommission);
        userTotalCommission.setText(STRtotalCommission);

        SharedPreferences prefs = getSharedPreferences("MA", MODE_PRIVATE);
        username = prefs.getString("UN", "UNKNOWN");
        String userQRinfo = username + " " + STRtotalCommission;

        try {
            Bitmap bitmap = encodeAsBitmap(userQRinfo);
            userQRcode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }



//        connectionClass = new ConnectionClass();

//        Connection con = connectionClass.CONN();
//        String query = "select commission from dbo.users where loginid='" + username + "';";
//        ResultSet rs;
//
//        try {
//            Statement stmt = con.createStatement();
//            rs = stmt.executeQuery(query);
//            while (rs.next()) {
//                double money = Double.parseDouble(rs.getString(1));
//                String moneyForm = String.format("%.2f", new BigDecimal(money));
//                userCommissionTV.setText("$ " + moneyForm);
//                userMemberCommissionTV.setText("$ " + moneyForm);
//            }
//            con.close();
//        } catch (Exception ex) {
//            ex.getMessage();
//        }
    }
    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        Bitmap bitmap = null;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);

            int w = result.getWidth();
            int h = result.getHeight();
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                int offset = y * w;
                for (int x = 0; x < w; x++) {
                    pixels[offset + x] = result.get(x, y) ? WHITE : BLACK;
                }
            }
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        } catch (Exception iae) {
            iae.printStackTrace();
            return null;
        }
        return bitmap;
    }

}
