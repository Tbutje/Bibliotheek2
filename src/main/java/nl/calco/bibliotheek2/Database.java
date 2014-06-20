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
import java.sql.Types;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
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

    public Boek getBoek(Integer boek_ID) throws SQLException, NamingException {
        Boek boek = new Boek();
        String sql = "select * from boeken"
                + " left join Categorien"
                + " on Boeken.Categorie_ID = Categorien.Categorie_ID"
                + " where Boek_ID = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, boek_ID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                boek.setBoek_ID(resultSet.getInt("Boek_ID"));
                boek.setBoekNummer(resultSet.getString("BoekNummer"));
                boek.setTitel(resultSet.getString("Titel"));
                boek.setAuteur(resultSet.getString("Auteur"));
                boek.setUitgeverij(resultSet.getString("Uitgeverij"));
                boek.setIsbn(resultSet.getString("ISBN"));
                boek.setLocatie(resultSet.getString("Locatie"));
                boek.setCategorie_ID(resultSet.getInt("Categorie_ID"));
                boek.setCategorieOmschrijving(resultSet.getString("Omschrijving"));

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

        return boek;
    }

    public void updateBoek(Boek boek) throws NamingException, SQLException {
        //update Categorien set Omschrijving = ? where Categorie_ID = ?

        String sql = "update Boeken set BoekNummer = ?, Titel = ?, Auteur = ?, Uitgeverij = ?, ISBN = ?, Locatie = ?, Categorie_ID = ?"
                + " where Boek_ID = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

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
            preparedStatement.setInt(8, boek.getBoek_ID());

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

    //*EIND* FUNCTIES VOOR BOEKEDIT/TOEVOEGEN
//*********FUNCTIES VOOR EXEMPLAREN TOEVOEGEN**********************************
    public List<Exemplaar> getExemplaren(Integer boek_ID) throws SQLException, NamingException {
        List<Exemplaar> result = new ArrayList<>();
        String sql = "select * from Exemplaren where Boek_ID = ? order by ExemplaarVolgnummer";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, boek_ID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Exemplaar exemplaar = new Exemplaar();
                exemplaar.setExemplaar_ID(resultSet.getInt("Exemplaar_ID"));
                exemplaar.setBoek_ID(resultSet.getInt("Boek_ID"));
                exemplaar.setExemplaarVolgnummer(resultSet.getInt("ExemplaarVolgnummer"));
                exemplaar.setDatumAanschaf(
                        resultSet.getDate("DatumAanschaf").toLocalDate()
                );
                exemplaar.setVermist(resultSet.getBoolean("Vermist"));
        

                result.add(exemplaar);
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

    //overloaded voor als je nieuwe wil toevoegen en er zijn al bestaande
    // neemt wel aan dat je niet bestaande nummers toevoegd
    public void insertExemplaar(Integer boek_ID, Integer begin, Integer aantal) throws NamingException, SQLException {

        String sql = "insert into Exemplaren (Boek_ID, ExemplaarVolgnummer, DatumAanschaf, vermist)"
                + " values (?,?,?,?)";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        LocalDate datumAanschaf = LocalDate.now();

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int idx = begin; idx < (begin + aantal); idx++) {

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

    public void updateExemplaar(Exemplaar exemplaar) throws NamingException, SQLException {

        String sql = "update Exemplaren set Vermist = ? where Exemplaar_ID = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setBoolean(1, exemplaar.getVermist());
            preparedStatement.setInt(2, exemplaar.getExemplaar_ID());

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

    public Uitlening getHuidigeUitlening(Integer exemplaar_id) throws NamingException, SQLException {
        Uitlening uitlening = new Uitlening();
        String sql = "select * from Uitleningen where exemplaar_id = ?"
                + " and datuminleveren is null";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, exemplaar_id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                uitlening.setExemplaar_ID(resultSet.getInt("Exemplaar_ID"));
                uitlening.setMedewerker_ID(resultSet.getInt("Medewerker_ID"));
                uitlening.setDatumUitleen(resultSet.getDate("DatumUitleen").toLocalDate());
//                uitlening.setDatumInleveren(resultSet.getDate("DatumInleveren").toLocalDate());

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

        return uitlening;
    }

    public Medewerker getMedewerker(Integer medewerker_id) throws NamingException, SQLException {
        String sql = "select * from Medewerkers where Medewerker_ID = ?";
        Medewerker medewerker = new Medewerker();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, medewerker_id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                medewerker.setMedewerker_ID(resultSet.getInt("Medewerker_ID"));
                medewerker.setAchternaam(resultSet.getString("Achternaam"));
                medewerker.setVoornaam(resultSet.getString("Voornaam"));
                medewerker.setTussenVoegsel(resultSet.getString("Tussenvoegsel"));
                medewerker.setEmail(resultSet.getString("Email"));

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

        return medewerker;
    }

    public List<Medewerker> getMedewerkers() throws NamingException, SQLException {
        List<Medewerker> result = new ArrayList<>();
        String sql = "select * from Medewerkers";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Medewerker medewerker = new Medewerker();
                medewerker.setMedewerker_ID(resultSet.getInt("Medewerker_ID"));
                medewerker.setAchternaam(resultSet.getString("Achternaam"));
                medewerker.setVoornaam(resultSet.getString("Voornaam"));
                medewerker.setTussenVoegsel(resultSet.getString("Tussenvoegsel"));
                medewerker.setEmail(resultSet.getString("Email"));

                result.add(medewerker);
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

    public String getDatumuitleen(Integer exemplaar_id) throws NamingException, SQLException {
        String result = "";
        String sql = "select * from Uitleningen where exemplaar_id = ?"
                + " and datuminleveren is null";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, exemplaar_id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                result = resultSet.getString(1);
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
        if (result == null) {
            result = "";
        }

        return result;
    }

    public void insertUitlening(Uitlening uitlening) throws NamingException, SQLException {

        String sql = "insert into Uitleningen (Exemplaar_ID, Medewerker_ID, DatumUitleen, DatumInleveren)"
                + " values (?,?,?,?)";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, uitlening.getExemplaar_ID());
            preparedStatement.setInt(2, uitlening.getMedewerker_ID());
            preparedStatement.setDate(3, Date.valueOf(uitlening.getDatumUitleen()));
            preparedStatement.setNull(4, Types.DATE);

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

//*********FUNCTIES VOOR INNEMEN/UITNEMEN
    public void updateUitlening(Uitlening uitlening) throws NamingException, SQLException {

        String sql = "update Uitleningen set DatumInleveren = ?"
                + " where Exemplaar_ID = ? and Medewerker_ID = ? and DatumUitleen = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(2, uitlening.getExemplaar_ID());
            preparedStatement.setInt(3, uitlening.getMedewerker_ID());
            preparedStatement.setDate(4, Date.valueOf(uitlening.getDatumUitleen()));

            // vang hier af als datum == null
            if (uitlening.getDatumInleveren() == null) {
                preparedStatement.setNull(1, java.sql.Types.NULL);
            } else {
                preparedStatement.setDate(1, Date.valueOf(uitlening.getDatumInleveren()));
            }

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

    public void verwijderUitleningen(Integer exemplaar_id) throws NamingException, SQLException {

        String sql = "delete from Uitleningen where Exemplaar_ID = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, exemplaar_id);
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

    public void verwijderExemplaar(Exemplaar exemplaar) throws NamingException, SQLException {

        String sql = "delete from Exemplaren where Exemplaar_ID = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, exemplaar.getExemplaar_ID());
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

    //*********FUNCTIES VOOR INNEMEN/UITNEMEN
    public List<Uitlening> getUitleningen(Integer medewerker_ID) throws SQLException, NamingException {

        List<Uitlening> result = new ArrayList<>();
        String sql = "select * from Uitleningen where Medewerker_ID = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, medewerker_ID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Uitlening uitlening = new Uitlening();

                uitlening.setExemplaar_ID(resultSet.getInt("Exemplaar_ID"));
                uitlening.setMedewerker_ID(resultSet.getInt("Medewerker_ID"));
                uitlening.setDatumUitleen(resultSet.getDate("DatumUitleen").toLocalDate());

                // soms is de datum null dit willen we afvangen
                try {
                    uitlening.setDatumInleveren(resultSet.getDate("DatumInleveren").toLocalDate());
                } catch (Exception e) {
                    uitlening.setDatumInleveren(null);
                }

                result.add(uitlening);
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

    public Exemplaar getExemplaar(Integer exemplaar_ID) throws SQLException, NamingException {
        Exemplaar exemplaar = new Exemplaar();
        String sql = "select * from Exemplaren where Exemplaar_ID = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, exemplaar_ID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                exemplaar.setExemplaar_ID(resultSet.getInt("Exemplaar_ID"));
                exemplaar.setBoek_ID(resultSet.getInt("Boek_ID"));
                exemplaar.setExemplaarVolgnummer(resultSet.getInt("ExemplaarVolgnummer"));
                exemplaar.setDatumAanschaf(
                        resultSet.getDate("DatumAanschaf").toLocalDate());
                exemplaar.setVermist(resultSet.getBoolean("Vermist"));

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

        return exemplaar;
    }

    //*****Eind* FUNCTIES VOOR EXEMPLAREN TOEVOEGEN**********************************
    //*********FUNCTIES VOOR MEDEWERKERs TOEVOEGEN**********************************
    public void insertMedewerker(Medewerker medewerker) throws NamingException, SQLException {

        String sql = "insert into Medewerkers (Achternaam, Voornaam, Tussenvoegsel, Email)"
                + " values (?,?,?,?)";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, medewerker.getAchternaam());
            preparedStatement.setString(2, medewerker.getVoornaam());
            preparedStatement.setString(4, medewerker.getEmail());

            // check tussen == null
            if (medewerker.getTussenVoegsel() == null) {
                preparedStatement.setNull(3, Types.VARCHAR);
            } else {
                preparedStatement.setString(3, medewerker.getTussenVoegsel());
            }

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

    public void updateMedewerker(Medewerker medewerker) throws NamingException, SQLException {

        String sql = "update Medewerkers set Achternaam = ?, Voornaam = ?, Tussenvoegsel = ?, Email = ? where Medewerker_ID = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, medewerker.getAchternaam());
            preparedStatement.setString(2, medewerker.getVoornaam());
            preparedStatement.setString(4, medewerker.getEmail());
            preparedStatement.setInt(5, medewerker.getMedewerker_ID());

            // check tussen == null
            if (medewerker.getTussenVoegsel() == null) {
                preparedStatement.setNull(3, Types.VARCHAR);
            } else {
                preparedStatement.setString(3, medewerker.getTussenVoegsel());
            }

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
