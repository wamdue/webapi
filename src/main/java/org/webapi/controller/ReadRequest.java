package org.webapi.controller;

import org.webapi.config.Connect;
import org.webapi.dao.EntryDao;
import org.webapi.model.Data;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Wamdue
 * @version 1.0
 * @since 12.03.2018
 */
public class ReadRequest extends HttpServlet {
    private EntryDao dao = new EntryDao(Connect.CONNECT.getConnection());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Data data = new Data();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSX");
        try {
            data.setDate(new Timestamp(sdf.parse(req.getParameter("Date")).getTime()));
            String temp = "";
            for (Map.Entry<String, String[]> map : req.getParameterMap().entrySet()) {

                if (map.getKey().equals("Date")) {
                    data.setDate(new Timestamp(sdf.parse(map.getValue()[0]).getTime()));
                } else {
                    if (map.getKey().contains("name")) {
                        temp = map.getValue()[0];
                    } else {
                        data.addData(temp, Integer.valueOf(map.getValue()[0]));
                    }
                }
                System.out.println(String.format("%s : %s", map.getKey(), Arrays.toString(map.getValue())));
            }
            this.dao.insertHead(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
//    f4c85649-f947-3170-b304-11feb0dc856a
}
