package org.example.databases;

import com.itextpdf.layout.element.Link;
import org.example.domain.Adduct;
import org.example.domain.FattyAcid;
import org.example.domain.Lipid;
import org.example.exceptions.FattyAcidCreation_Exception;
import org.example.exceptions.InvalidFormula_Exception;

import java.sql.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class Database {
    private static Connection connection = null;
    private final String url = "jdbc:mysql://localhost:3306/compounds";
    private final String username = "root";
    private final String password = "schoenstatt37";

    public Database() {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Set<FattyAcid> getFattyAcidsFromDatabase(double fattyAcidMass) throws SQLException, InvalidFormula_Exception, FattyAcidCreation_Exception {
        String query = "SELECT chain_id, num_carbons, double_bonds " +
                "FROM chains " +
                "WHERE mass <= " + fattyAcidMass + " + 0.01 " +
                "AND mass >= " + fattyAcidMass + " - 0.01 " +
                "AND oxidation = ''";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        Set<FattyAcid> fattyAcids = new LinkedHashSet<>();

        while (resultSet.next()) {
            int carbonAtoms = resultSet.getInt("num_carbons");
            int doubleBonds = resultSet.getInt("double_bonds");
            fattyAcids.add(new FattyAcid(carbonAtoms, doubleBonds));
        }
        return fattyAcids;
    }

    public Set<Lipid> getLipidsFromDatabase(double precursorIon) throws SQLException {
        String query = "SELECT DISTINCT compound_name" +
                "FROM compounds" +
                "INNER JOIN compound_chain ON compounds.compound_id = compound_chain.compound_id" +
                "INNER JOIN chains ON chains.chain_id = compound_chain.chain_id" +
                "WHERE compounds.formula = '" + null + "'" +
                "AND compounds.mass LIKE '582.%'" +
                "AND compounds.compound_name LIKE '%10:0%'" +
                "AND compounds.compound_name LIKE '%12:0%'";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        Set<Lipid> lipids = new LinkedHashSet<>();

        while (resultSet.next()) {
            int carbonAtoms = resultSet.getInt("num_carbons");
            int doubleBonds = resultSet.getInt("double_bonds");
            //lipids.add(new Lipid());
        }
        return lipids;
    }

    public Set<Double> calculateFattyAcidMasses(double precursorIon, Set<Double> fattyAcidMasses) throws SQLException, FattyAcidCreation_Exception {
        Set<Double> neutralLossAssociatedIonMasses = new LinkedHashSet<>();
        Iterator<Double> iterator = fattyAcidMasses.iterator();

        while (iterator.hasNext()) {
            neutralLossAssociatedIonMasses.add(precursorIon - iterator.next() - 17.02655d);
        }

        return neutralLossAssociatedIonMasses;
    }
}

