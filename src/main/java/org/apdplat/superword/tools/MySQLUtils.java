/*
 * APDPlat - Application Product Development Platform
 * Copyright (c) 2013, 杨尚川, yang-shangchuan@qq.com
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.apdplat.superword.tools;


import org.apache.commons.lang.StringUtils;
import org.apdplat.superword.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据存储层
 * @author 杨尚川
 */
public class MySQLUtils {
    private static final Logger LOG = LoggerFactory.getLogger(MySQLUtils.class);

    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/superword?useUnicode=true&characterEncoding=utf8";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            LOG.error("MySQL驱动加载失败：", e);
        }
    }

    private MySQLUtils() {
    }

    public static boolean login(User user){
        String sql = "select password from user where user_name=?";
        Connection con = getConnection();
        if(con == null){
            return false;
        }
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, user.getUserName());
            rs = pst.executeQuery();
            if (rs.next()) {
                String password = rs.getString(1);
                if(StringUtils.isNotBlank(user.getPassword()) && user.getPassword().equals(password)){
                    return true;
                }
            }
        } catch (SQLException e) {
            LOG.error("登录失败", e);
        } finally {
            close(con, pst, rs);
        }
        return false;
    }

    public static boolean register(User user) {
        String sql = "insert into user (user_name, password, date_time) values (?, ?, ?)";
        Connection con = getConnection();
        if(con == null){
            return false;
        }
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, user.getUserName());
            pst.setString(2, user.getPassword());
            pst.setTimestamp(3, new Timestamp(user.getDateTime().getTime()));
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            LOG.error("注册失败", e);
        } finally {
            close(con, pst, rs);
        }
        return false;
    }

    public static UserText getUseTextFromDatabase(int id) {
        String sql = "select id,text,date_time,user_name from user_text where id=?";
        Connection con = getConnection();
        if(con == null){
            return null;
        }
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                int _id = rs.getInt(1);
                String text = rs.getString(2);
                Timestamp timestamp = rs.getTimestamp(3);;
                String user_name = rs.getString(4);
                UserText userText = new UserText();
                userText.setId(id);
                userText.setText(text);
                userText.setDateTime(new java.util.Date(timestamp.getTime()));
                userText.setUserName(user_name);
                return userText;
            }
        } catch (SQLException e) {
            LOG.error("查询失败", e);
        } finally {
            close(con, pst, rs);
        }
        return null;
    }

    public static List<UserBook> getHistoryUseBooksFromDatabase(String userName) {
        List<UserBook> userBooks = new ArrayList<>();
        String sql = "select id,book,date_time from user_book where user_name=?";
        Connection con = getConnection();
        if(con == null){
            return userBooks;
        }
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, userName);
            rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                String book = rs.getString(2);
                Timestamp timestamp = rs.getTimestamp(3);
                UserBook userBook = new UserBook();
                userBook.setId(id);
                userBook.setBook(book);
                userBook.setDateTime(new java.util.Date(timestamp.getTime()));
                userBook.setUserName(userName);
                userBooks.add(userBook);
            }
        } catch (SQLException e) {
            LOG.error("查询失败", e);
        } finally {
            close(con, pst, rs);
        }
        return userBooks;
    }

    public static List<UserUrl> getHistoryUseUrlsFromDatabase(String userName) {
        List<UserUrl> userUrls = new ArrayList<>();
        String sql = "select id,url,date_time from user_url where user_name=?";
        Connection con = getConnection();
        if(con == null){
            return userUrls;
        }
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, userName);
            rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                String url = rs.getString(2);
                Timestamp timestamp = rs.getTimestamp(3);
                UserUrl userUrl = new UserUrl();
                userUrl.setId(id);
                userUrl.setUrl(url);
                userUrl.setDateTime(new java.util.Date(timestamp.getTime()));
                userUrl.setUserName(userName);
                userUrls.add(userUrl);
            }
        } catch (SQLException e) {
            LOG.error("查询失败", e);
        } finally {
            close(con, pst, rs);
        }
        return userUrls;
    }

    public static List<UserText> getHistoryUseTextsFromDatabase(String userName) {
        List<UserText> userTexts = new ArrayList<>();
        String sql = "select id,text,date_time from user_text where user_name=?";
        Connection con = getConnection();
        if(con == null){
            return userTexts;
        }
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, userName);
            rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                String text = rs.getString(2);
                Timestamp timestamp = rs.getTimestamp(3);
                UserText userText = new UserText();
                userText.setId(id);
                userText.setText(text);
                userText.setDateTime(new java.util.Date(timestamp.getTime()));
                userText.setUserName(userName);
                userTexts.add(userText);
            }
        } catch (SQLException e) {
            LOG.error("查询失败", e);
        } finally {
            close(con, pst, rs);
        }
        return userTexts;
    }

    public static List<UserWord> getHistoryUserWordsFromDatabase(String userName) {
        List<UserWord> userWords = new ArrayList<>();
        String sql = "select word,dictionary,date_time from user_word where user_name=?";
        Connection con = getConnection();
        if(con == null){
            return userWords;
        }
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, userName);
            rs = pst.executeQuery();
            while (rs.next()) {
                String word = rs.getString(1);
                String dictionary = rs.getString(2);
                Timestamp timestamp = rs.getTimestamp(3);
                UserWord userWord = new UserWord();
                userWord.setWord(word);
                userWord.setDictionary(dictionary);
                userWord.setDateTime(new java.util.Date(timestamp.getTime()));
                userWord.setUserName(userName);
                userWords.add(userWord);
            }
        } catch (SQLException e) {
            LOG.error("查询失败", e);
        } finally {
            close(con, pst, rs);
        }
        return userWords;
    }

    public static void saveUserBookToDatabase(UserBook userBook) {
        String sql = "insert into user_book (user_name, book, md5, date_time) values (?, ?, ?, ?)";
        Connection con = getConnection();
        if(con == null){
            return ;
        }
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, userBook.getUserName());
            pst.setString(2, userBook.getBook());
            pst.setString(3, MD5(userBook.getUserName()+userBook.getBook()));
            pst.setTimestamp(4, new Timestamp(userBook.getDateTime().getTime()));
            pst.executeUpdate();
        } catch (SQLException e) {
            LOG.error("保存失败", e);
        } finally {
            close(con, pst, rs);
        }
    }

    public static void saveUserUrlToDatabase(UserUrl userUrl) {
        String sql = "insert into user_url (user_name, url, md5, date_time) values (?, ?, ?, ?)";
        Connection con = getConnection();
        if(con == null){
            return ;
        }
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, userUrl.getUserName());
            pst.setString(2, userUrl.getUrl());
            pst.setString(3, MD5(userUrl.getUserName()+userUrl.getUrl()));
            pst.setTimestamp(4, new Timestamp(userUrl.getDateTime().getTime()));
            pst.executeUpdate();
        } catch (SQLException e) {
            LOG.error("保存失败", e);
        } finally {
            close(con, pst, rs);
        }
    }

    public static void saveUserTextToDatabase(UserText userText) {
        String sql = "insert into user_text (user_name, text, md5, date_time) values (?, ?, ?, ?)";
        Connection con = getConnection();
        if(con == null){
            return ;
        }
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, userText.getUserName());
            pst.setString(2, userText.getText());
            pst.setString(3, MD5(userText.getUserName() + userText.getText()));
            pst.setTimestamp(4, new Timestamp(userText.getDateTime().getTime()));
            pst.executeUpdate();
        } catch (SQLException e) {
            LOG.error("保存失败", e);
        } finally {
            close(con, pst, rs);
        }
    }

    public static void saveUserWordToDatabase(UserWord userWord) {
        String sql = "insert into user_word (user_name, word, dictionary, date_time) values (?, ?, ?, ?)";
        Connection con = getConnection();
        if(con == null){
            return ;
        }
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, userWord.getUserName());
            pst.setString(2, userWord.getWord());
            pst.setString(3, userWord.getDictionary());
            pst.setTimestamp(4, new Timestamp(userWord.getDateTime().getTime()));
            pst.executeUpdate();
        } catch (SQLException e) {
            LOG.error("保存失败", e);
        } finally {
            close(con, pst, rs);
        }
    }

    public static Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            LOG.error("MySQL获取数据库连接失败：", e);
        }
        return con;
    }

    public static void close(Statement st) {
        close(null, st, null);
    }

    public static void close(Statement st, ResultSet rs) {
        close(null, st, rs);
    }

    public static void close(Connection con, Statement st, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (st != null) {
                st.close();
                st = null;
            }
            if (con != null) {
                con.close();
                con = null;
            }
        } catch (SQLException e) {
            LOG.error("数据库关闭失败", e);
        }
    }

    public static void close(Connection con, Statement st) {
        close(con, st, null);
    }

    public static void close(Connection con) {
        close(con, null, null);
    }
    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
    public static void main(String[] args) throws Exception {
        UserWord userWord = new UserWord();
        userWord.setDateTime(new Date(System.currentTimeMillis()));
        userWord.setWord("fabulous");
        userWord.setUserName("ysc");
        MySQLUtils.saveUserWordToDatabase(userWord);

        System.out.println(MySQLUtils.getHistoryUserWordsFromDatabase("ysc"));
    }
}