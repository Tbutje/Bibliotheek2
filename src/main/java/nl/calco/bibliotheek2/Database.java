/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.calco.bibliotheek2;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
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

    //functies voor hoofdscherm
    public List<Boek> getBoeken(String filter) throws SQLException, NamingException {
        List<Boek> result = new ArrayList<>();
        filter = "%" + (filter == null ? "" : filter) + "%";
        String sql = "select * from Boeken"
                + " left join Categorien"
                + " on Boeken.Categorie_ID = Categorien.Categorie_ID"
                + " where boekNummer like ?"
                + " or titel like ?"
                + " or auteur like ?"
                + " or isbn like ?"
                + " order by boeknummer";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, filter);
            preparedStatement.setString(2, filter);
            preparedStatement.setString(3, filter);
            preparedStatement.setString(4, filter);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Boek boek = new Boek();
                boek.setBoek_ID(resultSet.getInt("Boek_ID"));
                boek.setBoekNummer(resultSet.getString("BoekNummer"));
                boek.setTitel(resultSet.getString("Titel"));
                boek.setAuteur(resultSet.getString("Auteur"));
                boek.setUitgeverij(resultSet.getString("Uitgeverij"));
                boek.setIsbn(resultSet.getString("ISBN"));
                boek.setLocatie(resultSet.getString("Locatie"));
                boek.setCategorie_ID(resultSet.getInt("Categorie_ID"));
                boek.setCategorieOmschrijving(resultSet.getString("Omschrijving"));

                result.add(boek);
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

    //*********FUNCTIES VOOR BOEKEDIT/TOEVOEGEN
    public String getMaxBoeknr() throws SQLException, NamingException {

        String sql = "select max(boeknummer) from boeken";
        String nummer = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                nummer = resultSet.getString(1);
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

        return nummer;
    }

    public Boek insertBoek(Boek boek) throws NamingException, SQLException {

        String sql = "insert into Boeken (BoekNummer, Titel, Auteur, Uitgeverij, ISBN, Locatie, Categorie_ID)"
                + "output inserted.boek_id"
                + " values (?,?,?,?,?,?,?)";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, boek.getBoekNummer());
            preparedStatement.setString(2, boek.getTitel());
            preparedStatement.setString(3, boek.getAuteur());
            preparedStatement.setString(4, boek.getUitgeverij());
            preparedStatement.setString(5, boek.getIsbn());
            preparedStatement.setString(6, boek.getLocatie());
            preparedStatement.setInt(7, boek.getCategorie_ID());

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                boek.setBoek_ID(resultSet.getInt(1));
            }

        } finally {
            if (preparedStatement != null && !preparedStatement.isClosed()) {
                preparedStatement.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }

        }
        return boek;

    }

    public void insertExemplaar(Integer boek_ID, Integer exemplaren) throws NamingException, SQLException {

        String sql = "insert into Exemplaren (Boek_ID, ExemplaarVolgnummer, DatumAanschaf, vermist)"
                + " values (?,?,?,?)";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        LocalDate datumAanschaf = LocalDate.now();

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int idx = 1; idx < (exemplaren + 1); idx++) {
                
                preparedStatement.setInt(1, boek_ID);
                preparedStatement.setInt(2, idx);
                preparedStatement.setDate(3, Date.valueOf(datumAanschaf));
                preparedStatement.setNull(4, java.sql.Types.NULL);

                preparedStatement.executeUpdate();
            }

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
