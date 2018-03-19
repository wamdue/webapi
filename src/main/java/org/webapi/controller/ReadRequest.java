package org.webapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.webapi.config.Connect;
import org.webapi.dao.DataDao;
import org.webapi.model.Data;
import org.webapi.model.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
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
    private DataDao dao = new DataDao(Connect.CONNECT.getConnection());
    /**
     * Date formatter for parsing timestamp in two ways.
     */
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSX");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String app = "application/json";
        resp.setContentType(app);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(this.sdf);
        if (id != null) {
            Data data = this.dao.getEntry(id);
            if (data.getDate() == null) {
                resp.sendError(404, "Cannot find data in DB.");
            } else {
                OutputStream out = resp.getOutputStream();
                mapper.writeValue(out, data);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Data data = new Data();
        try {
            data.setDate(new Timestamp(this.sdf.parse(req.getParameter("Date")).getTime()));
            Entry entry = new Entry();
            for (Map.Entry<String, String[]> map : req.getParameterMap().entrySet()) {

                if (map.getKey().equals("Date")) {
                    data.setDate(new Timestamp(this.sdf.parse(map.getValue()[0]).getTime()));
                } else {
                    if (map.getKey().contains("name")) {
                        entry.setName(map.getValue()[0]);
                    } else {
                        entry.setValue(Integer.valueOf(map.getValue()[0]));
                        data.addData(entry);
                        entry = new Entry();
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
