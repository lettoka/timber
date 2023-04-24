package com.example.gerenda.database;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import androidx.databinding.DataBindingComponent;

import com.example.gerenda.model.StockModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    private static DatabaseHandler instance;
    final static String url = "jdbc:mysql://" + "ip" + ":3306/" + "databasename";
    private DatabaseHandler() {

    }
    public static DatabaseHandler getInstance() {
        if(instance == null)
            instance = new DatabaseHandler();

        return instance;
    }
    /*static public <Type> List<Type> getAllRows(DatabaseTransformable dt, final String table, final String search, final String order) {
        new AsyncTask<Void, Void, Pair<String,List<Type>>>() {

            // Get a list of the user's devices
            protected Pair<String,List<Type>> doInBackground(Void... params) {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection connection = DriverManager.getConnection(url, "GetDatabaseUsername()", "GetDatabasePassword"());
                    Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery("SELECT * FROM "+table+" WHERE 1 AND "+search+" ORDER BY "+order+" ;");
                    ResultSetMetaData rsmd = rs.getMetaData();

                    List<Type> list= new ArrayList<>();


                    while (rs.next()) {
                        list.add(dt.TransformData(rs));

                    }

                    return new Pair<String, List<Type>>("SIKER",processingItems);


                } catch (SQLException error) {
                    error.printStackTrace();
                    return new Pair<String, List<ProcessingItem>>(error.toString(),null);
                } catch (ClassNotFoundException error) {
                    error.printStackTrace();
                    return new Pair<String, List<ProcessingItem>>(error.toString(),null);                }
            }

            protected void onPostExecute(final Pair<String,List<ProcessingItem>> data) {
                if (data.first.equals("SIKER")) {
                    ctx.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ctx.prodmanResult.SetSuccessful(data.second,0,-1, ProdmanResult.ACTION.INFO,ctx);

                        }
                    });
                } else {
                    ctx.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ctx.prodmanResult.SetError(null,data.first);

                        }
                    });
                }
            }
        }.execute();
    }*/
}
