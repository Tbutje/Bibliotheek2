/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.calco.bibliotheek2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author TKoole
 */
public class Database {

    private Connection getConnection() throws SQLException, NamingException {
        Context context = new InitialContext();
        DataSource dataSource
                = (DataSource) context.lookup("jdbc/Bibliotheek");
        Connection connection = dataSource.getConnection();
        return connection;
    }

    public List<Categorie> getCategorieen() throws SQLException, NamingException {
        List<Categorie> result = new ArrayList<>();
        String sql = "select * from Categorien order by Omschrijving";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Categorie categorie = new Categorie();
                categorie.setCategorieID(resultSet.getInt("Categorie_ID"));
                categorie.setOmschrijving(resultSet.getString("Omschrijving"));
                result.add(categorie);
            }
        } finally {
            if (resultSet != null && !resultSet.isClosed()) {
                resultSet.close();
            }
            if (preparedStatement != null && !preparedStatement.isClosed()) {
                preparedStatement.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }

        return result;
    }

    // functies voor mutaties database
    public void insertCategorie(Categorie categorie) throws NamingException, SQLException {

        String sql = "insert into Categorien (omschrijving) values (?)";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            // dit is 1 omdat je het eerste vraagteken aanpast
            preparedStatement.setString(1, categorie.getOmschrijving());
            preparedStatement.executeUpdate();

        } finally {
            if (preparedStatement != null && !preparedStatement.isClosed()) {
                preparedStatement.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }

    }

    public void updateCategorie(Categorie categorie) throws NamingException, SQLException {

        String sql = "update Categorien set Omschrijving = ? where Categorie_ID = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            // dit is 1 omdat je het eerste vraagteken aanpast
            preparedStatement.setString(1, categorie.getOmschrijving());
            preparedStatement.setInt(2, categorie.getCategorieID());
            preparedStatement.executeUpdate();

        } finally {
            if (preparedStatement != null && !preparedStatement.isClosed()) {
                preparedStatement.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }

    }

    public void verwijderCategorie(Categorie categorie) throws NamingException, SQLException {

        String sql = "delete from Categorien where Categorie_ID = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            // dit is 1 omdat je het eerste vraagteken aanpast
            preparedStatement.setInt(1, categorie.getCategorieID());
            preparedStatement.executeUpdate();

        } finally {
            if (preparedStatement != null && !preparedStatement.isClosed()) {
                preparedStatement.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }

    }
}
